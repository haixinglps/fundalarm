package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 基于实际指标的智能卖出策略 - 替代机械网格卖出
 * 
 * 核心逻辑：
 * 1. 【趋势强】突破上沿 + 放量 → 持有（可能有大行情）
 * 2. 【趋势弱】触及上沿 + 缩量/背离 → 卖出（大概率回落）
 * 3. 【成本保护】任何位置，跌破盈亏平衡价-0.5% → 止损
 * 4. 【利润保护】盈利超2%后，回撤1% → 移动止盈
 * 
 * 卖出决策权重：
 * - 趋势方向 40%
 * - 动量强度 30%
 * - 量价配合 20%
 * - 盈亏状态 10%
 */
@Slf4j
@Component
public class IndicatorBasedSellStrategy {

    // ==================== 权重配置 ====================
    private static final BigDecimal W_TREND = new BigDecimal("0.40");
    private static final BigDecimal W_MOMENTUM = new BigDecimal("0.30");
    private static final BigDecimal W_VOLUME = new BigDecimal("0.20");
    private static final BigDecimal W_PROFIT = new BigDecimal("0.10");
    
    // ==================== 阈值 ====================
    /** 强势阈值：得分>60，不卖 */
    private static final int STRONG_THRESHOLD = 60;
    /** 弱势阈值：得分<40，卖出 */
    private static final int WEAK_THRESHOLD = 40;
    /** 移动止盈回撤 */
    private static final BigDecimal TRAILING_PCT = new BigDecimal("0.01");
    /** 成本保护止损 */
    private static final BigDecimal COST_PROTECT_PCT = new BigDecimal("0.005");
    
    // ==================== 指标数据 ====================
    
    @Data
    @Builder
    public static class MarketIndicators {
        // 趋势指标
        private BigDecimal maSlope;        // 均线斜率（5周期）
        private boolean isAboveMa;         // 是否站在均线上方
        private int trendScore;            // -100到100
        
        // 动量指标
        private BigDecimal rsi;            // RSI值
        private BigDecimal macdHist;       // MACD柱状图
        private boolean isDivergence;      // 是否背离（价格新高，指标未新高）
        private int momentumScore;         // -100到100
        
        // 量价指标
        private BigDecimal volumeRatio;    // 当前量/均量
        private boolean isVolumeConfirm;   // 量价是否配合
        private int volumeScore;           // -100到100
        
        // 价格位置
        private BigDecimal gridPosition;   // 在网格中的位置 0-1
        private BigDecimal dayHighDist;    // 距日高距离
        
        // 整体盈亏
        private BigDecimal profitPct;      // 整体盈亏百分比
        private BigDecimal breakevenPrice; // 盈亏平衡价
    }
    
    // ==================== 智能卖出决策 ====================
    
    /**
     * 判断是否卖出（替代机械上沿卖出）
     * 
     * @param currentPrice 当前价格
     * @param gridUpperPrice 网格上沿价格
     * @param indicators 市场指标
     * @return 卖出建议
     */
    public SellDecision decideSell(BigDecimal currentPrice, BigDecimal gridUpperPrice,
                                    MarketIndicators indicators) {
        
        // 1. 优先检查：成本保护（最重要！）
        if (indicators.getBreakevenPrice() != null) {
            BigDecimal costStop = indicators.getBreakevenPrice()
                .multiply(BigDecimal.ONE.subtract(COST_PROTECT_PCT));
            if (currentPrice.compareTo(costStop) <= 0) {
                return SellDecision.builder()
                    .shouldSell(true)
                    .ratio(BigDecimal.ONE)
                    .price(currentPrice)
                    .reason(String.format("成本保护止损：盈亏平衡价%.2f，当前%.2f，跌破%.2f%%",
                        indicators.getBreakevenPrice(), currentPrice,
                        COST_PROTECT_PCT.multiply(new BigDecimal("100"))))
                    .priority(SellPriority.STOP_LOSS)
                    .build();
            }
        }
        
        // 2. 优先检查：移动止盈（已有利润保护）
        if (indicators.getProfitPct().compareTo(new BigDecimal("0.02")) > 0) {
            // 从最高点回撤超过1%
            BigDecimal highest = getHighestPrice(indicators);
            if (highest != null) {
                BigDecimal trailingPrice = highest.multiply(BigDecimal.ONE.subtract(TRAILING_PCT));
                if (currentPrice.compareTo(trailingPrice) <= 0) {
                    return SellDecision.builder()
                        .shouldSell(true)
                        .ratio(BigDecimal.ONE)
                        .price(currentPrice)
                        .reason(String.format("移动止盈：从高点%.2f回撤%.2f%%至%.2f",
                            highest, TRAILING_PCT.multiply(new BigDecimal("100")), currentPrice))
                        .priority(SellPriority.TRAILING_STOP)
                        .build();
                }
            }
        }
        
        // 3. 计算综合强度得分
        int totalScore = calculateTotalScore(indicators);
        
        // 4. 到达网格上沿时的智能决策
        if (currentPrice.compareTo(gridUpperPrice) >= 0) {
            // 强势：不卖，可能有大行情
            if (totalScore >= STRONG_THRESHOLD) {
                return SellDecision.builder()
                    .shouldSell(false)
                    .reason(String.format("触及上沿但趋势强（得分%d）：趋势%d 动量%d 量能%d，持有",
                        totalScore, indicators.getTrendScore(), 
                        indicators.getMomentumScore(), indicators.getVolumeScore()))
                    .priority(SellPriority.HOLD)
                    .strengthScore(totalScore)
                    .build();
            }
            
            // 中性：卖一半，留一半博弈
            if (totalScore >= WEAK_THRESHOLD && totalScore < STRONG_THRESHOLD) {
                return SellDecision.builder()
                    .shouldSell(true)
                    .ratio(new BigDecimal("0.5"))
                    .price(currentPrice)
                    .reason(String.format("触及上沿但趋势中性（得分%d），卖半仓锁定利润，留半仓博弈",
                        totalScore))
                    .priority(SellPriority.PARTIAL_PROFIT)
                    .strengthScore(totalScore)
                    .build();
            }
            
            // 弱势：全卖
            return SellDecision.builder()
                .shouldSell(true)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("触及上沿且趋势弱（得分%d）：趋势%d 动量%d 背离%s，全卖",
                    totalScore, indicators.getTrendScore(), 
                    indicators.getMomentumScore(), indicators.isDivergence()))
                .priority(SellPriority.FULL_PROFIT)
                .strengthScore(totalScore)
                .build();
        }
        
        // 5. 未到达上沿，但趋势极度弱势（提前止盈）
        if (totalScore < WEAK_THRESHOLD && indicators.getProfitPct().compareTo(new BigDecimal("0.01")) > 0) {
            return SellDecision.builder()
                .shouldSell(true)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("趋势极度弱势（得分%d），提前止盈，不等上沿", totalScore))
                .priority(SellPriority.EARLY_EXIT)
                .strengthScore(totalScore)
                .build();
        }
        
        // 6. 持有
        return SellDecision.builder()
            .shouldSell(false)
            .reason(String.format("持仓中，距离上沿%.2f%%，趋势得分%d",
                gridUpperPrice.subtract(currentPrice).divide(currentPrice, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")),
                totalScore))
            .priority(SellPriority.HOLD)
            .strengthScore(totalScore)
            .build();
    }
    
    /**
     * 计算综合强度得分
     */
    private int calculateTotalScore(MarketIndicators ind) {
        // 趋势得分映射
        int trend = ind.getTrendScore(); // -100到100
        
        // 动量得分调整
        int momentum = ind.getMomentumScore();
        if (ind.isDivergence()) {
            momentum -= 30; // 背离扣分
        }
        if (ind.getRsi() != null) {
            if (ind.getRsi().compareTo(new BigDecimal("70")) > 0) {
                momentum -= 20; // 超买扣分
            } else if (ind.getRsi().compareTo(new BigDecimal("30")) < 0) {
                momentum += 20; // 超卖加分
            }
        }
        
        // 量能得分
        int volume = ind.getVolumeScore();
        
        // 盈亏调整（已有利润时，可以冒险一点）
        int profitAdj = 0;
        if (ind.getProfitPct().compareTo(new BigDecimal("0.02")) > 0) {
            profitAdj = 10; // 盈利>2%，加点分，可以持久点
        } else if (ind.getProfitPct().compareTo(new BigDecimal("-0.01")) < 0) {
            profitAdj = -10; // 亏损，谨慎点
        }
        
        // 加权计算
        BigDecimal total = new BigDecimal(trend).multiply(W_TREND)
            .add(new BigDecimal(momentum).multiply(W_MOMENTUM))
            .add(new BigDecimal(volume).multiply(W_VOLUME))
            .add(new BigDecimal(profitAdj));
        
        return total.intValue();
    }
    
    /**
     * 获取最高价（简化，实际应从Redis/缓存获取）
     */
    private BigDecimal getHighestPrice(MarketIndicators ind) {
        if (ind.getDayHighDist() == null) return null;
        // 简化计算
        return null;
    }
    
    // ==================== 指标计算工具 ====================
    
    /**
     * 从价格序列计算指标
     */
    public MarketIndicators calculateIndicators(List<PriceBar> bars, BigDecimal currentPrice,
                                                  BigDecimal avgCost, BigDecimal totalQty) {
        if (bars.size() < 10) {
            return MarketIndicators.builder()
                .trendScore(0)
                .momentumScore(0)
                .volumeScore(0)
                .build();
        }
        
        // 计算均线斜率
        BigDecimal ma5Now = ma(bars, 5, bars.size() - 1);
        BigDecimal ma5Prev = ma(bars, 5, bars.size() - 6);
        BigDecimal slope = ma5Now.subtract(ma5Prev)
            .divide(ma5Prev, 6, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("10000")); // 放大
        int trendScore = slope.intValue();
        
        // 计算RSI（简化）
        BigDecimal rsi = calculateRSI(bars);
        int momentumScore = 0;
        if (rsi.compareTo(new BigDecimal("50")) > 0) {
            momentumScore = rsi.subtract(new BigDecimal("50")).multiply(new BigDecimal("2")).intValue();
        } else {
            momentumScore = rsi.subtract(new BigDecimal("50")).multiply(new BigDecimal("2")).intValue();
        }
        
        // 计算量比
        BigDecimal avgVol = bars.subList(0, bars.size() - 1).stream()
            .map(PriceBar::getVolume)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(new BigDecimal(bars.size() - 1), 8, RoundingMode.HALF_UP);
        BigDecimal currentVol = bars.get(bars.size() - 1).getVolume();
        BigDecimal volRatio = currentVol.divide(avgVol, 4, RoundingMode.HALF_UP);
        int volumeScore = volRatio.subtract(BigDecimal.ONE).multiply(new BigDecimal("100")).intValue();
        
        // 盈亏
        BigDecimal profitPct = currentPrice.subtract(avgCost)
            .divide(avgCost, 6, RoundingMode.HALF_UP);
        
        return MarketIndicators.builder()
            .maSlope(slope)
            .isAboveMa(currentPrice.compareTo(ma5Now) > 0)
            .trendScore(Math.max(-100, Math.min(100, trendScore)))
            .rsi(rsi)
            .momentumScore(Math.max(-100, Math.min(100, momentumScore)))
            .volumeRatio(volRatio)
            .isVolumeConfirm(volRatio.compareTo(new BigDecimal("1.2")) > 0)
            .volumeScore(Math.max(-100, Math.min(100, volumeScore)))
            .profitPct(profitPct)
            .breakevenPrice(avgCost.multiply(new BigDecimal("1.0015")))
            .build();
    }
    
    private BigDecimal ma(List<PriceBar> bars, int period, int endIdx) {
        if (endIdx < period - 1) return bars.get(endIdx).getClose();
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = endIdx - period + 1; i <= endIdx; i++) {
            sum = sum.add(bars.get(i).getClose());
        }
        return sum.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateRSI(List<PriceBar> bars) {
        // 简化14周期RSI
        int period = Math.min(14, bars.size() - 1);
        BigDecimal gain = BigDecimal.ZERO, loss = BigDecimal.ZERO;
        
        for (int i = bars.size() - period; i < bars.size(); i++) {
            BigDecimal change = bars.get(i).getClose().subtract(bars.get(i-1).getClose());
            if (change.compareTo(BigDecimal.ZERO) > 0) {
                gain = gain.add(change);
            } else {
                loss = loss.add(change.abs());
            }
        }
        
        if (loss.compareTo(BigDecimal.ZERO) == 0) return new BigDecimal("100");
        BigDecimal rs = gain.divide(loss, 6, RoundingMode.HALF_UP).divide(
            new BigDecimal(period), 6, RoundingMode.HALF_UP);
        return new BigDecimal("100").subtract(
            new BigDecimal("100").divide(BigDecimal.ONE.add(rs), 6, RoundingMode.HALF_UP));
    }
    
    // ==================== 数据类 ====================
    
    @Data
    @Builder
    public static class PriceBar {
        private long time;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal volume;
    }
    
    @Data
    @Builder
    public static class SellDecision {
        private boolean shouldSell;
        private BigDecimal ratio;        // 卖出比例
        private BigDecimal price;
        private String reason;
        private SellPriority priority;   // 优先级
        private int strengthScore;       // 强度得分
    }
    
    public enum SellPriority {
        STOP_LOSS,      // 止损（最高优先级）
        TRAILING_STOP,  // 移动止盈
        EARLY_EXIT,     // 提前止盈
        FULL_PROFIT,    // 全仓止盈
        PARTIAL_PROFIT, // 部分止盈
        HOLD            // 持有
    }
}
