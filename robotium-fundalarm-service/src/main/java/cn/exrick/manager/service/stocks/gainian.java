package cn.exrick.manager.service.stocks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;

import cn.exrick.manager.service.util.HttpsUrlValidator;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class gainian {

	static ThreadSafeFileWriter writerSubject = null;
	static ThreadSafeFileWriter writerLog = null;

	public static void main(String[] args) {
		System.out.println("testing...");
		try {
			writerSubject = new ThreadSafeFileWriter("d:/download/goodstock.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			writerLog = new ThreadSafeFileWriter("d:/download/goodstocklog.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<String> lines = new ArrayList<String>();

		Path filePath = Paths.get("d:/ts/shuoshuo2.txt");
		try {
			lines = Files.readAllLines(filePath);
//            for (String line : lines) {
//                System.out.println(line);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

//		List<String> resultList = new ArrayList<String>();
		int page = 0;
		int pageEnd = 1;
		int type = 0;
		String keywordStringOri = "";// 小妈地狱高难度 34
		String keywordStringOri2 = null;// "皮靴咀嚼物";// 小妈地狱高难度 34
//		String code = "002518";

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
			args = new String[] { "" };
		}

		String url = "https://dq.10jqka.com.cn/fuyao/thsweb_quote/fund/v1/get_list_obj?code=mypage&market=33";

		// http://127.0.0.1:54545/appapi/?service=MissVideo.searchVideo&uid=267412&token=abf8552d9c40faefd94e054a4c7a9c99&version=137&platform=2&key=%E9%AB%98%E8%B7%9F%E9%9E%8B&type=1&pnum=10&p=3
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Set<String> urlSet = new HashSet<String>();
		for (int k = 0; k < lines.size(); k++) {// 1910
			StringBuffer re = new StringBuffer();
			StringBuffer m3u8Result = new StringBuffer();
			try {
				String pU = url.replace("mypage", lines.get(k));
				System.out.println("ur:" + pU);
				String code = lines.get(k);

				String baseUrl = "https://dict.hexin.cn:9531/stocks?pattern=" + code
						+ "&isauto=1&associate=1&style=0&pl=g&isrealcode=1&json=1&br=sc&markettype=2&userid=805530754";
				// System.out.println("request...");// HttpUtil.get(pU);
				String baseInfo = Jsoup.connect(baseUrl).execute().body();
				JSONObject baseObject = new JSONObject(baseInfo);
				JSONArray arr = baseObject.getJSONObject("data").getJSONArray("body").getJSONArray(0);
				String stockString = arr.getStr(1);
				String marketString = arr.getStr(3);

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				// System.out.println("request...");// HttpUtil.get(pU);
				String docJson = Jsoup.connect(pU).userAgent(
						"Hexin_Gphone/11.36.05 (Royal Flush) hxtheme/0 innerversion/G037.09.065.1.32 followPhoneSystemTheme/0 userid/-805530754 getHXAPPAccessibilityMode/0 hxNewFont/1 isVip/0 getHXAPPFontSetting/normal getHXAPPAdaptOldSetting/0 okhttp/3.14.9")
						.header("Host", "dq.10jqka.com.cn").header("Cookie",
								"user=MDptdF83ZzR2b3RxcGY6Ok5vbmU6NTAwOjgxNTUzMDc1NDo3LDExMTExMTExMTExLDQwOzQ0LDExLDQwOzYsMSw0MDs1LDEsNDA7MSwxMDEsNDA7MiwxLDQwOzMsMSw0MDs1LDEsNDA7OCwwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMSw0MDsxMDIsMSw0MDo6Ojo4MDU1MzA3NTQ6MTc1NzI1NzU4NTo6OjE3NTcyNTc1NjA6MjY3ODQwMDowOjExNTU1ZTc2MjNiMzlhZTQ5YTdjYTZkNzJmN2QxNDE0YTo6MA%3D%3D; userid=805530754; u_name=mt_7g4votqpf; sess_tk=eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6InNlc3NfdGtfMSIsImJ0eSI6InNlc3NfdGsifQ.eyJqdGkiOiI0YTQxZDFmNzcyNmRjYWE3NDlhZTM5M2I2MmU3NTUxNTEiLCJpYXQiOjE3NTcyNTc1ODUsImV4cCI6MTc1OTkzNTk4NSwic3ViIjoiODA1NTMwNzU0IiwiaXNzIjoidXBhc3MuMTBqcWthLmNvbS5jbiIsImF1ZCI6IjIwMjMwODA0OTA3NTEyOTIiLCJhY3QiOiJtdCIsImN1aHMiOiJmZjA2MjkwZWQ2NTE3YzZlODA3ZjIwN2QzM2VmYTdmZDg3MmU2OGVjZDVhNmNhZWYzMjI1YjUyY2IzM2JlZTVhIn0.b350CboT8h8PWyU7hSezleWX_8JzdTbXIfZpEhieEo8D9CERiGbI9BgabEGw2lU7cV7s5R-VEll4zqcu8qIuHg; cuc=7862ab085c654f338b94ecd8694eb5d8; escapename=mt_7g4votqpf; ticket=d2dadd914675723c0d94c3747043f0aa; user_status=0; hxmPid=hqMarketPkgVersionControl; v=A8zIqFGmyKKCNtxedBzWFQ0QnyH-BXCvcqmEcyaN2HcasWMbThVAP8K5VAV1")
//						.header("appplatform", "ANDROID")
//						.header("accept-encoding", "gzip").header("host", "app.txcfgl.com")
//						.header("authorization",
//								"Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImFwcDo0NjIxNTM6YTNiYzdjY2EtM2IzMy00Yjc2LWJjYjItNmI0NzAyMzQ4ZDVhIn0.pNw-bBrp1tIIorDoSuWBlwPWDnAwYyc-oenzIvcI5EHrrWDJhO4ArzKn53hLl3ZwJuaI7zHfCAf3GqbsO0r-GQ")
//						
						.timeout(30000).ignoreContentType(true).execute().body();
				System.out.println("res:" + docJson);
//				System.out.println("Debug2:" + (docJson != null)); // 检查返回值
				// System.out.flush(); // 强制刷新
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				JSONArray links = object.getJSONObject("data").getJSONArray("gainian");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
				re.append(code + "\t" + stockString + "\t");
				re.append(object.getJSONObject("data").getJSONObject("location").getStr("name") + "\t"
						+ object.getJSONObject("data").getJSONObject("industry_l2").getStr("name") + ";"
						+ object.getJSONObject("data").getJSONObject("industry_l3").getStr("name") + "\t");

				if (links.size() == 0)
					break;
				String yidongInfo = yidong.getYidong(code, marketString);
				re.append(yidongInfo + "\t");

				String gainians = analysiseStock(links);
				re.append(gainians);
				System.out.println(re);
				writerSubject.write(re.toString());

			} catch (Exception e) {
				System.out.println("error");
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				// TODO: handle exception
				writerLog.write(lines.get(k));
			} catch (Throwable e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			// System.out.println("执行结束");

//			FileUtil.writeUtf8String(m3u8Result.toString(),
//					"d:/m3u8ListTaoluzhuboapi3/self_" + type + "_" + k + "_" + System.currentTimeMillis() + ".txt");

		}
		// stock2file.geneBat(args, diskNum, 1);

	}

	public static String analysiseStock(cn.hutool.json.JSONArray ja) {

		String gn = "";
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
			String name = subject.getStr("name");
//			String parentId = subject.getStr("parentId");
//			String pctChg = subject.getStr("pctChg");
//			String rankTimes = subject.getStr("rankTimes");
//			String subjectId = subject.getStr("subjectId");
//			String type = subject.getStr("type");

//			String subjectInfo = ancestors + "\t" + date + "\t" + fullName + "\t" + hasSubSubject + "\t" + level + "\t"
//					+ name + "\t" + parentId + "\t" + pctChg + "\t" + rankTimes + "\t" + subjectId + "\t" + type;

			gn += (name + "\t");
//			writerSubject.write(subjectInfo);

		}
		return gn;

	}

}
