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

public class getWanwuPageForApi3vid3Quark15239163812 {
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
//		List<String> resultList = new ArrayList<String>();
		List<String> millionLinks = new ArrayList();

		List<String> lines = new ArrayList<String>();
		Path filePath = Paths.get("d:/download/roomid.txt");
		HashSet<String> keySet = new HashSet<String>();
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
				System.out.println(articleUrlString);
				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
						.header("Cookie",
								"b-user-id=5c0e91e9-7d5c-a58b-67ee-04020d828576; _UP_A4A_11_=wb9c81ffa9bc48bab50a75bce2c5cf4a; xlly_s=1; __sdid=AATyk+Q8drQgzeM3pssns0Qixay8m7tg3Lyl9X2jq8f7Heg9V4eiDDDWp+og9Khsmtk=; _UP_D_=pc; _UP_30C_6A_=st9cb620136qivoo161v1gppcvpjo2fz; _UP_TS_=sg159c62f57758585fa4a6805549c0c262b; _UP_E37_B7_=sg159c62f57758585fa4a6805549c0c262b; _UP_TG_=st9cb620136qivoo161v1gppcvpjo2fz; _UP_335_2B_=1; __pus=736ed2e9f210baaabdd18d65e27349baAARVRLqZI1EpYTS/XljsmdZXRe66FFKhKW6Wlrs356hLBMKz6iQCz7pMO0CNmM+1Lqn8C4vn3AyJbrBI2GnRNQ0E; __kp=a74effb0-6147-11f0-bc92-47ad8a7af2a7; __kps=AAS5hcgdgt6p+1EIB02h/tr5; __ktd=MVA089y4vvW2XOFMIuV5jw==; __uid=AAS5hcgdgt6p+1EIB02h/tr5; isg=BO3tqD8_eTnihR0qZv7d6ATk_IlnSiEcuLcE2S_yFwTzpgxY95sC7BI1lHpAQjnU; tfstk=glOrjbs7QbhyxYSREI1Ub6PY4ffRp6o1qBsC-eYhPgjlOYpe3eTZywtW-MJFmeIuqWXh8ixNRuAIeMXF0ixDP7sCZM4eAe9WYe3R-MxHYM9ShfTJy6CnfBls1UF7SGuJLgf3m2bO8q_oLpo666CnfmwbtThN9327lnxkoq7C-7qHxgjmuN7lt7X3Z-qcDif3ZablnobGWWVlxHYmuwIcx6fHxs0VJxiesBhV7T0DSzwJAJx5UGYl058vgajTeUS4lrdVzTSiTiP3tI7y8pl3m5kCjdI5C9xmG75eow-VCLoaaH8HWpX2Tu0WjeJ2v_Om-8jMF36HZtznSGAJoCCchf0VZB-vBGBjHmQ2NC6OXtrm_g92mQWDsmkJzOL2ZOf38W-dZM5WatrbFECfDs7vOmFlYKvHNTCLqDSpgEAkLg8gvZbsSBpzxJWlkZSsuqrGVYWsrZXpSJede4QVf4U8pJBlkZSsuqyLpTKduGg8y; __puus=e1845ab3ae50e885e9e46c980ab51f9aAASUhn/7ZUlOCjxQ4mtnOO17OCMaHHDLRXolEvAifnPIr49603iIEjWtZ7dnEGgGKccQmznbszZqMaCVZGcTUPw2zWhKCzDu6Xgx0Mr6jF6C1Do1wkV4mwVjqwl3VytMkfJfQgRyIfm4QbiJ3zjnbo1UvZF7lSkSyZaPkjEpz74MjBW4OpibTB1qN9s7efMKa+ICipiQ100IJd9tWYFT8AYy")

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
					m3u8Result
							.append(rtmpUrl.replace("作品id_", "作品id--").replace("用户id_", "用户id--") + "\t" + (duration));

					writer.write(m3u8Result.toString());
				}

			} catch (Exception e) {

				writerErr.write(rtmpUrl);

			} finally {
				countDownLatch.countDown();
			}

		}
	}
}
