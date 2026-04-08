package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * RSI驱动建仓系统 - 超卖才买，超买才卖
 * 
 * 核心逻辑：
 * 1. 【RSI<25】极度超卖 → 激进建仓（投入30%）
 * 2. 【RSI 25-35】超卖 → 正常建仓（投入20%）
 * 3. 【RSI 35-45】偏弱 → 观望或做小T
 * 4. 【RSI 45-55】震荡 → 只做T，不建仓
 * 5. 【RSI 55-65】偏强 → 持有，考虑减仓
 * 6. 【RSI>65】超买 → 止盈或反T
 * 
 * 优势：避免下跌趋势中盲目建仓，只在市场情绪极端时介入
 */
@Slf4j
@Component
public class RSIBasedGridBuilder {

    // ==================== RSI区间配置 ====================
    
    /** 极度超卖：RSI<25，大力建仓 */
    private static final BigDecimal RSI_EXTREME_OVERSOLD = new BigDecimal("25");
    /** 超卖：RSI<35，正常建仓 */
    private static final BigDecimal RSI_OVERSOLD = new BigDecimal("35");
    /** 弱势：RSI<45，观望 */
    private static final BigDecimal RSI_WEAK = new BigDecimal("45");
    /** 强势：RSI>55，持有 */
    private static final BigDecimal RSI_STRONG = new BigDecimal("55");
    /** 超买：RSI>65，减仓 */
    private static final BigDecimal RSI_OVERBOUGHT = new BigDecimal("65");
    /** 极度超买：RSI>75，大力减仓 */
    private static final BigDecimal RSI_EXTREME_OVERBOUGHT = new BigDecimal("75");
    
    // ==================== 建仓资金配置 ====================
    
    /** 极度超卖建仓：30% */
    private static final BigDecimal BUILD_EXTREME = new BigDecimal("0.30");
    /** 超卖建仓：20% */
    private static final BigDecimal BUILD_NORMAL = new BigDecimal("0.20");
    /** 弱势小补：10% */
    private static final BigDecimal BUILD_SMALL = new BigDecimal("0.10");
    /** 保留现金：20% */
    private static final BigDecimal RESERVE_RATIO = new BigDecimal("0.20");
    
    // ==================== 状态 ====================
    
    @Data
    @Builder
    public static class RSIState {
        private String symbol;
        private BigDecimal totalCapital;
        private BigDecimal availableCash;
        private Map<Integer, GridPosition> positions = new LinkedHashMap<>();
        private BigDecimal totalQty = BigDecimal.ZERO;
        private BigDecimal avgCost = BigDecimal.ZERO;
        private int buildCount = 0;
        
        // RSI历史（用于背离检测）
        private LinkedList<RSIPoint> rsiHistory = new LinkedList<>();
        
        /**
         * 更新RSI历史
         */
        public void updateRSI(BigDecimal rsi, BigDecimal price, long time) {
            rsiHistory.addLast(new RSIPoint(rsi, price, time));
            if (rsiHistory.size() > 20) rsiHistory.pollFirst();
        }
        
        /**
         * 检测底背离（价格新低，RSI未新低）
         */
        public boolean isBullishDivergence() {
            if (rsiHistory.size() < 10) return false;
            
            RSIPoint recent = rsiHistory.getLast();
            RSIPoint prevLow = rsiHistory.stream()
                .min(Comparator.comparing(RSIPoint::getPrice))
                .orElse(recent);
            
            // 价格新低，但RSI未新低
            return recent.getPrice().compareTo(prevLow.getPrice()) < 0 &&
                   recent.getRsi().compareTo(prevLow.getRsi()) > 0;
        }
        
        /**
         * 计算整体盈亏平衡价
         */
        public BigDecimal getBreakevenPrice() {
            if (totalQty.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
            return avgCost.multiply(new BigDecimal("1.0015"));
        }
    }
    
    @Data
    @Builder
    public static class RSIPoint {
        private BigDecimal rsi;
        private BigDecimal price;
        private long time;
    }
    
    @Data
    @Builder
    public static class GridPosition {
        private Integer level;
        private BigDecimal qty;
        private BigDecimal cost;
        private BigDecimal maxQty;  // 最大持仓（补仓上限）
    }
    
    // ==================== RSI驱动决策 ====================
    
    /**
     * RSI驱动建仓决策
     * 
     * @param state 状态
     * @param currentRSI 当前RSI
     * @param currentPrice 当前价
     * @param currentLevel 当前档位
     * @return 建仓建议
     */
    public BuildDecision decideBuild(RSIState state, BigDecimal currentRSI,
                                      BigDecimal currentPrice, Integer currentLevel) {
        
        // 更新RSI历史
        state.updateRSI(currentRSI, currentPrice, System.currentTimeMillis());
        
        // 该档已有底仓，不重复建仓（考虑补仓逻辑）
        if (state.getPositions().containsKey(currentLevel)) {
            return checkAddPosition(state, currentRSI, currentPrice, currentLevel);
        }
        
        // 现金不足（保留20%）
        BigDecimal minReserve = state.getTotalCapital().multiply(RESERVE_RATIO);
        if (state.getAvailableCash().compareTo(minReserve.add(
            state.getTotalCapital().multiply(BUILD_SMALL))) < 0) {
            return BuildDecision.skip("现金不足，保留20%应急");
        }
        
        // ========== RSI极度超卖 (<25)：大力建仓 ==========
        if (currentRSI.compareTo(RSI_EXTREME_OVERSOLD) < 0) {
            BigDecimal cash = state.getTotalCapital().multiply(BUILD_EXTREME);
            BigDecimal qty = cash.divide(currentPrice, 8, RoundingMode.DOWN);
            
            boolean divergence = state.isBullishDivergence();
            
            return BuildDecision.builder()
                .shouldBuild(true)
                .level(currentLevel)
                .price(currentPrice)
                .qty(qty)
                .cash(cash)
                .reason(String.format("🟢 RSI极度超卖%s！RSI=%.1f，大力建仓%.0f%%资金%s",
                    divergence ? "+底背离" : "",
                    currentRSI, 
                    BUILD_EXTREME.multiply(new BigDecimal("100")),
                    divergence ? "（背离信号，反弹概率高）" : ""))
                .strength(divergence ? BuildStrength.STRONG : BuildStrength.NORMAL)
                .build();
        }
        
        // ========== RSI超卖 (25-35)：正常建仓 ==========
        if (currentRSI.compareTo(RSI_OVERSOLD) < 0) {
            BigDecimal cash = state.getTotalCapital().multiply(BUILD_NORMAL);
            BigDecimal qty = cash.divide(currentPrice, 8, RoundingMode.DOWN);
            
            return BuildDecision.builder()
                .shouldBuild(true)
                .level(currentLevel)
                .price(currentPrice)
                .qty(qty)
                .cash(cash)
                .reason(String.format("🟡 RSI超卖，RSI=%.1f，正常建仓%.0f%%资金",
                    currentRSI, BUILD_NORMAL.multiply(new BigDecimal("100"))))
                .strength(BuildStrength.NORMAL)
                .build();
        }
        
        // ========== RSI弱势 (35-45)：小仓位试探 ==========
        if (currentRSI.compareTo(RSI_WEAK) < 0) {
            // 只在有底背离时才小仓位建仓
            if (state.isBullishDivergence()) {
                BigDecimal cash = state.getTotalCapital().multiply(BUILD_SMALL);
                BigDecimal qty = cash.divide(currentPrice, 8, RoundingMode.DOWN);
                
                return BuildDecision.builder()
                    .shouldBuild(true)
                    .level(currentLevel)
                    .price(currentPrice)
                    .qty(qty)
                    .cash(cash)
                    .reason(String.format("🔵 RSI偏弱但有底背离，RSI=%.1f，小仓位试探%.0f%%",
                        currentRSI, BUILD_SMALL.multiply(new BigDecimal("100"))))
                    .strength(BuildStrength.WEAK)
                    .build();
            }
            
            return BuildDecision.skip(String.format("RSI=%.1f偏弱，无背离信号，观望", currentRSI));
        }
        
        // ========== RSI中性 (45-55)：不建仓 ==========
        if (currentRSI.compareTo(RSI_STRONG) <= 0) {
            return BuildDecision.skip(String.format("RSI=%.1f中性震荡，不建仓，等待超卖", currentRSI));
        }
        
        // ========== RSI强势：更不建仓 ==========
        return BuildDecision.skip(String.format("RSI=%.1f偏强/超买，绝不在高位建仓", currentRSI));
    }
    
    /**
     * 补仓决策（已有底仓的档位）
     */
    private BuildDecision checkAddPosition(RSIState state, BigDecimal currentRSI,
                                            BigDecimal currentPrice, Integer currentLevel) {
        GridPosition pos = state.getPositions().get(currentLevel);
        
        // 该档已满不补
        if (pos.getQty().compareTo(pos.getMaxQty()) >= 0) {
            return BuildDecision.skip("该档已满，不补仓");
        }
        
        BigDecimal dropPct = pos.getCost().subtract(currentPrice)
            .divide(pos.getCost(), 6, RoundingMode.HALF_UP);
        
        // RSI极度超卖 + 跌5%以上 → 补仓
        if (currentRSI.compareTo(RSI_EXTREME_OVERSOLD) < 0 && 
            dropPct.compareTo(new BigDecimal("0.05")) >= 0) {
            
            BigDecimal addCash = state.getTotalCapital().multiply(new BigDecimal("0.10"));
            BigDecimal addQty = addCash.divide(currentPrice, 8, RoundingMode.DOWN);
            
            return BuildDecision.builder()
                .shouldBuild(true)
                .isAddPosition(true)
                .level(currentLevel)
                .price(currentPrice)
                .qty(addQty)
                .cash(addCash)
                .reason(String.format("档%d极度超卖+跌%.1f%%，补仓摊薄成本",
                    currentLevel, dropPct.multiply(new BigDecimal("100"))))
                .build();
        }
        
        return BuildDecision.skip("不满足补仓条件");
    }
    
    /**
     * RSI驱动卖出/做T决策
     */
    public SellDecision decideSell(RSIState state, BigDecimal currentRSI,
                                    BigDecimal currentPrice, Integer currentLevel) {
        
        GridPosition pos = state.getPositions().get(currentLevel);
        if (pos == null) return SellDecision.skip("无持仓");
        
        BigDecimal profitPct = currentPrice.subtract(pos.getCost())
            .divide(pos.getCost(), 6, RoundingMode.HALF_UP);
        
        // ========== RSI极度超买 (>75)：大力减仓 ==========
        if (currentRSI.compareTo(RSI_EXTREME_OVERBOUGHT) > 0 && profitPct.compareTo(BigDecimal.ZERO) > 0) {
            return SellDecision.builder()
                .shouldSell(true)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("🔴 RSI极度超买！RSI=%.1f，盈利%.2f%%，清仓", 
                    currentRSI, profitPct.multiply(new BigDecimal("100"))))
                .type(SellType.RSI_OVERBOUGHT)
                .build();
        }
        
        // ========== RSI超买 (65-75)：减仓/做反T ==========
        if (currentRSI.compareTo(RSI_OVERBOUGHT) > 0 && profitPct.compareTo(BigDecimal.ZERO) > 0) {
            return SellDecision.builder()
                .shouldSell(true)
                .ratio(new BigDecimal("0.5"))  // 卖一半
                .price(currentPrice)
                .reason(String.format("🟠 RSI超买，RSI=%.1f，减仓50%%做反T",
                    currentRSI))
                .type(SellType.RSI_HIGH)
                .build();
        }
        
        // ========== RSI强势 (55-65)：持有观望 ==========
        if (currentRSI.compareTo(RSI_STRONG) > 0) {
            return SellDecision.skip(String.format("RSI=%.1f偏强，持有观望", currentRSI));
        }
        
        // ========== 移动止盈（已有利润回撤）==========
        // 这里简化，实际需要记录最高价
        
        return SellDecision.skip("持有中");
    }
    
    // ==================== 数据类 ====================
    
    public enum BuildStrength {
        WEAK, NORMAL, STRONG
    }
    
    public enum SellType {
        RSI_OVERBOUGHT, RSI_HIGH, TRAILING_STOP, NORMAL
    }
    
    @Data
    @Builder
    public static class BuildDecision {
        private boolean shouldBuild;
        private boolean isAddPosition;
        private Integer level;
        private BigDecimal price;
        private BigDecimal qty;
        private BigDecimal cash;
        private String reason;
        private BuildStrength strength;
        
        public static BuildDecision skip(String reason) {
            return BuildDecision.builder()
                .shouldBuild(false)
                .reason(reason)
                .build();
        }
    }
    
    @Data
    @Builder
    public static class SellDecision {
        private boolean shouldSell;
        private BigDecimal ratio;
        private BigDecimal price;
        private String reason;
        private SellType type;
        
        public static SellDecision skip(String reason) {
            return SellDecision.builder()
                .shouldSell(false)
                .reason(reason)
                .build();
        }
    }
}
