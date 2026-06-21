package cn.exrick.manager.service.huifang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.coremedia.iso.IsoFile;

import cn.exrick.manager.service.impl.RedisPoolExample;
import cn.exrick.manager.service.util.CerebrasFilter;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;
import redis.clients.jedis.Jedis;

public class stream2mpXJP {
//	private static final int MAX_THREADS = 100;
	private static final ExecutorService executor = Executors.newFixedThreadPool(50);
	private static final Set<String> processedUrls = ConcurrentHashMap.newKeySet();
	private static final long SCAN_INTERVAL = 10; // 分钟
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	static ThreadSafeFileWriter writer;
	static ThreadSafeFileWriter logWriter;
//	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
//	static RobotService roomService = null;
	static RedisPoolExample redisService = new RedisPoolExample();

	static String key = "csk-5ytctkm5w99wdcd9mvrdmxe225c3p2dvw9jencdk52tj5jmk";
	static CerebrasFilter ai = new CerebrasFilter(key, "http://127.0.0.1:7890", "gpt-oss-120b");
	static CerebrasFilter ai2 = new CerebrasFilter(key, "http://127.0.0.1:7890", "llama3.1-8b");
	static ThreadSafeFileWriter writerAI;


	public static void main(String[] args) {
		String[] rtmpUrls = new String[10]; // 假设有100个RTMP流地址

		String logpath = "/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/download/record.log";
		try (BufferedReader br = new BufferedReader(new FileReader(logpath))) {
			String item;
			while ((item = br.readLine()) != null) {

				processedUrls.add(item);

			}
		} catch (IOException e) {
			System.err.println("读取日志文件出错: " + e.getMessage());
		}

		try {
			writer = new ThreadSafeFileWriter(
					"/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/download/duration.txt");
			writerAI = new ThreadSafeFileWriter(
					"/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/download/ai.txt");

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			logWriter = new ThreadSafeFileWriter(
					"/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/download/record.log");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// 启动前恢复 luzhi_bak 遗留任务，防止旧进程崩溃导致积压
		try (Jedis jedis = RedisPoolExample.getJedis()) {
			String backupItem;
			int recovered = 0;
			while ((backupItem = jedis.rpop("luzhi_bak")) != null) {
				jedis.lpush("luzhi", backupItem);
				recovered++;
			}
			if (recovered > 0) {
				System.out.println("【启动恢复】已从 luzhi_bak 恢复 " + recovered + " 个遗留任务到 luzhi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		scheduler.scheduleAtFixedRate(() -> checkUrls(""), 0, SCAN_INTERVAL, TimeUnit.SECONDS);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			executor.shutdownNow();
			redisService.close();
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
		// close();

	}

	private static boolean isInRunWindow() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour >= 18 || hour < 2;
	}

	private static void checkUrls(String filePath) {
		// 时段限制已关闭，24小时运行
		/*
		if (!isInRunWindow()) {
			Calendar cal = Calendar.getInstance();
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			System.out.println("[时段限制] 当前时间 " + hour + ":00 不在运行窗口(18:00-02:00)，跳过本次扫描，10分钟后重试");
			return;
		}
		*/
		try {
			String item = redisService.brpoplpush("luzhi");
//			if (zbj == null)
//				return;

			// System.out.println("扫描中..");
			while (item != null) {
				try {
					String currentUrl = item.split(",")[0];// .replace("userId�", "userId");
					if (currentUrl.contentEquals("null"))
						continue;
					String roomid = currentUrl.split("_")[1].split("\\?")[0];
					String author = null;// item.split("	")[3];
//					System.out.println("roomid:" + roomid);

					if (!processedUrls.contains(roomid)) {
						System.out.println("发现新流：" + currentUrl);
//						currentUrl = "rtmp://play2.fjefu.cn/ww/room_355847?txSecret=528c4275eb876a92ecf65dcebf10d11e3e3d8bc81d4effa861dce7c96aadceec&txTime=69B5DCBE&userId�=267412&token=c7cd44c6d299a06d92115b1391f63e8";
						System.out.println("直播间号码：" + roomid);
						processedUrls.add(roomid);

						System.out.println("添加到排重库：" + roomid);
						// 判断author是否是客户关注的主播：

						executor.submit(new RtmpToMp4Task(currentUrl, roomid, author, item));
						System.out.println("成功添加到队列：" + roomid);
						System.out.println("开始下载: " + currentUrl);

					} else {
						try {
							redisService.lrem("luzhi_bak", item);
						} catch (Exception e) {
							// ignore
						}
					}
				} catch (Exception e) {
					System.out.println("[ERROR] checkUrls 处理任务异常: " + e.getMessage());
					try {
						redisService.lrem("luzhi_bak", item);
					} catch (Exception e2) {
						// ignore
					}
				}
				item = redisService.brpoplpush("luzhi");
			}
		} catch (Exception e) {
			System.out.println("[ERROR] checkUrls 外层异常: " + e.getMessage());
			e.printStackTrace();
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
			this.ffmpegPath = "ffmpeg"; // 替换为实际路径
		}

		@Override
		public void run() {

			titleString = item.split(",")[3].replace(".mp4", "");

			boolean exists = true;
			System.out.println("-----》值得录制:" + titleString);

			String outputPath = item.split(",")[2];
			// "f:\\录制中心\\" + roomidString + "_" + System.currentTimeMillis() + ".mp4";

			// 数据写入redis队列里去

			// 构建FFmpeg命令
			List<String> command = new ArrayList();
			command.add(ffmpegPath);
			command.add("-i");
			command.add(rtmpUrl);
			command.add("-c");
			command.add("copy");
			command.add("-f");
			command.add("mp4");

			if (!exists) {

				command.add("-t");
				command.add("60");

//				try {
//					Waiwang2Video re = roomService.getVideo(roomidString);
//					re.setTitle(re.getTitle() + "_非关注主播");
//					titleString = re.getTitle();
//					roomService.updateTitle(re);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}

			}
//
//		
//
			command.add("-fflags"); // 添加流信息输出标志
			command.add("+genpts"); // 强制生成PTS时间戳

			command.add(outputPath);

//			"-fflags", "+genpts+igndts+discardcorrupt",
//			"-use_wallclock_as_timestamps", "1",
//			"-vsync", "vfr"
//			

//			List<String> command = new ArrayList<>();
//			command.add(ffmpegPath);

			// 输入参数（-i 之前）
//			command.add("-reconnect");
//			command.add("1");
//			command.add("-reconnect_at_eof");
//			command.add("1");
//			command.add("-reconnect_streamed");
//			command.add("1");
//			command.add("-reconnect_delay_max");
//			command.add("10");
//			command.add("-timeout");
//			command.add("5000000");
//			command.add("-rw_timeout");
//			command.add("5000000");
//
//			command.add("-fflags");
//			command.add("+genpts+igndts+discardcorrupt");
//			command.add("-use_wallclock_as_timestamps");
//			command.add("1");
//
//			// 输入源
//			command.add("-i");
//			command.add("\"" + rtmpUrl + "\"");

			// 输出参数（-i 之后）
//			command.add("-t");
//			command.add("600");               // 录制10分钟（600秒），FFmpeg自动优雅退出

//			command.add("-c");
//			command.add("copy");
//			command.add("-vsync");
//			command.add("vfr");
//			command.add("-max_interleave_delta");
//			command.add("0");
//			command.add("-movflags");
//			command.add("+faststart");
//			command.add("-f");
//			command.add("mp4");
//			command.add("-y");
//			command.add(outputPath);

			if (1 == 1)
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
								long tm2 = System.currentTimeMillis();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}).start();

					// 等待最多 7200 秒
					boolean exited = process.waitFor(7200, TimeUnit.SECONDS);

					boolean ffmpegSuccess = false;
					if (exited) {
						int exitCode = process.exitValue();
						if (exitCode == 0) {
							System.out.println("进程正常退出，退出码：" + exitCode);
							ffmpegSuccess = true;
						} else {
							System.out.println("ffmpeg 录制异常，退出码：" + exitCode + "，跳过推入队列：" + titleString);
						}
					} else {
						System.out.println("进程超时未退出，强制销毁：" + titleString);
						process.destroyForcibly();
						if (!process.waitFor(5, TimeUnit.SECONDS)) {
							System.out.println("强制杀死后仍未退出，可能有问题");
						}
					}

						if (ffmpegSuccess) {
							File outputFile = new File(outputPath);
							if (!outputFile.exists() || outputFile.length() == 0) {
								System.out.println("ffmpeg 输出文件不存在或为空，跳过推入队列：" + titleString);
							} else {
								// 【修复】先读取时长，成功后再推队列；失败时不删文件
								String duration = "";
								boolean durationOk = false;
								try {
									ExecutorService executor = Executors.newSingleThreadExecutor();
									Future<Long> future = executor.submit(() -> readDuration(outputPath));

									try {
										Long re = future.get(10, TimeUnit.SECONDS);
										if (re == null) {
											System.out.println("[WARN] 读取时长返回null，不推队列保留文件: " + titleString);
										} else {
											duration = (re / 60) + ":" + (re % 60);
											durationOk = true;
										}
									} catch (TimeoutException | InterruptedException | ExecutionException e) {
										future.cancel(true);
										System.out.println("[WARN] 读取时长超时/异常，不推队列保留文件: " + titleString);
									} finally {
										executor.shutdown();
									}

									if (durationOk) {
										// 时长读取成功 → 推入 videos 队列
										System.out.println("------发送到队列：" + item);
										try {
											redisService.rpush("videos", item.substring(item.indexOf(",") + 1));
										} catch (Exception e) {
											e.printStackTrace();
										}
										// 发送时长信息到redis队列
										redisService.lpush("videosduration",
												roomidString + "," + duration + "," + (exists ? 1 : 0));
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

					try {
						redisService.lrem("luzhi_bak", item);
						System.out.println("------从 luzhi_bak 移除已处理任务");
					} catch (Exception e) {
						e.printStackTrace();
					}

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

	private static Long readDuration(String mp4Path) throws IOException {
		try (IsoFile isoFile = new IsoFile(mp4Path)) {
			long tm = isoFile.getMovieBox().getMovieHeaderBox().getDuration()
					/ isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
//			String roomidString = file.getFileName().toString().substring(0, indexa);
// 			roomService.updateDuration(roomidString, (tm / 60) + ":" + (tm % 60));
			return tm;

		} catch (Exception e2) {
			return null;
// 			if ((e2.getMessage() + "").contentEquals("null")) {
//
//				Path paths = Paths.get(path + "\\" + file.getFileName());
//				try {
//					Files.delete(paths);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}

//				return null;
//
//			}
			// e2.printStackTrace();
			// TODO: handle exception
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
