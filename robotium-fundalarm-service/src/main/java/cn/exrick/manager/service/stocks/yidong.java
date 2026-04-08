package cn.exrick.manager.service.stocks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import cn.exrick.common.utils.HttpUtil;
import cn.exrick.manager.service.util.HttpsUrlValidator;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class yidong {

	static ThreadSafeFileWriter writerSubject = null;
	static ThreadSafeFileWriter writerStock = null;
	static HashSet<String> keySet = new HashSet<String>();

	public static String getYidong(String code, String market) {
//		System.out.println("testing...");
		keySet.clear();
		try {
			writerSubject = new ThreadSafeFileWriter("d:/download/goodsubject.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		List<String> resultList = new ArrayList<String>();
		int page = 1;
		int pageEnd = 1;
		int type = 0;
		String keywordStringOri = "";// 小妈地狱高难度 34
		String keywordStringOri2 = null;// "皮靴咀嚼物";// 小妈地狱高难度 34
//		String code = "002518";
//		String market = "48";

		// 未知 寸+榨 1
		int diskNum = 2;// 0 k盘 1m盘
		String customerQQ = "";

		String nickname = null;// "玥樾";// "反差楠熙";
		int uid = 0;// 107002; 好玩不如嫂子
		String keywordString = null;

		try {
			keywordString = URLEncoder.encode(keywordStringOri.replace("==", ""), "UTF-8");
			System.out.println(keywordString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type == 1) {
			// args = new String[] { "" };
		}

		String url = "https://flow.10jqka.com.cn/anomaly/v1/history";

		// http://127.0.0.1:54545/appapi/?service=MissVideo.searchVideo&uid=267412&token=abf8552d9c40faefd94e054a4c7a9c99&version=137&platform=2&key=%E9%AB%98%E8%B7%9F%E9%9E%8B&type=1&pnum=10&p=3
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();
		for (int k = page; k <= pageEnd; k++) {// 1910
			StringBuffer m3u8Result = new StringBuffer();
			try {
				String pU = url.replace("mypage", k + "");
				System.out.println("ur:" + pU);

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				// System.out.println("request...");// HttpUtil.get(pU);
//				Element docJson = Jsoup.connect(pU).userAgent(
//						"Hexin_Gphone/11.36.05 (Royal Flush) hxtheme/0 innerversion/G037.09.065.1.32 followPhoneSystemTheme/0 userid/-805530754 getHXAPPAccessibilityMode/0 hxNewFont/1 isVip/0 getHXAPPFontSetting/normal getHXAPPAdaptOldSetting/0 okhttp/3.14.9")
//						.header("Host", "dq.10jqka.com.cn").header("Cookie",
//								"user=MDptdF83ZzR2b3RxcGY6Ok5vbmU6NTAwOjgxNTUzMDc1NDo3LDExMTExMTExMTExLDQwOzQ0LDExLDQwOzYsMSw0MDs1LDEsNDA7MSwxMDEsNDA7MiwxLDQwOzMsMSw0MDs1LDEsNDA7OCwwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMSw0MDsxMDIsMSw0MDo6Ojo4MDU1MzA3NTQ6MTc1NzI1NzU4NTo6OjE3NTcyNTc1NjA6MjY3ODQwMDowOjExNTU1ZTc2MjNiMzlhZTQ5YTdjYTZkNzJmN2QxNDE0YTo6MA%3D%3D; userid=805530754; u_name=mt_7g4votqpf; sess_tk=eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6InNlc3NfdGtfMSIsImJ0eSI6InNlc3NfdGsifQ.eyJqdGkiOiI0YTQxZDFmNzcyNmRjYWE3NDlhZTM5M2I2MmU3NTUxNTEiLCJpYXQiOjE3NTcyNTc1ODUsImV4cCI6MTc1OTkzNTk4NSwic3ViIjoiODA1NTMwNzU0IiwiaXNzIjoidXBhc3MuMTBqcWthLmNvbS5jbiIsImF1ZCI6IjIwMjMwODA0OTA3NTEyOTIiLCJhY3QiOiJtdCIsImN1aHMiOiJmZjA2MjkwZWQ2NTE3YzZlODA3ZjIwN2QzM2VmYTdmZDg3MmU2OGVjZDVhNmNhZWYzMjI1YjUyY2IzM2JlZTVhIn0.b350CboT8h8PWyU7hSezleWX_8JzdTbXIfZpEhieEo8D9CERiGbI9BgabEGw2lU7cV7s5R-VEll4zqcu8qIuHg; cuc=7862ab085c654f338b94ecd8694eb5d8; escapename=mt_7g4votqpf; ticket=d2dadd914675723c0d94c3747043f0aa; user_status=0; hxmPid=hqMarketPkgVersionControl; v=A8zIqFGmyKKCNtxedBzWFQ0QnyH-BXCvcqmEcyaN2HcasWMbThVAP8K5VAV1")
////						.header("appplatform", "ANDROID")
//						.header("accept-encoding", "gzip").header("host", "app.txcfgl.com")
//						.header("authorization",
//								"Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImFwcDo0NjIxNTM6YTNiYzdjY2EtM2IzMy00Yjc2LWJjYjItNmI0NzAyMzQ4ZDVhIn0.pNw-bBrp1tIIorDoSuWBlwPWDnAwYyc-oenzIvcI5EHrrWDJhO4ArzKn53hLl3ZwJuaI7zHfCAf3GqbsO0r-GQ")
//						
//						.timeout(30000).ignoreContentType(true).post().body();

				JSONObject jObject = new JSONObject();
//				{
//					  "thsHqCode": "300394",
//					  "marketId": "33",
//					  "count": 252
//					}
				jObject.put("thsHqCode", code);
				jObject.put("marketId", market);
				jObject.put("count", 252);

				String docJson = HttpUtil.sendJson(pU, jObject.toString());

				System.out.println("res:" + docJson);
//				System.out.println("Debug2:" + (docJson != null)); // 检查返回值
				// System.out.flush(); // 强制刷新
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				JSONArray links = object.getJSONObject("data").getJSONArray("anomalyAnalysisList");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");

				if (links.size() == 0)
					break;

				String result = analysiseStock(links);
				System.out.println("");
				return result;

			} catch (Exception e) {
				System.out.println("error");
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				// TODO: handle exception
			} catch (Throwable e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			// System.out.println("执行结束");

//			FileUtil.writeUtf8String(m3u8Result.toString(),
//					"d:/m3u8ListTaoluzhuboapi3/self_" + type + "_" + k + "_" + System.currentTimeMillis() + ".txt");
			// return null;
		}
		// stock2file.geneBat(args, diskNum, 1);
		return null;

	}

	public static String analysiseStock(cn.hutool.json.JSONArray ja) {

		String resultString = "";
		for (int i = 0; i < ja.size(); i++) {

			cn.hutool.json.JSONObject subject = ja.getJSONObject(i);
			// 题材保存到磁盘：
//			"ancestors": "0",
//		      "date": null,
//		      "fullName": "端侧AI",
//		      "hasSubSubject": null,
//		      "level": null,
//		      "name": "端侧AI",
//		      "parentId": null,
//		      "pctChg": 3.95,
//		      "rankTimes": 2,
//		      "subjectId": 9040800,
//		      "type": null

//			String ancestors = subject.getStr("ancestors");
//			String date = subject.getStr("date");
//			String fullName = subject.getStr("fullName");
//			String hasSubSubject = subject.getStr("hasSubSubject");
//			String level = subject.getStr("level");
			JSONArray keys = subject.getJSONArray("keywordList");
			for (int j = 0; j < keys.size(); j++) {
				if (keySet.add(keys.getStr(j))) {
					if (j == 0) {
//						System.out.print(keys.getStr(j));
						resultString += keys.getStr(j);
					} else {
//						System.out.print("+" + keys.getStr(j));
						resultString += "+" + keys.getStr(j);
					}
				}
			}
//			String parentId = subject.getStr("parentId");
//			String pctChg = subject.getStr("pctChg");
//			String rankTimes = subject.getStr("rankTimes");
//			String subjectId = subject.getStr("subjectId");
//			String type = subject.getStr("type");

//			String subjectInfo = ancestors + "\t" + date + "\t" + fullName + "\t" + hasSubSubject + "\t" + level + "\t"
//					+ name + "\t" + parentId + "\t" + pctChg + "\t" + rankTimes + "\t" + subjectId + "\t" + type;

//			writerSubject.write(subjectInfo);

		}
		return resultString;

	}

}
