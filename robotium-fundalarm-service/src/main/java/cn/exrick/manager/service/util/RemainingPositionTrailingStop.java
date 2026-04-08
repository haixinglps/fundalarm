package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 新仓剩余仓位移动止盈管理
 * 
 * 场景：新仓分批卖出后，剩余仓位如何管理？
 * 
 * 示例流程：
 * 1. 建仓@100（1个币）
 * 2. 回本@100.2 → 卖0.3个，剩0.7个（转老仓）
 * 3. 盈利1%@101 → 卖0.2个，剩0.5个（已是老仓）
 * 4. 剩余0.5个 → 启用移动止盈
 * 
 * 移动止盈规则：
 * - 盈利>3%开启移动止盈
 * - 从最高点回撤2%卖出剩余全部
 * - 或盈利>10%分批卖半仓
 */
@Slf4j
@Component
public class RemainingPositionTrailingStop {

    /** 移动止盈触发：盈利>3% */
    private static final BigDecimal TRAILING_TRIGGER = new BigDecimal("0.03");
    /** 移动止盈回撤：2% */
    private static final BigDecimal TRAILING_PCT = new BigDecimal("0.02");
    /** 分批止盈：盈利>10%卖半仓 */
    private static final BigDecimal TAKE_HALF_PROFIT = new BigDecimal("0.10");
    
    @Data
    @Builder
    public static class RemainingPosition {
        private String symbol;
        private Integer level;
        private BigDecimal originalQty;     // 原始建仓数量
        private BigDecimal remainingQty;    // 剩余数量（分批卖出后的）
        private BigDecimal cost;            // 成本价
        private BigDecimal highestPrice;    // 最高价（移动止盈用）
        private boolean trailingActive;     // 移动止盈是否已激活
        private boolean halfExited;         // 是否已卖半仓
        
        /**
         * 当前盈利百分比
         */
        public BigDecimal getProfitPct(BigDecimal currentPrice) {
            return currentPrice.subtract(cost).divide(cost, 6, RoundingMode.HALF_UP);
        }
        
        /**
         * 更新最高价
         */
        public void updateHighest(BigDecimal price) {
            if (highestPrice == null || price.compareTo(highestPrice) > 0) {
                highestPrice = price;
            }
        }
        
        /**
         * 计算移动止盈线
         */
        public BigDecimal getTrailingStopPrice() {
            if (highestPrice == null) return null;
            return highestPrice.multiply(BigDecimal.ONE.subtract(TRAILING_PCT));
        }
    }
    
    /**
     * 管理剩余仓位（新仓分批卖出后的）
     */
    public TrailingDecision manageRemaining(RemainingPosition pos, BigDecimal currentPrice) {
        BigDecimal profitPct = pos.getProfitPct(currentPrice);
        
        // 更新最高价
        pos.updateHighest(currentPrice);
        
        // ========== 1. 分批止盈：盈利>10%，卖半仓 ==========
        if (!pos.isHalfExited() && profitPct.compareTo(TAKE_HALF_PROFIT) >= 0) {
            BigDecimal sellHalf = pos.getRemainingQty().multiply(new BigDecimal("0.5"));
            pos.setRemainingQty(pos.getRemainingQty().subtract(sellHalf));
            pos.setHalfExited(true);
            
            return TrailingDecision.builder()
                .action(TrailingAction.TAKE_HALF)
                .sellQty(sellHalf)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("🚀 剩余仓位盈利%.2f%%！卖半仓锁定利润，留%.4f继续飞",
                    profitPct.multiply(new BigDecimal("100")), pos.getRemainingQty()))
                .build();
        }
        
        // ========== 2. 激活移动止盈：盈利>3% ==========
        if (!pos.isTrailingActive() && profitPct.compareTo(TRAILING_TRIGGER) >= 0) {
            pos.setTrailingActive(true);
            BigDecimal trailingPrice = pos.getTrailingStopPrice();
            
            return TrailingDecision.builder()
                .action(TrailingAction.TRAILING_ACTIVE)
                .remainingQty(pos.getRemainingQty())
                .trailingPrice(trailingPrice)
                .reason(String.format("✅ 移动止盈激活！最高点%s，回撤线%s（回撤%.0f%%）",
                    pos.getHighestPrice(), trailingPrice,
                    TRAILING_PCT.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // ========== 3. 触发移动止盈：回撤2% ==========
        if (pos.isTrailingActive()) {
            BigDecimal trailingPrice = pos.getTrailingStopPrice();
            if (currentPrice.compareTo(trailingPrice) <= 0) {
                return TrailingDecision.builder()
                    .action(TrailingAction.TRAILING_EXIT)
                    .sellQty(pos.getRemainingQty())
                    .remainingQty(BigDecimal.ZERO)
                    .reason(String.format("💰 移动止盈触发！从高点%s回撤%.0f%%至%s，盈利%.2f%%",
                        pos.getHighestPrice(),
                        TRAILING_PCT.multiply(new BigDecimal("100")),
                        currentPrice,
                        profitPct.multiply(new BigDecimal("100"))))
                    .build();
            }
        }
        
        // ========== 4. 持有中 ==========
        String status = pos.isTrailingActive() ? 
            String.format("移动止盈中，高点%s，回撤线%s", 
                pos.getHighestPrice(), pos.getTrailingStopPrice()) :
            String.format("盈利%.2f%%，未达%.0f%%激活线", 
                profitPct.multiply(new BigDecimal("100")),
                TRAILING_TRIGGER.multiply(new BigDecimal("100")));
        
        return TrailingDecision.builder()
            .action(TrailingAction.HOLD)
            .remainingQty(pos.getRemainingQty())
            .reason(status)
            .build();
    }
    
    /**
     * 创建剩余仓位（新仓分批卖出后调用）
     */
    public RemainingPosition createFromNewPosition(String symbol, Integer level,
                                                     BigDecimal originalQty,
                                                     BigDecimal remainingQty,
                                                     BigDecimal cost) {
        return RemainingPosition.builder()
            .symbol(symbol)
            .level(level)
            .originalQty(originalQty)
            .remainingQty(remainingQty)
            .cost(cost)
            .highestPrice(cost)  // 初始最高价为成本
            .trailingActive(false)
            .halfExited(false)
            .build();
    }
    
    // ==================== 数据类 ====================
    
    public enum TrailingAction {
        TAKE_HALF,        // 分批卖半仓（盈利10%）
        TRAILING_ACTIVE,  // 移动止盈激活（盈利3%）
        TRAILING_EXIT,    // 移动止盈触发（回撤2%）
        HOLD              // 持有中
    }
    
    @Data
    @Builder
    public static class TrailingDecision {
        private TrailingAction action;
        private BigDecimal sellQty;       // 本次卖出数量
        private BigDecimal remainingQty;  // 剩余数量
        private BigDecimal trailingPrice; // 移动止盈线
        private String reason;
    }
}
