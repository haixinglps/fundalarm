package cn.exrick.manager.service.util;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class TextProcessorLocalYunpan2 {

	private static final ExecutorService executor = Executors.newFixedThreadPool(50);

	public static void main(String[] args) {

		// 152

		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "7890");
		// 对于HTTPS
		System.setProperty("https.proxyHost", "localhost");
		System.setProperty("https.proxyPort", "7890");
//		doWith("\\\\MS-LNKKZQAQDHXV\\e\\XSBDownload");
//		doWith("\\\\MS-LNKKZQAQDHXV\\e\\m3u8work\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\f\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\f");

//		doWith("f:\\Downloads");
//		doWith("f:");
		doWith("F:\\C盘瘦身目录");

//		doWith("f:\\录制中心");
		executor.shutdown();

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
					// 获取文件名(String)
//					System.out.println("扫描到：" + file.getFileName().toString());
//					System.out.println("扫描到：" + file.toString());

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

						System.out.println("key:" + key);
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

	private static class DownVid implements Runnable {
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
					articleUrlString = URLEncoder.encode(articleUrlString, "UTF-8");
//					System.out.println(articleUrlString);
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
				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
						.header("Cookie",
								"_UP_A4A_11_=wb9cd10bc6fb495a854595fb4bf82829; b-user-id=d3e7e6e2-78d3-4aa5-f5a4-b642159c3aec; isg=BFVVko9IkVaiQ7VuAKsAyRuMZFEPUglk5FXZN9f6EEwbLnUgn6CmNGLt_DKYSiEc; tfstk=gwBZHo2ItReaNcuQCwpqY4gbXk99HKz5stTXmijDfFYMfdscTiszXn_1mE52-ZEtCd4tnEjRJCTGldg43wJMM1TX5EJVDarQP8w5XGpvEza7FpfnVWJvo-90OpACBnoTP8w5j5x9hM45heG55exDnhAMoDqeXexDmZYMt2xy2KYcoZvn-3-ijEAmnv0HJnvDoZv0YMYp0KYcoKqFxjDOEtee4CqUnszrbR-k_UjM8xSOLhAZ6GYEnxXebC8o3ekmn9-G0Jc1RYPXr_L6NE_3dAJVxn7eN1zrQZSc2sAF3Vq1ri5Fkd63mfYl1NOcIBo0qUXOxTpHO8qeIt7dwUdQ9yKFCTOJyBu3UF1F-ORhEyPObQIFIQvueVYfOgW20tUYLaSc2sAF3VSy7qK3BEWA_qcDsHKePkrUszY3mgr2kihxMCE9YUZtXjhvsHKePkrEMjdOkH87XcC..; _UP_F7E_8D_=smKZ6VuDKwnONzFsjk4Y0Esem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6euQE4T6y6mWW7Bigcz27lvOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhuu7vzFQ1HOEqvSSAsECx4dO%2F9T7juPaQsdqng1z0aknERU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; _UP_D_=pc; __pus=9f75524994d6baf650281a09e631b15eAARvwClq/mGBr8mTkDoj8Gn904pRYcnB4bBCA93jJV3v1YhpCW9e1wJE5hMt7BZay7njBjPP8j0Lx2dgUc/bEoKA; __kp=e83bf0f0-b557-11f0-b46f-ddfc9ba1fd04; __kps=AAS5hcgdgt6p+1EIB02h/tr5; __ktd=MVA089y4vvW2XOFMIuV5jw==; __uid=AAS5hcgdgt6p+1EIB02h/tr5; __puus=364097a73aef6b058b4f494ce2280f2fAASUhn/7ZUlOCjxQ4mtnOO1746jYQF/dKvzhE3/Qabv1Np3sQCcsuHjjLbyOQzy1NtKHi64u+1pMZ7fjMuBbiFuPeFFWSG+uTzW99wr5SKZMjE9AsM2FAacusCgAAxRT1a7ghFcMMJ5OuAaPTuTZutwH9F/DDsyek/4jZnQ3cKkLDnRrSpdJpM+nfohXrbNqh3UXLz554IdypUnhEpbwu3H+")

						.header("User-Agent",
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
						.execute();

				String sourceString = articleRes.body();
				System.out.println(sourceString);
				JSONObject jo = new JSONObject(sourceString);
				int total = jo.getJSONObject("metadata").getInt("_total");

//				System.out.println(rtmpUrl + "-m3u8:\t" + total + "\t" + articleUrlString);

				if (total > 0) {
//					m3u8Result.append(rtmpUrl + "\t" + (total > 0 ? 1 : 0));
					System.out.println("删除此文件：" + ffmpegPath);
//					new File(ffmpegPath).delete();

					// NIO API提供详细错误信息
					Path path = Paths.get(ffmpegPath);
					Files.delete(path); // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}

					System.out.println("删除成功");
					// writer.write(m3u8Result.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();

				// writerErr.write(rtmpUrl);

			} finally {
			}

		}
	}
}
