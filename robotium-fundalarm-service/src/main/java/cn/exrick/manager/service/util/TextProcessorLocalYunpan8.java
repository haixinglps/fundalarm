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

import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.service.RobotService;

//为数据库wanwu_video缺失分享记录的设置分享链接178

public class TextProcessorLocalYunpan8 {

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
			List<WanwuVideo> vidoes = roomService.getAllVideo2(i, 10);
			if (vidoes.size() == 0)
				break;
			for (int j = 0; j < vidoes.size(); j++) {
				WanwuVideo video = vidoes.get(j);
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
//					System.out.println(articleUrlString);
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
								"_UP_A4A_11_=wb9cf12999f44cd8a47eb09e4db32399; b-user-id=58a90ff1-3c86-a1e9-5d07-75969a9a415d; __sdid=AASVTQQYeoadAaspk4RTeonVN25uUlOqSQjiPkkcW1UrKIhQciIXlUUzt5/N7CA4mpw=; xlly_s=1; _UP_D_=pc; _UP_F7E_8D_=smKZ6VuDKwnfnc3502X4Rksem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM76U5K73pLyh4H%2B31svbkZhvOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhusewZI4nEXzuF4RLAenhJ7yQvHYZLrymgorEx6W10AMZxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; __pus=ef6418d27108688e773288f96cd8b376AAQKHqaW6hEaOFSfeLuTkyoa5eXSGT0E1H8hW39J3Sq+k3PcnK8vc3MH49xQPiwTPKwoABOfIRBBwdKJLWBpSAIV; __kp=c76665d0-db87-11f0-a3bc-1b087ad833a1; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; isg=BAIC7__43577ncKHO4rf0MBdUwhk0wbtj4TOnkwbC3Ugn6MZNGJj_ZfWS5vjz36F; tfstk=gGfxYd_-YuqcHUN7vj2ujTdh46zkW8bV2i7IshxmCgIR7GpMCKYD6dsXqsvMGnj8NGjOnPra7UTW-GofGIsg6h_e4jjg3dctu_IKsjq43NG9sCZ3xWVhuERw1kDI6nTx3FTsl5xXhIR5uvQAxWVhuZgDlyb7ttxymxL6fC9X10iW4En61jT_y3T98AiffCa8yFTihjG65aMWSEd6fGO_yaty5CTffCaRP38siq2Jbrtt6tjwPwm_Sphs1_LvF8bXVDpcNELWvZ1x1fgWkKKCl3ExmTPeFNJO_Vl9LZ6FbesjclKNwa1fdMZonn6pWwXOvWGwztsANdCu7W7JMnI6BTrKtEdceLLRpycpIIfHdI6uUu9MMeCJUsUKhnB1RdfW_kGXXd6lgp50VjdCLZAcCMZonn6p5gkFt6pxcDxpSxa8yAkwhURcJSd3n68OXUK3rEDZQLbyyH48yAkwhU8JxzmiQAJlz; __puus=5eaa06faa362e763cccce9eb23851c18AATUWlRqG+XKfscM8UdeWGo2ZS+3XrCrgNCQjzgkGoSYPqvTPii3X3ZyijJ0xaMBlMoXdbLYJ44A7SDNSBkg1h6sTOIW4m7eYKP9IYGb9YzBv++KU0CCGMfZoZwIO35le5xojuETeIjiKZeygNBe0aqPp2JdJfdizC7OoUpM9v1aD/Sv1xuVWNjg8jvxwfLXU9vfNJs84ni30JTaISKslnJG")

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
					// 开始分享：
					JSONObject fileObject = jo.getJSONObject("data").getJSONArray("list").getJSONObject(0);
					WanwuVideo video = roomService.getVideoWw(vidString);
					try {
						shareurl = share(fileObject.getString("fid"), fileObject.getString("file_name"));
						System.out.println(rtmpUrl + "----->" + shareurl);
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
						roomService.updateLocationww(vidString, shareurl);
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
						"_UP_A4A_11_=wb9cf12999f44cd8a47eb09e4db32399; b-user-id=58a90ff1-3c86-a1e9-5d07-75969a9a415d; __sdid=AASVTQQYeoadAaspk4RTeonVN25uUlOqSQjiPkkcW1UrKIhQciIXlUUzt5/N7CA4mpw=; xlly_s=1; _UP_D_=pc; _UP_F7E_8D_=smKZ6VuDKwnfnc3502X4Rksem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM76U5K73pLyh4H%2B31svbkZhvOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhusewZI4nEXzuF4RLAenhJ7yQvHYZLrymgorEx6W10AMZxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; __pus=ef6418d27108688e773288f96cd8b376AAQKHqaW6hEaOFSfeLuTkyoa5eXSGT0E1H8hW39J3Sq+k3PcnK8vc3MH49xQPiwTPKwoABOfIRBBwdKJLWBpSAIV; __kp=c76665d0-db87-11f0-a3bc-1b087ad833a1; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; isg=BAIC7__43577ncKHO4rf0MBdUwhk0wbtj4TOnkwbC3Ugn6MZNGJj_ZfWS5vjz36F; tfstk=gGfxYd_-YuqcHUN7vj2ujTdh46zkW8bV2i7IshxmCgIR7GpMCKYD6dsXqsvMGnj8NGjOnPra7UTW-GofGIsg6h_e4jjg3dctu_IKsjq43NG9sCZ3xWVhuERw1kDI6nTx3FTsl5xXhIR5uvQAxWVhuZgDlyb7ttxymxL6fC9X10iW4En61jT_y3T98AiffCa8yFTihjG65aMWSEd6fGO_yaty5CTffCaRP38siq2Jbrtt6tjwPwm_Sphs1_LvF8bXVDpcNELWvZ1x1fgWkKKCl3ExmTPeFNJO_Vl9LZ6FbesjclKNwa1fdMZonn6pWwXOvWGwztsANdCu7W7JMnI6BTrKtEdceLLRpycpIIfHdI6uUu9MMeCJUsUKhnB1RdfW_kGXXd6lgp50VjdCLZAcCMZonn6p5gkFt6pxcDxpSxa8yAkwhURcJSd3n68OXUK3rEDZQLbyyH48yAkwhU8JxzmiQAJlz; __puus=5eaa06faa362e763cccce9eb23851c18AATUWlRqG+XKfscM8UdeWGo2ZS+3XrCrgNCQjzgkGoSYPqvTPii3X3ZyijJ0xaMBlMoXdbLYJ44A7SDNSBkg1h6sTOIW4m7eYKP9IYGb9YzBv++KU0CCGMfZoZwIO35le5xojuETeIjiKZeygNBe0aqPp2JdJfdizC7OoUpM9v1aD/Sv1xuVWNjg8jvxwfLXU9vfNJs84ni30JTaISKslnJG")
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
						"_UP_A4A_11_=wb9cf12999f44cd8a47eb09e4db32399; b-user-id=58a90ff1-3c86-a1e9-5d07-75969a9a415d; __sdid=AASVTQQYeoadAaspk4RTeonVN25uUlOqSQjiPkkcW1UrKIhQciIXlUUzt5/N7CA4mpw=; xlly_s=1; _UP_D_=pc; _UP_F7E_8D_=smKZ6VuDKwnfnc3502X4Rksem8D%2BGJzLUdRO7YsGUI%2BibipNZ4wWoKA3nkVDHeUjK77U6xYgqMbXDik4ZZxmO%2B6jibaMeh%2F15GPrrCl5M87ivs0EP%2FjAQTQimMgEdat62Byd22%2BZGM76U5K73pLyh4H%2B31svbkZhvOTidzNw8s%2FWtKAIxWbnCzZn4%2FJMBUub0OScUYeEhusewZI4nEXzuF4RLAenhJ7yQvHYZLrymgorEx6W10AMZxU4wR0Pq7NklczEGdRq2nIAcu7v22Uw2o%2FxMY0xBdeC9Korm5%2FNHnxl6K%2Bd6FXSoT9a3XIMQO359auZPiZWzrNlZe%2BqnOahXcx7KAhQIRqSOapSmL4ygJor4r5isJhRuDoXy7vJAVuH%2FRDtEJJ8rZTq0BdC23Bz%2B0MrsdgbK%2BiW; __pus=ef6418d27108688e773288f96cd8b376AAQKHqaW6hEaOFSfeLuTkyoa5eXSGT0E1H8hW39J3Sq+k3PcnK8vc3MH49xQPiwTPKwoABOfIRBBwdKJLWBpSAIV; __kp=c76665d0-db87-11f0-a3bc-1b087ad833a1; __kps=AAT818QctXDh3OaWm2AiJB05; __ktd=o4NCSxaoZXEtgigIAvSYEw==; __uid=AAT818QctXDh3OaWm2AiJB05; isg=BAIC7__43577ncKHO4rf0MBdUwhk0wbtj4TOnkwbC3Ugn6MZNGJj_ZfWS5vjz36F; tfstk=gGfxYd_-YuqcHUN7vj2ujTdh46zkW8bV2i7IshxmCgIR7GpMCKYD6dsXqsvMGnj8NGjOnPra7UTW-GofGIsg6h_e4jjg3dctu_IKsjq43NG9sCZ3xWVhuERw1kDI6nTx3FTsl5xXhIR5uvQAxWVhuZgDlyb7ttxymxL6fC9X10iW4En61jT_y3T98AiffCa8yFTihjG65aMWSEd6fGO_yaty5CTffCaRP38siq2Jbrtt6tjwPwm_Sphs1_LvF8bXVDpcNELWvZ1x1fgWkKKCl3ExmTPeFNJO_Vl9LZ6FbesjclKNwa1fdMZonn6pWwXOvWGwztsANdCu7W7JMnI6BTrKtEdceLLRpycpIIfHdI6uUu9MMeCJUsUKhnB1RdfW_kGXXd6lgp50VjdCLZAcCMZonn6p5gkFt6pxcDxpSxa8yAkwhURcJSd3n68OXUK3rEDZQLbyyH48yAkwhU8JxzmiQAJlz; __puus=5eaa06faa362e763cccce9eb23851c18AATUWlRqG+XKfscM8UdeWGo2ZS+3XrCrgNCQjzgkGoSYPqvTPii3X3ZyijJ0xaMBlMoXdbLYJ44A7SDNSBkg1h6sTOIW4m7eYKP9IYGb9YzBv++KU0CCGMfZoZwIO35le5xojuETeIjiKZeygNBe0aqPp2JdJfdizC7OoUpM9v1aD/Sv1xuVWNjg8jvxwfLXU9vfNJs84ni30JTaISKslnJG")
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
