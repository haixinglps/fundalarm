package cn.exrick.manager.service.util;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

public class CerebrasFilter {

	private String key;
	private String model;
	private String url = "https://api.cerebras.ai/v1/chat/completions";
	private Proxy proxy;

//	static {
//		System.setProperty("http.proxyHost", "127.0.0.1");
//		System.setProperty("http.proxyPort", "7890");
//		System.setProperty("https.proxyHost", "127.0.0.1");
//		System.setProperty("https.proxyPort", "7890");
//	}

	private String systemPrompt = "你是SM/字母圈直播录制存档专家。判断标题是否值得录制。\n\n" + "SM圈黑话：\n" + "- \"调/调教/调教室\" = 核心内容，必录\n"
			+ "- \"训/任务/规则/考核\" = 有互动机制，值得录\n" + "- \"主/奴/M/S/Dom/Sub\" = 角色扮演，看具体活动\n"
			+ "- \"惩罚/奖励/仪式/辱母/羞辱/地狱/顶级/天花板/圣水/黄金/炸/榨/榨精/炸精/榨金/骨灰/职业装/口水/口舌/四爱/4爱\" = 内容驱动，值得录\n"
			+ "- \"圈/入圈/试圈\" = 入门内容，可能水\n" + "- \"纯聊/陪伴/挂机/睡觉\" = 低价值，不录制\n"
			+ "- \"加V/回放更新/进来/私信/external\" = 引流，不录制\n\n" + "，尽量自行思考判断是否是优质直播间，不要只根据提示词。结果必须返回一个整数：1（值得录制）或 0（不值得录制）";

	public CerebrasFilter(String key) {
		this(key, null, "gpt-oss-120b");
	}

	public CerebrasFilter(String key, String proxyUrl) {
		this(key, proxyUrl, "gpt-oss-120b");
	}

	public CerebrasFilter(String key, String proxyUrl, String model) {
		this.key = key;
		this.model = model;

		if (proxyUrl != null && !proxyUrl.isEmpty()) {
			try {
				String cleanUrl = proxyUrl.replace("http://", "").replace("https://", "");
				String[] parts = cleanUrl.split(":");
				String host = parts[0];
				int port = Integer.parseInt(parts[1]);
				this.proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
				System.out.println("代理设置: " + host + ":" + port);
			} catch (Exception e) {
				System.err.println("代理解析失败: " + e.getMessage());
			}
		}
	}

	public int judge(String title) {
		HttpURLConnection conn = null;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
//			URL apiUrl = new URL(this.url);
//
//			if (this.proxy != null) {
//				conn = (HttpURLConnection) apiUrl.openConnection();
//			} else {
//				conn = (HttpURLConnection) apiUrl.openConnection();
//			}
//			
			HttpRequest post = HttpUtil.createPost(this.url);

			post.header("Authorization", "Bearer " + this.key);

			post.header("Content-Type", "application/json; charset=UTF-8");
			post.header("Accept", "application/json");

			// 构建请求体
			JSONObject requestBody = new JSONObject();
			requestBody.put("model", this.model);
			requestBody.put("temperature", 0.1);
			requestBody.put("max_tokens", 150);

			JSONArray messages = new JSONArray();

			JSONObject systemMsg = new JSONObject();
			systemMsg.put("role", "system");
			systemMsg.put("content", this.systemPrompt);
			messages.put(systemMsg);

			JSONObject userMsg = new JSONObject();
			userMsg.put("role", "user");
			userMsg.put("content", title);
			messages.put(userMsg);

			requestBody.put("messages", messages);

			String jsonBody = requestBody.toString();
			System.out.println("请求体: " + jsonBody);
			post.body(jsonBody);
			HttpResponse response = post.execute();

			// 发送
			// conn.setRequestProperty("Content-Length",
			// String.valueOf(jsonBody.getBytes(StandardCharsets.UTF_8).length));

//			try (OutputStream os = conn.getOutputStream()) {
//				os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
//				os.flush();
//			}
//
//			// 检查响应码
//			int responseCode = conn.getResponseCode();
//			System.out.println("响应码: " + responseCode);
//
//			InputStream is;
//			if (responseCode >= 200 && responseCode < 300) {
//				is = conn.getInputStream();
//			} else {
//				is = conn.getErrorStream();
//			}
//
//			// 读取响应
//			StringBuilder response = new StringBuilder();
//			try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
//				String line;
//				while ((line = reader.readLine()) != null) {
//					response.append(line);
//				}
//			}

			String responseStr = response.body();
			System.out.println("响应: " + responseStr);

			// 解析
			JSONObject jsonResponse = new JSONObject(responseStr);
			JSONArray choices = jsonResponse.getJSONArray("choices");
			String answer = choices.getJSONObject(0).getJSONObject("message").getString("content").trim();

			System.out.println("模型回答: " + answer);
			if (answer.contains("不值得录制"))
				return 0;
			if (answer.contains("不能提供有关") || answer.contains("无法提供有关"))
				return 1;
			return (answer.contains("1")) ? 1 : 0;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	// 测试
	public static void main(String[] args) {
		CerebrasFilter filter = new CerebrasFilter("csk-5ytctkm5w99wdcd9mvrdmxe225c3p2dvw9jencdk52tj5jmk",
				"http://127.0.0.1:7890");

		int result = filter.judge("深夜调教室，任务考核");
		System.out.println("结果: " + result);
	}
}