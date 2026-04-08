package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 灵活仓位管理 - 解决新仓卖飞和转老仓矛盾
 * 
 * 核心改进：
 * 1. 【新仓不机械止盈】回本后分批止盈，不是全卖
 * 2. 【新仓可转老仓】6小时后自动转老仓，享受移动止盈
 * 3. 【分层止盈】新仓卖30%保本，留70%转老仓博利润
 * 4. 【动态调整】盈利>5%提前转老仓，享受更大回撤容忍
 */
@Slf4j
@Component
public class FlexiblePositionStrategy {

    // ==================== 新仓参数（0-6小时）====================
    
    /** 新仓保本止盈：回本后卖30%，留70% */
    private static final BigDecimal NEW_PARTIAL_EXIT_RATIO = new BigDecimal("0.30");
    /** 新仓微利止盈：盈利1%后再卖20%，累计卖50% */
    private static final BigDecimal NEW_SECOND_EXIT_RATIO = new BigDecimal("0.20");
    /** 新仓转老仓时间：6小时 */
    private static final int NEW_TO_OLD_HOURS = 6;
    /** 盈利加速转老仓：盈利>5%提前转 */
    private static final BigDecimal FAST_TO_OLD_PROFIT = new BigDecimal("0.05");
    
    // ==================== 老仓参数（>6小时）====================
    
    /** 老仓移动止盈回撤：2% */
    private static final BigDecimal OLD_TRAILING_PCT = new BigDecimal("0.02");
    /** 老仓移动止盈触发：盈利>3%开启 */
    private static final BigDecimal OLD_TRAILING_TRIGGER = new BigDecimal("0.03");
    /** 老仓分批止盈：盈利>10%卖50% */
    private static final BigDecimal OLD_PROFIT_TAKE_HALF = new BigDecimal("0.10");
    
    // ==================== 状态 ====================
    
    @Data
    @Builder
    public static class FlexiblePosition {
        private String symbol;
        private Integer level;
        private BigDecimal qty;
        private BigDecimal cost;
        private long buildTime;
        
        // 状态
        private boolean isNew;              // 是否新仓
        private BigDecimal remainingQty;    // 剩余数量
        private boolean partialExited;      // 是否已部分止盈
        private boolean secondExited;       // 是否第二次止盈
        private BigDecimal highestPrice;    // 最高价（移动止盈用）
        
        /**
         * 持仓小时数
         */
        public int getHoldHours() {
            return (int) (System.currentTimeMillis() - buildTime) / (1000 * 60 * 60);
        }
        
        /**
         * 当前盈亏百分比
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
         * 自动转老仓判断
         */
        public boolean shouldBecomeOld(BigDecimal currentPrice) {
            // 时间条件：满6小时
            if (getHoldHours() >= NEW_TO_OLD_HOURS) return true;
            
            // 盈利条件：盈利>5%，提前转老仓享受移动止盈
            if (getProfitPct(currentPrice).compareTo(FAST_TO_OLD_PROFIT) >= 0) {
                return true;
            }
            
            return false;
        }
    }
    
    // ==================== 新仓策略（分层止盈）====================
    
    /**
     * 管理新仓（0-6小时，分层止盈）
     * 
     * 策略：
     * 1. 回本（成本+0.15%）→ 卖30%，保本锁定，留70%
     * 2. 盈利1% → 再卖20%，累计卖50%，留50%博利润
     * 3. 盈利>5% → 提前转老仓，享受移动止盈2%回撤
     * 4. 被套 → 不止损，拿着，做T自救
     * 5. 满6小时 → 自动转老仓
     */
    public PositionAction manageNewPosition(FlexiblePosition pos, BigDecimal currentPrice) {
        BigDecimal profitPct = pos.getProfitPct(currentPrice);
        BigDecimal breakeven = pos.getCost().multiply(new BigDecimal("1.0015"));
        
        // 更新最高价
        pos.updateHighest(currentPrice);
        
        // 1. 第一次止盈：回本卖30%（保本）
        if (!pos.isPartialExited() && currentPrice.compareTo(breakeven) >= 0) {
            pos.setPartialExited(true);
            BigDecimal sellQty = pos.getQty().multiply(NEW_PARTIAL_EXIT_RATIO);
            pos.setRemainingQty(pos.getQty().subtract(sellQty));
            
            return PositionAction.builder()
                .actionType(ActionType.NEW_FIRST_EXIT)
                .sellQty(sellQty)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("新仓回本！卖%.0f%%锁定本金（%s），留%.0f%%继续博",
                    NEW_PARTIAL_EXIT_RATIO.multiply(new BigDecimal("100")),
                    sellQty, 
                    NEW_PARTIAL_EXIT_RATIO.multiply(new BigDecimal("70"))))
                .build();
        }
        
        // 2. 第二次止盈：盈利1%再卖20%
        if (pos.isPartialExited() && !pos.isSecondExited() && 
            profitPct.compareTo(new BigDecimal("0.01")) >= 0) {
            pos.setSecondExited(true);
            BigDecimal sellQty = pos.getQty().multiply(NEW_SECOND_EXIT_RATIO);
            pos.setRemainingQty(pos.getRemainingQty().subtract(sellQty));
            
            return PositionAction.builder()
                .actionType(ActionType.NEW_SECOND_EXIT)
                .sellQty(sellQty)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("新仓盈利%.2f%%！再卖%.0f%，累计卖50%%，留50%%博",
                    profitPct.multiply(new BigDecimal("100")),
                    NEW_SECOND_EXIT_RATIO.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 3. 提前转老仓：盈利>5%
        if (!pos.isNew() || profitPct.compareTo(FAST_TO_OLD_PROFIT) >= 0) {
            pos.setNew(false);
            return PositionAction.builder()
                .actionType(ActionType.BECOME_OLD_EARLY)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("🎉 新仓盈利%.2f%%提前转老仓！享受移动止盈2%%回撤",
                    profitPct.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 4. 时间转老仓：满6小时
        if (pos.getHoldHours() >= NEW_TO_OLD_HOURS) {
            pos.setNew(false);
            return PositionAction.builder()
                .actionType(ActionType.BECOME_OLD_TIME)
                .remainingQty(pos.getRemainingQty())
                .reason("新仓满6小时，自动转老仓，剩余" + pos.getRemainingQty() + "享受移动止盈")
                .build();
        }
        
        // 5. 被套中，提示做T自救
        if (profitPct.compareTo(BigDecimal.ZERO) < 0) {
            return PositionAction.builder()
                .actionType(ActionType.NEW_HOLD_UNDERWATER)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("新仓被套%.2f%%，不止损，建议做T自救",
                    profitPct.abs().multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 持有中
        return PositionAction.builder()
            .actionType(ActionType.NEW_HOLD)
            .remainingQty(pos.getRemainingQty())
            .reason(String.format("新仓持有中：盈利%.2f%%，剩余%s，最高%s",
                profitPct.multiply(new BigDecimal("100")),
                pos.getRemainingQty(), pos.getHighestPrice()))
            .build();
    }
    
    // ==================== 老仓策略（移动止盈）====================
    
    /**
     * 管理老仓（>6小时，移动止盈让利润奔跑）
     */
    public PositionAction manageOldPosition(FlexiblePosition pos, BigDecimal currentPrice) {
        BigDecimal profitPct = pos.getProfitPct(currentPrice);
        
        // 更新最高价
        pos.updateHighest(currentPrice);
        
        // 1. 盈利>10%，分批止盈卖50%
        if (profitPct.compareTo(OLD_PROFIT_TAKE_HALF) >= 0 && 
            pos.getRemainingQty().compareTo(pos.getQty().multiply(new BigDecimal("0.5"))) > 0) {
            
            BigDecimal sellHalf = pos.getRemainingQty().multiply(new BigDecimal("0.5"));
            pos.setRemainingQty(pos.getRemainingQty().subtract(sellHalf));
            
            return PositionAction.builder()
                .actionType(ActionType.OLD_TAKE_HALF)
                .sellQty(sellHalf)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("🚀 老仓盈利%.2f%%！卖半仓锁定利润，留半仓继续飞",
                    profitPct.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 2. 移动止盈：盈利>3%后回撤2%
        if (pos.getHighestPrice() != null && profitPct.compareTo(OLD_TRAILING_TRIGGER) >= 0) {
            BigDecimal trailingPrice = pos.getHighestPrice()
                .multiply(BigDecimal.ONE.subtract(OLD_TRAILING_PCT));
            
            if (currentPrice.compareTo(trailingPrice) <= 0) {
                return PositionAction.builder()
                    .actionType(ActionType.OLD_TRAILING_EXIT)
                    .sellQty(pos.getRemainingQty())
                    .remainingQty(BigDecimal.ZERO)
                    .reason(String.format("老仓移动止盈：从高点%s回撤%.0f%%至%s，盈利%.2f%%",
                        pos.getHighestPrice(), 
                        OLD_TRAILING_PCT.multiply(new BigDecimal("100")),
                        currentPrice,
                        profitPct.multiply(new BigDecimal("100"))))
                    .build();
            }
        }
        
        // 持有中
        return PositionAction.builder()
            .actionType(ActionType.OLD_HOLD)
            .remainingQty(pos.getRemainingQty())
            .reason(String.format("老仓持有中：盈利%.2f%%，最高%s，移动止盈线%s",
                profitPct.multiply(new BigDecimal("100")),
                pos.getHighestPrice(),
                pos.getHighestPrice() != null ? 
                    pos.getHighestPrice().multiply(new BigDecimal("0.98")) : "N/A"))
            .build();
    }
    
    // ==================== 主入口 ====================
    
    /**
     * 仓位管理主入口
     */
    public PositionAction manage(FlexiblePosition pos, BigDecimal currentPrice) {
        // 自动转老仓判断
        if (pos.isNew() && pos.shouldBecomeOld(currentPrice)) {
            pos.setNew(false);
            return PositionAction.builder()
                .actionType(ActionType.AUTO_BECOME_OLD)
                .remainingQty(pos.getRemainingQty())
                .reason(String.format("自动转老仓：持仓%d小时，盈利%.2f%%",
                    pos.getHoldHours(),
                    pos.getProfitPct(currentPrice).multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 根据状态调用对应策略
        if (pos.isNew()) {
            return manageNewPosition(pos, currentPrice);
        } else {
            return manageOldPosition(pos, currentPrice);
        }
    }
    
    // ==================== 数据类 ====================
    
    public enum ActionType {
        // 新仓
        NEW_FIRST_EXIT,      // 第一次止盈（回本卖30%）
        NEW_SECOND_EXIT,     // 第二次止盈（盈利1%再卖20%）
        NEW_HOLD_UNDERWATER, // 新仓被套持有
        NEW_HOLD,            // 新仓正常持有
        
        // 转老仓
        BECOME_OLD_EARLY,    // 提前转老仓（盈利>5%）
        BECOME_OLD_TIME,     // 时间转老仓（满6小时）
        AUTO_BECOME_OLD,     // 自动转老仓
        
        // 老仓
        OLD_TAKE_HALF,       // 老仓分批止盈（盈利10%卖半仓）
        OLD_TRAILING_EXIT,   // 老仓移动止盈
        OLD_HOLD             // 老仓持有
    }
    
    @Data
    @Builder
    public static class PositionAction {
        private ActionType actionType;
        private BigDecimal sellQty;      // 本次卖出数量
        private BigDecimal remainingQty; // 剩余数量
        private String reason;
    }
}
