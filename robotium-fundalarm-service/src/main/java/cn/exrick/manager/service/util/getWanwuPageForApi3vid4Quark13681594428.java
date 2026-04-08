package cn.exrick.manager.service.util;

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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class getWanwuPageForApi3vid4Quark13681594428 {
	private static final ExecutorService executor = Executors.newFixedThreadPool(50);
	private static final int BATCH_SIZE = 1000; // 分批处理量
	static AtomicInteger processed = new AtomicInteger(0);
	static ThreadSafeFileWriter writer = null;
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
		Path filePath = Paths.get("d:/download/roomid.txt");
		try {
			lines = Files.readAllLines(filePath);
//            for (String line : lines) {
//                System.out.println(line);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/self.txt");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			writerLog = new ThreadSafeFileWriter("d:/download/downloadlog.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writerErr = new ThreadSafeFileWriter("d:/download/downloaderror.txt");
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
			for (int j = 0; j < lines.size(); j++) {

				String keys1 = lines.get(j).replace("\"", "");
				if (keySet.add(keys1))
					millionLinks.add(keys1);
				if (keys1.indexOf("作品id--") != -1) {

					String keys2 = FileNameSanitizer.sanitize(keys1);
					if (keySet.add(keys2))
						millionLinks.add(keys2);

				}

			}
			int total = millionLinks.size();

			for (int i = 0; i < millionLinks.size(); i += BATCH_SIZE) {

				List<String> batch = millionLinks.subList(i, Math.min(i + BATCH_SIZE, millionLinks.size()));
				int end = Math.min(i + BATCH_SIZE, total);
				CountDownLatch batchLatch = new CountDownLatch(batch.size());
				System.out.println("批次开始: " + i + "-" + end);
//				FileUtil.appendUtf8String((vidbegin + i) + "\r\n", "d:/download/downlog.txt");
				writerLog.write((vidbegin + i) + "");
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
				try {
					articleUrlString = URLEncoder.encode(articleUrlString, "UTF-8");
//					System.out.println(articleUrlString);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// attr("href").replace("/cn/movie/", "");
				articleUrlString = "https://drive-pc.quark.cn/1/clouddrive/file/search?pr=ucpro&fr=pc&uc_param_str=&q="
						+ articleUrlString
						+ "&_page=1&_size=50&_fetch_total=1&_sort=file_type:desc,updated_at:desc&_is_hl=1";

				String url = articleUrlString;
				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
						.header("Cookie",
								"xlly_s=1; b-user-id=9d39ae94-33ca-12aa-1b63-f5c68e81ab71; __sdid=AAS304DHTXDC0g9E+WQg2/BuOE3Yz9wL0Fbp/v/AmdJhDnSqUftvccq2qsRBtjozXY6scACDXKE+yHapx0Dw6Tc5ZGTqI6rgRHEpHOzp6UahXg==; _UP_D_=pc; _UP_A4A_11_=wb9cb12fb9cb46f99b92b459907c2669; _UP_F7E_8D_=OU7RkS5Fl8AdDkyilkTsNsbnf14sWMzNqY85oZpxzjUH3dkF5IYkrnP2eiukeaFasLuGZhvzyXtD8CizlW8ST3INEc0DyOipgbVemTID4d%2FJfd6YLm4Bw0KoWmdkn3NKr%2FkWp7W34NZlhgFKUTD%2BEalz%2B%2FFr7vwn75FlhnOTYQk1e9bRjiBoAwUd%2Fruv44%2BOuS%2FGej9BWPFZp5DG2HYEJHY5ITnJa4IqamAG646kaPWLlBPf7OXKwxJ9tXWSwXlfsVA0Q8CRk9owEULl%2Buq4H94%2BvkjwKfcNCM%2F4fCtssLklNVWbuA7cEaAINyd%2BnUvWo9ZvT9tODUHTX%2BTcqN5XbcOk1ra3INMiXbnEFWx%2Fk%2BcjOk0NMklkPPJhcjIY9laLkPvhSjPtLS9YIGPd1Sag%2FoLmdhpsOV%2F1VxufgE6%2FXhCsSL5OwizVU2TxvjF8lUdBo8aG5K1wGd3iDExO0cma7g%3D%3D; _UP_30C_6A_=st9cb620134s28o7mfnsxi1ucbea6bvl; _UP_TS_=sg108f015fc6b244d30a60534601151f1d5; _UP_E37_B7_=sg108f015fc6b244d30a60534601151f1d5; _UP_TG_=st9cb620134s28o7mfnsxi1ucbea6bvl; _UP_335_2B_=1; __pus=4eeba9db433edcb768c0bd19289d5e08AAQL3a4pB/RQ60aZ3gSv8UBUnFuzcEtL1QJ+RwPo1sRwlOI2JaxzA3NH5MHZLqBUMzcG4hxizQ/lXMhL53WLn17j; __kp=16512ae0-61ad-11f0-ad29-75d1cb9bc3bd; __kps=AAR3/cn/73YEFI7Bh/b/NNVQ; __ktd=euHdlEdPc7wMd1ITqAD7Jg==; __uid=AAR3/cn/73YEFI7Bh/b/NNVQ; tfstk=gWvIUYATIy4QLhB8V6mah9T7REBWRckVpusJm3eU29Bp2TT17MPEt_uW1ULNJJWLplC9ogAP2J3lFGL5rwByTwSJ53LKxgoHFC2Wm3vExTohKHXlequq3q-HxT4WXO9o7hnRqTsKVQaMnHXleVraejkXxex2BGYdec6O4MQRewQ-fRIhWJI8pgUtfNbTeMCRvOBO0gaRvUB-fhQG2TQJy6n6XbtbdGPCJHibI43H_yb6vNw8epHlOZtza8eJCM1BnHQ6TBJ1A6_As68M5pLw2pJhsfF1EhRXPQLx7W7pGiT1gBgbdETh2FI980PlfQtJn9fQ28K1NwO9p1Z8eh6dS_RWR2NhJB81iGCTVxjwuNKHpCiuWHLVR9IO_xnXX_dwKsvEWJ6viHWewE3aTwdf2guU3ZT0p7Z1i8s1uci_Z7vSXl2jSqtme6IGb4osfyGl9Gj1uci_Z7fdjG-rfcaIZ; isg=BLKy--W-Dru7Zj3GA_meDjS4A_iUQ7bdscRo4nyL1GVQD1IJZNBT7Xdt-6uzeC51; __puus=712a2c7fcdded7d4847bab34bc20abc7AAR5Xf9+Lu1LQBx9CGMw+gYJMGlvbaoAtgHL+uzd/UF1KEp1XIPRQEAyEGZSrFRmGXD2BzcxUtlFu+yIgTildMTcQ7YbxqGYRD0kc7Un+DdNptZEPKBOHk7H4fQkIkU7btbPJwao8A/nePriyZxE7AcX3rLUQW/sFN5Q1JuH4myUPJr+didyPzEyglCwB29TDpoR3/AUfxgTDOYoDypgjUDD")

						.header("User-Agent",
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
						.execute();

				String sourceString = articleRes.body();
				System.out.println(sourceString);
				JSONObject jo = new JSONObject(sourceString);
				int total = jo.getJSONObject("metadata").getInt("_total");

				System.out.println(rtmpUrl + "-m3u8:\t" + total + "\t" + articleUrlString);

				if (total > 0) {
					JSONArray list = jo.getJSONObject("data").getJSONArray("list");
					String duration = (list.getJSONObject(0).getInt("duration") / 60) + ":"
							+ (list.getJSONObject(0).getInt("duration") % 60);
					m3u8Result.append(rtmpUrl.replace("作品id_", "作品id--").replace("用户id_", "用户id--") + "\t" + duration);

					writer.write(m3u8Result.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();

				writerErr.write(rtmpUrl);

			} finally {
				countDownLatch.countDown();
			}

		}
	}
}
