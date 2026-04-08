package cn.exrick.manager.service.task;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 高速做T引擎 - 专为降低成本设计
 * 
 * 核心能力：
 * 1. 【振幅识别】实时计算N秒内的波动区间，识别有效T空间
 * 2. 【精准买卖点】基于价格偏离度+成交量+微趋势判断最佳进出场点
 * 3. 【正T+反T】双向套利，下跌时正T（先买后卖），上涨时反T（先卖后买）
 * 4. 【极速风控】单笔T最大持仓30秒，0.3%强制止损，确保不套住
 */
@Slf4j
@Component
public class HighSpeedTTradingEngine {

    // ==================== 核心配置 ====================
    
    /** 最小做T振幅（0.4%以上才做，覆盖手续费+利润） */
    private static final BigDecimal MIN_T_AMPLITUDE = new BigDecimal("0.004");
    /** 理想做T振幅（0.8%以上优先做） */
    private static final BigDecimal IDEAL_AMPLITUDE = new BigDecimal("0.008");
    /** 单笔T最大持仓时间（秒） */
    private static final int MAX_HOLD_SECONDS = 30;
    /** T单止损线（0.3%） */
    private static final BigDecimal T_STOP_LOSS = new BigDecimal("0.003");
    /** T单止盈线（0.5%） */
    private static final BigDecimal T_TAKE_PROFIT = new BigDecimal("0.005");
    /** 价格采样窗口（秒） */
    private static final int PRICE_WINDOW_SECONDS = 60;
    
    // ==================== 振幅识别器 ====================
    
    @Data
    public static class AmplitudeAnalyzer {
        private String symbol;
        /** 价格采样队列 (时间戳, 价格) */
        private LinkedList<PricePoint> priceWindow = new LinkedList<>();
        /** 今日高低点 */
        private BigDecimal todayHigh = BigDecimal.ZERO;
        private BigDecimal todayLow = new BigDecimal("999999999");
        /** 实时振幅 */
        private BigDecimal currentAmplitude = BigDecimal.ZERO;
        /** 波动率评级 */
        private VolatilityLevel volatilityLevel = VolatilityLevel.LOW;
        
        public void onPriceTick(BigDecimal price, long timestamp) {
            // 清理过期数据
            long cutoff = timestamp - PRICE_WINDOW_SECONDS * 1000;
            while (!priceWindow.isEmpty() && priceWindow.peekFirst().timestamp < cutoff) {
                priceWindow.pollFirst();
            }
            
            // 添加新价格
            priceWindow.addLast(new PricePoint(price, timestamp));
            
            // 更新高低点
            if (price.compareTo(todayHigh) > 0) todayHigh = price;
            if (price.compareTo(todayLow) < 0) todayLow = price;
            
            // 计算实时振幅
            if (priceWindow.size() >= 10) {
                BigDecimal windowHigh = priceWindow.stream()
                    .map(p -> p.price)
                    .max(BigDecimal::compareTo)
                    .orElse(price);
                BigDecimal windowLow = priceWindow.stream()
                    .map(p -> p.price)
                    .min(BigDecimal::compareTo)
                    .orElse(price);
                
                BigDecimal avgPrice = windowHigh.add(windowLow)
                    .divide(new BigDecimal("2"), 8, RoundingMode.HALF_UP);
                currentAmplitude = windowHigh.subtract(windowLow)
                    .divide(avgPrice, 6, RoundingMode.HALF_UP);
                
                // 评级波动率
                if (currentAmplitude.compareTo(new BigDecimal("0.015")) > 0) {
                    volatilityLevel = VolatilityLevel.EXTREME;
                } else if (currentAmplitude.compareTo(new BigDecimal("0.008")) > 0) {
                    volatilityLevel = VolatilityLevel.HIGH;
                } else if (currentAmplitude.compareTo(new BigDecimal("0.004")) > 0) {
                    volatilityLevel = VolatilityLevel.MEDIUM;
                } else {
                    volatilityLevel = VolatilityLevel.LOW;
                }
            }
        }
        
        /**
         * 获取当前价格位置（0=最低点，100=最高点）
         */
        public int getPricePosition(BigDecimal currentPrice) {
            if (priceWindow.size() < 10) return 50;
            
            BigDecimal windowHigh = priceWindow.stream()
                .map(p -> p.price)
                .max(BigDecimal::compareTo)
                .orElse(currentPrice);
            BigDecimal windowLow = priceWindow.stream()
                .map(p -> p.price)
                .min(BigDecimal::compareTo)
                .orElse(currentPrice);
            
            if (windowHigh.compareTo(windowLow) == 0) return 50;
            
            return currentPrice.subtract(windowLow)
                .multiply(new BigDecimal("100"))
                .divide(windowHigh.subtract(windowLow), 0, RoundingMode.HALF_UP)
                .intValue();
        }
        
        /**
         * 预测下一个高低点
         */
        public PricePrediction predictNextExtreme() {
            if (priceWindow.size() < 20) return null;
            
            // 简单趋势分析（实际可用更复杂的算法）
            List<PricePoint> recent = new ArrayList<>(priceWindow);
            int size = recent.size();
            
            // 计算短期斜率
            BigDecimal priceChange = recent.get(size - 1).price
                .subtract(recent.get(size - 10).price);
            BigDecimal slope = priceChange.divide(
                new BigDecimal("10"), 8, RoundingMode.HALF_UP);
            
            // 计算加速度
            BigDecimal prevChange = recent.get(size - 10).price
                .subtract(recent.get(size - 20).price);
            BigDecimal acceleration = priceChange.subtract(prevChange)
                .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
            
            return PricePrediction.builder()
                .currentAmplitude(currentAmplitude)
                .volatilityLevel(volatilityLevel)
                .slope(slope)
                .acceleration(acceleration)
                .isTrendingUp(slope.compareTo(BigDecimal.ZERO) > 0)
                .build();
        }
    }
    
    @Data
    @Builder
    public static class PricePoint {
        private BigDecimal price;
        private long timestamp;
    }
    
    @Data
    @Builder
    public static class PricePrediction {
        private BigDecimal currentAmplitude;
        private VolatilityLevel volatilityLevel;
        private BigDecimal slope;
        private BigDecimal acceleration;
        private boolean isTrendingUp;
    }
    
    public enum VolatilityLevel {
        LOW, MEDIUM, HIGH, EXTREME
    }
    
    // ==================== 买卖点识别器 ====================
    
    @Data
    public static class EntryExitDetector {
        
        /**
         * 识别正T买点（先买后卖）
         * 条件：价格接近近期低点 + 有反弹迹象 + 振幅足够
         */
        public static Signal detectLongTEntry(AmplitudeAnalyzer analyzer, 
                                               BigDecimal currentPrice,
                                               BigDecimal avgCost) {
            // 基本过滤
            if (analyzer.getVolatilityLevel().ordinal() < VolatilityLevel.MEDIUM.ordinal()) {
                return Signal.builder().action(Action.NONE).reason("振幅不足").build();
            }
            
            int position = analyzer.getPricePosition(currentPrice);
            PricePrediction pred = analyzer.predictNextExtreme();
            
            // 买点条件：价格在低位(0-30) + 趋势转向上 + 当前价低于成本（降低平均成本）
            boolean isLowPosition = position <= 30;
            boolean isBelowCost = avgCost == null || currentPrice.compareTo(avgCost) < 0;
            boolean turningUp = pred != null && pred.getSlope().compareTo(new BigDecimal("-0.01")) > 0;
            
            if (isLowPosition && isBelowCost && turningUp) {
                // 计算目标卖点
                BigDecimal targetProfit = currentPrice.multiply(T_TAKE_PROFIT);
                BigDecimal targetSell = currentPrice.add(targetProfit);
                
                return Signal.builder()
                    .action(Action.LONG_T_ENTRY)
                    .price(currentPrice)
                    .targetExit(targetSell)
                    .stopLoss(currentPrice.multiply(new BigDecimal("0.997")))
                    .confidence(calculateConfidence(analyzer, position, true))
                    .reason(String.format("正T买点 位置%d%% 低于成本 反弹迹象 目标卖%.2f",
                        position, targetSell))
                    .build();
            }
            
            return Signal.builder()
                .action(Action.NONE)
                .reason(String.format("不满足买点 位置%d%% 低于成本%s 转向上%s",
                    position, isBelowCost, turningUp))
                .build();
        }
        
        /**
         * 识别反T卖点（先卖后买）
         * 条件：价格接近近期高点 + 有回落迹象 + 有持仓可卖
         */
        public static Signal detectShortTEntry(AmplitudeAnalyzer analyzer,
                                                BigDecimal currentPrice,
                                                BigDecimal avgCost,
                                                BigDecimal holdingQty) {
            if (holdingQty.compareTo(BigDecimal.ZERO) <= 0) {
                return Signal.builder().action(Action.NONE).reason("无持仓").build();
            }
            
            if (analyzer.getVolatilityLevel().ordinal() < VolatilityLevel.MEDIUM.ordinal()) {
                return Signal.builder().action(Action.NONE).reason("振幅不足").build();
            }
            
            int position = analyzer.getPricePosition(currentPrice);
            PricePrediction pred = analyzer.predictNextExtreme();
            
            // 卖点条件：价格在高位(70-100) + 当前价高于成本（有利润）+ 趋势转向下
            boolean isHighPosition = position >= 70;
            boolean isAboveCost = avgCost != null && 
                currentPrice.compareTo(avgCost.multiply(new BigDecimal("1.002"))) > 0;
            boolean turningDown = pred != null && pred.getSlope().compareTo(new BigDecimal("0.01")) < 0;
            
            if (isHighPosition && isAboveCost && turningDown) {
                // 计算目标买回价
                BigDecimal targetProfit = currentPrice.multiply(T_TAKE_PROFIT);
                BigDecimal targetBuy = currentPrice.subtract(targetProfit);
                
                return Signal.builder()
                    .action(Action.SHORT_T_ENTRY)
                    .price(currentPrice)
                    .targetExit(targetBuy)
                    .stopLoss(currentPrice.multiply(new BigDecimal("1.003")))
                    .confidence(calculateConfidence(analyzer, position, false))
                    .reason(String.format("反T卖点 位置%d%% 高于成本 回落迹象 目标买%.2f",
                        position, targetBuy))
                    .build();
            }
            
            return Signal.builder()
                .action(Action.NONE)
                .reason(String.format("不满足卖点 位置%d%% 高于成本%s 转向下%s",
                    position, isAboveCost, turningDown))
                .build();
        }
        
        /**
         * 检查平仓条件
         */
        public static Signal checkExit(TPosition position, BigDecimal currentPrice, 
                                        long currentTime) {
            // 1. 超时强制平仓（最重要！）
            long holdSeconds = (currentTime - position.getEntryTime()) / 1000;
            if (holdSeconds >= MAX_HOLD_SECONDS) {
                return Signal.builder()
                    .action(position.getType() == TType.LONG_T ? Action.LONG_T_EXIT : Action.SHORT_T_EXIT)
                    .price(currentPrice)
                    .confidence(100)
                    .reason("持仓超时" + holdSeconds + "秒，强制平仓")
                    .build();
            }
            
            // 2. 止盈
            if (position.getType() == TType.LONG_T) {
                // 正T止盈
                BigDecimal profit = currentPrice.subtract(position.getEntryPrice())
                    .divide(position.getEntryPrice(), 6, RoundingMode.HALF_UP);
                if (profit.compareTo(T_TAKE_PROFIT) >= 0) {
                    return Signal.builder()
                        .action(Action.LONG_T_EXIT)
                        .price(currentPrice)
                        .confidence(90)
                        .reason("正T止盈 " + profit.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%")
                        .build();
                }
                // 正T止损
                if (profit.abs().compareTo(T_STOP_LOSS) >= 0 && profit.compareTo(BigDecimal.ZERO) < 0) {
                    return Signal.builder()
                        .action(Action.LONG_T_EXIT)
                        .price(currentPrice)
                        .confidence(100)
                        .reason("正T止损 " + profit.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%")
                        .build();
                }
            } else {
                // 反T止盈（低买回来了）
                BigDecimal profit = position.getEntryPrice().subtract(currentPrice)
                    .divide(position.getEntryPrice(), 6, RoundingMode.HALF_UP);
                if (profit.compareTo(T_TAKE_PROFIT) >= 0) {
                    return Signal.builder()
                        .action(Action.SHORT_T_EXIT)
                        .price(currentPrice)
                        .confidence(90)
                        .reason("反T止盈 " + profit.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%")
                        .build();
                }
                // 反T止损（T飞了，高价买回）
                if (currentPrice.compareTo(position.getStopLossPrice()) >= 0) {
                    return Signal.builder()
                        .action(Action.SHORT_T_EXIT)
                        .price(currentPrice)
                        .confidence(100)
                        .reason("反T止损（T飞）")
                        .build();
                }
            }
            
            return Signal.builder().action(Action.NONE).reason("持有中").build();
        }
        
        private static int calculateConfidence(AmplitudeAnalyzer analyzer, int position, boolean isLong) {
            int confidence = 50;
            
            // 振幅加成
            if (analyzer.getVolatilityLevel() == VolatilityLevel.HIGH) confidence += 20;
            if (analyzer.getVolatilityLevel() == VolatilityLevel.EXTREME) confidence += 30;
            
            // 位置加成
            if (isLong && position <= 20) confidence += 20;
            if (!isLong && position >= 80) confidence += 20;
            
            return Math.min(confidence, 95);
        }
    }
    
    // ==================== 持仓管理 ====================
    
    @Data
    @Builder
    public static class TPosition {
        private String id;
        private String symbol;
        private TType type; // LONG_T正T, SHORT_T反T
        private BigDecimal entryPrice;
        private BigDecimal qty;
        private long entryTime;
        private BigDecimal targetExitPrice;
        private BigDecimal stopLossPrice;
        private BigDecimal costBefore; // 做T前的成本
        private BigDecimal expectedCostAfter; // 预期做T后的成本
    }
    
    public enum TType {
        LONG_T,   // 正T：先买后卖（低买高卖）
        SHORT_T   // 反T：先卖后买（高卖低买）
    }
    
    public enum Action {
        NONE,           // 无信号
        LONG_T_ENTRY,   // 正T买入
        LONG_T_EXIT,    // 正T卖出
        SHORT_T_ENTRY,  // 反T卖出
        SHORT_T_EXIT    // 反T买回
    }
    
    @Data
    @Builder
    public static class Signal {
        private Action action;
        private BigDecimal price;
        private BigDecimal targetExit;
        private BigDecimal stopLoss;
        private int confidence; // 0-100
        private String reason;
    }
    
    // ==================== 高速引擎主逻辑 ====================
    
    @Data
    public static class HighSpeedTEngine {
        private String symbol;
        private AmplitudeAnalyzer analyzer = new AmplitudeAnalyzer();
        /** 活跃T持仓 */
        private Map<String, TPosition> activePositions = new ConcurrentHashMap<>();
        /** 成本基准 */
        private BigDecimal baseCost = BigDecimal.ZERO;
        private BigDecimal baseQty = BigDecimal.ZERO;
        /** 统计 */
        private int totalTrades = 0;
        private int profitableTrades = 0;
        private BigDecimal totalCostReduction = BigDecimal.ZERO; // 累计降低成本
        
        /**
         * 主入口：每次价格更新调用
         */
        public List<TradeCommand> onPriceTick(BigDecimal price, long timestamp) {
            List<TradeCommand> commands = new ArrayList<>();
            
            // 1. 更新振幅分析
            analyzer.onPriceTick(price, timestamp);
            
            // 2. 检查现有持仓的平仓条件
            for (TPosition pos : new ArrayList<>(activePositions.values())) {
                Signal exitSignal = EntryExitDetector.checkExit(pos, price, timestamp);
                if (exitSignal.getAction() != Action.NONE) {
                    commands.add(createExitCommand(pos, exitSignal, price));
                    activePositions.remove(pos.getId());
                    updateStatistics(pos, price);
                }
            }
            
            // 3. 寻找新入场机会（限制同时持仓数量）
            if (activePositions.size() < 2) { // 最多2笔同时做T
                // 尝试正T
                Signal longSignal = EntryExitDetector.detectLongTEntry(analyzer, price, baseCost);
                if (longSignal.getAction() == Action.LONG_T_ENTRY && 
                    longSignal.getConfidence() >= 70) {
                    
                    String id = UUID.randomUUID().toString().substring(0, 8);
                    TPosition pos = TPosition.builder()
                        .id(id)
                        .symbol(symbol)
                        .type(TType.LONG_T)
                        .entryPrice(price)
                        .qty(calculateTQty())
                        .entryTime(timestamp)
                        .targetExitPrice(longSignal.getTargetExit())
                        .stopLossPrice(longSignal.getStopLoss())
                        .costBefore(baseCost)
                        .build();
                    
                    activePositions.put(id, pos);
                    commands.add(createEntryCommand(pos, longSignal));
                    
                    log.info("【正T开仓】{} 价{} 目标{} 置信度{}% {}", 
                        symbol, price, longSignal.getTargetExit(), 
                        longSignal.getConfidence(), longSignal.getReason());
                }
                
                // 尝试反T（有底仓才能做）
                if (baseQty.compareTo(BigDecimal.ZERO) > 0) {
                    Signal shortSignal = EntryExitDetector.detectShortTEntry(
                        analyzer, price, baseCost, baseQty);
                    if (shortSignal.getAction() == Action.SHORT_T_ENTRY && 
                        shortSignal.getConfidence() >= 70) {
                        
                        String id = UUID.randomUUID().toString().substring(0, 8);
                        TPosition pos = TPosition.builder()
                            .id(id)
                            .symbol(symbol)
                            .type(TType.SHORT_T)
                            .entryPrice(price)
                            .qty(calculateTQty().min(baseQty.multiply(new BigDecimal("0.5")))) // 最多卖50%底仓
                            .entryTime(timestamp)
                            .targetExitPrice(shortSignal.getTargetExit())
                            .stopLossPrice(shortSignal.getStopLoss())
                            .costBefore(baseCost)
                            .build();
                        
                        activePositions.put(id, pos);
                        commands.add(createEntryCommand(pos, shortSignal));
                        
                        log.info("【反T开仓】{} 价{} 目标买回{} 置信度{}% {}", 
                            symbol, price, shortSignal.getTargetExit(),
                            shortSignal.getConfidence(), shortSignal.getReason());
                    }
                }
            }
            
            return commands;
        }
        
        private BigDecimal calculateTQty() {
            // 单笔T使用10%资金
            return new BigDecimal("0.1");
        }
        
        private TradeCommand createEntryCommand(TPosition pos, Signal signal) {
            return TradeCommand.builder()
                .id(pos.getId())
                .symbol(symbol)
                .action(pos.getType() == TType.LONG_T ? "LONG_T_BUY" : "SHORT_T_SELL")
                .price(pos.getEntryPrice())
                .qty(pos.getQty())
                .tpPrice(pos.getTargetExitPrice())
                .slPrice(pos.getStopLossPrice())
                .confidence(signal.getConfidence())
                .reason(signal.getReason())
                .build();
        }
        
        private TradeCommand createExitCommand(TPosition pos, Signal signal, BigDecimal exitPrice) {
            return TradeCommand.builder()
                .id(pos.getId())
                .symbol(symbol)
                .action(pos.getType() == TType.LONG_T ? "LONG_T_SELL" : "SHORT_T_BUY")
                .price(exitPrice)
                .qty(pos.getQty())
                .confidence(signal.getConfidence())
                .reason(signal.getReason())
                .build();
        }
        
        private void updateStatistics(TPosition pos, BigDecimal exitPrice) {
            totalTrades++;
            BigDecimal profit;
            if (pos.getType() == TType.LONG_T) {
                profit = exitPrice.subtract(pos.getEntryPrice())
                    .divide(pos.getEntryPrice(), 6, RoundingMode.HALF_UP);
            } else {
                profit = pos.getEntryPrice().subtract(exitPrice)
                    .divide(pos.getEntryPrice(), 6, RoundingMode.HALF_UP);
            }
            
            if (profit.compareTo(BigDecimal.ZERO) > 0) {
                profitableTrades++;
                // 计算成本降低幅度
                BigDecimal reduction = profit.multiply(pos.getQty())
                    .multiply(pos.getEntryPrice())
                    .divide(baseQty, 8, RoundingMode.HALF_UP);
                totalCostReduction = totalCostReduction.add(reduction);
            }
            
            log.info("【T单结算】{} 类型{} 盈亏{}% 总交易{} 胜率{}% 累计降成本{}",
                symbol, pos.getType(), 
                profit.multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP),
                totalTrades,
                new BigDecimal(profitableTrades * 100).divide(
                    new BigDecimal(totalTrades), 1, RoundingMode.HALF_UP),
                totalCostReduction);
        }
        
        /**
         * 更新底仓成本（网格买入或卖出后调用）
         */
        public void updateBasePosition(BigDecimal newCost, BigDecimal newQty) {
            this.baseCost = newCost;
            this.baseQty = newQty;
        }
    }
    
    @Data
    @Builder
    public static class TradeCommand {
        private String id;
        private String symbol;
        private String action; // LONG_T_BUY, LONG_T_SELL, SHORT_T_SELL, SHORT_T_BUY
        private BigDecimal price;
        private BigDecimal qty;
        private BigDecimal tpPrice;
        private BigDecimal slPrice;
        private int confidence;
        private String reason;
    }
    
    // ==================== 使用示例 ====================
    
    public void example() {
        HighSpeedTEngine engine = new HighSpeedTEngine();
        engine.setSymbol("BTC-USDT-SWAP");
        
        // 模拟价格流
        long now = System.currentTimeMillis();
        engine.onPriceTick(new BigDecimal("70000"), now);
        engine.onPriceTick(new BigDecimal("69950"), now + 1000);
        engine.onPriceTick(new BigDecimal("69800"), now + 2000); // 低点，可能触发正T
        // ... 检测到正T买点，开仓
        
        engine.onPriceTick(new BigDecimal("69900"), now + 5000);
        engine.onPriceTick(new BigDecimal("70100"), now + 8000); // 到达止盈，平仓
        // ... 正T完成，降低成本
        
        engine.onPriceTick(new BigDecimal("70500"), now + 12000);
        engine.onPriceTick(new BigDecimal("70800"), now + 15000); // 高点，可能触发反T
        // ... 检测到反T卖点，开仓
        
        engine.onPriceTick(new BigDecimal("70500"), now + 18000);
        engine.onPriceTick(new BigDecimal("70200"), now + 22000); // 到达目标买回价
        // ... 反T完成，降低成本
    }
}
