package cn.exrick.manager.service.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import cn.exrick.manager.service.huifang.taoluSignUtil;
import cn.hutool.json.JSONArray;

public class getWanwuPageForApi3vidTaolu {
	private static final ExecutorService executor = Executors.newFixedThreadPool(1);
	private static final int BATCH_SIZE = 1; // 分批处理量
	static AtomicInteger processed = new AtomicInteger(0);
	static ThreadSafeFileWriter writer = null;
	static ThreadSafeFileWriter writerold = null;
	static ThreadSafeFileWriter writerHistory = null;
	static ThreadSafeFileWriter writerLog = null;
	static ThreadSafeFileWriter writerErr = null;
	static Set<String> urlSet = new HashSet<String>();

	// 假设已初始化包含100万链接的列表

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();
		List<String> millionLinks = new ArrayList();

		List<String> lines = new ArrayList<String>();
		Path filePath = Paths.get("d:/download/taolu8.txt");
		// "d:/download/taoluUser.txt"
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
			writerold = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/selfold.txt");

//			writer = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/self.txt");

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			writerLog = new ThreadSafeFileWriter("d:/download/downloadlog.txt");
			writerHistory = new ThreadSafeFileWriter("d:/download/history.txt");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Path filePathHistory = Paths.get("d:/download/history.txt");
		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				urlSet.add(his);
			}

		} catch (IOException e) {
			e.printStackTrace();
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
			Calendar calendar = Calendar.getInstance();
			int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH) - 1;
			System.out.println("day:" + dayofmonth);
			dayofmonth = 0;// 0循环到30就行。
			int begin = 70;// 70
			for (int j = 0; j < lines.size(); j++) {
				millionLinks.add(lines.get(j).replace("\"", ""));
			}
			int total = millionLinks.size();

			for (int i = 0; i < millionLinks.size(); i += BATCH_SIZE) {

				List<String> batch = millionLinks.subList(i, Math.min(i + BATCH_SIZE, millionLinks.size()));
				int end = Math.min(i + BATCH_SIZE, total);
				CountDownLatch batchLatch = new CountDownLatch(batch.size());
				System.out.println("批次开始: " + i + "-" + end);
//				FileUtil.appendUtf8String((vidbegin + i) + "\r\n", "d:/download/downlog.txt");
//				writerLog.write((vidbegin + i) + "");
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

//				String articleUrlString = rtmpUrl + "";
//				try {
//					articleUrlString = URLEncoder.encode(articleUrlString, "UTF-8");
////					System.out.println(articleUrlString);
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				try {
//					// live.taolu.black
//					String userInfoRes = getWanwuPageForApi3Journery3
//							.getRes("http://testlive.yueji.pro/user/userInfo/getUserInfo?id=" + rtmpUrl
//									+ "&sign=1750861480-2bd96cf95f374fd3bfab9a67654db497-0-f335c43428b060927c17dfbf979c6de0&uid=228316&systemModel=Redmi%20Note%207%20Pro&appType=1&appVer=3.8.4&phoneBrand=xiaomi&version=3.8.4&deviceId=3251ab06-3320-4e70-b6a3-f796e4e7223a&systemVersion=10&versionCode=20250528");
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}

//				

				// 1910
				for (int k = 1;; k++) {
//					if (urlSet.contains(rtmpUrl)) {
//						System.out.println("----------------------------跳过：" + rtmpUrl + "\t" + k);
//						break;
//					}

					Thread.sleep(5000);
					// http://testlive.yueji.pro/user/plot/video/list/1/10
					// ?sign=1763191643-279660445ed9420aac3c2c905e316bdb-0-935ec145c35e822d52467f7502f06e60&uid=526495&systemModel=Redmi%205%20Plus&appType=1&appVer=3.9.1&phoneBrand=xiaomi&version=3.9.1&deviceId=186d155c-1555-4559-86de-f8480f9081f7&systemVersion=8.1.0&versionCode=20250715

					String pU = "http://testlive.yueji.pro/live/live/video/anchor/" + k + "/10?anchorUserId=" + rtmpUrl
							+ "&"
							+ (taoluSignUtil.md5("http://testlive.yueji.pro/live/live/video/anchor/" + k + "/10")
									.replace("?", ""))
							+ "&uid=435407&systemModel=Redmi%205%20Plus&appType=1&appVer=3.9.1&phoneBrand=xiaomi&version=3.9.1&deviceId=186d155c-1555-4559-86de-f8480f9081f7&systemVersion=8.1.0&versionCode=20250715";

//					String pU = "http://testlive.yueji.pro/user/plot/video/list/" + k + "/10" + "?"
//							+ (taoluSignUtil.md5("http://testlive.yueji.pro/user/plot/video/list/" + k + "/10")
//									.replace("?", ""))
//							+ "&uid=526495&systemModel=Redmi%205%20Plus&appType=1&appVer=3.9.1&phoneBrand=xiaomi&version=3.9.1&deviceId=186d155c-1555-4559-86de-f8480f9081f7&systemVersion=8.1.0&versionCode=20250715";

//					String res = HttpUtil.get(pU);
					System.out.println(pU);
					try {

						// 提取res中的链接
						String docJson = getWanwuPageForApi3Journery3.getRes(pU);
						System.out.println(docJson);
//						Thread.sleep(500);
						// Jsoup.connect(pU).ignoreContentType(true).execute().body();
						cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
						JSONArray links = object.getJSONObject("data").getJSONArray("records");
//					Elements links = doc.select("a[href^=\"/cn/movie\"]");
						if (links.size() == 0)
							break;

						for (int j = 0; j < links.size(); j++) {
							StringBuffer m3u8Result = new StringBuffer();

							cn.hutool.json.JSONObject link = links.getJSONObject(j);
//						System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//						System.out.println("text: " + link.text()); // 输出链接文本

//						if (keywordString != null) {
//							String titleDb = link.getStr("videoTitle");
//							if (titleDb.indexOf(keywordString) == -1)
//								continue;
//						}

							String articleUrlString = link.getStr("id");

							if (urlSet.contains(articleUrlString)) {
								System.out.println("----------------------------跳过：" + articleUrlString);

								System.out.println("------------------");
//								System.out.println(sourceString);
//								JSONObject jsonObject = link;
								// new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//								int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//								int endIndex = jsonString.indexOf("'", startIndex);

								// 提取URL
								String videoUrl = link.getStr("videoUrl");
								// JSONObject ui = new JSONObject(userInfoRes).getJSONObject("data");
								String vid = "作品id--" + link.getStr("id") + "_用户id--" + rtmpUrl + "_昵称--"
										+ link.getStr("nickName");

								String img = link.getStr("converImage") == null ? link.getStr("coverImage")
										: link.getStr("converImage");

								System.out.println(k + "-" + j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

								m3u8Result.append(link.getStr("videoTitle") + "_" + vid + "\t" + videoUrl + "\t"
										+ link.getStr("id") + "\t" + img + "\tsk" + "\t" + rtmpUrl + "\t"
										+ link.getStr("addTime"));
								String str = m3u8Result.toString();
								writerold.write(str);

								continue;
							}
							// attr("href").replace("/cn/movie/", "");
//						articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
//								+ articleUrlString;
//						// http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id=47617
							//
//						Response articleRes = Jsoup.connect(articleUrlString)
//								.header("Content-Type", "application/json; charset=utf-8")
//								.header("Cookie", "PHPSESSID=s8ui3qddpcbmbeotptj2alfcnd").header("Host", "127.0.0.1:54545")
//								.header("User-Agent",
//										"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
//								.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
//								.execute();

//						String sourceString = articleRes.body();
							System.out.println("------------------");
//						System.out.println(sourceString);
//						JSONObject jsonObject = link;
							// new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//						int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//						int endIndex = jsonString.indexOf("'", startIndex);

							// 提取URL
							String videoUrl = link.getStr("videoUrl");
							// JSONObject ui = new JSONObject(userInfoRes).getJSONObject("data");
							String vid = "作品id--" + link.getStr("id") + "_用户id--" + rtmpUrl + "_昵称--"
									+ link.getStr("nickName");

							String img = link.getStr("converImage") == null ? link.getStr("coverImage")
									: link.getStr("converImage");

							System.out.println(k + "-" + j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

							m3u8Result.append(
									link.getStr("videoTitle") + "_" + vid + "\t" + videoUrl + "\t" + link.getStr("id")
											+ "\t" + img + "\tsk" + "\t" + rtmpUrl + "\t" + link.getStr("addTime"));
							String str = m3u8Result.toString();
							writer.write(str);
							writerHistory.write(articleUrlString);

//						}

						}

//						writerHistory.write(rtmpUrl);

					} catch (Exception e) {
						k--;
						continue;
//						writerErr.write(rtmpUrl);
//						break;

						// TODO: handle exception
					}

				}

			} catch (Exception e) {

			} finally {
				countDownLatch.countDown();
			}

		}
	}
}
