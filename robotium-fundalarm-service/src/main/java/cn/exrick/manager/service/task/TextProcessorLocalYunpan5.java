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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.exrick.common.utils.CommonUtils;
import cn.exrick.manager.pojo.Taolu3Video;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.pojo.WaiwangVideo;
import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.pojo.ZmqVideo;
import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.huifang.TimestampEncryptor;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;

//获取所有上传的文件，小飞机网盘
//@Component
public class TextProcessorLocalYunpan5 {

	private static ExecutorService executor = Executors.newFixedThreadPool(1);
//	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
	static ThreadSafeFileWriter writer = null;
	static ThreadSafeFileWriter writerHistory = null;
	static Set<String> urlSet = new HashSet<String>();
	@Reference
	RobotService robotService;

	static String token = "b3584d1133ec650194574416b220f1ce58833de3ea1a5468c2e42abc28cc7624ed2e15b5eac8803c1560cc42b8a55109";

//	static {
//
//		roomService = context.getBean(RobotService.class);
//
//	}
	@Scheduled(cron = "0 */2 * * * ?")

	public void updateLoc() {
		// 178
		urlSet.clear();
		try {
			writer = new ThreadSafeFileWriter("d:/download/xfj.txt");

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			token = CommonUtils.getProperties("token");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("token:" + token);

		try {
			writerHistory = new ThreadSafeFileWriter("d:/download/historyxfj.txt");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Path filePathHistory = Paths.get("d:/download/historyxfj.txt");
		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				urlSet.add(his);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			listFile("0");
			listFile("81531815");
			listFile("793423011");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

//		while (true) {
//			doWith("f:\\Downloads");
//			doWith("f");
//
//			doWith("f:\\录制中心");
//			executor.shutdown();
//			try {
//				Thread.sleep(10000);
//				executor = Executors.newFixedThreadPool(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}

	}

	public static void doWith(String path) {

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

	public void listFile(String fid) throws IOException {

		for (int i = 1; i <= 2; i++) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String hexTimestamp = TimestampEncryptor.encryptTimestamp();

			String articleUrlString = "https://api.feijipan.com/app/record/file/list?uuid=4b7c37a5-98ec-4b89-916e-a0e56b24bf38&devType=6&devCode=4b7c37a5-98ec-4b89-916e-a0e56b24bf38&devModel=chrome&devVersion=109&appVersion=&timestamp="
					+ hexTimestamp + "&appToken=" + token + "&extra=2&offset=" + i + "&limit=110&folderId=" + fid
					+ "&type=0";

			String url = articleUrlString;
			System.out.println(articleUrlString);
			Response articleRes = Jsoup.connect(articleUrlString)
					.header("Content-Type", "application/json; charset=utf-8")
//				.header("Cookie",
//						"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_335_2B_=1; _UP_D_=pc; _UP_30C_6A_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_TS_=sg1a749cec18934cec528de148c927b650d; _UP_E37_B7_=sg1a749cec18934cec528de148c927b650d; _UP_TG_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_F7E_8D_=smKZ6VuDKwki06CdVNYibEsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6XKu9ZLSu8ycXRgREMCL23vOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhutrRMcTa22fnM13pzR%2BsbbOVsg1OU1mtpMiYxCeyuGayxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; tfstk=gzASHfXjsuq5EQJOF6u41Oq-M45IbqlZPy_pSeFzJ_C89HTh0ayrE9oBhHKV2Qkl4nsB7eVF4U5-RHt9V9-B-4bdvHKCYLkq_UYlt6nwOflwrYgFMKR59k8AZwJexaeo_UYuk6nZbflaRPgEPgIpvTCAkNSYv6BRJqsARN2L2HCKlq_F7zURyTLAkN7R9MKd9qTAmwCdyHCKlEIcJjcQNZy52Ug1LlLt9fjMyiNL9QhhFGT7LWFpMa65XUIXPUOfPTsOpftFAQK2JQRlnYyltEJ6ApKt08SJCNtfQLi_NhtlJnQv42yhlpTps_X7JWLXlepNhIuts_1pHKC23xmAL_tWIGXqnmxBpERdGBnTGMCcc_WvOmh1ad8wtOd-NljVIwtfQLi_NhIzM5Ss7GeQlOVdlGujlJ2hmnCSVPteRDXRoZoElqZSKTQclGujlJ2heZb2zqgbVvf..; __pus=35cf49cd51a9e9a3b5a4ab7f621a8cbcAAR/o7P3QfbYG1Px1KT/QwUYh+iUw8cV8m0DRTMI1Swc/IsGuuZq2S7LeK8IF60izKDklwT9gCJdybihJh5Llnbb; __kp=cec3a650-7f82-11f0-9784-b7f85c90c87f; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; __puus=19982ead0765a7c18f6994c9d18e5988AATUWlRqG+XKfscM8UdeWGo27m3SQfo79IJKao7NGeo99SJOqONsJDu6cH3PObY3jXC0B0/Pa9m0olecgpBwAUsCPf7Bu7ldHSCedEvbSYL5spi2z3NBZl1bfxDM1dZ7QN44D0b6ANAFuOPiEDjyO580sak459ntjqr33ICtyEhb16t9h4U+2YYvL0TLU8x6O5j440OXHjrJ8aA699L+js7i")

					.header("User-Agent",
							"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
					.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
					.execute();

			String sourceString = articleRes.body();
//			System.out.println(sourceString);
			cn.hutool.json.JSONObject jo = new cn.hutool.json.JSONObject(sourceString);
			cn.hutool.json.JSONArray list = jo.getJSONArray("list");
			if (list.size() == 0)
				break;
			for (int j = 0; j < list.size(); j++) {
				System.out.println();
				cn.hutool.json.JSONObject link = list.getJSONObject(j);
				if (!link.containsKey("fileName"))
					continue;
				String ff = link.getStr("fileId");
				link.remove("parentName");
				// 删除磁盘文件
				File file = new File("z:/录制中心/" + link.getStr("fileName"));

				try {
					boolean re = file.delete();
//					if (re)
//						System.out.println("成功删除：" + link.getStr("fileName"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (urlSet.contains(ff))
					continue;
				System.out.println("----------小飞机网盘文件------------>：" + link.getStr("fileName"));

				StringBuffer m3u8Result = new StringBuffer();
				for (String key : link.keySet()) {
//					System.out.print(key + ",");
					m3u8Result.append(link.getStr(key) + "\t");

				}
				// 开始分享文件，获取分享链接：
				String shareUrl = share(ff);
				System.out.println("shareUrl:" + shareUrl);
				if (shareUrl == null) {
					System.out.println("分享失败");

					continue;
				}
//				if(shareUrl.contentEquals(""))
//				{
//					
//				}
				m3u8Result.append(shareUrl);

//				System.out.println();
//				m3u8Result.append("1");
				String str = m3u8Result.toString();

				writer.write(str);
				writerHistory.write(ff);
				try {
					// 分享链接更新到数据库：
					int fin = link.getStr("fileName").indexOf("_");

					int bc = 0;
					if (fin != -1) {
						try {
							int intbc = Integer.parseInt(link.getStr("fileName").substring(0, fin));
							bc = 1;
						} catch (Exception e) {

							// TODO: handle exception
						}

					}
					if (bc == 0) {
						String vid;
						// 提取淘露 vid，定位表进行更新url:
						if (link.getStr("fileName").indexOf("___tl") != -1) {
							int index = link.getStr("fileName").indexOf("___tl");
//						String vid = extractWorkId(link.getStr("fileName"));
//						if (vid == null)
//							vid = extractWorkId2(link.getStr("fileName"));
							vid = link.getStr("fileName").substring(index + 5).replace(".mp4", "");
							;

							// 应该是淘露作品。
							Taolu3Video video = robotService.getVideotl(vid);
							if (video != null) {
								// 更新数据库记录
								if (video.getTria() == null || video.getTria().indexOf("http") == -1) {
									robotService.updateLocationtl(vid, shareUrl);
									System.out.println("更新链接ok：" + vid + "----->" + shareUrl);

								}

							}

						}

						if (link.getStr("fileName").indexOf("___tg") != -1) {

							int index = link.getStr("fileName").indexOf("___tg");
//							String vid = extractWorkId(link.getStr("fileName"));
//							if (vid == null)
//								vid = extractWorkId2(link.getStr("fileName"));
							vid = link.getStr("fileName").substring(index + 5).replace(".mp4", "");

							// 定位电报数据记录
							WaiwangVideo video = robotService.getVideoTG(vid);
							if (video != null) {
								// 更新数据库记录
								if (video.getAuthor() == null || video.getAuthor().indexOf("http") == -1) {
									robotService.updateLocationTG(vid, shareUrl);
									System.out.println("更新链接ok：" + vid + "----->" + shareUrl);

								}

							}

						}

						if (link.getStr("fileName").indexOf("___ww") != -1) {
							int index = link.getStr("fileName").indexOf("___ww");
//							String vid = extractWorkId(link.getStr("fileName"));
//							if (vid == null)
//								vid = extractWorkId2(link.getStr("fileName"));
							vid = link.getStr("fileName").substring(index + 5).replace(".mp4", "");
							;

							// 定位电报数据记录
							WanwuVideo video = robotService.getVideoWw(vid);
							if (video != null) {
								// 更新数据库记录
								if (video.getTria() == null || video.getTria().indexOf("http") == -1) {
									robotService.updateLocationww(vid, shareUrl);
									System.out.println("更新链接ok：" + vid + "----->" + shareUrl);

								}

							}
						}

						if (link.getStr("fileName").indexOf("___zm") != -1) {
							int index = link.getStr("fileName").indexOf("___zm");
//							String vid = extractWorkId(link.getStr("fileName"));
//							if (vid == null)
//								vid = extractWorkId2(link.getStr("fileName"));
							vid = link.getStr("fileName").substring(index + 5).replace(".mp4", "");
							;

							// 定位电报数据记录
							ZmqVideo video = robotService.getVideoZmq(vid);
							if (video != null) {
								// 更新数据库记录
								if (video.getTria() == null || video.getTria().indexOf("http") == -1) {
									robotService.updateLocationzmq(vid, shareUrl);
									System.out.println("更新链接ok：" + vid + "----->" + shareUrl);

								}

							}
						}

					} else {
						int index = link.getStr("fileName").indexOf("_");
						if (index != -1) {
							String vid = link.getStr("fileName").substring(0, index);
							Waiwang2Video video = robotService.getVideo(vid);
							if (video != null) {
								// 更新数据库记录
								if (video.getPantag() == null || video.getPantag().indexOf("http") == -1) {
									robotService.updateLocation(vid, shareUrl);
									System.out.println("更新链接ok：" + vid + "----->" + shareUrl);

								}

							}

						}
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}

	}

	public static String extractWorkId(String input) {
		String regex = "作品id_(\\d+)_用户id";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	public static String extractWorkId2(String input) {
		String regex = "作品id--(\\d+)_用户id";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static class DownVid implements Runnable {
		private String rtmpUrl;
		private final String ffmpegPath;

		public DownVid(String j, String fp) {
			this.rtmpUrl = j;
			if (this.rtmpUrl.indexOf("_") != -1) {
				int index = this.rtmpUrl.indexOf("_");
				this.rtmpUrl = this.rtmpUrl.substring(0, index);
			}
			this.ffmpegPath = fp;
			// "D:\\360Downloads\\Software\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg"; //
			// 替换为实际路径
		}

		@Override
		public void run() {

			long timestamp = System.currentTimeMillis();
			String hexTimestamp = TimestampEncryptor.encryptTimestamp();
			// Long.toHexString(timestamp) + "6da346511fb1c2395087e725";
			// System.out.println("16进制时间戳: " + hexTimestamp); // 输出如"18e0b7c6f3a"

			try {
				StringBuffer m3u8Result = new StringBuffer();

				String articleUrlString = rtmpUrl + "";

				// 运行完，打开这里也测一下。
				// articleUrlString = FileNameSanitizer.sanitize(articleUrlString);
				try {
					articleUrlString = URLEncoder.encode(articleUrlString, "UTF-8");
//					System.out.println(articleUrlString);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// attr("href").replace("/cn/movie/", "");
				articleUrlString = "https://api.feijipan.com/app/record/search/list?uuid=371926b6-e577-491f-90a9-b5bb04195deb&devType=6&devCode=371926b6-e577-491f-90a9-b5bb04195deb&devModel=chrome&devVersion=109&appVersion=&timestamp="
						+ hexTimestamp
						+ "&appToken=cdbe396348b302ff4bc7305b4bd3a8e5fbf9218b8e63ed15b1711bb87500c74df79348c9ba29f29c9aa84e67e93648a4&extra=2&offset=1&limit=60&search="
						+ articleUrlString + "&folderId=0&type=0";

				String url = articleUrlString;
//				System.out.println(articleUrlString);
				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
//						.header("Cookie",
//								"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_335_2B_=1; _UP_D_=pc; _UP_30C_6A_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_TS_=sg1a749cec18934cec528de148c927b650d; _UP_E37_B7_=sg1a749cec18934cec528de148c927b650d; _UP_TG_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_F7E_8D_=smKZ6VuDKwki06CdVNYibEsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6XKu9ZLSu8ycXRgREMCL23vOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhutrRMcTa22fnM13pzR%2BsbbOVsg1OU1mtpMiYxCeyuGayxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; tfstk=gzASHfXjsuq5EQJOF6u41Oq-M45IbqlZPy_pSeFzJ_C89HTh0ayrE9oBhHKV2Qkl4nsB7eVF4U5-RHt9V9-B-4bdvHKCYLkq_UYlt6nwOflwrYgFMKR59k8AZwJexaeo_UYuk6nZbflaRPgEPgIpvTCAkNSYv6BRJqsARN2L2HCKlq_F7zURyTLAkN7R9MKd9qTAmwCdyHCKlEIcJjcQNZy52Ug1LlLt9fjMyiNL9QhhFGT7LWFpMa65XUIXPUOfPTsOpftFAQK2JQRlnYyltEJ6ApKt08SJCNtfQLi_NhtlJnQv42yhlpTps_X7JWLXlepNhIuts_1pHKC23xmAL_tWIGXqnmxBpERdGBnTGMCcc_WvOmh1ad8wtOd-NljVIwtfQLi_NhIzM5Ss7GeQlOVdlGujlJ2hmnCSVPteRDXRoZoElqZSKTQclGujlJ2heZb2zqgbVvf..; __pus=35cf49cd51a9e9a3b5a4ab7f621a8cbcAAR/o7P3QfbYG1Px1KT/QwUYh+iUw8cV8m0DRTMI1Swc/IsGuuZq2S7LeK8IF60izKDklwT9gCJdybihJh5Llnbb; __kp=cec3a650-7f82-11f0-9784-b7f85c90c87f; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; __puus=19982ead0765a7c18f6994c9d18e5988AATUWlRqG+XKfscM8UdeWGo27m3SQfo79IJKao7NGeo99SJOqONsJDu6cH3PObY3jXC0B0/Pa9m0olecgpBwAUsCPf7Bu7ldHSCedEvbSYL5spi2z3NBZl1bfxDM1dZ7QN44D0b6ANAFuOPiEDjyO580sak459ntjqr33ICtyEhb16t9h4U+2YYvL0TLU8x6O5j440OXHjrJ8aA699L+js7i")

						.header("User-Agent",
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
						.execute();

				String sourceString = articleRes.body();
//				System.out.println(sourceString);
				JSONObject jo = new JSONObject(sourceString);
				int total = jo.getInt("total");
//				JSONArray ja = jo.getJSONArray("list");

//				System.out.println(rtmpUrl + "-m3u8:\t" + total + "\t" + articleUrlString);

				if (total > 0) {

//					 for (int k=0;k<ja.length();k++) {
//						 String fnString=ja.getJSONObject(k).getString("fileName");
//						 if(fnString.contentEquals(""))
//						
//					}
//					m3u8Result.append(rtmpUrl + "\t" + (total > 0 ? 1 : 0));
//					System.out.println("删除此文件：" + ffmpegPath);
//					new File(ffmpegPath).delete();

					// NIO API提供详细错误信息
					Path path = Paths.get(ffmpegPath);
					Files.delete(path); // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}

//					System.out.println("删除成功");
					// writer.write(m3u8Result.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();

				// writerErr.write(rtmpUrl);

			} finally {
			}

		}
	}

	public static String share(String id) {

		long timestamp = System.currentTimeMillis();
		String hexTimestamp = TimestampEncryptor.encryptTimestamp();
		// Long.toHexString(timestamp) + "6da346511fb1c2395087e725";
//		System.out.println("16进制时间戳: " + hexTimestamp); // 输出如"18e0b7c6f3a"

		try {
			StringBuffer m3u8Result = new StringBuffer();

//			String articleUrlString = rtmpUrl + "";
//
//			// 运行完，打开这里也测一下。
//			// articleUrlString = FileNameSanitizer.sanitize(articleUrlString);
//			try {
//				articleUrlString = URLEncoder.encode(articleUrlString, "UTF-8");
////				System.out.println(articleUrlString);
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			// attr("href").replace("/cn/movie/", "");
			String articleUrlString = "https://api.feijipan.com/app/share/url?uuid=371926b6-e577-491f-90a9-b5bb04195deb&devType=6&devCode=371926b6-e577-491f-90a9-b5bb04195deb&devModel=chrome&devVersion=109&appVersion=&timestamp="
					+ hexTimestamp + "&appToken=" + token + "&extra=2";

			String url = articleUrlString;
//			System.out.println(articleUrlString);
			Document articleRes = Jsoup.connect(articleUrlString)
//					origin: https://www.feijipan.com
//						referer: https://www.feijipan.com/
					.header("origin", "https://www.feijipan.com").header("referer", "https://www.feijipan.com/")
					.header("Content-Type", "application/json; charset=utf-8")
//					.header("Cookie",
//							"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_335_2B_=1; _UP_D_=pc; _UP_30C_6A_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_TS_=sg1a749cec18934cec528de148c927b650d; _UP_E37_B7_=sg1a749cec18934cec528de148c927b650d; _UP_TG_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_F7E_8D_=smKZ6VuDKwki06CdVNYibEsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6XKu9ZLSu8ycXRgREMCL23vOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhutrRMcTa22fnM13pzR%2BsbbOVsg1OU1mtpMiYxCeyuGayxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; tfstk=gzASHfXjsuq5EQJOF6u41Oq-M45IbqlZPy_pSeFzJ_C89HTh0ayrE9oBhHKV2Qkl4nsB7eVF4U5-RHt9V9-B-4bdvHKCYLkq_UYlt6nwOflwrYgFMKR59k8AZwJexaeo_UYuk6nZbflaRPgEPgIpvTCAkNSYv6BRJqsARN2L2HCKlq_F7zURyTLAkN7R9MKd9qTAmwCdyHCKlEIcJjcQNZy52Ug1LlLt9fjMyiNL9QhhFGT7LWFpMa65XUIXPUOfPTsOpftFAQK2JQRlnYyltEJ6ApKt08SJCNtfQLi_NhtlJnQv42yhlpTps_X7JWLXlepNhIuts_1pHKC23xmAL_tWIGXqnmxBpERdGBnTGMCcc_WvOmh1ad8wtOd-NljVIwtfQLi_NhIzM5Ss7GeQlOVdlGujlJ2hmnCSVPteRDXRoZoElqZSKTQclGujlJ2heZb2zqgbVvf..; __pus=35cf49cd51a9e9a3b5a4ab7f621a8cbcAAR/o7P3QfbYG1Px1KT/QwUYh+iUw8cV8m0DRTMI1Swc/IsGuuZq2S7LeK8IF60izKDklwT9gCJdybihJh5Llnbb; __kp=cec3a650-7f82-11f0-9784-b7f85c90c87f; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; __puus=19982ead0765a7c18f6994c9d18e5988AATUWlRqG+XKfscM8UdeWGo27m3SQfo79IJKao7NGeo99SJOqONsJDu6cH3PObY3jXC0B0/Pa9m0olecgpBwAUsCPf7Bu7ldHSCedEvbSYL5spi2z3NBZl1bfxDM1dZ7QN44D0b6ANAFuOPiEDjyO580sak459ntjqr33ICtyEhb16t9h4U+2YYvL0TLU8x6O5j440OXHjrJ8aA699L+js7i")
					.requestBody("{\"shareId\":\"\",\"fileIds\":\"" + id
							+ "\",\"folderIds\":\"\",\"code\":\"\",\"amt\":\"\",\"term\":0,\"showRecommend\":0,\"showUpTime\":1,\"showDownloads\":1,\"showComments\":1,\"showStars\":1,\"showLikes\":1}")
					.header("User-Agent",
							"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
					.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true).post();

			String sourceString = articleRes.body().text().toString();
//			System.out.println(sourceString);
			JSONObject jo = new JSONObject(sourceString);
			return jo.getString("shareUrl");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// writerErr.write(rtmpUrl);

		} finally {
		}

	}
}
