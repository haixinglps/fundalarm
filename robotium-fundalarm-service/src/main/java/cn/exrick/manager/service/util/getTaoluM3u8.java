package cn.exrick.manager.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import cn.hutool.core.io.FileUtil;

public class getTaoluM3u8 {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();

		try {
			Path directoryPath = Paths.get("D:\\m3u8ListTaolu"); // 替换为你的目录路径

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
							}
						}
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
			System.out.println("进度：" + ccc + "/" + list.size());
//			System.out.println("url:" + url)
			System.out.println("url:" + m3u8UrlString);
			String url = m3u8UrlString;
			String[] vidStrings = url.split("/");
			File file = new File("D:\\m3u8taolu\\" + vidStrings[6] + ".m3u8");
			if (file.exists()) {
				System.out.println("已处理过，继续下一个url");
				continue;
			}
			try {
//				String res = HttpUtil.get(url);

//				JSONObject jObject = new JSONObject(res);
//				String m3u8Path = url;//.getStr("m3u8");
//				String m3u8UrlString = url;//"https://www.tlhf01.com" + m3u8Path;
				// System.out.println(m3u8UrlString);

				String m3u8Info = Jsoup.connect(m3u8UrlString).ignoreContentType(true).timeout(50000).method(Method.GET)
						.execute().body();
				// HttpUtil.get(m3u8UrlString);
				// System.out.println("m3u8:" + m3u8Info);
				String[] lineStrings = m3u8Info.split("\n");
				StringBuffer newStringBuffer = new StringBuffer();
				for (int j = 0; j < lineStrings.length - 1; j++) {
					newStringBuffer.append(lineStrings[j] + "\n");
				}
				String m3u8Ts = lineStrings[lineStrings.length - 2];
				// System.out.println("ts路径：" + m3u8Ts);
				// 获取最大ts然后拼接到m3u8Info;然后保存到磁盘
				String preString = m3u8Ts.substring(0, m3u8Ts.indexOf("/index"));
				String startTsString = m3u8Ts.replace(preString, "").substring(6, 7);
				int startTsInt = Integer.parseInt(startTsString) + 1;
				int endTsInt = 2000;
				int tc = 0;

				while (startTsInt < endTsInt) { // 注意这里是 < 而不是 <=
					int mid = startTsInt + (endTsInt - startTsInt) / 2;
					// System.out.println("ts path:" + preString + "/index" + mid + ".ts");
					String tsMaxRes = "tsfileinfo";
					try {
						tc++;
//						System.out.println("探测URL：" + preString + "/index" + mid + ".ts");
						tsMaxRes = Jsoup.connect(preString + "/index" + mid + ".ts").method(Method.HEAD)
								.ignoreContentType(true).timeout(50000).execute().body();
					} catch (HttpStatusException e) {
						// TODO: handle exception
						// System.out.println("有异常1：" + e.getMessage());
						tsMaxRes = "<!DOCTYPE html>";
					} catch (SocketException e) {
						// TODO: handle exception
						// System.out.println("有异常1：" + e.getMessage());
						System.out.println("网络异常-重试中:" + e.getMessage());
						continue;
					} catch (SocketTimeoutException e) {
						// TODO: handle exception
						// System.out.println("有异常1：" + e.getMessage());
						System.out.println("网络超时-重试中:" + e.getMessage());
						continue;
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("未知异常-重试中:" + e.getMessage());
						continue;
					}

//					catch (Exception e) {
//						System.out.println("有异常2：" + e.getMessage());
//						// TODO: handle exception
////						tsMaxRes="";
//					}

					if (tsMaxRes.startsWith("<!DOCTYPE html>")) {
						// 如果mid的响应是"签名错误"，则可能在mid的左侧找到有效响应
						endTsInt = mid; // 更新搜索范围到mid的左侧
					} else {
						// 如果mid的响应不是"签名错误"，则在mid的右侧继续查找
						startTsInt = mid + 1;
					}

					// 可以考虑添加延迟，以减少对服务器的压力
					// Thread.sleep(someDelay);
				}

				// 当循环结束时，startTsInt指向的是第一个"签名错误"的索引，或者它超出了实际的有效范围
				// 因此，我们需要检查startTsInt-1的响应是否有效
				System.out.println("探测次数：" + tc + " ; " + "ts结尾：" + (startTsInt - 1));

				String lastValidResponse = null;
				while (lastValidResponse == null) {
					try {
						lastValidResponse = Jsoup.connect(preString + "/index" + (startTsInt - 1) + ".ts")
								.method(Method.HEAD).ignoreContentType(true).timeout(50000).execute().body();

					} catch (HttpStatusException e) {
						// TODO: handle exception
						// System.out.println("有异常1：" + e.getMessage());
						lastValidResponse = "<!DOCTYPE html>";
					} catch (SocketException e) {
						// TODO: handle exception
						// System.out.println("有异常1：" + e.getMessage());
						System.out.println("[已结束探测]网络异常-重试中:" + e.getMessage());
						continue;
					} catch (SocketTimeoutException e) {
						// TODO: handle exception
						// System.out.println("有异常1：" + e.getMessage());
						System.out.println("网络超时-重试中:" + e.getMessage());
						continue;
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("未知异常-重试中:" + e.getMessage());
						continue;
					}
					lastValidResponse = "success";
				}

				// HttpUtil.get(preString + "/index" + (startTsInt - 1) + ".ts");
				if (!lastValidResponse.startsWith("<!DOCTYPE html>")) {
					// newStringBuffer.append(url + (startTsInt - 1)); // 添加有效的URL到字符串缓冲区

					for (int kk = Integer.parseInt(startTsString) + 1; kk <= startTsInt - 1; kk++) {

						newStringBuffer.append("#EXTINF:4.000000," + "\n" + preString + "/index" + kk + ".ts" + "\n");

					}

				} else {
					// 如果startTsInt-1也是"签名错误"，那么可能是没有有效的响应，或者所有响应都是"签名错误"
					// 这里需要根据你的业务需求来处理这种情况
					// 例如，你可能想要抛出异常，或者记录一个错误消息
				}
				newStringBuffer.append(lineStrings[lineStrings.length - 1] + "\n");

				FileUtil.writeUtf8String(
						newStringBuffer.toString().replace("\"/videos/202", "\"https://www.tlhf01.com/videos/202"),
						"d:/m3u8taolu/" + vidStrings[6] + ".m3u8");
				FileUtil.appendUtf8String(vidStrings[6] + ".m3u8\n", "d:/m3u8taolu/allm3u8.bat");
			} catch (SocketException e) {
				// TODO: handle exception
				// System.out.println("有异常1：" + e.getMessage());
				System.out.println("[首次访问]网络异常-重试中:" + e.getMessage());
				continue;
			} catch (SocketTimeoutException e) {
				// TODO: handle exception
				// System.out.println("有异常1：" + e.getMessage());
				System.out.println("网络超时-重试中:" + e.getMessage());
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
				System.out.println("未知异常-重试中:" + e.getMessage());
				continue;
			}

//			catch (Exception e) {
//				e.printStackTrace();
//				System.err.println("-----------------url下载异常：" + url);
//				FileUtil.appendUtf8String("title\t" + url + "\n", "d:/m3u8taolu/allm3u8.error");
//
//				// TODO: handle exception
//			}

		}
	}

}
