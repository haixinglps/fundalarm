package cn.exrick.manager.service.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.exrick.common.jedis.JedisClient;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 日盈利目标做T管理器
 * 
 * 核心理念：不求每笔都赚，但求每天正收益 - 日盈利目标：0.15%（达成后停止开新仓） - 日亏损上限：0.1%（触及后当日停止交易） -
 * 连续2笔亏损后暂停30分钟 - 只在高概率场景（评分>80分）才交易 - 多重过滤：趋势+RSI+成交量+波动率
 */
@Component
public class DailyProfitTManager {

	@Autowired
	private JedisClient jedisClient;

	// ========== 全局资金池配置（多币种共享）==========
	// 注意：需要覆盖底仓保证金 + 做T资金
	// XAUT底仓约95U + DOGE底仓约73U = 168U，建议设置250U以上
	private static final BigDecimal TOTAL_CAPITAL = new BigDecimal("300"); // 总本金300U（覆盖底仓+做T）
	private static final BigDecimal MAX_MARGIN_USAGE = new BigDecimal("0.9"); // 最大使用90%保证金（底仓已占用大部分，提高利用率）
	private static final String KEY_GLOBAL_MARGIN = "t:global:margin:used"; // 全局已用保证金Redis键

	// ========== ATR计算配置 ==========
	private static final int ATR_PERIOD = 14; // ATR计算周期
	private static final int MAX_PRICE_HISTORY = 100; // 最大保存价格数量

	/**
	 * 价格数据点（用于ATR计算）
	 */
	public static class PriceData {
		public BigDecimal high;
		public BigDecimal low;
		public BigDecimal close;
		public long time;

		public PriceData(BigDecimal high, BigDecimal low, BigDecimal close, long time) {
			this.high = high;
			this.low = low;
			this.close = close;
			this.time = time;
		}
	}

	// ========== 多合约参数配置 ==========
	/**
	 * 合约配置映射 key: 品种代码关键字（如XAUT、DOGE）
	 */
	private static final Map<String, ContractConfig> CONTRACT_CONFIGS = new HashMap<>();

	static {
		// XAUT配置（5倍，1张=4U，最低买1张）
		// 百分比模式：止盈0.3% / 止损0.2%（盈亏比1.5:1）
		CONTRACT_CONFIGS.put("XAUT", new ContractConfig(new BigDecimal("0.001"), // 1张=4U
				new BigDecimal("5"), // 5倍杠杆
				new BigDecimal("0.0015"), // 网格间距0.15%
				new BigDecimal("0.003"), // 止盈0.3%
				new BigDecimal("0.002"), // 止损0.2%
				30, // 日最大30次
				30, // 冷却30秒
				true, // 百分比模式
				new BigDecimal("1"), // 最小下单1张
				new BigDecimal("30") // 最大下单30张（30U本金限制）
		));

		// DOGE配置（3倍，1张=170U，最低买0.01张）
		// 百分比模式：止盈0.45% / 止损0.3%（盈亏比1.5:1）
		CONTRACT_CONFIGS.put("DOGE", new ContractConfig(new BigDecimal("1000"), // 1张=170U（0.01张=1.7U）
				new BigDecimal("3"), // 3倍杠杆
				new BigDecimal("0.0015"), // 网格间距0.15%（与XAUT统一，ATR自适应）
				new BigDecimal("0.0045"), // 止盈0.45%
				new BigDecimal("0.003"), // 止损0.3%
				20, // 日最大20次
				60, // 冷却60秒
				true, // 百分比模式
				new BigDecimal("0.01"), // 最小下单0.01张
				new BigDecimal("10") // 最大下单10张（风控）
		));
	}

	/**
	 * 合约配置类
	 */
	public static class ContractConfig {
		public BigDecimal valuePerZhang; // 1张价值
		public BigDecimal leverage; // 杠杆倍数
		public BigDecimal gridGap; // 网格间距
		public BigDecimal tpAmount; // 止盈金额（固定金额模式）
		public BigDecimal slAmount; // 止损金额（固定金额模式）
		public int maxTrades; // 日最大交易次数
		public int cooldownSec; // 冷却时间
		public boolean usePercentMode; // true=百分比模式, false=固定金额
		public BigDecimal tpPercent; // 止盈比例(如0.003=0.3%)
		public BigDecimal slPercent; // 止损比例(如0.002=0.2%)
		public BigDecimal minOrder; // 最小下单张数
		public BigDecimal maxOrder; // 最大下单张数

		// 百分比模式（带最小/最大下单限制）
		public ContractConfig(BigDecimal valuePerZhang, BigDecimal leverage, BigDecimal gridGap, BigDecimal tpPercent,
				BigDecimal slPercent, int maxTrades, int cooldownSec, boolean usePercentMode, BigDecimal minOrder,
				BigDecimal maxOrder) {
			this.valuePerZhang = valuePerZhang;
			this.leverage = leverage;
			this.gridGap = gridGap;
			this.maxTrades = maxTrades;
			this.cooldownSec = cooldownSec;
			this.usePercentMode = usePercentMode;
			this.tpPercent = tpPercent;
			this.slPercent = slPercent;
			this.tpAmount = BigDecimal.ZERO;
			this.slAmount = BigDecimal.ZERO;
			this.minOrder = minOrder;
			this.maxOrder = maxOrder;
		}

		// 固定金额模式（默认，兼容旧配置）
		public ContractConfig(BigDecimal valuePerZhang, BigDecimal leverage, BigDecimal gridGap, BigDecimal tpAmount,
				BigDecimal slAmount, int maxTrades, int cooldownSec) {
			this.valuePerZhang = valuePerZhang;
			this.leverage = leverage;
			this.gridGap = gridGap;
			this.tpAmount = tpAmount;
			this.slAmount = slAmount;
			this.maxTrades = maxTrades;
			this.cooldownSec = cooldownSec;
			this.usePercentMode = false;
			this.tpPercent = null;
			this.slPercent = null;
			this.minOrder = new BigDecimal("0.01");
			this.maxOrder = new BigDecimal("100");
		}
	}

	/**
	 * 获取合约配置
	 */
	private ContractConfig getConfig(String symbol) {
		for (Map.Entry<String, ContractConfig> entry : CONTRACT_CONFIGS.entrySet()) {
			if (symbol.toUpperCase().contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		// 默认配置（XAUT）
		return CONTRACT_CONFIGS.get("XAUT");
	}

	// ========== 每日目标参数（统一标准） ==========
	/** 日盈利目标：999U（实际不限制，让利润奔跑） */
	private static final BigDecimal DAILY_PROFIT_TARGET = new BigDecimal("999");
	/** 日亏损上限：-5U（必须保留，控制风险） */
	private static final BigDecimal DAILY_LOSS_LIMIT = new BigDecimal("-5");
	/** 移动止盈回撤：0.05%（保住利润） */
	private static final BigDecimal TRAILING_GAP = new BigDecimal("0.0005");

	// ========== 通用交易参数 ==========
	/** 最小交易单位：0.01张 */
	private static final BigDecimal MIN_UNIT = new BigDecimal("0.01");
	/** 最大持仓：5组 */
	private static final int MAX_POSITIONS = 5;

	// ========== Redis Keys ==========
	private static final String KEY_DAILY_STATE = "t:daily:{symbol}:{date}";
	private static final String KEY_POSITIONS = "t:pos:{symbol}";
	private static final String KEY_PAUSE_UNTIL = "t:pause:{symbol}";

	// ========== 数据结构 ==========

	/**
	 * 每日状态
	 */
	public static class DailyState {
		/** 当日累计盈亏 */
		public String totalPnl = "0";
		/** 交易次数 */
		public int tradeCount = 0;
		/** 盈利次数 */
		public int winCount = 0;
		/** 亏损次数 */
		public int lossCount = 0;
		/** 连续亏损次数 */
		public int consecutiveLosses = 0;
		/** 是否已达成目标 */
		public boolean targetReached = false;
		/** 是否已触及止损 */
		public boolean stopped = false;
		/** 最后交易时间 */
		public long lastTradeTime = 0;

		public BigDecimal getTotalPnl() {
			return new BigDecimal(totalPnl);
		}

		public void setTotalPnl(BigDecimal pnl) {
			this.totalPnl = pnl.toString();
		}

		/**
		 * 检查是否允许交易
		 */
		public boolean canTrade() {
			if (targetReached || stopped)
				return false;
			BigDecimal pnl = getTotalPnl();
			if (pnl.compareTo(DAILY_PROFIT_TARGET) >= 0) {
				targetReached = true;
				return false;
			}
			if (pnl.compareTo(DAILY_LOSS_LIMIT) <= 0) {
				stopped = true;
				return false;
			}
			if (consecutiveLosses >= 2) {
				return false; // 连续2亏后需要冷却
			}
			return true;
		}

		/**
		 * 获取状态描述
		 */
		public String getStatus() {
			BigDecimal pnl = getTotalPnl();
			if (targetReached)
				return "✓ 已达日盈利目标" + fmt(pnl) + "（实际不限制，让利润奔跑）";
			if (stopped)
				return "✗ 已触及日亏损上限" + fmt(pnl) + "，当日停止交易";
			if (consecutiveLosses >= 2)
				return "⏸ 连续" + consecutiveLosses + "亏，冷却中";
			return "▶ 交易中 累计" + pnl.setScale(2, RoundingMode.HALF_UP) + "U " + tradeCount + "次（盈利不限制）";
		}

		private String fmt(BigDecimal v) {
			return v.setScale(2, RoundingMode.HALF_UP) + "U";
		}
	}

	/**
	 * T仓位
	 */
	public static class TPosition {
		public String id;
		public long openTime;
		public String entryPrice;
		public String zhang; // 张数
		public String value; // 金额 = zhang × 4U
		public String tpPrice;
		public String slPrice;
		public String maxProfit = "0"; // 最大盈利金额（不是比例）
		public int stage = 0;
		public String status = "open";
		public int rescueCount = 0; // 解套补仓次数
		public String atrPercent = "0"; // 开仓时的ATR%(用于动态止盈止损)

		// 【必须添加】默认构造函数
		public TPosition() {
		}

		public TPosition(BigDecimal entryPrice, BigDecimal zhang) {
			this.id = "T" + (System.currentTimeMillis() % 100000);
			this.openTime = System.currentTimeMillis();
			this.entryPrice = entryPrice.toString();
			this.zhang = zhang.toString();
			// value在openPosition中根据品种设置
			this.value = "0";
			// 止盈止损价格也在openPosition中根据品种设置
			this.tpPrice = entryPrice.toString();
			this.slPrice = entryPrice.toString();
		}

		public BigDecimal getValue() {
			return new BigDecimal(value);
		}

		public BigDecimal getEntryPrice() {
			return new BigDecimal(entryPrice);
		}

		public BigDecimal getZhang() {
			return new BigDecimal(zhang);
		}

		public BigDecimal getMaxProfit() {
			return new BigDecimal(maxProfit);
		}

		public void setMaxProfit(BigDecimal profit) {
			this.maxProfit = profit.toString();
		}

		/**
		 * 计算当前盈亏率
		 */
		public BigDecimal getPnlRate(BigDecimal currentPrice) {
			return currentPrice.subtract(getEntryPrice()).divide(getEntryPrice(), 6, RoundingMode.HALF_UP);
		}
	}

	// ========== 入场评分系统（核心） ==========

	/**
	 * 入场质量评分（0-100分） 只有>=80分才允许交易
	 */
	public TradeScore calculateTradeScore(String symbol, BigDecimal currentPrice, BigDecimal rsi, BigDecimal atrPercent,
			String trend, BigDecimal volumeRatio) {

		TradeScore score = new TradeScore();
		int total = 0;

		// 1. RSI评分（25分）
		// 最佳区间：45-60，给25分
		// 30-45：超卖反弹，给20分
		// 60-70：偏强，给15分
		// <30或>70：不给分
		if (rsi != null) {
			if (rsi.compareTo(new BigDecimal("45")) >= 0 && rsi.compareTo(new BigDecimal("60")) <= 0) {
				score.rsiScore = 25;
				score.rsiComment = "RSI=" + rsi + " 健康区间";
			} else if (rsi.compareTo(new BigDecimal("30")) >= 0 && rsi.compareTo(new BigDecimal("45")) < 0) {
				score.rsiScore = 20;
				score.rsiComment = "RSI=" + rsi + " 超卖反弹";
			} else if (rsi.compareTo(new BigDecimal("60")) > 0 && rsi.compareTo(new BigDecimal("70")) <= 0) {
				score.rsiScore = 15;
				score.rsiComment = "RSI=" + rsi + " 偏强谨慎";
			} else {
				score.rsiScore = 0;
				score.rsiComment = "RSI=" + rsi + " 极端值，放弃";
				score.blocked = true;
			}
		}
		total += score.rsiScore;

		// 2. 趋势评分（25分）
		if ("up".equals(trend)) {
			score.trendScore = 25;
			score.trendComment = "上升趋势";
		} else if ("sideway_up".equals(trend)) {
			score.trendScore = 20;
			score.trendComment = "震荡偏多";
		} else if ("sideway".equals(trend)) {
			score.trendScore = 10;
			score.trendComment = "横盘震荡";
		} else {
			score.trendScore = 0;
			score.trendComment = "下降趋势，放弃";
			score.blocked = true;
		}
		total += score.trendScore;

		// 3. 波动率评分（20分）
		// ATR% 0.8%-2% 最佳
		if (atrPercent != null) {
			if (atrPercent.compareTo(new BigDecimal("0.008")) >= 0
					&& atrPercent.compareTo(new BigDecimal("0.020")) <= 0) {
				score.volatilityScore = 20;
				score.volComment = "波动率=" + atrPercent.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)
						+ "% 理想";
			} else if (atrPercent.compareTo(new BigDecimal("0.020")) > 0) {
				score.volatilityScore = 10;
				score.volComment = "波动率过高，谨慎";
			} else {
				score.volatilityScore = 5;
				score.volComment = "波动率偏低";
			}
		}
		total += score.volatilityScore;

		// 4. 成交量评分（15分）
		// 量比>1.2说明有资金关注
		if (volumeRatio != null) {
			if (volumeRatio.compareTo(new BigDecimal("1.5")) >= 0) {
				score.volumeScore = 15;
				score.volComment += " 量比=" + volumeRatio + " 放量";
			} else if (volumeRatio.compareTo(new BigDecimal("1.2")) >= 0) {
				score.volumeScore = 10;
				score.volComment += " 量比=" + volumeRatio + " 温和";
			} else {
				score.volumeScore = 5;
				score.volComment += " 量比=" + volumeRatio + " 缩量";
			}
		}
		total += score.volumeScore;

		// 5. 时间评分（15分）
		// 避开开盘9:00-9:10，收盘14:50-15:00
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int timeScore = 15;
		String timeComment = "正常时段";

		if (hour == 9 && minute < 10) {
			timeScore = 0;
			timeComment = "开盘波动大，观望";
			score.blocked = true;
		} else if (hour == 14 && minute >= 50) {
			timeScore = 5;
			timeComment = "收盘前，谨慎";
		} else if (hour == 11 && minute >= 25) {
			timeScore = 10;
			timeComment = "午盘前";
		} else if (hour == 13 && minute < 5) {
			timeScore = 10;
			timeComment = "午后开盘";
		}
		score.timeScore = timeScore;
		score.timeComment = timeComment;
		total += score.timeScore;

		score.totalScore = total;
		score.passed = !score.blocked && total >= 60;

		return score;
	}

	public static class TradeScore {
		public int rsiScore = 0;
		public int trendScore = 0;
		public int volatilityScore = 0;
		public int volumeScore = 0;
		public int timeScore = 0;
		public int totalScore = 0;
		public boolean passed = false;
		public boolean blocked = false;
		public String rsiComment = "";
		public String trendComment = "";
		public String volComment = "";
		public String timeComment = "";

		public String getSummary() {
			return String.format("评分=%d/100 RSI=%d %s 趋势=%d %s 波动=%d %s 成交=%d %s 时间=%d %s %s", totalScore, rsiScore,
					rsiComment, trendScore, trendComment, volatilityScore, volComment, volumeScore, volComment,
					timeScore, timeComment, passed ? "✓通过" : "✗放弃");
		}
	}

	// ========== 交易执行 ==========

	/**
	 * 检查是否可以开仓（XAUT版，支持自定义张数）
	 * 
	 * @param zhang      本次开仓张数（由fund.money控制）
	 * @param atrPercent 当前ATR波动率（如0.005表示0.5%），null则使用默认间距
	 */
	public CanTradeResult canOpen(String symbol, BigDecimal currentPrice, BigDecimal zhang, BigDecimal atrPercent) {
		CanTradeResult result = new CanTradeResult();
		ContractConfig config = getConfig(symbol);

		// 计算自适应间距：取配置间距和ATR的较大值（ATR×0.5作为最小有效间距）
		BigDecimal adaptiveGap = config.gridGap;
		if (atrPercent != null && atrPercent.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal atrBasedGap = atrPercent.multiply(new BigDecimal("0.5")); // ATR的一半作为间距
			adaptiveGap = adaptiveGap.max(atrBasedGap);
			// 限制最大间距不超过2%（绝对上限，防止极端行情无法开仓）
			BigDecimal maxGap = new BigDecimal("0.02");
			if (adaptiveGap.compareTo(maxGap) > 0) {
				adaptiveGap = maxGap;
			}
		}

		// 1. 检查每日状态
		DailyState state = getDailyState(symbol);
		if (!state.canTrade()) {
			result.reason = state.getStatus();
			result.allowed = false;
			return result;
		}

		// 2. 检查暂停时间
		long pauseUntil = getPauseUntil(symbol);
		if (System.currentTimeMillis() < pauseUntil) {
			int remainSec = (int) ((pauseUntil - System.currentTimeMillis()) / 1000);
			result.reason = "暂停中，剩余" + remainSec + "秒";
			result.allowed = false;
			return result;
		}

		// 3. 检查持仓数
		List<TPosition> positions = getPositions(symbol);
		if (positions.size() >= MAX_POSITIONS) {
			result.reason = "已达最大持仓" + MAX_POSITIONS;
			result.allowed = false;
			return result;
		}

		// 4. 检查冷却时间（根据品种）
		if (System.currentTimeMillis() - state.lastTradeTime < config.cooldownSec * 1000) {
			result.reason = "冷却中";
			result.allowed = false;
			return result;
		}

		// 5. 【已取消】偏离成本线检查 - 放宽限制允许更多交易机会
		// 只保留间距检查，确保网格间距合理
		if (!positions.isEmpty()) {
			TPosition last = positions.get(positions.size() - 1);
			BigDecimal priceGap = currentPrice.subtract(last.getEntryPrice())
					.divide(last.getEntryPrice(), 6, RoundingMode.HALF_UP).abs();
			if (priceGap.compareTo(adaptiveGap) < 0) {
				result.reason = "间距不足" + priceGap.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)
						+ "% < 要求" + adaptiveGap.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)
						+ "%";
				result.allowed = false;
				return result;
			}
		}

		// 6. 检查张数有效性（根据合约最小/最大下单限制）
		if (zhang == null || zhang.compareTo(config.minOrder) < 0) {
			result.reason = "张数不足最小下单量" + config.minOrder + "张（当前" + zhang + "张）";
			result.allowed = false;
			return result;
		}
		// 最大限制：合约限制和全局限制取较小值
		BigDecimal maxZhang = config.maxOrder;// .min(new BigDecimal("0.2"));
		if (zhang.compareTo(maxZhang) > 0) {
			zhang = maxZhang;
		}

		result.allowed = true;
		result.state = state;
		result.zhang = zhang;
		return result;
	}

	public static class CanTradeResult {
		public boolean allowed;
		public String reason;
		public DailyState state;
		public BigDecimal zhang; // 返回建议张数
		public boolean isTrapped = false; // 是否解套T模式
		public BigDecimal trapPct = BigDecimal.ZERO; // 被套百分比
	}

	/**
	 * 开仓（多品种适配，使用指定张数，支持ATR自适应止盈止损）
	 * 
	 * @param atrPercent 当前ATR百分比，用于动态调整止盈止损
	 */
	public TPosition openPosition(String symbol, BigDecimal entryPrice, TradeScore score, BigDecimal zhang,
			BigDecimal atrPercent) {
		ContractConfig config = getConfig(symbol);

		// ===== 全局资金池检查 =====
		// 1. 根据可用资金调整张数
		zhang = adjustZhangByMargin(symbol, zhang);
		if (zhang.compareTo(config.minOrder) < 0) {
			System.out.println("[T-Open] " + symbol + " 资金不足，无法开仓（需要" + config.minOrder + "张）");
			return null;
		}

		// 2. 记录保证金占用
		recordOpenMargin(symbol, zhang);

		TPosition pos = new TPosition(entryPrice, zhang);

		// 保存ATR用于后续动态调整
		pos.atrPercent = atrPercent != null ? atrPercent.toString() : "0";

		// 根据品种设置金额
		BigDecimal positionValue = zhang.multiply(config.valuePerZhang).multiply(entryPrice);
		pos.value = positionValue.toString();

		// ===== 止盈止损计算 =====
		BigDecimal actualTP;
		BigDecimal actualSL;

		if (config.usePercentMode && config.tpPercent != null && config.slPercent != null) {
			// ===== 百分比模式：完全按 fund.money 自适应 =====
			// 基础止盈止损 = 仓位价值 × 百分比
			actualTP = positionValue.multiply(config.tpPercent);
			actualSL = positionValue.multiply(config.slPercent);

			// ATR自适应调整：ATR主导，基础保底，3倍上限
			if (atrPercent != null && atrPercent.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal atrValue = atrPercent.multiply(positionValue);
				// ATR×1.5作为止盈，ATR×1.0作为止损，保持1.5:1盈亏比
				BigDecimal atrTP = atrValue.multiply(new BigDecimal("1.5"));
				BigDecimal atrSL = atrValue.multiply(new BigDecimal("1.0"));

				BigDecimal baseTP = positionValue.multiply(config.tpPercent);
				BigDecimal baseSL = positionValue.multiply(config.slPercent);

				// ATR主导，但保底基础值，上限3倍基础值
				actualTP = atrTP;
				actualSL = atrSL;

				// 保底：波动太小用基础值
				if (actualTP.compareTo(baseTP) < 0) {
					actualTP = baseTP;
					actualSL = baseSL;
					System.out.println("[T-ATR] 波动小，用保底止盈: " + actualTP + "U (ATR" + atrTP + "U)");
				}
				// 上限：波动太大限制在3倍基础值
				BigDecimal maxTP = baseTP.multiply(new BigDecimal("3"));
				BigDecimal maxSL = baseSL.multiply(new BigDecimal("3"));
				if (actualTP.compareTo(maxTP) > 0) {
					actualTP = maxTP;
					actualSL = maxSL;
					System.out.println("[T-ATR] 波动大，用上限止盈: " + actualTP + "U (ATR" + atrTP + "U)");
				} else if (actualTP.compareTo(baseTP) >= 0) {
					System.out.println("[T-ATR] 用ATR自适应止盈: " + actualTP + "U (基础" + baseTP + "U, ATR×1.5)");
				}
			}
		} else {
			// ===== 固定金额模式（默认） =====
			BigDecimal baseTP = config.tpAmount;
			BigDecimal baseSL = config.slAmount.abs();
			actualTP = baseTP;
			actualSL = baseSL;

			// ATR自适应调整
			if (atrPercent != null && atrPercent.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal atrValue = atrPercent.multiply(entryPrice).multiply(positionValue);
				BigDecimal atrTP = atrValue.multiply(new BigDecimal("1.5"));
				BigDecimal atrSL = atrValue.multiply(new BigDecimal("1.0"));

				actualTP = baseTP.max(atrTP);
				actualSL = baseSL.max(atrSL);

				BigDecimal maxTP = baseTP.multiply(new BigDecimal("3"));
				BigDecimal maxSL = baseSL.multiply(new BigDecimal("3"));
				if (actualTP.compareTo(maxTP) > 0)
					actualTP = maxTP;
				if (actualSL.compareTo(maxSL) > 0)
					actualSL = maxSL;
			}
		}

		// ===== 手续费检查（买卖合计约0.1%），最低要求0.15%覆盖成本=====
		BigDecimal feeRate = new BigDecimal("0.001"); // 0.1% 总手续费（买+卖）
		BigDecimal feeCost = positionValue.multiply(feeRate); // 手续费成本
		BigDecimal minProfit = positionValue.multiply(new BigDecimal("0.0005")); // 最低净利润要求 0.05%
		BigDecimal minCoverRate = new BigDecimal("0.0015"); // 最低止盈要求 0.15%
		BigDecimal minCoverTP = positionValue.multiply(minCoverRate); // 至少0.15%覆盖手续费
		BigDecimal netProfit = actualTP.subtract(feeCost); // 净利润

		// 检查是否满足最低0.15%要求
		if (actualTP.compareTo(minCoverTP) < 0) {
			System.out.println("[T-FeeWarn] " + symbol + " 止盈=" + actualTP + "U ("
					+ actualTP.divide(positionValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
					+ "%) 低于最低0.15%要求，调整到 " + minCoverTP + "U");
			actualTP = minCoverTP;
			// 同时调整止损保持盈亏比
			actualSL = actualTP.multiply(new BigDecimal("0.667")).setScale(6, RoundingMode.HALF_UP); // 1/1.5
		}

		// 再检查净利润是否为正
		if (netProfit.compareTo(minProfit) < 0) {
			System.out.println("[T-FeeWarn] " + symbol + " 净利润=" + netProfit + "U 不足，调整到覆盖成本+最低利润");
			actualTP = feeCost.add(minProfit);
			actualSL = actualTP.multiply(new BigDecimal("0.667")).setScale(6, RoundingMode.HALF_UP);
		}

		// 止盈止损价格 = 入场价 × (1 ± 金额/价值)
		BigDecimal tpRate = actualTP.divide(zhang.multiply(config.valuePerZhang).multiply(entryPrice), 6,
				RoundingMode.HALF_UP);
		BigDecimal slRate = actualSL.divide(zhang.multiply(config.valuePerZhang).multiply(entryPrice), 6,
				RoundingMode.HALF_UP);
		pos.tpPrice = entryPrice.multiply(BigDecimal.ONE.add(tpRate)).setScale(8, RoundingMode.HALF_UP).toString();
		pos.slPrice = entryPrice.multiply(BigDecimal.ONE.subtract(slRate)).setScale(8, RoundingMode.HALF_UP).toString();

		System.out.println("[T-Open-ATR] " + symbol + " ATR=" + atrPercent + " TP=" + actualTP + "U SL=" + actualSL
				+ "U 手续费=" + feeCost + "U 净利润=" + netProfit + "U" + "  止盈：" + pos.tpPrice + "(" + tpRate + ")" + "/"
				+ "止损：" + pos.slPrice + "(" + slRate + ")");

		List<TPosition> positions = getPositions(symbol);
		System.out.println(pos);
		positions.add(pos);
		System.out.println("size::::" + positions.size());
		savePositions(symbol, positions);
		System.out.println("======================" + getPositions(symbol).size());

		DailyState state = getDailyState(symbol);
		state.lastTradeTime = System.currentTimeMillis();
		saveDailyState(symbol, state);

		System.out.println("[T-Open] " + symbol + " " + pos.id + " @" + entryPrice + " 张数=" + zhang + " 价值="
				+ pos.getValue() + "U 评分=" + score.totalScore);
		return pos;
	}

	/**
	 * 检查退出信号
	 */
	public List<TExitSignal> checkExits(String symbol, BigDecimal currentPrice) {
		List<TExitSignal> signals = new ArrayList<>();
		List<TPosition> positions = getPositions(symbol);
		DailyState state = getDailyState(symbol);
		ContractConfig config = getConfig(symbol);

		for (TPosition pos : positions) {
			System.out.println("=======仓位检测=========");
			System.out.println(new JSONObject(pos).toString());
			if (!"open".equals(pos.status))
				continue;

			BigDecimal entry = pos.getEntryPrice();
			BigDecimal pnlRate = currentPrice.subtract(entry).divide(entry, 6, RoundingMode.HALF_UP);
			BigDecimal pnlAmount = pnlRate.multiply(pos.getValue()); // 盈亏金额

			// 更新最大盈利金额
			if (pnlAmount.compareTo(pos.getMaxProfit()) > 0) {
				pos.setMaxProfit(pnlAmount);
			}

			// 计算ATR自适应止盈止损（基于开仓时保存的ATR）
			BigDecimal posAtr = new BigDecimal(pos.atrPercent);
			BigDecimal baseTP = config.tpAmount;
			BigDecimal baseSL = config.slAmount.abs();
			BigDecimal actualTP = baseTP;
			BigDecimal actualSL = baseSL.negate();

			if (posAtr.compareTo(BigDecimal.ZERO) > 0) {
				// 使用开仓时的ATR计算动态目标
				BigDecimal atrValue = posAtr.multiply(pos.getEntryPrice()).multiply(pos.getZhang())
						.multiply(config.valuePerZhang);
				BigDecimal atrTP = atrValue.multiply(new BigDecimal("1.5"));
				BigDecimal atrSL = atrValue.multiply(new BigDecimal("1.0")).negate();

				// ATR主导，保底基础值，上限3倍基础值（与开仓逻辑一致）
				actualTP = atrTP;
				actualSL = atrSL;

				// 保底：波动太小用基础值
				if (actualTP.compareTo(baseTP) < 0) {
					actualTP = baseTP;
					actualSL = baseSL.negate();
				}
				// 上限：波动太大限制在3倍基础值
				BigDecimal maxTP = baseTP.multiply(new BigDecimal("3"));
				BigDecimal maxSL = baseSL.multiply(new BigDecimal("3")).negate();
				if (actualTP.compareTo(maxTP) > 0)
					actualTP = maxTP;
				if (actualSL.compareTo(maxSL) < 0)
					actualSL = maxSL;
			}

			// 解套T和标准T使用相同的ATR动态计算（统一风控标准）
			if (pos.rescueCount > 0) {
				// 解套T与标准T一致：止盈ATR×1.5，止损ATR×1.0，保持1.5:1盈亏比
				if (posAtr.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal atrValue = posAtr.multiply(pos.getEntryPrice()).multiply(pos.getZhang())
							.multiply(config.valuePerZhang);
					BigDecimal atrTP = atrValue.multiply(new BigDecimal("1.5")); // ATR×1.5
					BigDecimal atrSL = atrValue.multiply(new BigDecimal("1.0")).negate(); // ATR×1.0

					// ATR主导，保底基础值，上限3倍基础值（与标准T完全一致）
					actualTP = atrTP;
					actualSL = atrSL;

					// 保底：波动太小用基础值
					if (actualTP.compareTo(baseTP) < 0) {
						actualTP = baseTP;
						actualSL = baseSL.negate();
					}
					// 上限：波动太大限制在3倍基础值
					BigDecimal maxTP = baseTP.multiply(new BigDecimal("3"));
					BigDecimal maxSL = baseSL.multiply(new BigDecimal("3")).negate();
					if (actualTP.compareTo(maxTP) > 0)
						actualTP = maxTP;
					if (actualSL.compareTo(maxSL) < 0)
						actualSL = maxSL;

					System.out
							.println("[T-Rescue] 解套ATR模式 ATR=" + posAtr + " 止盈=" + actualTP + "U 止损=" + actualSL + "U");
				}
			}

			// 1. 止损检查
			if (pnlAmount.compareTo(actualSL) <= 0) {
				String reason = pos.rescueCount > 0 ? "解套止损" : "止损";
				signals.add(new TExitSignal(pos.id, "SL", currentPrice, pnlAmount,
						reason + pnlAmount.setScale(2, RoundingMode.HALF_UP) + "U (目标" + actualSL + "U)"));
				pos.status = "closing";
				continue;
			}

			// 2. 止盈检查
			if (pnlAmount.compareTo(actualTP) >= 0) {
				String reason = pos.rescueCount > 0 ? "解套止盈" : "止盈";
				signals.add(new TExitSignal(pos.id, "TP", currentPrice, pnlAmount,
						reason + pnlAmount.setScale(2, RoundingMode.HALF_UP) + "U (目标" + actualTP + "U)"));
				pos.status = "closing";
				continue;
			}

		}

		savePositions(symbol, positions);
		return signals;
	}

	/**
	 * 执行平仓
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

		if (target == null)
			return;

		// 计算盈亏（使用signal中的盈亏金额）
		BigDecimal grossPnl = signal.pnlRate; // 这是盈亏金额（U）
		BigDecimal fee = target.getValue().multiply(new BigDecimal("0.002")); // 0.2%手续费
		BigDecimal netPnl = grossPnl.subtract(fee);

		// 更新每日状态
		DailyState state = getDailyState(symbol);
		state.setTotalPnl(state.getTotalPnl().add(netPnl));
		state.tradeCount++;

		if (netPnl.compareTo(BigDecimal.ZERO) > 0) {
			state.winCount++;
			state.consecutiveLosses = 0; // 重置连续亏损
		} else {
			state.lossCount++;
			state.consecutiveLosses++;
			// 连续2亏，暂停30分钟
			if (state.consecutiveLosses >= 2) {
				setPauseUntil(symbol, System.currentTimeMillis() + 30 * 60 * 1000);
			}
		}

		// 检查是否达成目标或触及止损
		if (state.getTotalPnl().compareTo(DAILY_PROFIT_TARGET) >= 0) {
			state.targetReached = true;
		} else if (state.getTotalPnl().compareTo(DAILY_LOSS_LIMIT) <= 0) {
			state.stopped = true;
		}

		saveDailyState(symbol, state);

		// 释放保证金
		recordCloseMargin(symbol, target.getZhang());

		// 移除或更新持仓
		if ("SL".equals(signal.type) || "TRAILING".equals(signal.type) || "TP2".equals(signal.type)
				|| "TP".equals(signal.type)) {
			positions.remove(target);
		}
		savePositions(symbol, positions);

		System.out.println("[T-Close] " + symbol + " " + target.id + " " + signal.type + " @" + exitPrice + " 盈亏="
				+ netPnl.setScale(3, RoundingMode.HALF_UP) + "U 累计="
				+ state.getTotalPnl().setScale(2, RoundingMode.HALF_UP) + "U " + state.getStatus());
	}

	/**
	 * 获取每日状态报告
	 */
	public String getDailyReport(String symbol) {
		DailyState state = getDailyState(symbol);
		List<TPosition> positions = getPositions(symbol);

		StringBuilder sb = new StringBuilder();
		sb.append("\n========== ").append(symbol).append(" 做T日报 ==========\n");
		sb.append("状态: ").append(state.getStatus()).append("\n");
		sb.append("交易: ").append(state.tradeCount).append("次 盈").append(state.winCount).append(" 亏")
				.append(state.lossCount);
		if (state.tradeCount > 0) {
			sb.append(" 胜率").append((double) state.winCount / state.tradeCount * 100).append("%");
		}
		sb.append("\n");
		sb.append("持仓: ").append(positions.size()).append("组\n");
		sb.append("目标: 盈利不限制（让利润奔跑）, ");
		sb.append("亏损<=").append(DAILY_LOSS_LIMIT).append("U 停止（硬性风控）\n");
		sb.append("==================================\n");

		return sb.toString();
	}

	/**
	 * 获取T仓位实时报告（用于周期性打印）
	 */
	public String getPositionReport(String symbol) {
		List<TPosition> positions = getPositions(symbol);
		DailyState state = getDailyState(symbol);

		StringBuilder sb = new StringBuilder();
		sb.append("\n========== T仓位报告 ==========\n");
		sb.append("品种: ").append(symbol).append("\n");
		sb.append("时间: ").append(new SimpleDateFormat("HH:mm:ss").format(new Date())).append("\n");
		sb.append("状态: ").append(state.getStatus()).append("\n");
		sb.append("持仓: ").append(positions.size()).append("/").append(MAX_POSITIONS).append("\n");

		if (positions.isEmpty()) {
			sb.append("  (无持仓)\n");
		} else {
			BigDecimal totalZhang = BigDecimal.ZERO;
			BigDecimal totalValue = BigDecimal.ZERO;
			for (TPosition pos : positions) {
				sb.append("  - ").append(pos.id).append(" 张数=").append(pos.zhang).append(" 成本=")
						.append(pos.getEntryPrice()).append(" 状态=").append(pos.status);
				if (!"0".equals(pos.atrPercent)) {
					sb.append(" ATR=").append(new BigDecimal(pos.atrPercent).multiply(new BigDecimal("100")).setScale(2,
							RoundingMode.HALF_UP)).append("%");
				}
				sb.append("\n");
				totalZhang = totalZhang.add(pos.getZhang());
				totalValue = totalValue.add(pos.getValue());
			}
			sb.append("合计: ").append(totalZhang).append("张 ").append(totalValue.setScale(2, RoundingMode.HALF_UP))
					.append("U\n");
		}

		// 盈亏平衡价
		BigDecimal bePrice = getBreakevenPrice(symbol);
		if (bePrice.compareTo(BigDecimal.ZERO) > 0) {
			sb.append("盈亏平衡价: ").append(bePrice).append("\n");
		}

		sb.append("==============================\n");
		return sb.toString();
	}

	// ========== 全局资金池管理（多币种共享）==========

	/**
	 * 计算当前已用保证金（所有币种合计）
	 */
	public BigDecimal getTotalUsedMargin() {
		String val = jedisClient.get(KEY_GLOBAL_MARGIN);
		return val == null ? BigDecimal.ZERO : new BigDecimal(val);
	}

	/**
	 * 更新已用保证金
	 */
	private void updateUsedMargin(BigDecimal delta) {
		BigDecimal current = getTotalUsedMargin();
		BigDecimal updated = current.add(delta);
		if (updated.compareTo(BigDecimal.ZERO) < 0)
			updated = BigDecimal.ZERO;
		jedisClient.set(KEY_GLOBAL_MARGIN, updated.toString());
		jedisClient.expire(KEY_GLOBAL_MARGIN, 86400);
	}

	/**
	 * 检查是否有足够保证金开仓
	 * 
	 * @param neededMargin 本次需要占用的保证金
	 * @return true=可以开仓, false=资金不足
	 */
	public boolean hasEnoughMargin(BigDecimal neededMargin) {
		BigDecimal maxAllowed = TOTAL_CAPITAL.multiply(MAX_MARGIN_USAGE); // 30U × 80% = 24U
		BigDecimal used = getTotalUsedMargin();
		BigDecimal available = maxAllowed.subtract(used);
		return neededMargin.compareTo(available) <= 0;
	}

	/**
	 * 获取可用保证金
	 */
	public BigDecimal getAvailableMargin() {
		BigDecimal maxAllowed = TOTAL_CAPITAL.multiply(MAX_MARGIN_USAGE);
		return maxAllowed.subtract(getTotalUsedMargin());
	}

	/**
	 * 计算开仓所需保证金
	 */
	public BigDecimal calculateMargin(String symbol, BigDecimal zhang) {
		ContractConfig config = getConfig(symbol);
		BigDecimal notionalValue = zhang.multiply(config.valuePerZhang); // 名义价值
		return notionalValue.divide(config.leverage, 6, RoundingMode.HALF_UP); // 保证金 = 名义价值 / 杠杆
	}

	/**
	 * 根据可用资金动态调整开仓张数
	 * 
	 * @param zhang 希望开仓的张数
	 * @return 调整后的张数（如果资金不足会减小）
	 */
	public BigDecimal adjustZhangByMargin(String symbol, BigDecimal zhang) {
		ContractConfig config = getConfig(symbol);
		BigDecimal neededMargin = calculateMargin(symbol, zhang);

		if (hasEnoughMargin(neededMargin)) {
			return zhang; // 资金充足，按原计划
		}

		// 资金不足，计算最大可开张数
		BigDecimal available = getAvailableMargin();
		BigDecimal maxZhang = available.multiply(config.leverage).divide(config.valuePerZhang, 6, RoundingMode.HALF_UP);

		// 向下取整到最小下单单位
		BigDecimal minOrder = config.minOrder;
		maxZhang = maxZhang.divide(minOrder, 0, RoundingMode.DOWN).multiply(minOrder);

		if (maxZhang.compareTo(minOrder) < 0) {
			return BigDecimal.ZERO; // 资金不足以开最小单
		}

		System.out.println("[T-Margin] " + symbol + " 资金不足，调整张数: " + zhang + "→" + maxZhang);
		return maxZhang;
	}

	/**
	 * 记录开仓占用的保证金
	 */
	public void recordOpenMargin(String symbol, BigDecimal zhang) {
		BigDecimal margin = calculateMargin(symbol, zhang);
		updateUsedMargin(margin);
		System.out.println("[T-Margin] " + symbol + " 开仓占用: " + margin + "U, 总计: " + getTotalUsedMargin() + "U");
	}

	/**
	 * 记录平仓释放的保证金
	 */
	public void recordCloseMargin(String symbol, BigDecimal zhang) {
		BigDecimal margin = calculateMargin(symbol, zhang);
		updateUsedMargin(margin.negate());
		System.out.println("[T-Margin] " + symbol + " 平仓释放: " + margin + "U, 剩余: " + getTotalUsedMargin() + "U");
	}

	/**
	 * 获取全局资金报告（所有币种合计）
	 */
	public String getGlobalMarginReport() {
		BigDecimal used = getTotalUsedMargin();
		BigDecimal max = TOTAL_CAPITAL.multiply(MAX_MARGIN_USAGE);
		BigDecimal available = max.subtract(used);
		double usageRate = used.doubleValue() / max.doubleValue() * 100;

		StringBuilder sb = new StringBuilder();
		sb.append("\n========== 全局资金报告 ==========\n");
		sb.append("总本金: ").append(TOTAL_CAPITAL).append("U\n");
		sb.append("最大可用: ").append(max).append("U (").append(MAX_MARGIN_USAGE.multiply(new BigDecimal("100")))
				.append("%)\n");
		sb.append("已占用: ").append(used).append("U (").append(String.format("%.1f", usageRate)).append("%)\n");
		sb.append("剩余可用: ").append(available).append("U\n");
		sb.append("==================================\n");
		return sb.toString();
	}

	// ========== Redis 操作 ==========

	public DailyState getDailyState(String symbol) {
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String key = KEY_DAILY_STATE.replace("{symbol}", symbol).replace("{date}", date);
		String json = jedisClient.get(key);
		if (StrUtil.isBlank(json)) {
			return new DailyState();
		}
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

	private void savePositions(String symbol, List<TPosition> positions) {
		System.out.println("[Debug-SAVE] symbol=" + symbol + ", positions.size=" + positions.size());

		if (symbol == null || positions == null) {
			System.out.println("[Debug-SAVE] 参数为空，跳过");
			return;
		}

		try {
			String key = KEY_POSITIONS.replace("{symbol}", symbol);
			String json = JSONUtil.toJsonStr(positions);

			System.out.println("[Debug-SAVE] key=" + key);
			System.out.println("[Debug-SAVE] json长度=" + json.length() + ", 前100字符="
					+ json.substring(0, Math.min(100, json.length())));

			String result = jedisClient.set(key, json);
			System.out.println("[Debug-SAVE] redis set 返回=" + result);

			// 立即验证
			String verify = jedisClient.get(key);
			System.out.println("[Debug-SAVE] 立即验证读取=" + (verify != null ? "成功,长度=" + verify.length() : "失败"));

		} catch (Exception e) {
			System.out.println("[Debug-SAVE] 异常: " + e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	private List<TPosition> getPositions(String symbol) {
		System.out.println("[Debug-GET] symbol=" + symbol);

		try {
			String key = KEY_POSITIONS.replace("{symbol}", symbol);
			System.out.println("[Debug-GET] key=" + key);

			String val = jedisClient.get(key);
			System.out.println("[Debug-GET] redis get 返回=" + (val != null ? "非空,长度=" + val.length() : "NULL"));

			if (val == null || val.isEmpty()) {
				return new ArrayList<>();
			}

			List<TPosition> list = JSONUtil.toList(val, TPosition.class);
			System.out.println("[Debug-GET] 反序列化成功, size=" + list.size());
			return list;

		} catch (Exception e) {
			System.out.println("[Debug-GET] 异常: " + e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
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

	public void setBreakevenPrice(String symbol, BigDecimal price) {
		String key = "t:be:{symbol}".replace("{symbol}", symbol);
		jedisClient.set(key, price.toString());
	}

	public BigDecimal getBreakevenPrice(String symbol) {
		String key = "t:be:{symbol}".replace("{symbol}", symbol);
		String val = jedisClient.get(key);
		return val == null ? BigDecimal.ZERO : new BigDecimal(val);
	}

	// ========== ATR计算 ==========

	/**
	 * 更新价格数据并计算ATR 每次价格tick时调用，维护14周期的ATR
	 * 
	 * @param high  当前最高价（tick数据可用当前价代替）
	 * @param low   当前最低价（tick数据可用当前价代替）
	 * @param close 当前收盘价/最新价
	 */
	public void updatePrice(String symbol, BigDecimal high, BigDecimal low, BigDecimal close) {
		String key = "t:atr:{symbol}".replace("{symbol}", symbol);

		// 读取历史价格
		List<PriceData> prices = getPriceHistory(symbol);

		// 添加新数据点
		prices.add(new PriceData(high, low, close, System.currentTimeMillis()));

		// 保持最多100个数据点
		while (prices.size() > MAX_PRICE_HISTORY) {
			prices.remove(0);
		}

		// 保存回Redis
		savePriceHistory(symbol, prices);
	}

	/**
	 * 计算当前ATR（14周期）
	 * 
	 * @return ATR值（价格绝对值），如果数据不足返回null
	 */
	public BigDecimal calculateATR(String symbol) {
		List<PriceData> prices = getPriceHistory(symbol);
		if (prices.size() < ATR_PERIOD + 1) {
			return null; // 数据不足
		}

		// 取最近ATR_PERIOD+1个数据点（需要前一个close计算TR）
		List<PriceData> recent = prices.subList(prices.size() - ATR_PERIOD - 1, prices.size());

		BigDecimal trSum = BigDecimal.ZERO;
		BigDecimal prevClose = recent.get(0).close;

		for (int i = 1; i < recent.size(); i++) {
			PriceData curr = recent.get(i);

			// TR = max(High-Low, |High-Close_prev|, |Low-Close_prev|)
			BigDecimal hl = curr.high.subtract(curr.low).abs();
			BigDecimal hcp = curr.high.subtract(prevClose).abs();
			BigDecimal lcp = curr.low.subtract(prevClose).abs();

			BigDecimal tr = hl.max(hcp).max(lcp);
			trSum = trSum.add(tr);

			prevClose = curr.close;
		}

		// ATR = TR之和 / 周期
		return trSum.divide(new BigDecimal(ATR_PERIOD), 8, RoundingMode.HALF_UP);
	}

	/**
	 * 获取ATR百分比（ATR/当前价格）
	 * 
	 * @return ATR百分比，如0.005表示0.5%
	 */
	public BigDecimal getATRPercent(String symbol, BigDecimal currentPrice) {
		BigDecimal atr = calculateATR(symbol);
		if (atr == null || currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
			return null;
		}
		return atr.divide(currentPrice, 6, RoundingMode.HALF_UP);
	}

	/**
	 * 从K线数据计算ATR（更准确的版本） 调用OKX K线接口后，用返回的数据计算
	 * 
	 * @param candles K线列表（需包含high/low/close），时间升序排列
	 * @return ATR百分比（如0.005表示0.5%）
	 */
	public BigDecimal calculateATRFromKlines(List<?> candles, BigDecimal currentPrice) {
		if (candles == null || candles.size() < ATR_PERIOD + 1) {
			return null;
		}

		// 取最近ATR_PERIOD+1根K线
		List<?> recent = candles.subList(candles.size() - ATR_PERIOD - 1, candles.size());

		BigDecimal trSum = BigDecimal.ZERO;
		BigDecimal prevClose = null;

		for (int i = 0; i < recent.size(); i++) {
			Object candle = recent.get(i);
			try {
				// 使用反射获取high/low/close，适配Candle类或其他K线类
				java.lang.reflect.Method getHigh = candle.getClass().getMethod("getHigh");
				java.lang.reflect.Method getLow = candle.getClass().getMethod("getLow");
				java.lang.reflect.Method getClose = candle.getClass().getMethod("getClose");

				BigDecimal high = BigDecimal.valueOf(((Number) getHigh.invoke(candle)).doubleValue());
				BigDecimal low = BigDecimal.valueOf(((Number) getLow.invoke(candle)).doubleValue());
				BigDecimal close = BigDecimal.valueOf(((Number) getClose.invoke(candle)).doubleValue());

				if (i > 0) {
					// TR = max(High-Low, |High-Close_prev|, |Low-Close_prev|)
					BigDecimal hl = high.subtract(low).abs();
					BigDecimal hcp = high.subtract(prevClose).abs();
					BigDecimal lcp = low.subtract(prevClose).abs();
					BigDecimal tr = hl.max(hcp).max(lcp);
					trSum = trSum.add(tr);
				}
				prevClose = close;
			} catch (Exception e) {
				return null; // 反射失败返回null
			}
		}

		// ATR = TR之和 / 周期
		BigDecimal atr = trSum.divide(new BigDecimal(ATR_PERIOD), 8, RoundingMode.HALF_UP);
		return atr.divide(currentPrice, 6, RoundingMode.HALF_UP); // 返回百分比
	}

	private List<PriceData> getPriceHistory(String symbol) {
		String key = "t:atr:{symbol}".replace("{symbol}", symbol);
		String json = jedisClient.get(key);
		List<PriceData> list = new ArrayList<>();

		if (json != null && !json.isEmpty()) {
			try {
				String[] items = json.split("\\|");
				for (String item : items) {
					if (item.isEmpty())
						continue;
					String[] parts = item.split(",");
					if (parts.length >= 4) {
						list.add(new PriceData(new BigDecimal(parts[0]), new BigDecimal(parts[1]),
								new BigDecimal(parts[2]), Long.parseLong(parts[3])));
					}
				}
			} catch (Exception e) {
				// 解析失败返回空列表
			}
		}
		return list;
	}

	private void savePriceHistory(String symbol, List<PriceData> prices) {
		String key = "t:atr:{symbol}".replace("{symbol}", symbol);
		StringBuilder sb = new StringBuilder();

		for (PriceData p : prices) {
			if (sb.length() > 0)
				sb.append("|");
			sb.append(p.high.toPlainString()).append(",").append(p.low.toPlainString()).append(",")
					.append(p.close.toPlainString()).append(",").append(p.time);
		}

		jedisClient.set(key, sb.toString());
		jedisClient.expire(key, 86400); // 24小时过期
	}

	/**
	 * 强制平掉所有T持仓（日终强制平仓）
	 */
	@Autowired
	private cn.exrick.manager.service.OkxService okxService;

	@Autowired
	private cn.exrick.manager.service.FundService fundService;

	public BigDecimal forceCloseAll(String symbol, BigDecimal exitPrice) {
		List<TPosition> positions = getPositions(symbol);
		DailyState state = getDailyState(symbol);
		BigDecimal totalPnl = BigDecimal.ZERO;

		// ===== 分别平仓每个T仓位（支持posId）=====
		if (!positions.isEmpty() && fundService != null) {

			// 构建tableName
			String tableName = "fund_1_gaoduanzhuangbei2_ok";
			if (symbol.contains("XAUT")) {
				tableName = "fund_1_gaoduanzhuangbei2_ok";
			} else if (symbol.contains("DOGE")) {
				tableName = "fund_2_gaoduanzhuangbei2_ok";
			}

			cn.exrick.manager.pojo.Fund fundTemp = new cn.exrick.manager.pojo.Fund();
			fundTemp.setCode(symbol);
			fundTemp.setName(symbol);

			// 分别平仓每个仓位
			for (TPosition pos : positions) {
				if (!"open".equals(pos.status))
					continue;

				BigDecimal closeZhang = pos.getZhang();
				BigDecimal entryPrice = pos.getEntryPrice();

				// 计算单个仓位盈亏
				BigDecimal pnlRate = exitPrice.subtract(entryPrice).divide(entryPrice, 6, RoundingMode.HALF_UP);
				BigDecimal pnlAmount = pnlRate.multiply(pos.getValue());
				BigDecimal fee = pos.getValue().multiply(new BigDecimal("0.002"));
				BigDecimal netPnl = pnlAmount.subtract(fee);
				totalPnl = totalPnl.add(netPnl);

				System.out.println(
						"【日终强平】平仓T仓位 " + pos.id + " 张数=" + closeZhang + " 成本=" + entryPrice + " 现价=" + exitPrice);

				try {
					// 1. 创建临时档位对象
					cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok cwTemp = new cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok();
					int tId = -1 * (pos.id.hashCode() % 100000);
					cwTemp.setId(tId);
					int uniqueLevel = 900 + (pos.id.hashCode() % 100);
					cwTemp.setLevel(uniqueLevel);
					cwTemp.setCode(symbol);
					cwTemp.setName(symbol + "_bs"); // 加bs标记
					cwTemp.setFene(closeZhang);
					cwTemp.setBuypriceReal(entryPrice);
					cwTemp.setBuyprice(entryPrice);
					cwTemp.setCurrentprice(exitPrice);
					cwTemp.setDt(new Date());
					cwTemp.setIscurrent(0);
					cwTemp.setComment("日终强平-T平仓：" + pos.id + " " + closeZhang + "张");
					cwTemp.setZhisun(0);
					cwTemp.setZhiying(BigDecimal.ZERO);

					// 2. 获取保存的posId（双向持仓需要）
					String posKey = "t:position:" + symbol + ":" + pos.id;
					String closePosId = jedisClient.get(posKey + ":okxPosId");
					String lastValueWithPosId = "T" + pos.id;
					if (closePosId != null && !closePosId.isEmpty()) {
						lastValueWithPosId = "T" + pos.id + ":" + closePosId;
						cwTemp.setFirsttime(closePosId); // 设置posId到firsttime字段
						System.out.println("【日终强平-T平仓】使用posId: " + closePosId);
					}

					// 3. 调用FundService下单（alarmtag=3表示卖出）
					fundService.updateCurrentPrice(tableName, exitPrice, 3, exitPrice, closeZhang, fundTemp, cwTemp,
							lastValueWithPosId);

					System.out.println("【日终强平-T平仓成功】" + pos.id + " " + closeZhang + "张 盈亏="
							+ netPnl.setScale(3, RoundingMode.HALF_UP) + "U");

					// 4. 清理该仓位的Redis记录
					jedisClient.del(posKey + ":zhang");
					jedisClient.del(posKey + ":okxPosId");

				} catch (Exception e) {
					System.err.println("【日终强平-T平仓失败】" + pos.id + "：" + e.getMessage());
					e.printStackTrace();
					// 继续平仓其他仓位，不return
				}
			}

			// 清理持仓列表
			jedisClient.del(KEY_POSITIONS.replace("{symbol}", symbol));
		}

		// 更新当日总盈亏
		state.setTotalPnl(state.getTotalPnl().add(totalPnl));
		saveDailyState(symbol, state);

		System.out.println("【做T-日终强平】" + symbol + " 今日T总盈亏：" + state.getTotalPnl().setScale(2, RoundingMode.HALF_UP)
				+ "U " + (state.getTotalPnl().compareTo(BigDecimal.ZERO) >= 0 ? "✅盈利" : "❌亏损"));
		return state.getTotalPnl();
	}

	/**
	 * 【合约全平后】直接清理T仓位Redis记录（不下单，因为OKX已全平）
	 */
	public void clearAllPositionsRedis(String symbol) {
		List<TPosition> positions = getPositions(symbol);
		DailyState state = getDailyState(symbol);

		if (positions.isEmpty()) {
			System.out.println("【T仓位清理】" + symbol + " 无T仓位需要清理");
			return;
		}

		System.out.println("【T仓位清理】" + symbol + " 清理 " + positions.size() + " 个T仓位Redis记录");

		for (TPosition pos : positions) {
			String posKey = "t:position:" + symbol + ":" + pos.id;
			jedisClient.del(posKey + ":zhang");
			jedisClient.del(posKey + ":okxPosId");
			System.out.println("【T仓位清理】" + symbol + " 清理 " + pos.id);
		}

		// 清理持仓列表
		jedisClient.del(KEY_POSITIONS.replace("{symbol}", symbol));

		System.out.println("【T仓位清理】" + symbol + " 完成，清理 " + positions.size() + " 个仓位");
	}

	/**
	 * 检查是否需要日终强制平仓（每天23:55执行）
	 */
	public void checkDailyForceClose(String symbol, BigDecimal currentPrice) {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);

		// 每天23:55-23:59强制平仓
		if (hour == 23 && minute >= 55) {
			String key = "t:daily:closed:" + symbol + ":" + new SimpleDateFormat("yyyyMMdd").format(now.getTime());
			if (!jedisClient.exists(key)) {
				// 今天还没平仓，执行
				System.out.println("【日终强平触发】" + symbol + " 时间：" + hour + ":" + minute);
				forceCloseAll(symbol, currentPrice);
				jedisClient.setex(key, 3600, "1"); // 标记今天已平仓
			}
		}
	}

	public static class TExitSignal {
		public String positionId;
		public String type;
		public BigDecimal price;
		public BigDecimal pnlRate;
		public String reason;

		public TExitSignal(String positionId, String type, BigDecimal price, BigDecimal pnlRate, String reason) {
			this.positionId = positionId;
			this.type = type;
			this.price = price;
			this.pnlRate = pnlRate;
			this.reason = reason;
		}
	}
}
