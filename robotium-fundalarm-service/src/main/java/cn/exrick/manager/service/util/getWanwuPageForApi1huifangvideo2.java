package cn.exrick.manager.service.util;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import cn.exrick.manager.service.huifang.taoluSignUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;

public class getWanwuPageForApi1huifangvideo2 {

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();
		// 435506,435632,435566,435442,435402 这几个人的27部。
		String id = "447648";
		int page = 1;
		int pageEnd = 100;
		int type = 0;
		String keywordString = null;// 酒店调教服务生 鞋交手足榨
		int diskNum = 2;// 0 k盘 1m盘
		String customerQQ = "";

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

		String userInfoRes = "{}";
		String houzuiString = "&uid=228316&systemModel=Redmi%205%20Plus&appType=1&appVer=3.9.1&phoneBrand=xiaomi&version=3.9.1&deviceId=186d155c-1555-4559-86de-f8480f9081f7&systemVersion=8.1.0&versionCode=20250715";
		try {
			// live.taolu.black
			String userUrlString = "http://testlive.yueji.pro/user/userInfo/getUserInfo";
			String sign = taoluSignUtil.md5(userUrlString);
			userInfoRes = getWanwuPageForApi3Journery3.getRes("http://testlive.yueji.pro/user/userInfo/getUserInfo?id="
					+ id + "&" + sign.replace("?", "") + houzuiString);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println(userInfoRes);

		for (int k = page; k <= pageEnd; k++) {// 1910
			StringBuffer m3u8Result = new StringBuffer();
			try {
				String pU = "http://testlive.yueji.pro/live/live/video/anchor/" + k + "/10";

				pU += "?anchorUserId=" + id;
				String sign = taoluSignUtil.md5(pU);
				pU += "&" + sign.replace("?", "");

				// +
				// "&sign=1750861480-2bd96cf95f374fd3bfab9a67654db497-0-f335c43428b060927c17dfbf979c6de0&uid=228316&systemModel=Redmi%20Note%207%20Pro&appType=1&appVer=3.8.4&phoneBrand=xiaomi&version=3.8.4&deviceId=3251ab06-3320-4e70-b6a3-f796e4e7223a&systemVersion=10&versionCode=20250528";

//				String res = HttpUtil.get(pU);
				System.out.println(pU);

				// 提取res中的链接

				String docJson = getWanwuPageForApi3Journery3.getRes(pU);
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

					if (keywordString != null) {
						String titleDb = link.getStr("videoTitle");
						if (titleDb.indexOf(keywordString) == -1)
							continue;
					}

					String articleUrlString = link.getStr("id");
					// attr("href").replace("/cn/movie/", "");
//					articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
//							+ articleUrlString;
//					// http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id=47617
//
//					Response articleRes = Jsoup.connect(articleUrlString)
//							.header("Content-Type", "application/json; charset=utf-8")
//							.header("Cookie", "PHPSESSID=s8ui3qddpcbmbeotptj2alfcnd").header("Host", "127.0.0.1:54545")
//							.header("User-Agent",
//									"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
//							.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
//							.execute();

//					String sourceString = articleRes.body();
					System.out.println("------------------");
//					System.out.println(sourceString);
//					JSONObject jsonObject = link;
					// new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);

					// 提取URL
					String videoUrl = link.getStr("videoUrl");
					JSONObject ui = new JSONObject(userInfoRes).getJSONObject("data");
					String vid = "作品id--" + link.getStr("id") + "_用户id--" + id + "_昵称--" + link.getStr("nickName")
							+ "_拍摄地--" + "" + "_纬度--" + ui.getString("lat") + "_经度--" + ui.getString("lng") + "_常住省份--"
							+ ui.getString("province") + "_常住城市--" + ui.getString("city") + "_常住地址--"
							+ ui.getString("address") + "_余额--" + (ui.has("userCoin") ? ui.getInt("userCoin") : "")
							+ "_粉丝数--" + ui.getInt("userFans") + "_回放数--" + ui.getInt("userLiveVideos") + "_"
							+ customerQQ;
					String img = link.getStr("converImage");
//					if (link.getStr("originalname") != null && !link.getStr("originalname").equals("")) {
					if (urlSet.add(videoUrl)) {
						System.out.println(k + "-" + j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

						m3u8Result.append(link.getStr("videoTitle") + "_" + vid + "\t" + videoUrl + "\t"
								+ link.getStr("id") + "\t" + img + "\t" + "sk" + "\n");
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
		getWanwuM3u8ForApi.geneBat2(args, diskNum);

	}

}
