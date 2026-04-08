package cn.exrick.manager.service.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
//import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
//import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class okx2 {

	public static void main(String[] args) {
		// 1H NOT 适合空；PEPE 适合空；people 空多都行；DOGE 适合空；BTC 适合多；
		// 15m NOT多空均可；PEPE 只能空；PEOPLE 做多，但是时间长；DOGE 情况不明；BTC 适合做多。
		// 1m BTC不清楚；NOT 适合多；pepe 不清楚；PEOPLE 适合多；DOGE 适合多。

		String coinString = "DOGE-USDT-SWAP";
		int zhisun = 1;
		int zhisunfanxiang = 1;

		String url = "https://www.okx.com/api/v5/market/candles?instId=" + coinString + "&bar=15m&limit=100";// history-

		String dtString = "[{\"data\":[[\"1728204120000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204180000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204240000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204300000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204360000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204420000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204480000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204540000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204600000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"1\",\"100\",\"0.7319\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204660000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204720000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204780000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204840000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204900000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728204960000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205020000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205080000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205140000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205200000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205260000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205320000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205380000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205440000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205500000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205560000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205620000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205680000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205740000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205800000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205860000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205920000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"1\",\"100\",\"0.7301\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728205980000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206040000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206100000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206160000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"1\",\"100\",\"0.7319\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206220000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206280000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206340000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206400000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206460000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206520000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206580000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206640000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206700000\",\"0.007329\",\"0.007337\",\"0.007329\",\"0.007337\",\"238\",\"23800\",\"174.4424\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206760000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206820000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206880000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728206940000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207000000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207060000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207120000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207180000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"1\",\"100\",\"0.7319\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207240000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207300000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207360000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207420000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207480000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207540000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207600000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207660000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207720000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207780000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207840000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207900000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728207960000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"1\",\"100\",\"0.7337\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208020000\",\"0.007337\",\"0.007337\",\"0.007337\",\"0.007337\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208080000\",\"0.007347\",\"0.007358\",\"0.007347\",\"0.007358\",\"250658\",\"25065800\",\"184209.6449\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208140000\",\"0.007358\",\"0.007358\",\"0.007358\",\"0.007358\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208200000\",\"0.007358\",\"0.007358\",\"0.007358\",\"0.007358\",\"0\",\"0\",\"0\",\"0\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208440000\",\"0.007319\",\"0.00732\",\"0.007316\",\"0.00732\",\"206744\",\"20674400\",\"151292.4765\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208500000\",\"0.00732\",\"0.00732\",\"0.00732\",\"0.00732\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208560000\",\"0.00732\",\"0.00732\",\"0.00732\",\"0.00732\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208620000\",\"0.00732\",\"0.00732\",\"0.00732\",\"0.00732\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208680000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"1\",\"100\",\"0.7301\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208740000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208800000\",\"0.007303\",\"0.007303\",\"0.007299\",\"0.007299\",\"206743\",\"20674300\",\"150945.9363\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208860000\",\"0.007299\",\"0.007299\",\"0.007299\",\"0.007299\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208920000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"1\",\"100\",\"0.7319\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728208980000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209040000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209100000\",\"0.007319\",\"0.007319\",\"0.007319\",\"0.007319\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209160000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"4950\",\"495000\",\"3615.48\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209220000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209280000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209340000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209400000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209460000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209520000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209580000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209640000\",\"0.007304\",\"0.007304\",\"0.007304\",\"0.007304\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209700000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"1\",\"100\",\"0.7301\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209760000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209820000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209880000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728209940000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728210000000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728210060000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728210120000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728210180000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"1\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}},{\"data\":[[\"1728210240000\",\"0.007301\",\"0.007301\",\"0.007301\",\"0.007301\",\"0\",\"0\",\"0\",\"0\"]],\"arg\":{\"instId\":\"NOT-USDT-SWAP\",\"channel\":\"candle1m\"}}]";
		JSONArray json = new JSONArray(dtString);

		Map<String, Integer> buytag = getBuyTag(coinString, url, "0.15", zhisun, "0.15", zhisunfanxiang, null);

		System.out.println("buytag:" + buytag);
	}

	public static Properties getProperties() throws IOException {
		// 使用 FileInputStream 直接从文件系统中读取文件
		InputStream in = new FileInputStream("d:\\signal.property");
		Properties properties = new Properties();
		properties.load(in);
//        in.close(); // 不要忘记关闭 InputStream  

		return properties;
	}

	public static Map<String, Integer> getBuyTag(String code, String url, String zhiying, int zhisun,
			String zhiyingfanxiang, int zhisunfanxiang, JSONArray ja) {

		if (1 == 1) {
			Map<String, Integer> reMap = new HashMap<String, Integer>();
			reMap.put("buytag", 0);// 金叉附近。1金叉附近，0不是金叉附近，-1死叉附近

			reMap.put("tempbuytag", 1);// 最近一次是金叉还是死叉。非0情况下，修正buytagpperiod
			reMap.put("chajufangxiang", 1);// 增强标记。和temptag配套使用。
			reMap.put("cate", 0);
			reMap.put("currentbuytag", 0);

			return reMap;

		}
		Properties pro = new Properties();
		try {
			pro = getProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// https://geek.csdn.net/edu/8802d631b97a4a6af1f4d0bbf8527465?dp_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NDQ0MDg2MiwiZXhwIjoxNzA3MzcxOTM4LCJpYXQiOjE3MDY3NjcxMzgsInVzZXJuYW1lIjoid2VpeGluXzY4NjQ1NjQ1In0.RrTYEnMNYPC7AQdoij4SBb0kKEgHoyvF-bZOG2eGQvc&amp;spm=1055.2569.3001.10083)[]
		// args) {

		// 初始化数据
		BarSeries series = new BaseBarSeriesBuilder().withName("mySeries").build();
//		series.addBar(new BaseBar(Duration.ofMinutes(1), ZonedDateTime.now(), new BigDecimal("10"),
//				new BigDecimal("15"), new BigDecimal("8"), new BigDecimal("12"), new BigDecimal("100")));
//		series.addBar(new BaseBar(Duration.ofMinutes(1), ZonedDateTime.now(), new BigDecimal("12"),
//				new BigDecimal("17"), new BigDecimal("10"), new BigDecimal("14"), new BigDecimal("200")));
//		series.addBar(new BaseBar(Duration.ofMinutes(1), ZonedDateTime.now(), new BigDecimal("14"),
//				new BigDecimal("19"), new BigDecimal("12"), new BigDecimal("16"), new BigDecimal("300")));
//		series.addBar(new BaseBar(Duration.ofMinutes(1), ZonedDateTime.now(), new BigDecimal("16"),
//				new BigDecimal("21"), new BigDecimal("14"), new BigDecimal("18"), new BigDecimal("400")));

		Map<String, String> headers = new HashMap<String, String>();
//		Map<String, String> headers = new HashMap<String, String>();

		headers.put("sec-ch-ua", "\"Not A(Brand\";v=\"99\", \"Google Chrome\";v=\"121\", \"Chromium\";v=\"121\"");
		headers.put("sec-ch-ua-mobile", "?0");
		headers.put("sec-ch-ua-platform", "\"Windows\"");
		headers.put("Sec-Fetch-Dest", "document");
		headers.put("Sec-Fetch-Mode", "navigate");
		headers.put("Sec-Fetch-Site", "none");
		headers.put("sec-ch-ua-full-version", "\"121.0.6167.140\"");
		headers.put("sec-ch-ua-arch", "\"x86\"");
		headers.put("Host", "www.okx.com");
		headers.put("sec-ch-ua-platform-version", "\"15.0.0\"");
		headers.put("sec-ch-ua-model", "\"\"");
		headers.put("sec-ch-ua-bitness", "\"64\"");

		headers.put("sec-ch-ua-full-version-list",
				"\"Not A(Brand\";v=\"99.0.0.0\", \"Google Chrome\";v=\"121.0.6167.140\", \"Chromium\";v=\"121.0.6167.140\"");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
		headers.put("Sec-Fetch-User", "?1");

		headers.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
		headers.put("Accept-Language", "zh-CN,zh;q=0.9");
		headers.put("Accept-Encoding", "gzip, deflate, br, zstd");
		headers.put("Cache-Control", "max-age=0");
//		headers.put("Cookie", FundPriceUpdate2.ck);

//		String gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, headers);
////		System.out.println(gupiaoRes);
//		JSONObject jObject = new JSONObject(gupiaoRes);
//		JSONArray list = jObject.getJSONArray("data");
//		List<Candle> candles = new ArrayList<Candle>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		FileUtil.writeUtf8String("", "d:\\lianghua1.txt");
		JSONArray list = new JSONArray();

		String temptm = System.currentTimeMillis() + "";
//		int k=0;

		if (ja == null) {
			for (int k = 0; k < 1; k++) {

				String tempurl = url + "&after=" + temptm;
				System.out.println("url:" + tempurl);

				String gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(tempurl, headers);
//				System.out.println(gupiaoRes);
//			{"msg":"","gszzl":"0","vol":0.03,"code":"0","data":[{"last":"186.09","lastSz":"0.03","open24h":"178.37","askSz":"125.61","low24h":"177.51","askPx":"186.08","volCcy24h":"5891121.44","instType":"SWAP","instId":"SOL-USDT-SWAP","bidSz":"21.05","bidPx":"186.07","high24h":"187.38","sodUtc0":"183.11","vol24h":"5891121.44","sodUtc8":"181.52","ts":"1722084920604"}],"gsz":186.07}
//			

				if (gupiaoRes == null || gupiaoRes.indexOf("Just a moment") != -1) {
					// 从redis获取K线
					k = k - 1;
//				try {
//					Thread.sleep(1000l);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				continue;

					return null;

				}

				// {"message":"An unexpected error occurred"}
				JSONObject jObject = new JSONObject(gupiaoRes);
				JSONArray templist = jObject.getJSONArray("data");
				for (int m = 0; m < templist.size(); m++) {
					list.add(templist.get(m));
				}
				temptm = (String) templist.getJSONArray(templist.size() - 1).get(0);
//			list.addAll(templist);
//			System.out.println("size:" + templist.size());
//			System.out.println("allsize:" + list.size());

			}

		} else {
			System.out.println(ja);
			int size = ja.size();
			for (int kk = size - 1; kk >= 0; kk--) {
				JSONObject jObject = ja.getJSONObject(kk);
				JSONArray templist = jObject.getJSONArray("data");

				list.add(templist.get(0));
			}

//			temptm = (String) ja.getJSONObject(0).getJSONArray("data").get(0);

		}
		System.out.println("allsize:" + list.size());
//		System.out.println(list);
		JSONArray listSort = new JSONArray();

		int seq = 0;
		for (int i = list.size() - 1; i >= 0; i--) {

			JSONArray item = list.getJSONArray(i);
			JSONObject tempDataJsonObject = new JSONObject();
//			tempDataJsonObject.put("index", seq);
			tempDataJsonObject.put("data", item);
			listSort.add(tempDataJsonObject);

			String highP = (String) item.get(2);
			String lowP = (String) item.get(3);
			String closeP = (String) item.get(4);
			String tm = (String) item.get(0);
			String vol = (String) item.get(5);
			String openP = (String) item.get(1);
			String lianghua = sdf.format(new Date(Long.parseLong(tm))) + "\t" + openP + "\t" + highP + "\t" + lowP
					+ "\t" + closeP + "\t" + "" + "\t" + vol + "\n";
//			System.out.println(lianghua);
//			FileUtil.appendUtf8String(lianghua, "d:\\lianghua1.txt");

			long timestamp = Long.parseLong(tm); // 例如：UTC时间 2021-10-01T00:00:00Z

			// 使用Instant.ofEpochMilli将时间戳转换为Instant
			Instant instant = Instant.ofEpochMilli(timestamp);

			// 指定东八区（UTC+8），这里使用具体的时区名称
			ZoneId zoneId = ZoneId.of("Asia/Shanghai");

			// 使用ZonedDateTime.ofInstant将Instant和时区转换为ZonedDateTime
			ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);

			series.addBar(new BaseBar(Duration.ofHours(1), zonedDateTime, new BigDecimal(openP), new BigDecimal(highP),
					new BigDecimal(lowP), new BigDecimal(closeP), new BigDecimal(vol)));
			seq += 1;
		}

		// 创建指标
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		EMAIndicator ema12 = new EMAIndicator(closePrice, 12);
		EMAIndicator ema26 = new EMAIndicator(closePrice, 26);
		MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);

		// 使用上述两个EMA计算MACD
//        MACDIndicator macd = new MACDIndicator(ema12, ema26, 9); // 9是信号线的EMA周期  

		// 输出MACD值作为示例
//		for (int i = 0; i < series.getBarCount(); i++) {
//			Num macdValue = macd.getValue(i);
//			System.out.println("MACD at index " + i + ": " + macdValue);
//		}

		// 输出指标结果
		System.out.println("EMA 12: " + ema12.getValue(series.getEndIndex()));
		System.out.println("EMA 26: " + ema26.getValue(series.getEndIndex()));
		System.out.println("MACD: " + macd.getValue(series.getEndIndex()));
		// 手动计算DIF
		// 获取DIF和DEA
		Indicator<Num> dif = new AbstractIndicator<Num>(series) {
//		    @Override  
//		    protected Num calculate(int index) {  
//		        if (index < Math.max(ema12.getBarSeries().getBarCount(), ema26.getBarSeries().getBarCount()) - 1) {  
//		            return ema12.getValue(index).minus(ema26.getValue(index));  
//		        }  
//		        return null; // 或者你可以选择返回null或抛出异常，这取决于你的具体需求  
//		    }

			@Override
			public Num getValue(int index) {
				// TODO Auto-generated method stub
//				if (index < Math.max(ema12.getBarSeries().getBarCount(), ema26.getBarSeries().getBarCount()) - 1) {
				return ema12.getValue(index).minus(ema26.getValue(index));
			}
//				return Num; // 或者你可以选择返回null或抛出异常，这取决于你的具体需求
//			}

//			@Override
//			public int getUnstableBars() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
		};

		// 计算DEA（DIF的EMA）
		EMAIndicator dea = new EMAIndicator(dif, 9);
		Num previousHistogramValue = null; // 用于存储前一个柱状线的值
		if (previousHistogramValue == null) {
			previousHistogramValue = dif.getValue(0).minus(dea.getValue(0)).abs();
		}
		// 遍历时间序列，检查金叉和死叉
		int chajufangxing = 0;
		int buytag = 0;
		JSONArray jinchas = new JSONArray();
		JSONArray sichas = new JSONArray();
		int tempbuytag = 0;
		for (int i = 1; i < series.getBarCount(); i++) {
//			System.out.println(" 时间：" + series.getBar(i).getEndTime());
			Num currentHistogramValue = dif.getValue(i).minus(dea.getValue(i)).abs();

			if (dif.getValue(i).isGreaterThan(dea.getValue(i))
					&& dif.getValue(i - 1).isLessThanOrEqual(dea.getValue(i - 1))) {
				// 金叉
//				System.out.println("金叉发生在索引 " + i + " 价格：" + series.getBar(i).getClosePrice() + " 时间："
//						+ series.getBar(i).getEndTime() + " dif:" + dif.getValue(i) + " dea:" + dea.getValue(i)
//						+ " macd:" + macd.getValue(i));
				chajufangxing = 1;
				tempbuytag = 1;
				JSONObject jincha = listSort.getJSONObject(i);
				jincha.put("index", i);

				jinchas.add(jincha);

			} else if (dif.getValue(i).isLessThan(dea.getValue(i))
					&& dif.getValue(i - 1).isGreaterThanOrEqual(dea.getValue(i - 1))) {
				// 死叉
//				System.out.println("死叉发生在索引 " + i + " 价格：" + series.getBar(i).getClosePrice() + " 时间："
//						+ series.getBar(i).getEndTime() + " dif:" + dif.getValue(i) + " dea:" + dea.getValue(i)
//						+ " macd:" + macd.getValue(i));
				chajufangxing = 1;
				tempbuytag = -1;
				JSONObject sicha = listSort.getJSONObject(i);
				sicha.put("index", i);
				sichas.add(sicha);

			} else {

				if (previousHistogramValue.doubleValue() >= currentHistogramValue.doubleValue()) {
					chajufangxing = -1;
				} else {
					chajufangxing = 1;
				}
				// 更新previousHistogramValue为当前值，以便下一次比较
//				listSort.getJSONObject(i).put("buytag", 0);

//				System.out.println("柱状线高度：" + chajufangxing + "  值：" + currentHistogramValue.doubleValue() + " 价格："
//						+ series.getBar(i).getClosePrice() + " 时间：" + series.getBar(i).getEndTime());
			}
			previousHistogramValue = currentHistogramValue;
			listSort.getJSONObject(i).put("chajufangxiang", chajufangxing);
			listSort.getJSONObject(i).put("buytag", tempbuytag);

		}
		System.out.println("chajufangxiang:" + chajufangxing);
		int currentbuytag = 0;

		for (int i = series.getBarCount() - 4; i <= series.getBarCount() - 1; i++) {
//			System.out.println(" 时间：" + series.getBar(i).getEndTime());
			if (dif.getValue(i).isGreaterThan(dea.getValue(i))

					&& dif.getValue(i - 1).isLessThanOrEqual(dea.getValue(i - 1))) {
				// 金叉
				System.out.println("金叉发生在索引 " + i + " dif:" + dif.getValue(i) + " dea:" + dea.getValue(i) + " macd:"
						+ macd.getValue(i) + " 时间：" + series.getBar(i).getEndTime());
				buytag = 1;
				currentbuytag = 1;
			} else if (dif.getValue(i).isLessThan(dea.getValue(i))
					&& dif.getValue(i - 1).isGreaterThanOrEqual(dea.getValue(i - 1))) {
				// 死叉
				System.out.println("死叉发生在索引 " + i + " dif:" + dif.getValue(i) + " dea:" + dea.getValue(i) + " macd:"
						+ macd.getValue(i) + " 时间：" + series.getBar(i).getEndTime());
				buytag = -1;
				currentbuytag = -1;
			} else {
				currentbuytag = 0;
			}

		}
//		buytag = tempbuytag;
//		if (chajufangxing == -1)
//			buytag = 0;

		// 统计金叉后成功率
		Map<String, Integer> jinchaMap = new HashMap<String, Integer>();
		Map<String, Integer> sichaMap = new HashMap<String, Integer>();
		int jccount = jinchas.size();
		int sccount = sichas.size();
		for (int n = 0; n < jinchas.size(); n++) {

			int jcindex = jinchas.getJSONObject(n).getInt("index");
			JSONObject itemAll = jinchas.getJSONObject(n);
			JSONArray item = itemAll.getJSONArray("data");
			String highP = (String) item.get(2);
			String lowP = (String) item.get(3);
			String closeP = (String) item.get(4);
			String tm = (String) item.get(0);
			String vol = (String) item.get(5);
			String openP = (String) item.get(1);
			itemAll.put("open", closeP);
			itemAll.put("tm", sdf.format(new Date(Long.parseLong(tm))));
			System.out.println("金叉-open: 金叉收盘价" + itemAll.getStr("open") + " 时间："
					+ sdf.format(new Date(Long.parseLong(tm))) + " 成交量：" + vol + " index:" + jcindex);
			for (int j = 1; j <= 15; j++) {
				if ((jcindex + j) > listSort.size() - 1) {
					itemAll.put("signal_" + j, 0);

					break;
				}
				JSONObject itemAllAfter = listSort.getJSONObject(jcindex + j);
				Integer cjfx = itemAllAfter.getInt("chajufangxiang");
				JSONArray itemAfter = itemAllAfter.getJSONArray("data");
				String highPAfter = (String) itemAfter.get(2);
				String lowPAfter = (String) itemAfter.get(3);
				String closePAfter = (String) itemAfter.get(4);
				String tmAfter = (String) itemAfter.get(0);
				String volAfter = (String) itemAfter.get(5);
				String openPAfter = (String) itemAfter.get(1);

				BigDecimal zzl = new BigDecimal(closePAfter).subtract(new BigDecimal(closeP))
						.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));
				if (jinchaMap.containsKey("jincha_zzl_" + j))
					jinchaMap.put("jincha_zzl_" + j,
							new BigDecimal(jinchaMap.get("jincha_zzl_" + j) + "").add(zzl).intValue());
				else {
					jinchaMap.put("jincha_zzl_" + j, zzl.intValue());

				}

				if (new BigDecimal(closePAfter).compareTo(new BigDecimal(closeP)) > 0) {
					System.out.println("金叉-周期 " + n + "-" + j + " 现价 " + closePAfter + " 金叉收盘价：" + closeP + "  zdf:"
							+ zzl.divide(new BigDecimal("100")) + " 时间:" + sdf.format(new Date(Long.parseLong(tmAfter)))
							+ " 成交量：" + volAfter + " 【ok】");
					itemAll.put("signal_" + j, 1);
					if (jinchaMap.containsKey("jincha_" + j))
						jinchaMap.put("jincha_" + j, Integer.parseInt(jinchaMap.get("jincha_" + j) + "") + 1);
					else {
						jinchaMap.put("jincha_" + j, 1);

					}

				} else {
					System.out.println("金叉-周期 " + n + "-" + j + " 现价 " + closePAfter + " 金叉收盘价：" + closeP + "  zdf:"
							+ zzl.divide(new BigDecimal("100")) + " 时间:" + sdf.format(new Date(Long.parseLong(tmAfter)))
							+ " 成交量：" + volAfter + " 【fail】");
//					System.out.println("金叉不正常：" + sdf.format(new Date(Long.parseLong(tm))) + "--->" + j + "分钟后竟然下跌了");
				}
				if (cjfx == null)
					System.out.println("异常cfjx的索引：" + (jcindex + j));

				if (cjfx != null && cjfx == 1) {
					itemAll.put("signal_zz_" + j, 1);
					if (jinchaMap.containsKey("jincha_zz_" + j))
						jinchaMap.put("jincha_zz_" + j, Integer.parseInt(jinchaMap.get("jincha_zz_" + j) + "") + 1);
					else {
						jinchaMap.put("jincha_zz_" + j, 1);

					}

				}

			}
			int zhiyingtag = 0;
			int startbuytag = 0;
			BigDecimal lastclose = new BigDecimal(closeP);

			BigDecimal zhiyingBigDecimal = null;
			for (int j = 1; j <= 50; j++) {

				if ((jcindex + j) > listSort.size() - 1) {
					if (startbuytag == 0)
						itemAll.put("gc", 0);

					break;
				}

				JSONObject itemAllAfter = listSort.getJSONObject(jcindex + j);
				JSONArray itemAfter = itemAllAfter.getJSONArray("data");
				String highPAfter = (String) itemAfter.get(2);
				String lowPAfter = (String) itemAfter.get(3);
				String closePAfter = (String) itemAfter.get(4);
				String tmAfter = (String) itemAfter.get(0);
				String volAfter = (String) itemAfter.get(5);
				String openPAfter = (String) itemAfter.get(1);
				Integer chajufangxiangAfter = itemAllAfter.getInt("chajufangxiang");
				Integer buytagafter = itemAllAfter.getInt("buytag");

				if (buytagafter == null)
					buytagafter = 0;

				if (chajufangxiangAfter == null)
					chajufangxiangAfter = 0;
				if (buytagafter == -1)
					chajufangxiangAfter = -1;

				if (startbuytag == 0 && j == 50) {
//					System.out.println("配置文件丢失");
					itemAll.put("gc", 0);

					break;
				}
				if (buytagafter == -1 && startbuytag == 0) {
//					System.out.println("检测到死叉，废除这个金叉");
					itemAll.put("gc", 0);

					break;
				}
				BigDecimal shouyiBigDecimal = (new BigDecimal(closePAfter).subtract(new BigDecimal(closeP)))
						.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));
				if (startbuytag == 0) {

//					shouyiBigDecimal.compareTo(new BigDecimal("20")) > 0
//					new BigDecimal(closePAfter).compareTo(lastclose) > 0 && chajufangxiangAfter == 1
//							&& 
					SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMddHHmm");
					String historytag = code + "_historytag_long_" + sdfs.format(new Date(Long.parseLong(tmAfter)));
//					System.out.println("historytag_key:" + historytag);
					historytag = pro.getProperty(historytag);
					String buytagReal = code + "_buytag_long_" + sdfs.format(new Date(Long.parseLong(tmAfter)));
					buytagReal = pro.getProperty(buytagReal);
					String buypriceReal = code + "_buyprice_long_" + sdfs.format(new Date(Long.parseLong(tmAfter)));
					buypriceReal = pro.getProperty(buypriceReal);

					if ((buytagReal != null && buytagReal.equals("1") && historytag != null && historytag.equals("1")
							&& buypriceReal != null)) {// chajufangxiangAfter == 1
//						System.out.println("启动-涨跌幅：" + shouyiBigDecimal + " 柱子：" + chajufangxiangAfter + " 价格趋势："
//								+ (new BigDecimal(closePAfter).compareTo(lastclose) > 0) + " tm："
//								+ sdf.format(new Date(Long.parseLong(tmAfter))));
						startbuytag = 1;
						// 修改金叉的真实开盘价，收盘价，时间等。

						highP = (String) itemAfter.get(2);
						lowP = (String) itemAfter.get(3);
						closeP = (String) itemAfter.get(4);
						if (buypriceReal != null) {
							System.out.println("检测到配置文件价格：" + buypriceReal);
							closeP = buypriceReal;
						}
						tm = (String) itemAfter.get(0);
						vol = (String) itemAfter.get(5);
						openP = (String) itemAfter.get(1);
						itemAll.put("open", closeP);
						itemAll.put("tm", sdf.format(new Date(Long.parseLong(tm))));
						itemAll.put("startindex", j);
						startbuytag = 1;

					} else {
//						System.out.println("涨跌幅：" + shouyiBigDecimal + " 柱子：" + chajufangxiangAfter + " 价格趋势："
//								+ (new BigDecimal(closePAfter).compareTo(lastclose) > 0) + " tm："
//								+ sdf.format(new Date(Long.parseLong(tmAfter))));
					}
					lastclose = new BigDecimal(closePAfter);
					continue;
				}

				// 止盈2，止损1，倍数20
				BigDecimal zhisunBigDecimal = new BigDecimal(closeP)
						.multiply(new BigDecimal("1").subtract(new BigDecimal(zhisun).divide(new BigDecimal("100"))));
				zhiyingBigDecimal = new BigDecimal(closeP)
						.multiply(new BigDecimal("1").add(new BigDecimal(zhiying).divide(new BigDecimal("100"))));

//						.subtract(new BigDecimal("10"));
				BigDecimal shouyiBigDecimalMax = (new BigDecimal(highPAfter).subtract(new BigDecimal(closeP)))
						.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));// highPAfter

//				if (itemAll.containsKey("max"))
//					itemAll.put("max",
//							(shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(new BigDecimal("100")))
//									.compareTo(itemAll.getBigDecimal("max")) > 0
//											? shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(
//													new BigDecimal("100"))
//											: itemAll.getBigDecimal("max"));
//				else {
//					itemAll.put("max",
//							shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
//				}

//				
//						new BigDecimal(lowPAfter).compareTo(zhisunBigDecimal) >= 0&&&& new BigDecimal(highPAfter).compareTo(zhiyingBigDecimal) >= 0

				if (zhiyingBigDecimal != null && new BigDecimal(lowPAfter).compareTo(zhisunBigDecimal) >= 0
						&& new BigDecimal(highPAfter).compareTo(zhiyingBigDecimal) >= 0 && zhiyingtag == 1) {
					shouyiBigDecimal = (zhiyingBigDecimal.subtract(new BigDecimal(closeP)))
							.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));// highPAfter
//					System.out.println(
//							"止盈" + shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
					itemAll.put("result",
							"止盈" + shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
					itemAll.put("gc", 1);
					itemAll.put("close", closePAfter);// highPAfter

					if (jinchaMap.containsKey("gc"))
						jinchaMap.put("gc", Integer.parseInt(jinchaMap.get("gc") + "") + 1);
					else {
						jinchaMap.put("gc", 1);
//
					}
					itemAll.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")));
					if (jinchaMap.containsKey("gc_lirun"))
						jinchaMap.put("gc_lirun", Integer.parseInt(jinchaMap.get("gc_lirun") + "")
								+ shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
					else {
						jinchaMap.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
//
					}
					itemAll.put("tmafter", sdf.format(new Date(Long.parseLong(tmAfter))));
					itemAll.put("du", j);
					System.out.println("zhiyingtag:" + zhiyingtag + " 收益："
							+ shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")) + " max:"
							+ itemAll.getStr("max") + " index:" + itemAll.getBigDecimal("startindex") + " j:" + j
							+ " tm:" + sdf.format(new Date(Long.parseLong(tmAfter))));

					break;

				} else {

//					System.out.println("金叉不正常：" + sdf.format(new Date(Long.parseLong(tm))) + "--->" + j + "分钟后竟然下跌了");
//					itemAll.put("gc_lirun", shouyiBigDecimal);

					// lowPAfter

					if (new BigDecimal(lowPAfter).compareTo(zhisunBigDecimal) < 0) {
						// 止损了
						itemAll.put("close", zhisunBigDecimal);
						itemAll.put("gc", -1);

						shouyiBigDecimal = (zhisunBigDecimal.subtract(new BigDecimal(closeP)))
								.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));
						itemAll.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")));
						if (jinchaMap.containsKey("gc_lirun"))
							jinchaMap.put("gc_lirun", Integer.parseInt(jinchaMap.get("gc_lirun") + "")
									+ shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
						else {
							jinchaMap.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
							//
						}
						itemAll.put("tmafter", sdf.format(new Date(Long.parseLong(tmAfter))));
						itemAll.put("du", j);
//						System.out.println(
//								"止损" + shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
						itemAll.put("result",
								"止损" + shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
						System.out.println("zhiyingtag:" + zhiyingtag + " 收益："
								+ shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100"))
								+ " max:" + itemAll.getStr("max") + " index:" + itemAll.getBigDecimal("startindex")
								+ " j:" + j + " tm:" + sdf.format(new Date(Long.parseLong(tmAfter))));

						break;
					} else {
						// 正常范围波动
						System.out.println("zhiyingtag:" + zhiyingtag + " 收益："
								+ shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100"))
								+ " max:" + itemAll.getStr("max") + " index:" + itemAll.getBigDecimal("startindex")
								+ " j:" + j + " tm:" + sdf.format(new Date(Long.parseLong(tmAfter))));
						itemAll.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")));
						itemAll.put("du", j);
					}

				}
				if (j == 15) {
					itemAll.put("tmafter", sdf.format(new Date(Long.parseLong(tmAfter))));
					itemAll.put("du", j);
					itemAll.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")));
					itemAll.put("close", closePAfter);
					if (jinchaMap.containsKey("gc_lirun"))
						jinchaMap.put("gc_lirun", Integer.parseInt(jinchaMap.get("gc_lirun") + "")
								+ shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
					else {
						jinchaMap.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
//
					}
				}

				lastclose = new BigDecimal(closePAfter);
				if (chajufangxiangAfter == -1)
					zhiyingtag = 1;
				else {
					zhiyingtag = 0;
				}

				BigDecimal zhiyingBigDecimalBase = new BigDecimal(closeP)
						.multiply(new BigDecimal("1").add(new BigDecimal(zhiying).divide(new BigDecimal("100"))));

				if (new BigDecimal(closePAfter).compareTo(zhiyingBigDecimalBase) < 0) {
					itemAll.remove("max");
				} else {
					if (itemAll.containsKey("max"))
						itemAll.put("max",
								(shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(new BigDecimal("100")))
										.compareTo(itemAll.getBigDecimal("max")) > 0
												? shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(
														new BigDecimal("100"))
												: itemAll.getBigDecimal("max"));
					else {
						itemAll.put("max",
								shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
					}
				}

				if (chajufangxiangAfter == 1) {
//					itemAll.remove("max");
					zhiyingBigDecimal = null;

				} else if (chajufangxiangAfter == -1) {
					if (itemAll.containsKey("max") && itemAll.getBigDecimal("max").compareTo(new BigDecimal("0")) > 0) {
						BigDecimal biliBigDecimal = new BigDecimal("0.4")
								.multiply(itemAll.getBigDecimal("max").divide(new BigDecimal("100")));
						BigDecimal zhiyingBigDecimalNew = new BigDecimal(closeP)
								.multiply(new BigDecimal("1").subtract(biliBigDecimal));
						if (zhiyingBigDecimalNew.compareTo(zhiyingBigDecimalBase) > 0) {
							zhiyingBigDecimal = zhiyingBigDecimalNew;
						} else {
							zhiyingBigDecimal = zhiyingBigDecimalBase;
						}
					} else {
						zhiyingBigDecimal = zhiyingBigDecimalBase;
					}
				}

			}

		}
		// 统计金叉成功率
		BigDecimal jinchaSum = new BigDecimal("0");
		for (int j = 1; j <= 15; j++) {

			// 计算1分钟的有效金叉数：
			int cc = jinchas.size();
			for (int n = 0; n < jinchas.size(); n++) {
				JSONObject itemAll = jinchas.getJSONObject(n);
				if (itemAll.containsKey("signal_" + j) && itemAll.getInt("signal_" + j) == 0) {
					cc -= 1;
				}
			}
			// 统计成功数：
			int succ = 0;
			if (jinchaMap.containsKey("jincha_" + j))
				succ = Integer.parseInt(jinchaMap.get("jincha_" + j) + "");

			// 统计成功数：
			BigDecimal zzl = new BigDecimal("0");
			if (jinchaMap.containsKey("jincha_zzl_" + j))
				zzl = new BigDecimal(jinchaMap.get("jincha_zzl_" + j) + "").divide(new BigDecimal("100"), 2,
						RoundingMode.DOWN);

			int zz = 0;
			if (jinchaMap.containsKey("jincha_zz_" + j))
				zz = Integer.parseInt(jinchaMap.get("jincha_zz_" + j) + "");

			if (cc > 0) {
				jinchaSum = jinchaSum.add(new BigDecimal(succ).divide(new BigDecimal(cc + ""), 4, RoundingMode.DOWN));

				System.out.println("金叉" + "\t" + j + "\t成功率：" + succ + "/" + cc + "="
						+ (new BigDecimal(succ).divide(new BigDecimal(cc + ""), 4, RoundingMode.DOWN)) + "\t增强率：" + zz
						+ "/" + cc + "=" + (new BigDecimal(zz).divide(new BigDecimal(cc + ""), 4, RoundingMode.DOWN))
						+ "\t平均涨跌幅：" + zzl + "/" + cc + "="
						+ zzl.divide(new BigDecimal(cc + ""), 4, RoundingMode.DOWN));
			}

		}
		System.out.println("-------做多滚仓---------");
		// 统计滚仓成功率

		// 计算1分钟的有效滚仓数：
		int cc2 = jinchas.size();
//		int cc3 = jinchas.size();
		int qushi = 0;
		for (int n = 0; n < jinchas.size(); n++) {
			JSONObject itemAll = jinchas.getJSONObject(n);
			if (itemAll.containsKey("gc") && itemAll.getInt("gc") == 0) {
				cc2 -= 1;
			}
//			if (!itemAll.containsKey("gc_lirun")) {
//				cc3 -= 1;
//			}

			if (itemAll.containsKey("gc_lirun") && itemAll.get("gc_lirun") != null) {
				if (new BigDecimal(itemAll.get("gc_lirun") + "").divide(new BigDecimal("100"), 4, RoundingMode.DOWN)
						.compareTo(new BigDecimal("0")) > 0)
					qushi += 1;

				System.out.println(n + " 收益率："
						+ new BigDecimal(itemAll.get("gc_lirun") + "").divide(new BigDecimal("100"), 4,
								RoundingMode.DOWN)
						+ "  成功标记：" + itemAll.get("gc") + " 开盘价：" + itemAll.get("open") + " 收盘价：" + itemAll.get("close")
						+ " 时间：" + itemAll.get("tm") + "->" + itemAll.getStr("tmafter") + " 时长："
						+ (itemAll.getInt("du") - itemAll.getInt("startindex")) + " " + itemAll.getStr("result"));
//				if (itemAll.containsKey("max")) {
//					System.out.println(" 当前最大收益："
//							+ itemAll.getBigDecimal("max").divide(new BigDecimal("100"), 4, RoundingMode.DOWN));
//				} else {
//					System.out.println(" 当前最大收益：null");
//				}
			}

		}
		// 统计滚仓成功数：
		int succ2 = 0;
		if (jinchaMap.containsKey("gc"))
			succ2 = Integer.parseInt(jinchaMap.get("gc") + "");

		if (cc2 > 0) {
			System.out.println("滚仓" + "\t" + "" + "\t成功率：" + succ2 + "/" + cc2 + "="
					+ (new BigDecimal(succ2).divide(new BigDecimal(cc2 + ""), 4, RoundingMode.DOWN)));
			System.out.println("滚仓" + "\t" + "" + "\t匹配率：" + qushi + "/" + cc2 + "="
					+ (new BigDecimal(qushi).divide(new BigDecimal(cc2 + ""), 4, RoundingMode.DOWN)));
		}
		BigDecimal gcLirun = null;

		if (jinchaMap.containsKey("gc_lirun")) {
			gcLirun = new BigDecimal(jinchaMap.get("gc_lirun") + "").divide(new BigDecimal("100"), 4,
					RoundingMode.DOWN);
			// Integer.parseInt(jinchaMap.get("gc_lirun") + "");

			if (cc2 > 0)
				System.out.println("滚仓" + "\t" + "" + "\t总收益：" + gcLirun + "/" + cc2 + "="
						+ gcLirun.divide(new BigDecimal(1 + ""), 4, RoundingMode.DOWN));
		}

		System.out.println("==========================================================");

		// 统计死叉后成功率
		for (int n = 0; n < sichas.size(); n++) {
			int scindex = sichas.getJSONObject(n).getInt("index");
			JSONObject itemAll = sichas.getJSONObject(n);
			JSONArray item = itemAll.getJSONArray("data");
			String highP = (String) item.get(2);
			String lowP = (String) item.get(3);
			String closeP = (String) item.get(4);
			String tm = (String) item.get(0);
			String vol = (String) item.get(5);
			String openP = (String) item.get(1);

			itemAll.put("open", closeP);
			itemAll.put("tm", sdf.format(new Date(Long.parseLong(tm))));
			System.out.println("死叉-open:  死叉收盘价：" + itemAll.getStr("open") + " tm："
					+ sdf.format(new Date(Long.parseLong(tm))) + " 成交量：" + vol + " index:" + scindex);
			for (int j = 1; j <= 15; j++) {
				if ((scindex + j) > listSort.size() - 1) {
					itemAll.put("signal_" + j, 0);

					break;
				}
				JSONObject itemAllAfter = listSort.getJSONObject(scindex + j);
				Integer cjfx = itemAllAfter.getInt("chajufangxiang");
				JSONArray itemAfter = itemAllAfter.getJSONArray("data");
				String highPAfter = (String) itemAfter.get(2);
				String lowPAfter = (String) itemAfter.get(3);
				String closePAfter = (String) itemAfter.get(4);
				String tmAfter = (String) itemAfter.get(0);
				String volAfter = (String) itemAfter.get(5);
				String openPAfter = (String) itemAfter.get(1);

				BigDecimal zzl = new BigDecimal(closePAfter).subtract(new BigDecimal(closeP))
						.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));
				if (sichaMap.containsKey("sicha_zzl_" + j))
					sichaMap.put("sicha_zzl_" + j,
							new BigDecimal(sichaMap.get("sicha_zzl_" + j) + "").add(zzl).intValue());
				else {
					sichaMap.put("sicha_zzl_" + j, zzl.intValue());

				}

				if (new BigDecimal(closePAfter).compareTo(new BigDecimal(closeP)) < 0) {
					System.out.println("死叉-周期 " + n + "-" + j + " 现价 " + closePAfter + " 死叉收盘价：" + closeP + "  zdf:"
							+ zzl.divide(new BigDecimal("100")) + " 时间:" + sdf.format(new Date(Long.parseLong(tmAfter)))
							+ " 成交量：" + volAfter + " 【ok】");
					itemAll.put("signal_" + j, -1);
					if (sichaMap.containsKey("sicha_" + j))
						sichaMap.put("sicha_" + j, Integer.parseInt(sichaMap.get("sicha_" + j) + "") + 1);
					else {
						sichaMap.put("sicha_" + j, 1);

					}
				} else {
//					System.out.println("死叉不正常：" + sdf.format(new Date(Long.parseLong(tm))) + "--->" + j + "分钟后竟然上涨了");
					System.out.println("死叉-周期 " + n + "-" + j + " 现价 " + closePAfter + " 死叉开盘价：" + closeP + "  zdf:"
							+ zzl.divide(new BigDecimal("100")) + " 时间:" + sdf.format(new Date(Long.parseLong(tmAfter)))
							+ " 成交量：" + volAfter + " 【fail】");
				}
				if (cjfx == null)
					System.out.println("异常死叉 cfjx的索引：" + (scindex + j));

				if (cjfx != null && cjfx == 1) {
					itemAll.put("signal_zz_" + j, 1);
					if (sichaMap.containsKey("sicha_zz_" + j))
						sichaMap.put("sicha_zz_" + j, Integer.parseInt(sichaMap.get("sicha_zz_" + j) + "") + 1);
					else {
						sichaMap.put("sicha_zz_" + j, 1);

					}

				}

			}

			int zhiyingtag = 0;
			int startbuytag = 0;
			BigDecimal lastclose = new BigDecimal(closeP);

			BigDecimal zhiyingBigDecimal = null;
			for (int j = 1; j <= 50; j++) {
				if ((scindex + j) > listSort.size() - 1) {
					if (startbuytag == 0)
						itemAll.put("gc", 0);

					break;
				}
				JSONObject itemAllAfter = listSort.getJSONObject(scindex + j);
				JSONArray itemAfter = itemAllAfter.getJSONArray("data");
				String highPAfter = (String) itemAfter.get(2);
				String lowPAfter = (String) itemAfter.get(3);
				String closePAfter = (String) itemAfter.get(4);
				String tmAfter = (String) itemAfter.get(0);
				String volAfter = (String) itemAfter.get(5);
				String openPAfter = (String) itemAfter.get(1);
				Integer chajufangxiangAfter = itemAllAfter.getInt("chajufangxiang");
//				System.out.println("chajufangxiang-" + (scindex + j) + ":  " + chajufangxiangAfter);

//						.subtract(new BigDecimal("10"));

//				if (new BigDecimal(highPAfter).compareTo(zhisunBigDecimal) <= 0
//						&& new BigDecimal(lowPAfter).compareTo(zhiyingBigDecimal) <= 0) 
				if (chajufangxiangAfter == null)
					chajufangxiangAfter = 0;

				Integer buytagafter = itemAllAfter.getInt("buytag");
				if (buytagafter == null)
					buytagafter = 0;
				if (buytagafter == 1)
					chajufangxiangAfter = -1;

				if (startbuytag == 0 && j == 15) {
					itemAll.put("gc", 0);

					break;
				}
				if (buytagafter == 1 && startbuytag == 0) {
					itemAll.put("gc", 0);

					break;
				}
				BigDecimal shouyiBigDecimal = (new BigDecimal(closeP).subtract(new BigDecimal(closePAfter)))
						.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));
				if (startbuytag == 0) {
//					System.out.println("涨跌幅：" + shouyiBigDecimal);

					SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMddHHmm");
					String historytag = code + "_historytag_short_" + sdfs.format(new Date(Long.parseLong(tmAfter)));
//					System.out.println("historytag_key:" + historytag);
					historytag = pro.getProperty(historytag);
					String buytagReal = code + "_buytag_short_" + sdfs.format(new Date(Long.parseLong(tmAfter)));
					buytagReal = pro.getProperty(buytagReal);
					String buypriceReal = code + "_buyprice_short_" + sdfs.format(new Date(Long.parseLong(tmAfter)));
					buypriceReal = pro.getProperty(buypriceReal);
//					lastclose.compareTo(new BigDecimal(closePAfter)) > 0 && chajufangxiangAfter == 1
//							&& shouyiBigDecimal.compareTo(new BigDecimal("20")) > 0

					if ((historytag != null && historytag.equals("1") && buytagReal != null && buytagReal.equals("-1"))
							&& buypriceReal != null) {// || chajufangxiangAfter == 1
						startbuytag = 1;
						// 修改金叉的真实开盘价，收盘价，时间等。

						highP = (String) itemAfter.get(2);
						lowP = (String) itemAfter.get(3);
						closeP = (String) itemAfter.get(4);

						if (buypriceReal != null) {
							System.out.println("检测到配置文件价格：" + buypriceReal);
							closeP = buypriceReal;
						}

						tm = (String) itemAfter.get(0);
						vol = (String) itemAfter.get(5);
						openP = (String) itemAfter.get(1);
						itemAll.put("open", closeP);
						itemAll.put("tm", sdf.format(new Date(Long.parseLong(tm))));
						itemAll.put("startindex", j);
						startbuytag = 1;
//						System.out.println("启动-涨跌幅：" + shouyiBigDecimal + " 柱子：" + chajufangxiangAfter + "价格趋势："
//								+ (lastclose.compareTo(new BigDecimal(closePAfter)) > 0) + " tm："
//								+ sdf.format(new Date(Long.parseLong(tmAfter))));

					} else {
//						System.out.println("涨跌幅：" + shouyiBigDecimal + " 柱子：" + chajufangxiangAfter + "价格趋势："
//								+ (lastclose.compareTo(new BigDecimal(closePAfter)) > 0) + " tm："
//								+ sdf.format(new Date(Long.parseLong(tmAfter))));
					}
					lastclose = new BigDecimal(closePAfter);
					continue;
				}
				// 止盈2，止损1，倍数20
				BigDecimal zhisunBigDecimal = new BigDecimal(closeP).multiply(
						new BigDecimal("1").add(new BigDecimal(zhisunfanxiang).divide(new BigDecimal("100"))));

//				if (itemAll.containsKey("max"))
//					itemAll.put("max",
//							(shouyiBigDecimal.subtract(new BigDecimal("10")))
//									.compareTo(itemAll.getBigDecimal("max")) > 0
//											? shouyiBigDecimal.subtract(new BigDecimal("10"))
//											: itemAll.getBigDecimal("max"));
//				else {
//					itemAll.put("max", shouyiBigDecimal.subtract(new BigDecimal("10")));
//				}

				BigDecimal shouyiBigDecimalMax = (new BigDecimal(closeP).subtract(new BigDecimal(lowPAfter)))
						.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));// lowPAfter
				if (zhiyingBigDecimal != null && new BigDecimal(highPAfter).compareTo(zhisunBigDecimal) < 0
						&& new BigDecimal(lowPAfter).compareTo(zhiyingBigDecimal) < 0 && zhiyingtag == 1)

				{
					itemAll.put("gc", 1);
					itemAll.put("close", closePAfter);// lowPAfter
					shouyiBigDecimal = (new BigDecimal(closeP).subtract(zhiyingBigDecimal))
							.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));

					if (sichaMap.containsKey("gc"))
						sichaMap.put("gc", Integer.parseInt(sichaMap.get("gc") + "") + 1);
					else {
						sichaMap.put("gc", 1);
//
					}
					itemAll.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")));
					if (sichaMap.containsKey("gc_lirun"))
						sichaMap.put("gc_lirun", Integer.parseInt(sichaMap.get("gc_lirun") + "")
								+ (shouyiBigDecimal.subtract(new BigDecimal("10"))).intValue());
					else {
						sichaMap.put("gc_lirun", (shouyiBigDecimal.subtract(new BigDecimal("10"))).intValue());
//
					}
					itemAll.put("tmafter", sdf.format(new Date(Long.parseLong(tmAfter))));
					itemAll.put("du", j);
//					System.out.println(
//							"止盈" + shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
					itemAll.put("result",
							"止盈" + shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));

					System.out.println("zhiyingtag:" + zhiyingtag + " 收益："
							+ shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")) + " max:"
							+ itemAll.getStr("max") + " index:" + itemAll.getBigDecimal("startindex") + " j:" + j
							+ " tm:" + sdf.format(new Date(Long.parseLong(tmAfter))));

					break;

				} else {
//					System.out.println("金叉不正常：" + sdf.format(new Date(Long.parseLong(tm))) + "--->" + j + "分钟后竟然下跌了");
//					itemAll.put("gc_lirun", shouyiBigDecimal);

					if (new BigDecimal(highPAfter).compareTo(zhisunBigDecimal) > 0) {
						// 止损了
						itemAll.put("close", zhisunBigDecimal);
						itemAll.put("gc", -1);

						shouyiBigDecimal = (new BigDecimal(closeP).subtract(zhisunBigDecimal))
								.divide(new BigDecimal(closeP), 4, RoundingMode.DOWN).multiply(new BigDecimal("10000"));
						itemAll.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")));
						if (sichaMap.containsKey("gc_lirun"))
							sichaMap.put("gc_lirun", Integer.parseInt(sichaMap.get("gc_lirun") + "")
									+ shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
						else {
							sichaMap.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
							//
						}
						itemAll.put("tmafter", sdf.format(new Date(Long.parseLong(tmAfter))));
						itemAll.put("du", j);
//						System.out.println(
//								"止损" + shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
						itemAll.put("result",
								"止损" + shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
						System.out.println("zhiyingtag:" + zhiyingtag + " 收益："
								+ shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100"))
								+ " max:" + itemAll.getStr("max") + " index:" + itemAll.getBigDecimal("startindex")
								+ " j:" + j + " tm:" + sdf.format(new Date(Long.parseLong(tmAfter))));

						break;
					} else {
						System.out.println("zhiyingtag:" + zhiyingtag + " 收益："
								+ shouyiBigDecimal.subtract(new BigDecimal("10")).divide(new BigDecimal("100"))
								+ " max:" + itemAll.getStr("max") + " index:" + itemAll.getBigDecimal("startindex")
								+ " j:" + j + " tm:" + sdf.format(new Date(Long.parseLong(tmAfter))));
						// 正常范围波动
						itemAll.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")));
						itemAll.put("du", j);
					}

				}
				if (j == 15) {
					itemAll.put("du", j);
					itemAll.put("tmafter", sdf.format(new Date(Long.parseLong(tmAfter))));
					itemAll.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")));
					itemAll.put("close", closePAfter);
					if (sichaMap.containsKey("gc_lirun"))
						sichaMap.put("gc_lirun", Integer.parseInt(sichaMap.get("gc_lirun") + "")
								+ shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
					else {
						sichaMap.put("gc_lirun", shouyiBigDecimal.subtract(new BigDecimal("10")).intValue());
//
					}
				}
				lastclose = new BigDecimal(closePAfter);
				if (chajufangxiangAfter == -1)
					zhiyingtag = 1;
				else {
					zhiyingtag = 0;
				}

				BigDecimal zhiyingBigDecimalBase = new BigDecimal(closeP).multiply(
						new BigDecimal("1").subtract(new BigDecimal(zhiyingfanxiang).divide(new BigDecimal("100"))));

				if (new BigDecimal(closePAfter).compareTo(zhiyingBigDecimalBase) > 0) {
					itemAll.remove("max");
				} else {
					if (itemAll.containsKey("max"))
						itemAll.put("max",
								(shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(new BigDecimal("100")))
										.compareTo(itemAll.getBigDecimal("max")) > 0
												? shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(
														new BigDecimal("100"))
												: itemAll.getBigDecimal("max"));
					else {
						itemAll.put("max",
								shouyiBigDecimalMax.subtract(new BigDecimal("10")).divide(new BigDecimal("100")));
					}
				}

				if (chajufangxiangAfter == 1) {
//					itemAll.remove("max");
					zhiyingBigDecimal = null;

				} else if (chajufangxiangAfter == -1) {
					zhiyingBigDecimal = zhiyingBigDecimalBase;
					if (itemAll.containsKey("max") && itemAll.getBigDecimal("max").compareTo(new BigDecimal("0")) > 0) {
						BigDecimal biliBigDecimal = new BigDecimal("0.4")
								.multiply(itemAll.getBigDecimal("max").divide(new BigDecimal("100")));
						BigDecimal zhiyingBigDecimalNew = new BigDecimal(closeP)
								.multiply(new BigDecimal("1").subtract(biliBigDecimal));
						if (zhiyingBigDecimalNew.compareTo(zhiyingBigDecimalBase) < 0) {
							zhiyingBigDecimal = zhiyingBigDecimalNew;
						}

					}
				}

			}

		}

		// 统计死叉成功率
		BigDecimal sichaSum = new BigDecimal("0");
		for (int j = 1; j <= 15; j++) {

			// 计算1分钟的有效金叉数：
			int cc = sichas.size();

			for (int n = 0; n < sichas.size(); n++) {
				JSONObject itemAll = sichas.getJSONObject(n);
				if (itemAll.containsKey("signal_" + j) && itemAll.getInt("signal_" + j) == 0) {
					cc -= 1;
				}
			}
			// 统计成功数：
			int succ = 0;
			if (sichaMap.containsKey("sicha_" + j))
				succ = Integer.parseInt(sichaMap.get("sicha_" + j) + "");

			// 统计成功数：
			BigDecimal zzl = new BigDecimal("0");
			if (sichaMap.containsKey("sicha_zzl_" + j))
				zzl = new BigDecimal(sichaMap.get("sicha_zzl_" + j) + "").divide(new BigDecimal("100"), 2,
						RoundingMode.DOWN);

			// 统计成功数：
			int zz = 0;
			if (sichaMap.containsKey("sicha_zz_" + j))
				zz = Integer.parseInt(sichaMap.get("sicha_zz_" + j) + "");
			if (cc > 0) {

				sichaSum = sichaSum.add(new BigDecimal(succ).divide(new BigDecimal(cc + ""), 4, RoundingMode.DOWN));
				System.out.println("死叉" + "\t" + j + "\t成功率：" + succ + "/" + cc + "="
						+ (new BigDecimal(succ).divide(new BigDecimal(cc + ""), 4, RoundingMode.DOWN)) + "\t增强率：" + zz
						+ "/" + cc + "=" + (new BigDecimal(zz).divide(new BigDecimal(cc + ""), 4, RoundingMode.DOWN))
						+ "\t平均涨跌幅：" + zzl + "/" + cc + "="
						+ zzl.divide(new BigDecimal(cc + ""), 4, RoundingMode.DOWN));
			}

		}

		System.out.println("-------做空滚仓---------");
		// 统计做空滚仓成功率

		// 计算1分钟的有效做空滚仓数：
		cc2 = sichas.size();
		qushi = 0;
		for (int n = 0; n < sichas.size(); n++) {
			JSONObject itemAll = sichas.getJSONObject(n);
			if (itemAll.containsKey("gc") && itemAll.getInt("gc") == 0) {
				cc2 -= 1;
			}
//			if (!itemAll.containsKey("gc_lirun")) {
//				cc3 -= 1;
//			}
			if (itemAll.containsKey("gc_lirun") && itemAll.get("gc_lirun") != null) {
				if (new BigDecimal(itemAll.get("gc_lirun") + "").divide(new BigDecimal("100"), 4, RoundingMode.DOWN)
						.compareTo(new BigDecimal("0")) > 0)
					qushi += 1;

				System.out.println(n + " 做空滚仓收益率："
						+ new BigDecimal(itemAll.get("gc_lirun") + "").divide(new BigDecimal("100"), 4,
								RoundingMode.DOWN)
						+ "  成功标记：" + itemAll.get("gc") + " 开盘价：" + itemAll.get("open") + " 收盘价：" + itemAll.get("close")
						+ " 时间：" + itemAll.get("tm") + "->" + itemAll.getStr("tmafter") + " 时长："
						+ (itemAll.getInt("du") - itemAll.getInt("startindex")) + " " + itemAll.getStr("result"));
				if (itemAll.containsKey("max")) {
//					System.out.println(" 当前最大收益："
//							+ itemAll.getBigDecimal("max").divide(new BigDecimal("100"), 4, RoundingMode.DOWN));
				} else {
//					System.out.println(" 当前最大收益：null");
				}

			}
		}
		// 统计滚仓成功数：
		succ2 = 0;
		if (sichaMap.containsKey("gc"))
			succ2 = Integer.parseInt(sichaMap.get("gc") + "");

		if (cc2 > 0) {
			System.out.println("做空滚仓" + "\t" + "" + "\t成功率：" + succ2 + "/" + cc2 + "="
					+ (new BigDecimal(succ2).divide(new BigDecimal(cc2 + ""), 4, RoundingMode.DOWN)));
			System.out.println("做空滚仓" + "\t" + "" + "\t匹配率：" + qushi + "/" + cc2 + "="
					+ (new BigDecimal(qushi).divide(new BigDecimal(cc2 + ""), 4, RoundingMode.DOWN)));
		}

		gcLirun = null;

		if (sichaMap.containsKey("gc_lirun")) {
			gcLirun = new BigDecimal(sichaMap.get("gc_lirun") + "").divide(new BigDecimal("100"), 4, RoundingMode.DOWN);

			// Integer.parseInt(jinchaMap.get("gc_lirun") + "");
			if (cc2 > 0)
				System.out.println("做空滚仓" + "\t" + "" + "\t总收益：" + gcLirun + "/" + cc2 + "="
						+ gcLirun.divide(new BigDecimal(1 + ""), 4, RoundingMode.DOWN));
		}
		int cate = 0;
		if (jinchaSum.compareTo(sichaSum) <= 0) {
			cate = 1;
		}
		System.out.println("cate【0多1空】:" + cate);
		System.out.println("buytag【1金叉附近 0不是 -1死叉附近】:" + buytag);
		System.out.println("tempbuytag【1金叉周期 -1死叉周期】:" + tempbuytag);
		System.out.println("chajufangxiang【1增强 0减弱】:" + chajufangxing);
		System.out.println("currentbuytag【1现在金叉 0不是 -1死叉】:" + currentbuytag);
		if (cate == 0) {
			System.out.println("适合做多");
		} else {
			System.out.println("适合做空");
		}

		Map<String, Integer> reMap = new HashMap<String, Integer>();
		reMap.put("buytag", buytag);// 金叉附近。1金叉附近，0不是金叉附近，-1死叉附近

		reMap.put("tempbuytag", tempbuytag);// 最近一次是金叉还是死叉。非0情况下，修正buytagpperiod
		reMap.put("chajufangxiang", chajufangxing);// 增强标记。和temptag配套使用。
		reMap.put("cate", cate);
		reMap.put("currentbuytag", currentbuytag);

		return reMap;

//		System.out.println("Signal: " + signal.getValue(series.getEndIndex()));
//		System.out.println("Histogram: " + macd.getHistogramValue(series.getEndIndex()));

	}
}