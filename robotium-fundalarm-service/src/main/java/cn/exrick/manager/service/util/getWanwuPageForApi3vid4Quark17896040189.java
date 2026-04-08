package cn.exrick.manager.service.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class getWanwuPageForApi3vid4Quark17896040189 {
	private static final ExecutorService executor = Executors.newFixedThreadPool(50);
	private static final int BATCH_SIZE = 1000; // 分批处理量
	static AtomicInteger processed = new AtomicInteger(0);
	static ThreadSafeFileWriter writer = null;
	static ThreadSafeFileWriter writerLog = null;
	static ThreadSafeFileWriter writerErr = null;

	// 假设已初始化包含100万链接的列表

	public static void main(String[] args) {

//		System.setProperty("http.proxyHost", "localhost");
//		System.setProperty("http.proxyPort", "7890");
//		// 对于HTTPS
//		System.setProperty("https.proxyHost", "localhost");
//		System.setProperty("https.proxyPort", "7890");
		HashSet<String> keySet = new HashSet<String>();
//		List<String> resultList = new ArrayList<String>();
		List<String> millionLinks = new ArrayList();

		List<String> lines = new ArrayList<String>();
//		Path filePath = Paths.get("d:/download/taoluall3.txt");
//		Path filePath = Paths.get("d:/download/wwall.txt");
		Path filePath = Paths.get("d:/download/tla2.txt");
		try {
			lines = Files.readAllLines(filePath);
//            for (String line : lines) {
			System.out.println(lines);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer = new ThreadSafeFileWriter("d:/m3u8ListTaoluzhuboapi/self.txt");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			writerLog = new ThreadSafeFileWriter("d:/download/downloadlog.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writerErr = new ThreadSafeFileWriter("d:/download/downloaderror.txt");
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

				String keys1 = lines.get(j).replace("\"", "");//
				if (keySet.add(keys1))
					millionLinks.add(keys1);
				if (keys1.indexOf("作品id--") != -1) {

					String keys2 = FileNameSanitizer.sanitize(keys1);
					if (keySet.add(keys2))
						millionLinks.add(keys2);

				}

			}
			int total = millionLinks.size();

			for (int i = 0; i < millionLinks.size(); i += BATCH_SIZE) {

				List<String> batch = millionLinks.subList(i, Math.min(i + BATCH_SIZE, millionLinks.size()));
				int end = Math.min(i + BATCH_SIZE, total);
				CountDownLatch batchLatch = new CountDownLatch(batch.size());
				System.out.println("批次开始: " + i + "-" + end);
//				FileUtil.appendUtf8String((vidbegin + i) + "\r\n", "d:/download/downlog.txt");
				writerLog.write((vidbegin + i) + "");
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
				StringBuffer m3u8Result = new StringBuffer();

				String articleUrlString = rtmpUrl + "";
				// 运行完，打开这里也测一下。
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
				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
						.header("Cookie",
								"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_335_2B_=1; _UP_D_=pc; _UP_30C_6A_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_TS_=sg1a749cec18934cec528de148c927b650d; _UP_E37_B7_=sg1a749cec18934cec528de148c927b650d; _UP_TG_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_F7E_8D_=smKZ6VuDKwki06CdVNYibEsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6XKu9ZLSu8ycXRgREMCL23vOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhutrRMcTa22fnM13pzR%2BsbbOVsg1OU1mtpMiYxCeyuGayxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; tfstk=gzASHfXjsuq5EQJOF6u41Oq-M45IbqlZPy_pSeFzJ_C89HTh0ayrE9oBhHKV2Qkl4nsB7eVF4U5-RHt9V9-B-4bdvHKCYLkq_UYlt6nwOflwrYgFMKR59k8AZwJexaeo_UYuk6nZbflaRPgEPgIpvTCAkNSYv6BRJqsARN2L2HCKlq_F7zURyTLAkN7R9MKd9qTAmwCdyHCKlEIcJjcQNZy52Ug1LlLt9fjMyiNL9QhhFGT7LWFpMa65XUIXPUOfPTsOpftFAQK2JQRlnYyltEJ6ApKt08SJCNtfQLi_NhtlJnQv42yhlpTps_X7JWLXlepNhIuts_1pHKC23xmAL_tWIGXqnmxBpERdGBnTGMCcc_WvOmh1ad8wtOd-NljVIwtfQLi_NhIzM5Ss7GeQlOVdlGujlJ2hmnCSVPteRDXRoZoElqZSKTQclGujlJ2heZb2zqgbVvf..; __pus=35cf49cd51a9e9a3b5a4ab7f621a8cbcAAR/o7P3QfbYG1Px1KT/QwUYh+iUw8cV8m0DRTMI1Swc/IsGuuZq2S7LeK8IF60izKDklwT9gCJdybihJh5Llnbb; __kp=cec3a650-7f82-11f0-9784-b7f85c90c87f; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; __puus=19982ead0765a7c18f6994c9d18e5988AATUWlRqG+XKfscM8UdeWGo27m3SQfo79IJKao7NGeo99SJOqONsJDu6cH3PObY3jXC0B0/Pa9m0olecgpBwAUsCPf7Bu7ldHSCedEvbSYL5spi2z3NBZl1bfxDM1dZ7QN44D0b6ANAFuOPiEDjyO580sak459ntjqr33ICtyEhb16t9h4U+2YYvL0TLU8x6O5j440OXHjrJ8aA699L+js7i")

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
					JSONArray list = jo.getJSONObject("data").getJSONArray("list");
					String duration = (list.getJSONObject(0).getInt("duration") / 60) + ":"
							+ (list.getJSONObject(0).getInt("duration") % 60);
					m3u8Result.append(rtmpUrl.replace("作品id_", "作品id--").replace("用户id_", "用户id--") + "\t" + duration);

					writer.write(m3u8Result.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();

				writerErr.write(rtmpUrl);

			} finally {
				countDownLatch.countDown();
			}

		}
	}
}
