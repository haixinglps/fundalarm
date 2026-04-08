package cn.exrick.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Exrickx
 */
public class HttpUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	// static HttpHost proxy = new HttpHost("127.0.0.1", 7890);  // 禁用代理
	private static HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			// TODO Auto-generated method stub
			return false;
		}

	};

	private static final CloseableHttpClient httpclient = HttpClients.custom().setRetryHandler(myRetryHandler)
			// .setProxy(proxy)  // 禁用代理，直连OKX API
			// .setDefaultCredentialsProvider(credentialsProvider)
			.build();

	private static final CloseableHttpClient httpclient2 = HttpClients.createDefault();

	// HttpClients.createDefault();
	private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36";

	/**
	 * 发送HttpGet请求
	 * 
	 * @param url 请求地址
	 * @return 返回字符串
	 */
	public static String sendGet(String url) {
		String result = null;
		CloseableHttpResponse response = null;

		try {
			HttpGet httpGet = new HttpGet(url);
			// 设置请求配置，包括连接超时和socket超时
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2000) // 连接超时时间5秒
					.setSocketTimeout(2000) // 数据读取超时时间10秒
					.build();
			httpGet.setConfig(requestConfig);

			httpGet.setHeader("User-Agent", userAgent);
			httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			log.error("处理失败，" + e);
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * 发送HttpPost请求，参数为map
	 * 
	 * @param url 请求地址
	 * @param map 请求参数
	 * @return 返回字符串
	 */
	public static String sendPost(String url, Map<String, String> map) {
		// 设置参数
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		// 编码
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		// 取得HttpPost对象
		HttpPost httpPost = new HttpPost(url);
		// 防止被当成攻击添加的
		httpPost.setHeader("User-Agent", userAgent);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		// 参数放入Entity
		httpPost.setEntity(formEntity);
		CloseableHttpResponse response = null;
		String result = null;
		try {
			// 执行post请求
			response = httpclient.execute(httpPost);
			// 得到entity
			HttpEntity entity = response.getEntity();
			// 得到字符串
			result = EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
		return result;
	}

	public static String sendJson(String url, String reqInfo) {

		// 定义发送数据
//    	JSONObject param = new JSONObject();
//    	param.put("username", "zhangshan");
//    	param.put("age", "18");
		// 定义接收数据
		String result = null;

//    	String url = "http://www.baidu.com";
		HttpPost httpPost = new HttpPost(url);
//    	CloseableHttpClient client = HttpClients.createDefault();
		// 请求参数转JOSN字符串
		StringEntity entity = new StringEntity(reqInfo, "UTF-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		try {
			HttpResponse response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				// 得到entity
				HttpEntity entityRes = response.getEntity();
				// 得到字符串
				result = EntityUtils.toString(entityRes);
//    			result = JSONOb.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			} else {
				System.out.println(
						"response.getStatusLine().getStatusCode():" + response.getStatusLine().getStatusCode());
			}

		} catch (Exception e) {
			System.out.println("发生异常：" + e.getMessage());
			e.printStackTrace();
//			result.put("error", "连接错误！");
		}
		// 关闭连接
//    	try {
//    		client.close();
//    	} catch (Exception e) {
//    		System.out.println(e.getMessage());
//    	}

		return result;

	}

	public static String sendGetWithHeader(String url, Map<String, String> map) {
		String result = null;
		CloseableHttpResponse response = null;
		// 设置请求配置，包括连接超时和socket超时
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) // 连接超时时间5秒
				.setSocketTimeout(5000) // 数据读取超时时间10秒
				.build();
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("User-Agent", userAgent);
			httpGet.setConfig(requestConfig);
			// 设置参数
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				httpGet.setHeader(entry.getKey(), entry.getValue());
			}

			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			log.error("处理失败，" + e);
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
		return result;
	}
}
