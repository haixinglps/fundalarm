package cn.exrick.manager.service.util;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.hutool.core.io.FileUtil;

public class getWanwuPageForApiTest {
	public static void main(String[] args) {
		go();
	}

	private static String buildLoCommand() {
		return "adb shell \"su -c 'cd /data/local && " + "nohup ./tcpdump -i lo -B 4096 -s 0 -U -w - | "
				+ "./busybox nc -l -p 12346 > /dev/null 2>&1 &'\"";
	}

	private static String buildWlan0Command() {
		return "adb shell \"su -c 'cd /data/local && "
				+ "nohup ./tcpdump -i wlan0 -B 4096 -s 0 -U \\\"port 1935\\\" -w - | "
				+ "./busybox nc -l -p 12345 > /dev/null 2>&1 &'\"";
	}

	public static void go2() {
//		List<String> resultList = new ArrayList<String>();

		String host = "localhost"; // 或者其他IP地址
		int port = 54545; // 你要检查的端口号
		System.out.println("链接ing。。。");
		if (!isPortOpen(host, port) || 1 == 1) {
			// 端口未开启，执行ADB命令
			try {
				Process p1 = Runtime.getRuntime().exec("adb connect 192.168.1.47:8888");
				Process p2 = Runtime.getRuntime().exec("adb forward tcp:12345 tcp:12345");
				Process p3 = Runtime.getRuntime().exec(buildWlan0Command());
				boolean finish1 = p1.waitFor(10, TimeUnit.SECONDS);
				boolean finish2 = p2.waitFor(10, TimeUnit.SECONDS);

				boolean finish3 = p3.waitFor(10, TimeUnit.SECONDS);

				if (!finish1) {
					p1.destroyForcibly(); // 超时后强制终止
					throw new RuntimeException("命令执行超时");
				}
				if (!finish2) {
					p2.destroyForcibly(); // 超时后强制终止
					throw new RuntimeException("命令执行超时");
				}
				if (!finish3) {
					p3.destroyForcibly(); // 超时后强制终止
					throw new RuntimeException("命令执行超时");
				}

				// System.out.println("ADB forward命令已执行");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// System.out.println("端口已开启");
		}
		if (1 == 1)
			return;

		String vid = "186433";// args[0];

		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();

		StringBuffer m3u8Result = new StringBuffer();

		String videoUrl = "";

		try {
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

			String articleUrlString = vid;
			// attr("href").replace("/cn/movie/", "");
			articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
					+ articleUrlString;
			// http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id=47617

			Response articleRes = Jsoup.connect(articleUrlString)
					.header("Content-Type", "application/json; charset=utf-8")
					.header("Cookie", "PHPSESSID=s8ui3qddpcbmbeotptj2alfcnd").header("Host", "127.0.0.1:54545")
					.header("User-Agent",
							"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
					.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
					.execute();

			String sourceString = articleRes.body();
//					System.out.println("------------------");
//					System.out.println(sourceString);
			JSONObject jsonObject = new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);

			// 提取URL
			videoUrl = jsonObject.getString("url");
			System.out.println(videoUrl);

		} catch (Exception e) {
			System.out.println(vid);

			FileUtil.appendUtf8String(vid + "\n", "error.txt");

			// TODO: handle exception
		}
		// return videoUrl;

	}

	public static void go() {
//		List<String> resultList = new ArrayList<String>();

		String host = "localhost"; // 或者其他IP地址
		int port = 54545; // 你要检查的端口号
		if (!isPortOpen(host, port) || 1 == 1) {
			// 端口未开启，执行ADB命令
			try {
				Process p1 = Runtime.getRuntime().exec("adb connect 192.168.1.47:8888");
				boolean f1 = p1.waitFor(10, TimeUnit.SECONDS);

				Process p2 = Runtime.getRuntime().exec("adb forward tcp:12346 tcp:12346");
				boolean f2 = p2.waitFor(10, TimeUnit.SECONDS);

				Process p3 = Runtime.getRuntime().exec(buildLoCommand());
				boolean f3 = p3.waitFor(10, TimeUnit.SECONDS);

				if (!f1) {
					p1.destroyForcibly(); // 超时后强制终止
					throw new RuntimeException("命令执行超时");
				}
				if (!f2) {
					p2.destroyForcibly(); // 超时后强制终止
					throw new RuntimeException("命令执行超时");
				}
				if (!f3) {
					p3.destroyForcibly(); // 超时后强制终止
					throw new RuntimeException("命令执行超时");
				}

				// System.out.println("ADB forward命令已执行");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// System.out.println("端口已开启");
		}
		if (1 == 1)
			return;

		String vid = "186433";// args[0];

		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();

		StringBuffer m3u8Result = new StringBuffer();

		String videoUrl = "";

		try {
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

			String articleUrlString = vid;
			// attr("href").replace("/cn/movie/", "");
			articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
					+ articleUrlString;
			// http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id=47617

			Response articleRes = Jsoup.connect(articleUrlString)
					.header("Content-Type", "application/json; charset=utf-8")
					.header("Cookie", "PHPSESSID=s8ui3qddpcbmbeotptj2alfcnd").header("Host", "127.0.0.1:54545")
					.header("User-Agent",
							"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
					.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
					.execute();

			String sourceString = articleRes.body();
//					System.out.println("------------------");
//					System.out.println(sourceString);
			JSONObject jsonObject = new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);

			// 提取URL
			videoUrl = jsonObject.getString("url");
			System.out.println(videoUrl);

		} catch (Exception e) {
			System.out.println(vid);

			FileUtil.appendUtf8String(vid + "\n", "error.txt");

			// TODO: handle exception
		}
		// return videoUrl;

	}

	public static boolean isPortOpen(String host, int port) {
		try (Socket socket = new Socket(InetAddress.getByName(host), port)) {
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
