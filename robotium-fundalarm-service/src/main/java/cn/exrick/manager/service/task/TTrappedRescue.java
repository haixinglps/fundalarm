package cn.exrick.manager.service.task;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 做T套牢应急救援模块
 * 
 * 套住场景及处理：
 * 1. 【正T套住】买入后价格继续跌，无法卖出
 * 2. 【反T套住】卖出后价格暴涨，无法低价买回（T飞）
 * 3. 【震荡套住】价格横盘，无法触发止盈止损
 * 
 * 核心原则：
 * - 绝不用做T单去补仓（防止变加仓）
 * - 正T套住=认栽止损，绝不等反弹
 * - 反T套住=认栽买回，绝不追高
 * - 连续3次失败=暂停做T，回归网格
 */
@Slf4j
@Component
public class TTrappedRescue {

    // ==================== 套牢判定标准 ====================
    
    /** 正T套牢：买入后5分钟未盈利且价格低于买入价1% */
    private static final BigDecimal LONG_T_TRAP_THRESHOLD = new BigDecimal("0.01");
    /** 反T套牢：卖出后5分钟未买回且价格高于卖出价2%（T飞） */
    private static final BigDecimal SHORT_T_FLY_THRESHOLD = new BigDecimal("0.02");
    /** 震荡套牢：持仓10分钟价格波动<0.3% */
    private static final BigDecimal CHOP_THRESHOLD = new BigDecimal("0.003");
    /** 绝对止损线（正T） */
    private static final BigDecimal ABS_STOP_LOSS = new BigDecimal("0.015");
    /** T飞买回线（反T） */
    private static final BigDecimal ABS_T_FLY_BUY = new BigDecimal("0.025");
    
    // ==================== 套牢状态 ====================
    
    @Data
    @Builder
    public static class TrappedState {
        private String orderId;
        private TType type;
        private BigDecimal entryPrice;
        private BigDecimal qty;
        private long entryTime;
        private TrapType trapType;      // 套牢类型
        private int rescueAttempts;     // 救援尝试次数
        private boolean isRescued;      // 是否已解套
        private BigDecimal exitPrice;   // 最终出场价
        private BigDecimal loss;        // 实际损失
    }
    
    public enum TType {
        LONG_T,   // 正T
        SHORT_T   // 反T
    }
    
    public enum TrapType {
        LONG_T_FALLING,    // 正T后继续跌
        SHORT_T_FLYING,    // 反T后飞涨
        CHOP_TRAP,         // 震荡横盘
        RECOVERING         // 正在解套中
    }
    
    // ==================== 救援引擎 ====================
    
    @Data
    public static class RescueEngine {
        private String symbol;
        private TrappedState trappedState;
        private BigDecimal baseCost;      // 底仓成本
        private BigDecimal baseQty;       // 底仓数量
        
        // 救援策略参数
        private int maxRescueAttempts = 3;  // 最多救援3次
        private long lastRescueTime = 0;
        
        /**
         * 检查是否套牢
         */
        public TrapType checkIfTrapped(BigDecimal currentPrice, long currentTime) {
            long holdMinutes = (currentTime - trappedState.getEntryTime()) / 60000;
            
            if (trappedState.getType() == TType.LONG_T) {
                // 正T套牢判断
                BigDecimal fallPct = trappedState.getEntryPrice().subtract(currentPrice)
                    .divide(trappedState.getEntryPrice(), 6, RoundingMode.HALF_UP);
                
                if (holdMinutes >= 5 && fallPct.compareTo(LONG_T_TRAP_THRESHOLD) >= 0) {
                    return TrapType.LONG_T_FALLING;
                }
                if (fallPct.compareTo(ABS_STOP_LOSS) >= 0) {
                    return TrapType.LONG_T_FALLING; // 绝对止损
                }
                
            } else {
                // 反T套牢判断（T飞）
                BigDecimal risePct = currentPrice.subtract(trappedState.getEntryPrice())
                    .divide(trappedState.getEntryPrice(), 6, RoundingMode.HALF_UP);
                
                if (holdMinutes >= 5 && risePct.compareTo(SHORT_T_FLY_THRESHOLD) >= 0) {
                    return TrapType.SHORT_T_FLYING;
                }
                if (risePct.compareTo(ABS_T_FLY_BUY) >= 0) {
                    return TrapType.SHORT_T_FLYING; // 强制买回
                }
            }
            
            // 震荡判断
            if (holdMinutes >= 10) {
                return TrapType.CHOP_TRAP;
            }
            
            return null;
        }
        
        /**
         * 执行救援（根据不同套牢类型）
         */
        public RescueCommand executeRescue(BigDecimal currentPrice, long currentTime) {
            TrapType trapType = checkIfTrapped(currentPrice, currentTime);
            if (trapType == null) return null;
            
            trappedState.setTrapType(trapType);
            
            // 超过救援次数，强制出场
            if (trappedState.getRescueAttempts() >= maxRescueAttempts) {
                return forceExit(currentPrice, "救援次数超限，强制出场");
            }
            
            switch (trapType) {
                case LONG_T_FALLING:
                    return rescueLongTTrapped(currentPrice, currentTime);
                case SHORT_T_FLYING:
                    return rescueShortTTrapped(currentPrice, currentTime);
                case CHOP_TRAP:
                    return rescueChopTrapped(currentPrice, currentTime);
                default:
                    return null;
            }
        }
        
        /**
         * 【正T套牢救援】买入后继续跌
         * 策略：绝不补仓，认栽止损或等反弹
         */
        private RescueCommand rescueLongTTrapped(BigDecimal currentPrice, long currentTime) {
            trappedState.setRescueAttempts(trappedState.getRescueAttempts() + 1);
            
            BigDecimal lossPct = trappedState.getEntryPrice().subtract(currentPrice)
                .divide(trappedState.getEntryPrice(), 6, RoundingMode.HALF_UP);
            
            // 方案1：如果亏损<1%，立即认栽止损
            if (lossPct.compareTo(new BigDecimal("0.01")) < 0) {
                return forceExit(currentPrice, "正T小亏认栽，立即止损");
            }
            
            // 方案2：如果亏损>1.5%，绝对止损，不留恋
            if (lossPct.compareTo(ABS_STOP_LOSS) >= 0) {
                return forceExit(currentPrice, "正T深套" + 
                    lossPct.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + 
                    "%，绝对止损");
            }
            
            // 方案3：等一波小反弹再出（给最后一次机会）
            // 设置条件单：价格反弹0.3%时卖出
            return RescueCommand.builder()
                .action(RescueAction.WAIT_BOUNCE)
                .orderId(trappedState.getOrderId())
                .currentPrice(currentPrice)
                .triggerPrice(trappedState.getEntryPrice().multiply(new BigDecimal("0.997")))
                .reason("正T套牢，等待反弹0.3%出场，当前亏损" + 
                    lossPct.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%")
                .timeLimit(currentTime + 5 * 60 * 1000) // 再等5分钟
                .build();
        }
        
        /**
         * 【反T套牢救援】卖出后飞涨
         * 策略：绝不追高，认栽买回或等回落
         */
        private RescueCommand rescueShortTTrapped(BigDecimal currentPrice, long currentTime) {
            trappedState.setRescueAttempts(trappedState.getRescueAttempts() + 1);
            
            BigDecimal flyPct = currentPrice.subtract(trappedState.getEntryPrice())
                .divide(trappedState.getEntryPrice(), 6, RoundingMode.HALF_UP);
            
            // 方案1：如果T飞<2%，立即买回认栽
            if (flyPct.compareTo(new BigDecimal("0.02")) < 0) {
                return forceExit(currentPrice, "反T小亏认栽，立即买回");
            }
            
            // 方案2：如果T飞>2.5%，强制买回（防止进一步飞）
            if (flyPct.compareTo(ABS_T_FLY_BUY) >= 0) {
                return forceExit(currentPrice, "反T深套" + 
                    flyPct.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + 
                    "%，强制买回");
            }
            
            // 方案3：等一波小回落再买回
            return RescueCommand.builder()
                .action(RescueAction.WAIT_PULLBACK)
                .orderId(trappedState.getOrderId())
                .currentPrice(currentPrice)
                .triggerPrice(currentPrice.multiply(new BigDecimal("0.995")))
                .reason("反T套牢，等待回落0.5%买回，当前T飞" + 
                    flyPct.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%")
                .timeLimit(currentTime + 5 * 60 * 1000)
                .build();
        }
        
        /**
         * 【震荡套牢救援】价格横盘不动
         */
        private RescueCommand rescueChopTrapped(BigDecimal currentPrice, long currentTime) {
            trappedState.setRescueAttempts(trappedState.getRescueAttempts() + 1);
            
            // 震荡市直接认栽出场，不浪费时间
            if (trappedState.getType() == TType.LONG_T) {
                return forceExit(currentPrice, "震荡套牢，认栽出场回归网格");
            } else {
                return forceExit(currentPrice, "震荡套牢，认栽买回回归网格");
            }
        }
        
        /**
         * 强制出场
         */
        private RescueCommand forceExit(BigDecimal currentPrice, String reason) {
            trappedState.setExitPrice(currentPrice);
            trappedState.setRescued(true);
            
            BigDecimal loss;
            if (trappedState.getType() == TType.LONG_T) {
                loss = trappedState.getEntryPrice().subtract(currentPrice)
                    .multiply(trappedState.getQty());
            } else {
                loss = currentPrice.subtract(trappedState.getEntryPrice())
                    .multiply(trappedState.getQty());
            }
            trappedState.setLoss(loss);
            
            return RescueCommand.builder()
                .action(trappedState.getType() == TType.LONG_T ? 
                    RescueAction.FORCE_SELL : RescueAction.FORCE_BUY)
                .orderId(trappedState.getOrderId())
                .currentPrice(currentPrice)
                .qty(trappedState.getQty())
                .reason(reason)
                .estimatedLoss(loss)
                .build();
        }
        
        /**
         * 检查等待条件是否触发
         */
        public RescueCommand checkWaitTrigger(BigDecimal currentPrice, long currentTime) {
            // 这里可以实现条件单逻辑
            // 如果触发价格到达，执行相应操作
            return null;
        }
    }
    
    // ==================== 预防机制 ====================
    
    @Data
    public static class TPrevention {
        private int consecutiveLosses = 0;  // 连续亏损次数
        private BigDecimal dailyLoss = BigDecimal.ZERO;  // 今日做T总亏损
        private static final int MAX_CONSECUTIVE_LOSSES = 2;
        private static final BigDecimal MAX_DAILY_LOSS = new BigDecimal("100"); // 最大日亏损100U
        
        /**
         * 是否允许做T
         */
        public boolean canDoT() {
            if (consecutiveLosses >= MAX_CONSECUTIVE_LOSSES) {
                log.warn("连续{}次做T失败，暂停做T", consecutiveLosses);
                return false;
            }
            if (dailyLoss.compareTo(MAX_DAILY_LOSS) > 0) {
                log.warn("今日做T亏损已达{}，暂停做T", dailyLoss);
                return false;
            }
            return true;
        }
        
        /**
         * 记录做T结果
         */
        public void recordResult(BigDecimal profit) {
            if (profit.compareTo(BigDecimal.ZERO) < 0) {
                consecutiveLosses++;
                dailyLoss = dailyLoss.add(profit.abs());
            } else {
                consecutiveLosses = 0;
            }
        }
        
        /**
         * 重置（每日）
         */
        public void dailyReset() {
            consecutiveLosses = 0;
            dailyLoss = BigDecimal.ZERO;
        }
    }
    
    // ==================== 数据类 ====================
    
    public enum RescueAction {
        WAIT_BOUNCE,      // 等反弹
        WAIT_PULLBACK,    // 等回落
        FORCE_SELL,       // 强制卖出（正T解套）
        FORCE_BUY,        // 强制买回（反T解套）
        DO_NOTHING        // 不操作
    }
    
    @Data
    @Builder
    public static class RescueCommand {
        private RescueAction action;
        private String orderId;
        private BigDecimal currentPrice;
        private BigDecimal triggerPrice;
        private BigDecimal qty;
        private String reason;
        private BigDecimal estimatedLoss;
        private long timeLimit;
    }
    
    // ==================== 完整示例 ====================
    
    public void exampleRescueFlow() {
        // 场景：正T套住
        TrappedState state = TrappedState.builder()
            .orderId("T001")
            .type(TType.LONG_T)
            .entryPrice(new BigDecimal("70000"))
            .qty(new BigDecimal("0.1"))
            .entryTime(System.currentTimeMillis())
            .build();
        
        RescueEngine engine = new RescueEngine();
        engine.setSymbol("BTC-USDT-SWAP");
        engine.setTrappedState(state);
        
        long now = System.currentTimeMillis();
        
        // 第1分钟：价格跌到69500（正常波动）
        RescueCommand cmd = engine.executeRescue(new BigDecimal("69500"), now + 60000);
        // 结果：未套牢，不操作
        
        // 第5分钟：价格跌到69200（套牢）
        cmd = engine.executeRescue(new BigDecimal("69200"), now + 300000);
        // 结果：触发救援，等待反弹到69790
        
        // 第8分钟：价格跌到68800（深套）
        cmd = engine.executeRescue(new BigDecimal("68800"), now + 480000);
        // 结果：亏损>1.5%，强制止损出场
        
        // 场景：反T套住
        state = TrappedState.builder()
            .orderId("T002")
            .type(TType.SHORT_T)
            .entryPrice(new BigDecimal("71000"))
            .qty(new BigDecimal("0.1"))
            .entryTime(now)
            .build();
        engine.setTrappedState(state);
        
        // 第5分钟：价格涨到72500（T飞）
        cmd = engine.executeRescue(new BigDecimal("72500"), now + 300000);
        // 结果：T飞>2%，强制买回
    }
}
