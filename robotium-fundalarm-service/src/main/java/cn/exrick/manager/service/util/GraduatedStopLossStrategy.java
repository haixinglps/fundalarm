package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 分级止损策略 - 根据持仓类型和盈亏状态灵活止损
 * 
 * 核心原则：
 * 1. 【做T单】严格止损 -0.5%，因为做T就是快进快出，不恋战
 * 2. 【底仓单】宽松止损 -3%，给价格回归时间，不轻易止损底仓
 * 3. 【盈利底仓】移动止盈，回撤2%才出，让利润奔跑
 * 4. 【成本附近】只设时间止损（24小时），不设价格止损，防止震荡洗出
 * 5. 【网格补仓】下跌时补仓摊薄成本，而非止损
 */
@Slf4j
@Component
public class GraduatedStopLossStrategy {

    // ==================== 分级止损参数 ====================
    
    /** 【做T单】严格止损：-0.5%（因为做T是超短线，必须快进快出） */
    private static final BigDecimal T_STOP_LOSS = new BigDecimal("0.005");
    
    /** 【底仓单】宽松止损：-3%（给价格回归时间，不轻易止损底仓） */
    private static final BigDecimal BASE_STOP_LOSS = new BigDecimal("0.03");
    
    /** 【底仓单】时间止损：持仓超过48小时且亏损超过1% */
    private static final int BASE_TIME_STOP_HOURS = 48;
    private static final BigDecimal BASE_TIME_STOP_LOSS = new BigDecimal("0.01");
    
    /** 【盈利单】移动止盈回撤：从最高点回撤2% */
    private static final BigDecimal TRAILING_STOP_PCT = new BigDecimal("0.02");
    
    /** 【成本附近】不设价格止损，只设时间止损 */
    private static final BigDecimal NEAR_COST_THRESHOLD = new BigDecimal("0.005"); // ±0.5%算成本附近
    
    // ==================== 止损决策 ====================
    
    /**
     * 分级止损决策
     * 
     * @param isTOrder 是否做T单
     * @param buyPrice 买入价
     * @param currentPrice 当前价
     * @param highestPrice 最高价（移动止盈用）
     * @param holdHours 持仓小时数
     * @param breakevenPrice 盈亏平衡价
     * @return 止损建议
     */
    public StopLossAdvice checkStopLoss(boolean isTOrder, BigDecimal buyPrice,
                                         BigDecimal currentPrice, BigDecimal highestPrice,
                                         int holdHours, BigDecimal breakevenPrice) {
        
        // ========== 1. 做T单：严格止损 -0.5% ==========
        if (isTOrder) {
            return checkTStopLoss(buyPrice, currentPrice, holdHours);
        }
        
        // ========== 2. 底仓单：宽松策略 ==========
        return checkBaseStopLoss(buyPrice, currentPrice, highestPrice, holdHours, breakevenPrice);
    }
    
    /**
     * 做T单止损（严格）
     * 
     * 理由：做T就是套利，不对就砍，绝不让T单变套牢单
     */
    private StopLossAdvice checkTStopLoss(BigDecimal buyPrice, BigDecimal currentPrice, int holdMinutes) {
        BigDecimal lossPct = buyPrice.subtract(currentPrice)
            .divide(buyPrice, 6, RoundingMode.HALF_UP);
        
        // 1. 价格止损 -0.5%
        if (lossPct.compareTo(T_STOP_LOSS) >= 0) {
            return StopLossAdvice.builder()
                .shouldStop(true)
                .price(currentPrice)
                .reason(String.format("【做T止损】跌幅%.2f%%，严格止损-0.5%%，防止T单变套牢",
                    lossPct.multiply(new BigDecimal("100"))))
                .type(StopType.T_PRICE_STOP)
                .build();
        }
        
        // 2. 时间止损 15分钟
        if (holdMinutes >= 15) {
            return StopLossAdvice.builder()
                .shouldStop(true)
                .price(currentPrice)
                .reason("【做T时间止损】持仓15分钟，无论盈亏都出")
                .type(StopType.T_TIME_STOP)
                .build();
        }
        
        return StopLossAdvice.hold();
    }
    
    /**
     * 底仓单止损（宽松，分情况）
     */
    private StopLossAdvice checkBaseStopLoss(BigDecimal buyPrice, BigDecimal currentPrice,
                                              BigDecimal highestPrice, int holdHours,
                                              BigDecimal breakevenPrice) {
        
        BigDecimal lossPct = buyPrice.subtract(currentPrice)
            .divide(buyPrice, 6, RoundingMode.HALF_UP);
        BigDecimal profitPct = currentPrice.subtract(buyPrice)
            .divide(buyPrice, 6, RoundingMode.HALF_UP);
        
        // ========== 情况1：有盈利，用移动止盈 ==========
        if (profitPct.compareTo(new BigDecimal("0.01")) > 0) {
            // 盈利超1%，启用移动止盈
            if (highestPrice != null) {
                BigDecimal trailingPrice = highestPrice.multiply(
                    BigDecimal.ONE.subtract(TRAILING_STOP_PCT));
                
                if (currentPrice.compareTo(trailingPrice) <= 0) {
                    return StopLossAdvice.builder()
                        .shouldStop(true)
                        .price(currentPrice)
                        .reason(String.format("【移动止盈】从高点%.2f回撤%.0f%%至%.2f，盈利%.2f%%落袋",
                            highestPrice, TRAILING_STOP_PCT.multiply(new BigDecimal("100")),
                            currentPrice, profitPct.multiply(new BigDecimal("100"))))
                        .type(StopType.TRAILING_STOP)
                        .build();
                }
            }
            
            // 盈利但不触发移动止盈 → 持有
            return StopLossAdvice.hold();
        }
        
        // ========== 情况2：成本附近（±0.5%），不设价格止损 ==========
        if (lossPct.abs().compareTo(NEAR_COST_THRESHOLD) <= 0) {
            // 只设时间止损：持仓超48小时且还亏着
            if (holdHours >= BASE_TIME_STOP_HOURS && lossPct.compareTo(BigDecimal.ZERO) > 0) {
                return StopLossAdvice.builder()
                    .shouldStop(true)
                    .price(currentPrice)
                    .reason(String.format("【时间止损】持仓%d小时仍微亏%.2f%%，认栽出场",
                        holdHours, lossPct.multiply(new BigDecimal("100"))))
                    .type(StopType.TIME_STOP)
                    .build();
            }
            
            // 成本附近，给时间
            return StopLossAdvice.builder()
                .shouldStop(false)
                .reason(String.format("成本附近（盈亏%.2f%%），不设价格止损，等待反弹",
                    lossPct.multiply(new BigDecimal("100"))))
                .type(StopType.HOLD)
                .build();
        }
        
        // ========== 情况3：亏损但未到止损线，观望 ==========
        if (lossPct.compareTo(BASE_STOP_LOSS) < 0 && lossPct.compareTo(NEAR_COST_THRESHOLD) > 0) {
            return StopLossAdvice.builder()
                .shouldStop(false)
                .reason(String.format("亏损%.2f%%，未达-3%%止损线，观望等待",
                    lossPct.multiply(new BigDecimal("100"))))
                .type(StopType.HOLD)
                .build();
        }
        
        // ========== 情况4：亏损超3%，宽松止损 ==========
        if (lossPct.compareTo(BASE_STOP_LOSS) >= 0) {
            return StopLossAdvice.builder()
                .shouldStop(true)
                .price(currentPrice)
                .reason(String.format("【底仓止损】亏损%.2f%%，触及-3%%止损线，防止深套",
                    lossPct.multiply(new BigDecimal("100"))))
                .type(StopType.BASE_STOP_LOSS)
                .build();
        }
        
        return StopLossAdvice.hold();
    }
    
    /**
     * 补仓建议（替代止损）
     * 
     * 下跌时不止损，而是考虑补仓摊薄成本
     */
    public AddPositionAdvice checkAddPosition(BigDecimal avgCost, BigDecimal currentPrice,
                                               int gridLevel, BigDecimal availableCash) {
        BigDecimal dropPct = avgCost.subtract(currentPrice).divide(avgCost, 6, RoundingMode.HALF_UP);
        
        // 每跌2%补仓一次
        if (dropPct.compareTo(new BigDecimal("0.02")) >= 0 && 
            dropPct.compareTo(new BigDecimal("0.10")) < 0) { // 最多补到跌10%
            
            // 计算补仓金额（可用资金的20%）
            BigDecimal addAmount = availableCash.multiply(new BigDecimal("0.2"));
            
            return AddPositionAdvice.builder()
                .shouldAdd(true)
                .amount(addAmount)
                .price(currentPrice)
                .reason(String.format("价格从成本跌%.2f%%，建议补仓摊薄成本，不买新档",
                    dropPct.multiply(new BigDecimal("100"))))
                .build();
        }
        
        return AddPositionAdvice.builder()
            .shouldAdd(false)
            .reason("跌幅不足2%，不补仓")
            .build();
    }
    
    // ==================== 数据类 ====================
    
    @Data
    @Builder
    public static class StopLossAdvice {
        private boolean shouldStop;
        private BigDecimal price;
        private String reason;
        private StopType type;
        
        public static StopLossAdvice hold() {
            return StopLossAdvice.builder()
                .shouldStop(false)
                .type(StopType.HOLD)
                .build();
        }
    }
    
    @Data
    @Builder
    public static class AddPositionAdvice {
        private boolean shouldAdd;
        private BigDecimal amount;
        private BigDecimal price;
        private String reason;
    }
    
    public enum StopType {
        HOLD,           // 持有
        T_PRICE_STOP,   // 做T价格止损（-0.5%）
        T_TIME_STOP,    // 做T时间止损（15分钟）
        TRAILING_STOP,  // 移动止盈（回撤2%）
        TIME_STOP,      // 时间止损（48小时）
        BASE_STOP_LOSS  // 底仓止损（-3%）
    }
}
