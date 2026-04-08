package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 每档止盈止损优化器 - 专为 FundPriceUpdate2 设计
 * 
 * 核心改进（基于您的代码逻辑）：
 * 1. 【盈亏平衡优先】止盈线 = max(原止盈逻辑, 盈亏平衡价 + 0.2%)
 * 2. 【动态止损】每档独立止损 = 成本价 - 0.8%，或跌破下2档
 * 3. 【分层止盈】到达盈亏平衡后，卖80%保本金，留20%博弈
 * 4. 【快速回本】被套档位优先做T，盈利档位优先止盈
 * 
 * 集成方式：在 FundPriceUpdate2 的止盈判断处调用
 */
@Slf4j
@Component
public class GridLevelProfitOptimizer {

    // ==================== 优化参数 ====================
    
    /** 手续费缓冲（0.15%，覆盖买卖成本） */
    private static final BigDecimal FEE_BUFFER = new BigDecimal("1.0015");
    /** 盈亏平衡后微利润（0.2%） */
    private static final BigDecimal PROFIT_BUFFER = new BigDecimal("1.002");
    /** 档位止损比例（0.8%） */
    private static final BigDecimal STOP_LOSS_PCT = new BigDecimal("0.008");
    /** 快速回本卖出比例（80%） */
    private static final BigDecimal FAST_EXIT_RATIO = new BigDecimal("0.80");
    /** 移动止盈回撤比例（3%） */
    private static final BigDecimal TRAILING_STOP_PCT = new BigDecimal("0.03");
    
    // ==================== 档位状态管理 ====================
    
    @Data
    @Builder
    public static class GridLevelState {
        private Integer level;               // 档位编号
        private BigDecimal buyPrice;         // 买入价（成本）
        private BigDecimal quantity;         // 持仓数量
        private BigDecimal breakevenPrice;   // 盈亏平衡价（含手续费）
        private BigDecimal takeProfitPrice;  // 止盈价
        private BigDecimal stopLossPrice;    // 止损价
        private BigDecimal highestPrice;     // 最高价（移动止盈用）
        private boolean breakevenHit;        // 是否已回本
        private int status;                  // 0:空仓 1:持仓 2:部分止盈
        private List<BatchTrade> batches;    // 分批买入记录
        
        /**
         * 计算当前盈亏平衡价
         */
        public BigDecimal calculateBreakeven() {
            if (batches == null || batches.isEmpty()) {
                return buyPrice.multiply(FEE_BUFFER);
            }
            
            BigDecimal totalCost = BigDecimal.ZERO;
            BigDecimal totalQty = BigDecimal.ZERO;
            
            for (BatchTrade batch : batches) {
                totalCost = totalCost.add(batch.getPrice().multiply(batch.getQuantity()));
                totalQty = totalQty.add(batch.getQuantity());
            }
            
            if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
                return buyPrice.multiply(FEE_BUFFER);
            }
            
            BigDecimal avgCost = totalCost.divide(totalQty, 8, RoundingMode.HALF_UP);
            return avgCost.multiply(FEE_BUFFER);
        }
        
        /**
         * 更新最高价（移动止盈）
         */
        public void updateHighestPrice(BigDecimal currentPrice) {
            if (highestPrice == null || currentPrice.compareTo(highestPrice) > 0) {
                highestPrice = currentPrice;
            }
        }
        
        /**
         * 获取移动止盈价
         */
        public BigDecimal getTrailingStopPrice() {
            if (highestPrice == null) return null;
            return highestPrice.multiply(BigDecimal.ONE.subtract(TRAILING_STOP_PCT));
        }
    }
    
    @Data
    @Builder
    public static class BatchTrade {
        private BigDecimal price;
        private BigDecimal quantity;
        private long timestamp;
    }
    
    // ==================== 核心优化方法 ====================
    
    /**
     * 优化止盈价（在 FundPriceUpdate2 中替代原 zhiying 计算）
     * 
     * @param levelState 档位状态
     * @param currentPrice 当前价格
     * @param maxPriceLine 最高价线（从Redis获取的maxValueLine）
     * @param originalZhiying 原代码计算的止盈价
     * @return 优化后的止盈价
     */
    public BigDecimal optimizeTakeProfitPrice(GridLevelState levelState, BigDecimal currentPrice,
                                               BigDecimal maxPriceLine, BigDecimal originalZhiying) {
        // 1. 计算盈亏平衡价
        BigDecimal breakeven = levelState.calculateBreakeven();
        
        // 2. 确保止盈价不低于盈亏平衡价 + 0.2%
        BigDecimal minTpPrice = breakeven.multiply(PROFIT_BUFFER);
        
        // 3. 取原止盈价和最低止盈价的较大者
        BigDecimal optimizedTp = originalZhiying.compareTo(minTpPrice) > 0 ? 
            originalZhiying : minTpPrice;
        
        // 4. 更新档位状态
        levelState.setBreakevenPrice(breakeven);
        levelState.setTakeProfitPrice(optimizedTp);
        
        // 5. 检查是否已回本
        if (!levelState.isBreakevenHit() && currentPrice.compareTo(breakeven) >= 0) {
            levelState.setBreakevenHit(true);
            log.info("【档位{}】首次回本！盈亏平衡价{}，当前价{}", 
                levelState.getLevel(), breakeven, currentPrice);
        }
        
        return optimizedTp;
    }
    
    /**
     * 检查是否触发止盈（优化版）
     * 
     * 返回：
     * - PARTIAL_EXIT: 触发快速回本（卖80%）
     * - FULL_EXIT: 触发原止盈逻辑
     * - TRAILING_EXIT: 触发移动止盈
     * - HOLD: 持有
     */
    public ExitAdvice checkTakeProfit(GridLevelState levelState, BigDecimal currentPrice, 
                                       BigDecimal originalZhiying) {
        // 更新最高价
        levelState.updateHighestPrice(currentPrice);
        
        BigDecimal breakeven = levelState.calculateBreakeven();
        
        // 1. 【快速回本】首次到达盈亏平衡价，建议卖80%
        if (levelState.isBreakevenHit() && levelState.getStatus() == 1 && 
            currentPrice.compareTo(breakeven.multiply(PROFIT_BUFFER)) >= 0 &&
            currentPrice.compareTo(originalZhiying) < 0) {
            
            return ExitAdvice.builder()
                .action(ExitAction.PARTIAL_EXIT)
                .ratio(FAST_EXIT_RATIO)
                .price(currentPrice)
                .reason(String.format("档位%d快速回本：盈亏平衡价%.4f，当前%.4f，卖%.0f%%保本金", 
                    levelState.getLevel(), breakeven, currentPrice, 
                    FAST_EXIT_RATIO.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 2. 【原止盈逻辑】到达计算止盈价
        if (originalZhiying.compareTo(new BigDecimal("0")) > 0 && 
            currentPrice.compareTo(originalZhiying) <= 0 &&
            currentPrice.compareTo(breakeven.multiply(new BigDecimal("1.001"))) > 0) {
            
            // 如果已经部分止盈，这次全部卖出
            BigDecimal ratio = levelState.getStatus() == 2 ? BigDecimal.ONE : BigDecimal.ONE;
            
            return ExitAdvice.builder()
                .action(ExitAction.FULL_EXIT)
                .ratio(ratio)
                .price(currentPrice)
                .reason(String.format("档位%d到达止盈价%.4f", levelState.getLevel(), originalZhiying))
                .build();
        }
        
        // 3. 【移动止盈】从最高点回撤3%
        BigDecimal trailingStop = levelState.getTrailingStopPrice();
        if (trailingStop != null && currentPrice.compareTo(trailingStop) <= 0 &&
            currentPrice.compareTo(breakeven) > 0) {
            
            return ExitAdvice.builder()
                .action(ExitAction.TRAILING_EXIT)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("档位%d移动止盈：从高点%.4f回撤3%%至%.4f", 
                    levelState.getLevel(), levelState.getHighestPrice(), currentPrice))
                .build();
        }
        
        return ExitAdvice.builder()
            .action(ExitAction.HOLD)
            .reason("持有中")
            .build();
    }
    
    /**
     * 检查止损（每档独立）
     * 
     * 优先级：
     * 1. 跌破下2档价格（网格逻辑）
     * 2. 跌破成本价0.8%
     * 3. 持仓超48小时
     */
    public ExitAdvice checkStopLoss(GridLevelState levelState, BigDecimal currentPrice,
                                     BigDecimal lower2GridPrice, int holdHours) {
        BigDecimal breakeven = levelState.calculateBreakeven();
        BigDecimal stopLossPrice = levelState.getBuyPrice().multiply(
            BigDecimal.ONE.subtract(STOP_LOSS_PCT));
        
        // 1. 跌破下2档
        if (lower2GridPrice != null && currentPrice.compareTo(lower2GridPrice) <= 0) {
            return ExitAdvice.builder()
                .action(ExitAction.STOP_LOSS)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("档位%d跌破下2档价格%.4f", levelState.getLevel(), lower2GridPrice))
                .build();
        }
        
        // 2. 跌破成本0.8%
        if (currentPrice.compareTo(stopLossPrice) <= 0) {
            return ExitAdvice.builder()
                .action(ExitAction.STOP_LOSS)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("档位%d跌破止损价%.4f（成本%.4f的%.1f%%）", 
                    levelState.getLevel(), stopLossPrice, levelState.getBuyPrice(), 
                    STOP_LOSS_PCT.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 3. 持仓超时
        if (holdHours >= 48) {
            return ExitAdvice.builder()
                .action(ExitAction.TIME_EXIT)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("档位%d持仓超48小时", levelState.getLevel()))
                .build();
        }
        
        return ExitAdvice.builder()
            .action(ExitAction.HOLD)
            .reason("持有中")
            .build();
    }
    
    /**
     * 判断档位是否适合做T
     * 
     * 原则：
     * - 盈利档位：做反T（减仓）
     * - 亏损档位：做正T（但需谨慎，防止越套越深）
     */
    public TRecommendation recommendT(GridLevelState levelState, BigDecimal currentPrice) {
        BigDecimal breakeven = levelState.calculateBreakeven();
        BigDecimal profitPct = currentPrice.subtract(breakeven)
            .divide(breakeven, 6, RoundingMode.HALF_UP);
        
        // 盈利超过1%：建议反T（卖出部分，低位买回）
        if (profitPct.compareTo(new BigDecimal("0.01")) > 0) {
            return TRecommendation.builder()
                .canDoT(true)
                .type(TType.SHORT_T)
                .suggestedRatio(new BigDecimal("0.3")) // 建议30%仓位
                .targetBuyBack(currentPrice.multiply(new BigDecimal("0.995"))) // 目标买回价
                .reason(String.format("档位%d盈利%.2f%%，建议反T", 
                    levelState.getLevel(), profitPct.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 亏损超过1%：谨慎正T（低位买入，高位卖出）
        if (profitPct.compareTo(new BigDecimal("-0.01")) < 0) {
            return TRecommendation.builder()
                .canDoT(true)
                .type(TType.LONG_T)
                .suggestedRatio(new BigDecimal("0.2")) // 建议20%仓位（轻仓）
                .targetSell(currentPrice.multiply(new BigDecimal("1.005"))) // 目标卖出价
                .reason(String.format("档位%d亏损%.2f%%，谨慎正T（轻仓）", 
                    levelState.getLevel(), profitPct.multiply(new BigDecimal("100"))))
                .build();
        }
        
        // 盈亏平衡附近：不建议做T
        return TRecommendation.builder()
            .canDoT(false)
            .reason("盈亏平衡附近，空间不足，不做T")
            .build();
    }
    
    // ==================== 集成到 FundPriceUpdate2 的工具方法 ====================
    
    /**
     * 为 FundPriceUpdate2 创建档位状态
     * 
     * @param level 档位
     * @param buyPriceReal 实际买入价（cw.getBuypriceReal()）
     * @param quantity 数量
     * @return 档位状态
     */
    public GridLevelState createLevelState(Integer level, BigDecimal buyPriceReal, BigDecimal quantity) {
        GridLevelState state = GridLevelState.builder()
            .level(level)
            .buyPrice(buyPriceReal)
            .quantity(quantity)
            .batches(new ArrayList<>())
            .status(1)
            .breakevenHit(false)
            .build();
        
        // 添加初始批次
        state.getBatches().add(BatchTrade.builder()
            .price(buyPriceReal)
            .quantity(quantity)
            .timestamp(System.currentTimeMillis())
            .build());
        
        // 计算初始值
        state.setBreakevenPrice(state.calculateBreakeven());
        state.setStopLossPrice(buyPriceReal.multiply(BigDecimal.ONE.subtract(STOP_LOSS_PCT)));
        
        return state;
    }
    
    // ==================== 数据类 ====================
    
    public enum ExitAction {
        HOLD,           // 持有
        PARTIAL_EXIT,   // 部分止盈（快速回本）
        FULL_EXIT,      // 全部止盈
        TRAILING_EXIT,  // 移动止盈
        STOP_LOSS,      // 止损
        TIME_EXIT       // 时间出场
    }
    
    public enum TType {
        LONG_T,   // 正T
        SHORT_T   // 反T
    }
    
    @Data
    @Builder
    public static class ExitAdvice {
        private ExitAction action;
        private BigDecimal ratio;    // 卖出比例
        private BigDecimal price;
        private String reason;
    }
    
    @Data
    @Builder
    public static class TRecommendation {
        private boolean canDoT;
        private TType type;
        private BigDecimal suggestedRatio;
        private BigDecimal targetBuyBack;
        private BigDecimal targetSell;
        private String reason;
    }
}
