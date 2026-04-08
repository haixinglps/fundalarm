package cn.exrick.manager.service.util;

import java.util.HashSet;
import java.util.Set;

import cn.exrick.manager.service.huifang.taoluSignUtil;
import cn.hutool.core.io.FileUtil;

public class getWanwuPageForApi1AuthorTaolu {

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();

		// String id = "106416-144209";395209 484222 518962
		int page = 518963;
		int pageEnd = 528963;
		// int type = 0;
		// 开始执行
		String url = "http://testlive.yueji.pro/user/userInfo/getUserInfo?id="
				+ "&sign=1750249447-9c3b4d63394145158a430ee470ff0b3b-0-174d16d182c27211b1c64b34e533b071&uid=228316&systemModel=Redmi%20Note%207%20Pro&appType=1&appVer=3.8.4&phoneBrand=xiaomi&version=3.8.4&deviceId=3251ab06-3320-4e70-b6a3-f796e4e7223a&systemVersion=10&versionCode=20250528"
				+ "";
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
				String sign = taoluSignUtil.md5("http://testlive.yueji.pro/user/userInfo/getUserInfo?id=" + k);

				String pU = "http://testlive.yueji.pro/user/userInfo/getUserInfo?id=" + k + sign.replace("?", "&")
						+ "&uid=228316&systemModel=Redmi%20Note%207%20Pro&appType=1&appVer=3.9.1&phoneBrand=xiaomi&version=3.9.1&deviceId=d81949c4064b9fa4&systemVersion=10&versionCode=20250715"
						+ "";
				System.out.println(pU);

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				String docJson = getWanwuPageForApi3Journery3.getRes(pU);
				System.out.println(docJson);
				// Jsoup.connect(pU).ignoreContentType(true).execute().body();
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				cn.hutool.json.JSONObject link = object.getJSONObject("data");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");

//					String name = links.getJSONObject(0).getStr("user_nicename");
				if (link.toString().equals("{}")) {
					System.out.println("用户不存在:" + k);
					continue;
				}
				// System.out.println(link.toString());

//				for (int j = 0; j < links.size(); j++) {

//					cn.hutool.json.JSONObject link = links.getJSONObject(j);
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

				String uid = link.getStr("userId");
				String userNicename = link.getStr("nickName").replace("\\", "");
				String avatar = link.getStr("userLogo");
				String sex = link.getStr("userGender");
				String signature = link.getStr("userSlogan").replace("\\", "");
				String balance = link.getStr("userCoin");
				String province = link.getStr("province");
				String city = link.getStr("city");
				String userStatus = link.getStr("userType");
				String location = link.getStr("address");
				String isauth = link.getStr("userPraises");
				String issuper = link.getStr("userIntegral");
				String privateMessagePrice = "0";// link.getStr("private_message_price");
				String fans = link.getStr("userFans");
				String follows = link.getStr("userFollows");
				String lastActiveTime = link.getStr("lat");
				;// link.getStr("last_active_time");
				String islive = link.getStr("lng");
				;// link.getStr("islive");
				String videonums = link.getStr("userVideos");
				String livenums = link.getStr("userLiveVideos");

				String item = uid + "\t" + userNicename + "\t" + avatar + "\t" + sex + "\t";
				item += signature + "\t";
				item += balance + "\t";
				item += province + "\t";
				item += city + "\t";
				item += userStatus + "\t";
				item += location + "\t";
				item += isauth + "\t";
				item += issuper + "\t";
				item += privateMessagePrice + "\t";
				item += fans + "\t";
				item += follows + "\t";
				item += lastActiveTime + "\t";
				item += islive + "\t";
				item += videonums + "\t";
				item += livenums + "\t";
				item += "\r\n";

				m3u8Result.append(item);

				System.out.println("-----" + k + "-------------");
//					}

//				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				FileUtil.appendUtf8String(k + "\t" + e.getMessage() + "\r\n", "d:/m3u8ListTaoluzhuboapi2/error.txt");
				// TODO: handle exception
			}
//			Thread.sleep(millis);

			FileUtil.appendUtf8String(m3u8Result.toString(), "d:/m3u8ListTaoluzhuboapi2/userList.txt");
//			break;
		}

		// getWanwuM3u8ForApi.geneBat(args);

	}

}
