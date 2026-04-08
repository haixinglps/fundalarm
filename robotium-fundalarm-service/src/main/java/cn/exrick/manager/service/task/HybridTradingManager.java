package cn.exrick.manager.service.task;

import cn.exrick.common.jedis.JedisClient;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 混合交易管理器：底仓网格 + 独立T
 * 
 * 资金分配（1000元）：
 * - 底仓网格：700元（70%），长期持有，分批止盈
 * - 独立T：300元（30%），日内交易，每日平仓
 * 
 * 核心原则：
 * 1. 两系统资金隔离，T盈亏不影响底仓
 * 2. T交易每日强制平仓，不留隔夜
 * 3. 底仓触发全平时，T系统同步清仓
 * 4. 总风险控制在3%以内（30元）
 */
@Component
public class HybridTradingManager {

    @Autowired
    private JedisClient jedisClient;

    // ========== 资金配置 ==========
    /** 总本金 */
    private static final BigDecimal TOTAL_CAPITAL = new BigDecimal("1000");
    /** 底仓最大占用 */
    private static final BigDecimal BASE_MAX = new BigDecimal("700");
    /** T交易最大占用 */
    private static final BigDecimal T_MAX = new BigDecimal("300");
    /** 日总亏损上限 */
    private static final BigDecimal DAILY_MAX_LOSS = new BigDecimal("-30"); // 3%

    // ========== 底仓网格参数 ==========
    /** 网格档位：3档 */
    private static final int GRID_LEVELS = 3;
    /** 每档金额：约233元 */
    private static final BigDecimal PER_GRID = BASE_MAX.divide(new BigDecimal("3"), 0, RoundingMode.DOWN);
    /** 网格间距：0.5% */
    private static final BigDecimal GRID_GAP = new BigDecimal("0.005");
    /** 分批止盈比例 */
    private static final BigDecimal[] TP_PCTS = {
        new BigDecimal("0.03"),   // 第一单3%
        new BigDecimal("0.06"),   // 第二单6%
        new BigDecimal("0.10")    // 第三单移动止盈
    };

    // ========== T交易参数 ==========
    /** T交易单位：100元/次 */
    private static final BigDecimal T_UNIT = new BigDecimal("100");
    /** T止盈：0.5%（赚0.5元） */
    private static final BigDecimal T_TP = new BigDecimal("0.005");
    /** T止损：0.3%（亏0.3元） */
    private static final BigDecimal T_SL = new BigDecimal("0.003");
    /** T冷却时间：5分钟 */
    private static final int T_COOLDOWN = 300;
    /** T日最大次数：5次 */
    private static final int T_MAX_TRADES = 5;

    // ========== Redis Keys ==========
    private static final String KEY_BASE_POS = "hybrid:base:{symbol}:{level}";
    private static final String KEY_T_STATE = "hybrid:t:{symbol}:{date}";
    private static final String KEY_T_POS = "hybrid:tpos:{symbol}";
    private static final String KEY_HYBRID_STATE = "hybrid:state:{symbol}:{date}";

    // ========== 数据结构 ==========
    
    /**
     * 底仓仓位
     */
    public static class BasePosition {
        public int level; // 1/2/3档
        public String entryPrice;
        public String positionValue; // 233元
        public String status; // holding/partial1/partial2/closed
        public long openTime;
        public String highestPrice; // 用于移动止盈
        
        public BasePosition(int level, BigDecimal entryPrice) {
            this.level = level;
            this.entryPrice = entryPrice.toString();
            this.positionValue = PER_GRID.toString();
            this.status = "holding";
            this.openTime = System.currentTimeMillis();
            this.highestPrice = entryPrice.toString();
        }
        
        public BigDecimal getEntryPrice() { return new BigDecimal(entryPrice); }
        public BigDecimal getPositionValue() { return new BigDecimal(positionValue); }
        public BigDecimal getHighestPrice() { return new BigDecimal(highestPrice); }
        public void setHighestPrice(BigDecimal p) { this.highestPrice = p.toString(); }
    }
    
    /**
     * T仓位
     */
    public static class TPosition {
        public String id;
        public String entryPrice;
        public String positionValue; // 100元
        public long openTime;
        public String status = "open";
        
        public TPosition(BigDecimal entryPrice) {
            this.id = "T" + System.currentTimeMillis() % 10000;
            this.entryPrice = entryPrice.toString();
            this.positionValue = T_UNIT.toString();
            this.openTime = System.currentTimeMillis();
        }
        
        public BigDecimal getEntryPrice() { return new BigDecimal(entryPrice); }
        public BigDecimal getPositionValue() { return new BigDecimal(positionValue); }
    }
    
    /**
     * 混合状态
     */
    public static class HybridState {
        public String basePnl = "0"; // 底仓累计盈亏
        public String tPnl = "0";    // T交易累计盈亏
        public String totalPnl = "0"; // 总盈亏
        public int tTradeCount = 0;
        public boolean tStopped = false;
        public boolean forceCloseAll = false; // 强制全平信号
        
        public BigDecimal getBasePnl() { return new BigDecimal(basePnl); }
        public BigDecimal getTPnl() { return new BigDecimal(tPnl); }
        public BigDecimal getTotalPnl() { return new BigDecimal(totalPnl); }
        
        public void updateTotal() {
            this.totalPnl = getBasePnl().add(getTPnl()).toString();
        }
    }

    // ========== 底仓网格逻辑 ==========
    
    /**
     * 检查底仓是否需要建仓
     * @return 建议建仓档位（0表示不建）
     */
    public int checkBaseEntry(String symbol, BigDecimal currentPrice, BigDecimal avgCost) {
        // 已有3档满，不建
        int existingLevels = countBaseLevels(symbol);
        if (existingLevels >= GRID_LEVELS) return 0;
        
        // 第一档：直接建（如果底仓为空）
        if (existingLevels == 0) return 1;
        
        // 第二、三档：价格下跌0.5%以上才建
        BasePosition lastPos = getLastBasePosition(symbol);
        if (lastPos == null) return 1;
        
        BigDecimal lastPrice = lastPos.getEntryPrice();
        BigDecimal drop = lastPrice.subtract(currentPrice).divide(lastPrice, 6, RoundingMode.HALF_UP);
        
        if (drop.compareTo(GRID_GAP) >= 0) {
            return existingLevels + 1;
        }
        
        return 0;
    }
    
    /**
     * 建立底仓
     */
    public BasePosition openBasePosition(String symbol, int level, BigDecimal price) {
        BasePosition pos = new BasePosition(level, price);
        String key = KEY_BASE_POS.replace("{symbol}", symbol).replace("{level}", String.valueOf(level));
        jedisClient.set(key, JSONUtil.toJsonStr(pos));
        
        System.out.println("【底仓建仓-" + level + "档】" + symbol + " @" + price + " 金额=" + PER_GRID + "元");
        return pos;
    }
    
    /**
     * 检查底仓止盈
     * @return 需要止盈的档位列表
     */
    public List<BaseExitSignal> checkBaseTakeProfit(String symbol, BigDecimal currentPrice) {
        List<BaseExitSignal> signals = new ArrayList<>();
        
        for (int level = 1; level <= GRID_LEVELS; level++) {
            BasePosition pos = getBasePosition(symbol, level);
            if (pos == null || "closed".equals(pos.status)) continue;
            
            BigDecimal entryPrice = pos.getEntryPrice();
            BigDecimal profitRate = currentPrice.subtract(entryPrice).divide(entryPrice, 6, RoundingMode.HALF_UP);
            
            // 更新最高价
            if (currentPrice.compareTo(pos.getHighestPrice()) > 0) {
                pos.setHighestPrice(currentPrice);
                saveBasePosition(symbol, pos);
            }
            
            // 分批止盈判断
            if ("holding".equals(pos.status) && profitRate.compareTo(TP_PCTS[0]) >= 0) {
                signals.add(new BaseExitSignal(level, "TP1", profitRate, "第一档止盈3%"));
                pos.status = "partial1";
                saveBasePosition(symbol, pos);
            } else if ("partial1".equals(pos.status) && profitRate.compareTo(TP_PCTS[1]) >= 0) {
                signals.add(new BaseExitSignal(level, "TP2", profitRate, "第二档止盈6%"));
                pos.status = "partial2";
                saveBasePosition(symbol, pos);
            } else if ("partial2".equals(pos.status)) {
                // 移动止盈：从最高点回撤1%
                BigDecimal maxPrice = pos.getHighestPrice();
                BigDecimal pullback = maxPrice.subtract(currentPrice).divide(maxPrice, 6, RoundingMode.HALF_UP);
                if (pullback.compareTo(new BigDecimal("0.01")) >= 0) {
                    signals.add(new BaseExitSignal(level, "TRAILING", profitRate, 
                            "第三档移动止盈 最高" + maxPrice + " 回撤" + pullback.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%"));
                    pos.status = "closed";
                    saveBasePosition(symbol, pos);
                }
            }
        }
        
        return signals;
    }

    // ========== T交易逻辑 ==========
    
    /**
     * 检查T交易入场
     */
    public TCanTradeResult checkTEntry(String symbol, BigDecimal currentPrice) {
        TCanTradeResult result = new TCanTradeResult();
        HybridState state = getHybridState(symbol);
        
        // 检查总亏损
        if (state.getTotalPnl().compareTo(DAILY_MAX_LOSS) <= 0) {
            result.allowed = false;
            result.reason = "总亏损达上限" + DAILY_MAX_LOSS + "元";
            return result;
        }
        
        // T交易独立风控
        if (state.tStopped || state.tTradeCount >= T_MAX_TRADES) {
            result.allowed = false;
            result.reason = "T交易已达上限";
            return result;
        }
        
        // 检查T持仓
        List<TPosition> tPositions = getTPositions(symbol);
        if (!tPositions.isEmpty()) {
            // 已有T仓位，检查间距
            TPosition last = tPositions.get(tPositions.size() - 1);
            BigDecimal gap = currentPrice.subtract(last.getEntryPrice())
                    .divide(last.getEntryPrice(), 6, RoundingMode.HALF_UP).abs();
            if (gap.compareTo(new BigDecimal("0.003")) < 0) { // T交易间距0.3%
                result.allowed = false;
                result.reason = "T间距不足";
                return result;
            }
        }
        
        // 检查T资金
        BigDecimal usedT = tPositions.stream()
                .map(TPosition::getPositionValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (usedT.add(T_UNIT).compareTo(T_MAX) > 0) {
            result.allowed = false;
            result.reason = "T资金已满";
            return result;
        }
        
        result.allowed = true;
        result.state = state;
        return result;
    }
    
    /**
     * 开T仓
     */
    public TPosition openTPosition(String symbol, BigDecimal price) {
        TPosition pos = new TPosition(price);
        List<TPosition> positions = getTPositions(symbol);
        positions.add(pos);
        saveTPositions(symbol, positions);
        
        HybridState state = getHybridState(symbol);
        state.tTradeCount++;
        saveHybridState(symbol, state);
        
        System.out.println("【T开仓】" + symbol + " " + pos.id + " @" + price + " 金额=100元");
        return pos;
    }
    
    /**
     * 检查T平仓
     */
    public List<TExitSignal> checkTExit(String symbol, BigDecimal currentPrice) {
        List<TExitSignal> signals = new ArrayList<>();
        List<TPosition> positions = getTPositions(symbol);
        
        for (TPosition pos : positions) {
            if (!"open".equals(pos.status)) continue;
            
            BigDecimal entry = pos.getEntryPrice();
            BigDecimal profitRate = currentPrice.subtract(entry).divide(entry, 6, RoundingMode.HALF_UP);
            BigDecimal profitAmount = profitRate.multiply(pos.getPositionValue()); // 盈亏金额
            
            // 止盈0.5%
            if (profitRate.compareTo(T_TP) >= 0) {
                signals.add(new TExitSignal(pos.id, "TP", currentPrice, profitAmount, "T止盈"));
                pos.status = "closed";
            }
            // 止损0.3%
            else if (profitRate.compareTo(T_SL.negate()) <= 0) {
                signals.add(new TExitSignal(pos.id, "SL", currentPrice, profitAmount, "T止损"));
                pos.status = "closed";
            }
            // 超时强平（持仓>30分钟）
            else if (System.currentTimeMillis() - pos.openTime > 30 * 60 * 1000) {
                signals.add(new TExitSignal(pos.id, "TIMEOUT", currentPrice, profitAmount, "T超时平仓"));
                pos.status = "closed";
            }
        }
        
        saveTPositions(symbol, positions);
        return signals;
    }
    
    /**
     * 平T仓
     */
    public void closeTPosition(String symbol, TExitSignal signal) {
        List<TPosition> positions = getTPositions(symbol);
        TPosition target = positions.stream()
                .filter(p -> p.id.equals(signal.positionId))
                .findFirst().orElse(null);
        
        if (target == null) return;
        
        BigDecimal fee = target.getPositionValue().multiply(new BigDecimal("0.002"));
        BigDecimal netPnl = signal.profitAmount.subtract(fee);
        
        HybridState state = getHybridState(symbol);
        state.tPnl = state.getTPnl().add(netPnl).toString();
        state.updateTotal();
        
        // 检查T连续亏损
        if (netPnl.compareTo(BigDecimal.ZERO) < 0) {
            // 简单风控：T累计亏损达-5元，停止当日T交易
            if (state.getTPnl().compareTo(new BigDecimal("-5")) < 0) {
                state.tStopped = true;
            }
        }
        
        saveHybridState(symbol, state);
        positions.remove(target);
        saveTPositions(symbol, positions);
        
        System.out.println("【T平仓】" + symbol + " " + target.id + " " + signal.type 
                + " 盈亏=" + netPnl.setScale(2, RoundingMode.HALF_UP) + "元 T累计=" + state.getTPnl() + "元");
    }
    
    /**
     * 收盘前强制平所有T
     */
    public void forceCloseAllT(String symbol, BigDecimal currentPrice) {
        List<TPosition> positions = getTPositions(symbol);
        HybridState state = getHybridState(symbol);
        
        BigDecimal totalTProfit = BigDecimal.ZERO;
        for (TPosition pos : positions) {
            if ("open".equals(pos.status)) {
                BigDecimal profitRate = currentPrice.subtract(pos.getEntryPrice())
                        .divide(pos.getEntryPrice(), 6, RoundingMode.HALF_UP);
                BigDecimal profitAmount = profitRate.multiply(pos.getPositionValue());
                BigDecimal fee = pos.getPositionValue().multiply(new BigDecimal("0.002"));
                totalTProfit = totalTProfit.add(profitAmount.subtract(fee));
            }
        }
        
        state.tPnl = state.getTPnl().add(totalTProfit).toString();
        state.updateTotal();
        saveHybridState(symbol, state);
        
        jedisClient.del(KEY_T_POS.replace("{symbol}", symbol));
        System.out.println("【T收盘清仓】" + symbol + " 当日T总盈亏=" + state.getTPnl() + "元");
    }

    // ========== 总控逻辑 ==========
    
    /**
     * 获取每日报告
     */
    public String getDailyReport(String symbol) {
        HybridState state = getHybridState(symbol);
        int baseLevels = countBaseLevels(symbol);
        List<TPosition> tPositions = getTPositions(symbol);
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== ").append(symbol).append(" 混合交易日报 ==========\n");
        sb.append("总盈亏: ").append(state.getTotalPnl()).append("元\n");
        sb.append("  ├─ 底仓盈亏: ").append(state.getBasePnl()).append("元 (持仓").append(baseLevels).append("档)\n");
        sb.append("  └─ T交易盈亏: ").append(state.getTPnl()).append("元 (交易").append(state.tTradeCount).append("次)\n");
        sb.append("T持仓: ").append(tPositions.size()).append("单 ");
        BigDecimal tUsed = tPositions.stream().map(TPosition::getPositionValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        sb.append("(占用").append(tUsed).append("/").append(T_MAX).append("元)\n");
        sb.append("状态: ").append(state.tStopped ? "T已停止" : "交易中").append("\n");
        sb.append("============================================\n");
        
        return sb.toString();
    }

    // ========== Redis操作 ==========
    
    private BasePosition getBasePosition(String symbol, int level) {
        String key = KEY_BASE_POS.replace("{symbol}", symbol).replace("{level}", String.valueOf(level));
        String json = jedisClient.get(key);
        if (StrUtil.isBlank(json)) return null;
        return JSONUtil.toBean(json, BasePosition.class);
    }
    
    private void saveBasePosition(String symbol, BasePosition pos) {
        String key = KEY_BASE_POS.replace("{symbol}", symbol).replace("{level}", String.valueOf(pos.level));
        jedisClient.set(key, JSONUtil.toJsonStr(pos));
    }
    
    private BasePosition getLastBasePosition(String symbol) {
        for (int i = GRID_LEVELS; i >= 1; i--) {
            BasePosition pos = getBasePosition(symbol, i);
            if (pos != null) return pos;
        }
        return null;
    }
    
    private int countBaseLevels(String symbol) {
        int count = 0;
        for (int i = 1; i <= GRID_LEVELS; i++) {
            if (getBasePosition(symbol, i) != null) count++;
        }
        return count;
    }
    
    private List<TPosition> getTPositions(String symbol) {
        String key = KEY_T_POS.replace("{symbol}", symbol);
        String json = jedisClient.get(key);
        if (StrUtil.isBlank(json)) return new ArrayList<>();
        return JSONUtil.toList(JSONUtil.parseArray(json), TPosition.class);
    }
    
    private void saveTPositions(String symbol, List<TPosition> positions) {
        String key = KEY_T_POS.replace("{symbol}", symbol);
        jedisClient.set(key, JSONUtil.toJsonStr(positions));
    }
    
    private HybridState getHybridState(String symbol) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = KEY_HYBRID_STATE.replace("{symbol}", symbol).replace("{date}", date);
        String json = jedisClient.get(key);
        if (StrUtil.isBlank(json)) return new HybridState();
        return JSONUtil.toBean(json, HybridState.class);
    }
    
    private void saveHybridState(String symbol, HybridState state) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = KEY_HYBRID_STATE.replace("{symbol}", symbol).replace("{date}", date);
        jedisClient.set(key, JSONUtil.toJsonStr(state));
        jedisClient.expire(key, 86400);
    }

    // ========== 数据结构 ==========
    
    public static class BaseExitSignal {
        public int level;
        public String type;
        public BigDecimal profitRate;
        public String reason;
        
        public BaseExitSignal(int level, String type, BigDecimal profitRate, String reason) {
            this.level = level;
            this.type = type;
            this.profitRate = profitRate;
            this.reason = reason;
        }
    }
    
    public static class TCanTradeResult {
        public boolean allowed;
        public String reason;
        public HybridState state;
    }
    
    public static class TExitSignal {
        public String positionId;
        public String type;
        public BigDecimal price;
        public BigDecimal profitAmount;
        public String reason;
        
        public TExitSignal(String positionId, String type, BigDecimal price, 
                BigDecimal profitAmount, String reason) {
            this.positionId = positionId;
            this.type = type;
            this.price = price;
            this.profitAmount = profitAmount;
            this.reason = reason;
        }
    }
}
