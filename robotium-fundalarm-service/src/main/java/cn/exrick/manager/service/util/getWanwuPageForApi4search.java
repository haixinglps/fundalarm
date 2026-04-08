package cn.exrick.manager.service.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;

public class getWanwuPageForApi4search {

	public static void main(String[] args) {
		System.out.println("testing...");

		getWanwuM3u8ForApi3.geneBat(args, 2, 1);

		if (1 == 1)
			return;

//		List<String> resultList = new ArrayList<String>();
		int page = 1;
		int pageEnd = 100;
		int type = 0;
		String keywordStringOri = "凹凸曼";// 小妈地狱高难度 34
		String keywordStringOri2 = null;// "皮靴咀嚼物";// 小妈地狱高难度 34

		// 未知 寸+榨 1
		int diskNum = 2;// 0 k盘 1m盘
		String customerQQ = "";

		String nickname = null;// "玥樾";// "反差楠熙";
		int uid = 0;// 107002; 好玩不如嫂子
		String keywordString = null;

		try {
			keywordString = URLEncoder.encode(keywordStringOri.replace("==", ""), "UTF-8");
			System.out.println(keywordString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type == 1) {
			args = new String[] { "" };
		}

		String url = "http://127.0.0.1:54545/appapi/?service=MissVideo.searchVideo&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2"
				+ "&key=" + keywordString + "&type=" + type + "&pnum=10&p=mypage";// &sign=05285abd75763e1ad24c8974e625958b

		// http://127.0.0.1:54545/appapi/?service=MissVideo.searchVideo&uid=267412&token=abf8552d9c40faefd94e054a4c7a9c99&version=137&platform=2&key=%E9%AB%98%E8%B7%9F%E9%9E%8B&type=1&pnum=10&p=3
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
				String pU = url.replace("mypage", k + "");
				System.out.println("ur:" + pU);

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				// System.out.println("request...");// HttpUtil.get(pU);
				String docJson = Jsoup.connect(pU).timeout(30000).ignoreContentType(true).execute().body();
				// System.out.println("res:" + docJson.length());
//				System.out.println("Debug2:" + (docJson != null)); // 检查返回值
				// System.out.flush(); // 强制刷新
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				JSONArray links = object.getJSONObject("data").getJSONArray("info");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
				if (links.size() == 0)
					break;

				for (int j = 0; j < links.size(); j++) {

					cn.hutool.json.JSONObject link = links.getJSONObject(j);
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本
					if (nickname != null) {
						String nicknameDb = link.getStr("nickname");
						// 相等校验，防止重名。后边加上。
						if (nicknameDb.indexOf(nickname) == -1)
							continue;
					}
					if (uid != 0) {
						int uidDb = link.getInt("uid");
						if (uidDb != uid)
							continue;
					}

					if (keywordStringOri2 != null) {
						String titleDb = link.getStr("title");
						if (titleDb.indexOf(keywordStringOri2) == -1) {
							continue;
						}

					}

					if (1 == 0)
						if (keywordStringOri != null) {
							String titleDb = link.getStr("title");

							if (keywordStringOri.contentEquals("hld")) {
								if (!titleDb.contains("\uD83D\uDEA5寸\uD83D\uDE05榨"))
									continue;
							} else {

//						System.out.println("titledb:" + titleDb);
//						// 相等校验，防止重名。后边加上。
//						System.out.println("keywordstringori:" + keywordStringOri);
								if (keywordStringOri.indexOf("==") != -1) {
									if (titleDb.indexOf(keywordStringOri.replace("==", "")) == -1
											|| !titleDb.contentEquals(keywordStringOri.replace("==", "")))
										continue;
								} else if (titleDb.indexOf(keywordStringOri) == -1)
									continue;

							}

							if (titleDb.indexOf("盯") == -1)
								continue;

						}

					String articleUrlString = link.getStr("id");
					// attr("href").replace("/cn/movie/", "");
					articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
							+ articleUrlString;

					Response articleRes = Jsoup.connect(articleUrlString)
							.header("Content-Type", "application/json; charset=utf-8")
//							.header("Cookie",
//									"hls=s%3Aj9h_seF6e_ES1cK24p8h1WZUGx5Ny-Ba.Ds7igDStVbbGpFkVG0YYnMCJokUn6Lp3k49tqdjR9pE")
							.header("User-Agent",
									"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
							.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
							.execute();

					String sourceString = articleRes.body();
					JSONObject jsonObject = new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);

					// 提取URL
					String videoUrl = jsonObject.getString("url");
					String sk = jsonObject.getString("trial_url");

					JSONObject ui = jsonObject.getJSONObject("userinfo");
					String vid = "作品id--" + jsonObject.getString("id") + "_用户id--" + jsonObject.getString("uid")
							+ "_昵称--" + jsonObject.getString("nickname") + "_拍摄地--" + jsonObject.getString("city")
							+ "_纬度--" + jsonObject.getString("lat") + "_经度--" + jsonObject.getString("lng") + "_常住省份--"
							+ ui.getString("province") + "_常住城市--" + ui.getString("city") + "_常住地址--"
							+ ui.getString("location") + "_余额--" + (ui.has("balance") ? "0" : "") + "_粉丝数--"
							+ ui.getString("fans") + "_" + customerQQ;

					String img = jsonObject.getString("cover_pic");
//					if (link.getStr("originalname") != null && !link.getStr("originalname").equals("")) {
					if (urlSet.add(videoUrl)) {
						System.out.println(k + "-" + j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

						m3u8Result.append(jsonObject.getString("title") + "_" + vid + "\t" + videoUrl + "\t"
								+ jsonObject.getString("id") + "\t" + img + "\t" + sk + "\n");
					}
//					}

				}

			} catch (Exception e) {
				System.out.println("error");
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				// TODO: handle exception
			} catch (Throwable e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			// System.out.println("执行结束");

			FileUtil.writeUtf8String(m3u8Result.toString(),
					"d:/m3u8ListTaoluzhuboapi3/self_" + type + "_" + k + "_" + System.currentTimeMillis() + ".txt");

		}
		getWanwuM3u8ForApi3.geneBat(args, diskNum, 1);

	}

}
