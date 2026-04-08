package cn.exrick.manager.service.util;

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

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class getWanwuPageForApi3vid2 {
	private static final ExecutorService executor = Executors.newFixedThreadPool(50);
	private static final int BATCH_SIZE = 1000; // 分批处理量
	static AtomicInteger processed = new AtomicInteger(0);
	static ThreadSafeFileWriter writer = null;
	static ThreadSafeFileWriter writerLog = null;
	static ThreadSafeFileWriter writerErr = null;

	// 假设已初始化包含100万链接的列表

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();
		List<String> millionLinks = new ArrayList();

		List<String> lines = new ArrayList<String>();
		Path filePath = Paths.get("d:/download/sourcecc.txt");
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
				millionLinks.add(lines.get(j));
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
				// attr("href").replace("/cn/movie/", "");
				articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
						+ articleUrlString;
				String url = articleUrlString;
				System.out.println(articleUrlString);
				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
//						.header("Cookie",
//								"hls=s%3Aj9h_seF6e_ES1cK24p8h1WZUGx5Ny-Ba.Ds7igDStVbbGpFkVG0YYnMCJokUn6Lp3k49tqdjR9pE")
						.header("User-Agent",
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
						.execute();

				String sourceString = articleRes.body();
				System.out.println(sourceString);
				JSONObject jo = new JSONObject(sourceString);
				JSONObject jsonObject = jo.getJSONObject("data").getJSONObject("info");
//				int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//				int endIndex = jsonString.indexOf("'", startIndex);

				// 提取URL
				String videoUrl = jsonObject.getString("url");
				JSONObject ui = jsonObject.getJSONObject("userinfo");
				String vid = "作品id--" + jsonObject.getString("id") + "_用户id--" + jsonObject.getString("uid") + "_昵称--"
						+ jsonObject.getString("nickname") + "_拍摄地--" + jsonObject.getString("city") + "_纬度--"
						+ jsonObject.getString("lat") + "_经度--" + jsonObject.getString("lng") + "_常住省份--"
						+ ui.getString("province") + "_常住城市--" + ui.getString("city") + "_常住地址--"
						+ ui.getString("location") + "_余额--" + (ui.has("balance") ? ui.getString("balance") : "")
						+ "_粉丝数--" + ui.getString("fans");

				String img = jsonObject.getString("cover_pic");
				String sk = jsonObject.getString("trial_url");
				String price = jsonObject.getString("price");
				String seconds = jsonObject.getString("seconds_label");
				String sales = jsonObject.getString("sales");
				String addtime = jsonObject.getString("addtime");

				String views = jsonObject.getString("views");
				String likes = jsonObject.getString("likes");
				String comments = jsonObject.getString("comments");

				String collectNums = jsonObject.getString("collect_nums");
				String summary = jsonObject.getString("summary").replace("\r", "").replace("\n", "");

				// collect_nums summary

//				if (link.getStr("originalname") != null && !link.getStr("originalname").equals("")) {
//			if (urlSet.add(videoUrl)) {
				System.out.println(rtmpUrl + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

//				m3u8Result.append(jsonObject.getString("id") + "\t" + price + "\t" + sales + "\t" + seconds + "\t"
//						+ views + "\t" + likes + "\t" + comments + "\t" + addtime + "\t"
//						+ jsonObject.getString("status_label") + "\n");

				m3u8Result.append(jsonObject.getString("id") + "\t" + collectNums + "\t" + summary + "\n");

//				FileUtil.appendUtf8String(m3u8Result.toString(),
//						"d:/m3u8ListTaoluzhuboapi/self_" + 3 + "_" + Thread.currentThread().getId() + ".txt");

				writer.write(m3u8Result.toString());

//						processed.incrementAndGet();

//			getWanwuM3u8ForApi.geneBat(args, diskNum, 1);
//			m3u8Result = new StringBuffer();

//			}
			} catch (Exception e) {
//				FileUtil.writeUtf8String(rtmpUrl + "",
//						"d:/download/viderror_" + Thread.currentThread().getId() + ".txt");
				writerErr.write(rtmpUrl);

				// TODO: handle exception
			} finally {
				countDownLatch.countDown();
			}

		}
	}
}
