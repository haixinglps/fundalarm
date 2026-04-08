package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 动态底仓建仓系统 - 逐步建仓，不一次建满
 * 
 * 核心设计：
 * 1. 【初始轻仓】只在当前价附近1-2个档位建底仓（30%资金）
 * 2. 【下跌补仓】价格每跌2%，在下方新档位建仓（每次20%资金）
 * 3. 【上涨观望】价格上涨时，上方档位不建仓，只做T
 * 4. 【资金保留】始终保持30%现金备用，应对极端行情
 * 5. 【做T周转】用底仓做T，不额外占用资金
 * 
 * 优势：
 * - 不会一次性套在所有档位
 * - 越跌越有资金补仓，摊薄成本
 * - 保留灵活性，进退自如
 */
@Slf4j
@Component
public class DynamicBaseBuilder {

    // ==================== 建仓参数 ====================
    
    /** 初始建仓资金：30% */
    private static final BigDecimal INIT_RATIO = new BigDecimal("0.30");
    /** 每次补仓资金：20% */
    private static final BigDecimal ADD_RATIO = new BigDecimal("0.20");
    /** 保留现金：30% */
    private static final BigDecimal RESERVE_RATIO = new BigDecimal("0.30");
    /** 补仓触发：距上次建仓价跌2% */
    private static final BigDecimal ADD_TRIGGER = new BigDecimal("0.02");
    /** 最大档位：最多建5个档位的底仓 */
    private static final int MAX_GRID_POSITIONS = 5;
    
    // ==================== 状态 ====================
    
    @Data
    @Builder
    public static class DynamicState {
        private String symbol;
        private BigDecimal totalCapital;        // 总资金
        private BigDecimal availableCash;       // 可用现金
        private BigDecimal reserveCash;         // 保留现金
        
        // 已建底仓
        private Map<Integer, GridPosition> positions = new TreeMap<>(); // 档位号->持仓
        private BigDecimal totalQty = BigDecimal.ZERO;
        private BigDecimal avgCost = BigDecimal.ZERO;
        private BigDecimal lastBuildPrice;      // 上次建仓价格
        private int buildCount = 0;             // 建仓次数
        
        // 做T状态
        private TOrder activeTOrder = null;
        private BigDecimal totalTProfit = BigDecimal.ZERO;
        
        /**
         * 计算整体盈亏平衡价
         */
        public BigDecimal getBreakevenPrice() {
            if (totalQty.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
            BigDecimal totalCost = avgCost.multiply(totalQty).subtract(totalTProfit);
            return totalCost.divide(totalQty, 8, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("1.0015"));
        }
        
        /**
         * 更新平均成本
         */
        public void recalculate() {
            BigDecimal totalCost = BigDecimal.ZERO;
            totalQty = BigDecimal.ZERO;
            for (GridPosition pos : positions.values()) {
                totalCost = totalCost.add(pos.getAvgCost().multiply(pos.getQuantity()));
                totalQty = totalQty.add(pos.getQuantity());
            }
            if (totalQty.compareTo(BigDecimal.ZERO) > 0) {
                avgCost = totalCost.divide(totalQty, 8, RoundingMode.HALF_UP);
            }
        }
    }
    
    @Data
    @Builder
    public static class GridPosition {
        private Integer level;
        private BigDecimal quantity;
        private BigDecimal avgCost;
        private BigDecimal maxQty;      // 该档最大持仓（用于做T限制）
        private boolean isBase;         // 是否底仓（true=底仓 false=做T临时持仓）
    }
    
    @Data
    @Builder
    public static class TOrder {
        private String id;
        private int type;
        private int level;
        private BigDecimal entryPrice;
        private BigDecimal qty;
        private long entryTime;
        private BigDecimal targetPrice;
        private BigDecimal stopPrice;
    }
    
    // ==================== 初始化 ====================
    
    /**
     * 初始化动态建仓
     * 
     * @param symbol 交易对
     * @param totalCapital 总资金
     * @param currentPrice 当前价格
     * @param currentLevel 当前档位
     */
    public DynamicState init(String symbol, BigDecimal totalCapital, 
                              BigDecimal currentPrice, Integer currentLevel) {
        BigDecimal initCash = totalCapital.multiply(INIT_RATIO);
        BigDecimal reserveCash = totalCapital.multiply(RESERVE_RATIO);
        
        DynamicState state = DynamicState.builder()
            .symbol(symbol)
            .totalCapital(totalCapital)
            .availableCash(totalCapital.subtract(initCash).subtract(reserveCash))
            .reserveCash(reserveCash)
            .lastBuildPrice(currentPrice)
            .build();
        
        // 初始建1个档
        BigDecimal qty = initCash.divide(currentPrice, 8, RoundingMode.DOWN);
        GridPosition pos = GridPosition.builder()
            .level(currentLevel)
            .quantity(qty)
            .avgCost(currentPrice)
            .maxQty(qty.multiply(new BigDecimal("1.5"))) // 最多补仓到1.5倍
            .isBase(true)
            .build();
        
        state.getPositions().put(currentLevel, pos);
        state.setBuildCount(1);
        state.recalculate();
        
        log.info("【动态建仓初始化】{} 档{} 建仓{}@{} 资金占比{} 保留现金{}",
            symbol, currentLevel, qty, currentPrice, INIT_RATIO, reserveCash);
        
        return state;
    }
    
    // ==================== 核心循环 ====================
    
    /**
     * 主循环
     * 
     * @param state 状态
     * @param currentPrice 当前价
     * @param currentLevel 当前档位
     * @return 操作建议
     */
    public List<TradeAction> onTick(DynamicState state, BigDecimal currentPrice, 
                                     Integer currentLevel) {
        List<TradeAction> actions = new ArrayList<>();
        
        // 1. 检查做T单
        if (state.getActiveTOrder() != null) {
            TradeAction closeAction = checkCloseT(state, currentPrice);
            if (closeAction != null) {
                actions.add(closeAction);
                return actions;
            }
        }
        
        // 2. 检查是否需要新建仓（下跌时）
        if (shouldBuildNewGrid(state, currentPrice, currentLevel)) {
            actions.add(buildNewGridAction(state, currentPrice, currentLevel));
        }
        
        // 3. 检查是否需要补仓（已建仓档位跌2%）
        GridPosition existingPos = state.getPositions().get(currentLevel);
        if (existingPos != null) {
            BigDecimal dropPct = existingPos.getAvgCost().subtract(currentPrice)
                .divide(existingPos.getAvgCost(), 6, RoundingMode.HALF_UP);
            if (dropPct.compareTo(ADD_TRIGGER) >= 0 && 
                existingPos.getQuantity().compareTo(existingPos.getMaxQty()) < 0) {
                actions.add(addPositionAction(state, existingPos, currentPrice));
            }
        }
        
        // 4. 寻找做T机会（在已有底仓的档位做T）
        if (state.getActiveTOrder() == null && existingPos != null) {
            TradeAction tAction = findTOpportunity(state, existingPos, currentPrice);
            if (tAction != null) {
                actions.add(tAction);
            }
        }
        
        return actions;
    }
    
    /**
     * 判断是否新建仓
     */
    private boolean shouldBuildNewGrid(DynamicState state, BigDecimal currentPrice, 
                                        Integer currentLevel) {
        // 已有该档不建
        if (state.getPositions().containsKey(currentLevel)) return false;
        // 超过最大档位数不建
        if (state.getPositions().size() >= MAX_GRID_POSITIONS) return false;
        // 距上次建仓跌不够2%不建
        if (state.getLastBuildPrice() != null) {
            BigDecimal drop = state.getLastBuildPrice().subtract(currentPrice)
                .divide(state.getLastBuildPrice(), 6, RoundingMode.HALF_UP);
            if (drop.compareTo(ADD_TRIGGER) < 0) return false;
        }
        // 没现金不建（保留资金）
        if (state.getAvailableCash().compareTo(
            state.getTotalCapital().multiply(ADD_RATIO)) < 0) {
            return false;
        }
        return true;
    }
    
    private TradeAction buildNewGridAction(DynamicState state, BigDecimal price, 
                                            Integer level) {
        BigDecimal cash = state.getTotalCapital().multiply(ADD_RATIO);
        BigDecimal qty = cash.divide(price, 8, RoundingMode.DOWN);
        
        GridPosition pos = GridPosition.builder()
            .level(level)
            .quantity(qty)
            .avgCost(price)
            .maxQty(qty.multiply(new BigDecimal("1.5")))
            .isBase(true)
            .build();
        
        state.getPositions().put(level, pos);
        state.setAvailableCash(state.getAvailableCash().subtract(cash));
        state.setLastBuildPrice(price);
        state.setBuildCount(state.getBuildCount() + 1);
        state.recalculate();
        
        return TradeAction.builder()
            .actionType(ActionType.BUILD_NEW_GRID)
            .level(level)
            .price(price)
            .qty(qty)
            .reason(String.format("新建底仓档%d@%s，累计%d档，资金利用率%.0f%%",
                level, price, state.getPositions().size(),
                state.getTotalCapital().subtract(state.getAvailableCash())
                    .divide(state.getTotalCapital(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))))
            .build();
    }
    
    private TradeAction addPositionAction(DynamicState state, GridPosition pos, 
                                           BigDecimal price) {
        BigDecimal cash = state.getTotalCapital().multiply(new BigDecimal("0.10")); // 补仓10%
        BigDecimal qty = cash.divide(price, 8, RoundingMode.DOWN);
        
        // 更新该档成本
        BigDecimal newCost = pos.getAvgCost().multiply(pos.getQuantity())
            .add(price.multiply(qty));
        pos.setQuantity(pos.getQuantity().add(qty));
        pos.setAvgCost(newCost.divide(pos.getQuantity(), 8, RoundingMode.HALF_UP));
        
        state.setAvailableCash(state.getAvailableCash().subtract(cash));
        state.recalculate();
        
        return TradeAction.builder()
            .actionType(ActionType.ADD_POSITION)
            .level(pos.getLevel())
            .price(price)
            .qty(qty)
            .reason(String.format("档%d补仓@%s，新成本%.2f",
                pos.getLevel(), price, pos.getAvgCost()))
            .build();
    }
    
    /**
     * 寻找做T机会（在已有底仓的档位）
     */
    private TradeAction findTOpportunity(DynamicState state, GridPosition pos, 
                                          BigDecimal currentPrice) {
        BigDecimal profitPct = currentPrice.subtract(pos.getAvgCost())
            .divide(pos.getAvgCost(), 6, RoundingMode.HALF_UP);
        
        // 该档盈利>0.3% → 反T（卖出部分，低价买回）
        if (profitPct.compareTo(new BigDecimal("0.003")) > 0) {
            BigDecimal tQty = pos.getQuantity().multiply(new BigDecimal("0.3"));
            
            String id = "T_" + System.currentTimeMillis();
            TOrder order = TOrder.builder()
                .id(id)
                .type(-1)
                .level(pos.getLevel())
                .entryPrice(currentPrice)
                .qty(tQty)
                .entryTime(System.currentTimeMillis())
                .targetPrice(currentPrice.multiply(new BigDecimal("0.997")))
                .stopPrice(currentPrice.multiply(new BigDecimal("1.002")))
                .build();
            
            state.setActiveTOrder(order);
            
            return TradeAction.builder()
                .actionType(ActionType.OPEN_T)
                .orderId(id)
                .level(pos.getLevel())
                .type(-1)
                .price(currentPrice)
                .qty(tQty)
                .target(order.getTargetPrice())
                .stop(order.getStopPrice())
                .reason(String.format("档%d反T：盈利%.2f%%",
                    pos.getLevel(), profitPct.multiply(new BigDecimal("100"))))
                .build();
        }
        
        return null;
    }
    
    private TradeAction checkCloseT(DynamicState state, BigDecimal currentPrice) {
        TOrder order = state.getActiveTOrder();
        long holdMin = (System.currentTimeMillis() - order.getEntryTime()) / 60000;
        
        BigDecimal profit = order.getEntryPrice().subtract(currentPrice).multiply(order.getQty());
        
        // 止盈
        if (currentPrice.compareTo(order.getTargetPrice()) <= 0) {
            return closeTAction(state, currentPrice, profit, "止盈");
        }
        // 止损
        if (currentPrice.compareTo(order.getStopPrice()) >= 0) {
            return closeTAction(state, currentPrice, profit, "止损");
        }
        // 超时
        if (holdMin >= 10) {
            return closeTAction(state, currentPrice, profit, "超时");
        }
        
        return null;
    }
    
    private TradeAction closeTAction(DynamicState state, BigDecimal price, 
                                      BigDecimal profit, String reason) {
        state.setActiveTOrder(null);
        state.setTotalTProfit(state.getTotalTProfit().add(profit));
        
        // 更新平均成本
        state.recalculate();
        
        return TradeAction.builder()
            .actionType(ActionType.CLOSE_T)
            .price(price)
            .profit(profit)
            .reason(reason)
            .build();
    }
    
    // ==================== 数据类 ====================
    
    public enum ActionType {
        BUILD_NEW_GRID,  // 新建底仓档
        ADD_POSITION,    // 补仓
        OPEN_T,          // 开做T
        CLOSE_T          // 平做T
    }
    
    @Data
    @Builder
    public static class TradeAction {
        private ActionType actionType;
        private Integer level;
        private String orderId;
        private Integer type;
        private BigDecimal price;
        private BigDecimal qty;
        private BigDecimal target;
        private BigDecimal stop;
        private BigDecimal profit;
        private String reason;
    }
}
