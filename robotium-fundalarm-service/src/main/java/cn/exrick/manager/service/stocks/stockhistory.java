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

public class stockhistory {

	static ThreadSafeFileWriter writerSubject = null;
	static ThreadSafeFileWriter writerStock = null;
	static ThreadSafeFileWriter err = null;
	static String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImFwcDo0NTg1MTI6MWQ5NDc5MjUtZTc4ZS00YTIzLTkwZGUtNDAzMGE4NmQyOTExIn0.sXp4wPqyu8znmqqV7vjM6zX4FI9uh4fWdcE6_0rrOldbg61TO2o_yQ7OiloLk8dmtL-7R7F-9xoYrMS_c-xEVw";

	static String days = "0";

	public static void main(String[] args) {
		days = args[0];
		System.out.println("testing...");
		try {
			writerSubject = new ThreadSafeFileWriter("/home/www/stock/allstockhistory.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			err = new ThreadSafeFileWriter("/home/www/stock/err.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		List<String> resultList = new ArrayList<String>();
		int page = 1;
		int pageEnd = 1000;
		int type = 0;
		String keywordStringOri = "";// 小妈地狱高难度 34
		String keywordStringOri2 = null;// "皮靴咀嚼物";// 小妈地狱高难度 34

		// 未知 寸+榨 1
		int diskNum = 2;// 0 k盘 1m盘
		String customerQQ = "";

		List<String> lines = new ArrayList<String>();

		Path filePath = Paths.get("/home/www/stock/ts/err.txt");
		try {
			lines = Files.readAllLines(filePath);
//            for (String line : lines) {
//                System.out.println(line);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		String nickname = null;// "玥樾";// "反差楠熙";
		int uid = 0;// 107002; 好玩不如嫂子
		String keywordString = null;

		try {
			keywordString = URLEncoder.encode(keywordStringOri.replace("==", ""), "UTF-8");
			// System.out.println(keywordString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type == 1) {
			args = new String[] { "" };
		}

		String url = "https://app.txcfgl.com/api/app/data/one-stock-daily?stockId=stockidvalue&days=650&platform=app&searchStockType=one";

		// http://127.0.0.1:54545/appapi/?service=MissVideo.searchVideo&uid=267412&token=abf8552d9c40faefd94e054a4c7a9c99&version=137&platform=2&key=%E9%AB%98%E8%B7%9F%E9%9E%8B&type=1&pnum=10&p=3
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();
		for (int k = 0; k < lines.size(); k++) {// 1910
			StringBuffer m3u8Result = new StringBuffer();
			try {
				String pU = url.replace("stockidvalue", lines.get(k));
				// System.out.println("ur:" + pU);

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				// System.out.println("request...");// HttpUtil.get(pU);
				String docJson = Jsoup.connect(pU).userAgent("Dart/3.4 (dart:io)").header("appplatformbrand", "xiaomi")
						.header("appversion", "10403").header("appplatform", "ANDROID")
						.header("accept-encoding", "gzip").header("host", "app.txcfgl.com")
						.header("authorization", token).timeout(30000).ignoreContentType(true).execute().body();
//				System.out.println("res:" + docJson);
//				System.out.println("Debug2:" + (docJson != null)); // 检查返回值
				// System.out.flush(); // 强制刷新
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				JSONArray links = object.getJSONObject("data").getJSONArray("items");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
				if (links.size() == 0)
					break;

				analysiseStock(links);

			} catch (Exception e) {
				System.out.println("error");
				e.printStackTrace();
				err.write(lines.get(k));
				System.err.println("-----------------url下载异常：" + url);
				k = k - 1;
				continue;
				// TODO: handle exception
			} catch (Throwable e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			// System.out.println("执行结束");
//			https://app.txcfgl.com/api/app/subject/stock-subject-lead/300058?date=2025-11-18

//			FileUtil.writeUtf8String(m3u8Result.toString(),
//					"/home/www/m3u8ListTaoluzhuboapi3/self_" + type + "_" + k + "_" + System.currentTimeMillis() + ".txt");

		}
		// stock2file.geneBat(args, diskNum, 1);

	}

	public static void analysiseStock(cn.hutool.json.JSONArray ja) {

		for (int i = 0; i < ja.size(); i++) {

			cn.hutool.json.JSONArray subject = ja.getJSONArray(i);
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
			StringBuffer subjectInfo = new StringBuffer();
//			if(!subject.get(1).toString().contentEquals("20251119"))
//				continue;

			for (int j = 0; j < subject.size(); j++) {
				// System.out.print(key + ",");

				subjectInfo.append(subject.get(j) + "\t");
			}
//			System.out.println();

//			String ancestors = subject.getStr("ancestors");
//			String date = subject.getStr("date");
//			String fullName = subject.getStr("fullName");
//			String hasSubSubject = subject.getStr("hasSubSubject");
//			String level = subject.getStr("level");
//			String name = subject.getStr("name");
//			String parentId = subject.getStr("parentId");
//			String pctChg = subject.getStr("pctChg");
//			String rankTimes = subject.getStr("rankTimes");
//			String subjectId = subject.getStr("subjectId");
//			String type = subject.getStr("type");

//			String subjectInfo = ancestors + "\t" + date + "\t" + fullName + "\t" + hasSubSubject + "\t" + level + "\t"
//					+ name + "\t" + parentId + "\t" + pctChg + "\t" + rankTimes + "\t" + subjectId + "\t" + type;
			subjectInfo.append("1");
			writerSubject.write(subjectInfo.toString());
			if (i == Integer.parseInt(days))// 0代表1天
				break;

		}

	}

}
