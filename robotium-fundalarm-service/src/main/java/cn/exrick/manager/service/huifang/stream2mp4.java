package cn.exrick.manager.service.huifang;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;

public class stream2mp4 {
//	private static final int MAX_THREADS = 100;
	private static final ExecutorService executor = Executors.newCachedThreadPool();
	private static final Set<String> processedUrls = new HashSet<String>();
	private static final long SCAN_INTERVAL = 10; // 分钟
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static ThreadSafeFileWriter writer;
	static ThreadSafeFileWriter logWriter;
	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
	static RobotService roomService = null;
	static {

		roomService = context.getBean(RobotService.class);

	}
	static Set<String> authorSet = new HashSet<String>();

	public static void main(String[] args) {
		String[] rtmpUrls = new String[10]; // 假设有100个RTMP流地址

		Path filePathHistory = Paths.get("d:/download/goodauthor.txt");
		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				authorSet.add(his.split("=")[1]);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		String logpath = "f:\\录制中心\\record.log";
		try (BufferedReader br = new BufferedReader(new FileReader(logpath))) {
			String item;
			while ((item = br.readLine()) != null) {

				processedUrls.add(item);

			}
		} catch (IOException e) {
			System.err.println("读取日志文件出错: " + e.getMessage());
		}

		try {
			writer = new ThreadSafeFileWriter("f:/录制中心/duration.txt");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			logWriter = new ThreadSafeFileWriter("f:/录制中心/record.log");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

//		// 填充流地址数组    

//		for (int i = 0; i < 10; i++) {
//			rtmpUrls[i] = "rtmp://example.com/stream_" + i;
//		}
//
//		// 提交所有转换任务
//		for (String url : rtmpUrls) {
//			executor.submit(new RtmpToMp4Task(url));
//		}
		String configFile = "d:\\Download\\mp4.txt";

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
		Path filePathHistory = Paths.get("d:/download/goodauthor.txt");

		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				try {
					authorSet.add(his.split("=")[1]);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
			String item;
			System.out.println("扫描中..");
			while ((item = br.readLine()) != null) {
				String currentUrl = item.split("\t")[2];// .replace("userId�", "userId");
				if (currentUrl.contentEquals("null"))
					continue;
				String roomid = currentUrl.split("_")[1].split("\\?")[0];
				String author = null;// item.split("\t")[3];
//				System.out.println("roomid:" + roomid);

				if (!processedUrls.contains(roomid)) {
					System.out.println("发现新流：" + currentUrl);
					System.out.println("直播间号码：" + roomid);
					processedUrls.add(roomid);

					System.out.println("添加到排重库：" + roomid);
					// 判断author是否是客户关注的主播：

					executor.submit(new RtmpToMp4Task(currentUrl, roomid, author, item));
					System.out.println("成功添加到队列：" + roomid);
					System.out.println("开始下载: " + currentUrl);

				} else {
//					System.out.println("已处理过：" + roomid);
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
		String author;
		String item;
		int run = 1;
		long tm = System.currentTimeMillis();
		int tag = 0;

		public RtmpToMp4Task(String rtmpUrl, String room, String author, String item) {
			this.rtmpUrl = rtmpUrl;
			this.roomidString = room;
			this.author = author;
			this.item = item;
			this.ffmpegPath = "D:\\360Downloads\\Software\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg"; // 替换为实际路径
		}

		@Override
		public void run() {
			// 生成动态输出文件名
			String fnString = "";

			Waiwang2Video video = roomService.getVideo(roomidString);
//			String name = roomidString + "_" + System.currentTimeMillis() + ".mp4";
			if (video == null) {
				Waiwang2Video record = new Waiwang2Video();
				int len = item.split("\t").length;

				record.setNickname(len == 12 ? item.split("\t")[11] : "");
				if (record.getNickname().contentEquals("")) {
					// 查询某个主播的昵称：
//					109798
					Waiwang2Video historyRecord = roomService.getValidRecord(author);
					if (historyRecord != null)
						record.setNickname(historyRecord.getNickname());
				}
				// 读取最近的那个android标题：

				Path filePathHistory = null;
				String title = roomidString;

				filePathHistory = Paths.get("y:/ww.txt");

				try {
					List<String> linesHistory = Files.readAllLines(filePathHistory);
					for (int j = linesHistory.size() - 1; j >= 0;) {
						String item = linesHistory.get(j);
						String itemdt = item.split("\t")[1];
//							if (item.indexOf(nc) != -1 && itemdt.contentEquals(strdate)) {
//								title = item.split("\t")[0] + "_" + title;
//								break;
//							}
						title = item.split("\t")[0] + "_" + title;
						break;

					}

				} catch (IOException e) {
//					e.printStackTrace();
				}

				record.setTitle(title);
				record.setVid(Integer.parseInt(roomidString));
				record.setDt(item.split("\t")[1]);
				record.setType(item.split("\t")[2]);
				record.setAuthor(author);
//				record.setUid(item.split("\t")[4]);

				record.setCover(
						"https://qqcdn7.cjvhh.cn/app/user/162737_20251123205035_4862c74c514ae0066fe72f797bc38d74.png?imageView2/2/w/56");
				record.setPhoto(
						"https://qqcdn7.cjvhh.cn/app/user/162737_20251123205035_4862c74c514ae0066fe72f797bc38d74.png?imageView2/2/w/56");

				record.setUrl(UUID.randomUUID().toString());
				try {
					roomService.addVideo(record);

				} catch (Exception e2) {
					// TODO: handle exception
				}
				// video = roomService.getVideo(roomidString);
			}

			String outputPath = "f:\\录制中心\\" + roomidString + "_" + System.currentTimeMillis() + ".mp4";

			// 构建FFmpeg命令
			List<String> command = new ArrayList();
			command.add(ffmpegPath);
			command.add("-i");
			command.add("\"" + rtmpUrl + "\"");
			command.add("-c");
			command.add("copy");
			command.add("-f");
			command.add("mp4");
			command.add(outputPath);

			command.add("-fflags"); // 添加流信息输出标志
			command.add("+genpts"); // 强制生成PTS时间戳
//			command.add("-map"); // 显式映射流
//			command.add("0"); // 输入流索引

			if (1 == 1)
				try {
					ProcessBuilder builder = new ProcessBuilder(command);
					builder.redirectErrorStream(true);
					Process process = builder.start();
					Waiwang2Video video2 = roomService.getVideo(roomidString);
					// 更新数据库的直播地址：
					if (video2 != null && video2.getType() == null) {
						try {
							roomService.updateUrl(roomidString, rtmpUrl, item);

						} catch (Exception e2) {
							// TODO: handle exception
						}
					}

					// 异步处理FFmpeg输出

					new Thread(() -> {
						try (BufferedReader reader = new java.io.BufferedReader(
								new java.io.InputStreamReader(process.getInputStream()))) {
							String line;
							while (reader.readLine() != null) {
								long tm2 = System.currentTimeMillis();
//								System.out.println(line);
//							try {
//								Thread.sleep(600000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
								if (tag == 0) {
									if (author == null) {
										Waiwang2Video video3 = roomService.getVideo(roomidString);
										author = video3.getAuthor();
									}
									if (author != null) {
										tag = 1;
										if (!authorSet.contains(author)) {
											// 关闭process;
											process.destroyForcibly();
										} else {

										}
									}
								}

							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}).start();

					int exitCode = process.waitFor();
//					run = 0;

//				FileUtil.appendUtf8String(roomidString + "\r\n", "f:\\录制中心\\record.log");

					// System.out.println(roomidString + "--->" + "流 " + rtmpUrl + " 转换完成，退出码：" +
					// exitCode);
				} catch (Exception e) {
					System.err.println(roomidString + "--->" + "处理流 " + rtmpUrl + " 失败: " + e.getMessage());
				}

			logWriter.write(roomidString);
//			// 方法2：解析已生成的MP4文件
//			File outputFile = new File(outputPath);
//			System.out.println("计算文件时长：" + roomidString);
//			while (outputFile.exists()) {
//				System.out.println("正在获取文件时长...");
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				try (IsoFile isoFile = new IsoFile(outputPath)) {
//
//					long tm = isoFile.getMovieBox().getMovieHeaderBox().getDuration()
//							/ isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
//					writer.write(roomidString + "\t" + (tm / 60) + ":" + (tm % 60));
//
//					roomService.updateDuration(roomidString, (tm / 60) + ":" + (tm % 60));
//					break;
//				} catch (Exception e2) {
////					e2.printStackTrace();
//					System.out.println("无法获取时长：" + roomidString);
//					break;
//					// TODO: handle exception
//				} catch (Throwable e) {
//					System.out.println("未知异常");
//					break;
////					e.printStackTrace();
//					// TODO: handle exception
//				}
//			}
//			System.out.println("查找视频：" + roomidString);
//
//			Waiwang2Video video = roomService.getVideo(roomidString);
//			if (video != null) {
//				fnString = video.getTitle();
//				fnString = FileNameSanitizer.sanitize(fnString);
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//				String tmString = sdf.format(new Date());
//				// 新文件名
//				File newFile = new File("f:\\录制中心\\" + roomidString + "_" + fnString + "_" + tmString + ".mp4");
//
//				// 执行重命名
//				boolean isRenamed = outputFile.renameTo(newFile);
//
//				// 检查结果
//				if (isRenamed) {
//					System.out.println("文件重命名成功！" + newFile.getName());
//				} else {
//					System.out.println("文件重命名失败！");
//				}
//
//			}
		}
	}

//	public static boolean noChineseByChar(String str) {
//		if (str == null)
//			return true;
//		for (char c : str.toCharArray()) {
//			if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
//				return false;
//			}
//		}
//		return true;
//	}
}
