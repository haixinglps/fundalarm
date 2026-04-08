package cn.exrick.manager.service.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConcurrentUrlChecker {
	private static final int THREAD_COUNT = 50;
	private static final int RANGE_START = 23;
	private static final int RANGE_END = 25;
	private static final String BASE_URL = "https://kelly.dgshwhcb.top/android_117431_20250517_1959";
	static int ok = 0;
	private static final int BATCH_SIZE = 1000; // 分批处理量

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		BlockingQueue<String> results = new LinkedBlockingQueue<>();

		// 生成所有要测试的URL
		for (int sec = RANGE_START; sec < RANGE_END; sec++) {
			List<String> millionLinks = new ArrayList();
			for (long fileId = 1000000; fileId < 10000000; fileId++) {
				String url = BASE_URL + sec + "_" + String.format("%07d", fileId) + ".mp4";

				millionLinks.add(url);

			}

			int total = millionLinks.size();

			for (int i = 0; i < millionLinks.size(); i += BATCH_SIZE) {

				List<String> batch = millionLinks.subList(i, Math.min(i + BATCH_SIZE, millionLinks.size()));
				int end = Math.min(i + BATCH_SIZE, total);
				CountDownLatch batchLatch = new CountDownLatch(batch.size());
				System.out.println("7批次开始: " + i + "-" + end);
// 				writerLog.write((1000000 + i) + "");
				batch.forEach(link -> {

//					executor.submit(new DownVid(link + "", batchLatch));
					executor.execute(() -> {
						try {
							if (checkUrl(link)) {
								results.offer(link);
								ok = 1;
							}
						} catch (Exception e) {
							// TODO: handle exception
						} finally {
							batchLatch.countDown(); // 任务完成计数-1

						}

					});

//					executor.submit(
//							new DownVid(j + "")
//							() -> {
//						try {
//							downloadLink(link);
//							processed.incrementAndGet();
//						} catch (Exception e) {
//							System.err.println("Failed to download: " + link);
//						}
				});
//				}

//				);
				try {
					batchLatch.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 阻塞直到本批次全部结束
					// System.out.println("7批次完成: " + i + "-" + end);

			}

		}

		for (int sec = RANGE_START; sec < RANGE_END; sec++) {
			ArrayList<String> millionLinks = new ArrayList<String>();
			for (long fileId = 100000; fileId < 1000000; fileId++) {
				String url = BASE_URL + sec + "_" + String.format("%06d", fileId) + ".mp4";

				millionLinks.add(url);

			}

			int total = millionLinks.size();

			for (int i = 0; i < millionLinks.size(); i += BATCH_SIZE) {

				List<String> batch = millionLinks.subList(i, Math.min(i + BATCH_SIZE, millionLinks.size()));
				int end = Math.min(i + BATCH_SIZE, total);
				CountDownLatch batchLatch = new CountDownLatch(batch.size());
				System.out.println("6批次开始: " + i + "-" + end);
//	 				writerLog.write((1000000 + i) + "");
				batch.forEach(link -> {

//						executor.submit(new DownVid(link + "", batchLatch));
					executor.execute(() -> {
						try {
							if (checkUrl(link)) {
								results.offer(link);
								ok = 1;
							}
						} catch (Exception e) {
							// TODO: handle exception
						} finally {
							batchLatch.countDown(); // 任务完成计数-1

						}

					});

//						executor.submit(
//								new DownVid(j + "")
//								() -> {
//							try {
//								downloadLink(link);
//								processed.incrementAndGet();
//							} catch (Exception e) {
//								System.err.println("Failed to download: " + link);
//							}
				});
//					}

//					);
				try {
					batchLatch.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 阻塞直到本批次全部结束
					// System.out.println("6批次完成: " + i + "-" + end);

			}

		}
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS); // 设置超时时间
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// 输出结果
		System.out.println("找到的有效URL:");
		for (String validUrl : results) {
			System.out.println(validUrl);
		}
	}

	private static boolean checkUrl(String url) {
		try {
			// System.out.println("url:" + url);
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			connection.disconnect();

			if (responseCode == 200) {
				System.out.println("找到有效URL: " + url);
				return true;
			}
		} catch (Exception e) {
			System.out.println("检查URL时发生错误: " + url + " - " + e.getMessage());
		}
		return false;
	}
}
