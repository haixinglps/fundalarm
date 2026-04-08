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

//https://app.txcfgl.com/api/app/subject/child-stock-tree/9055600
public class stocktree {
	static ThreadSafeFileWriter writerSubject = null;
	static ThreadSafeFileWriter writerStock = null;
	static ThreadSafeFileWriter errorlog = null;

	static String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImFwcDo0NTg1MTI6M2ZjOGY4N2UtN2M5ZS00YzkwLTkwMWMtM2FjOTYyOWRhZmFiIn0.xFnvq1vvc0LJMpRczGB9q91au0TjnexYl7MhfLt7gC6QebDX679ohWo9lQtRtrxfjv0OcwKt8zmJ-IBy3LUGcg";

	public static void main(String[] args) {
		int subjectid = 9039664;
		System.out.println("testing...");
		List<String> lines = new ArrayList<String>();

		Path filePath = Paths.get("d:/download/linyuan.txt");
		try {
			lines = Files.readAllLines(filePath);
//            for (String line : lines) {
//                System.out.println(line);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writerSubject = new ThreadSafeFileWriter("d:/download/subjectAll.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			errorlog = new ThreadSafeFileWriter("d:/download/stocklog.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writerStock = new ThreadSafeFileWriter("d:/download/stockAll.txt");
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

		String url = "https://app.txcfgl.com/api/app/subject/child-stock-tree/";

		// http://127.0.0.1:54545/appapi/?service=MissVideo.searchVideo&uid=267412&token=abf8552d9c40faefd94e054a4c7a9c99&version=137&platform=2&key=%E9%AB%98%E8%B7%9F%E9%9E%8B&type=1&pnum=10&p=3
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();
		for (int k = 9020493; k <= 9020493; k++) {// 1910
			StringBuffer m3u8Result = new StringBuffer();// || k >= 0
//			k = 9055817;
//			if (k > 10000000)
//				break;
			try {
				String pU = url + k;// lines.get(k);
				System.out.println("ur:" + pU);

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				// System.out.println("request...");// HttpUtil.get(pU);
//				Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImFwcDo0NjIxNTM6YTNiYzdjY2EtM2IzMy00Yjc2LWJjYjItNmI0NzAyMzQ4ZDVhIn0.pNw-bBrp1tIIorDoSuWBlwPWDnAwYyc-oenzIvcI5EHrrWDJhO4ArzKn53hLl3ZwJuaI7zHfCAf3GqbsO0r-GQ

				String docJson = Jsoup.connect(pU).userAgent("Dart/3.4 (dart:io)").header("appplatformbrand", "xiaomi")
						.header("appversion", "10403").header("appplatform", "ANDROID")
						.header("accept-encoding", "gzip").header("host", "app.txcfgl.com")
						.header("authorization", token).timeout(30000).ignoreContentType(true).execute().body();
				System.out.println("res:" + docJson);
//				System.out.println("Debug2:" + (docJson != null)); // 检查返回值
				// System.out.flush(); // 强制刷新
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				JSONArray links = object.getJSONArray("data");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
				if (links == null || links.size() == 0)
					continue;

				analysiseStock(links);

			} catch (Exception e) {
				System.out.println("error");
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				errorlog.write(k + "");
//				k = k - 1;
//				continue;

				// TODO: handle exception
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

	public static void analysiseStock(cn.hutool.json.JSONArray ja) {

		for (int i = 0; i < ja.size(); i++) {

			cn.hutool.json.JSONObject subject = ja.getJSONObject(i);
			// 题材保存到磁盘：

			String ancestors = subject.getStr("ancestors");
			String bizKey = subject.getStr("bizKey");
			String createBy = subject.getStr("createBy");
			String createTime = subject.getStr("createTime");
			String detail = subject.getStr("detail");
			String firstLetter = subject.getStr("firstLetter");
			String imgUrl = subject.getStr("imgUrl");
			String importance = subject.getStr("importance");
			String leadTimes = subject.getStr("leadTimes");
			String level = subject.getStr("level");
			String limitUpCount = subject.getStr("limitUpCount");
			String limitUpTimes = subject.getStr("limitUpTimes");
			String name = subject.getStr("name");
			String parentId = subject.getStr("parentId");
			String parentName = subject.getStr("parentName");
			String pctChg = subject.getStr("pctChg");
			String reason = subject.getStr("reason");
			String remark = subject.getStr("remark");
			String selectReason = subject.getStr("selectReason");
			String sort = subject.getStr("sort");
			String status = subject.getStr("status");
			String stockCount = subject.getStr("stockCount");

			String subjectId = subject.getStr("subjectId");
			String type = subject.getStr("type");

			String updateBy = subject.getStr("updateBy");
			String updateTime = subject.getStr("updateTime");

			String subjectInfo = ancestors + "\t" + bizKey + "\t" + createBy + "\t" + createTime + "\t" + detail + "\t"
					+ firstLetter + "\t" + imgUrl + "\t" + importance + "\t" + leadTimes + "\t" + level + "\t"
					+ limitUpCount + "\t" + limitUpTimes + "\t" + name + "\t" + parentId + "\t" + parentName + "\t"
					+ pctChg + "\t" + reason + "\t" + remark + "\t" + selectReason + "\t" + sort + "\t" + status + "\t"
					+ stockCount + "\t" + subjectId + "\t" + type + "\t" + updateBy + "\t" + updateTime;

			writerSubject.write(subjectInfo);

			// 股票保存到磁盘：
			JSONArray stocks = null;
//			System.out.println("stocks:");
//			System.out.println("[" + subject.getStr("stocks") + "]");
			if (subject.get("stocks") != null && !(subject.getStr("stocks") + "").contentEquals("null"))
				stocks = subject.getJSONArray("stocks");
			if (stocks != null && stocks.size() > 0) {

				for (int j = 0; j < stocks.size(); j++) {
//	                  "business": null,
//	                  "createTime": "2025-03-06 16:15:57",
//	                  "importance": 2,
//	                  "limitUp": null,
//	                  "name": "佳都科技",
//	                  "pctChg": 5.66,
//	                  "reason": "与北京明略合资成立苏州千视通算力大模型一体机公司，持股22.92%",
//	                  "remark": "聚焦“一个主赛道”（城市轨道交通和城市交通）、“两个主要应用场景”（城市安全和应急）的数字化建设。",
//	                  "selectedId": 126001,
//	                  "sort": 11,
//	                  "stockId": "600728",
//	                  "subjectId": 9043144,
//	                  "top": 1
					cn.hutool.json.JSONObject stock = stocks.getJSONObject(j);
					String business2 = stock.getStr("business");
					String createTime2 = stock.getStr("createTime");
					String importance2 = stock.getStr("importance");
					String limitUp2 = stock.getStr("limitUp");
					String name2 = stock.getStr("name");
					String pctChg2 = stock.getStr("pctChg");
					String reason2 = stock.getStr("reason");
					String remark2 = stock.getStr("remark");
					String selectedId2 = stock.getStr("selectedId");
					String sort2 = stock.getStr("sort");
					String stockId2 = stock.getStr("stockId");
					String subjectId2 = stock.getStr("subjectId");
					String top2 = stock.getStr("top");
					String stockInfo = business2 + "\t" + createTime2 + "\t" + importance2 + "\t" + limitUp2 + "\t"
							+ name2 + "\t" + pctChg2 + "\t" + reason2 + "\t" + remark2 + "\t" + selectedId2 + "\t"
							+ sort2 + "\t" + stockId2 + "\t" + subjectId2 + "\t" + top2;

					writerStock.write(stockInfo);

				}
			}

			// 继续循环子节点
			JSONArray children = subject.getJSONArray("children");
			if (children != null && children.size() > 0) {
				analysiseStock(children);
			}

		}

	}

}
