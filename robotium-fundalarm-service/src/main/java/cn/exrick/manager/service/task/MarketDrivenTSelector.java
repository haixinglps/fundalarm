package cn.exrick.manager.service.task;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 市场驱动做T决策器 - 全自动判断正T/反T/观望
 * 
 * 决策逻辑：
 * 1. 【趋势判断】上涨势反T、下跌势正T、震荡势双向
 * 2. 【位置判断】高位反T、低位正T、中位观望
 * 3. 【动能判断】动能衰竭反向做T、动能延续顺势做T
 * 4. 【量价配合】放量上涨反T、放量下跌正T、缩量观望
 */
@Slf4j
@Component
public class MarketDrivenTSelector {

    // ==================== 决策权重配置 ====================
    
    /** 趋势权重 40% */
    private static final BigDecimal WEIGHT_TREND = new BigDecimal("0.40");
    /** 位置权重 30% */
    private static final BigDecimal WEIGHT_POSITION = new BigDecimal("0.30");
    /** 动能权重 20% */
    private static final BigDecimal WEIGHT_MOMENTUM = new BigDecimal("0.20");
    /** 量能权重 10% */
    private static final BigDecimal WEIGHT_VOLUME = new BigDecimal("0.10");
    
    /** 执行阈值：得分绝对值>60才执行，<40观望 */
    private static final int EXECUTE_THRESHOLD = 60;
    private static final int WATCH_THRESHOLD = 40;
    
    // ==================== 市场状态分析 ====================
    
    @Data
    @Builder
    public static class MarketContext {
        // 趋势指标 (-100到100，负为下跌，正为上涨)
        private int trendScore;
        private BigDecimal maSlope;        // 均线斜率
        private int higherHighs;           // 创新高次数
        private int lowerLows;             // 创新低次数
        
        // 位置指标 (-100到100，负为低位，正为高位)
        private int positionScore;
        private BigDecimal dayRangePosition; // 当日区间位置 0-1
        private BigDecimal distanceToResistance; // 距压力位距离
        private BigDecimal distanceToSupport;    // 距支撑位距离
        
        // 动能指标 (-100到100，负为下跌动能，正为上涨动能)
        private int momentumScore;
        private BigDecimal rsi;            // RSI值
        private BigDecimal macdHistogram;  // MACD柱状图
        private boolean isDivergence;      // 是否背离
        
        // 量能指标 (-100到100，负为缩量，正为放量)
        private int volumeScore;
        private BigDecimal volumeRatio;    // 量比
        private boolean isVolumeConfirming; // 量能是否配合价格
        
        // 综合评分
        private int overallScore;          // -100到100
        private TDirection recommendation; // 最终建议
        private String reason;             // 决策理由
    }
    
    public enum TDirection {
        LONG_T,      // 正T：先买后卖
        SHORT_T,     // 反T：先卖后买
        HOLD,        // 观望
        BOTH         // 双向（震荡市）
    }
    
    // ==================== 实时分析器 ====================
    
    @Data
    public static class MarketAnalyzer {
        private LinkedList<Bar> bars = new LinkedList<>();
        private static final int LOOKBACK = 20;
        
        /**
         * 分析当前市场状态
         */
        public MarketContext analyze(BigDecimal price, BigDecimal volume, long time) {
            updateBars(price, volume, time);
            if (bars.size() < LOOKBACK) {
                return MarketContext.builder()
                    .recommendation(TDirection.HOLD)
                    .reason("数据不足")
                    .build();
            }
            
            // 分别计算各项得分
            int trendScore = calculateTrendScore();
            int positionScore = calculatePositionScore(price);
            int momentumScore = calculateMomentumScore();
            int volumeScore = calculateVolumeScore();
            
            // 加权计算综合得分
            BigDecimal overall = new BigDecimal(trendScore).multiply(WEIGHT_TREND)
                .add(new BigDecimal(positionScore).multiply(WEIGHT_POSITION))
                .add(new BigDecimal(momentumScore).multiply(WEIGHT_MOMENTUM))
                .add(new BigDecimal(volumeScore).multiply(WEIGHT_VOLUME));
            
            int overallScore = overall.intValue();
            
            // 根据得分决定方向
            TDirection direction;
            String reason;
            
            if (Math.abs(overallScore) < WATCH_THRESHOLD) {
                direction = TDirection.HOLD;
                reason = "信号不明确，观望";
            } else if (overallScore >= EXECUTE_THRESHOLD) {
                // 强势市场：高位反T
                if (positionScore > 50) {
                    direction = TDirection.SHORT_T;
                    reason = buildReason("强势高位", trendScore, positionScore, momentumScore, volumeScore);
                } else {
                    direction = TDirection.HOLD;
                    reason = "上涨中继，观望等高位";
                }
            } else if (overallScore <= -EXECUTE_THRESHOLD) {
                // 弱势市场：低位正T
                if (positionScore < -50) {
                    direction = TDirection.LONG_T;
                    reason = buildReason("弱势低位", trendScore, positionScore, momentumScore, volumeScore);
                } else {
                    direction = TDirection.HOLD;
                    reason = "下跌中继，观望等低位";
                }
            } else {
                // 震荡区间
                if (Math.abs(trendScore) < 30 && Math.abs(positionScore) > 60) {
                    direction = overallScore > 0 ? TDirection.SHORT_T : TDirection.LONG_T;
                    reason = buildReason("震荡 extremes", trendScore, positionScore, momentumScore, volumeScore);
                } else {
                    direction = TDirection.HOLD;
                    reason = "震荡市，等待明确信号";
                }
            }
            
            return MarketContext.builder()
                .trendScore(trendScore)
                .positionScore(positionScore)
                .momentumScore(momentumScore)
                .volumeScore(volumeScore)
                .overallScore(overallScore)
                .recommendation(direction)
                .reason(reason)
                .build();
        }
        
        /**
         * 趋势得分：均线斜率+高低点结构
         */
        private int calculateTrendScore() {
            List<Bar> recent = new ArrayList<>(bars);
            int size = recent.size();
            
            // 均线斜率
            BigDecimal ma5_now = ma(recent, 5, size - 1);
            BigDecimal ma5_prev = ma(recent, 5, size - 6);
            BigDecimal slope = ma5_now.subtract(ma5_prev).divide(ma5_prev, 6, RoundingMode.HALF_UP);
            
            int trend = slope.multiply(new BigDecimal("5000")).intValue(); // 放大到-50到50
            
            // 高低点结构
            int hh = 0, ll = 0;
            for (int i = size - 5; i < size; i++) {
                if (recent.get(i).getHigh().compareTo(recent.get(i-1).getHigh()) > 0) hh++;
                if (recent.get(i).getLow().compareTo(recent.get(i-1).getLow()) < 0) ll++;
            }
            
            // 综合：上涨势+创新高=正分，下跌势+创新低=负分
            if (trend > 10 && hh >= 3) trend += 20;
            if (trend < -10 && ll >= 3) trend -= 20;
            
            return Math.max(-100, Math.min(100, trend));
        }
        
        /**
         * 位置得分：当前价格在日内区间的位置
         */
        private int calculatePositionScore(BigDecimal currentPrice) {
            List<Bar> recent = new ArrayList<>(bars);
            BigDecimal dayLow = recent.stream().map(Bar::getLow)
                .min(BigDecimal::compareTo).orElse(currentPrice);
            BigDecimal dayHigh = recent.stream().map(Bar::getHigh)
                .max(BigDecimal::compareTo).orElse(currentPrice);
            
            if (dayHigh.compareTo(dayLow) == 0) return 0;
            
            BigDecimal position = currentPrice.subtract(dayLow)
                .divide(dayHigh.subtract(dayLow), 6, RoundingMode.HALF_UP);
            
            // 映射到-100到100
            return position.multiply(new BigDecimal("200")).subtract(new BigDecimal("100")).intValue();
        }
        
        /**
         * 动能得分：RSI+MACD+背离
         */
        private int calculateMomentumScore() {
            List<Bar> recent = new ArrayList<>(bars);
            
            // 简化RSI计算（实际可用更精确算法）
            BigDecimal rsi = calculateSimplifiedRSI(recent);
            
            // RSI映射：>70超买（负分），<30超卖（正分）
            int score;
            if (rsi.compareTo(new BigDecimal("70")) > 0) {
                score = 70 - rsi.intValue(); // 70以上递减
            } else if (rsi.compareTo(new BigDecimal("30")) < 0) {
                score = 30 - rsi.intValue(); // 30以下递增
            } else {
                score = 0;
            }
            
            // 背离检测
            if (isDivergence(recent)) {
                score = score > 0 ? 80 : -80; // 背离时强化信号
            }
            
            return Math.max(-100, Math.min(100, score * 2)); // 放大
        }
        
        /**
         * 量能得分：量价配合度
         */
        private int calculateVolumeScore() {
            List<Bar> recent = new ArrayList<>(bars);
            int size = recent.size();
            
            // 量比
            BigDecimal avgVol = recent.subList(0, size - 1).stream()
                .map(Bar::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(size - 1), 8, RoundingMode.HALF_UP);
            BigDecimal currentVol = recent.get(size - 1).getVolume();
            BigDecimal volRatio = currentVol.divide(avgVol, 6, RoundingMode.HALF_UP);
            
            // 价量配合
            BigDecimal priceChange = recent.get(size - 1).getClose()
                .subtract(recent.get(size - 2).getClose())
                .divide(recent.get(size - 2).getClose(), 6, RoundingMode.HALF_UP);
            
            boolean volumeConfirming = 
                (priceChange.compareTo(BigDecimal.ZERO) > 0 && volRatio.compareTo(new BigDecimal("1.2")) > 0) ||
                (priceChange.compareTo(BigDecimal.ZERO) < 0 && volRatio.compareTo(new BigDecimal("1.2")) > 0);
            
            int score = volRatio.subtract(new BigDecimal("1")).multiply(new BigDecimal("100")).intValue();
            if (volumeConfirming) score *= 1.5;
            
            return Math.max(-100, Math.min(100, score));
        }
        
        private BigDecimal ma(List<Bar> bars, int period, int endIdx) {
            if (endIdx < period - 1) return bars.get(endIdx).getClose();
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = endIdx - period + 1; i <= endIdx; i++) {
                sum = sum.add(bars.get(i).getClose());
            }
            return sum.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);
        }
        
        private BigDecimal calculateSimplifiedRSI(List<Bar> bars) {
            // 简化14周期RSI
            BigDecimal gain = BigDecimal.ZERO, loss = BigDecimal.ZERO;
            for (int i = bars.size() - 14; i < bars.size(); i++) {
                BigDecimal change = bars.get(i).getClose().subtract(bars.get(i-1).getClose());
                if (change.compareTo(BigDecimal.ZERO) > 0) {
                    gain = gain.add(change);
                } else {
                    loss = loss.add(change.abs());
                }
            }
            if (loss.compareTo(BigDecimal.ZERO) == 0) return new BigDecimal("100");
            BigDecimal rs = gain.divide(loss, 6, RoundingMode.HALF_UP);
            return new BigDecimal("100").subtract(
                new BigDecimal("100").divide(new BigDecimal("1").add(rs), 6, RoundingMode.HALF_UP));
        }
        
        private boolean isDivergence(List<Bar> bars) {
            // 简化背离检测：价格新高但RSI未新高 = 顶背离
            int size = bars.size();
            if (size < 10) return false;
            
            BigDecimal priceNow = bars.get(size - 1).getHigh();
            BigDecimal pricePrev = bars.get(size - 6).getHigh();
            
            // 这里简化处理，实际需要计算两个点的RSI
            return priceNow.compareTo(pricePrev) > 0; // 简化判断
        }
        
        private String buildReason(String condition, int trend, int position, int momentum, int volume) {
            return String.format("%s 趋势%d 位置%d 动能%d 量能%d", 
                condition, trend, position, momentum, volume);
        }
        
        private void updateBars(BigDecimal price, BigDecimal volume, long time) {
            // 简化：假设每个tick是一个1分钟K线
            bars.addLast(Bar.builder()
                .open(price)
                .high(price)
                .low(price)
                .close(price)
                .volume(volume)
                .time(time)
                .build());
            while (bars.size() > LOOKBACK) bars.pollFirst();
        }
    }
    
    @Data
    @Builder
    public static class Bar {
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal volume;
        private long time;
    }
    
    // ==================== 执行引擎 ====================
    
    @Data
    public static class SmartTExecutor {
        private String symbol;
        private MarketAnalyzer analyzer = new MarketAnalyzer();
        
        // 当前持仓
        private BigDecimal baseCost;
        private BigDecimal baseQty;
        private BigDecimal availableCash;
        
        // 活跃订单
        private ActiveOrder activeOrder;
        private int dailyLongTCount = 0;
        private int dailyShortTCount = 0;
        private static final int MAX_DAILY_T = 5;
        
        /**
         * 主入口：每次价格更新
         */
        public List<TDecision> onTick(BigDecimal price, BigDecimal volume, long time) {
            List<TDecision> decisions = new ArrayList<>();
            
            // 1. 分析市场
            MarketContext ctx = analyzer.analyze(price, volume, time);
            
            // 2. 检查已有订单
            if (activeOrder != null) {
                TDecision exit = checkExit(activeOrder, price, time, ctx);
                if (exit != null) {
                    decisions.add(exit);
                    activeOrder = null;
                }
                return decisions; // 有持仓不新开仓
            }
            
            // 3. 检查日限制
            if (dailyLongTCount + dailyShortTCount >= MAX_DAILY_T) {
                return decisions;
            }
            
            // 4. 根据市场建议执行
            TDirection rec = ctx.getRecommendation();
            
            if (rec == TDirection.LONG_T && dailyLongTCount < 3) {
                TDecision entry = createLongT(price, ctx);
                if (entry != null) {
                    decisions.add(entry);
                    activeOrder = new ActiveOrder(entry.getOrderId(), TDirection.LONG_T, 
                        price, time, ctx);
                    dailyLongTCount++;
                }
            } else if (rec == TDirection.SHORT_T && dailyShortTCount < 3 && baseQty.compareTo(BigDecimal.ZERO) > 0) {
                TDecision entry = createShortT(price, ctx);
                if (entry != null) {
                    decisions.add(entry);
                    activeOrder = new ActiveOrder(entry.getOrderId(), TDirection.SHORT_T, 
                        price, time, ctx);
                    dailyShortTCount++;
                }
            }
            
            return decisions;
        }
        
        private TDecision createLongT(BigDecimal price, MarketContext ctx) {
            // 动态计算目标止损
            BigDecimal target = price.multiply(new BigDecimal("1.005")); // +0.5%
            BigDecimal stop = price.multiply(new BigDecimal("0.998"));  // -0.2%
            BigDecimal qty = baseQty.multiply(new BigDecimal("0.15")); // 15%仓位
            
            return TDecision.builder()
                .orderId("LT_" + System.currentTimeMillis() % 10000)
                .action("ENTRY")
                .direction(TDirection.LONG_T)
                .price(price)
                .qty(qty)
                .target(target)
                .stop(stop)
                .holdMinutes(10)
                .marketContext(ctx)
                .reason("【正T】" + ctx.getReason())
                .build();
        }
        
        private TDecision createShortT(BigDecimal price, MarketContext ctx) {
            BigDecimal target = price.multiply(new BigDecimal("0.995")); // -0.5%
            BigDecimal stop = price.multiply(new BigDecimal("1.003"));  // +0.3%
            BigDecimal qty = baseQty.multiply(new BigDecimal("0.15"));
            
            return TDecision.builder()
                .orderId("ST_" + System.currentTimeMillis() % 10000)
                .action("ENTRY")
                .direction(TDirection.SHORT_T)
                .price(price)
                .qty(qty)
                .target(target)
                .stop(stop)
                .holdMinutes(10)
                .marketContext(ctx)
                .reason("【反T】" + ctx.getReason())
                .build();
        }
        
        private TDecision checkExit(ActiveOrder order, BigDecimal price, long time, MarketContext ctx) {
            long holdMin = (time - order.getEntryTime()) / 60000;
            
            // 止盈
            if (order.getDirection() == TDirection.LONG_T && price.compareTo(order.getTarget()) >= 0) {
                return exitDecision(order, price, "止盈", true, ctx);
            }
            if (order.getDirection() == TDirection.SHORT_T && price.compareTo(order.getTarget()) <= 0) {
                return exitDecision(order, price, "止盈", true, ctx);
            }
            
            // 止损
            if (order.getDirection() == TDirection.LONG_T && price.compareTo(order.getStop()) <= 0) {
                return exitDecision(order, price, "止损", false, ctx);
            }
            if (order.getDirection() == TDirection.SHORT_T && price.compareTo(order.getStop()) >= 0) {
                return exitDecision(order, price, "止损", false, ctx);
            }
            
            // 时间到
            if (holdMin >= order.getHoldMinutes()) {
                boolean profit = (order.getDirection() == TDirection.LONG_T && price.compareTo(order.getEntryPrice()) > 0) ||
                                (order.getDirection() == TDirection.SHORT_T && price.compareTo(order.getEntryPrice()) < 0);
                return exitDecision(order, price, "时间到", profit, ctx);
            }
            
            // 市场反转
            if (shouldReverse(order, ctx)) {
                return exitDecision(order, price, "市场反转", false, ctx);
            }
            
            return null;
        }
        
        private boolean shouldReverse(ActiveOrder order, MarketContext ctx) {
            // 正T持仓时，市场转强势，提前止盈
            if (order.getDirection() == TDirection.LONG_T && ctx.getTrendScore() > 70) {
                return true;
            }
            // 反T持仓时，市场转弱势，提前买回
            if (order.getDirection() == TDirection.SHORT_T && ctx.getTrendScore() < -70) {
                return true;
            }
            return false;
        }
        
        private TDecision exitDecision(ActiveOrder order, BigDecimal price, String reason, 
                                        boolean profit, MarketContext ctx) {
            BigDecimal pnl = order.getDirection() == TDirection.LONG_T ?
                price.subtract(order.getEntryPrice()) :
                order.getEntryPrice().subtract(price);
            pnl = pnl.multiply(order.getQty());
            
            return TDecision.builder()
                .orderId(order.getId())
                .action("EXIT")
                .direction(order.getDirection())
                .price(price)
                .qty(order.getQty())
                .profit(pnl)
                .reason(reason + (profit ? " 盈利" : " 亏损") + pnl)
                .marketContext(ctx)
                .build();
        }
    }
    
    @Data
    public static class ActiveOrder {
        private String id;
        private TDirection direction;
        private BigDecimal entryPrice;
        private long entryTime;
        private MarketContext context;
        private BigDecimal target;
        private BigDecimal stop;
        private int holdMinutes;
        private BigDecimal qty;
        
        public ActiveOrder(String id, TDirection dir, BigDecimal price, long time, MarketContext ctx) {
            this.id = id;
            this.direction = dir;
            this.entryPrice = price;
            this.entryTime = time;
            this.context = ctx;
            this.target = dir == TDirection.LONG_T ? 
                price.multiply(new BigDecimal("1.005")) : price.multiply(new BigDecimal("0.995"));
            this.stop = dir == TDirection.LONG_T ? 
                price.multiply(new BigDecimal("0.998")) : price.multiply(new BigDecimal("1.003"));
            this.holdMinutes = 10;
        }
    }
    
    @Data
    @Builder
    public static class TDecision {
        private String orderId;
        private String action; // ENTRY, EXIT
        private TDirection direction;
        private BigDecimal price;
        private BigDecimal qty;
        private BigDecimal target;
        private BigDecimal stop;
        private BigDecimal profit;
        private int holdMinutes;
        private MarketContext marketContext;
        private String reason;
    }
}
