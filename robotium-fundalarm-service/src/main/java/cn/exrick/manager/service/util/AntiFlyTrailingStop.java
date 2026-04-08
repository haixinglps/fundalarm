package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 防卖飞移动止盈策略 - 解决卖飞后成本变高问题
 * 
 * 核心改进：
 * 1. 【分批止盈】盈利>5%卖30%，>10%再卖30%，留40%让利润奔跑
 * 2. 【阶梯移动止盈】不同批次不同回撤容忍（留的仓位容忍更大回撤）
 * 3. 【卖飞买回机制】卖飞后在更高位置做反T，降低成本增幅
 * 4. 【趋势判断】趋势强时放宽回撤，趋势弱时收紧
 */
@Slf4j
@Component
public class AntiFlyTrailingStop {

    // ==================== 分批止盈参数 ====================
    
    /** 第一批：盈利>5%，卖30% */
    private static final BigDecimal BATCH1_PROFIT = new BigDecimal("0.05");
    private static final BigDecimal BATCH1_RATIO = new BigDecimal("0.30");
    
    /** 第二批：盈利>10%，再卖30% */
    private static final BigDecimal BATCH2_PROFIT = new BigDecimal("0.10");
    private static final BigDecimal BATCH2_RATIO = new BigDecimal("0.30");
    
    /** 剩余40%：使用更宽松的移动止盈 */
    private static final BigDecimal REMAINING_TRAILING = new BigDecimal("0.05"); // 回撤5%
    
    /** 原单批移动止盈：回撤2%（用于第一批第二批） */
    private static final BigDecimal NORMAL_TRAILING = new BigDecimal("0.02");
    
    // ==================== 趋势自适应 ====================
    
    /** ADX阈值 */
    private static final BigDecimal ADX_STRONG = new BigDecimal("30");
    private static final BigDecimal ADX_WEAK = new BigDecimal("20");
    
    /**
     * 根据趋势强度调整回撤容忍
     */
    public BigDecimal getAdaptiveTrailing(BigDecimal baseTrailing, BigDecimal adx) {
        if (adx == null) return baseTrailing;
        
        if (adx.compareTo(ADX_STRONG) > 0) {
            // 趋势强，放宽回撤容忍（让利润奔跑）
            return baseTrailing.multiply(new BigDecimal("1.5")); // 3%或7.5%
        } else if (adx.compareTo(ADX_WEAK) < 0) {
            // 趋势弱，收紧回撤容忍（保护利润）
            return baseTrailing.multiply(new BigDecimal("0.8")); // 1.6%或4%
        }
        return baseTrailing;
    }
    
    // ==================== 分批移动止盈管理 ====================
    
    @Data
    @Builder
    public static class BatchPosition {
        private String symbol;
        private Integer level;
        private BigDecimal cost;
        
        // 分批状态
        private BigDecimal originalQty;     // 原始数量
        private BigDecimal remainingQty;    // 剩余数量
        private boolean batch1Exited;       // 第一批是否已卖出
        private boolean batch2Exited;       // 第二批是否已卖出
        
        // 各批最高价
        private BigDecimal batch1High;      // 第一批卖出后的最高价（用于剩余仓位）
        private BigDecimal batch2High;      // 第二批卖出后的最高价
        private BigDecimal remainingHigh;   // 剩余仓位的最高价
        
        /**
         * 更新最高价
         */
        public void updateHigh(BigDecimal price) {
            if (batch1High == null || price.compareTo(batch1High) > 0) {
                batch1High = price;
            }
            if (batch2High == null || price.compareTo(batch2High) > 0) {
                batch2High = price;
            }
            if (remainingHigh == null || price.compareTo(remainingHigh) > 0) {
                remainingHigh = price;
            }
        }
        
        /**
         * 当前盈利百分比
         */
        public BigDecimal getProfitPct(BigDecimal currentPrice) {
            return currentPrice.subtract(cost).divide(cost, 6, RoundingMode.HALF_UP);
        }
    }
    
    /**
     * 检查分批止盈
     */
    public BatchDecision checkBatchExit(BatchPosition pos, BigDecimal currentPrice, BigDecimal adx) {
        BigDecimal profitPct = pos.getProfitPct(currentPrice);
        pos.updateHigh(currentPrice);
        
        // ========== 第一批止盈：盈利>5%，卖30% ==========
        if (!pos.isBatch1Exited() && profitPct.compareTo(BATCH1_PROFIT) >= 0) {
            BigDecimal sellQty = pos.getOriginalQty().multiply(BATCH1_RATIO);
            pos.setRemainingQty(pos.getRemainingQty().subtract(sellQty));
            pos.setBatch1Exited(true);
            
            return BatchDecision.builder()
                .action(BatchAction.BATCH1_EXIT)
                .sellQty(sellQty)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("🥉 第一批止盈：盈利%.2f%%，卖%.0f%%，锁定部分利润",
                    profitPct.multiply(new BigDecimal("100")),
                    BATCH1_RATIO.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // ========== 第二批止盈：盈利>10%，再卖30% ==========
        if (pos.isBatch1Exited() && !pos.isBatch2Exited() && 
            profitPct.compareTo(BATCH2_PROFIT) >= 0) {
            
            BigDecimal sellQty = pos.getOriginalQty().multiply(BATCH2_RATIO);
            pos.setRemainingQty(pos.getRemainingQty().subtract(sellQty));
            pos.setBatch2Exited(true);
            
            return BatchDecision.builder()
                .action(BatchAction.BATCH2_EXIT)
                .sellQty(sellQty)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("🥈 第二批止盈：盈利%.2f%%，再卖%.0f%%，累计卖%.0f%%",
                    profitPct.multiply(new BigDecimal("100")),
                    BATCH2_RATIO.multiply(new BigDecimal("100")),
                    BATCH1_RATIO.add(BATCH2_RATIO).multiply(new BigDecimal("100"))))
                .build();
        }
        
        // ========== 剩余仓位移动止盈（防卖飞关键）==========
        if (pos.isBatch2Exited() && pos.getRemainingQty().compareTo(BigDecimal.ZERO) > 0) {
            // 剩余仓位使用更宽松的回撤（5%），且根据趋势调整
            BigDecimal trailingPct = getAdaptiveTrailing(REMAINING_TRAILING, adx);
            BigDecimal trailingPrice = pos.getRemainingHigh().multiply(
                BigDecimal.ONE.subtract(trailingPct));
            
            if (currentPrice.compareTo(trailingPrice) <= 0) {
                return BatchDecision.builder()
                    .action(BatchAction.REMAINING_EXIT)
                    .sellQty(pos.getRemainingQty())
                    .remainingQty(BigDecimal.ZERO)
                    .reason(String.format("🥇 剩余仓位移动止盈：从高点%s回撤%.2f%%至%s，盈利%.2f%%",
                        pos.getRemainingHigh(),
                        trailingPct.multiply(new BigDecimal("100")),
                        currentPrice,
                        profitPct.multiply(new BigDecimal("100"))))
                    .build();
            }
            
            // 持有剩余仓位，让利润奔跑
            return BatchDecision.builder()
                .action(BatchAction.HOLD_REMAINING)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("🚀 剩余仓位持有中：盈利%.2f%%，高点%s，回撤线%s（容忍%.2f%%）",
                    profitPct.multiply(new BigDecimal("100")),
                    pos.getRemainingHigh(),
                    trailingPrice,
                    trailingPct.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // ========== 前两批的移动止盈（2%回撤）==========
        if (pos.isBatch1Exited() && !pos.isBatch2Exited()) {
            BigDecimal trailingPrice = pos.getBatch1High().multiply(
                new BigDecimal("0.98")); // 2%回撤
            
            if (currentPrice.compareTo(trailingPrice) <= 0 && 
                profitPct.compareTo(BATCH2_PROFIT) < 0) {
                // 盈利没到10%，但回撤了，触发第二批提前卖出
                BigDecimal sellQty = pos.getOriginalQty().multiply(BATCH2_RATIO);
                pos.setRemainingQty(pos.getRemainingQty().subtract(sellQty));
                pos.setBatch2Exited(true);
                
                return BatchDecision.builder()
                    .action(BatchAction.BATCH2_EARLY_EXIT)
                    .sellQty(sellQty)
                    .remainingQty(pos.getRemainingQty())
                    .reason(String.format("第二批提前止盈：从高点%s回撤2%%，盈利%.2f%%",
                        pos.getBatch1High(), profitPct.multiply(new BigDecimal("100"))))
                    .build();
            }
        }
        
        return BatchDecision.builder()
            .action(BatchAction.HOLD)
            .remainingQty(pos.getRemainingQty())
            .reason(String.format("持有中：盈利%.2f%%", profitPct.multiply(new BigDecimal("100"))))
            .build();
    }
    
    // ==================== 卖飞买回策略 ====================
    
    /**
     * 卖飞后的买回策略（降低新成本）
     * 
     * 场景：成本100，在110卖出（移动止盈），价格涨到120
     * 问题：想买回，成本120，比原来高20%
     * 解决：在120做反T（卖出其他仓位或借币卖出，在回调时买回）
     */
    public BuyBackStrategy calculateBuyBack(String symbol, BigDecimal originalCost,
                                             BigDecimal sellPrice, BigDecimal currentPrice,
                                             BigDecimal totalQty) {
        
        BigDecimal costIncrease = currentPrice.subtract(originalCost)
            .divide(originalCost, 6, RoundingMode.HALF_UP);
        
        if (costIncrease.compareTo(new BigDecimal("0.05")) < 0) {
            // 涨幅<5%，直接买回，成本增加不多
            return BuyBackStrategy.builder()
                .shouldBuyBack(true)
                .immediate(true)
                .targetPrice(currentPrice)
                .reason(String.format("涨幅%.2f%%<5%%，直接买回，成本从%s增至%s",
                    costIncrease.multiply(new BigDecimal("100")),
                    originalCost, currentPrice))
                .build();
        }
        
        // 涨幅>5%，采用做T策略买回
        // 在高位做反T，等回调买回
        BigDecimal targetBuy = currentPrice.multiply(new BigDecimal("0.97")); // 目标回调3%买回
        
        return BuyBackStrategy.builder()
            .shouldBuyBack(false) // 不立即买回
            .immediate(false)
            .doTFirst(true)
            .targetPrice(targetBuy)
            .tSellPrice(currentPrice)
            .estimatedTProfit(currentPrice.subtract(targetBuy))
            .reason(String.format("⚠️ 卖飞！成本%s→现价%s（+%.2f%%），建议做反T在%s买回，降低实际成本",
                originalCost, currentPrice, costIncrease.multiply(new BigDecimal("100")), targetBuy))
            .build();
    }
    
    // ==================== 数据类 ====================
    
    public enum BatchAction {
        BATCH1_EXIT,        // 第一批止盈（30%）
        BATCH2_EXIT,        // 第二批止盈（30%）
        BATCH2_EARLY_EXIT,  // 第二批提前止盈（回撤触发）
        REMAINING_EXIT,     // 剩余仓位移动止盈（40%）
        HOLD_REMAINING,     // 持有剩余仓位
        HOLD                // 持有
    }
    
    @Data
    @Builder
    public static class BatchDecision {
        private BatchAction action;
        private BigDecimal sellQty;
        private BigDecimal remainingQty;
        private String reason;
    }
    
    @Data
    @Builder
    public static class BuyBackStrategy {
        private boolean shouldBuyBack;
        private boolean immediate;      // 是否立即买回
        private boolean doTFirst;       // 是否先做T
        private BigDecimal targetPrice; // 目标买回价
        private BigDecimal tSellPrice;  // 做T卖出价
        private BigDecimal estimatedTProfit; // 预估T利润
        private String reason;
    }
}
