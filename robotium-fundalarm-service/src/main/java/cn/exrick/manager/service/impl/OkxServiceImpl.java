package cn.exrick.manager.service.impl;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import cn.exrick.manager.service.OkxService;
import cn.exrick.manager.service.util.SignatureGenerator;
import cn.exrick.manager.service.util.getWanwuPageForApiTest;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

/**
 * @author Exrickx
 */
@com.alibaba.dubbo.config.annotation.Service
public class OkxServiceImpl implements OkxService {

	private static final Logger log = LoggerFactory.getLogger(OkxServiceImpl.class);

	@Value("${codingapi.okx.proxy.enable-proxy}")
	private String proxyEnable;
	@Value("${codingapi.okx.proxy.proxy-host}")
	private String proxyHost;
	@Value("${codingapi.okx.proxy.proxy-port}")
	private String port;
	@Value("${codingapi.okx.proxy.proxy-type}")
	private String proxyType;
	@Value("${codingapi.okx.config.apikey}")
	private String apiKey;
	@Value("${codingapi.okx.config.host}")
	private String apiHost;

	@Value("${codingapi.okx.config.secretkey}")
	private String apiSecret;

	@Value("${codingapi.okx.config.passphrase}")
	private String passphrase;

	/**
	 * 下单接口
	 * <a href="https://www.okx.com/docs-v5/zh/#rest-api-trade-place-order">api</a>
	 * 
	 * @param request {@link Order.Request}
	 * @return {@link Order.Response}
	 */
//	public Order.Response order(Order.Request request) {
//		String data = signOkxApi.postSign("/api/v5/trade/order", request.toParameters());
//		log.debug("response:{}", data);
//		return JSONObject.parseObject(data, Order.Response.class);
//	}

	@Override
	public String trade(String path, String method, String body) {
		Map<String, String> headers = new HashMap<String, String>();
		// startDate: 2023-10-22
		// url = "https://price.btcfans.com/zh-cn/coin/" + fund.getCode();
		String url = apiHost + path;

//		String timestamp = System.currentTimeMillis() + "";
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
			return null;
		}

		System.out.println("key:" + apiKey);
		headers.put("OK-ACCESS-KEY", apiKey);
		headers.put("OK-ACCESS-SIGN", sign);
		headers.put("OK-ACCESS-TIMESTAMP", timestamp);
		headers.put("OK-ACCESS-PASSPHRASE", passphrase);
		String gupiaoRes = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dt = sdf.format(new Date());
		if (method.equals("GET"))

		{
//			gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, headers);

			Type type = Type.HTTP;
			SocketAddress sa = new InetSocketAddress("127.0.0.1", 7890);
			Proxy proxy = new Proxy(type, sa);
			HttpRequest req = HttpUtil.createGet(url);// .setProxy(proxy);
//			okhttp
			req.addHeaders(headers);
//			req.body(body);
			try {
				gupiaoRes = req.execute().body();
			} catch (Exception e) {

				try {
					FileUtil.appendUtf8String(dt + "网络异常：" + body + "\t" + e.getMessage() + "\n----\n",
							"d:\\netError.txt");
				} catch (Exception e2) {
					e2.printStackTrace();
					log.info("写入日志异常");
				}

				e.printStackTrace();
				// TODO: handle exception
			}

		}

		else {
			Type type = Type.HTTP;
			SocketAddress sa = new InetSocketAddress("127.0.0.1", 7890);
			Proxy proxy = new Proxy(type, sa);
			HttpRequest req = HttpUtil.createPost(url);// .setProxy(proxy);
			req.addHeaders(headers);
			req.body(body);
			try {
				gupiaoRes = req.execute().body();
			} catch (Exception e) {
				try {
					FileUtil.appendUtf8String(dt + "\t网络异常：" + body + "\t" + e.getMessage() + "\n----\n",
							"d:\\netError.txt");
				} catch (Exception e2) {
					e2.printStackTrace();
					log.info("写入日志异常");
				}

				e.printStackTrace();
				// TODO: handle exception
			}

		}
		if (gupiaoRes == null) {
			System.out.println("=========网络异常，url:" + url);
			return null;
		} else {

			return gupiaoRes;
		}

	}

	@Async
	@Override
	public void connectWanwuConnect() {

		getWanwuPageForApiTest.go();
	}

}
