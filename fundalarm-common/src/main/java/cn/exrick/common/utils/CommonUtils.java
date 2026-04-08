package cn.exrick.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public class CommonUtils {
	public static Map<String, Object> oppen_id(HttpServletRequest request) {
		String oppen_id = "";
		String code = "";
		String access_token = "";
		code = (String) request.getParameter("code");// 获取code值
		System.out.println("------------------------------------");
		System.out.println("code=" + code);
		System.out.println("------------------------------------");
		if (code != null) {

			String token_url = null;
			try {
				token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
						+ getProperties("APP_ID")
						+ "&secret="
						+ getProperties("APP_SECRET")
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 获取用户的openid
			JSONObject json = new JSONObject();
			// CommonUtil commonUtil=new CommonUtil();
			json = httpsRequest(token_url, "GET", null);
			if (json != null) {
				oppen_id = json.getString("openid");
				access_token = json.getString("access_token");
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("oppen_id", oppen_id);
		map.put("access_token", access_token);
		return map;
	}

	/**
	 * 
	 * @param request
	 * @param code
	 * @return 2018年9月1日 sunhj
	 */
	public static JSONObject getOpenidByCode(String code) {
		System.out.println("------------------------------------");
		System.out.println("code=" + code);
		System.out.println("------------------------------------");
		String openId = null;
		if (code != null) {

			String token_url = null;
			try {
				token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
						+ getProperties("APP_ID")
						+ "&secret="
						+ getProperties("APP_SECRET")
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 获取用户的openid
			JSONObject json = new JSONObject();

			// CommonUtil commonUtil=new CommonUtil();
			json = httpsRequest(token_url, "GET", null);
			System.out.println(json.toString());
			return json;
			/*
			 * if (json != null) { openId = json.getString("openid"); }
			 */
		}
		return null;

	}

	/**
	 * 
	 * @param request
	 * @param code
	 * @return 2018年9月1日 sunhj 通州公众号授权登录
	 */
	public static JSONObject getOpenidByCodeoftz(String code) {
		System.out.println("------------------------------------");
		System.out.println("code=" + code);
		System.out.println("------------------------------------");
		String openId = null;
		if (code != null) {

			String token_url = null;
			try {
				token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
						+ getProperties("APP_ID_TZ")
						+ "&secret="
						+ getProperties("APP_SECRET_TZ")
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 获取用户的openid
			JSONObject json = new JSONObject();

			// CommonUtil commonUtil=new CommonUtil();
			json = httpsRequest(token_url, "GET", null);
			System.out.println(json.toString());
			return json;
			/*
			 * if (json != null) { openId = json.getString("openid"); }
			 */
		}
		return null;

	}
	/**
	 * 
	 * @param request
	 * @param code
	 * @return 2018年9月1日  通过code获取accesstoken 支付宝授权
	 */

	public static JSONObject getwxuserinfo(String code) {
		System.out.println("------------------------------------");
		System.out.println("code=" + code);
		System.out.println("------------------------------------");
		String openId = null;
		if (code != null) {

			String token_url = null;
			try {
				token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
						+ getProperties("APP_ID")
						+ "&secret="
						+ getProperties("APP_SECRET")
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 获取用户的openid
			JSONObject json = new JSONObject();

			// CommonUtil commonUtil=new CommonUtil();
			json = httpsRequest(token_url, "GET", null);
			String openid = json.getString("openid");
			System.out.println(json.toString());
			String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
					+ json.getString("access_token") + "&openid="
					+ json.getString("openid") + "&lang=zh_CN";
			json = httpsRequest(url, "POST", null);
			json.put("openid", openid);
			// if (json != null) {
			// openId = json.getString("openid");
			// }
			// }
			return json;
		} else {
			return null;
		}
	}

	public static JSONObject getwxuserinfo2(String openid, String token) {

		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ token + "&openid=" + openid + "&lang=zh_CN";
		JSONObject json = httpsRequest(url, "POST", null);
		json.put("openid", openid);
		// if (json != null) {
		// openId = json.getString("openid");
		// }
		// }
		return json;

	}
	
	
	
	public static JSONObject getwxuserinfo3(String openid, String token) {

		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
				+ token + "&openid=" + openid + "&lang=zh_CN";
		JSONObject json = httpsRequest(url, "GET", null);
		json.put("openid", openid);
		// if (json != null) {
		// openId = json.getString("openid");
		// }
		// }
		return json;

	}
	
	
	
	
	
	

	/**
	 * 获取接口访问凭证
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 * @throws IOException
	 */

	public static String getToken() throws IOException {
		String token;
		// Long nowtime = new Date().getTime();
		// if (accessTokenService2 == null) {
		// if (ctx == null)
		// ctx = new ClassPathXmlApplicationContext(new String[] {
		// "classpath:applicationContext.xml" });
		// accessTokenService2 = (AccessTokenService)
		// ctx.getBean("accessTokenService");
		// }
		// List<AccessToken> token_list =
		// accessTokenService2.listById(accessToken);
		//
		// // log.info(token_list.size()+token_list.get(0).getAccess_token());
		// Long add_time = token_list.get(0).getAdd_time();

		// System.out.println("te:"+(add_time + (7200 * 1000)) +"/"+nowtime);
		// if (add_time + (7200 * 1000) <= nowtime) { //
		// 如果数据库保存得时间加上7200秒（2小时）<=当前时间，说明token已经过期，需重新获取，并更新到数据库

		// 发起GET请求获取凭证
		String token_url2 = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";// 全局accesstoken接口

		token_url2 = token_url2.replace("APPID", getProperties("APP_ID"))
				.replace("APPSECRET", getProperties("APP_SECRET"));
		JSONObject jsonObject = httpsRequest(token_url2, "GET", null);
		try {

			token = jsonObject.getString("access_token");
			// return Token;
			// accessTokenService2.update(map);
			// System.out.println(jsonObject.getString("access_token"));
		} catch (Exception e) {
			token = null;
			// 获取token失败
			// log.error("获取token失败 errcode:{} errmsg:{}",
			// jsonObject.getInt("errcode"),
			// jsonObject.getStr("errmsg"));
			System.out.println("-----------获取token失败");
		}

		// } else {
		// token.setExpiresIn(7200);
		// token.setAccessToken(token_list.get(0).getAccess_token());
		// }
		return token;
	}

	/*
	 * public static JSONObject snsapi_userinfo(String openid,String
	 * acces_token){ String url
	 * ="https://api.weixin.qq.com/sns/userinfo?access_token="
	 * +acces_token+"&openid="+openid+"&lang=zh_CN"; return Util.postJson(url,
	 * null); }
	 */

	public static String getProperties(String key) throws IOException {
		InputStream in = CommonUtils.class
				.getResourceAsStream("/config.properties");
		Properties properties = new Properties();
		properties.load(in);
		return properties.getProperty(key);
	}

	public static Properties getPropertiesByConfig() {
		InputStream in = CommonUtils.class
				.getResourceAsStream("mongodb.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 发送https请求
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpsRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);

			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			// BufferedReader bufferedReader = new
			// BufferedReader(inputStreamReader);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"));
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			jsonObject = new JSONObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static boolean juadeIpoOpentime(String openedtime) {
		if (openedtime != null && openedtime.length() > 0) {
			String[] times = openedtime.split("\r\n");
			String nowtime = DateTimeTool.getNow().split(" ")[1].substring(0,
					DateTimeTool.getNow().split(" ")[1].lastIndexOf(":"));
			int openedtag = 0;
			System.out.println(nowtime);
			System.out.println(times[0]);
			for (int i = 0; i < times.length; i++) {

				String item = times[i];
				String[] items = item.split("-");

				System.out.println(nowtime.compareTo(items[1]) <= 0);

				if (nowtime.compareTo(items[0]) >= 0
						&& nowtime.compareTo(items[1]) <= 0) {
					System.out.println("ok");

					openedtag += 1;
				}

			}
			if (openedtag == 0) {// 没到开盘时间
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}