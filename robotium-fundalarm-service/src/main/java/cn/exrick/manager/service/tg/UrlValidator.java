package cn.exrick.manager.service.tg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class UrlValidator {
	public static boolean isUrlValid(String url) {
		HttpURLConnection connection = null;
		try {
			URL targetUrl = new URL(url);
			connection = (HttpURLConnection) targetUrl.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(5000); // 5秒超时
			connection.setReadTimeout(5000);
			int responseCode = connection.getResponseCode();
			return (responseCode == HttpURLConnection.HTTP_OK);
		} catch (IOException e) {
			System.out.println(url);
			e.printStackTrace();
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private static final int TIMEOUT_MS = 3000;

	public static boolean isRTMPReachable(String server, int port) {
		try (Socket socket = new Socket()) {
			socket.connect(new java.net.InetSocketAddress(server, port), TIMEOUT_MS);
			return socket.isConnected();
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean validateWithFFmpeg(String rtmpUrl) {
		try {
			ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-i", rtmpUrl, "-t", "5", "-f", "null", "-");
			Process process = pb.start();

			// 读取错误输出流（FFmpeg输出到stderr）
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String line;
			boolean hasStream = false;
			while ((line = errorReader.readLine()) != null) {
				if (line.contains("Stream") || line.contains("Duration")) {
					hasStream = true;
					break;
				}
			}

			int exitCode = process.waitFor();
			return exitCode == 0 && hasStream;

		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) {
		System.out.println(validateWithFFmpeg(
				"rtmp://play2.fjefu.cn/ww/room_278004?txSecret=9cf8aea2f10749197dcbf556541750620a04e8f15e1c59aba5974bb494ef1e5b&txTime=691A3304&userId=267412&token=c7cd44c6d299a06d92115b1391f63e80"
						+ "")); // 测试用例
	}
}
