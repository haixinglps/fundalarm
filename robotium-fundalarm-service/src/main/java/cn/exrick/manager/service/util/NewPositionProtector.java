package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 新仓保护系统 - 防止新建仓变成套牢单
 * 
 * 核心问题：刚建仓，市场涨了又回调，新仓怎么办？
 * 
 * 策略：
 * 1. 【新仓定义】建仓后4小时内算新仓
 * 2. 【保本优先】新仓任何盈利回调，先保本出（至少不亏）
 * 3. 【微利也走】新仓盈利>0.5%后回撤0.3%，止盈
 * 4. 【时间保护】新仓最长持有8小时，到点必出
 * 5. 【转为老仓】4小时后按正常策略（移动止盈）
 */
@Slf4j
@Component
public class NewPositionProtector {

    // ==================== 新仓定义 ====================
    
    /** 新仓时间：4小时 */
    private static final int NEW_POSITION_HOURS = 4;
    /** 新仓最长持有：8小时 */
    private static final int MAX_HOLD_HOURS = 8;
    
    // ==================== 新仓止盈参数 ====================
    
    /** 保本线：成本+0.15%（覆盖手续费） */
    private static final BigDecimal BREAKEVEN_BUFFER = new BigDecimal("1.0015");
    /** 微利止盈：盈利0.5%后回撤0.3% */
    private static final BigDecimal PROFIT_TARGET = new BigDecimal("0.005");
    private static final BigDecimal PROFIT_PULLBACK = new BigDecimal("0.003");
    /** 硬止损：-1%（新仓也不深套） */
    private static final BigDecimal NEW_STOP_LOSS = new BigDecimal("0.01");
    
    // ==================== 状态 ====================
    
    @Data
    @Builder
    public static class Position {
        private Integer level;
        private BigDecimal qty;
        private BigDecimal cost;
        private long buildTime;
        private BigDecimal highestPrice;
        private boolean isNew;  // 是否新仓
        
        /**
         * 持仓小时数
         */
        public int getHoldHours() {
            return (int) (System.currentTimeMillis() - buildTime) / (1000 * 60 * 60);
        }
        
        /**
         * 是否新仓（4小时内）
         */
        public boolean isNewPosition() {
            return getHoldHours() < NEW_POSITION_HOURS;
        }
        
        /**
         * 更新最高价
         */
        public void updateHigh(BigDecimal price) {
            if (highestPrice == null || price.compareTo(highestPrice) > 0) {
                highestPrice = price;
            }
        }
    }
    
    // ==================== 新仓保护决策 ====================
    
    /**
     * 检查新仓是否需要保护性止盈
     * 
     * @param pos 仓位
     * @param currentPrice 当前价
     * @return 操作建议
     */
    public ProtectDecision checkNewPosition(Position pos, BigDecimal currentPrice) {
        int holdHours = pos.getHoldHours();
        BigDecimal cost = pos.getCost();
        BigDecimal breakeven = cost.multiply(BREAKEVEN_BUFFER);
        
        // 更新最高价
        pos.updateHigh(currentPrice);
        
        // ========== 1. 硬止损（新仓也设-1%）==========
        BigDecimal stopPrice = cost.multiply(new BigDecimal("0.99"));
        if (currentPrice.compareTo(stopPrice) <= 0) {
            return ProtectDecision.builder()
                .shouldSell(true)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("新仓硬止损：建仓%.2f，当前%.2f，跌幅%.2f%%",
                    cost, currentPrice, new BigDecimal("0.01").multiply(new BigDecimal("100"))))
                .type(ProtectType.NEW_STOP)
                .build();
        }
        
        // ========== 2. 时间强制出场（8小时）==========
        if (holdHours >= MAX_HOLD_HOURS) {
            BigDecimal pnl = currentPrice.subtract(cost).divide(cost, 6, RoundingMode.HALF_UP);
            String pnlStr = pnl.compareTo(BigDecimal.ZERO) > 0 ? 
                "盈利" + pnl.multiply(new BigDecimal("100")).setScale(2) + "%" : 
                "亏损" + pnl.abs().multiply(new BigDecimal("100")).setScale(2) + "%";
            
            return ProtectDecision.builder()
                .shouldSell(true)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("新仓时间到%d小时，%s，强制出场", MAX_HOLD_HOURS, pnlStr))
                .type(ProtectType.TIME_EXIT)
                .build();
        }
        
        // ========== 3. 新仓特殊保护（4小时内）==========
        if (pos.isNewPosition()) {
            // 3.1 先回到成本附近，保本出（防止变亏损）
            if (pos.getHighestPrice() != null && 
                pos.getHighestPrice().compareTo(breakeven.multiply(new BigDecimal("1.003"))) > 0 &&
                currentPrice.compareTo(breakeven) <= 0) {
                
                return ProtectDecision.builder()
                    .shouldSell(true)
                    .ratio(BigDecimal.ONE)
                    .price(currentPrice)
                    .reason(String.format("🟡 新仓%s小时：曾盈利，现回到成本%.2f，保本出防套牢",
                        holdHours, breakeven))
                    .type(ProtectType.BREAKEVEN_EXIT)
                    .build();
            }
            
            // 3.2 盈利0.5%后回撤0.3%，微利止盈
            if (pos.getHighestPrice() != null) {
                BigDecimal profitPct = pos.getHighestPrice().subtract(cost).divide(cost, 6, RoundingMode.HALF_UP);
                BigDecimal pullbackPrice = pos.getHighestPrice().multiply(new BigDecimal("0.997"));
                
                if (profitPct.compareTo(PROFIT_TARGET) >= 0 && 
                    currentPrice.compareTo(pullbackPrice) <= 0) {
                    
                    return ProtectDecision.builder()
                        .shouldSell(true)
                        .ratio(BigDecimal.ONE)
                        .price(currentPrice)
                        .reason(String.format("🟢 新仓%s小时：盈利%.2f%%后回撤0.3%%，微利止盈",
                            holdHours, profitPct.multiply(new BigDecimal("100"))))
                        .type(ProtectType.PROFIT_PULLBACK)
                        .build();
                }
            }
            
            // 3.3 新仓持有中，提示
            return ProtectDecision.builder()
                .shouldSell(false)
                .reason(String.format("新仓%s小时：成本%.2f，当前%.2f，最高%.2f，保本线%.2f",
                    holdHours, cost, currentPrice, 
                    pos.getHighestPrice() != null ? pos.getHighestPrice() : cost,
                    breakeven))
                .build();
        }
        
        // ========== 4. 转为老仓（4小时后），正常移动止盈 ==========
        return checkOldPosition(pos, currentPrice, holdHours);
    }
    
    /**
     * 老仓（4小时以上）用正常移动止盈
     */
    private ProtectDecision checkOldPosition(Position pos, BigDecimal currentPrice, int holdHours) {
        BigDecimal cost = pos.getCost();
        
        // 盈利超2%，移动止盈回撤2%
        if (pos.getHighestPrice() != null) {
            BigDecimal maxProfit = pos.getHighestPrice().subtract(cost).divide(cost, 6, RoundingMode.HALF_UP);
            BigDecimal trailingPrice = pos.getHighestPrice().multiply(new BigDecimal("0.98"));
            
            if (maxProfit.compareTo(new BigDecimal("0.02")) >= 0 && 
                currentPrice.compareTo(trailingPrice) <= 0) {
                
                return ProtectDecision.builder()
                    .shouldSell(true)
                    .ratio(BigDecimal.ONE)
                    .price(currentPrice)
                    .reason(String.format("老仓%s小时：从高点%.2f回撤2%%，移动止盈",
                        holdHours, pos.getHighestPrice()))
                    .type(ProtectType.TRAILING_STOP)
                    .build();
            }
        }
        
        return ProtectDecision.builder()
            .shouldSell(false)
            .reason(String.format("老仓%s小时：持有中，最高%.2f，当前%.2f",
                holdHours, pos.getHighestPrice(), currentPrice))
            .build();
    }
    
    /**
     * 获取新仓保护建议（人性化提示）
     */
    public String getProtectionAdvice(Position pos, BigDecimal currentPrice) {
        if (!pos.isNewPosition()) return "已转为老仓，使用移动止盈策略";
        
        int hours = pos.getHoldHours();
        BigDecimal breakeven = pos.getCost().multiply(BREAKEVEN_BUFFER);
        BigDecimal profitTarget = pos.getCost().multiply(new BigDecimal("1.005"));
        BigDecimal stopPrice = pos.getCost().multiply(new BigDecimal("0.99"));
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("【新仓%s小时保护】\n", hours));
        sb.append(String.format("成本: %.2f\n", pos.getCost()));
        sb.append(String.format("现价: %.2f\n", currentPrice));
        sb.append(String.format("保本出: ≥%.2f\n", breakeven));
        sb.append(String.format("微利目标: ≥%.2f (盈利0.5%%后回撤0.3%%止盈)\n", profitTarget));
        sb.append(String.format("硬止损: ≤%.2f (-1%%)\n", stopPrice));
        sb.append(String.format("时间限制: 最长%d小时\n", MAX_HOLD_HOURS));
        
        // 当前建议
        if (currentPrice.compareTo(stopPrice) <= 0) {
            sb.append("⚠️ 建议：立即止损！");
        } else if (pos.getHighestPrice() != null && 
                   pos.getHighestPrice().compareTo(profitTarget) > 0 &&
                   currentPrice.compareTo(pos.getHighestPrice().multiply(new BigDecimal("0.997"))) <= 0) {
            sb.append("✅ 建议：微利止盈！");
        } else if (currentPrice.compareTo(breakeven) > 0 && currentPrice.compareTo(pos.getCost()) < 1.002) {
            sb.append("💡 建议：接近成本，可保本出防套牢");
        } else {
            sb.append("⏳ 建议：持有观察");
        }
        
        return sb.toString();
    }
    
    // ==================== 数据类 ====================
    
    public enum ProtectType {
        NEW_STOP,       // 新仓硬止损
        TIME_EXIT,      // 时间强制出场
        BREAKEVEN_EXIT, // 保本出
        PROFIT_PULLBACK,// 微利回撤止盈
        TRAILING_STOP,  // 老仓移动止盈
        HOLD            // 持有
    }
    
    @Data
    @Builder
    public static class ProtectDecision {
        private boolean shouldSell;
        private BigDecimal ratio;
        private BigDecimal price;
        private String reason;
        private ProtectType type;
        
        public static ProtectDecision hold(String reason) {
            return ProtectDecision.builder()
                .shouldSell(false)
                .type(ProtectType.HOLD)
                .reason(reason)
                .build();
        }
    }
}
