package cn.exrick.manager.service.stocks;

import java.io.File;
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

public class getstocksofgroup {

	static ThreadSafeFileWriter writerSubject = null;
	static ThreadSafeFileWriter writerLog = null;
//	static String mid = "3Q6APFJPTW0WK2D";
//	static String name = "零售";

//	先进封装	3Q0PNBN9800WK2D	574245965
//	太空光伏	3Q0PNHG9600QK2D	574245965
//	航天	3Q0PNMZD700LK2D	574245965
//	电网设备新	3PRGL3TD0L07K2D	574245965
//	ai应用可以看	3PRGKXSK0L0TK2D	574245965
//	腾讯	3PRGKR030G0YK2D	574245965
//	存储	3PRGKKJL0G0RK2D	574245965
//	今日看好	3PRGKAZH0L06K2D	574245965
//	今日	3P9N2E1EC805K2D	574245965
//	1月龙头	3NM87W7ZMW02K2D	574245965
//	Ai应用	3NGW7S24RC04K2D	574245965
//	1月	3NGJ6509JC06K2D	574245965
//	1	3N6BDS4RJL08K2D	574245965
//	广州加商业	3N6A67NHP00BK2D	574245965
//	火箭回收	3LZ423FK0G0CK2D	574245965
//	cpo	3LD5MGXR0004K2D	574245965
//	脑机接口	3LZ40Z8SHL0BK2D	574245965
//	12月第二周的主线	3LD7LE5E6G0CK2D	574245965
//	人形机器-新	3LD7L6ZN6C0DK2D	574245965
//	液冷	3LD5C12MZW05K2D	574245965
//	可控核聚变	3FWYL4L0XQ06K2D	574245965
//	海两岸	3KE9MACBZ008K2D	574245965
//	商业航天-新	3FWYLWSK880AK2D	574245965
//	25年趋势	3C9KDCJ9SC05K2D	574245965

	public static void main(String[] args) {
		System.out.println("testing...");

//		try {
//			writerLog = new ThreadSafeFileWriter("/home/www/stock/ts/" + name + ".txt");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		List<String> lines = new ArrayList<String>();

		Path filePath = Paths.get("/home/www/stock/goodgroup.txt");
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

		String url = "https://ugc.10jqka.com.cn/optdata/selfgroup/open/api/share/v1/query?share_userid=574245965&id_type=meta&id=mypage&from=sjcg_gphone";

		// http://127.0.0.1:54545/appapi/?service=MissVideo.searchVideo&uid=267412&token=abf8552d9c40faefd94e054a4c7a9c99&version=137&platform=2&key=%E9%AB%98%E8%B7%9F%E9%9E%8B&type=1&pnum=10&p=3
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Set<String> urlSet = new HashSet<String>();
		for (int i = 0; i < lines.size(); i++) {// 1910

			try {
				new File("/home/www/stock/ts/" + lines.get(i).split("\t")[0] + ".txt").delete();
				writerSubject = new ThreadSafeFileWriter("/home/www/stock/ts/" + lines.get(i).split("\t")[0] + ".txt");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			StringBuffer re = new StringBuffer();
			StringBuffer m3u8Result = new StringBuffer();
			try {
				String pU = url.replace("mypage", lines.get(i).split("\t")[1]);
				System.out.println("ur:" + pU);
//				String code = lines.get(k);

//				String baseUrl = "https://dict.hexin.cn:9531/stocks?pattern=" + code
//						+ "&isauto=1&associate=1&style=0&pl=g&isrealcode=1&json=1&br=sc&markettype=2&userid=805530754";
//				// System.out.println("request...");// HttpUtil.get(pU);
//				String baseInfo = Jsoup.connect(baseUrl).execute().body();
//				JSONObject baseObject = new JSONObject(baseInfo);
//				JSONArray arr = baseObject.getJSONObject("data").getJSONArray("body").getJSONArray(0);
//				String stockString = arr.getStr(1);
//				String marketString = arr.getStr(3);

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				// System.out.println("request...");// HttpUtil.get(pU);
				String docJson = Jsoup.connect(pU).userAgent(
						"Hexin_Gphone/11.36.05 (Royal Flush) hxtheme/0 innerversion/G037.09.065.1.32 followPhoneSystemTheme/0 userid/-805530754 getHXAPPAccessibilityMode/0 hxNewFont/1 isVip/0 getHXAPPFontSetting/normal getHXAPPAdaptOldSetting/0 okhttp/3.14.9")
						.header("Host", "dq.10jqka.com.cn").header("Cookie",
								"hxmPid=hqMarketPkgVersionControl; v=A0FFjzRNzS3iVCEZ0Q_-V_GHUoZbbrVg3-JZdKOWPcinim78677FMG8yaU4w; user=MDpteF82NjAzODkzOTI6Ok5vbmU6NTAwOjY3MDM4OTM5Mjo3LDExMTExMTExMTExLDQwOzQ0LDExLDQwOzYsMSw0MDs1LDEsNDA7MSwxMDEsNDA7MiwxLDQwOzMsMSw0MDs1LDEsNDA7OCwwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMSw0MDsxMDIsMSw0MDoxNjo6OjY2MDM4OTM5MjoxNzY3NjE5MTcwOjo6MTY3MTQwMDg2MDoyNjc4NDAwOjA6MTJmNTc2ZTQ0MDRhMGRkZDQyNjRkMzFkNGVhMDIyZGY3Ojow; userid=660389392; u_name=mx_660389392; sess_tk=eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6InNlc3NfdGtfMSIsImJ0eSI6InNlc3NfdGsifQ.eyJqdGkiOiJmNzJkMDJlYWQ0MzE0ZDI2ZDRkZGEwMDQ0NDZlNTcyZjEiLCJpYXQiOjE3Njc2MTkxNzAsImV4cCI6MTc3MDI5NzU3MCwic3ViIjoiNjYwMzg5MzkyIiwiaXNzIjoidXBhc3MuMTBqcWthLmNvbS5jbiIsImF1ZCI6IjIwMjMwODA0OTA3NTEyOTIiLCJhY3QiOiJvZmMiLCJjdWhzIjoiMDAzMDc0ODRiN2EzMDc4NWQxMGJjYzI5NDIzYWU0ZDA3OTFjMmZhYTllYTA3MTYwYjRkMDYxMmQzNWUxN2Q5MiJ9.g-Fm14M2JlrEYp_fIiwLAcfo5ftf2J9qbEfVSMtfQBxQCrbEapE0EDjYFqsPkIt30Lfh59eSl4VkR6qCq0CzXA; cuc=b22341aa15e5465bbf09d7107549a9f8; escapename=mx_660389392; ticket=8e6a76559e1c7a926ffe152fa6d373e6; user_status=0")
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
				JSONArray links = object.getJSONObject("data").getJSONArray("selfstock");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
//				re.append(code + "\t" + stockString + "\t");
//				re.append(object.getJSONObject("data").getJSONObject("location").getStr("name") + "\t"
//						+ object.getJSONObject("data").getJSONObject("industry_l2").getStr("name") + ";"
//						+ object.getJSONObject("data").getJSONObject("industry_l3").getStr("name") + "\t");

				if (links.size() == 0)
					break;
//				String yidongInfo = yidong.getYidong(code, marketString);
//				re.append(yidongInfo + "\t");

				String gainians = analysiseStock(links);
//				re.append(gainians);
//				System.out.println(re);
//				writerSubject.write(re.toString());

				//

			} catch (Exception e) {
				System.out.println("error");
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				// TODO: handle exception
//				writerLog.write(lines.get(k));
//				i--;
//				continue;
			} catch (Throwable e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			// System.out.println("执行结束");

//			FileUtil.writeUtf8String(m3u8Result.toString(),
//					"/home/www/stock/m3u8ListTaoluzhuboapi3/self_" + type + "_" + k + "_" + System.currentTimeMillis() + ".txt");

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
//			  "meta_id": "3NGJ6509JC06K2D",
//		        "name": "1月",
//		        "userid": "574245965",
			String code = subject.getStr("code");

			System.out.println(code);
			writerSubject.write(code);

		}
		return gn;

	}

}
