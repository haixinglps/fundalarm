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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.util.FileNameSanitizer;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;

class stream2mp4AI {
//	private static final int MAX_THREADS = 100;
	private static final ExecutorService executor = Executors.newFixedThreadPool(1);
	private static final Set<String> processedUrls = new HashSet<String>();
	private static final long SCAN_INTERVAL = 10; // 分钟（已弃用，改用随机间隔）
	private static String configFileStatic;
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static ThreadSafeFileWriter writer;
	static ThreadSafeFileWriter logWriter;
	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
	static RobotService roomService = null;
	static JedisClient redisService = null;

	static String key = "csk-5ytctkm5w99wdcd9mvrdmxe225c3p2dvw9jencdk52tj5jmk";
	static {

		roomService = context.getBean(RobotService.class);
		redisService = context.getBean(JedisClient.class);

	}
	static Set<String> authorSet = new HashSet<String>();
	static Set<String> authorSet2 = new HashSet<String>();

	// 每日录制上限控制
	private static final int MAX_DAILY_RECORDS = 10000;
	private static AtomicInteger dailyRecordCount = new AtomicInteger(0);
	private static String lastRecordDate = "";

	public static void main(String[] args) {
		String[] rtmpUrls = new String[10]; // 假设有100个RTMP流地址

		Path filePathHistory = Paths
				.get("/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/download/goodauthor1.txt");
		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				authorSet.add(his.split("=")[0]);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				authorSet2.add(his.split("=")[1]);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		String logpath = "/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/download/record.log";
		try (BufferedReader br = new BufferedReader(new FileReader(logpath))) {
			String item;
			while ((item = br.readLine()) != null) {

				processedUrls.add(item);

			}
		} catch (IOException e) {
			System.err.println("读取日志文件出错: " + e.getMessage());
		}

//		try {
//			//writer = new ThreadSafeFileWriter("f:/录制中心/duration.txt");
//
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		try {
			logWriter = new ThreadSafeFileWriter(
					"/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/download/record.log");
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
		String configFile = "/home/www/telegramsender/download/mp4.txt";
		stream2mp4AI.configFileStatic = configFile;

		// 首次调度，之后 checkUrls 内部递归随机间隔调度
		scheduler.schedule(() -> stream2mp4AI.checkUrls(configFile), 0, TimeUnit.SECONDS);
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
					Thread.sleep(600000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}).start();

		// 关闭线程池
//		executor.shutdown();
	}

	private static boolean isInRunWindow() {
		// 运行窗口已取消，全天采集
		return true;
	}

	private static void checkUrls(String filePath) {
		if (!isInRunWindow()) {
			Calendar cal = Calendar.getInstance();
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			System.out.println("[时段限制] 当前时间 " + hour + ":00 不在运行窗口(18:00-02:00)，跳过本次扫描，10分钟后重试");
			scheduler.schedule(() -> checkUrls(configFileStatic), 600, TimeUnit.SECONDS);
			return;
		}

		Path filePathHistory = Paths
				.get("/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/download/goodauthor1.txt");

		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				try {
					authorSet.add(his.split("=")[0]);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				try {
					authorSet2.add(his.split("=")[1]);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
//		if (zbj == null)
//			return;

		try {

			String itema = redisService.brpoplpushdemand("videosduration");

			// System.out.println("扫描中..");
			while (itema != null) {
				String vid = itema.split(",")[0];// .replace("userId�", "userId");

				String duration = itema.split(",")[1];// .replace("userId�", "userId");
				roomService.updateDuration(vid, duration);
				redisService.lrem("videosduration_bak", 1, itema);

				itema = redisService.brpoplpushdemand("videosduration");
			}

			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
				String item;
				// System.out.println("扫描中..");
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
		} catch (Exception e) {
			// TODO: handle exception
		}

		// 递归调度下一次扫描，随机间隔 5-30 秒
		int delay = 5 + (int)(Math.random() * 26);
		scheduler.schedule(() -> checkUrls(configFileStatic), delay, TimeUnit.SECONDS);
	}


	// 调用 Python 脚本进行 AI 判断
	private static int judgeByPython(String title) {
		try {
			ProcessBuilder pb = new ProcessBuilder(
				"python3",
				"/home/www/telegramsender/url2redis/judge_live.py",
				title
			);
			pb.redirectErrorStream(true);
			Process process = pb.start();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				StringBuilder output = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					output.append(line).append("\n");
				}
				boolean finished = process.waitFor(60, TimeUnit.SECONDS);
				if (!finished) {
					process.destroyForcibly();
					System.out.println("[AI判断] Python脚本超时，默认不录制");
					return -1;
				}
				String result = output.toString().trim();
				String[] lines = result.split("\n");
				String lastLine = lines[lines.length - 1].trim();
				System.out.println("[AI判断] Python返回: " + lastLine);
				if (lastLine.equals("1")) return 1;
				if (lastLine.equals("0")) return 0;
				return -1;
			}
		} catch (Exception e) {
			System.out.println("[AI判断] 异常: " + e.getMessage());
			return -1;
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
		String titleString = "";

		public RtmpToMp4Task(String rtmpUrl, String room, String author, String item) {
			this.rtmpUrl = rtmpUrl;
			this.roomidString = room;
			this.author = author;
			this.item = item;
			this.ffmpegPath = "D:\\360Downloads\\Software\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg"; // 替换为实际路径
		}

		/**
		 * 调用 Python API 获取直播间信息（替代 pcap1 抓包）
		 */
		private JSONObject callPythonApi(String roomId) {
			try {
				ProcessBuilder pb = new ProcessBuilder(
					"python3",
					"/home/www/code/ww/get_room_info_for_java.py",
					roomId
				);
				pb.redirectErrorStream(true);
				Process process = pb.start();
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line;
					StringBuilder output = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						output.append(line);
					}
					boolean finished = process.waitFor(30, TimeUnit.SECONDS);
					if (!finished) {
						process.destroyForcibly();
						System.out.println("[API] Python脚本超时: room=" + roomId);
						return null;
					}
					String result = output.toString().trim();
					if (result.isEmpty()) {
						return null;
					}
					// 过滤 stderr 日志，只取最后一行 JSON
					String[] lines = result.split("\n");
					String jsonLine = lines[lines.length - 1].trim();
					if (jsonLine.isEmpty()) {
						return null;
					}
					return new JSONObject(jsonLine);
				}
			} catch (Exception e) {
				System.out.println("[API] 调用异常: " + e.getMessage());
				return null;
			}
		}

		@Override
		public void run() {
			try {
				_run();
			} catch (Exception e) {
				System.out.println("[ERROR] RtmpToMp4Task异常: " + e.getMessage());
				e.printStackTrace();
			}
		}

		public void _run() {
			// 生成动态输出文件名
//			String fnString = "";

			Waiwang2Video video = roomService.getVideo(roomidString);
			if (video == null || video.getAuthor() == null || video.getAuthor().isEmpty()) {
				// 通过 API 接口获取直播间信息（替代 pcap1 抓包）
				JSONObject apiResult = callPythonApi(roomidString);
				if (apiResult != null && apiResult.getBoolean("success")) {
					Waiwang2Video record = new Waiwang2Video();
					record.setVid(Integer.parseInt(roomidString));
					record.setDt(item.split("\t")[1]);
					record.setType(item.split("\t")[2]);
					record.setUrl(UUID.randomUUID().toString());

					// 从 API 返回填充字段
					record.setAuthor(apiResult.optString("userId", ""));
					record.setNickname(apiResult.optString("nickName", ""));
					record.setTitle(apiResult.optString("title", roomidString));
					record.setPhoto(apiResult.optString("avatar", ""));
					record.setCover(apiResult.optString("coverUrl", ""));
					record.setBio(apiResult.optString("bio", ""));
					record.setCity(apiResult.optString("city", ""));
					record.setLocationcity(apiResult.optString("locationCity", ""));
					record.setProvince(apiResult.optString("province", ""));

					try {
						if (video == null) {
							roomService.addVideo(record);
							System.out.println("[API] 新建记录: room=" + roomidString + ", author=" + record.getAuthor() + ", nick=" + record.getNickname() + ", title=" + record.getTitle());
						} else {
							// 更新现有记录
							record.setId(video.getId());
							roomService.updateTitle(record);
							System.out.println("[API] 更新记录: room=" + roomidString + ", author=" + record.getAuthor() + ", nick=" + record.getNickname());
						}
					} catch (Exception e2) {
						System.out.println("[API] 保存记录失败: " + e2.getMessage());
					}
				} else {
					System.out.println("[API] 获取房间信息失败: room=" + roomidString + ", 降级使用原始逻辑");
					// 降级：创建空记录，让后续流程继续（兼容旧逻辑）
					if (video == null) {
						Waiwang2Video record = new Waiwang2Video();
						record.setVid(Integer.parseInt(roomidString));
						record.setDt(item.split("\t")[1]);
						record.setType(item.split("\t")[2]);
						record.setTitle(roomidString);
						record.setUrl(UUID.randomUUID().toString());
						try {
							roomService.addVideo(record);
						} catch (Exception e) {}
					}
				}
			}

			// 重新获取（API 已写入/更新）
			video = roomService.getVideo(roomidString);
			if (video == null) {
				System.out.println("[ERROR] 无法获取视频记录: " + roomidString);
				return;
			}

			titleString = video.getTitle();
			author = video.getAuthor();
			// 检查日期并重置每日计数
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String today = dateFormat.format(new Date());
			if (!today.equals(lastRecordDate)) {
				lastRecordDate = today;
				String redisKey = "record_count:" + today;
				String countStr = redisService.get(redisKey);
				if (countStr != null) {
					dailyRecordCount.set(Integer.parseInt(countStr));
					System.out.println("[日期切换] 从Redis恢复计数: " + countStr);
				} else {
					dailyRecordCount.set(0);
					redisService.set(redisKey, "0");
					System.out.println("[日期切换] 每日录制计数已重置");
				}
			}

			// 最快判断方式：找到即停，不会遍历全部元素
			boolean exists = false;

			if (author != null) {
				if (authorSet2.contains(author))
					exists = true;
			}
			if (!exists) {
				exists = authorSet.stream().anyMatch(s -> titleString.toLowerCase().indexOf(s.toLowerCase()) != -1);
			}

			// 直接根据记事本判断，不再走AI
			if (!exists) {
				System.out.println("[跳过] 不在记事本中，不录制: " + titleString + " | author=" + author);
				logWriter.write(roomidString);
				return; // 不发送数据到队列
			}
			System.out.println("-----》大主播/关注主播，直接录制（不计入上限）: " + titleString);


			// 更新数据库的直播地址：
			if (video != null && video.getType() == null) {
				try {
					roomService.updateUrl(roomidString, rtmpUrl, item);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

			String fnString2 = video.getTitle();
			fnString2 = FileNameSanitizer.sanitize(fnString2);
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
			String tmString = sdf.format(new Date());
			String tt = fnString2 + "_" + tmString;
			String up = "/root/data/disk/" + tt + ".mp4";

			String info = "@linyuan56" + "," + up + "," + tt + "," + "bc" + video.getId() + "," + "135" + ","
					+ video.getCover();
			info = info + "," + up + ",0," + (video.getNickname() == null ? "" : video.getNickname()) + "," + 0 + ","
					+ 1;

			// 数据写入redis队列里去
			long be = System.currentTimeMillis();
			System.out.println("发送数据到国外：" + rtmpUrl + "," + info);
			try {
				redisService.lpush("luzhi", rtmpUrl + "," + info);
				long en = System.currentTimeMillis();
				System.out.println("发送成功  耗时：" + ((en - be) / 1000) + "秒");
			} catch (Exception e) {
				System.out.println("发送失败");
				// TODO: handle exception
			}

//			if (!exists) {
//
//				try {
//					Waiwang2Video re = roomService.getVideo(roomidString);
//					re.setTitle(re.getTitle() + "_非关注主播");
//					titleString = re.getTitle();
//					roomService.updateTitle(re);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				// titleString = video.getTitle();
//
//			}

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
