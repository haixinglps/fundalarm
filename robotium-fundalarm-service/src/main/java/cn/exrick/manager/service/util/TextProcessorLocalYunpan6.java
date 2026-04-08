package cn.exrick.manager.service.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.coremedia.iso.IsoFile;

import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.service.RobotService;
//为数据库缺失分享记录的设置分享链接178

public class TextProcessorLocalYunpan6 {

	private static ExecutorService executor = Executors.newFixedThreadPool(1);
	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");

	static RobotService roomService = null;
	static {

		roomService = context.getBean(RobotService.class);

	}

	public static void main(String[] args) {
		// 178

//		System.setProperty("http.proxyHost", "localhost");
//		System.setProperty("http.proxyPort", "7890");
//		// 对于HTTPS
//		System.setProperty("https.proxyHost", "localhost");
//		System.setProperty("https.proxyPort", "7890");

//		doWith("\\\\MS-LNKKZQAQDHXV\\c\\XSBDownload\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\d\\360Downloads\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\e\\XSBDownload");
//		doWith("\\\\MS-LNKKZQAQDHXV\\e\\m3u8work\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\f\\Downloads");
//		doWith("\\\\MS-LNKKZQAQDHXV\\f");
//		doWith("\\\\MS-LNKKZQAQDHXV\\f\\录制中心");

//		while (true) {
		System.out.println("scaning...");
		doWith("f:\\Downloads");
//			doWith("f:");
//			doWith("f:\\C盘瘦身目录");
//
//			doWith("f:\\录制中心");
//			System.out.println("scaning ok...");
		executor.shutdown();
//			System.out.println("shutdown ok...");
//			try {
//				Thread.sleep(120000);
//				executor = Executors.newFixedThreadPool(50);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

//		}

	}

	public static void doWith(String path) {

		for (int i = 1;; i++) {
			List<Waiwang2Video> vidoes = roomService.getAllVideo(i, 10);
			if (vidoes.size() == 0)
				break;
			for (int j = 0; j < vidoes.size(); j++) {
				Waiwang2Video video = vidoes.get(j);
				executor.submit(new DownVid(video.getVid() + "_", video.getVid() + ""));

			}

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
					System.out.println(articleUrlString);
					articleUrlString = URLEncoder.encode(articleUrlString, "UTF-8");

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
								"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_335_2B_=1; _UP_D_=pc; _UP_30C_6A_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_TS_=sg1a749cec18934cec528de148c927b650d; _UP_E37_B7_=sg1a749cec18934cec528de148c927b650d; _UP_TG_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_F7E_8D_=smKZ6VuDKwki06CdVNYibEsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6XKu9ZLSu8ycXRgREMCL23vOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhutrRMcTa22fnM13pzR%2BsbbOVsg1OU1mtpMiYxCeyuGayxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; tfstk=gzASHfXjsuq5EQJOF6u41Oq-M45IbqlZPy_pSeFzJ_C89HTh0ayrE9oBhHKV2Qkl4nsB7eVF4U5-RHt9V9-B-4bdvHKCYLkq_UYlt6nwOflwrYgFMKR59k8AZwJexaeo_UYuk6nZbflaRPgEPgIpvTCAkNSYv6BRJqsARN2L2HCKlq_F7zURyTLAkN7R9MKd9qTAmwCdyHCKlEIcJjcQNZy52Ug1LlLt9fjMyiNL9QhhFGT7LWFpMa65XUIXPUOfPTsOpftFAQK2JQRlnYyltEJ6ApKt08SJCNtfQLi_NhtlJnQv42yhlpTps_X7JWLXlepNhIuts_1pHKC23xmAL_tWIGXqnmxBpERdGBnTGMCcc_WvOmh1ad8wtOd-NljVIwtfQLi_NhIzM5Ss7GeQlOVdlGujlJ2hmnCSVPteRDXRoZoElqZSKTQclGujlJ2heZb2zqgbVvf..; __pus=35cf49cd51a9e9a3b5a4ab7f621a8cbcAAR/o7P3QfbYG1Px1KT/QwUYh+iUw8cV8m0DRTMI1Swc/IsGuuZq2S7LeK8IF60izKDklwT9gCJdybihJh5Llnbb; __kp=cec3a650-7f82-11f0-9784-b7f85c90c87f; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; __puus=19982ead0765a7c18f6994c9d18e5988AATUWlRqG+XKfscM8UdeWGo27m3SQfo79IJKao7NGeo99SJOqONsJDu6cH3PObY3jXC0B0/Pa9m0olecgpBwAUsCPf7Bu7ldHSCedEvbSYL5spi2z3NBZl1bfxDM1dZ7QN44D0b6ANAFuOPiEDjyO580sak459ntjqr33ICtyEhb16t9h4U+2YYvL0TLU8x6O5j440OXHjrJ8aA699L+js7i")

						.header("User-Agent",
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
						.execute();

				String sourceString = articleRes.body();
				System.out.println(sourceString);
				JSONObject jo = new JSONObject(sourceString);
				int total = jo.getJSONObject("metadata").getInt("_total");

//				System.out.println(rtmpUrl + "-m3u8:\t" + total + "\t" + articleUrlString);
				String vidString = rtmpUrl.substring(0, rtmpUrl.indexOf("_"));
				String shareurl = null;
				if (total == 1) {
					System.out.println("total:" + total);

					// 开始分享：
					JSONObject fileObject = jo.getJSONObject("data").getJSONArray("list").getJSONObject(0);
					Waiwang2Video video = roomService.getVideo(vidString);
					try {
						shareurl = share(fileObject.getString("fid"), fileObject.getString("file_name"));
						System.out.println(rtmpUrl + "--暂停3秒，防止封号--->" + shareurl);
						Thread.sleep(3000);
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}

//					m3u8Result.append(rtmpUrl + "\t" + (total > 0 ? 1 : 0));
//					System.out.println("删除此文件：" + ffmpegPath);
////					new File(ffmpegPath).delete();
//
//					// NIO API提供详细错误信息
//					Path path = Paths.get(ffmpegPath);
//					Files.delete(path); // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}
//
//					System.out.println("删除成功");
					// writer.write(m3u8Result.toString());

					if (video != null && shareurl != null) {
						// 更新分享链接
						roomService.updateLocation(vidString, shareurl);
					}

				}

			} catch (Exception e) {
				// e.printStackTrace();

				// writerErr.write(rtmpUrl);

			} finally {
			}

		}
	}

	public static String share(String id, String title) throws IOException {
		String articleUrlString = "https://drive-pc.quark.cn/1/clouddrive/share?pr=ucpro&fr=pc&uc_param_str=";

		String url = articleUrlString;
//		System.out.println(articleUrlString);
//		origin: https://pan.quark.cn
//			referer: https://pan.quark.cn/

		Document articleRes = Jsoup.connect(articleUrlString).header("Content-Type", "application/json; charset=utf-8")
				.requestBody(
						"{\"fid_list\":[\"" + id + "\"],\"title\":\"" + title + "\",\"url_type\":1,\"expired_type\":1}")
				.header("Cookie",
						"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_335_2B_=1; _UP_D_=pc; _UP_30C_6A_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_TS_=sg1a749cec18934cec528de148c927b650d; _UP_E37_B7_=sg1a749cec18934cec528de148c927b650d; _UP_TG_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_F7E_8D_=smKZ6VuDKwki06CdVNYibEsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6XKu9ZLSu8ycXRgREMCL23vOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhutrRMcTa22fnM13pzR%2BsbbOVsg1OU1mtpMiYxCeyuGayxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; tfstk=gzASHfXjsuq5EQJOF6u41Oq-M45IbqlZPy_pSeFzJ_C89HTh0ayrE9oBhHKV2Qkl4nsB7eVF4U5-RHt9V9-B-4bdvHKCYLkq_UYlt6nwOflwrYgFMKR59k8AZwJexaeo_UYuk6nZbflaRPgEPgIpvTCAkNSYv6BRJqsARN2L2HCKlq_F7zURyTLAkN7R9MKd9qTAmwCdyHCKlEIcJjcQNZy52Ug1LlLt9fjMyiNL9QhhFGT7LWFpMa65XUIXPUOfPTsOpftFAQK2JQRlnYyltEJ6ApKt08SJCNtfQLi_NhtlJnQv42yhlpTps_X7JWLXlepNhIuts_1pHKC23xmAL_tWIGXqnmxBpERdGBnTGMCcc_WvOmh1ad8wtOd-NljVIwtfQLi_NhIzM5Ss7GeQlOVdlGujlJ2hmnCSVPteRDXRoZoElqZSKTQclGujlJ2heZb2zqgbVvf..; __pus=35cf49cd51a9e9a3b5a4ab7f621a8cbcAAR/o7P3QfbYG1Px1KT/QwUYh+iUw8cV8m0DRTMI1Swc/IsGuuZq2S7LeK8IF60izKDklwT9gCJdybihJh5Llnbb; __kp=cec3a650-7f82-11f0-9784-b7f85c90c87f; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; __puus=19982ead0765a7c18f6994c9d18e5988AATUWlRqG+XKfscM8UdeWGo27m3SQfo79IJKao7NGeo99SJOqONsJDu6cH3PObY3jXC0B0/Pa9m0olecgpBwAUsCPf7Bu7ldHSCedEvbSYL5spi2z3NBZl1bfxDM1dZ7QN44D0b6ANAFuOPiEDjyO580sak459ntjqr33ICtyEhb16t9h4U+2YYvL0TLU8x6O5j440OXHjrJ8aA699L+js7i")
				.header("origin", "https://pan.quark.cn").header("referer", "https://pan.quark.cn/")
				.header("User-Agent",
						"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
				.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true).post();
		String res = articleRes.body().text().toString();
		System.out.println(res);
		cn.hutool.json.JSONObject jObject = new cn.hutool.json.JSONObject(res);
		String sid = jObject.getJSONObject("data").getJSONObject("task_resp").getJSONObject("data").getStr("share_id");
		Document articleRes2 = Jsoup
				.connect("https://drive-pc.quark.cn/1/clouddrive/share/password?pr=ucpro&fr=pc&uc_param_str=")
				.header("Content-Type", "application/json; charset=utf-8").requestBody("{\"share_id\":\"" + sid + "\"}")
				.header("Cookie",
						"b-user-id=2501f9c3-07be-8fd1-b355-6e281ae46cf2; __sdid=AAT3ijezk3W6wDHWhwRfijol0ILFKIXsxqEgakOfYbAjxZ6dif6L2el8DvfhPs79CQU=; _UP_A4A_11_=wb9cb1abc7a641c0a66e2dbabad96003; isg=BCcnENiEA6oERYc0rsWy011KtlvxrPuOYtNLjfmVILbd6EOqAX9m3xirDuj2ZNMG; _UP_335_2B_=1; _UP_D_=pc; _UP_30C_6A_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_TS_=sg1a749cec18934cec528de148c927b650d; _UP_E37_B7_=sg1a749cec18934cec528de148c927b650d; _UP_TG_=st9cc620133hkh11hfnz4sejhjof57w8; _UP_F7E_8D_=smKZ6VuDKwki06CdVNYibEsem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM6XKu9ZLSu8ycXRgREMCL23vOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhutrRMcTa22fnM13pzR%2BsbbOVsg1OU1mtpMiYxCeyuGayxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; tfstk=gzASHfXjsuq5EQJOF6u41Oq-M45IbqlZPy_pSeFzJ_C89HTh0ayrE9oBhHKV2Qkl4nsB7eVF4U5-RHt9V9-B-4bdvHKCYLkq_UYlt6nwOflwrYgFMKR59k8AZwJexaeo_UYuk6nZbflaRPgEPgIpvTCAkNSYv6BRJqsARN2L2HCKlq_F7zURyTLAkN7R9MKd9qTAmwCdyHCKlEIcJjcQNZy52Ug1LlLt9fjMyiNL9QhhFGT7LWFpMa65XUIXPUOfPTsOpftFAQK2JQRlnYyltEJ6ApKt08SJCNtfQLi_NhtlJnQv42yhlpTps_X7JWLXlepNhIuts_1pHKC23xmAL_tWIGXqnmxBpERdGBnTGMCcc_WvOmh1ad8wtOd-NljVIwtfQLi_NhIzM5Ss7GeQlOVdlGujlJ2hmnCSVPteRDXRoZoElqZSKTQclGujlJ2heZb2zqgbVvf..; __pus=35cf49cd51a9e9a3b5a4ab7f621a8cbcAAR/o7P3QfbYG1Px1KT/QwUYh+iUw8cV8m0DRTMI1Swc/IsGuuZq2S7LeK8IF60izKDklwT9gCJdybihJh5Llnbb; __kp=cec3a650-7f82-11f0-9784-b7f85c90c87f; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; __puus=19982ead0765a7c18f6994c9d18e5988AATUWlRqG+XKfscM8UdeWGo27m3SQfo79IJKao7NGeo99SJOqONsJDu6cH3PObY3jXC0B0/Pa9m0olecgpBwAUsCPf7Bu7ldHSCedEvbSYL5spi2z3NBZl1bfxDM1dZ7QN44D0b6ANAFuOPiEDjyO580sak459ntjqr33ICtyEhb16t9h4U+2YYvL0TLU8x6O5j440OXHjrJ8aA699L+js7i")
				.header("origin", "https://pan.quark.cn").header("referer", "https://pan.quark.cn/")
				.header("User-Agent",
						"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
				.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true).post();
		String res2 = articleRes2.body().text().toString();
		cn.hutool.json.JSONObject jObject2 = new cn.hutool.json.JSONObject(res2);
		String sid2 = jObject2.getJSONObject("data").getStr("share_url");
		return sid2;

	}

	public static boolean noChineseByChar(String str) {
		if (str == null)
			return true;
		for (char c : str.toCharArray()) {
			if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
				return false;
			}
		}
		return true;
	}

	public static boolean noChineseByRegex(String str) {
		return str == null || !Pattern.compile("[\u4e00-\u9fa5]").matcher(str).find();
	}

	private static Long readDuration(String mp4Path) throws IOException {
		try (IsoFile isoFile = new IsoFile(mp4Path)) {
			long tm = isoFile.getMovieBox().getMovieHeaderBox().getDuration()
					/ isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
//			String roomidString = file.getFileName().toString().substring(0, indexa);
// 			roomService.updateDuration(roomidString, (tm / 60) + ":" + (tm % 60));
			return tm;

		} catch (Exception e2) {
			return null;
// 			if ((e2.getMessage() + "").contentEquals("null")) {
//
//				Path paths = Paths.get(path + "\\" + file.getFileName());
//				try {
//					Files.delete(paths);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} // 抛出具体异常‌:ml-citation{ref="6,7" data="citationList"}

//				return null;
//
//			}
			// e2.printStackTrace();
			// TODO: handle exception
		}
	}

}
