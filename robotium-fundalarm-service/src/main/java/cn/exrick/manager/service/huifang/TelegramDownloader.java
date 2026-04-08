package cn.exrick.manager.service.huifang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelegramDownloader {
	private static final int CHUNK_SIZE = 1048576; // 1MB
	private static final String TARGET_URL = "https://web.telegram.org/a/progressive/document6061999274881717798";
	private static final String OUTPUT_FILE = "merged.mp4";

	public static void main(String[] args) {
		System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "7890");
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "7890");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "7890");
		long totalSize = getContentLength();
		System.out.println(totalSize);
		totalSize = 1000000;
		int numChunks = (int) ((totalSize + CHUNK_SIZE - 1) / CHUNK_SIZE);

		System.out.println("开始分块下载 " + numChunks + " 块...");
		numChunks = 1;
		downloadChunks(totalSize, numChunks);
//		mergeFiles();
	}

	private static long getContentLength() {
		try {
			URL url = new URL(TARGET_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			return conn.getContentLength();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private static void downloadChunks(long totalSize, int numChunks) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < numChunks; i++) {
			long start = i * CHUNK_SIZE;
			long end = Math.min(start + CHUNK_SIZE - 1, totalSize - 1);
			executor.submit(() -> downloadChunk(start, end));
		}
		executor.shutdown();
	}
//	
//	:authority: web.telegram.org
//	:method: GET
//	:path: /a/progressive/document6055362231854438604
//	:scheme: https
//	accept: */*
//	accept-encoding: identity;q=1, *;q=0
//	accept-language: zh-CN,zh;q=0.9,en;q=0.8
//	cache-control: no-cache
//	cookie: stel_ln=en
//	pragma: no-cache
//	range: bytes=0-
//	referer: https://web.telegram.org/a/
//	sec-ch-ua: "Not_A Brand";v="99", "Google Chrome";v="109", "Chromium";v="109"
//	sec-ch-ua-mobile: ?0
//	sec-ch-ua-platform: "Windows"
//	sec-fetch-dest: video
//	sec-fetch-mode: no-cors
//	sec-fetch-site: same-origin
//	user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36

	private static void downloadChunk(long start, long end) {
		String range = "bytes=" + start + "-";
		try {
			System.out.println(range);
			URL url = new URL(TARGET_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Range", range);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Referer", "https://web.telegram.org/a/");
//			conn.setRequestProperty("Cookie", value);
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
			conn.setRequestProperty("Accept-Encoding", "identity;q=1, *;q=0");

			conn.setRequestProperty("sec-ch-ua",
					"\"Not_A Brand\";v=\"99\", \"Google Chrome\";v=\"109\", \"Chromium\";v=\"109\"");
			conn.setRequestProperty("sec-ch-ua-mobile", "?0");
			conn.setRequestProperty("sec-ch-ua-platform", "Windows");
			System.out.println(conn.getRequestProperties().toString());

			int code = conn.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK || code == 206) {
				String filename = "chunk_" + (start / CHUNK_SIZE + 1) + ".tmp";
				FileOutputStream fos = new FileOutputStream(filename);
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
				fos.close();
				is.close();
			} else {
				System.err.println("下载分块失败: " + code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void mergeFiles() {
		try {
			List<File> chunks = new ArrayList<>();
			File dir = new File(".");
			for (File file : dir.listFiles()) {
				if (file.getName().startsWith("chunk_") && file.getName().endsWith(".tmp")) {
					chunks.add(file);
				}
			}
			Collections.sort(chunks, (f1, f2) -> {
				int num1 = Integer.parseInt(f1.getName().split("_")[1].split("\\.")[0]);
				int num2 = Integer.parseInt(f2.getName().split("_")[1].split("\\.")[0]);
				return Integer.compare(num1, num2);
			});

			FileOutputStream fos = new FileOutputStream(OUTPUT_FILE);
			for (File chunk : chunks) {
				FileInputStream fis = new FileInputStream(chunk);
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = fis.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
				fis.close();
				chunk.delete();
			}
			fos.close();
			System.out.println("文件已成功合并为 " + OUTPUT_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("文件合并失败");
		}
	}

}
