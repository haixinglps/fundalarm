package cn.exrick.manager.service.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

//import com.codingapi.sdk.okx.rest.client.SignOkxApi;
//import com.codingapi.sdk.okx.rest.properties.OkxConfigProperties;
//import com.codingapi.sdk.okx.rest.protocol.trade.Order;
//import com.codingapi.springboot.framework.rest.properties.HttpProxyProperties;

public class okx {
//	   @Autowired
//	    private MarketApi marketApi;

//	    @Test
//	    void trades() {
//	        Trades.Response response = marketApi.trades("BTC-USDT");
//	        assertTrue(response!=null);
//	        assertTrue(response.getData().size()>0);
//	        log.info("list:{}",response.getData());
//	    }
	public static void main(String[] args) {
//		 instId=bitname+"-USDT-SWAP",
//			        tdMode="isolated",
//			        clOrdId=random_str,
//			        side="buy",
//			        # posSide="long",
//
//			        ordType="market",
//			        # ordType="limit",
//			        # px="0.19579",
//
//			        sz="1"
//		Order order = new Order();
//		Order.Request request = new Order.Request();
//		request.setSide("sell");
//
//		HttpProxyProperties prox = new HttpProxyProperties();
//		prox.setProxyHost("127.0.0.1");
//		prox.setProxyPort(7890);
//		prox.setEnableProxy(true);
//		Type tt = java.net.Proxy.Type.HTTP;
//		prox.setProxyType(tt);
//
//		OkxConfigProperties conf = new OkxConfigProperties();
//		conf.setApikey("f2caee79-82e8-40e2-838c-d5531a223584");
//		conf.setSecretkey("C2F7A676D20563EB797E1D9B80BCDF85");
//		conf.setPassphrase("~89*0374~6512*aI");
//		SignOkxApi signOkxApi = new SignOkxApi(prox, conf);
//
//		String data = signOkxApi.postSign("/api/v5/trade/fills", request.toParameters());

		String apiHost = "https://www.okx.com";
		String keyString = "?instType=SWAP" + "&instId=" + "ETHFI-USDT-SWAP";
		String path = "/api/v5/account/positions" + keyString;// fills
		String method = "GET";
		String body = "";
		String apiSecret = "35DC54ED1F023E51F082CC6AF8985342";
		String apiKey = "dcd08b32-9450-4100-bc83-a989feadaed4";
		String passphrase = "~89*0374~6512*aI";

		Map<String, String> headers = new HashMap<String, String>();
		String url = apiHost + path;

//		String timestamp = (System.currentTimeMillis() / 1000) + "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		String timestamp = format.format(new Date());
		System.out.println("时间：" + timestamp);
		String sign = "";
		try {
			sign = SignatureGenerator.generateSignature(timestamp, method, path, body, apiSecret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		headers.put("OK-ACCESS-KEY", apiKey);
		headers.put("OK-ACCESS-SIGN", sign);
		headers.put("OK-ACCESS-TIMESTAMP", timestamp);
		headers.put("OK-ACCESS-PASSPHRASE", passphrase);

//		String transOrder = okxService.trade("/api/v5/trade/order" + keyString, "GET", "");

		String gupiaoRes = null;
		if (method.equals("GET"))
			gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, headers);
		else {
			gupiaoRes = cn.exrick.common.utils.HttpUtil.sendPost(url, headers);
		}
		if (gupiaoRes == null) {
			System.out.println("=========网络异常，url:" + url);

		} else {
			System.out.println(gupiaoRes);

		}

	}

	public static void mains(String[] args) {
		System.out.println(UUID.randomUUID().toString().replace("-", ""));
		//
		String url = "https://www.okx.com/api/v5/market/candles?instId=SOL-USDT-SWAP&bar=1m";// history-

		Map<String, String> headers = new HashMap<String, String>();
		String gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, headers);
//		System.out.println(gupiaoRes);
		JSONObject jObject = new JSONObject(gupiaoRes);
		JSONArray list = jObject.getJSONArray("data");
		List<Candle> candles = new ArrayList<Candle>();
		for (int i = list.size() - 1; i >= 0; i--) {

			JSONArray item = list.getJSONArray(i);

			String highP = (String) item.get(2);
			String lowP = (String) item.get(3);
			String closeP = (String) item.get(4);
			String tm = (String) item.get(0);
			String vol = (String) item.get(5);

			Candle candle = new Candle(Double.parseDouble(highP), Double.parseDouble(lowP), Double.parseDouble(closeP),
					Long.parseLong(tm));
			candle.setCjl(Double.parseDouble(vol));
			candles.add(candle);

		}
		getSar(candles);

	}

	public static List<Candle> getline(String url) {
		//
//		String url = "https://www.okx.com/api/v5/market/history-candles?instId=NOT-USDT-SWAP&bar=1m";

		Map<String, String> headers = new HashMap<String, String>();
		System.out.println("【OKX】请求URL: " + url);
		String gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, headers);
		System.out.println("【OKX】响应: " + (gupiaoRes == null ? "null" : gupiaoRes.substring(0, Math.min(200, gupiaoRes.length()))));
		if (gupiaoRes == null || gupiaoRes.isEmpty()) {
			System.out.println("【OKX】响应为空，返回null");
			return null;
		}
		JSONObject jObject = new JSONObject(gupiaoRes);
		JSONArray list = jObject.getJSONArray("data");
		if (list == null) {
			System.out.println("【OKX】data字段为null，code=" + jObject.getStr("code") + ", msg=" + jObject.getStr("msg"));
			return null;
		}
		List<Candle> candles = new ArrayList<Candle>();
		for (int i = list.size() - 1; i >= 0; i--) {

			JSONArray item = list.getJSONArray(i);

			String highP = (String) item.get(2);
			String lowP = (String) item.get(3);
			String closeP = (String) item.get(4);
			String tm = (String) item.get(0);
			String vol = (String) item.get(5);

			Candle candle = new Candle(Double.parseDouble(highP), Double.parseDouble(lowP), Double.parseDouble(closeP),
					Long.parseLong(tm));
			candle.setCjl(Double.parseDouble(vol));
			candles.add(candle);

		}
		return getSar(candles);

	}

	public static List<Candle> getSar(List<Candle> candles) {
		double INITIAL_ACCELERATION_FACTOR = 0.02;
		double MAX_ACCELERATION_FACTOR = 0.2;
		double ACCELERATION_FACTOR_INCREASE = 0.02;
		// 假设candles是包含Candle对象的列表，每个Candle包含high, low, close
		double sar = candles.get(0).low; // 初始SAR
		double af = INITIAL_ACCELERATION_FACTOR; // 初始加速因子
		boolean longPosition = true; // 假设初始是多头市场

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 1; i < candles.size(); i++) {
			Candle current = candles.get(i);
			double ep = (longPosition ? Double.MIN_VALUE : Double.MAX_VALUE);

			// 计算EP
			if (longPosition) {
				ep = Math.max(ep, current.high);
			} else {
				ep = Math.min(ep, current.low);
			}

			// 更新SAR和AF
			if (longPosition) {
				if (current.close < sar) {
					longPosition = false;
					sar = current.close + (af * (ep - sar));
				} else {
					sar = sar + af * (ep - sar);
					if (sar > current.high) {
						sar = current.high;
						af = INITIAL_ACCELERATION_FACTOR;
					}
				}
			} else {
				// 空头市场的逻辑与多头市场类似，但方向相反
				// ...（这里省略了空头市场的代码，因为它与多头市场非常相似）
				if (current.close > sar) {
					longPosition = true; // 反转市场位置
					sar = current.close - (af * (sar - ep)); // 使用反向的公式来更新SAR
				} else {
					sar = sar - af * (sar - ep); // 在空头市场中，SAR应该向下移动
					if (sar < current.low) {
						sar = current.low; // 确保SAR不会低于当前最低价
						af = INITIAL_ACCELERATION_FACTOR; // 重置加速因子
					}
				}
			}

			if (af < MAX_ACCELERATION_FACTOR) {
				af += ACCELERATION_FACTOR_INCREASE;
			}

			// 在这里可以保存或输出每个周期的SAR值

			System.out.println(sdf.format(new Date(current.tm)) + "	" + sar + "	" + current.getCjl() + "	"
					+ current.getClose()); // 假设Candle有getTimestamp()方法
			current.setSar(sar);
		}
		return candles;

		// 最终的sar变量将包含最新SAR值

	}

}
