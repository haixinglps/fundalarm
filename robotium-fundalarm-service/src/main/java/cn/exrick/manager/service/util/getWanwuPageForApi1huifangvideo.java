package cn.exrick.manager.service.util;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;

public class getWanwuPageForApi1huifangvideo {

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();
		// 435506,435632,435566,435442,435402 这几个人的27部。
		String id = "281209";
		int page = 1;
		int pageEnd = 1;
		int type = 0;
		String keywordString = null;// 酒店调教服务生 鞋交手足榨
		int diskNum = 2;// 0 k盘 1m盘
		String customerQQ = "";
		int write = 0;

		// 开始执行
		String url = "http://127.0.0.1:54545/appapi/?service=MissVideo.getHomeVideosOfUser&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&touid="
				+ id + "&type=" + type + "&pnum=10&p=";
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();
		for (int k = page; k <= pageEnd; k++) {// 1910
			StringBuffer m3u8Result = new StringBuffer();
			try {
				String pU = url + k;

//				String res = HttpUtil.get(pU);
				System.out.println(pU);

				// 提取res中的链接
				String docJson = Jsoup.connect(pU).ignoreContentType(true).execute().body();
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				JSONArray links = object.getJSONObject("data").getJSONArray("info");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
				if (links.size() == 0)
					break;

				for (int j = 0; j < links.size(); j++) {

					cn.hutool.json.JSONObject link = links.getJSONObject(j);
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

					if (keywordString != null) {
						String titleDb = link.getStr("title");
						if (keywordString.contentEquals("hld")) {
							if (!titleDb.contains("\uD83D\uDEA5"))
								continue;
						} else {

							if (titleDb.indexOf(keywordString) == -1)
								continue;
						}

					}

					String articleUrlString = link.getStr("id");
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
					System.out.println("------------------");
					System.out.println(sourceString);
					JSONObject jsonObject = new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);

					// 提取URL
					String videoUrl = jsonObject.getString("url");
					JSONObject ui = jsonObject.getJSONObject("userinfo");
					String vid = "作品id--" + jsonObject.getString("id") + "_用户id--" + jsonObject.getString("uid")
							+ "_昵称--" + jsonObject.getString("nickname") + "_拍摄地--" + jsonObject.getString("city")
							+ "_纬度--" + jsonObject.getString("lat") + "_经度--" + jsonObject.getString("lng") + "_常住省份--"
							+ ui.getString("province") + "_常住城市--" + ui.getString("city") + "_常住地址--"
							+ ui.getString("location") + "_余额--" + (ui.has("balance") ? ui.getString("balance") : "")
							+ "_粉丝数--" + ui.getString("fans") + "_" + customerQQ;
					String img = jsonObject.getString("cover_pic");
//					if (link.getStr("originalname") != null && !link.getStr("originalname").equals("")) {
					if (urlSet.add(videoUrl)) {
						System.out.println(k + "-" + j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

						m3u8Result.append(jsonObject.getString("title") + "_" + vid + "\t" + videoUrl + "\t"
								+ jsonObject.getString("id") + "\t" + img + "\n");
					}
//					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("-----------------url下载异常：");
				// TODO: handle exception
			}
			FileUtil.writeUtf8String(m3u8Result.toString(), "d:/m3u8ListTaoluzhuboapi/" + id + "_" + type + "_" + k
					+ "_" + System.currentTimeMillis() + ".txt");

		}
		getWanwuM3u8ForApi.geneBat(args, diskNum, write);

	}

}
