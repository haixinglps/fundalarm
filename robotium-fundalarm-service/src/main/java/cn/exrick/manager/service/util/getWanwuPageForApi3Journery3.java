package cn.exrick.manager.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class getWanwuPageForApi3Journery3 {

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();

		int type = 3;
		int tag = 0;// 1只提取有效行程 0无效也提取
		// 123主播：240485,117853,150192,170351,163453,379212,108281,287463,301869,288449,313834,372044,106423,159156,150087,429103,170573,107001,359168,133964,223351,117638,288200,251452,402216,441597,120352,109884,106546,129792,194348,150357,191200,266230,107047,365788,372016,155285,367323,401572,399844,404718,125212,164850,116571,128296,156996,110963,140316,170603,108564,192630,264457,272388,289358,115104,293452,295503,141015,324891,303664,315216,114459,163724,370738,213338,115701,327500,107296,123753,153985,109439,318184,262312,371777,191595,258359,136338,253808,168037,107422,191798,124839,319183,225696,107439,173090,205058,390760,112639,111257,219040,174770,113220,348757,252714,111431,254809,435582,435506,435402,135280,115697,212826,323373,428174,209291,161968,202960,186923,192066,293077,171766,186208,331281,385690,131325,428148,155129,161152,281209,107428,107631,150219,133671,416846,273587
//215415,218865,404603,208770,475043,467027,219005,472150,215991,287046,318244,211567,272820,258278,374730,445538,445533,219129,453040,459969,443090,419849,215253,332293,373069,219041,249751,207663,211896,457669,409242,374220,351663,377928,
		String vids = "323156,150219,120352,352568,300051,240485,140668,365788,171766,364275,106423,414937,173090,133964,111037,132791,428174,115701,391238,372016,110081,133671,399394,316997,138250,323373,176962,389863,123486,403520,259750,313834,136343,107422,179073,247294,191425,128296,156996,150087,110963,140316,170603,192630,264457,289358";//
		// 142881,142450,142150,141429,128311,127576,64514
		// 143266,139099,136129,13061,29768,29696,28890,46491,46488,59391,131071,130245,143209,143174,145457,145390,144716
		// 146240,133640,68486,145233,145231,145229,144544

		if (type == 1) {
			args = new String[] { "" };
		}
		String url = "http://127.0.0.1:54545/appapi/?service=Journey.getUserJourney&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&dev_id=c3bb248a1abcecb0218cf5c6d3b6f812&platform=2&to_uid=219040";
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();
		// for (int k = page; k <= page; k++) {// 1910
		StringBuffer m3u8Result = new StringBuffer();
		try {
			// String pU = url.replace("mypage", k + "");
			// System.out.println(pU);

//				String res = HttpUtil.get(pU);

			// 提取res中的链接
//				String docJson = Jsoup.connect(pU).ignoreContentType(true).execute().body();
//				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
//				JSONArray links = object.getJSONObject("data").getJSONArray("info");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
			String[] links = vids.split(",");
			if (links.length == 0)
				return;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (int j = 0; j < links.length; j++) {

//					cn.hutool.json.JSONObject link = links[j];
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

				String articleUrlString = links[j];
				// attr("href").replace("/cn/movie/", "");
				// articleUrlString = // 365855
				articleUrlString = "http://testlive.yueji.pro/dynamic/parade/parade?current=1&userId="
						+ articleUrlString + "&status=1&size=20"
						+ "&sign=1753496492-77a2d919cf8848cf9800cfc6bc952c1e-0-b883642d241fc5e27d9a18da423591f1"
						+ "&uid=228316&systemModel=Redmi%20Note%207%20Pro&appType=1&appVer=3.8.4"
						+ "&phoneBrand=xiaomi&version=3.8.4&deviceId=3251ab06-3320-4e70-b6a3-f796e4e7223a"
						+ "&systemVersion=10&versionCode=20250528";
				// "http://127.0.0.1:54545/appapi/?service=Journey.getUserJourney&uid=267412&token=9933096e2d731a5807210e0212a3f838&version=139&dev_id=c3bb248a1abcecb0218cf5c6d3b6f812&platform=2&to_uid="
				// + articleUrlString;
				url = articleUrlString;

//				Response articleRes = Jsoup.connect(articleUrlString)
//						.header("Content-Type", "application/json; charset=utf-8")
////							.header("Cookie",
////									"hls=s%3Aj9h_seF6e_ES1cK24p8h1WZUGx5Ny-Ba.Ds7igDStVbbGpFkVG0YYnMCJokUn6Lp3k49tqdjR9pE")
//						.header("User-Agent",
//								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
//						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
//						.execute();
				// System.out.println(url);
				String sourceString = null;
				try {
					sourceString = getRes(url);
				} catch (Exception e2) {
					System.out.println("异常：" + links[j]);
					continue;
					// TODO: handle exception
				}

				// articleRes.body();
				org.json.JSONArray arr = new JSONObject(sourceString).getJSONObject("data").getJSONArray("records");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);
				if (arr.length() == 0) {
					System.out.println("无数据：" + links[j]);
					continue;
				}
				String name = arr.getJSONObject(0).getString("userNiceName");
				System.out.println("---------" + name + "[" + links[j] + "]" + "-----------");
				for (int i = 0; i < arr.length(); i++) {

//				

					// 提取URL
					JSONObject jo = arr.getJSONObject(i);
					String endt = jo.getString("paradeEndTime");
					String todayT = sdf.format(new Date());
					if (todayT.compareTo(endt) > 0 && tag == 1)
						continue;

					System.out.println("主题：" + jo.getString("title"));
					System.out.println("活动日期：" + jo.getString("paradeBeginTime") + "-" + jo.getString("paradeEndTime"));
					System.out.println("发布日期：" + jo.getString("createTime"));
					String thumbsArr = jo.getString("thumb");
					String[] pics = thumbsArr.split(",");
					System.out.println(thumbsArr);
					for (int k = 0; k < pics.length; k++) {
						System.out.println(pics[k]);
					}
					System.out.println("==========");

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("-----------------url下载异常：" + url);
			// TODO: handle exception
		}

		// FileUtil.writeUtf8String(m3u8Result.toString(),
		// "d:/m3u8ListTaoluzhuboapi/self_" + type + "_" + 0 + "_" +
		// System.currentTimeMillis() + ".txt");

		// getWanwuM3u8ForApi.geneBat(args);

	}

	public static String getRes(String url) throws Exception {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000)
				.setConnectionRequestTimeout(3000).build();
		// HttpClients.createDefault();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

		HttpGet request = new HttpGet(url);

//		request.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		request.setHeader("User-Agent", "okhttp-okgo/jeasonlzy");
//		request.setHeader("Blade-Auth", "bearer");
//		request.setHeader("token", "aiya_39c0c548-a88b-426c-b55b-36ac72c492dbkh");
//		request.setHeader("appVersion",
//				"{\"uid\":\"228316\",\"systemModel\":\"Redmi 5 Plus\",\"appType\":\"1\",\"appVer\":\"3.9.1\",\"phoneBrand\":\"xiaomi\",\"version\":\"3.9.1\",\"deviceId\":\"186d155c-1555-4559-86de-f8480f9081f7\",\"systemVersion\":\"8.1.0\",\"versionCode\":\"20250715\"}");
//		request.setHeader("Connection", "keep-alive");
//		request.setHeader("referer", "http://testlive.yueji.pro");
//		request.setHeader("Host", "testlive.yueji.pro");// testlive.yueji.pro
//		request.setHeader("Accept-Encoding", "gzip");
//		request.setHeader("Cookie", "tgw_l7_route=11ffadb24678a5dfe7d7767d8c8c0053");
//		
//		
//		
//		
//
////		request.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
////		request.setHeader("User-Agent", "okhttp/4.2.2");
////		request.setHeader("Blade-Auth", "bearer");
////		request.setHeader("token", "aiya_ded3e1dc-9c39-4d44-b6ff-173a19038655fr");
//		request.setHeader("appVersion",
//				"{\"uid\":\"228316\",\"systemModel\":\"Redmi 5 Plus\",\"appType\":\"1\",\"appVer\":\"3.9.1\",\"phoneBrand\":\"xiaomi\",\"version\":\"3.9.1\",\"deviceId\":\"186d155c-1555-4559-86de-f8480f9081f7\",\"systemVersion\":\"8.1.0\",\"versionCode\":\"20250715\"}");
////		request.setHeader("Connection", "keep-alive");
////		request.setHeader("referer", "http://testlive.yueji.pro");
////		request.setHeader("Host", "testlive.yueji.pro");
////		request.setHeader("Accept-Encoding", "gzip");
//		// request.setHeader("Cookie", "tgw_l7_route=11ffadb24678a5dfe7d7767d8c8c0053");

		// 配置请求头
		request.setHeader("token", "aiya_aade486f-3dd9-4e52-b7a0-2e0edb1610c4n7");
		// 注意：appVersion通常不作为HTTP头传递，而是作为请求体或查询参数（本例中未作为查询参数，故此处不设置）
		// 若确实需要将其作为头信息发送，请确保服务器能够正确解析
		// request.setHeader("appVersion", "{\"uid\":\"228316\",...}"); //
		// 省略了完整的JSON字符串以节省空间
		request.setHeader("Content-Type", "application/json; charset=UTF-8");
		// Content-Length头通常由HttpClient根据请求体内容自动设置，无需手动指定
		request.setHeader("Host", "testlive.yueji.pro");
		// Connection: Keep-Alive通常由HttpClient管理，但在需要时也可明确设置
		request.setHeader("Connection", "Keep-Alive");
		request.setHeader("Accept-Encoding", "gzip");
		request.setHeader("versionName", "3.9.1");
		request.setHeader("appVersion",
				"{\"uid\":\"435407\",\"systemModel\":\"Redmi 5 Plus\",\"appType\":\"1\",\"appVer\":\"3.9.1\",\"phoneBrand\":\"xiaomi\",\"version\":\"3.9.1\",\"deviceId\":\"186d155c-1555-4559-86de-f8480f9081f7\",\"systemVersion\":\"8.1.0\",\"versionCode\":\"20250715\"}"
						+ "");

		request.setHeader("versionCode", "20250715");
		request.setHeader("clientType", "Android");
		request.setHeader("referer", "http://testlive.yueji.pro");

		request.setHeader("User-Agent", "TaoLuApp (Android 8.1.0)" + "");

		try (CloseableHttpResponse response = client.execute(request)) {
			// System.out.println("Status: " + response.getStatusLine());
			String reString = EntityUtils.toString(response.getEntity());
			// System.out.println(reString);
			return reString;
		}
	}

//	http://testlive.yueji.pro/dynamic/dynamic/dynamic?status=1&userId=365855&current=1&size=20&city=&sign=1749619027-6443e2a6ecc34c46a887b3ead56d62e9-0-f08192267371c8de411d51fb8632e93e&uid=228316&systemModel=Redmi%20Note%207%20Pro&appType=1&appVer=3.8.4&phoneBrand=xiaomi&version=3.8.4&deviceId=3251ab06-3320-4e70-b6a3-f796e4e7223a&systemVersion=10&versionCode=20250528
	public static String postRes(String url, String keyword) throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost request = new HttpPost(url);

//		request.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		request.setHeader("User-Agent", "okhttp/4.2.2");
//		request.setHeader("Blade-Auth", "bearer");
//		request.setHeader("token", "aiya_ded3e1dc-9c39-4d44-b6ff-173a19038655fr");
		request.setHeader("appVersion",
				"{\"uid\":\"228316\",\"systemModel\":\"Redmi Note 7 Pro\",\"appType\":\"1\",\"appVer\":\"3.9.1\",\"phoneBrand\":\"xiaomi\",\"version\":\"3.9.1\",\"deviceId\":\"d81949c4064b9fa4\",\"systemVersion\":\"10\",\"versionCode\":\"20250715\"}");
//		request.setHeader("Connection", "keep-alive");
//		request.setHeader("referer", "http://testlive.yueji.pro");
//		request.setHeader("Host", "testlive.yueji.pro");
//		request.setHeader("Accept-Encoding", "gzip");
		// request.setHeader("Cookie", "tgw_l7_route=11ffadb24678a5dfe7d7767d8c8c0053");

		// 配置请求头
		request.setHeader("token", "aiya_9816a632-8f93-4743-b0a6-75b559a893993o");
		// 注意：appVersion通常不作为HTTP头传递，而是作为请求体或查询参数（本例中未作为查询参数，故此处不设置）
		// 若确实需要将其作为头信息发送，请确保服务器能够正确解析
		// request.setHeader("appVersion", "{\"uid\":\"228316\",...}"); //
		// 省略了完整的JSON字符串以节省空间
		request.setHeader("Content-Type", "application/json; charset=UTF-8");
		// Content-Length头通常由HttpClient根据请求体内容自动设置，无需手动指定
		request.setHeader("Host", "testlive.yueji.pro");
		// Connection: Keep-Alive通常由HttpClient管理，但在需要时也可明确设置
//		request.setHeader("Connection", "Keep-Alive");
//		request.setHeader("Accept-Encoding", "gzip");
		request.setHeader("versionName", "3.9.1");

		request.setHeader("versionCode", "20250715");
		request.setHeader("clientType", "Android");

		request.setHeader("User-Agent", "TaoLuApp (Android 10)" + "" + "");

		String jsonBody = "{\"videoTitle\":\"" + keyword + "\"}";
//		String jsonBody = "{\"videoTitle\":\"12\"}";
		StringEntity entity = new StringEntity(jsonBody, "UTF-8");
		request.setEntity(entity);
		try (CloseableHttpResponse response = client.execute(request)) {
			// System.out.println("Status: " + response.getStatusLine());
			String reString = EntityUtils.toString(response.getEntity());
			// System.out.println(reString);
			return reString;
		}
	}

	public static String postRes2(String url, String uid) throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost request = new HttpPost(url);

//		request.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		request.setHeader("User-Agent", "okhttp/4.2.2");
//		request.setHeader("Blade-Auth", "bearer");
//		request.setHeader("token", "aiya_ded3e1dc-9c39-4d44-b6ff-173a19038655fr");

//		request.setHeader("Connection", "keep-alive");
//		request.setHeader("referer", "http://testlive.yueji.pro");
//		request.setHeader("Host", "testlive.yueji.pro");
//		request.setHeader("Accept-Encoding", "gzip");
		// request.setHeader("Cookie", "tgw_l7_route=11ffadb24678a5dfe7d7767d8c8c0053");

		// 配置请求头
		// 注意：appVersion通常不作为HTTP头传递，而是作为请求体或查询参数（本例中未作为查询参数，故此处不设置）
		// 若确实需要将其作为头信息发送，请确保服务器能够正确解析
		// request.setHeader("appVersion", "{\"uid\":\"228316\",...}"); //
		// 省略了完整的JSON字符串以节省空间
		// Content-Length头通常由HttpClient根据请求体内容自动设置，无需手动指定
		// Connection: Keep-Alive通常由HttpClient管理，但在需要时也可明确设置
//		request.setHeader("Connection", "Keep-Alive");
//		request.setHeader("Accept-Encoding", "gzip");

		request.setHeader("token", "aiya_aade486f-3dd9-4e52-b7a0-2e0edb1610c4n7");
		// 注意：appVersion通常不作为HTTP头传递，而是作为请求体或查询参数（本例中未作为查询参数，故此处不设置）
		// 若确实需要将其作为头信息发送，请确保服务器能够正确解析
		// request.setHeader("appVersion", "{\"uid\":\"228316\",...}"); //
		// 省略了完整的JSON字符串以节省空间
		request.setHeader("Content-Type", "application/json; charset=UTF-8");
		// Content-Length头通常由HttpClient根据请求体内容自动设置，无需手动指定
		request.setHeader("Host", "testlive.yueji.pro");
		// Connection: Keep-Alive通常由HttpClient管理，但在需要时也可明确设置
		request.setHeader("Connection", "Keep-Alive");
		request.setHeader("Accept-Encoding", "gzip");
		request.setHeader("versionName", "3.9.1");
		request.setHeader("appVersion",
				"{\"uid\":\"435407\",\"systemModel\":\"Redmi 5 Plus\",\"appType\":\"1\",\"appVer\":\"3.9.1\",\"phoneBrand\":\"xiaomi\",\"version\":\"3.9.1\",\"deviceId\":\"186d155c-1555-4559-86de-f8480f9081f7\",\"systemVersion\":\"8.1.0\",\"versionCode\":\"20250715\"}"
						+ "");

		request.setHeader("versionCode", "20250715");
		request.setHeader("clientType", "Android");
		request.setHeader("referer", "http://testlive.yueji.pro");

		request.setHeader("User-Agent", "TaoLuApp (Android 8.1.0)" + "");

		String jsonBody = "{\"userId\":\"" + uid + "\"}";
//		String jsonBody = "{\"videoTitle\":\"12\"}";
		StringEntity entity = new StringEntity(jsonBody, "UTF-8");
		request.setEntity(entity);
		try (CloseableHttpResponse response = client.execute(request)) {
			// System.out.println("Status: " + response.getStatusLine());
			String reString = EntityUtils.toString(response.getEntity());
			// System.out.println(reString);
			return reString;
		}
	}

}
