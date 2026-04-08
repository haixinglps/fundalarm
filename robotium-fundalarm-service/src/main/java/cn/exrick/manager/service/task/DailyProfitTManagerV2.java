package cn.exrick.manager.service.task;

import cn.exrick.common.jedis.JedisClient;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日盈利目标做T管理器 V2 - 适配1000元本金
 * 
 * 核心调整：
 * - 单次开仓100元（本金的10%），而非10元
 * - 日盈利目标1.5元（0.15%），更合理
 * - 支持金字塔加仓（50→100→150）
 * - 日亏损上限3元（0.3%），严格风控
 */
@Component
public class DailyProfitTManagerV2 {

    @Autowired
    private JedisClient jedisClient;

    // ========== 资金配置（基于1000元本金） ==========
    /** 本金总额 */
    private static final BigDecimal TOTAL_CAPITAL = new BigDecimal("1000");
    /** 单次基础仓位：100元（10%） */
    private static final BigDecimal BASE_POSITION = new BigDecimal("100");
    /** 最大持仓：300元（30%，分3单） */
    private static final BigDecimal MAX_TOTAL_POSITION = new BigDecimal("300");
    /** 合约面值（假设0.01张=1元） */
    private static final BigDecimal CONTRACT_UNIT = new BigDecimal("0.01");
    
    // ========== 每日目标 ==========
    /** 日盈利目标：1.5元（0.15%） */
    private static final BigDecimal DAILY_PROFIT_TARGET = new BigDecimal("1.5");
    /** 日亏损上限：-3元（-0.3%） */
    private static final BigDecimal DAILY_LOSS_LIMIT = new BigDecimal("-3");
    /** 单笔下限：-1元（-1%） */
    private static final BigDecimal PER_TRADE_SL = new BigDecimal("-1");
    /** 单笔止盈：1.5元（1.5%） */
    private static final BigDecimal PER_TRADE_TP = new BigDecimal("1.5");
    /** 移动止盈回撤：0.5元 */
    private static final BigDecimal TRAILING_GAP = new BigDecimal("0.5");
    
    // ========== 交易参数 ==========
    /** 冷却时间：3分钟 */
    private static final int COOLDOWN_SEC = 180;
    /** 网格间距：0.15% */
    private static final BigDecimal GRID_GAP = new BigDecimal("0.0015");
    /** 日最大交易次数：10次 */
    private static final int MAX_TRADES_PER_DAY = 10;
    /** 连续2亏后暂停：30分钟 */
    private static final int PAUSE_AFTER_CONSEC_LOSS = 30;

    // ========== Redis Keys ==========
    private static final String KEY_DAILY_STATE = "t2:daily:{symbol}:{date}";
    private static final String KEY_POSITIONS = "t2:pos:{symbol}";
    private static final String KEY_PAUSE_UNTIL = "t2:pause:{symbol}";

    // ========== 数据结构 ==========
    
    public static class DailyState {
        public String totalPnl = "0";
        public int tradeCount = 0;
        public int winCount = 0;
        public int lossCount = 0;
        public int consecutiveLosses = 0;
        public boolean targetReached = false;
        public boolean stopped = false;
        public long lastTradeTime = 0;
        public BigDecimal currentPosition = new BigDecimal("0"); // 当前总持仓金额
        
        public BigDecimal getTotalPnl() {
            return new BigDecimal(totalPnl);
        }
        
        public void setTotalPnl(BigDecimal pnl) {
            this.totalPnl = pnl.toString();
        }
        
        public boolean canTrade() {
            if (targetReached || stopped) return false;
            BigDecimal pnl = getTotalPnl();
            if (pnl.compareTo(DAILY_PROFIT_TARGET) >= 0) {
                targetReached = true;
                return false;
            }
            if (pnl.compareTo(DAILY_LOSS_LIMIT) <= 0) {
                stopped = true;
                return false;
            }
            if (consecutiveLosses >= 2) return false;
            if (tradeCount >= MAX_TRADES_PER_DAY) return false;
            return true;
        }
        
        public String getStatus() {
            BigDecimal pnl = getTotalPnl();
            if (targetReached) return "🎯 达标 " + pnl + "元";
            if (stopped) return "🛑 止损 " + pnl + "元";
            if (consecutiveLosses >= 2) return "💤 冷却";
            if (tradeCount >= MAX_TRADES_PER_DAY) return "⛔ 次数满";
            return "▶ 交易中 盈亏" + pnl + "元 持仓" + currentPosition + "元";
        }
    }
    
    public static class TPosition {
        public String id;
        public long openTime;
        public String entryPrice;
        public String positionValue; // 仓位金额（元）
        public String zhang; // 张数
        public String tpPrice;
        public String slPrice;
        public String maxProfit = "0";
        public int stage = 0;
        public String status = "open";
        public int level; // 加仓层级 1/2/3
        
        public TPosition(BigDecimal entryPrice, BigDecimal positionValue, int level) {
            this.id = "T2" + (System.currentTimeMillis() % 10000);
            this.openTime = System.currentTimeMillis();
            this.entryPrice = entryPrice.toString();
            this.positionValue = positionValue.toString();
            this.level = level;
            // 张数 = 金额 / 价格 / 合约单位
            this.zhang = positionValue.divide(entryPrice, 8, RoundingMode.HALF_UP)
                    .divide(CONTRACT_UNIT, 8, RoundingMode.HALF_UP).toString();
            
            // 止盈止损价格
            BigDecimal tpRate = new BigDecimal("0.015"); // 1.5%
            BigDecimal slRate = new BigDecimal("0.01");  // 1%
            this.tpPrice = entryPrice.multiply(BigDecimal.ONE.add(tpRate))
                    .setScale(8, RoundingMode.HALF_UP).toString();
            this.slPrice = entryPrice.multiply(BigDecimal.ONE.subtract(slRate))
                    .setScale(8, RoundingMode.HALF_UP).toString();
        }
        
        public BigDecimal getEntryPrice() { return new BigDecimal(entryPrice); }
        public BigDecimal getPositionValue() { return new BigDecimal(positionValue); }
        public BigDecimal getZhang() { return new BigDecimal(zhang); }
        public BigDecimal getMaxProfit() { return new BigDecimal(maxProfit); }
        public void setMaxProfit(BigDecimal p) { this.maxProfit = p.toString(); }
    }

    // ========== 核心逻辑 ==========
    
    /**
     * 计算本次开仓金额（金字塔加仓）
     */
    public BigDecimal calcPositionSize(String symbol) {
        DailyState state = getDailyState(symbol);
        BigDecimal currentPos = state.currentPosition;
        
        if (currentPos.compareTo(new BigDecimal("0")) == 0) {
            // 第一单：50元（5%）
            return new BigDecimal("50");
        } else if (currentPos.compareTo(new BigDecimal("50")) <= 0) {
            // 第二单：100元（10%）
            return new BigDecimal("100");
        } else if (currentPos.compareTo(new BigDecimal("150")) <= 0) {
            // 第三单：150元（15%）
            return new BigDecimal("150");
        }
        
        return BigDecimal.ZERO; // 已达最大持仓
    }
    
    /**
     * 检查是否可以开仓
     */
    public CanTradeResult canOpen(String symbol, BigDecimal currentPrice) {
        CanTradeResult result = new CanTradeResult();
        DailyState state = getDailyState(symbol);
        
        if (!state.canTrade()) {
            result.allowed = false;
            result.reason = state.getStatus();
            return result;
        }
        
        // 检查暂停
        long pauseUntil = getPauseUntil(symbol);
        if (System.currentTimeMillis() < pauseUntil) {
            result.allowed = false;
            result.reason = "暂停中";
            return result;
        }
        
        // 检查冷却
        if (System.currentTimeMillis() - state.lastTradeTime < COOLDOWN_SEC * 1000) {
            result.allowed = false;
            result.reason = "冷却中";
            return result;
        }
        
        // 计算仓位
        BigDecimal positionSize = calcPositionSize(symbol);
        if (positionSize.compareTo(BigDecimal.ZERO) == 0) {
            result.allowed = false;
            result.reason = "已达最大持仓300元";
            return result;
        }
        
        // 检查网格间距
        List<TPosition> positions = getPositions(symbol);
        if (!positions.isEmpty()) {
            TPosition last = positions.get(positions.size() - 1);
            BigDecimal gap = currentPrice.subtract(last.getEntryPrice())
                    .divide(last.getEntryPrice(), 6, RoundingMode.HALF_UP).abs();
            if (gap.compareTo(GRID_GAP) < 0) {
                result.allowed = false;
                result.reason = "间距不足" + gap.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%";
                return result;
            }
        }
        
        result.allowed = true;
        result.state = state;
        result.positionSize = positionSize;
        result.level = positions.size() + 1;
        return result;
    }
    
    public static class CanTradeResult {
        public boolean allowed;
        public String reason;
        public DailyState state;
        public BigDecimal positionSize;
        public int level;
    }
    
    /**
     * 开仓
     */
    public TPosition openPosition(String symbol, BigDecimal entryPrice, CanTradeResult decision) {
        TPosition pos = new TPosition(entryPrice, decision.positionSize, decision.level);
        
        List<TPosition> positions = getPositions(symbol);
        positions.add(pos);
        savePositions(symbol, positions);
        
        DailyState state = getDailyState(symbol);
        state.lastTradeTime = System.currentTimeMillis();
        state.currentPosition = state.currentPosition.add(decision.positionSize);
        saveDailyState(symbol, state);
        
        System.out.println("【做T开仓-" + decision.level + "阶】" + symbol + " " + pos.id 
                + " 金额=" + decision.positionSize + "元 价格=" + entryPrice);
        return pos;
    }
    
    /**
     * 检查退出信号
     */
    public List<TExitSignal> checkExits(String symbol, BigDecimal currentPrice) {
        List<TExitSignal> signals = new ArrayList<>();
        List<TPosition> positions = getPositions(symbol);
        
        for (TPosition pos : positions) {
            if (!"open".equals(pos.status)) continue;
            
            // 计算当前盈亏（金额）
            BigDecimal priceDiff = currentPrice.subtract(pos.getEntryPrice());
            BigDecimal pnlAmount = priceDiff.multiply(pos.getZhang()).multiply(CONTRACT_UNIT);
            BigDecimal pnlRate = priceDiff.divide(pos.getEntryPrice(), 6, RoundingMode.HALF_UP);
            
            // 更新最大盈利
            if (pnlAmount.compareTo(pos.getMaxProfit()) > 0) {
                pos.setMaxProfit(pnlAmount);
            }
            
            // 止损：-1元
            if (pnlAmount.compareTo(PER_TRADE_SL) <= 0) {
                signals.add(new TExitSignal(pos.id, "SL", currentPrice, pnlAmount, 
                        "止损" + pnlAmount + "元"));
                pos.status = "closing";
                continue;
            }
            
            if (pos.stage == 0) {
                // 一阶止盈：1.5元，平50%
                if (pnlAmount.compareTo(PER_TRADE_TP) >= 0) {
                    BigDecimal closeValue = pos.getPositionValue().multiply(new BigDecimal("0.5"));
                    signals.add(new TExitSignal(pos.id, "TP1", currentPrice, pnlAmount, 
                            "止盈50% 金额=" + closeValue + "元"));
                    pos.stage = 1;
                }
            } else {
                // 移动止盈：从最高点回撤0.5元
                BigDecimal pullback = pos.getMaxProfit().subtract(pnlAmount);
                if (pullback.compareTo(TRAILING_GAP) >= 0) {
                    signals.add(new TExitSignal(pos.id, "TRAILING", currentPrice, pnlAmount,
                            "移动止盈 最高" + pos.getMaxProfit() + "元 回撤" + pullback + "元"));
                    pos.status = "closing";
                }
            }
        }
        
        savePositions(symbol, positions);
        return signals;
    }
    
    /**
     * 平仓
     */
    public void closePosition(String symbol, TExitSignal signal, BigDecimal exitPrice) {
        List<TPosition> positions = getPositions(symbol);
        TPosition target = null;
        
        for (TPosition pos : positions) {
            if (pos.id.equals(signal.positionId)) {
                target = pos;
                break;
            }
        }
        
        if (target == null) return;
        
        // 计算盈亏
        BigDecimal priceDiff = exitPrice.subtract(target.getEntryPrice());
        BigDecimal grossPnl = priceDiff.multiply(target.getZhang()).multiply(CONTRACT_UNIT);
        BigDecimal fee = target.getPositionValue().multiply(new BigDecimal("0.002")); // 0.2%手续费
        BigDecimal netPnl = grossPnl.subtract(fee);
        
        // 更新状态
        DailyState state = getDailyState(symbol);
        state.setTotalPnl(state.getTotalPnl().add(netPnl));
        state.tradeCount++;
        state.currentPosition = state.currentPosition.subtract(target.getPositionValue());
        
        if (netPnl.compareTo(BigDecimal.ZERO) > 0) {
            state.winCount++;
            state.consecutiveLosses = 0;
        } else {
            state.lossCount++;
            state.consecutiveLosses++;
            if (state.consecutiveLosses >= 2) {
                setPauseUntil(symbol, System.currentTimeMillis() + PAUSE_AFTER_CONSEC_LOSS * 60 * 1000);
            }
        }
        
        if (state.getTotalPnl().compareTo(DAILY_PROFIT_TARGET) >= 0) {
            state.targetReached = true;
        } else if (state.getTotalPnl().compareTo(DAILY_LOSS_LIMIT) <= 0) {
            state.stopped = true;
        }
        
        saveDailyState(symbol, state);
        
        if ("SL".equals(signal.type) || "TRAILING".equals(signal.type)) {
            positions.remove(target);
        }
        savePositions(symbol, positions);
        
        System.out.println("【做T平仓】" + symbol + " " + target.id + " " + signal.type 
                + " 盈亏=" + netPnl.setScale(2, RoundingMode.HALF_UP) + "元 " + state.getStatus());
    }
    
    /**
     * 强制平仓
     */
    public void forceCloseAll(String symbol, BigDecimal exitPrice) {
        List<TPosition> positions = getPositions(symbol);
        for (TPosition pos : positions) {
            if ("open".equals(pos.status)) {
                BigDecimal priceDiff = exitPrice.subtract(pos.getEntryPrice());
                BigDecimal grossPnl = priceDiff.multiply(pos.getZhang()).multiply(CONTRACT_UNIT);
                System.out.println("【强制平仓】" + pos.id + " 盈亏=" + grossPnl.setScale(2, RoundingMode.HALF_UP) + "元");
            }
        }
        jedisClient.del(KEY_POSITIONS.replace("{symbol}", symbol));
    }
    
    public DailyState getDailyState(String symbol) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = KEY_DAILY_STATE.replace("{symbol}", symbol).replace("{date}", date);
        String json = jedisClient.get(key);
        if (StrUtil.isBlank(json)) return new DailyState();
        try {
            return JSONUtil.toBean(json, DailyState.class);
        } catch (Exception e) {
            return new DailyState();
        }
    }
    
    private void saveDailyState(String symbol, DailyState state) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = KEY_DAILY_STATE.replace("{symbol}", symbol).replace("{date}", date);
        jedisClient.set(key, JSONUtil.toJsonStr(state));
        jedisClient.expire(key, 86400);
    }
    
    public List<TPosition> getPositions(String symbol) {
        String key = KEY_POSITIONS.replace("{symbol}", symbol);
        String json = jedisClient.get(key);
        if (StrUtil.isBlank(json)) return new ArrayList<>();
        try {
            return JSONUtil.toList(JSONUtil.parseArray(json), TPosition.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    private void savePositions(String symbol, List<TPosition> positions) {
        String key = KEY_POSITIONS.replace("{symbol}", symbol);
        jedisClient.set(key, JSONUtil.toJsonStr(positions));
    }
    
    private long getPauseUntil(String symbol) {
        String key = KEY_PAUSE_UNTIL.replace("{symbol}", symbol);
        String val = jedisClient.get(key);
        return val == null ? 0 : Long.parseLong(val);
    }
    
    private void setPauseUntil(String symbol, long timestamp) {
        String key = KEY_PAUSE_UNTIL.replace("{symbol}", symbol);
        jedisClient.set(key, String.valueOf(timestamp));
        jedisClient.expire(key, 3600);
    }
    
    public static class TExitSignal {
        public String positionId;
        public String type;
        public BigDecimal price;
        public BigDecimal pnlAmount;
        public String reason;
        
        public TExitSignal(String positionId, String type, BigDecimal price, 
                BigDecimal pnlAmount, String reason) {
            this.positionId = positionId;
            this.type = type;
            this.price = price;
            this.pnlAmount = pnlAmount;
            this.reason = reason;
        }
    }
}
