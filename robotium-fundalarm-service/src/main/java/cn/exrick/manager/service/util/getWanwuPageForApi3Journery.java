package cn.exrick.manager.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class getWanwuPageForApi3Journery {

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();

		int type = 3;
		int tag = 0;// 1只提取有效行程 0无效也提取
		// 123主播：240485,117853,150192,170351,163453,379212,108281,287463,301869,288449,313834,372044,106423,159156,150087,429103,170573,107001,359168,133964,223351,117638,288200,251452,402216,441597,120352,109884,106546,129792,194348,150357,191200,266230,107047,365788,372016,155285,367323,401572,399844,404718,125212,164850,116571,128296,156996,110963,140316,170603,108564,192630,264457,272388,289358,115104,293452,295503,141015,324891,303664,315216,114459,163724,370738,213338,115701,327500,107296,123753,153985,109439,318184,262312,371777,191595,258359,136338,253808,168037,107422,191798,124839,319183,225696,107439,173090,205058,390760,112639,111257,219040,174770,113220,348757,252714,111431,254809,435582,435506,435402,135280,115697,212826,323373,428174,209291,161968,202960,186923,192066,293077,171766,186208,331281,385690,131325,428148,155129,161152,281209,107428,107631,150219,133671,416846,273587

		String vids = "108148,386276,329861";//
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

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (int j = 0; j < links.length; j++) {

//					cn.hutool.json.JSONObject link = links[j];
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

				String articleUrlString = links[j];
				// attr("href").replace("/cn/movie/", "");
				articleUrlString = "http://127.0.0.1:54545/appapi/?service=Journey.getUserJourney&uid=267412&token=9933096e2d731a5807210e0212a3f838&version=139&dev_id=c3bb248a1abcecb0218cf5c6d3b6f812&platform=2&to_uid="
						+ articleUrlString;
				url = articleUrlString;

				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
//							.header("Cookie",
//									"hls=s%3Aj9h_seF6e_ES1cK24p8h1WZUGx5Ny-Ba.Ds7igDStVbbGpFkVG0YYnMCJokUn6Lp3k49tqdjR9pE")
						.header("User-Agent",
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
						.execute();

				String sourceString = articleRes.body();
				org.json.JSONArray arr = new JSONObject(sourceString).getJSONObject("data").getJSONArray("info");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);
				if (arr.length() == 0) {
					System.out.println("无数据：" + links[j]);
					continue;
				}
				String name = arr.getJSONObject(0).getJSONObject("userinfo").getString("user_nicename");
				System.out.println("---------" + name + "-----------");
				for (int i = 0; i < arr.length(); i++) {

//				

					// 提取URL
					JSONObject jo = arr.getJSONObject(i);
					String endt = jo.getString("end_time");
					String todayT = sdf.format(new Date());
					if (todayT.compareTo(endt) > 0 && tag == 1)
						continue;

					System.out.println("主题：" + jo.getString("title"));
					System.out.println("活动日期：" + jo.getString("start_time") + "-" + jo.getString("end_time"));
					System.out.println("发布日期：" + jo.getString("datetime"));
					org.json.JSONArray thumbsArr = jo.getJSONArray("thumbs");
					for (int k = 0; k < thumbsArr.length(); k++) {
						System.out.println(thumbsArr.getString(k));
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

}
