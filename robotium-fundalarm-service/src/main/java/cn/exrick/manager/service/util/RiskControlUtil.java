package cn.exrick.manager.service.util;

import cn.exrick.common.jedis.JedisClient;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 止盈止损控制工具
 * 
 * 核心功能：
 * 1. 移动止盈跟踪
 * 2. 分级止盈
 * 3. 硬止损控制
 * 4. 做T严格止损
 * 5. 每日风险限额
 */
@Slf4j
@Component
public class RiskControlUtil {

    @Autowired
    private JedisClient jedisClient;
    
    // ==================== 风险参数配置 ====================
    
    /** 底仓初始止损 */
    private static final BigDecimal BASE_STOP_LOSS = new BigDecimal("0.02");  // -2%
    /** 底仓最大止损 */
    private static final BigDecimal BASE_MAX_LOSS = new BigDecimal("0.05");   // -5%
    /** 做T止损 */
    private static final BigDecimal T_STOP_LOSS = new BigDecimal("0.005");    // -0.5%
    /** 移动止盈回撤 */
    private static final BigDecimal TRAILING_STOP = new BigDecimal("0.03");   // 回撤3%止盈
    /** 做T时间限制（分钟） */
    private static final int T_MAX_HOLD_MINUTES = 120;
    /** 每日最大做T亏损 */
    private static final BigDecimal MAX_DAILY_T_LOSS = new BigDecimal("0.01"); // 本金的1%
    
    // ==================== 核心方法 ====================
    
    /**
     * 检查底仓是否需要止盈止损
     * 
     * @param symbol 交易对
     * @param currentPrice 当前价格
     * @param avgCost 持仓成本
     * @param highestPrice 历史最高价（用于移动止盈）
     * @param positionAmount 持仓数量
     * @return 风控建议
     */
    public RiskAdvice checkBasePosition(String symbol, BigDecimal currentPrice, 
                                        BigDecimal avgCost, BigDecimal highestPrice,
                                        BigDecimal positionAmount) {
        if (avgCost.compareTo(BigDecimal.ZERO) <= 0 || positionAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return RiskAdvice.hold("无持仓");
        }
        
        // 计算盈亏比例
        BigDecimal pnlPct = currentPrice.subtract(avgCost)
            .divide(avgCost, 6, RoundingMode.DOWN);
        
        RiskAdvice.RiskAdviceBuilder advice = RiskAdvice.builder()
            .symbol(symbol)
            .currentPrice(currentPrice)
            .avgCost(avgCost)
            .pnlPercent(pnlPct)
            .positionAmount(positionAmount);
        
        // ========== 1. 止损检查（优先）==========
        if (pnlPct.compareTo(BASE_STOP_LOSS.negate()) <= 0) {
            // 跌破2%：减仓50%
            advice.action(RiskAction.REDUCE_HALF)
                   .reduceAmount(positionAmount.multiply(new BigDecimal("0.5")))
                   .stopLossPrice(avgCost.multiply(BigDecimal.ONE.subtract(BASE_STOP_LOSS)))
                   .reason(String.format("触发初始止损 %.2f%%", pnlPct.multiply(new BigDecimal("100"))));
            return advice.build();
        }
        
        if (pnlPct.compareTo(BASE_MAX_LOSS.negate()) <= 0) {
            // 跌破5%：全部清仓
            advice.action(RiskAction.STOP_LOSS_ALL)
                   .reduceAmount(positionAmount)
                   .stopLossPrice(avgCost.multiply(BigDecimal.ONE.subtract(BASE_MAX_LOSS)))
                   .reason(String.format("触发最大止损 %.2f%%", pnlPct.multiply(new BigDecimal("100"))));
            return advice.build();
        }
        
        // ========== 2. 移动止盈检查 ==========
        if (highestPrice != null && highestPrice.compareTo(avgCost) > 0) {
            BigDecimal drawdownFromHigh = currentPrice.subtract(highestPrice)
                .divide(highestPrice, 6, RoundingMode.DOWN);
            
            // 计算动态止盈价
            BigDecimal trailingStopPrice = calculateTrailingStopPrice(symbol, highestPrice);
            
            if (currentPrice.compareTo(trailingStopPrice) <= 0) {
                // 触发移动止盈
                BigDecimal totalPnl = currentPrice.subtract(avgCost).multiply(positionAmount);
                
                if (pnlPct.compareTo(new BigDecimal("0.20")) > 0) {
                    // 盈利超20%：清仓
                    advice.action(RiskAction.TAKE_PROFIT_ALL)
                           .reduceAmount(positionAmount)
                           .reason(String.format("移动止盈清仓 盈利%.2f%% 回撤%.2f%%", 
                               pnlPct.multiply(new BigDecimal("100")),
                               drawdownFromHigh.abs().multiply(new BigDecimal("100"))));
                } else if (pnlPct.compareTo(new BigDecimal("0.05")) > 0) {
                    // 盈利5-20%：减仓50%，余下继续跟踪
                    advice.action(RiskAction.TAKE_PROFIT_HALF)
                           .reduceAmount(positionAmount.multiply(new BigDecimal("0.5")))
                           .reason(String.format("移动止盈减仓 盈利%.2f%%", 
                               pnlPct.multiply(new BigDecimal("100"))));
                }
                return advice.build();
            }
        }
        
        // ========== 3. 分级止盈检查 ==========
        if (pnlPct.compareTo(new BigDecimal("0.10")) > 0) {
            // 盈利超10%：检查是否已止盈过
            int tpLevel = getTakeProfitLevel(symbol);
            if (tpLevel < 2) {
                advice.action(RiskAction.TAKE_PROFIT_PARTIAL)
                       .reduceAmount(positionAmount.multiply(new BigDecimal("0.3")))
                       .reason("二级止盈：盈利超10%减仓30%");
                setTakeProfitLevel(symbol, 2);
                return advice.build();
            }
        } else if (pnlPct.compareTo(new BigDecimal("0.05")) > 0) {
            int tpLevel = getTakeProfitLevel(symbol);
            if (tpLevel < 1) {
                advice.action(RiskAction.TAKE_PROFIT_PARTIAL)
                       .reduceAmount(positionAmount.multiply(new BigDecimal("0.3")))
                       .reason("一级止盈：盈利超5%减仓30%")
                       .stopLossPrice(avgCost); // 余下设置保本止损
                setTakeProfitLevel(symbol, 1);
                return advice.build();
            }
        }
        
        // 持有
        return RiskAdvice.hold(String.format("持仓中 盈亏%.2f%%", 
            pnlPct.multiply(new BigDecimal("100"))));
    }
    
    /**
     * 检查做T仓位是否需要止损
     * 
     * @param symbol 交易对
     * @param currentPrice 当前价格
     * @param tCost 做T成本价
     * @param tAmount 做T数量
     * @param tDirection 1:正T -1:反T
     * @param tStartTime 做T开始时间（毫秒）
     * @return 风控建议
     */
    public RiskAdvice checkTPosition(String symbol, BigDecimal currentPrice,
                                     BigDecimal tCost, BigDecimal tAmount,
                                     int tDirection, long tStartTime) {
        if (tAmount.compareTo(BigDecimal.ZERO) <= 0 || tCost.compareTo(BigDecimal.ZERO) <= 0) {
            return RiskAdvice.hold("无做T持仓");
        }
        
        long holdMinutes = (System.currentTimeMillis() - tStartTime) / 60000;
        
        // 计算做T盈亏
        BigDecimal pnlPct;
        if (tDirection == 1) {
            // 正T：买后卖
            pnlPct = currentPrice.subtract(tCost).divide(tCost, 6, RoundingMode.DOWN);
        } else {
            // 反T：卖后买
            pnlPct = tCost.subtract(currentPrice).divide(tCost, 6, RoundingMode.DOWN);
        }
        
        RiskAdvice.RiskAdviceBuilder advice = RiskAdvice.builder()
            .symbol(symbol)
            .currentPrice(currentPrice)
            .tCost(tCost)
            .pnlPercent(pnlPct)
            .holdMinutes((int) holdMinutes)
            .isTPosition(true);
        
        // ========== 1. 做T硬止损（最优先）==========
        if (pnlPct.compareTo(T_STOP_LOSS.negate()) <= 0) {
            advice.action(tDirection == 1 ? RiskAction.T_STOP_LOSS_SELL : RiskAction.T_STOP_LOSS_BUY)
                   .reduceAmount(tAmount)
                   .reason(String.format("做T止损 亏损%.2f%% 超过%.1f%%", 
                       pnlPct.multiply(new BigDecimal("100")),
                       T_STOP_LOSS.multiply(new BigDecimal("100"))));
            
            // 记录当日做T亏损
            recordDailyTLoss(symbol, pnlPct.multiply(tAmount).multiply(tCost));
            return advice.build();
        }
        
        // ========== 2. 做T时间止损 ==========
        if (holdMinutes >= T_MAX_HOLD_MINUTES) {
            if (pnlPct.compareTo(BigDecimal.ZERO) > 0) {
                // 有盈利：止盈平仓
                advice.action(tDirection == 1 ? RiskAction.T_TAKE_PROFIT : RiskAction.T_COVER)
                       .reduceAmount(tAmount)
                       .reason(String.format("做T时间止盈 持仓%d分钟 盈利%.2f%%", 
                           holdMinutes, pnlPct.multiply(new BigDecimal("100"))));
            } else {
                // 亏损或保本：止损平仓
                advice.action(tDirection == 1 ? RiskAction.T_STOP_LOSS_SELL : RiskAction.T_STOP_LOSS_BUY)
                       .reduceAmount(tAmount)
                       .reason(String.format("做T时间止损 持仓%d分钟 盈亏%.2f%%", 
                           holdMinutes, pnlPct.multiply(new BigDecimal("100"))));
                recordDailyTLoss(symbol, pnlPct.abs().multiply(tAmount).multiply(tCost));
            }
            return advice.build();
        }
        
        // ========== 3. 做T止盈 ==========
        // 正T盈利超1%或反T盈利超1%
        if (pnlPct.compareTo(new BigDecimal("0.01")) >= 0) {
            advice.action(tDirection == 1 ? RiskAction.T_TAKE_PROFIT : RiskAction.T_COVER)
                   .reduceAmount(tAmount)
                   .reason(String.format("做T止盈 盈利%.2f%%", 
                       pnlPct.multiply(new BigDecimal("100"))));
            return advice.build();
        }
        
        // ========== 4. 当日做T亏损限额检查 ==========
        BigDecimal dailyLoss = getDailyTLoss(symbol);
        if (dailyLoss.compareTo(MAX_DAILY_T_LOSS) > 0) {
            advice.action(RiskAction.T_BAN)
                   .reason(String.format("当日做T亏损%.2f%%超过限额，禁止再做T", 
                       dailyLoss.multiply(new BigDecimal("100"))));
            return advice.build();
        }
        
        return RiskAdvice.hold(String.format("做T持仓%d分钟 盈亏%.2f%%", 
            holdMinutes, pnlPct.multiply(new BigDecimal("100"))));
    }
    
    /**
     * 检查趋势止损（EMA60或趋势线）
     */
    public boolean checkTrendStop(String symbol, BigDecimal currentPrice, BigDecimal ema60) {
        if (ema60 == null || ema60.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        boolean broken = currentPrice.compareTo(ema60.multiply(new BigDecimal("0.995"))) < 0;
        if (broken) {
            log.warn("{} 触发趋势止损 当前价{} EMA60{}", symbol, currentPrice, ema60);
        }
        return broken;
    }
    
    /**
     * 更新最高价（用于移动止盈）
     */
    public void updateHighestPrice(String symbol, BigDecimal currentPrice) {
        String key = "risk:high:" + symbol;
        String lastHigh = jedisClient.get(key);
        
        BigDecimal highest = lastHigh == null ? currentPrice : new BigDecimal(lastHigh);
        if (currentPrice.compareTo(highest) > 0) {
            jedisClient.setex(key, 86400, currentPrice.toString());
            log.info("{} 更新最高价 {}", symbol, currentPrice);
        }
    }
    
    /**
     * 重置止盈等级（新开仓时调用）
     */
    public void resetTakeProfitLevel(String symbol) {
        jedisClient.del("risk:tp:level:" + symbol);
        jedisClient.del("risk:high:" + symbol);
        jedisClient.del("risk:t:loss:" + symbol + ":" + getToday());
    }
    
    // ==================== 私有方法 ====================
    
    private BigDecimal calculateTrailingStopPrice(String symbol, BigDecimal highestPrice) {
        // 根据盈利幅度调整回撤容忍度
        // 盈利越大，回撤容忍度可以适当放大
        BigDecimal drawdown = TRAILING_STOP;
        
        // 盈利超20%，回撤容忍度放宽到5%
        // 盈利超50%，回撤容忍度放宽到8%
        // 这里简化处理，固定3%
        
        return highestPrice.multiply(BigDecimal.ONE.subtract(drawdown));
    }
    
    private int getTakeProfitLevel(String symbol) {
        String val = jedisClient.get("risk:tp:level:" + symbol);
        return val == null ? 0 : Integer.parseInt(val);
    }
    
    private void setTakeProfitLevel(String symbol, int level) {
        jedisClient.setex("risk:tp:level:" + symbol, 86400, String.valueOf(level));
    }
    
    private void recordDailyTLoss(String symbol, BigDecimal loss) {
        String key = "risk:t:loss:" + symbol + ":" + getToday();
        String current = jedisClient.get(key);
        BigDecimal total = current == null ? loss : new BigDecimal(current).add(loss);
        jedisClient.setex(key, 86400, total.toString());
    }
    
    private BigDecimal getDailyTLoss(String symbol) {
        String key = "risk:t:loss:" + symbol + ":" + getToday();
        String val = jedisClient.get(key);
        return val == null ? BigDecimal.ZERO : new BigDecimal(val);
    }
    
    private String getToday() {
        return new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
    }
    
    // ==================== 数据类 ====================
    
    @Data
    @Builder
    public static class RiskAdvice {
        private String symbol;
        private RiskAction action;
        private BigDecimal currentPrice;
        private BigDecimal avgCost;
        private BigDecimal tCost;
        private BigDecimal pnlPercent;
        private BigDecimal reduceAmount;
        private BigDecimal stopLossPrice;
        private String reason;
        private Integer holdMinutes;
        private Boolean isTPosition;
        private BigDecimal positionAmount;
        
        public static RiskAdvice hold(String reason) {
            return RiskAdvice.builder()
                .action(RiskAction.HOLD)
                .reason(reason)
                .build();
        }
    }
    
    public enum RiskAction {
        // 底仓操作
        HOLD("持有观望"),
        REDUCE_HALF("减仓50%"),
        STOP_LOSS_ALL("全部止损"),
        TAKE_PROFIT_ALL("全部止盈"),
        TAKE_PROFIT_HALF("减仓50%止盈"),
        TAKE_PROFIT_PARTIAL("部分止盈"),
        
        // 做T操作
        T_TAKE_PROFIT("做T止盈卖出"),
        T_COVER("做T平仓买回"),
        T_STOP_LOSS_SELL("做T止损卖出"),
        T_STOP_LOSS_BUY("做T止损买回"),
        T_BAN("禁止做T");
        
        private final String desc;
        
        RiskAction(String desc) {
            this.desc = desc;
        }
        
        public String getDesc() {
            return desc;
        }
    }
}
