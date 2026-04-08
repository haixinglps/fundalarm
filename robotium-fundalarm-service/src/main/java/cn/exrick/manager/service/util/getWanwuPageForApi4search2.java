package cn.exrick.manager.service.util;

import java.util.HashSet;
import java.util.Set;

import cn.exrick.manager.service.huifang.taoluSignUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;

public class getWanwuPageForApi4search2 {

	public static void main(String[] args) {

//		getWanwuM3u8ForApi.geneBat2(args, 2);
//
//		if (1 == 1)
//			return;
//		List<String> resultList = new ArrayList<String>();
		int page = 1;
		int pageEnd = 100;
		int type = 0;
		String keywordStringOri = "玩母狗";
		int diskNum = 2;// 0 k盘 1m盘
		String customerQQ = "";

		String nickname = "辣姐";
		int uid = 0;
		String keywordString = null;

		try {
			keywordString = keywordStringOri.replace("==", "");
			// URLEncoder.encode(, "UTF-8");
			System.out.println(keywordString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type == 1) {
			args = new String[] { "" };
		}
		String houzuiString = "&uid=228316&systemModel=Redmi%20Note%207%20Pro&appType=1&appVer=3.9.1&phoneBrand=xiaomi&version=3.9.1&deviceId=d81949c4064b9fa4&systemVersion=10&versionCode=20250715";

		String url = "http://testlive.yueji.pro/live/live/video/list/mypage/20";// &sign=05285abd75763e1ad24c8974e625958b

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
				String sign = taoluSignUtil.md5(pU);
				pU = pU + sign + houzuiString;

				System.out.println(pU);
				// System.out.println(keywordString);

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				String docJson = getWanwuPageForApi3Journery3.postRes(pU, keywordString);
				// Thread.sleep(5000);
				System.out.println(docJson);
				// Jsoup.connect(pU).ignoreContentType(true).execute().body();
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				JSONArray links = object.getJSONObject("data").getJSONArray("records");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
				if (links.size() == 0)
					break;

				for (int j = 0; j < links.size(); j++) {

					cn.hutool.json.JSONObject link = links.getJSONObject(j);
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本
					if (nickname != null) {
						String nicknameDb = link.getStr("nickName");
						// 相等校验，防止重名。后边加上。
						if (nicknameDb.indexOf(nickname) == -1)
							continue;
					}
					if (uid != 0) {
						int uidDb = link.getInt("anchorUserId");
						if (uidDb != uid)
							continue;
					}

					if (keywordStringOri != null) {
						String titleDb = link.getStr("videoTitle");
//						System.out.println("titledb:" + titleDb);
//						// 相等校验，防止重名。后边加上。
//						System.out.println("keywordstringori:" + keywordStringOri);
						if (keywordStringOri.indexOf("==") != -1) {
							if (titleDb.indexOf(keywordStringOri.replace("==", "")) == -1
									|| !titleDb.contentEquals(keywordStringOri.replace("==", "")))
								continue;
						} else if (titleDb.indexOf(keywordStringOri) == -1)
							continue;

//						if (titleDb.indexOf("靴") == -1)
//							continue;
					}

					String articleUrlString = link.getStr("id");
					// attr("href").replace("/cn/movie/", "");
//					articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
//							+ articleUrlString;

//					Response articleRes = Jsoup.connect(articleUrlString)
//							.header("Content-Type", "application/json; charset=utf-8")
////							.header("Cookie",
////									"hls=s%3Aj9h_seF6e_ES1cK24p8h1WZUGx5Ny-Ba.Ds7igDStVbbGpFkVG0YYnMCJokUn6Lp3k49tqdjR9pE")
//							.header("User-Agent",
//									"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
//							.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
//							.execute();

//					String sourceString = articleRes.body();
//					JSONObject jsonObject = 
					// new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);

					// 提取URL
					String videoUrl = link.getStr("videoUrl");
//					JSONObject ui = jsonObject.getJSONObject("userinfo");
					String vid = "作品id--" + link.getStr("id") + "_用户id--" + link.getStr("anchorUserId") + "_昵称--"
							+ link.getStr("nickName") + "_拍摄地--" + "" + "_纬度--" + "" + "_经度--" + "" + "_常住省份--" + ""
							+ "_常住城市--" + "" + "_常住地址--" + "_" + customerQQ;

					String img = link.getStr("converImage");
//					if (link.getStr("originalname") != null && !link.getStr("originalname").equals("")) {
					if (urlSet.add(videoUrl)) {
						System.out.println(k + "-" + j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

						m3u8Result.append(link.getStr("videoTitle") + "_" + vid + "\t" + videoUrl + "\t"
								+ link.getStr("id") + "\t" + img + "\t" + "yl" + "\n");
					}
//					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				k--;
				continue;
				// TODO: handle exception
			}

			FileUtil.writeUtf8String(m3u8Result.toString(),
					"d:/m3u8ListTaoluzhuboapi/self_" + type + "_" + k + "_" + System.currentTimeMillis() + ".txt");

		}
		getWanwuM3u8ForApi.geneBat2(args, diskNum);

	}

}
