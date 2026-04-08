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

//为数据库缺失分享记录的设置分享链接152

public class TextProcessorLocalYunpan7 {

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
								"_UP_A4A_11_=wb9cf12999f44cd8a47eb09e4db32399; __pus=90561191fb15f1f17808d8817903fcdcAAS+X9StGNLeHIgsVlCPfY4F8bQn3dI3tJZ566GyztDbNMo68bpXCw3I6mK4I5zMKCXpmrmqXHyD8xZwD0ygyq6D; __kp=4c5ae0a0-cade-11f0-b168-cb887c6a75b1; __kps=AAS5hcgdgt6p+1EIB02h/tr5; __ktd=MVA089y4vvW2XOFMIuV5jw==; __uid=AAS5hcgdgt6p+1EIB02h/tr5; b-user-id=58a90ff1-3c86-a1e9-5d07-75969a9a415d; __sdid=AASVTQQYeoadAaspk4RTeonVN25uUlOqSQjiPkkcW1UrKIhQciIXlUUzt5/N7CA4mpw=; xlly_s=1; isg=BF9faAxRSi_URE-Mpt1qi9Xi7rPpxLNm-kuD1fGs1I5VgH8C-ZYYtt1TRhD-fIve; tfstk=gn0rvtTfmULr0bWXPxUeuv-hEhz8zyJ_E2wQtXc3N82uR3nE0XGwwWMStJoUnXelqWbkoZV3sWjk98bU3-DmNawQrJfEdXiSLXd8tJDnLJiWcFG-wyUHC2865bLN6aw-zyXIizFQkIXhmFG-wsfPKCiD5e8mFrVuKvqoitVgn723Zvf0o-FLtTVhrIl0H-VlZy2uoZVg6TboKyAqi-F3-JD3qIl0HW43Kj4KS2L4_uAxtxC-YMzL4Rcu3Nlxu7V12b2VHt34auyG8-7h-qPrtorDkN-QIce7fJMDlazEiWk4f0JNzvln6Dqq8TASIXoqpzgDtg2iVYZnro5HsR0-iVU0cFA4r2kxXREXDKFqPVZTBoWD78iqn4rmSK--alhqrl4MWT2IcfuUT2ppuAln6Dqq8TSPU9FmiDgK49jUqSF4CIR2qd2DtCpMc5jdvud8gRORwMILqSF4CIRVvME-pSy6w_C..; __puus=21f5f9e1ba996385b5655928b6d25dc6AASUhn/7ZUlOCjxQ4mtnOO17uUXfHQcFg/dOvlNQiNOnE3cOgmCGK8+jKw2cH3OYqwFAL/q+JeU+pqmwkK/358u91GBr+BhGsQE0lMpVM1L6prFaUlvRxENLg3VgrDvp5VQI0OBMg/Df9MQ52L+YzTpdMFZ9gnn0eAq6QjHIw/Y7ybZOAhiBL8kv3IKioYt4HvbJI7/zXiJSUBsRgLTsmpIo")

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
					Waiwang2Video video = roomService.getVideo(vidString);
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
						"_UP_A4A_11_=wb9cf12999f44cd8a47eb09e4db32399; __pus=90561191fb15f1f17808d8817903fcdcAAS+X9StGNLeHIgsVlCPfY4F8bQn3dI3tJZ566GyztDbNMo68bpXCw3I6mK4I5zMKCXpmrmqXHyD8xZwD0ygyq6D; __kp=4c5ae0a0-cade-11f0-b168-cb887c6a75b1; __kps=AAS5hcgdgt6p+1EIB02h/tr5; __ktd=MVA089y4vvW2XOFMIuV5jw==; __uid=AAS5hcgdgt6p+1EIB02h/tr5; b-user-id=58a90ff1-3c86-a1e9-5d07-75969a9a415d; __sdid=AASVTQQYeoadAaspk4RTeonVN25uUlOqSQjiPkkcW1UrKIhQciIXlUUzt5/N7CA4mpw=; xlly_s=1; isg=BF9faAxRSi_URE-Mpt1qi9Xi7rPpxLNm-kuD1fGs1I5VgH8C-ZYYtt1TRhD-fIve; tfstk=gn0rvtTfmULr0bWXPxUeuv-hEhz8zyJ_E2wQtXc3N82uR3nE0XGwwWMStJoUnXelqWbkoZV3sWjk98bU3-DmNawQrJfEdXiSLXd8tJDnLJiWcFG-wyUHC2865bLN6aw-zyXIizFQkIXhmFG-wsfPKCiD5e8mFrVuKvqoitVgn723Zvf0o-FLtTVhrIl0H-VlZy2uoZVg6TboKyAqi-F3-JD3qIl0HW43Kj4KS2L4_uAxtxC-YMzL4Rcu3Nlxu7V12b2VHt34auyG8-7h-qPrtorDkN-QIce7fJMDlazEiWk4f0JNzvln6Dqq8TASIXoqpzgDtg2iVYZnro5HsR0-iVU0cFA4r2kxXREXDKFqPVZTBoWD78iqn4rmSK--alhqrl4MWT2IcfuUT2ppuAln6Dqq8TSPU9FmiDgK49jUqSF4CIR2qd2DtCpMc5jdvud8gRORwMILqSF4CIRVvME-pSy6w_C..; __puus=21f5f9e1ba996385b5655928b6d25dc6AASUhn/7ZUlOCjxQ4mtnOO17uUXfHQcFg/dOvlNQiNOnE3cOgmCGK8+jKw2cH3OYqwFAL/q+JeU+pqmwkK/358u91GBr+BhGsQE0lMpVM1L6prFaUlvRxENLg3VgrDvp5VQI0OBMg/Df9MQ52L+YzTpdMFZ9gnn0eAq6QjHIw/Y7ybZOAhiBL8kv3IKioYt4HvbJI7/zXiJSUBsRgLTsmpIo")
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
						"_UP_A4A_11_=wb9cf12999f44cd8a47eb09e4db32399; __pus=90561191fb15f1f17808d8817903fcdcAAS+X9StGNLeHIgsVlCPfY4F8bQn3dI3tJZ566GyztDbNMo68bpXCw3I6mK4I5zMKCXpmrmqXHyD8xZwD0ygyq6D; __kp=4c5ae0a0-cade-11f0-b168-cb887c6a75b1; __kps=AAS5hcgdgt6p+1EIB02h/tr5; __ktd=MVA089y4vvW2XOFMIuV5jw==; __uid=AAS5hcgdgt6p+1EIB02h/tr5; b-user-id=58a90ff1-3c86-a1e9-5d07-75969a9a415d; __sdid=AASVTQQYeoadAaspk4RTeonVN25uUlOqSQjiPkkcW1UrKIhQciIXlUUzt5/N7CA4mpw=; xlly_s=1; isg=BF9faAxRSi_URE-Mpt1qi9Xi7rPpxLNm-kuD1fGs1I5VgH8C-ZYYtt1TRhD-fIve; tfstk=gn0rvtTfmULr0bWXPxUeuv-hEhz8zyJ_E2wQtXc3N82uR3nE0XGwwWMStJoUnXelqWbkoZV3sWjk98bU3-DmNawQrJfEdXiSLXd8tJDnLJiWcFG-wyUHC2865bLN6aw-zyXIizFQkIXhmFG-wsfPKCiD5e8mFrVuKvqoitVgn723Zvf0o-FLtTVhrIl0H-VlZy2uoZVg6TboKyAqi-F3-JD3qIl0HW43Kj4KS2L4_uAxtxC-YMzL4Rcu3Nlxu7V12b2VHt34auyG8-7h-qPrtorDkN-QIce7fJMDlazEiWk4f0JNzvln6Dqq8TASIXoqpzgDtg2iVYZnro5HsR0-iVU0cFA4r2kxXREXDKFqPVZTBoWD78iqn4rmSK--alhqrl4MWT2IcfuUT2ppuAln6Dqq8TSPU9FmiDgK49jUqSF4CIR2qd2DtCpMc5jdvud8gRORwMILqSF4CIRVvME-pSy6w_C..; __puus=21f5f9e1ba996385b5655928b6d25dc6AASUhn/7ZUlOCjxQ4mtnOO17uUXfHQcFg/dOvlNQiNOnE3cOgmCGK8+jKw2cH3OYqwFAL/q+JeU+pqmwkK/358u91GBr+BhGsQE0lMpVM1L6prFaUlvRxENLg3VgrDvp5VQI0OBMg/Df9MQ52L+YzTpdMFZ9gnn0eAq6QjHIw/Y7ybZOAhiBL8kv3IKioYt4HvbJI7/zXiJSUBsRgLTsmpIo")
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
