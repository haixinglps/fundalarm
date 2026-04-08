package cn.exrick.manager.service.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 简化版每档风控 - 整体盈亏平衡 + 单档止盈止损
 * 
 * 设计理念：
 * 1. 【整体视角】只算一个总盈亏平衡价，不看每档
 * 2. 【每档任务】每档只设止损保护（-0.8%），不设止盈下限
 * 3. 【灵活止盈】每档到达网格上沿就卖，不纠结是否回本
 * 4. 【做T降本】通过高频做T来降低整体成本，不补仓摊薄
 * 
 * 在 FundPriceUpdate2 的止盈止损判断处调用
 */
@Slf4j
@Component
public class SimpleGridRiskManager {

    // ==================== 简化参数 ====================
    
    /** 单档止损：-0.8% */
    private static final BigDecimal GRID_STOP_LOSS = new BigDecimal("0.008");
    /** 移动止盈回撤：3% */
    private static final BigDecimal TRAILING_PCT = new BigDecimal("0.03");
    /** 做T止盈：+0.3% */
    private static final BigDecimal T_TAKE_PROFIT = new BigDecimal("0.003");
    /** 做T止损：-0.3% */
    private static final BigDecimal T_STOP_LOSS = new BigDecimal("0.003");
    /** 做T超时：15分钟 */
    private static final int T_TIMEOUT_MIN = 15;
    
    // ==================== 整体盈亏平衡计算 ====================
    
    @Data
    @Builder
    public static class PositionSummary {
        private String symbol;
        private BigDecimal totalQty;        // 总持仓
        private BigDecimal avgCost;         // 平均成本
        private BigDecimal totalInvested;   // 总投入
        private BigDecimal breakevenPrice;  // 盈亏平衡价（含手续费0.15%）
        private BigDecimal unrealizedPnl;   // 未实现盈亏
        
        /**
         * 计算盈亏平衡价
         */
        public void calculateBreakeven() {
            if (totalQty.compareTo(BigDecimal.ZERO) == 0) return;
            // 成本 + 0.15%手续费缓冲
            this.breakevenPrice = avgCost.multiply(new BigDecimal("1.0015"));
        }
        
        /**
         * 更新当前价格下的盈亏
         */
        public void updateUnrealizedPnl(BigDecimal currentPrice) {
            this.unrealizedPnl = currentPrice.subtract(avgCost).multiply(totalQty);
        }
        
        /**
         * 整体是否盈利
         */
        public boolean isOverallProfit(BigDecimal currentPrice) {
            return currentPrice.compareTo(breakevenPrice) > 0;
        }
    }
    
    // ==================== 单档风控（简化版）====================
    
    @Data
    @Builder
    public static class GridLevelRisk {
        private Integer level;              // 档位
        private BigDecimal buyPrice;        // 买入价
        private BigDecimal quantity;        // 数量
        private BigDecimal highestPrice;    // 最高价（移动止盈）
        private long buyTime;               // 买入时间
        private boolean isTOrder;           // 是否做T单
        
        /**
         * 检查止损
         */
        public boolean shouldStopLoss(BigDecimal currentPrice) {
            BigDecimal stopPrice = buyPrice.multiply(BigDecimal.ONE.subtract(GRID_STOP_LOSS));
            return currentPrice.compareTo(stopPrice) <= 0;
        }
        
        /**
         * 检查移动止盈
         */
        public boolean shouldTrailingStop(BigDecimal currentPrice) {
            if (highestPrice == null) {
                highestPrice = currentPrice;
                return false;
            }
            
            if (currentPrice.compareTo(highestPrice) > 0) {
                highestPrice = currentPrice;
                return false;
            }
            
            BigDecimal trailingPrice = highestPrice.multiply(BigDecimal.ONE.subtract(TRAILING_PCT));
            return currentPrice.compareTo(trailingPrice) <= 0;
        }
        
        /**
         * 获取档位当前盈亏
         */
        public BigDecimal getGridPnl(BigDecimal currentPrice) {
            return currentPrice.subtract(buyPrice).multiply(quantity);
        }
    }
    
    // ==================== 做T管理 ====================
    
    @Data
    @Builder
    public static class TOrder {
        private String id;
        private int type;  // 1:正T -1:反T
        private BigDecimal entryPrice;
        private BigDecimal qty;
        private long entryTime;
        private BigDecimal targetPrice;
        private BigDecimal stopPrice;
        
        /**
         * 检查平仓条件
         */
        public TCloseCheck checkClose(BigDecimal currentPrice, long now) {
            long holdMin = (now - entryTime) / 60000;
            
            // 止盈
            if (type == 1 && currentPrice.compareTo(targetPrice) >= 0) {
                return TCloseCheck.builder().shouldClose(true).reason("正T止盈").profit(calculateProfit(currentPrice)).build();
            }
            if (type == -1 && currentPrice.compareTo(targetPrice) <= 0) {
                return TCloseCheck.builder().shouldClose(true).reason("反T止盈").profit(calculateProfit(currentPrice)).build();
            }
            
            // 止损
            if (type == 1 && currentPrice.compareTo(stopPrice) <= 0) {
                return TCloseCheck.builder().shouldClose(true).reason("正T止损").profit(calculateProfit(currentPrice)).build();
            }
            if (type == -1 && currentPrice.compareTo(stopPrice) >= 0) {
                return TCloseCheck.builder().shouldClose(true).reason("反T止损").profit(calculateProfit(currentPrice)).build();
            }
            
            // 超时
            if (holdMin >= T_TIMEOUT_MIN) {
                BigDecimal profit = calculateProfit(currentPrice);
                String reason = profit.compareTo(BigDecimal.ZERO) > 0 ? "超时盈利" : "超时亏损";
                return TCloseCheck.builder().shouldClose(true).reason(reason).profit(profit).build();
            }
            
            return TCloseCheck.builder().shouldClose(false).build();
        }
        
        private BigDecimal calculateProfit(BigDecimal currentPrice) {
            if (type == 1) {
                return currentPrice.subtract(entryPrice).multiply(qty);
            } else {
                return entryPrice.subtract(currentPrice).multiply(qty);
            }
        }
    }
    
    // ==================== 核心风控决策 ====================
    
    /**
     * 单档卖出决策（简化版）
     * 
     * 决策优先级：
     * 1. 止损（-0.8%）→ 必须卖，认栽
     * 2. 移动止盈（回撤3%）→ 卖，保住利润
     * 3. 网格上沿 → 正常卖
     * 4. 其他 → 持有
     * 
     * @param levelRisk 档位风险状态
     * @param currentPrice 当前价
     * @param gridSellPrice 网格上沿卖出价
     * @return 卖出建议
     */
    public SellAdvice decideSell(GridLevelRisk levelRisk, BigDecimal currentPrice, 
                                  BigDecimal gridSellPrice) {
        // 1. 止损检查（最重要）
        if (levelRisk.shouldStopLoss(currentPrice)) {
            return SellAdvice.builder()
                .shouldSell(true)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("档位%d止损：买入价%.2f，当前%.2f，跌幅%.2f%%", 
                    levelRisk.getLevel(), levelRisk.getBuyPrice(), currentPrice,
                    GRID_STOP_LOSS.multiply(new BigDecimal("100"))))
                .isStopLoss(true)
                .build();
        }
        
        // 2. 移动止盈检查（有利润时保护）
        if (levelRisk.shouldTrailingStop(currentPrice)) {
            return SellAdvice.builder()
                .shouldSell(true)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("档位%d移动止盈：从高点%.2f回撤%.0f%%", 
                    levelRisk.getLevel(), levelRisk.getHighestPrice(), 
                    TRAILING_PCT.multiply(new BigDecimal("100"))))
                .isTrailingStop(true)
                .build();
        }
        
        // 3. 网格上沿卖出（正常网格逻辑）
        if (currentPrice.compareTo(gridSellPrice) >= 0) {
            return SellAdvice.builder()
                .shouldSell(true)
                .ratio(BigDecimal.ONE)
                .price(currentPrice)
                .reason(String.format("档位%d到达网格上沿%.2f", levelRisk.getLevel(), gridSellPrice))
                .isNormalSell(true)
                .build();
        }
        
        // 4. 持有
        return SellAdvice.builder()
            .shouldSell(false)
            .reason("持有中")
            .build();
    }
    
    /**
     * 开做T决策（根据市场）
     * 
     * @param summary 整体持仓
     * @param currentPrice 当前价
     * @param availableCash 可用现金
     * @param availableQty 可卖数量
     * @return 做T建议
     */
    public TAdvice decideT(PositionSummary summary, BigDecimal currentPrice,
                           BigDecimal availableCash, BigDecimal availableQty) {
        boolean isOverallProfit = summary.isOverallProfit(currentPrice);
        BigDecimal profitPct = currentPrice.subtract(summary.getAvgCost())
            .divide(summary.getAvgCost(), 6, RoundingMode.HALF_UP);
        
        // 整体盈利 + 价格偏高 → 反T（卖出，低位买回）
        if (isOverallProfit && profitPct.compareTo(new BigDecimal("0.01")) > 0 && 
            availableQty.compareTo(summary.getTotalQty().multiply(new BigDecimal("0.2"))) > 0) {
            
            BigDecimal tQty = summary.getTotalQty().multiply(new BigDecimal("0.1")); // 10%仓位
            BigDecimal targetBuy = currentPrice.multiply(new BigDecimal("0.995")); // 目标买回
            BigDecimal stopSell = currentPrice.multiply(new BigDecimal("1.003"));  // 止损（卖飞）
            
            return TAdvice.builder()
                .shouldDoT(true)
                .type(-1) // 反T
                .qty(tQty.min(availableQty))
                .entryPrice(currentPrice)
                .targetPrice(targetBuy)
                .stopPrice(stopSell)
                .reason(String.format("整体盈利%.2f%%，反T卖出%.4f，目标买回%.2f", 
                    profitPct.multiply(new BigDecimal("100")), tQty, targetBuy))
                .build();
        }
        
        // 整体亏损 + 价格偏低 → 正T（买入，高位卖出）
        if (!isOverallProfit && profitPct.compareTo(new BigDecimal("-0.01")) < 0 && 
            availableCash.compareTo(summary.getTotalInvested().multiply(new BigDecimal("0.1"))) > 0) {
            
            BigDecimal tQty = summary.getTotalQty().multiply(new BigDecimal("0.1"));
            BigDecimal targetSell = currentPrice.multiply(new BigDecimal("1.005"));
            BigDecimal stopBuy = currentPrice.multiply(new BigDecimal("0.997"));
            
            return TAdvice.builder()
                .shouldDoT(true)
                .type(1) // 正T
                .qty(tQty)
                .entryPrice(currentPrice)
                .targetPrice(targetSell)
                .stopPrice(stopBuy)
                .reason(String.format("整体亏损%.2f%%，正T买入%.4f，目标卖出%.2f", 
                    profitPct.abs().multiply(new BigDecimal("100")), tQty, targetSell))
                .build();
        }
        
        return TAdvice.builder()
            .shouldDoT(false)
            .reason("不满足做T条件")
            .build();
    }
    
    // ==================== 集成工具方法 ====================
    
    /**
     * 从 OKX 持仓数据构建 PositionSummary
     */
    public PositionSummary buildFromOkxPosition(String symbol, BigDecimal avgPx, 
                                                  BigDecimal pos, BigDecimal upl) {
        PositionSummary summary = PositionSummary.builder()
            .symbol(symbol)
            .avgCost(avgPx)
            .totalQty(pos.abs())
            .totalInvested(avgPx.multiply(pos.abs()))
            .build();
        
        summary.calculateBreakeven();
        return summary;
    }
    
    /**
     * 从仓位数据构建 GridLevelRisk
     */
    public GridLevelRisk buildLevelRisk(Integer level, BigDecimal buyPrice, BigDecimal qty) {
        return GridLevelRisk.builder()
            .level(level)
            .buyPrice(buyPrice)
            .quantity(qty)
            .buyTime(System.currentTimeMillis())
            .highestPrice(buyPrice)
            .build();
    }
    
    // ==================== 数据类 ====================
    
    @Data
    @Builder
    public static class SellAdvice {
        private boolean shouldSell;
        private BigDecimal ratio;      // 卖出比例
        private BigDecimal price;
        private String reason;
        private boolean isStopLoss;    // 是否止损
        private boolean isTrailingStop; // 是否移动止盈
        private boolean isNormalSell;  // 是否正常网格卖出
    }
    
    @Data
    @Builder
    public static class TAdvice {
        private boolean shouldDoT;
        private int type;              // 1:正T -1:反T
        private BigDecimal qty;
        private BigDecimal entryPrice;
        private BigDecimal targetPrice;
        private BigDecimal stopPrice;
        private String reason;
    }
    
    @Data
    @Builder
    public static class TCloseCheck {
        private boolean shouldClose;
        private String reason;
        private BigDecimal profit;
    }
}
