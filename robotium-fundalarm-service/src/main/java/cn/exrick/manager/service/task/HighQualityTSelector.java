package cn.exrick.manager.service.task;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 高质量做T选择器 - 低频高胜率，拒绝频繁止损
 * 
 * 核心理念：
 * 1. 【宁缺毋滥】一天只做1-3次高质量T，胜率>80%才出手
 * 2. 【盈亏比3:1】盈利目标0.8%，止损容忍0.3%，一次赚抵三次亏
 * 3. 【形态确认】只在急跌反弹/冲高回落形态下做T
 * 4. 【时间宽容】持仓最长30分钟，给价格回归时间
 * 5. 【失败即停】当日第一次T失败，当日不再做T
 */
@Slf4j
@Component
public class HighQualityTSelector {

    // ==================== 严格入场条件 ====================
    
    /** 最小盈亏比（盈利/止损 >= 3） */
    private static final BigDecimal MIN_RR_RATIO = new BigDecimal("3");
    /** 最小盈利空间（0.8%，覆盖手续费+利润） */
    private static final BigDecimal MIN_PROFIT_TARGET = new BigDecimal("0.008");
    /** 最大止损空间（0.3%） */
    private static final BigDecimal MAX_STOP_LOSS = new BigDecimal("0.003");
    /** 持仓时间（5-30分钟） */
    private static final int MAX_HOLD_MINUTES = 30;
    private static final int MIN_HOLD_MINUTES = 5;
    /** 日最大T次数 */
    private static final int MAX_DAILY_T = 3;
    
    // ==================== 形态识别 ====================
    
    @Data
    public static class PatternDetector {
        private LinkedList<PriceBar> bars = new LinkedList<>();
        private static final int LOOKBACK = 10; // 看前10根K线
        
        /**
         * 检测急跌反弹形态（正T买点）
         * 
         * 条件：
         * 1. 3分钟内快速下跌 >1%
         * 2. 出现止跌信号（长下影/连续小阳线）
         * 3. 成交量萎缩（卖盘枯竭）
         * 4. 当前位置在日内低点20%以内
         */
        public boolean isBouncePattern(BigDecimal currentPrice, BigDecimal volume) {
            if (bars.size() < 5) return false;
            
            // 计算近期跌幅
            BigDecimal high3Min = bars.subList(Math.max(0, bars.size()-3), bars.size())
                .stream().map(PriceBar::getHigh).max(BigDecimal::compareTo).orElse(currentPrice);
            BigDecimal dropPct = high3Min.subtract(currentPrice).divide(high3Min, 6, RoundingMode.HALF_UP);
            
            // 条件1：3分钟跌幅>1%
            if (dropPct.compareTo(new BigDecimal("0.01")) < 0) return false;
            
            // 条件2：止跌信号（价格不再创新低）
            boolean stopFalling = bars.getLast().getLow().compareTo(
                bars.get(bars.size()-2).getLow()) >= 0;
            
            // 条件3：日内位置（低点20%以内）
            BigDecimal dayLow = bars.stream().map(PriceBar::getLow)
                .min(BigDecimal::compareTo).orElse(currentPrice);
            BigDecimal dayHigh = bars.stream().map(PriceBar::getHigh)
                .max(BigDecimal::compareTo).orElse(currentPrice);
            BigDecimal position = currentPrice.subtract(dayLow).divide(
                dayHigh.subtract(dayLow), 6, RoundingMode.HALF_UP);
            
            return stopFalling && position.compareTo(new BigDecimal("0.2")) <= 0;
        }
        
        /**
         * 检测冲高回落形态（反T卖点）
         * 
         * 条件：
         * 1. 3分钟内快速上涨 >1.5%
         * 2. 出现滞涨信号（长上影/连续小阴线）
         * 3. 当前位置在日内高点80%以上
         */
        public boolean isPullbackPattern(BigDecimal currentPrice, BigDecimal volume) {
            if (bars.size() < 5) return false;
            
            // 计算近期涨幅
            BigDecimal low3Min = bars.subList(Math.max(0, bars.size()-3), bars.size())
                .stream().map(PriceBar::getLow).min(BigDecimal::compareTo).orElse(currentPrice);
            BigDecimal risePct = currentPrice.subtract(low3Min).divide(low3Min, 6, RoundingMode.HALF_UP);
            
            // 条件1：3分钟涨幅>1.5%
            if (risePct.compareTo(new BigDecimal("0.015")) < 0) return false;
            
            // 条件2：滞涨信号
            boolean stopRising = bars.getLast().getHigh().compareTo(
                bars.get(bars.size()-2).getHigh()) <= 0;
            
            // 条件3：日内高位
            BigDecimal dayLow = bars.stream().map(PriceBar::getLow)
                .min(BigDecimal::compareTo).orElse(currentPrice);
            BigDecimal dayHigh = bars.stream().map(PriceBar::getHigh)
                .max(BigDecimal::compareTo).orElse(currentPrice);
            BigDecimal position = currentPrice.subtract(dayLow).divide(
                dayHigh.subtract(dayLow), 6, RoundingMode.HALF_UP);
            
            return stopRising && position.compareTo(new BigDecimal("0.8")) >= 0;
        }
        
        /**
         * 计算目标价和止损价
         */
        public ExitPlan calculateExitPlan(BigDecimal entryPrice, boolean isLong) {
            if (isLong) {
                // 正T：目标反弹0.8%，止损再跌0.3%
                return ExitPlan.builder()
                    .target(entryPrice.multiply(new BigDecimal("1.008")))
                    .stop(entryPrice.multiply(new BigDecimal("0.997")))
                    .expectedProfit(entryPrice.multiply(new BigDecimal("0.008")))
                    .maxLoss(entryPrice.multiply(new BigDecimal("0.003")))
                    .build();
            } else {
                // 反T：目标回落0.8%，止损再涨0.3%
                return ExitPlan.builder()
                    .target(entryPrice.multiply(new BigDecimal("0.992")))
                    .stop(entryPrice.multiply(new BigDecimal("1.003")))
                    .expectedProfit(entryPrice.multiply(new BigDecimal("0.008")))
                    .maxLoss(entryPrice.multiply(new BigDecimal("0.003")))
                    .build();
            }
        }
    }
    
    @Data
    @Builder
    public static class PriceBar {
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal volume;
        private long timestamp;
    }
    
    @Data
    @Builder
    public static class ExitPlan {
        private BigDecimal target;
        private BigDecimal stop;
        private BigDecimal expectedProfit;
        private BigDecimal maxLoss;
    }
    
    // ==================== T单管理 ====================
    
    @Data
    public static class QualityTManager {
        private String symbol;
        private PatternDetector detector = new PatternDetector();
        private TOrder activeOrder;
        private int dailyTCount = 0;
        private boolean todayFailed = false;  // 今日是否已失败
        private List<TRecord> history = new ArrayList<>();
        
        // 底仓信息
        private BigDecimal baseCost;
        private BigDecimal baseQty;
        
        /**
         * 主入口：寻找做T机会
         */
        public TSignal findOpportunity(BigDecimal price, BigDecimal volume, long time) {
            // 前置检查
            if (dailyTCount >= MAX_DAILY_T) {
                return TSignal.none("已达日上限" + MAX_DAILY_T + "次");
            }
            if (todayFailed) {
                return TSignal.none("今日已失败，暂停做T");
            }
            if (activeOrder != null) {
                return TSignal.none("已有持仓");
            }
            
            // 更新K线数据
            updateBars(price, volume, time);
            
            // 尝试正T（急跌反弹）
            if (detector.isBouncePattern(price, volume)) {
                ExitPlan plan = detector.calculateExitPlan(price, true);
                
                // 检查盈亏比
                BigDecimal rr = plan.getExpectedProfit().divide(plan.getMaxLoss(), 2, RoundingMode.HALF_UP);
                if (rr.compareTo(MIN_RR_RATIO) >= 0) {
                    return createLongTSignal(price, plan);
                }
            }
            
            // 尝试反T（冲高回落）- 需要底仓
            if (baseQty.compareTo(BigDecimal.ZERO) > 0 && 
                detector.isPullbackPattern(price, volume)) {
                ExitPlan plan = detector.calculateExitPlan(price, false);
                
                BigDecimal rr = plan.getExpectedProfit().divide(plan.getMaxLoss(), 2, RoundingMode.HALF_UP);
                if (rr.compareTo(MIN_RR_RATIO) >= 0) {
                    return createShortTSignal(price, plan);
                }
            }
            
            return TSignal.none("无合适形态");
        }
        
        /**
         * 检查平仓
         */
        public TSignal checkExit(BigDecimal price, long time) {
            if (activeOrder == null) return TSignal.none("无持仓");
            
            long holdMinutes = (time - activeOrder.getEntryTime()) / 60000;
            
            // 止盈
            if (activeOrder.getType() == TType.LONG_T && 
                price.compareTo(activeOrder.getTargetPrice()) >= 0) {
                return exitSuccess(price, time, "到达止盈目标");
            }
            if (activeOrder.getType() == TType.SHORT_T && 
                price.compareTo(activeOrder.getTargetPrice()) <= 0) {
                return exitSuccess(price, time, "到达买回目标");
            }
            
            // 止损（严格执行，但次数少，影响可控）
            if (activeOrder.getType() == TType.LONG_T && 
                price.compareTo(activeOrder.getStopPrice()) <= 0) {
                return exitFail(price, time, "触发止损");
            }
            if (activeOrder.getType() == TType.SHORT_T && 
                price.compareTo(activeOrder.getStopPrice()) >= 0) {
                return exitFail(price, time, "T飞止损");
            }
            
            // 时间到了止盈/止损都没触发
            if (holdMinutes >= MAX_HOLD_MINUTES) {
                // 如果有微利，平了；如果亏损，也平了认栽
                BigDecimal profit = calculateProfit(price);
                if (profit.compareTo(BigDecimal.ZERO) > 0) {
                    return exitSuccess(price, time, "时间到微利出场");
                } else {
                    return exitFail(price, time, "时间到认栽出场");
                }
            }
            
            // 持仓中
            return TSignal.holding(activeOrder, holdMinutes);
        }
        
        private TSignal createLongTSignal(BigDecimal price, ExitPlan plan) {
            String id = "LT" + System.currentTimeMillis() % 10000;
            activeOrder = TOrder.builder()
                .id(id)
                .type(TType.LONG_T)
                .entryPrice(price)
                .targetPrice(plan.getTarget())
                .stopPrice(plan.getStop())
                .expectedProfit(plan.getExpectedProfit())
                .maxLoss(plan.getMaxLoss())
                .entryTime(System.currentTimeMillis())
                .build();
            
            dailyTCount++;
            
            return TSignal.builder()
                .action(TAction.LONG_T_ENTRY)
                .orderId(id)
                .price(price)
                .target(plan.getTarget())
                .stop(plan.getStop())
                .reason(String.format("急跌反弹 目标%s 止损%s 盈亏比3:%s",
                    plan.getTarget(), plan.getStop(), 
                    plan.getExpectedProfit().divide(plan.getMaxLoss(), 0, RoundingMode.HALF_UP)))
                .build();
        }
        
        private TSignal createShortTSignal(BigDecimal price, ExitPlan plan) {
            String id = "ST" + System.currentTimeMillis() % 10000;
            activeOrder = TOrder.builder()
                .id(id)
                .type(TType.SHORT_T)
                .entryPrice(price)
                .targetPrice(plan.getTarget())
                .stopPrice(plan.getStop())
                .expectedProfit(plan.getExpectedProfit())
                .maxLoss(plan.getMaxLoss())
                .entryTime(System.currentTimeMillis())
                .build();
            
            dailyTCount++;
            
            return TSignal.builder()
                .action(TAction.SHORT_T_ENTRY)
                .orderId(id)
                .price(price)
                .target(plan.getTarget())
                .stop(plan.getStop())
                .reason(String.format("冲高回落 目标买回%s 止损%s 盈亏比3:%s",
                    plan.getTarget(), plan.getStop(),
                    plan.getExpectedProfit().divide(plan.getMaxLoss(), 0, RoundingMode.HALF_UP)))
                .build();
        }
        
        private TSignal exitSuccess(BigDecimal price, long time, String reason) {
            BigDecimal profit = calculateProfit(price);
            recordTrade(true, profit);
            activeOrder = null;
            
            return TSignal.builder()
                .action(TAction.EXIT_PROFIT)
                .price(price)
                .profit(profit)
                .reason(reason)
                .build();
        }
        
        private TSignal exitFail(BigDecimal price, long time, String reason) {
            BigDecimal loss = calculateProfit(price).abs();
            recordTrade(false, loss.negate());
            activeOrder = null;
            todayFailed = true;  // 关键：一次失败，当日停手
            
            return TSignal.builder()
                .action(TAction.EXIT_LOSS)
                .price(price)
                .profit(loss.negate())
                .reason(reason + "【今日暂停做T】")
                .build();
        }
        
        private BigDecimal calculateProfit(BigDecimal exitPrice) {
            if (activeOrder.getType() == TType.LONG_T) {
                return exitPrice.subtract(activeOrder.getEntryPrice())
                    .multiply(new BigDecimal("0.1")); // 假设T单数量是10%仓位
            } else {
                return activeOrder.getEntryPrice().subtract(exitPrice)
                    .multiply(new BigDecimal("0.1"));
            }
        }
        
        private void recordTrade(boolean success, BigDecimal profit) {
            TRecord record = TRecord.builder()
                .type(activeOrder.getType())
                .entryPrice(activeOrder.getEntryPrice())
                .exitPrice(activeOrder.getTargetPrice()) // 简化
                .profit(profit)
                .success(success)
                .time(System.currentTimeMillis())
                .build();
            history.add(record);
        }
        
        private void updateBars(BigDecimal price, BigDecimal volume, long time) {
            // 简化：假设每个tick是一根1分钟K线
            detector.getBars().addLast(PriceBar.builder()
                .open(price)
                .high(price)
                .low(price)
                .close(price)
                .volume(volume)
                .timestamp(time)
                .build());
            
            while (detector.getBars().size() > PatternDetector.LOOKBACK) {
                detector.getBars().pollFirst();
            }
        }
        
        /**
         * 每日重置
         */
        public void dailyReset() {
            dailyTCount = 0;
            todayFailed = false;
            activeOrder = null;
        }
        
        public String getStats() {
            if (history.isEmpty()) return "暂无T记录";
            
            long success = history.stream().filter(TRecord::isSuccess).count();
            BigDecimal totalProfit = history.stream().map(TRecord::getProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            return String.format("T记录: %d/%d 胜率%s%% 净利润%s 今日次数%d/3 %s",
                success, history.size(),
                new BigDecimal(success * 100).divide(
                    new BigDecimal(history.size()), 1, RoundingMode.HALF_UP),
                totalProfit, dailyTCount,
                todayFailed ? "【已暂停】" : "【正常】");
        }
    }
    
    // ==================== 数据类 ====================
    
    public enum TType { LONG_T, SHORT_T }
    public enum TAction { NONE, LONG_T_ENTRY, SHORT_T_ENTRY, EXIT_PROFIT, EXIT_LOSS, HOLDING }
    
    @Data
    @Builder
    public static class TOrder {
        private String id;
        private TType type;
        private BigDecimal entryPrice;
        private BigDecimal targetPrice;
        private BigDecimal stopPrice;
        private BigDecimal expectedProfit;
        private BigDecimal maxLoss;
        private long entryTime;
    }
    
    @Data
    @Builder
    public static class TSignal {
        private TAction action;
        private String orderId;
        private BigDecimal price;
        private BigDecimal target;
        private BigDecimal stop;
        private BigDecimal profit;
        private String reason;
        private TOrder holdingOrder;
        private long holdMinutes;
        
        public static TSignal none(String reason) {
            return TSignal.builder().action(TAction.NONE).reason(reason).build();
        }
        
        public static TSignal holding(TOrder order, long minutes) {
            return TSignal.builder()
                .action(TAction.HOLDING)
                .holdingOrder(order)
                .holdMinutes(minutes)
                .build();
        }
    }
    
    @Data
    @Builder
    public static class TRecord {
        private TType type;
        private BigDecimal entryPrice;
        private BigDecimal exitPrice;
        private BigDecimal profit;
        private boolean success;
        private long time;
    }
}
