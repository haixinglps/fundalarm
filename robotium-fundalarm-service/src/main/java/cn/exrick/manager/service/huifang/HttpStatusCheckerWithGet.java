package cn.exrick.manager.service.huifang;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.FileUtil;

public class HttpStatusCheckerWithGet {
	public static void main(String[] args) throws IOException {

		// File file = new File("");
		List<String> lines = new ArrayList<String>();

		Path history = Paths.get("d:/m3u8ListTaoluzhuboapi2/videourl.txt");

//		String urlString = "https://kelly.dgshwhcb.top/android_269467_20250105_052929_1777255.mp4";

		lines = Files.readAllLines(history);
		for (String urlString : lines) {
			String vidString = urlString.split("\t")[0];
			String urlString2 = urlString.split("\t")[1];
			try {

				URL url = new URL(urlString2);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				// 设置GET请求方法
				connection.setRequestMethod("GET");

				// 获取响应状态码
				int responseCode = connection.getResponseCode();

				System.out.println("HTTP状态码: " + responseCode);

				if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
					System.out.println("该资源返回403禁止访问状态");
					FileUtil.appendUtf8String(vidString + "\r\n", "d:/result.txt");
				} else {
//				System.out.println("该资源未返回403状态");

//				// 读取响应内容
//				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//				String inputLine;
//				StringBuilder content = new StringBuilder();
//
//				while ((inputLine = in.readLine()) != null) {
//					content.append(inputLine);
//				}
//				in.close();
//
//				System.out.println("响应内容: " + content.toString());
				}

				connection.disconnect();
			} catch (Exception e) {
				FileUtil.appendUtf8String(urlString + "\r\n", "d:/error.txt");

				continue;
				// TODO: handle exception
			}
		}

	}
}
