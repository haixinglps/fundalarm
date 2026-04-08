package cn.exrick.manager.service.stocks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.exrick.manager.service.util.HttpsUrlValidator;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;
import cn.hutool.json.JSONArray;

public class getSubjects {
	private static final ExecutorService executor = Executors.newFixedThreadPool(50);
	private static final int BATCH_SIZE = 100000; // 分批处理量
	static AtomicInteger processed = new AtomicInteger(0);
	static ThreadSafeFileWriter writer = null;
	static ThreadSafeFileWriter writerstock = null;

	static ThreadSafeFileWriter writerLog = null;
	static ThreadSafeFileWriter writerErr = null;

	// 假设已初始化包含100万链接的列表

	public static void main(String[] args) {

//		System.setProperty("http.proxyHost", "localhost");
//		System.setProperty("http.proxyPort", "7890");
//		// 对于HTTPS
//		System.setProperty("https.proxyHost", "localhost");
//		System.setProperty("https.proxyPort", "7890");
		HashSet<String> keySet = new HashSet<String>();
//		List<String> resultList = new ArrayList<String>();
		List<String> millionLinks = new ArrayList();

		List<String> lines = new ArrayList<String>();
		Path filePath = Paths.get("f:/stock/linyuanall2.txt");
		try {
			lines = Files.readAllLines(filePath);
//            for (String line : lines) {
//                System.out.println(line);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer = new ThreadSafeFileWriter("f:/stock/subject.txt");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			writerstock = new ThreadSafeFileWriter("f:/stock/stock.txt");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

//		try {
//			writerLog = new ThreadSafeFileWriter("f:/downloadlog.txt");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		try {
			writerErr = new ThreadSafeFileWriter("f:/stock/downloaderror.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int type = 3;
		String vids = "190004,201099,203305,154901,120772,213749,208063,218511,217014,135671,219083,217498";//
		int vidbegin = 1;
		int vidend = 300000;
		int diskNum = 1;// 0 k盘 1m盘

		if (type == 1) {
			args = new String[] { "" };
		}
		String url = "http://127.0.0.1:54545/appapi/?service=Collect.list&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&type="
				+ type + "&p=mypage&sign=05285abd75763e1ad24c8974e625958b";
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();
		// for (int k = page; k <= page; k++) {// 1910
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

//			for (int j = vidbegin; j <= vidend; j++) {
//				millionLinks.add(j + "");
// 
//
//			}
			for (int j = lines.size() - 1; j >= 0; j--) {

//				String keys1 = lines.get(j).replace("\"", "");
//				if (keySet.add(keys1))
				int id = Integer.parseInt(lines.get(j));
				if (id > 9100000)
					continue;
				millionLinks.add(lines.get(j));
//				if (keys1.indexOf("作品id--") != -1) {
//
//					String keys2 = FileNameSanitizer.sanitize(keys1);
//					if (keySet.add(keys2))
//						millionLinks.add(keys2);
//
//				}

			}
			int total = millionLinks.size();

			for (int i = 0; i < millionLinks.size(); i += BATCH_SIZE) {

				List<String> batch = millionLinks.subList(i, Math.min(i + BATCH_SIZE, millionLinks.size()));
				int end = Math.min(i + BATCH_SIZE, total);
				CountDownLatch batchLatch = new CountDownLatch(batch.size());
				System.out.println("批次开始: " + i + "-" + end);
//				FileUtil.appendUtf8String((vidbegin + i) + "\r\n", "d:/download/downlog.txt");
				// writerLog.write((vidbegin + i) + "");
				processed.incrementAndGet();
				batch.forEach(link -> {

					executor.submit(new DownVid(link + "", batchLatch));

//					executor.submit(
//							new DownVid(j + "")
//							() -> {
//						try {
//							downloadLink(link);
//							processed.incrementAndGet();
//						} catch (Exception e) {
//							System.err.println("Failed to download: " + link);
//						}
//					});
				}

				);
				batchLatch.await(); // 阻塞直到本批次全部结束
				System.out.println("批次完成: " + i + "-" + end);

			}

			executor.shutdown();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("-----------------url下载异常：" + url);
			// TODO: handle exception
		}

	}

	private static class DownVid implements Runnable {
		private final String rtmpUrl;
		private final String ffmpegPath;
		CountDownLatch countDownLatch = null;
		ThreadSafeFileWriter writerStock = null;

		public DownVid(String j, CountDownLatch countDownLatch) {
			this.rtmpUrl = j;
			this.countDownLatch = countDownLatch;
			this.ffmpegPath = "D:\\360Downloads\\Software\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg"; // 替换为实际路径

		}

		@Override
		public void run() {

			try {
				StringBuffer m3u8Result = new StringBuffer();

				String articleUrlString = rtmpUrl + "";
				// 运行完，打开这里也测一下。
//				try {
//					articleUrlString = URLEncoder.encode(articleUrlString, "UTF-8");
////					System.out.println(articleUrlString);
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				// attr("href").replace("/cn/movie/", "");
				articleUrlString = "https://app.txcfgl.com/api/app/subject/child-stock-tree/" + articleUrlString;

				String url = articleUrlString;
				System.out.println(url);
				Response articleRes = Jsoup.connect(articleUrlString).userAgent("Dart/3.4 (dart:io)")
						.header("appplatformbrand", "xiaomi").header("appversion", "10403")
						.header("appplatform", "ANDROID").header("accept-encoding", "gzip")
						.header("host", "app.txcfgl.com")
						.header("authorization",
								"Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImFwcDo0NTg1MTI6Njk3MjFiOTgtOTBiZS00OWFlLThlOWItYjY3ZWEwNmRhNzYyIn0.A3VLHVHjlnWphFlaoLYXKt9Y2GKbjkd-83SqFzNXCRRhFIl9WIIEPun8VOGEnGr9eXGny97OBlR4fHbWWlU6SA")
						.timeout(30000).ignoreContentType(true).execute();

//						Jsoup.connect(articleUrlString)
//						.header("Content-Type", "application/json; charset=utf-8")
//						.header("Cookie",
//								"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; _UP_D_=pc; xlly_s=1; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_F7E_8D_=smKZ6VuDKwlQQE2dkCveEUsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM62yOCoUR6TdbRHu%2FVAp87k4dnjse%2B2%2BrbAu1yFnsYfYpkeu7XFGZeS7%2FtRGMdfr%2BsMk7nu2h4yt8zkCl0KABn6XDtkXtWwc%2BaGykxuPAZ3oN2V0G8Hu2tUwfciFhKhTDYBhMXt42IX5T0OABK9vhV%2B%2Biyn4QWXu%2BfLRM2xxJkw6XwNx%2BLGXCRhOnBIJjjnrh8cYD1zLEqy17ekFFQtJNHwljufc%2FrNRhob6h8YLOwy6Xc0ZjrCXqUEhJgnZxINLXTQeOqFvcVxa4vBJJJ28YPE; _UP_30C_6A_=st9cc620113dp03uxy550hz74dij4p8s; _UP_TS_=sg1a09b16b191791ba1bd999bdea8d3f9c8; _UP_E37_B7_=sg1a09b16b191791ba1bd999bdea8d3f9c8; _UP_TG_=st9cc620113dp03uxy550hz74dij4p8s; _UP_335_2B_=1; __pus=35e79faf503e20f2571be885777c304bAASb6PH7kGtNd2whNZC/oDExtXyP7ZuzXM5kzHcHrLqi8PlMDy6ux+rOIUMaI311IX1cHrYK9hh4+/1tb/AoMKuw; __kp=b3de5740-7528-11f0-b4b3-0b237d9a473d; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; tfstk=gdZIer01mBAI-kzxR0WwccaFBrm53OSVdLM8n8KeeDnp27wo_v8F-45757FZ9k7nT5G738xrTJodF7NYO4P7E9D-w7FSay7N0J2nq0C4ViS4Kpw0xXFIJ0IOeYMJyOuKrdmT80CVgGShA90S27RstIN_BfDSp3K8wdBsFxoJv4KpWChie0n-JXH96xkppYHKpOwtsYn-w73dCchie0h8w0h5ZHMwRXeChl47F5scSJG623EsB_DYd1KSQkHIWjeK13KRHvgIMJh19foEgVeoPuvkVq2Lo7DY9BCSZlwTvrFA_elLlYw4PSsvqA4LOl37bTj0CDGQw4aGGnkIYPgsCr62c4oU0qnL7NftzDMbfVqChehQy-3zX0fvvbg87k00Y1KIDr2EYzFA_elLlYIyunlf3jLWCqx-CjW1CUYos5nCOuhR760KIA5PCOOCr2HiCjW1CUYoJADaLO66Oaf..; __puus=3dc1606b4ccda3ca8f555bf8014fb075AATUWlRqG+XKfscM8UdeWGo2oQvqv+k5Nv4dGf9xtEJT+SbnSaKpyjf2fOV2J1wsJ3iQLCVhyfiSZIkTIoBfhbwsrQLpw1Kz/jQSxWuBWrMU6x9AZfTx+KtdD5XVHj/PAZeUp0cu2X9vwH4eUDe+ZHp9JlAnJzSgcMz2SR5RSjWPct3y01FhcFAGQE1a4KRjGnmdTEdTPecRuQIOfhY50/sT")
//
//						.header("User-Agent",
//								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
//						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
//						.execute();

				String sourceString = articleRes.body();
				System.out.println(sourceString);

				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(sourceString);
				JSONArray links = object.getJSONArray("data");

//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
				if (links == null || links.size() == 0)
					return;
				System.out.println(sourceString);
				System.out.println(rtmpUrl + ":" + links.size());

				analysiseStock(links);

			} catch (Exception e) {

				writerErr.write(rtmpUrl);

			} finally {
				countDownLatch.countDown();
			}

		}
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

			writer.write(subjectInfo);

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

					writerstock.write(stockInfo);

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
