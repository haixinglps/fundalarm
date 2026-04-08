package cn.exrick.manager.service.util;

import cn.exrick.common.jedis.JedisClient;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 每档独立风控系统
 * 
 * 核心设计：
 * 1. 每档网格独立设置止盈止损（不同档位不同策略）
 * 2. 做T仓位每单必带止盈止损（OCO订单）
 * 3. 底仓用移动止盈，做T用固定止盈止损
 * 4. 档位止盈价 = 上档买入价（网格天然止盈）
 * 5. 档位止损价 = 下下档买入价（留缓冲）
 */
@Slf4j
@Component
public class PerGridRiskControl {

    @Autowired
    private JedisClient jedisClient;
    
    // ==================== 档位风控参数 ====================
    
    /** 底仓档位止损：跌破下2档止损 */
    private static final int BASE_STOP_GRID_DISTANCE = 2;
    /** 底仓档位止盈：涨至上1档止盈 */
    private static final int BASE_TP_GRID_DISTANCE = 1;
    /** 做T档位止损：固定-0.5% */
    private static final BigDecimal T_STOP_PCT = new BigDecimal("0.005");
    /** 做T档位止盈：固定+0.3% */
    private static final BigDecimal T_TP_PCT = new BigDecimal("0.003");
    /** 做T时间止损：30分钟 */
    private static final int T_TIME_STOP_MINUTES = 30;
    
    // ==================== 核心风控方法 ====================
    
    /**
     * 初始化档位风控参数（每档独立）
     * 
     * @param symbol 交易对
     * @param gridLevels 所有档位价格
     * @param centerIndex 中心档位索引
     */
    public void initGridRisk(String symbol, List<GridLevelInfo> gridLevels, int centerIndex) {
        for (GridLevelInfo level : gridLevels) {
            int idx = level.getIndex();
            
            // 计算该档位的止损价（下2档的价格）
            BigDecimal stopLossPrice = calculateStopPrice(gridLevels, idx, BASE_STOP_GRID_DISTANCE);
            
            // 计算该档位的止盈价（上1档的价格）
            BigDecimal takeProfitPrice = calculateTpPrice(gridLevels, idx, BASE_TP_GRID_DISTANCE);
            
            // 保存该档位的风控参数
            GridRiskConfig config = GridRiskConfig.builder()
                .symbol(symbol)
                .levelIndex(idx)
                .buyPrice(level.getBuyPrice())
                .sellPrice(level.getSellPrice())
                .stopLossPrice(stopLossPrice)
                .takeProfitPrice(takeProfitPrice)
                .maxHoldHours(48) // 底仓最长持有48小时
                .build();
            
            saveGridRiskConfig(symbol, idx, config);
        }
        
        log.info("【档位风控初始化】{} 共{}档，每档独立止盈止损已设置", symbol, gridLevels.size());
    }
    
    /**
     * 检查底仓档位是否需要止盈止损
     * 
     * @param symbol 交易对
     * @param levelIdx 档位索引
     * @param entryPrice 实际买入价
     * @param currentPrice 当前价格
     * @param holdHours 持仓小时数
     * @return 风控建议
     */
    public GridRiskAdvice checkBaseGridRisk(String symbol, int levelIdx, BigDecimal entryPrice,
                                             BigDecimal currentPrice, int holdHours) {
        GridRiskConfig config = getGridRiskConfig(symbol, levelIdx);
        if (config == null) {
            return GridRiskAdvice.hold("无风控配置");
        }
        
        // 1. 止盈检查：涨至上档价格
        if (currentPrice.compareTo(config.getTakeProfitPrice()) >= 0) {
            return GridRiskAdvice.builder()
                .action(GridRiskAction.TAKE_PROFIT)
                .symbol(symbol)
                .levelIndex(levelIdx)
                .price(currentPrice)
                .reason("触及档位止盈价 " + config.getTakeProfitPrice())
                .build();
        }
        
        // 2. 止损检查：跌破下2档价格
        if (currentPrice.compareTo(config.getStopLossPrice()) <= 0) {
            return GridRiskAdvice.builder()
                .action(GridRiskAction.STOP_LOSS)
                .symbol(symbol)
                .levelIndex(levelIdx)
                .price(currentPrice)
                .reason("触及档位止损价 " + config.getStopLossPrice())
                .build();
        }
        
        // 3. 时间检查：持仓超48小时
        if (holdHours >= config.getMaxHoldHours()) {
            return GridRiskAdvice.builder()
                .action(GridRiskAction.TIME_EXIT)
                .symbol(symbol)
                .levelIndex(levelIdx)
                .price(currentPrice)
                .reason("持仓超" + config.getMaxHoldHours() + "小时")
                .build();
        }
        
        // 4. 移动止盈：从最高点回撤3%
        BigDecimal highestPrice = getGridLevelHighestPrice(symbol, levelIdx);
        if (highestPrice != null && highestPrice.compareTo(entryPrice) > 0) {
            BigDecimal trailingStop = highestPrice.multiply(new BigDecimal("0.97"));
            if (currentPrice.compareTo(trailingStop) <= 0) {
                return GridRiskAdvice.builder()
                    .action(GridRiskAction.TRAILING_STOP)
                    .symbol(symbol)
                    .levelIndex(levelIdx)
                    .price(currentPrice)
                    .reason("从高点" + highestPrice + "回撤3%")
                    .build();
            }
        }
        
        return GridRiskAdvice.hold("持仓中");
    }
    
    /**
     * 做T仓位风控（每单必带OCO）
     * 
     * @param symbol 交易对
     * @param tId 做T单ID
     * @param entryPrice 入场价
     * @param currentPrice 当前价
     * @param holdMinutes 持仓分钟
     * @return 风控建议
     */
    public TRiskAdvice checkTPositionRisk(String symbol, String tId, BigDecimal entryPrice,
                                          BigDecimal currentPrice, int holdMinutes) {
        TPositionRisk risk = getTPositionRisk(tId);
        if (risk == null) {
            // 新做单，初始化风控参数
            risk = initTPositionRisk(symbol, tId, entryPrice);
        }
        
        // 计算盈亏
        BigDecimal pnlPct = currentPrice.subtract(entryPrice).divide(entryPrice, 6, RoundingMode.DOWN);
        
        // 1. 时间止损（最优先，防被套）
        if (holdMinutes >= T_TIME_STOP_MINUTES) {
            return TRiskAdvice.builder()
                .action(TRiskAction.CLOSE)
                .symbol(symbol)
                .tId(tId)
                .price(currentPrice)
                .closeType(TCloseType.TIME_STOP)
                .reason("做T时间到" + T_TIME_STOP_MINUTES + "分钟")
                .pnlPct(pnlPct)
                .build();
        }
        
        // 2. 固定止损 -0.5%
        if (pnlPct.compareTo(T_STOP_PCT.negate()) <= 0) {
            return TRiskAdvice.builder()
                .action(TRiskAction.CLOSE)
                .symbol(symbol)
                .tId(tId)
                .price(currentPrice)
                .closeType(TCloseType.STOP_LOSS)
                .reason("做T止损-0.5%")
                .pnlPct(pnlPct)
                .build();
        }
        
        // 3. 固定止盈 +0.3%
        if (pnlPct.compareTo(T_TP_PCT) >= 0) {
            return TRiskAdvice.builder()
                .action(TRiskAction.CLOSE)
                .symbol(symbol)
                .tId(tId)
                .price(currentPrice)
                .closeType(TCloseType.TAKE_PROFIT)
                .reason("做T止盈+0.3%")
                .pnlPct(pnlPct)
                .build();
        }
        
        // 4. 盈亏平衡保本（至少不亏）
        BigDecimal breakeven = entryPrice.multiply(new BigDecimal("1.001"));
        if (currentPrice.compareTo(breakeven) >= 0 && holdMinutes > 5) {
            return TRiskAdvice.builder()
                .action(TRiskAction.CLOSE)
                .symbol(symbol)
                .tId(tId)
                .price(currentPrice)
                .closeType(TCloseType.BREAKEVEN)
                .reason("做T盈亏平衡保本")
                .pnlPct(pnlPct)
                .build();
        }
        
        // 更新最高价（用于移动止盈）
        if (currentPrice.compareTo(risk.getHighestPrice()) > 0) {
            risk.setHighestPrice(currentPrice);
            saveTPositionRisk(risk);
        }
        
        return TRiskAdvice.hold();
    }
    
    /**
     * 挂OCO订单（同时挂止盈止损，一个成交另一个自动取消）
     * 
     * @param symbol 交易对
     * @param amount 数量
     * @param tpPrice 止盈价
     * @param slPrice 止损价
     * @return OCO订单ID
     */
    public String placeOCOOrder(String symbol, BigDecimal amount, BigDecimal tpPrice, BigDecimal slPrice) {
        // 实际调用交易所API挂OCO单
        String ocoId = "OCO_" + symbol + "_" + System.currentTimeMillis();
        
        log.info("【OCO挂单】{} 量{} 止盈{} 止损{}", symbol, amount, tpPrice, slPrice);
        
        // 保存OCO记录
        OCOOrder oco = OCOOrder.builder()
            .ocoId(ocoId)
            .symbol(symbol)
            .amount(amount)
            .tpPrice(tpPrice)
            .slPrice(slPrice)
            .status(0) // 0:挂单中
            .build();
        saveOCOOrder(oco);
        
        return ocoId;
    }
    
    /**
     * 更新档位最高价（用于移动止盈）
     */
    public void updateGridLevelHighest(String symbol, int levelIdx, BigDecimal price) {
        String key = String.format("grid:high:%s:%d", symbol, levelIdx);
        String current = jedisClient.get(key);
        
        BigDecimal highest = current == null ? price : new BigDecimal(current);
        if (price.compareTo(highest) > 0) {
            jedisClient.setex(key, 86400, price.toString());
        }
    }
    
    /**
     * 获取档位风控摘要
     */
    public GridRiskSummary getGridRiskSummary(String symbol) {
        GridRiskSummary summary = new GridRiskSummary();
        summary.setSymbol(symbol);
        
        // 统计各档位情况
        List<GridLevelRisk> levels = new ArrayList<>();
        for (int i = -10; i <= 10; i++) {
            GridRiskConfig config = getGridRiskConfig(symbol, i);
            if (config == null) continue;
            
            GridLevelRisk level = new GridLevelRisk();
            level.setIndex(i);
            level.setBuyPrice(config.getBuyPrice());
            level.setSellPrice(config.getSellPrice());
            level.setStopLossPrice(config.getStopLossPrice());
            level.setTakeProfitPrice(config.getTakeProfitPrice());
            
            levels.add(level);
        }
        
        summary.setLevels(levels);
        return summary;
    }
    
    // ==================== 私有计算工具 ====================
    
    private BigDecimal calculateStopPrice(List<GridLevelInfo> grids, int currentIdx, int distance) {
        int stopIdx = currentIdx - distance;
        if (stopIdx < 0) {
            // 超出范围，用固定百分比
            return grids.get(currentIdx).getBuyPrice().multiply(new BigDecimal("0.95"));
        }
        return grids.get(stopIdx).getBuyPrice();
    }
    
    private BigDecimal calculateTpPrice(List<GridLevelInfo> grids, int currentIdx, int distance) {
        int tpIdx = currentIdx + distance;
        if (tpIdx >= grids.size()) {
            return grids.get(currentIdx).getBuyPrice().multiply(new BigDecimal("1.05"));
        }
        return grids.get(tpIdx).getBuyPrice();
    }
    
    private TPositionRisk initTPositionRisk(String symbol, String tId, BigDecimal entryPrice) {
        TPositionRisk risk = TPositionRisk.builder()
            .tId(tId)
            .symbol(symbol)
            .entryPrice(entryPrice)
            .tpPrice(entryPrice.multiply(BigDecimal.ONE.add(T_TP_PCT)))
            .slPrice(entryPrice.multiply(BigDecimal.ONE.subtract(T_STOP_PCT)))
            .breakevenPrice(entryPrice.multiply(new BigDecimal("1.001")))
            .highestPrice(entryPrice)
            .openTime(System.currentTimeMillis())
            .build();
        
        saveTPositionRisk(risk);
        return risk;
    }
    
    // ==================== Redis操作（简化）====================
    
    private void saveGridRiskConfig(String symbol, int idx, GridRiskConfig config) {
        String key = String.format("grid:risk:%s:%d", symbol, idx);
        jedisClient.setex(key, 86400 * 7, serialize(config));
    }
    
    private GridRiskConfig getGridRiskConfig(String symbol, int idx) {
        String key = String.format("grid:risk:%s:%d", symbol, idx);
        String data = jedisClient.get(key);
        return data == null ? null : deserialize(data, GridRiskConfig.class);
    }
    
    private void saveTPositionRisk(TPositionRisk risk) {
        String key = "t:risk:" + risk.getTId();
        jedisClient.setex(key, 86400, serialize(risk));
    }
    
    private TPositionRisk getTPositionRisk(String tId) {
        String key = "t:risk:" + tId;
        String data = jedisClient.get(key);
        return data == null ? null : deserialize(data, TPositionRisk.class);
    }
    
    private void saveOCOOrder(OCOOrder oco) {
        String key = "oco:" + oco.getOcoId();
        jedisClient.setex(key, 86400, serialize(oco));
    }
    
    private BigDecimal getGridLevelHighestPrice(String symbol, int idx) {
        String key = String.format("grid:high:%s:%d", symbol, idx);
        String val = jedisClient.get(key);
        return val == null ? null : new BigDecimal(val);
    }
    
    private String serialize(Object obj) {
        return new cn.hutool.json.JSONObject(obj).toString();
    }
    
    private <T> T deserialize(String data, Class<T> clazz) {
        return new cn.hutool.json.JSONObject(data).toBean(clazz);
    }
    
    // ==================== 数据类 ====================
    
    @Data
    @Builder
    public static class GridRiskConfig {
        private String symbol;
        private int levelIndex;
        private BigDecimal buyPrice;
        private BigDecimal sellPrice;
        private BigDecimal stopLossPrice;    // 止损价
        private BigDecimal takeProfitPrice;  // 止盈价
        private int maxHoldHours;            // 最大持仓时间
    }
    
    @Data
    @Builder
    public static class GridRiskAdvice {
        private GridRiskAction action;
        private String symbol;
        private int levelIndex;
        private BigDecimal price;
        private String reason;
        
        public static GridRiskAdvice hold(String reason) {
            return GridRiskAdvice.builder().action(GridRiskAction.HOLD).reason(reason).build();
        }
    }
    
    @Data
    @Builder
    public static class TRiskAdvice {
        private TRiskAction action;
        private String symbol;
        private String tId;
        private BigDecimal price;
        private TCloseType closeType;
        private String reason;
        private BigDecimal pnlPct;
        
        public static TRiskAdvice hold() {
            return TRiskAdvice.builder().action(TRiskAction.HOLD).build();
        }
    }
    
    @Data
    @Builder
    public static class TPositionRisk {
        private String tId;
        private String symbol;
        private BigDecimal entryPrice;
        private BigDecimal tpPrice;        // 止盈价
        private BigDecimal slPrice;        // 止损价
        private BigDecimal breakevenPrice; // 盈亏平衡价
        private BigDecimal highestPrice;   // 最高价（移动止盈用）
        private long openTime;
    }
    
    @Data
    @Builder
    public static class OCOOrder {
        private String ocoId;
        private String symbol;
        private BigDecimal amount;
        private BigDecimal tpPrice;
        private BigDecimal slPrice;
        private int status; // 0:挂单中 1:止盈成交 2:止损成交
    }
    
    @Data
    public static class GridLevelInfo {
        private int index;
        private BigDecimal buyPrice;
        private BigDecimal sellPrice;
    }
    
    @Data
    public static class GridRiskSummary {
        private String symbol;
        private List<GridLevelRisk> levels;
    }
    
    @Data
    public static class GridLevelRisk {
        private int index;
        private BigDecimal buyPrice;
        private BigDecimal sellPrice;
        private BigDecimal stopLossPrice;
        private BigDecimal takeProfitPrice;
    }
    
    public enum GridRiskAction {
        HOLD,           // 持有
        TAKE_PROFIT,    // 档位止盈
        STOP_LOSS,      // 档位止损
        TIME_EXIT,      // 时间出场
        TRAILING_STOP   // 移动止盈
    }
    
    public enum TRiskAction {
        HOLD,   // 持有
        CLOSE   // 平仓
    }
    
    public enum TCloseType {
        TAKE_PROFIT, // +0.3%
        STOP_LOSS,   // -0.5%
        TIME_STOP,   // 30分钟
        BREAKEVEN    // 保本
    }
}
