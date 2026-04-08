package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 底仓不止损策略 - 通过做T而非止损来应对下跌
 * 
 * 核心原则：
 * 1. 【底仓不割】无论跌多少，底仓坚决不止损，止损=永久损失
 * 2. 【下跌补仓】每跌2%补一次仓，摊薄成本（不是做T，是真补仓）
 * 3. 【反弹做T】反弹时用做T降成本，不增加仓位
 * 4. 【时间换空间】深套时躺平，等周期轮回
 * 5. 【强制清仓】只有极端情况（跌20%+且趋势恶化）才考虑清仓
 */
@Slf4j
@Component
public class NoStopLossBaseStrategy {

    // ==================== 底仓坚守参数 ====================
    
    /** 底仓最大回撤容忍：20%（极端情况才考虑） */
    private static final BigDecimal MAX_DRAWBACK = new BigDecimal("0.20");
    /** 补仓触发：每跌2% */
    private static final BigDecimal ADD_POSITION_DROP = new BigDecimal("0.02");
    /** 补仓金额：可用资金的20% */
    private static final BigDecimal ADD_POSITION_RATIO = new BigDecimal("0.20");
    /** 最大补仓次数：5次（最多跌到10%补仓完毕） */
    private static final int MAX_ADD_TIMES = 5;
    
    // ==================== 做T降本参数 ====================
    
    /** 做T触发：反弹0.5% */
    private static final BigDecimal T_TRIGGER_BOUNCE = new BigDecimal("0.005");
    /** 做T止盈：0.3% */
    private static final BigDecimal T_TAKE_PROFIT = new BigDecimal("0.003");
    /** 做T仓位：底仓的10% */
    private static final BigDecimal T_POSITION_RATIO = new BigDecimal("0.10");
    
    // ==================== 状态管理 ====================
    
    @Data
    public static class BasePosition {
        private String symbol;
        private BigDecimal totalQty;        // 总持仓
        private BigDecimal avgCost;         // 平均成本
        private BigDecimal totalInvested;   // 总投入
        private int addTimes = 0;           // 已补仓次数
        private BigDecimal highestCost;     // 最高成本（用于对比降本效果）
        private BigDecimal lastAddPrice;    // 上次补仓价格
        private boolean isExtreme = false;  // 是否极端情况
        
        /**
         * 计算盈亏平衡价
         */
        public BigDecimal getBreakevenPrice() {
            return avgCost.multiply(new BigDecimal("1.0015")); // 含手续费
        }
        
        /**
         * 计算当前回撤
         */
        public BigDecimal getDrawback(BigDecimal currentPrice) {
            return avgCost.subtract(currentPrice).divide(avgCost, 6, RoundingMode.HALF_UP);
        }
        
        /**
         * 补仓
         */
        public void addPosition(BigDecimal price, BigDecimal amount) {
            BigDecimal newCost = totalQty.multiply(avgCost).add(price.multiply(amount));
            totalQty = totalQty.add(amount);
            avgCost = newCost.divide(totalQty, 8, RoundingMode.HALF_UP);
            addTimes++;
            lastAddPrice = price;
            totalInvested = totalInvested.add(price.multiply(amount));
            
            log.info("【补仓】{} 第{}次补仓 价{} 量{} 新成本{} 总投入{}", 
                symbol, addTimes, price, amount, avgCost, totalInvested);
        }
        
        /**
         * 做T降本（卖出后买回，或买入后卖出）
         */
        public void reduceCostByT(BigDecimal tProfit, BigDecimal tQty) {
            // T利润分摊到总持仓，降低平均成本
            BigDecimal costReduction = tProfit.divide(totalQty, 8, RoundingMode.HALF_UP);
            avgCost = avgCost.subtract(costReduction);
            
            log.info("【做T降本】{} 本次T利润{} 分摊{}份 每份降本{} 新成本{}", 
                symbol, tProfit, totalQty, costReduction, avgCost);
        }
    }
    
    // ==================== 核心策略 ====================
    
    /**
     * 底仓管理决策
     * 
     * @param position 底仓状态
     * @param currentPrice 当前价
     * @param availableCash 可用现金
     * @param availableQty 可卖数量
     * @return 操作建议
     */
    public BaseDecision manage(BasePosition position, BigDecimal currentPrice,
                                BigDecimal availableCash, BigDecimal availableQty) {
        
        BigDecimal drawback = position.getDrawback(currentPrice);
        BigDecimal breakeven = position.getBreakevenPrice();
        
        // ========== 1. 极端情况检查（跌20%+）==========
        if (drawback.compareTo(MAX_DRAWBACK) >= 0) {
            position.setExtreme(true);
            return BaseDecision.builder()
                .action(BaseAction.EXTREME_ALERT)
                .reason(String.format("极端情况：回撤%.2f%%，建议人工干预", 
                    drawback.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // ========== 2. 补仓判断（下跌时）==========
        // 每跌2%且距离上次补仓也跌了2%，才补仓
        if (position.getLastAddPrice() == null || 
            currentPrice.compareTo(position.getLastAddPrice().multiply(
                new BigDecimal("0.98"))) <= 0) {
            
            if (position.getAddTimes() < MAX_ADD_TIMES && 
                availableCash.compareTo(BigDecimal.ZERO) > 0) {
                
                BigDecimal addAmount = availableCash.multiply(ADD_POSITION_RATIO)
                    .divide(currentPrice, 8, RoundingMode.DOWN);
                
                return BaseDecision.builder()
                    .action(BaseAction.ADD_POSITION)
                    .qty(addAmount)
                    .price(currentPrice)
                    .reason(String.format("下跌%.2f%%，第%d次补仓，摊薄成本",
                        drawback.multiply(new BigDecimal("100")), position.getAddTimes() + 1))
                    .build();
            }
        }
        
        // ========== 3. 做T降本（反弹时）==========
        // 当前价比最低成本高0.5%，适合做反T
        if (position.getLastAddPrice() != null && 
            currentPrice.compareTo(position.getLastAddPrice().multiply(
                new BigDecimal("1.005"))) >= 0 &&
            availableQty.compareTo(position.getTotalQty().multiply(T_POSITION_RATIO)) >= 0) {
            
            BigDecimal tQty = position.getTotalQty().multiply(T_POSITION_RATIO);
            BigDecimal targetBuy = currentPrice.multiply(new BigDecimal("0.997")); // 低0.3%买回
            
            return BaseDecision.builder()
                .action(BaseAction.DO_T)
                .qty(tQty)
                .price(currentPrice)
                .targetPrice(targetBuy)
                .reason(String.format("反弹%.2f%%，做反T降本，卖出%.4f，目标买回%.2f",
                    currentPrice.subtract(position.getLastAddPrice())
                        .divide(position.getLastAddPrice(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")),
                    tQty, targetBuy))
                .build();
        }
        
        // ========== 4. 持有等待 ==========
        return BaseDecision.builder()
            .action(BaseAction.HOLD)
            .reason(String.format("持有中 成本%.2f 现价%.2f 回撤%.2f%% 盈亏平衡%.2f",
                position.getAvgCost(), currentPrice, 
                drawback.multiply(new BigDecimal("100")), breakeven))
            .build();
    }
    
    /**
     * 做T完成后的成本更新
     */
    public void onTCompleted(BasePosition position, BigDecimal sellPrice, 
                             BigDecimal buyBackPrice, BigDecimal qty) {
        if (buyBackPrice == null) {
            // 只卖没买回（不应该发生）
            return;
        }
        
        // 计算T利润
        BigDecimal tProfit = sellPrice.subtract(buyBackPrice).multiply(qty);
        
        // 更新成本
        position.reduceCostByT(tProfit, qty);
        
        log.info("【做T完成】卖出{}@{} 买回{}@{} 利润{} 成本降至{}", 
            qty, sellPrice, qty, buyBackPrice, tProfit, position.getAvgCost());
    }
    
    /**
     * 获取状态报告
     */
    public String getReport(BasePosition position, BigDecimal currentPrice) {
        BigDecimal breakeven = position.getBreakevenPrice();
        BigDecimal currentValue = currentPrice.multiply(position.getTotalQty());
        BigDecimal pnl = currentValue.subtract(position.getTotalInvested());
        BigDecimal needRise = breakeven.subtract(currentPrice).divide(currentPrice, 4, RoundingMode.HALF_UP);
        
        return String.format(
            "【底仓报告】\n" +
            "总投入: %s\n" +
            "总持仓: %s\n" +
            "平均成本: %.2f\n" +
            "当前价格: %.2f\n" +
            "盈亏平衡: %.2f\n" +
            "当前盈亏: %s (%.2f%%)\n" +
            "需涨: %.2f%% 回本\n" +
            "补仓次数: %d/%d\n" +
            "策略: %s",
            position.getTotalInvested(),
            position.getTotalQty(),
            position.getAvgCost(),
            currentPrice,
            breakeven,
            pnl, pnl.divide(position.getTotalInvested(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")),
            needRise.multiply(new BigDecimal("100")),
            position.getAddTimes(), MAX_ADD_TIMES,
            position.isExtreme() ? "⚠️ 极端情况" : "坚守底仓+做T降本"
        );
    }
    
    // ==================== 数据类 ====================
    
    public enum BaseAction {
        HOLD,           // 持有
        ADD_POSITION,   // 补仓
        DO_T,           // 做T
        EXTREME_ALERT   // 极端警报
    }
    
    @Data
    @Builder
    public static class BaseDecision {
        private BaseAction action;
        private BigDecimal qty;
        private BigDecimal price;
        private BigDecimal targetPrice;
        private String reason;
    }
}
