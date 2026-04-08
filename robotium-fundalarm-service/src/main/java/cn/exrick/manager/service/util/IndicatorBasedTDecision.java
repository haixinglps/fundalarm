package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 多指标综合做T决策系统
 * 
 * 综合指标：
 * 1. 【振幅分析】当前波动区间、历史波动率 percentile
 * 2. 【趋势强度】ADX判断趋势强弱，避免震荡假突破
 * 3. 【动能指标】RSI超买超卖 + MACD金叉死叉
 * 4. 【量价配合】成交量确认价格有效性
 * 5. 【波动率】ATR自适应止盈止损
 * 6. 【支撑阻力】关键价位突破/回调
 * 
 * 输出：综合得分 + 方向 + 仓位 + 自适应止盈止损
 */
@Slf4j
@Component
public class IndicatorBasedTDecision {

    // ==================== 指标权重 ====================
    
    private static final BigDecimal W_AMPLITUDE = new BigDecimal("0.25");   // 振幅 25%
    private static final BigDecimal W_TREND = new BigDecimal("0.20");       // 趋势 20%
    private static final BigDecimal W_MOMENTUM = new BigDecimal("0.25");    // 动能 25%
    private static final BigDecimal W_VOLUME = new BigDecimal("0.15");      // 量能 15%
    private static final BigDecimal W_SUPPORT = new BigDecimal("0.15");     // 支撑阻力 15%
    
    // ==================== 阈值 ====================
    
    private static final BigDecimal MIN_AMPLITUDE = new BigDecimal("0.005");  // 最小振幅0.5%
    private static final BigDecimal IDEAL_AMPLITUDE = new BigDecimal("0.015"); // 理想振幅1.5%
    private static final BigDecimal HIGH_VOLATILITY = new BigDecimal("0.025"); // 高波动2.5%
    
    // ==================== 数据结构 ====================
    
    @Data
    @Builder
    public static class MarketData {
        // 价格数据
        private BigDecimal currentPrice;
        private BigDecimal avgCost;
        private List<PriceBar> recentBars;      // 最近20根K线
        
        // 技术指标
        private BigDecimal rsi;                 // RSI值
        private BigDecimal macdLine;            // MACD快线
        private BigDecimal macdSignal;          // MACD信号线
        private BigDecimal macdHist;            // MACD柱状图
        private BigDecimal adx;                 // ADX趋势强度
        private BigDecimal atr;                 // ATR波动率
        
        // 支撑阻力
        private BigDecimal supportLevel;        // 支撑位
        private BigDecimal resistanceLevel;     // 阻力位
        
        // 成交量
        private BigDecimal currentVolume;
        private BigDecimal avgVolume;           // 均量
        
        // 振幅计算
        private BigDecimal dayHigh;
        private BigDecimal dayLow;
    }
    
    @Data
    @Builder
    public static class PriceBar {
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal volume;
    }
    
    // ==================== 振幅分析 ====================
    
    /**
     * 振幅分析器
     */
    public AmplitudeAnalysis analyzeAmplitude(MarketData data) {
        if (data.getRecentBars().size() < 10) {
            return AmplitudeAnalysis.builder().valid(false).build();
        }
        
        List<PriceBar> bars = data.getRecentBars();
        int n = bars.size();
        
        // 计算20周期波动率（ATR简化版）
        BigDecimal atrSum = BigDecimal.ZERO;
        for (int i = n - 20; i < n; i++) {
            BigDecimal tr = bars.get(i).getHigh().subtract(bars.get(i).getLow());
            atrSum = atrSum.add(tr);
        }
        BigDecimal atr20 = atrSum.divide(new BigDecimal(20), 8, RoundingMode.HALF_UP)
            .divide(data.getCurrentPrice(), 6, RoundingMode.HALF_UP);
        
        // 计算日内振幅
        BigDecimal dayRange = data.getDayHigh().subtract(data.getDayLow())
            .divide(data.getCurrentPrice(), 6, RoundingMode.HALF_UP);
        
        // 当前在日内区间的位置（0=最低，100=最高）
        BigDecimal position = data.getCurrentPrice().subtract(data.getDayLow())
            .multiply(new BigDecimal("100"))
            .divide(data.getDayHigh().subtract(data.getDayLow()), 0, RoundingMode.HALF_UP);
        
        // 波动率评级
        VolatilityLevel level;
        if (atr20.compareTo(HIGH_VOLATILITY) > 0) {
            level = VolatilityLevel.HIGH;
        } else if (atr20.compareTo(IDEAL_AMPLITUDE) > 0) {
            level = VolatilityLevel.MEDIUM;
        } else if (atr20.compareTo(MIN_AMPLITUDE) > 0) {
            level = VolatilityLevel.LOW;
        } else {
            level = VolatilityLevel.TOO_LOW;
        }
        
        return AmplitudeAnalysis.builder()
            .valid(true)
            .atr20(atr20)
            .dayRange(dayRange)
            .positionInRange(position.intValue())
            .volatilityLevel(level)
            .build();
    }
    
    @Data
    @Builder
    public static class AmplitudeAnalysis {
        private boolean valid;
        private BigDecimal atr20;
        private BigDecimal dayRange;
        private int positionInRange;  // 0-100
        private VolatilityLevel volatilityLevel;
    }
    
    public enum VolatilityLevel {
        TOO_LOW, LOW, MEDIUM, HIGH
    }
    
    // ==================== 综合评分 ====================
    
    /**
     * 综合评分做T决策
     */
    public TDecisionResult makeDecision(MarketData data) {
        // 1. 振幅分析
        AmplitudeAnalysis amp = analyzeAmplitude(data);
        if (!amp.isValid() || amp.getVolatilityLevel() == VolatilityLevel.TOO_LOW) {
            return TDecisionResult.skip("振幅不足，不操作");
        }
        
        // 2. 各维度评分（-100到100）
        int amplitudeScore = scoreAmplitude(amp);
        int trendScore = scoreTrend(data);
        int momentumScore = scoreMomentum(data);
        int volumeScore = scoreVolume(data);
        int supportScore = scoreSupportResistance(data, amp);
        
        // 3. 加权综合得分
        BigDecimal totalScore = new BigDecimal(amplitudeScore).multiply(W_AMPLITUDE)
            .add(new BigDecimal(trendScore)).multiply(W_TREND)
            .add(new BigDecimal(momentumScore)).multiply(W_MOMENTUM)
            .add(new BigDecimal(volumeScore)).multiply(W_VOLUME)
            .add(new BigDecimal(supportScore)).multiply(W_SUPPORT);
        
        int finalScore = totalScore.intValue();
        
        // 4. 根据得分和位置决定方向
        return determineDirection(finalScore, amp, data);
    }
    
    /**
     * 振幅评分
     */
    private int scoreAmplitude(AmplitudeAnalysis amp) {
        int score = 0;
        
        switch (amp.getVolatilityLevel()) {
            case HIGH:    score += 80; break;  // 高波动，机会大
            case MEDIUM:  score += 60; break;  // 中等波动，机会适中
            case LOW:     score += 30; break;  // 低波动，机会小
            case TOO_LOW: score -= 50; break; // 波动太小，不做
        }
        
        // 位置评分（极端位置才有空间）
        if (amp.getPositionInRange() < 20) score += 40;  // 接近最低，正T机会
        if (amp.getPositionInRange() > 80) score -= 40;  // 接近最高，反T机会
        
        return Math.max(-100, Math.min(100, score));
    }
    
    /**
     * 趋势评分
     */
    private int scoreTrend(MarketData data) {
        if (data.getAdx() == null) return 0;
        
        int score = 0;
        BigDecimal adx = data.getAdx();
        
        // ADX > 25 趋势强，< 20 震荡
        if (adx.compareTo(new BigDecimal("25")) > 0) {
            score += 30;  // 趋势强，顺势做T
        } else if (adx.compareTo(new BigDecimal("20")) < 0) {
            score -= 20;  // 震荡，谨慎做T
        }
        
        // 简单趋势判断（5周期斜率）
        List<PriceBar> bars = data.getRecentBars();
        if (bars.size() >= 5) {
            BigDecimal ma5Now = calculateMA(bars, 5, bars.size() - 1);
            BigDecimal ma5Prev = calculateMA(bars, 5, bars.size() - 6);
            BigDecimal slope = ma5Now.subtract(ma5Prev).divide(ma5Prev, 6, RoundingMode.HALF_UP);
            
            if (slope.compareTo(new BigDecimal("0.001")) > 0) score += 20;  // 向上趋势
            if (slope.compareTo(new BigDecimal("-0.001")) < 0) score -= 20; // 向下趋势
        }
        
        return Math.max(-100, Math.min(100, score));
    }
    
    /**
     * 动能评分
     */
    private int scoreMomentum(MarketData data) {
        int score = 0;
        
        // RSI评分
        if (data.getRsi() != null) {
            BigDecimal rsi = data.getRsi();
            if (rsi.compareTo(new BigDecimal("30")) < 0) score += 50;      // 极度超卖，正T机会
            else if (rsi.compareTo(new BigDecimal("40")) < 0) score += 30; // 超卖
            else if (rsi.compareTo(new BigDecimal("60")) > 0) score -= 30; // 超买
            else if (rsi.compareTo(new BigDecimal("70")) > 0) score -= 50; // 极度超买，反T机会
        }
        
        // MACD评分
        if (data.getMacdHist() != null) {
            BigDecimal hist = data.getMacdHist();
            if (hist.compareTo(BigDecimal.ZERO) > 0) score += 20;  // 多头
            else score -= 20;  // 空头
        }
        
        // 背离检测
        if (isBullishDivergence(data)) score += 40;  // 底背离，强烈正T信号
        if (isBearishDivergence(data)) score -= 40;  // 顶背离，强烈反T信号
        
        return Math.max(-100, Math.min(100, score));
    }
    
    /**
     * 量能评分
     */
    private int scoreVolume(MarketData data) {
        if (data.getCurrentVolume() == null || data.getAvgVolume() == null) return 0;
        
        BigDecimal volRatio = data.getCurrentVolume().divide(data.getAvgVolume(), 4, RoundingMode.HALF_UP);
        int score = volRatio.subtract(BigDecimal.ONE).multiply(new BigDecimal("50")).intValue();
        
        // 量价配合
        List<PriceBar> bars = data.getRecentBars();
        if (bars.size() >= 2) {
            PriceBar last = bars.get(bars.size() - 1);
            PriceBar prev = bars.get(bars.size() - 2);
            
            boolean priceUp = last.getClose().compareTo(prev.getClose()) > 0;
            boolean volUp = last.getVolume().compareTo(prev.getVolume()) > 0;
            
            if (priceUp && volUp) score += 20;  // 价涨量增，健康
            if (priceUp && !volUp) score -= 15; // 价涨量缩，假突破
        }
        
        return Math.max(-100, Math.min(100, score));
    }
    
    /**
     * 支撑阻力评分
     */
    private int scoreSupportResistance(MarketData data, AmplitudeAnalysis amp) {
        int score = 0;
        BigDecimal price = data.getCurrentPrice();
        
        // 接近支撑位 → 正T机会
        if (data.getSupportLevel() != null) {
            BigDecimal distToSupport = price.subtract(data.getSupportLevel())
                .divide(price, 4, RoundingMode.HALF_UP);
            if (distToSupport.abs().compareTo(new BigDecimal("0.005")) < 0) {
                score += 50;  // 非常接近支撑
            }
        }
        
        // 接近阻力位 → 反T机会
        if (data.getResistanceLevel() != null) {
            BigDecimal distToResist = data.getResistanceLevel().subtract(price)
                .divide(price, 4, RoundingMode.HALF_UP);
            if (distToResist.abs().compareTo(new BigDecimal("0.005")) < 0) {
                score -= 50;  // 非常接近阻力
            }
        }
        
        return Math.max(-100, Math.min(100, score));
    }
    
    /**
     * 确定方向
     */
    private TDecisionResult determineDirection(int score, AmplitudeAnalysis amp, MarketData data) {
        // 置信度门槛
        if (Math.abs(score) < 40) {
            return TDecisionResult.skip(String.format("综合得分%d，信号不明确", score));
        }
        
        // 基于得分方向 + 自适应止盈止损（基于ATR）
        BigDecimal atr = amp.getAtr20();
        BigDecimal tpPct, slPct;
        
        if (score > 60) {  // 强烈正T信号
            tpPct = atr.multiply(new BigDecimal("1.5")).max(new BigDecimal("0.005"));  // 止盈1.5倍ATR
            slPct = atr.multiply(new BigDecimal("0.8")).max(new BigDecimal("0.002"));  // 止损0.8倍ATR
            
            return TDecisionResult.builder()
                .shouldTrade(true)
                .direction(TDirection.LONG_T)
                .confidence(score)
                .tpPercent(tpPct)
                .slPercent(slPct)
                .reason(String.format("强烈正T信号：得分%d，振幅%s，RSI%s，ATR自适应止盈%s止损%s",
                    score, amp.getVolatilityLevel(), data.getRsi(),
                    tpPct.multiply(new BigDecimal("100")).setScale(2),
                    slPct.multiply(new BigDecimal("100")).setScale(2)))
                .build();
                
        } else if (score < -60) {  // 强烈反T信号
            tpPct = atr.multiply(new BigDecimal("1.5")).max(new BigDecimal("0.005"));
            slPct = atr.multiply(new BigDecimal("0.8")).max(new BigDecimal("0.002"));
            
            return TDecisionResult.builder()
                .shouldTrade(true)
                .direction(TDirection.SHORT_T)
                .confidence(score)
                .tpPercent(tpPct)
                .slPercent(slPct)
                .reason(String.format("强烈反T信号：得分%d，振幅%s，位置%d%%，ATR自适应止盈%s止损%s",
                    score, amp.getVolatilityLevel(), amp.getPositionInRange(),
                    tpPct.multiply(new BigDecimal("100")).setScale(2),
                    slPct.multiply(new BigDecimal("100")).setScale(2)))
                .build();
        }
        
        return TDecisionResult.skip(String.format("得分%d，未达交易门槛（±60）", score));
    }
    
    // ==================== 工具方法 ====================
    
    private BigDecimal calculateMA(List<PriceBar> bars, int period, int endIdx) {
        if (endIdx < period - 1) return bars.get(endIdx).getClose();
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = endIdx - period + 1; i <= endIdx; i++) {
            sum = sum.add(bars.get(i).getClose());
        }
        return sum.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);
    }
    
    private boolean isBullishDivergence(MarketData data) {
        // 简化：价格新低，RSI未新低
        // 实际需要比较多个点
        return false;
    }
    
    private boolean isBearishDivergence(MarketData data) {
        return false;
    }
    
    // ==================== 数据类 ====================
    
    public enum TDirection {
        LONG_T, SHORT_T
    }
    
    @Data
    @Builder
    public static class TDecisionResult {
        private boolean shouldTrade;
        private TDirection direction;
        private int confidence;
        private BigDecimal tpPercent;  // 止盈百分比
        private BigDecimal slPercent;  // 止损百分比
        private String reason;
        
        public static TDecisionResult skip(String reason) {
            return TDecisionResult.builder()
                .shouldTrade(false)
                .reason(reason)
                .build();
        }
    }
}
