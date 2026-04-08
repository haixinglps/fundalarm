package cn.exrick.manager.service.task;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.dubbo.config.annotation.Reference;
import com.coremedia.iso.IsoFile;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.util.FileNameSanitizer;

//为磁盘文件重命名并设置文件时长
//@Component
public class TextProcessorLocalYunpan3 {

	private static ExecutorService executor = Executors.newFixedThreadPool(50);
//	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
	@Reference
	RobotService robotService;

	@Autowired
	JedisClient jedisClient;

//	static {
//
//		roomService = context.getBean(RobotService.class);
//
//	}
	@Scheduled(cron = "0 */1 * * * ?")

	public void rename() {
		// 178

//		System.setProperty("http.proxyHost", "localhost");
//		System.setProperty("http.proxyPort", "7890");
//		// 对于HTTPS
//		System.setProperty("https.proxyHost", "localhost");
//		System.setProperty("https.proxyPort", "7890");

//		doWith("\\\\MS-LNKKZQAQDHXV\\c\\XSBDownload\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\d\\360Downloads\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\e\\XSBDownload");
//		doWith("\\\\MS-LNKKZQAQDHXV\\e\\m3u8work\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\f\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\f");
//		doWith("\\\\MS-LNKKZQAQDHXV\\f\\录制中心");

		while (true) {
			// System.out.println("scaning3...");
			doWith("z:\\Downloads");
			doWith("z:");
			doWith("z:\\C盘瘦身目录");
//
			doWith("z:\\录制中心");
			// System.out.println("scaning3 ok...");
			executor.shutdown();
//			System.out.println("shutdown ok...");
			try {
				Thread.sleep(120000);
				executor = Executors.newFixedThreadPool(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void doWith(String path) {

		try {
			Path directoryPath = Paths.get(path); // 替换为你的目录路径

			Files.walkFileTree(directoryPath, Collections.emptySet(), 1, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					// 如果需要，可以在这里处理目录
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					if (attrs.isDirectory()) {
						return FileVisitResult.CONTINUE;
					}
					if (!file.getFileName().toString().endsWith(".mp4")) {
						return FileVisitResult.CONTINUE;

					}

//					try {

					// writer.write(roomidString + "\t" + (tm / 60) + ":" + (tm % 60));
					int indexa = file.getFileName().toString().indexOf("_");
					//
					if (indexa != -1 && file.getFileName().toString().indexOf("video_") == -1
							&& noChineseByRegex(file.getFileName().toString())) {
						// System.out.println("get time：" + path + "\\" + file.getFileName());

						ExecutorService executor = Executors.newSingleThreadExecutor();
						Future<Long> future = executor.submit(() -> readDuration(path + "\\" + file.getFileName()));

						try {
							Long re = future.get(10, TimeUnit.SECONDS);
							if (re == null) {
								try {
									Path paths = Paths.get(path + "\\" + file.getFileName());

									Files.delete(paths);
								} catch (Exception e3) {
									// System.err.println("无法删除1");
									// TODO Auto-generated catch block
									// e3.printStackTrace();
								} // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}

								return FileVisitResult.CONTINUE;
							} else {
								String roomidString = file.getFileName().toString().substring(0, indexa);
//								System.out.println("update time：" + path + "\\" + file.getFileName());
								robotService.updateDuration(roomidString, (re / 60) + ":" + (re % 60));
								// 上传文件到电报频道

								// 注册事务提交后的回调
//								org.springframework.transaction.support.TransactionSynchronizationManager
//										.registerSynchronization(new TransactionSynchronizationAdapter() {
//											@Override
//											public void afterCommit() {
//												// 使用异步服务发送消息
//												// 推送数据到redis队列里进行计算。
//
////												publisher.publishEventAsync(nt.getEventContent(), transactionCode);
//											}
//										});

//								System.out.println("update time ok：" + path + "\\" + file.getFileName());

							}
						} catch (TimeoutException | InterruptedException | ExecutionException e) {
							future.cancel(true);
//				            throw new RuntimeException("解析MP4时长超时", e);
							Path paths = Paths.get(path + "\\" + file.getFileName());
							try {
								Files.delete(paths);
							} catch (Exception e3) {
								// System.err.println("无法删除2");

								// TODO Auto-generated catch block
//								e.printStackTrace();
							} // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}

							return FileVisitResult.CONTINUE;

						} finally {
							executor.shutdown();
						}

//						try (IsoFile isoFile = new IsoFile(path + "\\" + file.getFileName())) {
//							System.out.println("begin get time....：" + path + "\\" + file.getFileName());
//							long tm = isoFile.getMovieBox().getMovieHeaderBox().getDuration()
//									/ isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
//							String roomidString = file.getFileName().toString().substring(0, indexa);
//							System.out.println("update time：" + path + "\\" + file.getFileName());
//							roomService.updateDuration(roomidString, (tm / 60) + ":" + (tm % 60));
//							System.out.println("update time ok：" + path + "\\" + file.getFileName());
//
//						} catch (Exception e2) {
//							System.out.println(e2.getMessage() + ":" + path + "\\" + file.getFileName());
//							if ((e2.getMessage() + "").contentEquals("null")) {
//
//								Path paths = Paths.get(path + "\\" + file.getFileName());
//								try {
//									Files.delete(paths);
//								} catch (IOException e) {
//									 
//									e.printStackTrace();
//								}  
//								return FileVisitResult.CONTINUE;
//
//							}
//							 
//						}
					}

					String line = file.getFileName().toString().substring(0,
							file.getFileName().toString().lastIndexOf("."));
//					System.out.println(line);

					String key = line;

					if (line.indexOf("作品id_") != -1) {
						int index = line.indexOf("作品id_");

						int endindex = line.indexOf("_昵称");
						key = line.substring(index, endindex);

					}
					if (line.indexOf("作品id--") != -1) {
						int index = line.indexOf("作品id--");

						int endindex = line.indexOf("_昵称");
						key = line.substring(index, endindex);
					}

					if (key == null)
						return FileVisitResult.CONTINUE;
					else {

//						System.out.println("key:" + key);
						executor.submit(new DownVid(key, file.toString()));

//						key = FileNameSanitizer.sanitize(key);
//						executor.submit(new DownVid(key, file.toString()));

						// 检测key，如果存在网盘文件，删除磁盘文件，不上传。

						if (line.indexOf("作品id_") != -1) {
							String oldKeyString = key.replace("作品id_", "作品id--").replace("用户id_", "用户id--");

							executor.submit(new DownVid(oldKeyString, file.toString()));

						}

					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
					// 如果需要，可以在这里处理目录结束后的操作
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					// 处理文件访问失败的情况
					exc.printStackTrace();
					return FileVisitResult.CONTINUE;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	private class DownVid implements Runnable {
		private final String rtmpUrl;
		private final String ffmpegPath;

		public DownVid(String j, String fp) {
			this.rtmpUrl = j;
			this.ffmpegPath = fp;
			// "D:\\360Downloads\\Software\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg"; //
			// 替换为实际路径
		}

		@Override
		public void run() {

			try {
				StringBuffer m3u8Result = new StringBuffer();

				String articleUrlString = rtmpUrl + "";

				// 运行完，打开这里也测一下。
				// articleUrlString = FileNameSanitizer.sanitize(articleUrlString);
				try {
//					System.out.println(articleUrlString);
					articleUrlString = URLEncoder.encode(articleUrlString, "UTF-8");

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// attr("href").replace("/cn/movie/", "");
				articleUrlString = "https://drive-pc.quark.cn/1/clouddrive/file/search?pr=ucpro&fr=pc&uc_param_str=&q="
						+ articleUrlString
						+ "&_page=1&_size=50&_fetch_total=1&_sort=file_type:desc,updated_at:desc&_is_hl=1";

				String url = articleUrlString;
//				System.out.println(articleUrlString);
//				Response articleRes = Jsoup.connect(articleUrlString)
//						.header("Content-Type", "application/json; charset=utf-8")
//						.header("Cookie",
//								"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_335_2B_=1; _UP_D_=pc; _UP_30C_6A_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_TS_=sg1a749cec18934cec528de148c927b650d; _UP_E37_B7_=sg1a749cec18934cec528de148c927b650d; _UP_TG_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_F7E_8D_=smKZ6VuDKwki06CdVNYibEsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6XKu9ZLSu8ycXRgREMCL23vOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhutrRMcTa22fnM13pzR%2BsbbOVsg1OU1mtpMiYxCeyuGayxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; tfstk=gzASHfXjsuq5EQJOF6u41Oq-M45IbqlZPy_pSeFzJ_C89HTh0ayrE9oBhHKV2Qkl4nsB7eVF4U5-RHt9V9-B-4bdvHKCYLkq_UYlt6nwOflwrYgFMKR59k8AZwJexaeo_UYuk6nZbflaRPgEPgIpvTCAkNSYv6BRJqsARN2L2HCKlq_F7zURyTLAkN7R9MKd9qTAmwCdyHCKlEIcJjcQNZy52Ug1LlLt9fjMyiNL9QhhFGT7LWFpMa65XUIXPUOfPTsOpftFAQK2JQRlnYyltEJ6ApKt08SJCNtfQLi_NhtlJnQv42yhlpTps_X7JWLXlepNhIuts_1pHKC23xmAL_tWIGXqnmxBpERdGBnTGMCcc_WvOmh1ad8wtOd-NljVIwtfQLi_NhIzM5Ss7GeQlOVdlGujlJ2hmnCSVPteRDXRoZoElqZSKTQclGujlJ2heZb2zqgbVvf..; __pus=35cf49cd51a9e9a3b5a4ab7f621a8cbcAAR/o7P3QfbYG1Px1KT/QwUYh+iUw8cV8m0DRTMI1Swc/IsGuuZq2S7LeK8IF60izKDklwT9gCJdybihJh5Llnbb; __kp=cec3a650-7f82-11f0-9784-b7f85c90c87f; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; __puus=19982ead0765a7c18f6994c9d18e5988AATUWlRqG+XKfscM8UdeWGo27m3SQfo79IJKao7NGeo99SJOqONsJDu6cH3PObY3jXC0B0/Pa9m0olecgpBwAUsCPf7Bu7ldHSCedEvbSYL5spi2z3NBZl1bfxDM1dZ7QN44D0b6ANAFuOPiEDjyO580sak459ntjqr33ICtyEhb16t9h4U+2YYvL0TLU8x6O5j440OXHjrJ8aA699L+js7i")
//
//						.header("User-Agent",
//								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
//						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
//						.execute();

//				String sourceString = articleRes.body();
//				System.out.println(sourceString);
//				JSONObject jo = new JSONObject(sourceString);
				int total = 0;// jo.getJSONObject("metadata").getInt("_total");

//				System.out.println(rtmpUrl + "-m3u8:\t" + total + "\t" + articleUrlString);
				String vidString = rtmpUrl.substring(0, rtmpUrl.indexOf("_"));
				Waiwang2Video video = robotService.getVideo(vidString);
				String up = "z:\\录制中心\\" + rtmpUrl + ".mp4";
				if (total > 0) {
//					m3u8Result.append(rtmpUrl + "\t" + (total > 0 ? 1 : 0));
					// System.out.println("删除此文件：" + ffmpegPath);
//					new File(ffmpegPath).delete();

					// NIO API提供详细错误信息
					Path path = Paths.get(ffmpegPath);
					Files.delete(path); // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}

					// System.out.println("删除成功");
					// writer.write(m3u8Result.toString());
				} else if (noChineseByRegex(rtmpUrl) && rtmpUrl.indexOf("video_") == -1) {

					if (video != null) {
						try {
							String fnString = video.getTitle();
							fnString = FileNameSanitizer.sanitize(fnString);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
							String tmString = sdf.format(new Date());
							// 新文件名
							File newFile = new File(
									"z:\\录制中心\\" + vidString + "_" + fnString + "_" + tmString + ".mp4");
							File outputFile = new File("z:\\录制中心\\" + rtmpUrl + ".mp4");

							// 执行重命名
							boolean isRenamed = outputFile.renameTo(newFile);

							// 检查结果
							if (isRenamed) {
								// System.out.println("文件重命名成功！" + rtmpUrl + "-->" + newFile.getName());
							} else {
								// System.out.println("文件重命名失败1！" + rtmpUrl + "-->" + newFile.getName());
							}
//							video.setPantag("z:\\录制中心\\" + vidString + "_" + fnString + "_" + tmString + ".mp4");
//							roomService.updateLocation(vidString, video.getPantag());
							up = "z:\\录制中心\\" + vidString + "_" + fnString + "_" + tmString + ".mp4";

							String info = "@linyuan56" + "," + up + "," + vidString + "_" + fnString + "_" + tmString
									+ ".mp4" + "," + "bc" + video.getId() + "," + "135" + "," + video.getCover();
							System.out.println("------发送到队列：" + info);
							try {
//								jedisClient.lpush("videos", info);

							} catch (Exception e) {
								e.printStackTrace();
								// TODO: handle exception
							}
							robotService.updateLocation(vidString, up);

						} catch (Exception e) {
							e.printStackTrace();
							// System.out.println("重命名失败2" + rtmpUrl + "-->" + "z:\\录制中心\\" + vidString +
							// "_"
							// + video.getTitle() + "_" + System.currentTimeMillis() + ".mp4");
							// TODO: handle exception
						}

					}

				}
//				if (video != null && (video.getPantag() == null || video.getPantag().indexOf("http") == -1)) {
//					robotService.updateLocation(vidString, up);
//					// video = robotService.getVideo(vidString);
//
//				
//				}

			} catch (Exception e) {
				// e.printStackTrace();

				// writerErr.write(rtmpUrl);

			} finally {
			}

		}
	}

	public static boolean noChineseByChar(String str) {
		if (str == null)
			return true;
		for (char c : str.toCharArray()) {
			if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
				return false;
			}
		}
		return true;
	}

	public static boolean noChineseByRegex(String str) {
		return str == null || !Pattern.compile("[\u4e00-\u9fa5]").matcher(str).find();
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

}
