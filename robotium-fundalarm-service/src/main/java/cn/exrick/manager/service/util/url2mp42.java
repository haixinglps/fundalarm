
package cn.exrick.manager.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
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

public class url2mp42 {
	private static final ExecutorService executor = Executors.newFixedThreadPool(1);
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
		Path filePath = Paths.get("d:/allm3u8.bat");
		HashSet<String> keySet = new HashSet<String>();
		try {
			lines = Files.readAllLines(filePath, Charset.forName("GBK"));
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
				String keys1 = lines.get(j);
				millionLinks.add(keys1);
				System.out.println("导入命令：" + j + "/" + lines.size());

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

		} catch (

		Exception e) {
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
//
//				ProcessBuilder builder = new ProcessBuilder(command);
//				builder.redirectErrorStream(true);
//				Process process = builder.start();
				System.err.println("开始运行" + rtmpUrl);
//				Process p = Runtime.getRuntime().exec(rtmpUrl);
				ProcessBuilder pb = new ProcessBuilder("cmd", "/c", // Windows必须通过cmd执行
						rtmpUrl // -o con 输出到控制台
				);
				Process p = pb.start();

				// 异步处理FFmpeg输出
				new Thread(() -> {
					try (BufferedReader reader = new java.io.BufferedReader(
							new java.io.InputStreamReader(p.getInputStream()))) {
						String line;
						while ((line = reader.readLine()) != null) {
							System.out.println("--->" + "[wget] " + line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();

				int exitCode = p.waitFor();

				System.out.println(rtmpUrl + "\t" + exitCode);

				if (exitCode == 0) {
					writer.write(rtmpUrl);
				} else {
					writerErr.write(rtmpUrl);
				}

			} catch (Exception e) {

				writerErr.write(rtmpUrl);

			} finally {
				countDownLatch.countDown();
			}

		}
	}
}
