package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 高质量低频做T策略 - 杜绝频繁止损
 * 
 * 核心原则：
 * 1. 【宁缺毋滥】一天最多2-3次做T，没有好机会就不做
 * 2. 【高胜率入场】只在高概率形态下做T（胜率高才出手）
 * 3. 【严格冷却】一次做T失败后，冷却30分钟才能再做
 * 4. 【日限额】当日做T累计亏损达0.5%，当日禁止再做T
 * 5. 【不追单】错过就错过，不追价做T
 */
@Slf4j
@Component
public class QualityLowFreqTStrategy {

    // ==================== 严格限制 ====================
    
    /** 日最大做T次数：2次 */
    private static final int MAX_DAILY_T = 2;
    /** 做T失败后冷却时间：30分钟 */
    private static final int COOLDOWN_MINUTES = 30;
    /** 日累计亏损限额：0.5% */
    private static final BigDecimal DAILY_LOSS_LIMIT = new BigDecimal("0.005");
    /** 单次做T最大仓位：5% */
    private static final BigDecimal T_POSITION_RATIO = new BigDecimal("0.05");
    
    // ==================== 入场门槛（高胜率条件）====================
    
    /** 最小波动空间：0.8%（覆盖手续费+利润） */
    private static final BigDecimal MIN_FLUCTUATION = new BigDecimal("0.008");
    /** 理想波动空间：1.5% */
    private static final BigDecimal IDEAL_FLUCTUATION = new BigDecimal("0.015");
    /** 趋势确认：需要连续3根K线同向 */
    private static final int TREND_CONFIRM_BARS = 3;
    /** RSI阈值：反T需要RSI>65，正T需要RSI<35 */
    private static final BigDecimal RSI_OVERBOUGHT = new BigDecimal("65");
    private static final BigDecimal RSI_OVERSOLD = new BigDecimal("35");
    
    // ==================== 状态管理 ====================
    
    @Data
    public static class TState {
        private String symbol;
        private int dailyCount = 0;           // 今日做T次数
        private BigDecimal dailyPnl = BigDecimal.ZERO; // 今日累计盈亏
        private long lastFailureTime = 0;     // 上次失败时间
        private boolean inCooldown = false;   // 是否在冷却期
        private TOrder activeOrder = null;    // 当前进行中的T单
        private List<TRecord> history = new ArrayList<>();
        
        /**
         * 检查是否允许新开做T
         */
        public boolean canOpenNewT() {
            // 1. 有持仓，不新开
            if (activeOrder != null) return false;
            
            // 2. 次数用完
            if (dailyCount >= MAX_DAILY_T) {
                log.info("【{}】今日做T次数已用完（{}/{}），不再做T", symbol, dailyCount, MAX_DAILY_T);
                return false;
            }
            
            // 3. 亏损超限
            if (dailyPnl.compareTo(DAILY_LOSS_LIMIT.negate()) < 0) {
                log.info("【{}】今日做T累计亏损{}，超过限额{}，当日禁止做T", 
                    symbol, dailyPnl, DAILY_LOSS_LIMIT);
                return false;
            }
            
            // 4. 冷却期
            if (inCooldown) {
                long minutesSinceFail = (System.currentTimeMillis() - lastFailureTime) / 60000;
                if (minutesSinceFail < COOLDOWN_MINUTES) {
                    log.info("【{}】做T冷却中，还需{}分钟", symbol, COOLDOWN_MINUTES - minutesSinceFail);
                    return false;
                } else {
                    inCooldown = false;
                }
            }
            
            return true;
        }
        
        /**
         * 记录做T结果
         */
        public void recordResult(BigDecimal pnl, boolean success) {
            dailyPnl = dailyPnl.add(pnl);
            if (!success) {
                lastFailureTime = System.currentTimeMillis();
                inCooldown = true;
            }
            dailyCount++;
            
            TRecord record = TRecord.builder()
                .pnl(pnl)
                .success(success)
                .time(System.currentTimeMillis())
                .build();
            history.add(record);
        }
        
        public void dailyReset() {
            dailyCount = 0;
            dailyPnl = BigDecimal.ZERO;
            inCooldown = false;
            history.clear();
        }
    }
    
    @Data
    @Builder
    public static class TOrder {
        private String id;
        private int type;  // 1正T -1反T
        private BigDecimal entryPrice;
        private BigDecimal qty;
        private long entryTime;
        private BigDecimal targetPrice;
        private BigDecimal stopPrice;
        private String reason;
    }
    
    @Data
    @Builder
    public static class TRecord {
        private BigDecimal pnl;
        private boolean success;
        private long time;
    }
    
    // ==================== 入场质量评估 ====================
    
    /**
     * 评估当前是否适合做T（高质量门槛）
     * 
     * @return 评分 0-100，<60不做，60-80谨慎做，>80果断做
     */
    public int evaluateOpportunity(BigDecimal currentPrice, BigDecimal avgCost,
                                    List<PriceBar> recentBars, BigDecimal rsi,
                                    boolean isOverallProfit) {
        
        if (recentBars.size() < 10) return 0;
        
        int score = 50; // 基础分
        
        // 1. 波动空间评估（30分）
        BigDecimal high = recentBars.stream().map(PriceBar::getHigh).max(BigDecimal::compareTo).orElse(currentPrice);
        BigDecimal low = recentBars.stream().map(PriceBar::getLow).min(BigDecimal::compareTo).orElse(currentPrice);
        BigDecimal fluctuation = high.subtract(low).divide(currentPrice, 6, RoundingMode.HALF_UP);
        
        if (fluctuation.compareTo(IDEAL_FLUCTUATION) >= 0) {
            score += 30; // 波动大，空间大
        } else if (fluctuation.compareTo(MIN_FLUCTUATION) >= 0) {
            score += 15; // 波动中等
        } else {
            score -= 20; // 波动太小，不值得做
        }
        
        // 2. 趋势确认评估（30分）
        boolean trendUp = isTrendUp(recentBars, TREND_CONFIRM_BARS);
        boolean trendDown = isTrendDown(recentBars, TREND_CONFIRM_BARS);
        
        // 反T需要趋势向上（高位）
        // 正T需要趋势向下（低位）
        BigDecimal profitPct = currentPrice.subtract(avgCost).divide(avgCost, 6, RoundingMode.HALF_UP);
        
        if (isOverallProfit && profitPct.compareTo(new BigDecimal("0.01")) > 0 && trendUp) {
            score += 30; // 反T理想条件：盈利+趋势向上
        } else if (!isOverallProfit && profitPct.compareTo(new BigDecimal("-0.01")) < 0 && trendDown) {
            score += 30; // 正T理想条件：亏损+趋势向下
        } else if (trendUp || trendDown) {
            score += 10; // 有趋势但不理想
        } else {
            score -= 10; // 震荡，不明确
        }
        
        // 3. RSI评估（20分）
        if (rsi != null) {
            if (rsi.compareTo(RSI_OVERBOUGHT) > 0 && isOverallProfit) {
                score += 20; // 超买+盈利，反T好机会
            } else if (rsi.compareTo(RSI_OVERSOLD) < 0 && !isOverallProfit) {
                score += 20; // 超卖+亏损，正T好机会（但小资金难做）
            } else if (rsi.compareTo(new BigDecimal("50")) > 0 && !isOverallProfit) {
                score -= 10; // 偏高还亏损，不适合正T
            }
        }
        
        // 4. 量价评估（20分）
        boolean volumeConfirm = isVolumeConfirming(recentBars);
        if (volumeConfirm) {
            score += 20;
        } else {
            score -= 10; // 无量，假突破风险
        }
        
        return Math.max(0, Math.min(100, score));
    }
    
    /**
     * 决定是否做T
     */
    public TDecision decideT(TState state, int score, BigDecimal currentPrice, 
                             BigDecimal avgCost, BigDecimal totalQty,
                             boolean isOverallProfit, BigDecimal availableCash,
                             BigDecimal availableQty) {
        
        // 1. 前置检查
        if (!state.canOpenNewT()) {
            return TDecision.skip("不满足开新仓条件");
        }
        
        // 2. 质量门槛
        if (score < 60) {
            return TDecision.skip("机会质量低（得分" + score + "），等待更好机会");
        }
        
        // 3. 根据评分决定仓位
        BigDecimal ratio = score >= 80 ? T_POSITION_RATIO : T_POSITION_RATIO.multiply(new BigDecimal("0.5"));
        BigDecimal tQty = totalQty.multiply(ratio);
        
        // 4. 决定方向
        BigDecimal profitPct = currentPrice.subtract(avgCost).divide(avgCost, 6, RoundingMode.HALF_UP);
        
        // 反T：整体盈利+价格偏高
        if (isOverallProfit && profitPct.compareTo(new BigDecimal("0.005")) > 0) {
            if (availableQty.compareTo(tQty) < 0) {
                return TDecision.skip("可卖仓位不足");
            }
            
            return createShortTDecision(currentPrice, tQty, score, ratio);
        }
        
        // 正T：整体盈利+价格偏低（回踩买入）
        if (isOverallProfit && profitPct.compareTo(new BigDecimal("0")) > 0 && 
            profitPct.compareTo(new BigDecimal("0.005")) <= 0) {
            
            // 小资金满仓做不了正T，需要检查现金
            if (availableCash.compareTo(currentPrice.multiply(tQty)) < 0) {
                return TDecision.skip("现金不足，无法做正T");
            }
            
            return createLongTDecision(currentPrice, tQty, score, ratio);
        }
        
        // 整体亏损时不做T（已在外部过滤，这里做保险）
        return TDecision.skip("整体亏损或条件不符，不做T");
    }
    
    private TDecision createShortTDecision(BigDecimal price, BigDecimal qty, int score, BigDecimal ratio) {
        String id = "ST_" + System.currentTimeMillis() % 10000;
        
        return TDecision.builder()
            .shouldDoT(true)
            .type(-1)
            .orderId(id)
            .qty(qty)
            .entryPrice(price)
            .targetPrice(price.multiply(new BigDecimal("0.995"))) // 目标低0.5%买回
            .stopPrice(price.multiply(new BigDecimal("1.003")))   // 止损高0.3%
            .score(score)
            .reason(String.format("高质量反T（得分%d，仓位%.0f%%），目标买回%.2f，止损%.2f",
                score, ratio.multiply(new BigDecimal("100")),
                price.multiply(new BigDecimal("0.995")),
                price.multiply(new BigDecimal("1.003"))))
            .build();
    }
    
    private TDecision createLongTDecision(BigDecimal price, BigDecimal qty, int score, BigDecimal ratio) {
        String id = "LT_" + System.currentTimeMillis() % 10000;
        
        return TDecision.builder()
            .shouldDoT(true)
            .type(1)
            .orderId(id)
            .qty(qty)
            .entryPrice(price)
            .targetPrice(price.multiply(new BigDecimal("1.005"))) // 目标高0.5%卖出
            .stopPrice(price.multiply(new BigDecimal("0.997")))   // 止损低0.3%
            .score(score)
            .reason(String.format("高质量正T（得分%d，仓位%.0f%%），目标卖出%.2f，止损%.2f",
                score, ratio.multiply(new BigDecimal("100")),
                price.multiply(new BigDecimal("1.005")),
                price.multiply(new BigDecimal("0.997"))))
            .build();
    }
    
    /**
     * 检查平仓（做T单严格止损）
     */
    public TCloseDecision checkClose(TOrder order, BigDecimal currentPrice, long now) {
        long holdMin = (now - order.getEntryTime()) / 60000;
        
        // 止盈
        if (order.getType() == 1 && currentPrice.compareTo(order.getTargetPrice()) >= 0) {
            return TCloseDecision.close("止盈", calculateProfit(order, currentPrice));
        }
        if (order.getType() == -1 && currentPrice.compareTo(order.getTargetPrice()) <= 0) {
            return TCloseDecision.close("止盈", calculateProfit(order, currentPrice));
        }
        
        // 止损（做T单严格-0.3%）
        if (order.getType() == 1 && currentPrice.compareTo(order.getStopPrice()) <= 0) {
            return TCloseDecision.close("止损", calculateProfit(order, currentPrice));
        }
        if (order.getType() == -1 && currentPrice.compareTo(order.getStopPrice()) >= 0) {
            return TCloseDecision.close("止损", calculateProfit(order, currentPrice));
        }
        
        // 时间止损（15分钟）
        if (holdMin >= 15) {
            BigDecimal profit = calculateProfit(order, currentPrice);
            String reason = profit.compareTo(BigDecimal.ZERO) > 0 ? "时间到盈利" : "时间到亏损";
            return TCloseDecision.close(reason, profit);
        }
        
        return TCloseDecision.hold();
    }
    
    private BigDecimal calculateProfit(TOrder order, BigDecimal exitPrice) {
        if (order.getType() == 1) {
            return exitPrice.subtract(order.getEntryPrice()).multiply(order.getQty());
        } else {
            return order.getEntryPrice().subtract(exitPrice).multiply(order.getQty());
        }
    }
    
    // ==================== 工具方法 ====================
    
    private boolean isTrendUp(List<PriceBar> bars, int confirmBars) {
        if (bars.size() < confirmBars + 1) return false;
        int upCount = 0;
        for (int i = bars.size() - confirmBars; i < bars.size(); i++) {
            if (bars.get(i).getClose().compareTo(bars.get(i-1).getClose()) > 0) {
                upCount++;
            }
        }
        return upCount >= confirmBars;
    }
    
    private boolean isTrendDown(List<PriceBar> bars, int confirmBars) {
        if (bars.size() < confirmBars + 1) return false;
        int downCount = 0;
        for (int i = bars.size() - confirmBars; i < bars.size(); i++) {
            if (bars.get(i).getClose().compareTo(bars.get(i-1).getClose()) < 0) {
                downCount++;
            }
        }
        return downCount >= confirmBars;
    }
    
    private boolean isVolumeConfirming(List<PriceBar> bars) {
        if (bars.size() < 5) return false;
        BigDecimal avgVol = bars.subList(0, bars.size() - 1).stream()
            .map(PriceBar::getVolume)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(new BigDecimal(bars.size() - 1), 8, RoundingMode.HALF_UP);
        BigDecimal currentVol = bars.get(bars.size() - 1).getVolume();
        return currentVol.compareTo(avgVol.multiply(new BigDecimal("1.2"))) > 0;
    }
    
    // ==================== 数据类 ====================
    
    @Data
    @Builder
    public static class PriceBar {
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal volume;
    }
    
    @Data
    @Builder
    public static class TDecision {
        private boolean shouldDoT;
        private int type;
        private String orderId;
        private BigDecimal qty;
        private BigDecimal entryPrice;
        private BigDecimal targetPrice;
        private BigDecimal stopPrice;
        private int score;
        private String reason;
        
        public static TDecision skip(String reason) {
            return TDecision.builder()
                .shouldDoT(false)
                .reason(reason)
                .build();
        }
    }
    
    @Data
    @Builder
    public static class TCloseDecision {
        private boolean shouldClose;
        private String reason;
        private BigDecimal profit;
        
        public static TCloseDecision close(String reason, BigDecimal profit) {
            return TCloseDecision.builder()
                .shouldClose(true)
                .reason(reason)
                .profit(profit)
                .build();
        }
        
        public static TCloseDecision hold() {
            return TCloseDecision.builder()
                .shouldClose(false)
                .build();
        }
    }
}
