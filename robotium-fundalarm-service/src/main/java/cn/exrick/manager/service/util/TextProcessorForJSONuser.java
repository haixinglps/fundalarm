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

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class TextProcessorForJSONuser {
	private static final ExecutorService executor = Executors.newFixedThreadPool(1);
	private static final int BATCH_SIZE = 100; // 分批处理量
	static AtomicInteger processed = new AtomicInteger(0);
	static ThreadSafeFileWriter writer = null;
	static ThreadSafeFileWriter writerelse = null;
	static ThreadSafeFileWriter writerHistory = null;
	static ThreadSafeFileWriter writerLog = null;
	static ThreadSafeFileWriter writerErr = null;
	static ThreadSafeFileWriter writerOlder = null;

	static Set<String> urlSet = new HashSet<String>();

	// 假设已初始化包含100万链接的列表

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();
		List<String> millionLinks = new ArrayList();

		List<String> lines = new ArrayList<String>();
		Path filePath = Paths.get("F:\\BaiduNetdiskDownload\\userinfo.txt");
		try {

			lines = Files.readAllLines(filePath);
//            for (String line : lines) {
//                System.out.println(line);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/self2.txt");
			writerelse = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/self3.txt");
			writerOlder = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/selfold.txt");

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			writerLog = new ThreadSafeFileWriter("d:/download/downloadlog2.txt");
			writerHistory = new ThreadSafeFileWriter("d:/download/history2.txt");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Path filePathHistory = Paths.get("d:/download/history2.txt");
		try {
			List<String> linesHistory = Files.readAllLines(filePathHistory);
			for (String his : linesHistory) {
				urlSet.add(his);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writerErr = new ThreadSafeFileWriter("d:/download/downloaderror2.txt");
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
				String itemString = lines.get(j);
				if (itemString != null && !itemString.contentEquals(""))
					millionLinks.add(itemString);// .replace("\"", "")
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

//					if (urlSet.contains(rtmpUrl)) {
//						System.out.println("----------------------------跳过：" + rtmpUrl + "\t" + k);
//						break;
//					}

//					String pU = "http://testlive.yueji.pro/live/live/video/anchor/" + k + "/10?anchorUserId=" + rtmpUrl
//							+ "&"
//							+ (taoluSignUtil.md5("http://testlive.yueji.pro/live/live/video/anchor/" + k + "/10")
//									.replace("?", ""))
//							+ "&uid=228316&systemModel=Redmi%205%20Plus&appType=1&appVer=3.9.1&phoneBrand=xiaomi&version=3.9.1&deviceId=186d155c-1555-4559-86de-f8480f9081f7&systemVersion=8.1.0&versionCode=20250715";
//
////					String res = HttpUtil.get(pU);
//					System.out.println(pU);

				// 提取res中的链接
				String docJson = rtmpUrl;

				// System.out.println("[" + docJson + "]");
				if (docJson.indexOf("subTypeCode") != -1)
					return;
//						Thread.sleep(500);
				// Jsoup.connect(pU).ignoreContentType(true).execute().body();
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				String userInfoString = null;
				String userkeyString = null;
				for (String key : object.keySet()) {
//					System.out.println("Key: " + key);
					if (key.startsWith("off_24")) {
						userInfoString = object.getStr(key);
						userkeyString = key;
						break;
					}
				}

				if (userInfoString == null)
					return;
				JSONObject userObj = object.getJSONObject(userkeyString);

				// System.out.println("userobject str:" + userObj.toString());
				if (!userObj.getJSONObject("data").containsKey("list"))
					return;
				JSONArray links = userObj.getJSONObject("data").getJSONArray("list");
//					Elements links = doc.select("a[href^=\"/cn/movie\"]");
//				if (userObj.toString().indexOf("\"data\":{\"balance\"") != -1
//						|| userObj.toString().indexOf("unreadLikeFavorites") != -1)
//
//					return;
				if (userObj.toString().indexOf("bindPhone") == -1)
					return;
				if (links == null) {

					StringBuffer m3u8Result = new StringBuffer();

					cn.hutool.json.JSONObject link = userObj.getJSONObject("data");
//						System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//						System.out.println("text: " + link.text()); // 输出链接文本

//						if (keywordString != null) {
//							String titleDb = link.getStr("videoTitle");
//							if (titleDb.indexOf(keywordString) == -1)
//								continue;
//						}

					String articleUrlString = link.getStr("userId");
					if (articleUrlString == null)
						return;

					if (urlSet.contains(articleUrlString)) {
						System.out.println("----------------------------跳过：" + rtmpUrl);
//								return;
						return;
					}
					if (!link.containsKey("bindPhone")) {
						return;
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

//						System.out.println(sourceString);
//						JSONObject jsonObject = link;
					// new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//						int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//						int endIndex = jsonString.indexOf("'", startIndex);

					for (String key : link.keySet()) {
						if (key.equals("bindPhone")) {

							System.out.println(link.getStr("nickName") + link.getStr(key));
						}
						m3u8Result.append(link.getStr(key) + "\t");
					}
					m3u8Result.append("1");
					String str = m3u8Result.toString();
					writerelse.write(str);

//						}
					writerHistory.write(articleUrlString);
					System.out.println("------------------");

				} else
					for (int j = 0; j < links.size(); j++) {
						StringBuffer m3u8Result = new StringBuffer();

						cn.hutool.json.JSONObject link = links.getJSONObject(j).getJSONObject("user");
//						System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//						System.out.println("text: " + link.text()); // 输出链接文本

//						if (keywordString != null) {
//							String titleDb = link.getStr("videoTitle");
//							if (titleDb.indexOf(keywordString) == -1)
//								continue;
//						}

						String articleUrlString = link.getStr("userId");
						if (articleUrlString == null)
							continue;

						if (urlSet.contains(articleUrlString)) {
							// System.out.println("----------------------------跳过：" + articleUrlString);

							for (String key : link.keySet()) {
								System.out.print(key + ",");
//								if (key.equals("bindPhone")) {
								//
//									System.out.println(link.getStr("nickName") + link.getStr(key));
//								}
								m3u8Result.append(link.getStr(key) + "\t");
							}
							// System.out.println();
							m3u8Result.append("1");
							String str = m3u8Result.toString();
//							writer.write(str);
							System.out.println("以前采集过这个人：" + articleUrlString);
							writerOlder.write(str);
							continue;
						}
						if (!link.containsKey("bindPhone")) {
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

//						System.out.println(sourceString);
//						JSONObject jsonObject = link;
						// new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//						int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//						int endIndex = jsonString.indexOf("'", startIndex);
						// System.out.println();
						for (String key : link.keySet()) {
							// System.out.print(key + ",");
//							if (key.equals("bindPhone")) {
//
//								System.out.println(link.getStr("nickName") + link.getStr(key));
//							}
							m3u8Result.append(link.getStr(key) + "\t");
						}
						// System.out.println();
						m3u8Result.append("1");
						String str = m3u8Result.toString();
						writer.write(str);

//						}
						writerHistory.write(articleUrlString);
						urlSet.add(articleUrlString);
						// System.out.println("------------------");
					}

			} catch (Exception e) {
				e.printStackTrace();
				writerErr.write(rtmpUrl);
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			} finally {
				countDownLatch.countDown();
			}

		}
	}
}
