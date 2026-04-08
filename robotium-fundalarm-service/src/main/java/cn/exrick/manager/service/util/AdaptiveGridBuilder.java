package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 自适应分级建仓系统 - 解决"一直跌"资金不够的问题
 * 
 * 核心改进：
 * 1. 【分级间隔】跌得越多，建仓间隔越大（2%→3%→5%→8%）
 * 2. 【递减仓位】越往后建仓，单次仓位越小（20%→15%→10%→5%）
 * 3. 【最多4次】无论跌多少，最多建4个档，保留最后20%现金
 * 4. 【极端止损】跌15%且趋势恶化，全部清仓保命（极少触发）
 */
@Slf4j
@Component
public class AdaptiveGridBuilder {

    // ==================== 分级建仓配置 ====================
    
    /** 建仓间隔（递增，防止一直跌建太密） */
    private static final BigDecimal[] ADD_TRIGGERS = {
        new BigDecimal("0.02"),  // 第1次：跌2%
        new BigDecimal("0.03"),  // 第2次：再跌3%（累计5%）
        new BigDecimal("0.05"),  // 第3次：再跌5%（累计10%）
        new BigDecimal("0.08"),  // 第4次：再跌8%（累计18%）
    };
    
    /** 建仓资金比例（递减，保存实力） */
    private static final BigDecimal[] ADD_RATIOS = {
        new BigDecimal("0.25"),  // 第1次：25%
        new BigDecimal("0.20"),  // 第2次：20%
        new BigDecimal("0.15"),  // 第3次：15%
        new BigDecimal("0.10"),  // 第4次：10%
    };
    
    /** 最大建仓次数 */
    private static final int MAX_BUILD_TIMES = 4;
    /** 保留现金比例（绝不投入） */
    private static final BigDecimal RESERVE_CASH = new BigDecimal("0.20");
    /** 极端止损线 */
    private static final BigDecimal EXTREME_STOP = new BigDecimal("0.15");
    
    // ==================== 状态 ====================
    
    @Data
    @Builder
    public static class AdaptiveState {
        private String symbol;
        private BigDecimal totalCapital;
        private BigDecimal availableCash;
        private Map<Integer, GridPos> positions = new TreeMap<>();
        private int buildCount = 0;
        private BigDecimal lastBuildPrice;
        private BigDecimal totalQty = BigDecimal.ZERO;
        private BigDecimal avgCost = BigDecimal.ZERO;
        private BigDecimal totalInvested = BigDecimal.ZERO;
        
        /**
         * 获取下次建仓需要的跌幅
         */
        public BigDecimal getNextTrigger() {
            if (buildCount >= MAX_BUILD_TIMES) return null;
            return ADD_TRIGGERS[buildCount];
        }
        
        /**
         * 获取下次建仓投入比例
         */
        public BigDecimal getNextRatio() {
            if (buildCount >= MAX_BUILD_TIMES) return BigDecimal.ZERO;
            return ADD_RATIOS[buildCount];
        }
        
        /**
         * 是否还能建仓
         */
        public boolean canBuild() {
            return buildCount < MAX_BUILD_TIMES && 
                   availableCash.compareTo(totalCapital.multiply(RESERVE_CASH)) > 0;
        }
        
        /**
         * 计算整体回撤
         */
        public BigDecimal getDrawback(BigDecimal currentPrice) {
            if (avgCost.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
            return avgCost.subtract(currentPrice).divide(avgCost, 6, RoundingMode.HALF_UP);
        }
    }
    
    @Data
    @Builder
    public static class GridPos {
        private Integer level;
        private BigDecimal qty;
        private BigDecimal cost;
    }
    
    // ==================== 核心逻辑 ====================
    
    /**
     * 判断是否建仓（自适应间隔）
     */
    public BuildDecision shouldBuild(AdaptiveState state, BigDecimal currentPrice, 
                                      Integer currentLevel) {
        // 1. 检查是否还能建
        if (!state.canBuild()) {
            return BuildDecision.skip("已达最大建仓次数或现金不足");
        }
        
        // 2. 该档已有底仓不建
        if (state.getPositions().containsKey(currentLevel)) {
            return BuildDecision.skip("该档已有底仓");
        }
        
        // 3. 计算需要的跌幅
        BigDecimal trigger = state.getNextTrigger();
        BigDecimal drop = state.getLastBuildPrice().subtract(currentPrice)
            .divide(state.getLastBuildPrice(), 6, RoundingMode.HALF_UP);
        
        // 4. 跌幅不够不建
        if (drop.compareTo(trigger) < 0) {
            return BuildDecision.skip(String.format("跌幅%.2f%%，还需%.2f%%才建仓",
                drop.multiply(new BigDecimal("100")),
                trigger.subtract(drop).multiply(new BigDecimal("100"))));
        }
        
        // 5. 计算建仓金额（递减）
        BigDecimal ratio = state.getNextRatio();
        BigDecimal cash = state.getTotalCapital().multiply(ratio);
        
        // 确保不超过可用现金（保留20%）
        BigDecimal minReserve = state.getTotalCapital().multiply(RESERVE_CASH);
        if (state.getAvailableCash().subtract(cash).compareTo(minReserve) < 0) {
            cash = state.getAvailableCash().subtract(minReserve);
            if (cash.compareTo(BigDecimal.ZERO) <= 0) {
                return BuildDecision.skip("现金不足（需保留20%）");
            }
        }
        
        BigDecimal qty = cash.divide(currentPrice, 8, RoundingMode.DOWN);
        
        return BuildDecision.builder()
            .shouldBuild(true)
            .level(currentLevel)
            .price(currentPrice)
            .qty(qty)
            .cash(cash)
            .ratio(ratio)
            .triggerPct(trigger)
            .reason(String.format("第%d次建仓，跌%.2f%%≥%.0f%%，投入%.0f%%资金",
                state.getBuildCount() + 1,
                drop.multiply(new BigDecimal("100")),
                trigger.multiply(new BigDecimal("100")),
                ratio.multiply(new BigDecimal("100"))))
            .build();
    }
    
    /**
     * 极端情况检查（一直跌的保险）
     */
    public EmergencyCheck checkEmergency(AdaptiveState state, BigDecimal currentPrice,
                                          List<PriceBar> recentBars) {
        BigDecimal drawback = state.getDrawback(currentPrice);
        
        // 情况1：跌15%且趋势向下 → 清仓保命
        if (drawback.compareTo(EXTREME_STOP) >= 0) {
            boolean trendingDown = isTrendingDown(recentBars, 5);
            if (trendingDown) {
                return EmergencyCheck.builder()
                    .isEmergency(true)
                    .shouldLiquidate(true)
                    .reason(String.format("⚠️ 极端情况：回撤%.2f%%且趋势向下，清仓保命！",
                        drawback.multiply(new BigDecimal("100"))))
                    .build();
            }
        }
        
        // 情况2：跌10%以上，停止一切做T，只观望
        if (drawback.compareTo(new BigDecimal("0.10")) >= 0) {
            return EmergencyCheck.builder()
                .isEmergency(true)
                .shouldLiquidate(false)
                .stopTrading(true)
                .reason(String.format("回撤%.2f%%，停止做T，观望等待",
                    drawback.multiply(new BigDecimal("100"))))
                .build();
        }
        
        return EmergencyCheck.builder()
            .isEmergency(false)
            .build();
    }
    
    /**
     * 执行建仓
     */
    public void executeBuild(AdaptiveState state, BuildDecision decision) {
        GridPos pos = GridPos.builder()
            .level(decision.getLevel())
            .qty(decision.getQty())
            .cost(decision.getPrice())
            .build();
        
        state.getPositions().put(decision.getLevel(), pos);
        state.setAvailableCash(state.getAvailableCash().subtract(decision.getCash()));
        state.setLastBuildPrice(decision.getPrice());
        state.setBuildCount(state.getBuildCount() + 1);
        
        // 更新平均成本
        recalculate(state);
        
        log.info("【建仓完成】{} 档{} {}@{} 投入{} 剩余现金{}（保留{}）",
            state.getSymbol(), decision.getLevel(), decision.getQty(), 
            decision.getPrice(), decision.getCash(), state.getAvailableCash(),
            state.getTotalCapital().multiply(RESERVE_CASH));
    }
    
    private void recalculate(AdaptiveState state) {
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        for (GridPos pos : state.getPositions().values()) {
            totalQty = totalQty.add(pos.getQty());
            totalCost = totalCost.add(pos.getCost().multiply(pos.getQty()));
        }
        state.setTotalQty(totalQty);
        if (totalQty.compareTo(BigDecimal.ZERO) > 0) {
            state.setAvgCost(totalCost.divide(totalQty, 8, RoundingMode.HALF_UP));
        }
        state.setTotalInvested(totalCost);
    }
    
    private boolean isTrendingDown(List<PriceBar> bars, int n) {
        if (bars.size() < n) return false;
        int down = 0;
        for (int i = bars.size() - n + 1; i < bars.size(); i++) {
            if (bars.get(i).getClose().compareTo(bars.get(i-1).getClose()) < 0) {
                down++;
            }
        }
        return down >= n - 1; // 至少4/5根下跌
    }
    
    // ==================== 数据类 ====================
    
    @Data
    @Builder
    public static class BuildDecision {
        private boolean shouldBuild;
        private Integer level;
        private BigDecimal price;
        private BigDecimal qty;
        private BigDecimal cash;
        private BigDecimal ratio;
        private BigDecimal triggerPct;
        private String reason;
        
        public static BuildDecision skip(String reason) {
            return BuildDecision.builder()
                .shouldBuild(false)
                .reason(reason)
                .build();
        }
    }
    
    @Data
    @Builder
    public static class EmergencyCheck {
        private boolean isEmergency;
        private boolean shouldLiquidate;  // 是否清仓
        private boolean stopTrading;      // 是否停止交易
        private String reason;
    }
    
    @Data
    @Builder
    public static class PriceBar {
        private BigDecimal close;
    }
}
