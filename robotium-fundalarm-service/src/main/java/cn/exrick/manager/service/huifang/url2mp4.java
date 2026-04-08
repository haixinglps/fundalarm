package cn.exrick.manager.service.huifang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class url2mp4 {
//	private static final int MAX_THREADS = 100;
	private static final ExecutorService executor = Executors.newCachedThreadPool();
	private static final Set<String> processedUrls = new HashSet<String>();
	private static final long SCAN_INTERVAL = 10; // 分钟
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static void main(String[] args) {
		String[] rtmpUrls = new String[10]; // 假设有100个RTMP流地址

//		// 填充流地址数组    

//		for (int i = 0; i < 10; i++) {
//			rtmpUrls[i] = "rtmp://example.com/stream_" + i;
//		}
//
//		// 提交所有转换任务
//		for (String url : rtmpUrls) {
//			executor.submit(new RtmpToMp4Task(url));
//		}
		String configFile = "D:\\Download\\mp4.txt";

		scheduler.scheduleAtFixedRate(() -> checkUrls(configFile), 0, SCAN_INTERVAL, TimeUnit.SECONDS);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			executor.shutdownNow();
		}));

		// 启动监控
		new Thread(() -> {
			ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
			while (true) {
				System.out.printf("活跃线程: %d | 池中线程: %d | 已完成: %d | 队列: %d%n", pool.getActiveCount(), pool.getPoolSize(),
						pool.getCompletedTaskCount(), pool.getQueue().size());
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}).start();

		// 关闭线程池
//		executor.shutdown();
	}

	private static void checkUrls(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String currentUrl;
			while ((currentUrl = br.readLine()) != null) {
				currentUrl = currentUrl.split("\t")[2];// .replace("userId�", "userId");
				String roomid = currentUrl.split("_")[1].split("\\?")[0];
				if (!processedUrls.contains(roomid)) {
					System.out.println("发现新流：" + currentUrl);
					System.out.println("直播间号码：" + roomid);

					processedUrls.add(roomid);
					System.out.println("添加到排重库：" + roomid);
					executor.submit(new RtmpToMp4Task(currentUrl, roomid));
					System.out.println("成功添加到队列：" + roomid);
					System.out.println("开始下载: " + currentUrl);
				}
			}
		} catch (IOException e) {
			System.err.println("监控文件出错: " + e.getMessage());
		}
	}

	private static class RtmpToMp4Task implements Runnable {
		private final String rtmpUrl;
		private final String ffmpegPath;
		String roomidString;

		public RtmpToMp4Task(String rtmpUrl, String room) {
			this.rtmpUrl = rtmpUrl;
			this.roomidString = room;
			this.ffmpegPath = "D:\\360Downloads\\Software\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg"; // 替换为实际路径
		}

		@Override
		public void run() {
			// 生成动态输出文件名
			String outputPath = roomidString + "_" + System.currentTimeMillis() + ".mp4";

			// 构建FFmpeg命令
			List<String> command = new ArrayList();
			command.add(ffmpegPath);
			command.add("-i");
			command.add(rtmpUrl);
			command.add("-c");
			command.add("copy");
			command.add("-f");
			command.add("mp4");
			command.add(outputPath);

			try {
				ProcessBuilder builder = new ProcessBuilder(command);
				builder.redirectErrorStream(true);
				Process process = builder.start();

				// 异步处理FFmpeg输出
				new Thread(() -> {
					try (BufferedReader reader = new java.io.BufferedReader(
							new java.io.InputStreamReader(process.getInputStream()))) {
						String line;
						while ((line = reader.readLine()) != null) {
							System.out.println(roomidString + "--->" + "[FFmpeg] " + line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();

				int exitCode = process.waitFor();
				System.out.println(roomidString + "--->" + "流 " + rtmpUrl + " 转换完成，退出码：" + exitCode);
			} catch (Exception e) {
				System.err.println(roomidString + "--->" + "处理流 " + rtmpUrl + " 失败: " + e.getMessage());
			}
		}
	}
}
