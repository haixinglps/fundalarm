package cn.exrick.manager.service.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.exrick.manager.service.huifang.taoluSignUtil;
import cn.hutool.core.io.FileUtil;

public class getWanwuPageForApi1AuthorFile2Taolu {

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();

		// String id = "106416-144209";
		int page = 170068;
		int pageEnd = 395209;
		// int type = 0;
		// 开始执行
		String url = "http://127.0.0.1:54545/appapi/?service=User.getUserHome2&uid=267412&token=abf8552d9c40faefd94e054a4c7a9c99&version=137&platform=2&touid=";

		// File file = new File("");
		List<String> lines = new ArrayList<String>();
		List<String> linesHistory = new ArrayList<String>();
		HashSet<String> hashSet = new HashSet<String>();
		Path filePath = Paths.get("d:/m3u8ListTaoluzhuboapi2/source.txt");
		Path history = Paths.get("d:/m3u8ListTaoluzhuboapi2/history.txt");

		try {
			lines = Files.readAllLines(filePath);
			linesHistory = Files.readAllLines(history);
			for (String his : linesHistory) {
				hashSet.add(his);
			}

//            for (String line : lines) {
//                System.out.println(line);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();

		for (int k = 0; k < lines.size(); k++) {// 1910
			StringBuffer m3u8Result = new StringBuffer();
			try {
				if (hashSet.contains(lines.get(k).split("\t")[0])) {
					continue;
				}
				String pU = url = "http://testlive.yueji.pro/user/userInfo/getUserInfo?id="
						+ lines.get(k).split("\t")[0] + "&"
						+ (taoluSignUtil.md5("http://testlive.yueji.pro/user/userInfo/getUserInfo").replace("?", ""))
						+ "&uid=228316&systemModel=Redmi%20Note%207%20Pro&appType=1&appVer=3.8.4&phoneBrand=xiaomi&version=3.8.4&deviceId=3251ab06-3320-4e70-b6a3-f796e4e7223a&systemVersion=10&versionCode=20250528"
						+ "";

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				String docJson = getWanwuPageForApi3Journery3.getRes(pU);
				// System.out.println(docJson);
				// Jsoup.connect(pU).ignoreContentType(true).execute().body();
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				cn.hutool.json.JSONObject link = object.getJSONObject("data");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");

//					String name = links.getJSONObject(0).getStr("user_nicename");
				if (link.toString().equals("{}")) {
					System.out.println("用户不存在:" + k);
					FileUtil.appendUtf8String(lines.get(k).split("\t")[0] + "\r\n",
							"d:/m3u8ListTaoluzhuboapi2/history.txt");

					continue;
				}

//				cn.hutool.json.JSONObject link = links.getJSONObject(j);
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

				String uid = link.getStr("userId");
				String userNicename = link.getStr("nickName");
				String avatar = link.getStr("userLogo");
				String sex = link.getStr("userGender");
				String signature = link.getStr("userSlogan");
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
				String livenums = link.getStr("userLives");

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

				System.out.println("-----" + lines.get(k).split("\t")[0] + "-------------");
//					}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
//				FileUtil.appendUtf8String(lines.get(k).split("\t")[0] + "\t" + e.getMessage() + "\r\n",
//						"d:/m3u8ListTaoluzhuboapi2/error.txt");
				k--;
				continue;
				// TODO: handle exception
			}

			FileUtil.appendUtf8String(m3u8Result.toString(), "d:/m3u8ListTaoluzhuboapi2/userList.txt");

		}

		// getWanwuM3u8ForApi.geneBat(args);

	}

}
