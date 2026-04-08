package cn.exrick.manager.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.FileUtil;

public class getOuzhouM3u8 {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		Path directoryPath = Paths.get("D:\\m3u8ListOuZhou"); // 替换为你的目录路径
//	        List<String> secondFields = new ArrayList<>();  

		try {
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
								list.add(parts[1].replace("end=70", "end="));
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
//		list.add("https://www.91mjplay.com/20221209/WVYshFDu/index.m3u8?start=10&end=");

		for (String url : list) {
			System.out.println("开始处理：" + url);
			StringBuffer newStringBuffer = new StringBuffer();
			try {

				String startTsString = "70";
				int startTsInt = Integer.parseInt(startTsString) + 1;
//				for (int i = startTsInt; i <= 7200; i++) {
//					System.out.println("探测最大ts:" + i);
//					String tsMaxRes = HttpUtil.get(url + i + "");
////					System.out.println(tsMaxRes);
//					if (tsMaxRes.startsWith("签名错误")) {
//						System.out.println("到头了：");
//						newStringBuffer.append(url + (i - 1));
//						break;
//					} else {
//						System.out.println("ts【" + i + "】  有数据");
//
//					}
////					Thread.sleep(5000);
//
//				}
//				int startTsInt = Integer.parseInt(startTsString) + 1;  
				int endTsInt = 650;
				/*
				 * while (startTsInt < endTsInt) { // 注意这里是 < 而不是 <= int mid = startTsInt +
				 * (endTsInt - startTsInt) / 2; String tsMaxRes = HttpUtil.get(url + mid);
				 * 
				 * if (tsMaxRes.startsWith("签名错误")) { // 如果mid的响应是"签名错误"，则可能在mid的左侧找到有效响应
				 * endTsInt = mid; // 更新搜索范围到mid的左侧 } else { // 如果mid的响应不是"签名错误"，则在mid的右侧继续查找
				 * startTsInt = mid + 1; }
				 * 
				 * // 可以考虑添加延迟，以减少对服务器的压力 // Thread.sleep(someDelay); }
				 */

				// 当循环结束时，startTsInt指向的是第一个"签名错误"的索引，或者它超出了实际的有效范围
				// 因此，我们需要检查startTsInt-1的响应是否有效
//				String lastValidResponse = HttpUtil.get(url + (startTsInt - 1));
//				if (!lastValidResponse.startsWith("签名错误")) {
				newStringBuffer.append(url + 609); // 添加有效的URL到字符串缓冲区 (startTsInt - 1)
//				} else {
				// 如果startTsInt-1也是"签名错误"，那么可能是没有有效的响应，或者所有响应都是"签名错误"
				// 这里需要根据你的业务需求来处理这种情况
				// 例如，你可能想要抛出异常，或者记录一个错误消息
//				}

//				newStringBuffer.append(lineStrings[lineStrings.length - 1] + "\n");
				String[] vidStrings = url.split("=");
				FileUtil.appendUtf8String(newStringBuffer.toString() + "\n", "d:/m3u8ouzhou/allm3u8.txt");

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				// TODO: handle exception
			}

		}
	}

}
