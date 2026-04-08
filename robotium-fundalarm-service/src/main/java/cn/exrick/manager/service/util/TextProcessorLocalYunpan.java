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

public class TextProcessorLocalYunpan {

	private static final ExecutorService executor = Executors.newFixedThreadPool(50);

	public static void main(String[] args) {

		// 136
		// 清除Java内置DNS缓存
		java.security.Security.setProperty("networkaddress.cache.ttl", "0");
		java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "7890");
		// 对于HTTPS
		System.setProperty("https.proxyHost", "localhost");
		System.setProperty("https.proxyPort", "7890");

		doWith("e:\\Downloads");
		doWith("e:");

		doWith("e:\\录制中心");
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
//					return FileVisitResult.SKIP_SUBTREE;

				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					Path filename = file.getFileName();
					if (attrs.isDirectory()) {
						return FileVisitResult.CONTINUE;
					}
					// 获取文件名(String)
//					System.out.println("扫描到：" + file.getFileName().toString());
//					System.out.println("扫描到：" + file.toString());
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

					if (key == null) {
						System.out.println("line:" + line);

						executor.submit(new DownVid(line, file.toString()));
					} else {

						System.out.println("key:" + key);

						executor.submit(new DownVid(key, file.toString()));
//						key = FileNameSanitizer.sanitize(key);
//
//						executor.submit(new DownVid(key, file.toString()));
						if (line.indexOf("作品id_") != -1) {
							String oldKeyString = key.replace("作品id_", "作品id--").replace("用户id_", "用户id--");

							executor.submit(new DownVid(oldKeyString, file.toString()));

						}

						// 检测key，如果存在网盘文件，删除磁盘文件，不上传。

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
//				articleUrlString = FileNameSanitizer.sanitize(articleUrlString);
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
				System.out.println(articleUrlString);
				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
						.header("Cookie",
								"b-user-id=5c0e91e9-7d5c-a58b-67ee-04020d828576; _UP_A4A_11_=wb9c81ffa9bc48bab50a75bce2c5cf4a; __sdid=AATyk+Q8drQgzeM3pssns0Qixay8m7tg3Lyl9X2jq8f7Heg9V4eiDDDWp+og9Khsmtk=; _UP_D_=pc; _UP_30C_6A_=st9cb620136qivoo161v1gppcvpjo2fz; _UP_TS_=sg159c62f57758585fa4a6805549c0c262b; _UP_E37_B7_=sg159c62f57758585fa4a6805549c0c262b; _UP_TG_=st9cb620136qivoo161v1gppcvpjo2fz; _UP_335_2B_=1; __pus=736ed2e9f210baaabdd18d65e27349baAARVRLqZI1EpYTS/XljsmdZXRe66FFKhKW6Wlrs356hLBMKz6iQCz7pMO0CNmM+1Lqn8C4vn3AyJbrBI2GnRNQ0E; __kp=a74effb0-6147-11f0-bc92-47ad8a7af2a7; __kps=AAS5hcgdgt6p+1EIB02h/tr5; __ktd=MVA089y4vvW2XOFMIuV5jw==; __uid=AAS5hcgdgt6p+1EIB02h/tr5; isg=BERENoB48E-zCkRx94nEI-XLFcI2XWjHOUh9Il7lzY_SieVThmwmV1BizSFR9aAf; tfstk=glU-fvqSgtXo3vU-i7SDt3qWn45ciiVzH8P6-vDkA-eYTJnnA0coJDwSnzmnPY2vhbl3-U1EAKGLKf1UP4whJvNZ3U2h4DYpare9-UXP4WTK-26GINbgabuEROqDaavv4XhWAwDIA4ujp4bq0Nbga7OoN1VfSyACJ7M-d2iSRx9j3X8SRXMCMxGK_H97dJ1vcXGpA3GIdE1xTxMIR2wQGshnhvMj7iHFNvLL2uyH_-5tfeTQkbnfm7MXDv4jwVMTNxLBRsG-2ANSHTtD3-nbTDUeTeHTNlyE1895pvzYGzZ_CtxoFlhLskeO5el4V8UjvrfF7S3-eJiL0OLiw4qxDjEOsFnnrPmsPojVhD0-6rn4y1KSekNsxrgFCUMKfYi_jmOWjSg-_DaoqCxqrlVg8ca5ydkmbXexjuB9yRsynP49hRpiBXx5MsKeY0GqnKljXGJwc5GxIsckYHo0gjHGMsKeY0GqMAfDKH-EmS5..; __puus=1c7e5be7cd1a18d8e0fda17e8a88aed9AASUhn/7ZUlOCjxQ4mtnOO174hpZE39h1zqDWveL52YEghTyz5wkfGLzuXD0AG+sCHgw4xEenXrCU/cvoZ617vp0PRI5E+E1BBv/uEX9713tlr+iLJA/T//Vke07J0V+9ryVCZxBkvcTMsAcINlA35GSOzmoWin3jDQUOKUidYc0UNlWmCC1QDedIQ0nb0CoSJ/b66HL46FGK4wioM3g/oHk")

						.header("User-Agent",
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
						.execute();

				String sourceString = articleRes.body();
				System.out.println(sourceString);
				JSONObject jo = new JSONObject(sourceString);
				int total = jo.getJSONObject("metadata").getInt("_total");

				System.out.println(rtmpUrl + "-m3u8:\t" + total + "\t" + articleUrlString);

				if (total > 0) {
//					m3u8Result.append(rtmpUrl + "\t" + (total > 0 ? 1 : 0));
					System.out.println("删除此文件：" + ffmpegPath);
//					new File(ffmpegPath).delete();
					Path path = Paths.get(ffmpegPath);
					Files.delete(path); // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}

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
