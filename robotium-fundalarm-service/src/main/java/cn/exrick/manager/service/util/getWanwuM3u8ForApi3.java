package cn.exrick.manager.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.FileUtil;

public class getWanwuM3u8ForApi3 {

	public static void geneBat(String[] args, int disknum, int write) {
		List<String> list = new ArrayList<String>();
		List<String> listT = new ArrayList<String>();
		List<String> listUI = new ArrayList<String>();
		List<String> listImg = new ArrayList<String>();
		List<String> shikans = new ArrayList<String>();

		try {
			Path directoryPath = Paths.get("D:\\m3u8ListTaoluzhuboapi3"); // 替换为你的目录路径

			Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					// 如果需要，可以在这里处理目录
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					try (BufferedReader reader = Files.newBufferedReader(file)) {
						String line;
						while ((line = reader.readLine()) != null) {
							String[] parts = line.split("\t");
							if (parts.length > 1) {
								list.add(parts[1]);
								listT.add(parts[0].replace("?", "").replace("？", "").replace(":", "").replace("：", ""));
								listUI.add(parts[2]);
								listImg.add(parts[3]);
								shikans.add(parts[4]);

							}
						}
						FileUtil.del(file.toFile());
					} catch (IOException e) {
						e.printStackTrace();
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
//		HttpsUrlValidator.retrieveResponseFromServer("https://tlvod-cdn.chinaysfc.com/");
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int ccc = 0;
		for (String m3u8UrlString : list) {
			ccc += 1;
			// System.out.println("进度：" + ccc + "/" + list.size());
//			System.out.println("url:" + url)
			// System.out.println("url:" + m3u8UrlString);
			String url = m3u8UrlString;
			String[] vidStrings = url.split("/");
			// File file = new File("D:\\m3u8taoluapi\\" + vidStrings[4] + ".m3u8");
			// if (file.exists()) {
			// System.out.println("已处理过，继续下一个url");
			// continue;
			// }
//			try {
//				String res = HttpUtil.get(url);

//				JSONObject jObject = new JSONObject(res);
//				String m3u8Path = url;//.getStr("m3u8");
//				String m3u8UrlString = url;//"https://www.tlhf01.com" + m3u8Path;
			// System.out.println(m3u8UrlString);

//				String m3u8Info = Jsoup.connect(m3u8UrlString).ignoreContentType(true).timeout(50000).method(Method.GET)
//						.execute().body();
			// HttpUtil.get(m3u8UrlString);
			// System.out.println("m3u8:" + m3u8Info);
//				String[] lineStrings = m3u8Info.split("\n");
			StringBuffer newStringBuffer = new StringBuffer();
//				for (int j = 0; j < lineStrings.length - 1; j++) {
//					newStringBuffer.append(lineStrings[j] + "\n");
//				}
//				newStringBuffer.append(url + "\n");
//				String m3u8Ts = lineStrings[lineStrings.length - 2];
			// System.out.println("ts路径：" + m3u8Ts);
			// 获取最大ts然后拼接到m3u8Info;然后保存到磁盘
//				String preString = m3u8Ts.substring(0, m3u8Ts.indexOf("/index"));
//				String startTsString = m3u8Ts.replace(preString, "").substring(6, 7);
//				int startTsInt = Integer.parseInt(startTsString) + 1;
//				int endTsInt = 10000;
//				int tc = 0;
			/*
			 * 
			 * while (startTsInt < endTsInt) { // 注意这里是 < 而不是 <= int mid = startTsInt +
			 * (endTsInt - startTsInt) / 2; // System.out.println("ts path:" + preString +
			 * "/index" + mid + ".ts"); String tsMaxRes = "tsfileinfo"; try { tc++; //
			 * System.out.println("探测URL：" + preString + "/index" + mid + ".ts"); tsMaxRes =
			 * Jsoup.connect(preString + "/index" + mid + ".ts").method(Method.HEAD)
			 * .ignoreContentType(true).timeout(50000).execute().body(); } catch
			 * (HttpStatusException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage()); tsMaxRes = "<!DOCTYPE html>"; }
			 * catch (SocketException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage()); System.out.println("网络异常-重试中:"
			 * + e.getMessage()); continue; } catch (SocketTimeoutException e) { // TODO:
			 * handle exception // System.out.println("有异常1：" + e.getMessage());
			 * System.out.println("网络超时-重试中:" + e.getMessage()); continue; } catch
			 * (Exception e) { // TODO: handle exception System.out.println("未知异常-重试中:" +
			 * e.getMessage()); continue; }
			 * 
			 * // catch (Exception e) { // System.out.println("有异常2：" + e.getMessage()); //
			 * // TODO: handle exception //// tsMaxRes=""; // }
			 * 
			 * if (tsMaxRes.startsWith("<!DOCTYPE html>")) { //
			 * 如果mid的响应是"签名错误"，则可能在mid的左侧找到有效响应 endTsInt = mid; // 更新搜索范围到mid的左侧 } else { //
			 * 如果mid的响应不是"签名错误"，则在mid的右侧继续查找 startTsInt = mid + 1; }
			 * 
			 * // 可以考虑添加延迟，以减少对服务器的压力 // Thread.sleep(someDelay); }
			 * 
			 * 
			 * 
			 * 
			 * // 当循环结束时，startTsInt指向的是第一个"签名错误"的索引，或者它超出了实际的有效范围 //
			 * 因此，我们需要检查startTsInt-1的响应是否有效 System.out.println("探测次数：" + tc + " ; " +
			 * "ts结尾：" + (startTsInt - 1));
			 * 
			 * String lastValidResponse = null; while (lastValidResponse == null) { try {
			 * lastValidResponse = Jsoup.connect(preString + "/index" + (startTsInt - 1) +
			 * ".ts")
			 * .method(Method.HEAD).ignoreContentType(true).timeout(50000).execute().body();
			 * 
			 * } catch (HttpStatusException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage()); lastValidResponse =
			 * "<!DOCTYPE html>"; } catch (SocketException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage());
			 * System.out.println("[已结束探测]网络异常-重试中:" + e.getMessage()); continue; } catch
			 * (SocketTimeoutException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage()); System.out.println("网络超时-重试中:"
			 * + e.getMessage()); continue; } catch (Exception e) { // TODO: handle
			 * exception System.out.println("未知异常-重试中:" + e.getMessage()); continue; }
			 * lastValidResponse = "success"; }
			 * 
			 * // HttpUtil.get(preString + "/index" + (startTsInt - 1) + ".ts"); if
			 * (!lastValidResponse.startsWith("<!DOCTYPE html>")) { //
			 * newStringBuffer.append(url + (startTsInt - 1)); // 添加有效的URL到字符串缓冲区
			 * 
			 * for (int kk = Integer.parseInt(startTsString) + 1; kk <= startTsInt - 1;
			 * kk++) {
			 * 
			 * newStringBuffer.append("#EXTINF:4.000000," + "\n" + preString + "/index" + kk
			 * + ".ts" + "\n");
			 * 
			 * }
			 * 
			 * } else { // 如果startTsInt-1也是"签名错误"，那么可能是没有有效的响应，或者所有响应都是"签名错误" //
			 * 这里需要根据你的业务需求来处理这种情况 // 例如，你可能想要抛出异常，或者记录一个错误消息 }
			 * 
			 */

//				newStringBuffer.append(lineStrings[lineStrings.length - 1] + "\n");

			// FileUtil.writeUtf8String(url, "d:/m3u8taoluapi/" + vidStrings[4] + ".m3u8");
			String ttt = listT.get(ccc - 1);
			ttt = FileNameSanitizer.sanitize(ttt);

			System.out.println(listT.get(ccc - 1) + "\t" + "封面：" + listImg.get(ccc - 1) + "\t" + "观看地址: " + url);
			// + "\t试看地址: "+ shikans.get(ccc - 1)

			if (write == 1)
				if (args.length > 0 || url.indexOf(".mp4") != -1) {
					// for /F \"tokens=* delims=\" %%a in ('java -cp \".;lib/*\"
					// cn.exrick.manager.service.util.getWanwuPageForApiTest "+listUI.get(ccc-1)+"')
					// do (set IP=\"%%a\")\r\n
					if (disknum == 0)
						FileUtil.appendString(
								"" + "echo 标题: " + ttt + "\t" + "观看地址: " + url + "\t试看地址: " + shikans.get(ccc - 1)
										+ "\t封面: " + listImg.get(ccc - 1) + "\r\n" + "wget -O \"" + ttt + "_"
										+ System.currentTimeMillis() + ".mp4\"     " + url + " \r\n",
								"K:/index/app3zhubo/allm3u8.bat", Charset.forName("GBK"));
					else if (disknum == 1) {
						FileUtil.appendString(
								"" + "echo 标题: " + ttt + "\t" + "观看地址: " + url + "\t试看地址: " + shikans.get(ccc - 1)
										+ "\t封面: " + listImg.get(ccc - 1) + "\r\n" + "wget -O \"" + ttt + "_"
										+ System.currentTimeMillis() + ".mp4\"     " + url + " \r\n",
								"M:/m3u8taoluapi/allm3u8.bat", Charset.forName("GBK"));
					} else {
						FileUtil.appendString("" + "" + "wget -O \"" + ttt + "_" + System.currentTimeMillis()
								+ ".mp4\"     \"" + url + "\" \r\n", "d:/allm3u8.bat", Charset.forName("GBK"));
					}
				} else {
					if (disknum == 0)
						FileUtil.appendString(
								"for /F \"tokens=* delims=\" %%a in ('java -cp \".;lib/*\"   cn.exrick.manager.service.util.getWanwuPageForApiTest "
										+ listUI.get(ccc - 1) + "') do (set IP=\"%%a\")\r\n" + "echo 标题: "
										+ listT.get(ccc - 1) + "\t" + "观看地址: " + url + "\t试看地址: " + shikans.get(ccc - 1)
										+ "\t封面: " + listImg.get(ccc - 1) + "\r\n" + "N_m3u8DL-CLI_v3.0.2 --saveName \""
										+ listT.get(ccc - 1) + "\"  --enableDelAfterDone   %IP% \r\n",
								"K:/index/app3zhubo/allm3u8.bat", Charset.forName("GBK"));
					else if (disknum == 1) {
						FileUtil.appendString(
								"for /F \"tokens=* delims=\" %%a in ('java -cp \".;lib/*\"   cn.exrick.manager.service.util.getWanwuPageForApiTest "
										+ listUI.get(ccc - 1) + "') do (set IP=\"%%a\")\r\n" + "echo 标题: "
										+ listT.get(ccc - 1) + "\t" + "观看地址: " + url + "\t试看地址: " + shikans.get(ccc - 1)
										+ "\t封面: " + listImg.get(ccc - 1) + "\r\n" + "N_m3u8DL-CLI_v3.0.2 --saveName \""
										+ listT.get(ccc - 1) + "\"  --enableDelAfterDone   %IP% \r\n",
								"M:/m3u8taoluapi/allm3u8.bat", Charset.forName("GBK"));
					} else {
						FileUtil.appendString(
								"for /F \"tokens=* delims=\" %%a in ('java -cp \".;lib/*\"   cn.exrick.manager.service.util.getWanwuPageForApiTest "
										+ listUI.get(ccc - 1) + "') do (set IP=\"%%a\")\r\n" + ""
										+ "N_m3u8DL-CLI_v3.0.2 --saveName \"" + listT.get(ccc - 1)
										+ "\"  --enableDelAfterDone   \"%IP%\" \r\n",
								"d:/allm3u8.bat", Charset.forName("GBK"));
					}

				}
		}
		/*
		 * catch (SocketException e) { // TODO: handle exception //
		 * System.out.println("有异常1：" + e.getMessage());
		 * System.out.println("[首次访问]网络异常-重试中:" + e.getMessage()); continue; } catch
		 * (SocketTimeoutException e) { // TODO: handle exception //
		 * System.out.println("有异常1：" + e.getMessage()); System.out.println("网络超时-重试中:"
		 * + e.getMessage()); continue; } catch (Exception e) { e.printStackTrace(); //
		 * TODO: handle exception System.out.println("未知异常-重试中:" + e.getMessage());
		 * continue; }
		 */

//			catch (Exception e) {
//				e.printStackTrace();
//				System.err.println("-----------------url下载异常：" + url);
//				FileUtil.appendUtf8String("title\t" + url + "\n", "d:/m3u8taolu/allm3u8.error");
//
//				// TODO: handle exception
//			}

//		}
	}

	public static void geneBat2(String[] args, int diskNum) {
		List<String> list = new ArrayList<String>();
		List<String> listT = new ArrayList<String>();
		List<String> listUI = new ArrayList<String>();
		List<String> listImg = new ArrayList<String>();
		List<String> shikans = new ArrayList<String>();

		try {
			Path directoryPath = Paths.get("D:\\m3u8ListTaoluzhuboapi"); // 替换为你的目录路径

			Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					// 如果需要，可以在这里处理目录
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					try (BufferedReader reader = Files.newBufferedReader(file)) {
						String line;
						while ((line = reader.readLine()) != null) {
							String[] parts = line.split("\t");
							if (parts.length > 1) {
								list.add(parts[1]);
								listT.add(parts[0].replace("?", "").replace("？", "").replace(":", "").replace("：", ""));
								listUI.add(parts[2]);
								listImg.add(parts[3]);
								shikans.add(parts[4]);
							}
						}
						FileUtil.del(file.toFile());
					} catch (IOException e) {
						e.printStackTrace();
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
//		HttpsUrlValidator.retrieveResponseFromServer("https://tlvod-cdn.chinaysfc.com/");
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int ccc = 0;
		for (String m3u8UrlString : list) {
			ccc += 1;
			// System.out.println("进度：" + ccc + "/" + list.size());
//			System.out.println("url:" + url)
			// System.out.println("url:" + m3u8UrlString);
			String url = m3u8UrlString;
			String[] vidStrings = url.split("/");
			// File file = new File("D:\\m3u8taoluapi\\" + vidStrings[4] + ".m3u8");
			// if (file.exists()) {
			// System.out.println("已处理过，继续下一个url");
			// continue;
			// }
//			try {
//				String res = HttpUtil.get(url);

//				JSONObject jObject = new JSONObject(res);
//				String m3u8Path = url;//.getStr("m3u8");
//				String m3u8UrlString = url;//"https://www.tlhf01.com" + m3u8Path;
			// System.out.println(m3u8UrlString);

//				String m3u8Info = Jsoup.connect(m3u8UrlString).ignoreContentType(true).timeout(50000).method(Method.GET)
//						.execute().body();
			// HttpUtil.get(m3u8UrlString);
			// System.out.println("m3u8:" + m3u8Info);
//				String[] lineStrings = m3u8Info.split("\n");
			StringBuffer newStringBuffer = new StringBuffer();
//				for (int j = 0; j < lineStrings.length - 1; j++) {
//					newStringBuffer.append(lineStrings[j] + "\n");
//				}
//				newStringBuffer.append(url + "\n");
//				String m3u8Ts = lineStrings[lineStrings.length - 2];
			// System.out.println("ts路径：" + m3u8Ts);
			// 获取最大ts然后拼接到m3u8Info;然后保存到磁盘
//				String preString = m3u8Ts.substring(0, m3u8Ts.indexOf("/index"));
//				String startTsString = m3u8Ts.replace(preString, "").substring(6, 7);
//				int startTsInt = Integer.parseInt(startTsString) + 1;
//				int endTsInt = 10000;
//				int tc = 0;
			/*
			 * 
			 * while (startTsInt < endTsInt) { // 注意这里是 < 而不是 <= int mid = startTsInt +
			 * (endTsInt - startTsInt) / 2; // System.out.println("ts path:" + preString +
			 * "/index" + mid + ".ts"); String tsMaxRes = "tsfileinfo"; try { tc++; //
			 * System.out.println("探测URL：" + preString + "/index" + mid + ".ts"); tsMaxRes =
			 * Jsoup.connect(preString + "/index" + mid + ".ts").method(Method.HEAD)
			 * .ignoreContentType(true).timeout(50000).execute().body(); } catch
			 * (HttpStatusException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage()); tsMaxRes = "<!DOCTYPE html>"; }
			 * catch (SocketException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage()); System.out.println("网络异常-重试中:"
			 * + e.getMessage()); continue; } catch (SocketTimeoutException e) { // TODO:
			 * handle exception // System.out.println("有异常1：" + e.getMessage());
			 * System.out.println("网络超时-重试中:" + e.getMessage()); continue; } catch
			 * (Exception e) { // TODO: handle exception System.out.println("未知异常-重试中:" +
			 * e.getMessage()); continue; }
			 * 
			 * // catch (Exception e) { // System.out.println("有异常2：" + e.getMessage()); //
			 * // TODO: handle exception //// tsMaxRes=""; // }
			 * 
			 * if (tsMaxRes.startsWith("<!DOCTYPE html>")) { //
			 * 如果mid的响应是"签名错误"，则可能在mid的左侧找到有效响应 endTsInt = mid; // 更新搜索范围到mid的左侧 } else { //
			 * 如果mid的响应不是"签名错误"，则在mid的右侧继续查找 startTsInt = mid + 1; }
			 * 
			 * // 可以考虑添加延迟，以减少对服务器的压力 // Thread.sleep(someDelay); }
			 * 
			 * 
			 * 
			 * 
			 * // 当循环结束时，startTsInt指向的是第一个"签名错误"的索引，或者它超出了实际的有效范围 //
			 * 因此，我们需要检查startTsInt-1的响应是否有效 System.out.println("探测次数：" + tc + " ; " +
			 * "ts结尾：" + (startTsInt - 1));
			 * 
			 * String lastValidResponse = null; while (lastValidResponse == null) { try {
			 * lastValidResponse = Jsoup.connect(preString + "/index" + (startTsInt - 1) +
			 * ".ts")
			 * .method(Method.HEAD).ignoreContentType(true).timeout(50000).execute().body();
			 * 
			 * } catch (HttpStatusException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage()); lastValidResponse =
			 * "<!DOCTYPE html>"; } catch (SocketException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage());
			 * System.out.println("[已结束探测]网络异常-重试中:" + e.getMessage()); continue; } catch
			 * (SocketTimeoutException e) { // TODO: handle exception //
			 * System.out.println("有异常1：" + e.getMessage()); System.out.println("网络超时-重试中:"
			 * + e.getMessage()); continue; } catch (Exception e) { // TODO: handle
			 * exception System.out.println("未知异常-重试中:" + e.getMessage()); continue; }
			 * lastValidResponse = "success"; }
			 * 
			 * // HttpUtil.get(preString + "/index" + (startTsInt - 1) + ".ts"); if
			 * (!lastValidResponse.startsWith("<!DOCTYPE html>")) { //
			 * newStringBuffer.append(url + (startTsInt - 1)); // 添加有效的URL到字符串缓冲区
			 * 
			 * for (int kk = Integer.parseInt(startTsString) + 1; kk <= startTsInt - 1;
			 * kk++) {
			 * 
			 * newStringBuffer.append("#EXTINF:4.000000," + "\n" + preString + "/index" + kk
			 * + ".ts" + "\n");
			 * 
			 * }
			 * 
			 * } else { // 如果startTsInt-1也是"签名错误"，那么可能是没有有效的响应，或者所有响应都是"签名错误" //
			 * 这里需要根据你的业务需求来处理这种情况 // 例如，你可能想要抛出异常，或者记录一个错误消息 }
			 * 
			 */

//				newStringBuffer.append(lineStrings[lineStrings.length - 1] + "\n");

			// FileUtil.writeUtf8String(url, "d:/m3u8taoluapi/" + vidStrings[4] + ".m3u8");
			System.out.println(listT.get(ccc - 1) + "\t" + "观看地址: " + url + "\t" + "封面：" + listImg.get(ccc - 1));
			String ttt = listT.get(ccc - 1);
			ttt = FileNameSanitizer.sanitize(ttt);

			if (args.length > 0 || url.indexOf(".mp4") != -1) {
				// for /F \"tokens=* delims=\" %%a in ('java -cp \".;lib/*\"
				// cn.exrick.manager.service.util.getWanwuPageForApiTest "+listUI.get(ccc-1)+"')
				// do (set IP=\"%%a\")\r\n
				if (diskNum == 0)
					FileUtil.appendString("" + "echo 标题: " + ttt + "\t" + "观看地址: " + url + "\t试看地址: "
							+ shikans.get(ccc - 1) + "\t封面: " + listImg.get(ccc - 1) + "\r\n"
							+ "wget   --no-proxy --header=\"Referer: http://MAfAIOo0E8EMOWPA.black\"  --header=\"User-Agent: 2.32.0.00471\"      -O \""
							+ ttt + "_" + System.currentTimeMillis() + ".mp4\"     " + url + " \r\n",
							"K:/index/app3zhubo/allm3u8.bat", Charset.forName("GBK"));
				else if (diskNum == 1) {
					FileUtil.appendString("" + "echo 标题: " + ttt + "\t" + "观看地址: " + url + "\t试看地址: "
							+ shikans.get(ccc - 1) + "\t封面: " + listImg.get(ccc - 1) + "\r\n"
							+ "wget   --no-proxy --header=\"Referer: http://MAfAIOo0E8EMOWPA.black\"  --header=\"User-Agent: 2.32.0.00471\"      -O \""
							+ ttt + "_" + System.currentTimeMillis() + ".mp4\"     " + url + " \r\n",
							"M:/m3u8taoluapi/allm3u8.bat", Charset.forName("GBK"));
				} else {
					FileUtil.appendString("" + "echo 标题: " + ttt + "\t" + "观看地址: " + url + "\t试看地址: "
							+ shikans.get(ccc - 1) + "\t封面: " + listImg.get(ccc - 1) + "\r\n"
							+ "wget   --no-proxy --header=\"Referer: http://MAfAIOo0E8EMOWPA.black\"  --header=\"User-Agent: 2.32.0.00471\"      -O \""
							+ ttt + "_" + System.currentTimeMillis() + ".mp4\"     " + url + " \r\n", "I:/allm3u8.bat",
							Charset.forName("GBK"));
				}
			} else {
				if (diskNum == 0)
					FileUtil.appendString("echo 标题: " + listT.get(ccc - 1) + "\t" + "观看地址: " + url + "\t试看地址: "
							+ shikans.get(ccc - 1) + "\t封面: " + listImg.get(ccc - 1) + "\r\n"
							+ "N_m3u8DL-CLI_v3.0.2  --headers \"Referer: http://MAfAIOo0E8EMOWPA.black|User-Agent: 2.32.0.00471\"   --saveName \""
							+ listT.get(ccc - 1) + "\"  --enableDelAfterDone   " + url + " \r\n",
							"K:/index/app3zhubo/allm3u8.bat", Charset.forName("GBK"));
				else if (diskNum == 1) {
					FileUtil.appendString("echo 标题: " + listT.get(ccc - 1) + "\t" + "观看地址: " + url + "\t试看地址: "
							+ shikans.get(ccc - 1) + "\t封面: " + listImg.get(ccc - 1) + "\r\n"
							+ "N_m3u8DL-CLI_v3.0.2  --headers \"Referer: http://MAfAIOo0E8EMOWPA.black|User-Agent: 2.32.0.00471\"   --saveName \""
							+ listT.get(ccc - 1) + "\"  --enableDelAfterDone   " + url + " \r\n",
							"M:/m3u8taoluapi/allm3u8.bat", Charset.forName("GBK"));
				} else {
					FileUtil.appendString("echo 标题: " + listT.get(ccc - 1) + "\t" + "观看地址: " + url + "\t试看地址: "
							+ shikans.get(ccc - 1) + "\t封面: " + listImg.get(ccc - 1) + "\r\n"
							+ "N_m3u8DL-CLI_v3.0.2  --headers \"Referer: http://MAfAIOo0E8EMOWPA.black|User-Agent: 2.32.0.00471\"   --saveName \""
							+ listT.get(ccc - 1) + "\"  --enableDelAfterDone   " + url + " \r\n", "I:/allm3u8.bat",
							Charset.forName("GBK"));
				}

			}
		}

//		"for /F \"tokens=* delims=\" %%a in ('java -cp \".;lib/*\"   cn.exrick.manager.service.util.getWanwuPageForApiTest "
//		+ listUI.get(ccc - 1) + "') do (set IP=\"%%a\")\r\n" + 
		/*
		 * catch (SocketException e) { // TODO: handle exception //
		 * System.out.println("有异常1：" + e.getMessage());
		 * System.out.println("[首次访问]网络异常-重试中:" + e.getMessage()); continue; } catch
		 * (SocketTimeoutException e) { // TODO: handle exception //
		 * System.out.println("有异常1：" + e.getMessage()); System.out.println("网络超时-重试中:"
		 * + e.getMessage()); continue; } catch (Exception e) { e.printStackTrace(); //
		 * TODO: handle exception System.out.println("未知异常-重试中:" + e.getMessage());
		 * continue; }
		 */

//			catch (Exception e) {
//				e.printStackTrace();
//				System.err.println("-----------------url下载异常：" + url);
//				FileUtil.appendUtf8String("title\t" + url + "\n", "d:/m3u8taolu/allm3u8.error");
//
//				// TODO: handle exception
//			}

//		}
	}

}
