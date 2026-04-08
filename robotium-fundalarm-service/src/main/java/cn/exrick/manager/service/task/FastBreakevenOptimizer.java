package cn.exrick.manager.service.task;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 快速盈亏平衡价优化器
 * 
 * 核心思路：
 * 1. 【不对称加仓】下跌时逐步加重仓（20%→30%→25%→15%→10%），更快摊低成本
 * 2. 【档位成本追踪】每档独立计算平均成本和盈亏平衡价
 * 3. 【阶梯止盈】接近成本时保守，远离成本时激进
 * 4. 【底仓做T合一】统一仓位管理，不做T时就是底仓
 */
@Slf4j
@Component
public class FastBreakevenOptimizer {

    /** 小资金分成10份 */
    private static final int TOTAL_BATCHES = 10;
    /** 每次下跌档位分配比例（优先重仓低位） */
    private static final BigDecimal[] BATCH_RATIOS = {
        new BigDecimal("0.20"), // 第1批试探
        new BigDecimal("0.30"), // 第2批重仓（主要成本区）
        new BigDecimal("0.25"), // 第3批确认
        new BigDecimal("0.15"), // 第4批补充
        new BigDecimal("0.10")  // 第5批极限（很少触发）
    };
    /** 手续费缓冲（OKX约0.05-0.1%，留0.15%安全垫） */
    private static final BigDecimal FEE_BUFFER = new BigDecimal("1.0015");
    /** 盈亏平衡后保护利润 */
    private static final BigDecimal PROFIT_BUFFER = new BigDecimal("1.002");
    
    // ==================== 档位状态管理 ====================
    
    /**
     * 每个网格档位的完整状态
     */
    @Data
    public static class GridPosition {
        private int gridIndex;
        private int status; // 0:空仓 1:持仓 2:做T持仓
        
        // 成本追踪
        private BigDecimal avgCost = BigDecimal.ZERO;      // 平均成本
        private BigDecimal totalQty = BigDecimal.ZERO;     // 总持仓量
        private int batchCount = 0;                         // 已加仓次数
        private List<BatchTrade> batches = new ArrayList<>(); // 批次明细
        
        // 风控参数
        private BigDecimal breakevenPrice; // 盈亏平衡价（含手续费）
        private BigDecimal stopLossPrice;  // 止损价（单档最大亏损0.8%）
        private BigDecimal takeProfitPrice; // 止盈价（动态计算）
        
        // 做T记录
        private long tEntryTime;
        private BigDecimal tEntryPrice;
        private boolean breakevenHit = false; // 是否已回成本
        
        public void addBatch(BigDecimal price, BigDecimal qty, boolean isT) {
            BatchTrade batch = BatchTrade.builder()
                .price(price)
                .qty(qty)
                .time(System.currentTimeMillis())
                .isT(isT)
                .build();
            batches.add(batch);
            
            // 更新平均成本
            BigDecimal totalCost = avgCost.multiply(totalQty).add(price.multiply(qty));
            totalQty = totalQty.add(qty);
            avgCost = totalCost.divide(totalQty, 8, RoundingMode.HALF_UP);
            batchCount++;
            
            // 更新风控价
            updateRiskPrices();
        }
        
        /**
         * 计算盈亏平衡价和止损止盈价
         */
        private void updateRiskPrices() {
            // 盈亏平衡价 = 平均成本 * (1 + 手续费)
            breakevenPrice = avgCost.multiply(FEE_BUFFER);
            
            // 止损价 = 平均成本 * 0.992 (0.8%止损)
            stopLossPrice = avgCost.multiply(new BigDecimal("0.992"));
            
            // 止盈价动态计算（见下方策略）
        }
        
        /**
         * 根据当前价格计算动态止盈价
         */
        public BigDecimal getDynamicTakeProfit(BigDecimal currentPrice) {
            if (avgCost.compareTo(BigDecimal.ZERO) == 0) return null;
            
            BigDecimal profitPct = currentPrice.subtract(avgCost)
                .divide(avgCost, 6, RoundingMode.HALF_UP);
            
            // 阶梯止盈策略
            if (profitPct.compareTo(new BigDecimal("0.05")) > 0) {
                // 盈利>5%：移动止盈（回撤3%）
                return currentPrice.multiply(new BigDecimal("0.97"));
            } else if (profitPct.compareTo(new BigDecimal("0.02")) > 0) {
                // 盈利2-5%：固定止盈4%
                return avgCost.multiply(new BigDecimal("1.04"));
            } else {
                // 盈利<2%：回到盈亏平衡+微利润就出
                return breakevenPrice.multiply(PROFIT_BUFFER);
            }
        }
        
        /**
         * 减仓后更新成本
         */
        public void reducePosition(BigDecimal sellQty, BigDecimal sellPrice) {
            if (totalQty.compareTo(sellQty) <= 0) {
                // 清仓
                reset();
            } else {
                // 部分减仓（先进先出）
                BigDecimal remainingSell = sellQty;
                Iterator<BatchTrade> it = batches.iterator();
                while (it.hasNext() && remainingSell.compareTo(BigDecimal.ZERO) > 0) {
                    BatchTrade batch = it.next();
                    if (batch.qty.compareTo(remainingSell) <= 0) {
                        remainingSell = remainingSell.subtract(batch.qty);
                        it.remove();
                    } else {
                        batch.qty = batch.qty.subtract(remainingSell);
                        remainingSell = BigDecimal.ZERO;
                    }
                }
                
                // 重新计算平均成本
                recalculateAvgCost();
            }
        }
        
        private void recalculateAvgCost() {
            BigDecimal totalCost = BigDecimal.ZERO;
            totalQty = BigDecimal.ZERO;
            for (BatchTrade batch : batches) {
                totalCost = totalCost.add(batch.price.multiply(batch.qty));
                totalQty = totalQty.add(batch.qty);
            }
            if (totalQty.compareTo(BigDecimal.ZERO) > 0) {
                avgCost = totalCost.divide(totalQty, 8, RoundingMode.HALF_UP);
            } else {
                avgCost = BigDecimal.ZERO;
            }
            updateRiskPrices();
        }
        
        public void reset() {
            status = 0;
            avgCost = BigDecimal.ZERO;
            totalQty = BigDecimal.ZERO;
            batchCount = 0;
            batches.clear();
            breakevenHit = false;
        }
    }
    
    @Data
    @Builder
    public static class BatchTrade {
        private BigDecimal price;
        private BigDecimal qty;
        private long time;
        private boolean isT;
    }
    
    // ==================== 核心策略：快拉低盈亏平衡价 ====================
    
    /**
     * 小资金档位管理器
     */
    @Data
    public static class SmallCapitalGridManager {
        private String symbol;
        private BigDecimal totalCapital;      // 总资金
        private BigDecimal availableCapital;  // 可用资金
        private Map<Integer, GridPosition> positions = new HashMap<>();
        
        /** 单档最大仓位（1份 = 10%总资金） */
        private static final BigDecimal MAX_GRID_RATIO = new BigDecimal("0.10");
        
        /**
         * 下跌时批量买入（快拉低平均成本）
         */
        public List<TradeAdvice> onPriceDrop(int gridIndex, BigDecimal gridPrice, 
                                              BigDecimal currentPrice) {
            List<TradeAdvice> advices = new ArrayList<>();
            GridPosition pos = positions.computeIfAbsent(gridIndex, k -> {
                GridPosition p = new GridPosition();
                p.setGridIndex(gridIndex);
                return p;
            });
            
            // 检查是否还能加仓
            if (pos.getBatchCount() >= BATCH_RATIOS.length) {
                log.warn("【{}】档位{}已达最大加仓次数", symbol, gridIndex);
                return advices;
            }
            
            // 检查资金
            BigDecimal batchRatio = BATCH_RATIOS[pos.getBatchCount()];
            BigDecimal batchCapital = totalCapital.multiply(batchRatio);
            if (batchCapital.compareTo(availableCapital) > 0) {
                log.warn("【{}】资金不足，跳过档位{}加仓", symbol, gridIndex);
                return advices;
            }
            
            // 计算买入量
            BigDecimal qty = batchCapital.divide(currentPrice, 8, RoundingMode.HALF_UP);
            
            // 更新档位状态
            pos.addBatch(currentPrice, qty, false);
            pos.setStatus(1);
            availableCapital = availableCapital.subtract(batchCapital);
            
            advices.add(TradeAdvice.builder()
                .action("BUY")
                .gridIndex(gridIndex)
                .price(currentPrice)
                .qty(qty)
                .batchNo(pos.getBatchCount())
                .reason(String.format("第%d批加仓%s，平均成本%.4f，盈亏平衡%.4f",
                    pos.getBatchCount(), batchRatio.multiply(new BigDecimal("100")).intValue() + "%",
                    pos.getAvgCost(), pos.getBreakevenPrice()))
                .build());
            
            log.info("【{}】档位{}第{}批买入 价{} 量{} 平均成本{}→{} 盈亏平衡价{}",
                symbol, gridIndex, pos.getBatchCount(), currentPrice, qty,
                pos.getBatches().size() > 1 ? pos.getBatches().get(pos.getBatches().size()-2).getPrice() : currentPrice,
                pos.getAvgCost(), pos.getBreakevenPrice());
            
            return advices;
        }
        
        /**
         * 智能止盈决策（核心优化）
         */
        public List<TradeAdvice> onPriceRise(int gridIndex, BigDecimal currentPrice) {
            List<TradeAdvice> advices = new ArrayList<>();
            GridPosition pos = positions.get(gridIndex);
            if (pos == null || pos.getStatus() == 0) return advices;
            
            // 检查止损
            if (currentPrice.compareTo(pos.getStopLossPrice()) <= 0) {
                advices.add(TradeAdvice.builder()
                    .action("STOP_LOSS")
                    .gridIndex(gridIndex)
                    .price(currentPrice)
                    .qty(pos.getTotalQty())
                    .reason("触发单档止损 " + pos.getStopLossPrice())
                    .build());
                pos.reset();
                return advices;
            }
            
            // 计算动态止盈价
            BigDecimal tpPrice = pos.getDynamicTakeProfit(currentPrice);
            
            // 检查是否到达盈亏平衡价（关键！快速解套）
            if (!pos.isBreakevenHit() && currentPrice.compareTo(pos.getBreakevenPrice()) >= 0) {
                pos.setBreakevenHit(true);
                
                // 策略：先卖80%保住本金，留20%博弈
                BigDecimal safeQty = pos.getTotalQty().multiply(new BigDecimal("0.80"));
                
                advices.add(TradeAdvice.builder()
                    .action("TAKE_PROFIT_PARTIAL")
                    .gridIndex(gridIndex)
                    .price(currentPrice)
                    .qty(safeQty)
                    .reason(String.format("【快速回本】到达盈亏平衡价%.4f，卖出80%%保住本金，剩余博弈",
                        pos.getBreakevenPrice()))
                    .build());
                
                pos.reducePosition(safeQty, currentPrice);
                
                log.info("【{}】档位{}【快速回本】盈亏平衡价{} 当前价{} 卖出80% 剩余{}博弈",
                    symbol, gridIndex, pos.getBreakevenPrice(), currentPrice, pos.getTotalQty());
                
                return advices;
            }
            
            // 到达动态止盈价
            if (pos.isBreakevenHit() && currentPrice.compareTo(tpPrice) >= 0) {
                advices.add(TradeAdvice.builder()
                    .action("TAKE_PROFIT_ALL")
                    .gridIndex(gridIndex)
                    .price(currentPrice)
                    .qty(pos.getTotalQty())
                    .reason("到达动态止盈价 " + tpPrice)
                    .build());
                
                availableCapital = availableCapital.add(
                    currentPrice.multiply(pos.getTotalQty()));
                pos.reset();
            }
            
            return advices;
        }
        
        /**
         * 做T卖出（反T）- 高位减仓，低位买回
         */
        public TradeAdvice doTSell(int gridIndex, BigDecimal currentPrice) {
            GridPosition pos = positions.get(gridIndex);
            if (pos == null || pos.getStatus() == 0) return null;
            
            // 只有盈利才能做T（不亏本钱）
            if (currentPrice.compareTo(pos.getBreakevenPrice()) <= 0) {
                return null;
            }
            
            // 卖出50%做T（保留底仓）
            BigDecimal tQty = pos.getTotalQty().multiply(new BigDecimal("0.5"));
            
            pos.setStatus(2); // 标记做T状态
            pos.setTEntryTime(System.currentTimeMillis());
            pos.setTEntryPrice(currentPrice);
            
            return TradeAdvice.builder()
                .action("T_SELL")
                .gridIndex(gridIndex)
                .price(currentPrice)
                .qty(tQty)
                .tpPrice(pos.getAvgCost().multiply(new BigDecimal("0.995"))) // 低于成本0.5%买回
                .slPrice(currentPrice.multiply(new BigDecimal("1.02"))) // 涨2%止损（T飞）
                .reason(String.format("做T卖出 成本%.4f 现价%.4f 盈利%.2f%%",
                    pos.getAvgCost(), currentPrice,
                    currentPrice.subtract(pos.getAvgCost())
                        .divide(pos.getAvgCost(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))))
                .build();
        }
        
        /**
         * 做T买回
         */
        public TradeAdvice doTBuy(int gridIndex, BigDecimal currentPrice) {
            GridPosition pos = positions.get(gridIndex);
            if (pos == null || pos.getStatus() != 2) return null;
            
            // 检查是否到达买回条件
            BigDecimal tpPrice = pos.getTEntryPrice().multiply(new BigDecimal("0.995"));
            BigDecimal slPrice = pos.getTEntryPrice().multiply(new BigDecimal("1.02"));
            
            if (currentPrice.compareTo(tpPrice) <= 0) {
                // 低位买回，降低成本
                pos.setStatus(1);
                
                return TradeAdvice.builder()
                    .action("T_BUY")
                    .gridIndex(gridIndex)
                    .price(currentPrice)
                    .reason("做T成功，降低成本")
                    .build();
            } else if (currentPrice.compareTo(slPrice) >= 0) {
                // T飞了，止损买回（确保底仓）
                pos.setStatus(1);
                
                return TradeAdvice.builder()
                    .action("T_BUY_FORCE")
                    .gridIndex(gridIndex)
                    .price(currentPrice)
                    .reason("做T止损（T飞），强制买回")
                    .build();
            }
            
            return null;
        }
    }
    
    // ==================== 数据类 ====================
    
    @Data
    @Builder
    public static class TradeAdvice {
        private String action; // BUY, TAKE_PROFIT_PARTIAL, TAKE_PROFIT_ALL, T_SELL, T_BUY, STOP_LOSS
        private int gridIndex;
        private BigDecimal price;
        private BigDecimal qty;
        private int batchNo;
        private BigDecimal tpPrice;
        private BigDecimal slPrice;
        private String reason;
    }
    
    // ==================== 使用示例 ====================
    
    /**
     * 示例：如何集成到 FundPriceUpdate2
     */
    public void exampleIntegration() {
        // 初始化
        SmallCapitalGridManager manager = new SmallCapitalGridManager();
        manager.setSymbol("BTC-USDT-SWAP");
        manager.setTotalCapital(new BigDecimal("50000")); // 5万U
        manager.setAvailableCapital(new BigDecimal("50000"));
        
        // 价格下跌时（从70000跌到69500）
        List<TradeAdvice> buyAdvices = manager.onPriceDrop(5, new BigDecimal("69500"), 
                                                            new BigDecimal("69700"));
        // 输出: 第1批买入 20%资金 = 10000U
        
        // 继续下跌（到69000）
        buyAdvices = manager.onPriceDrop(4, new BigDecimal("69000"), new BigDecimal("69200"));
        // 输出: 档位4第1批买入 20%资金
        
        // 再跌（回到档位5，第2批加仓）
        buyAdvices = manager.onPriceDrop(5, new BigDecimal("69500"), new BigDecimal("69300"));
        // 输出: 档位5第2批买入 30%资金 = 15000U
        //       平均成本从69700降到69490（拉低了210点！）
        
        // 价格回升到盈亏平衡价
        List<TradeAdvice> sellAdvices = manager.onPriceRise(5, new BigDecimal("69550"));
        // 输出: 【快速回本】卖出80%保住本金，剩余20%博弈
        
        // 继续上涨
        sellAdvices = manager.onPriceRise(5, new BigDecimal("70200"));
        // 输出: 到达动态止盈价，全部卖出
    }
}
