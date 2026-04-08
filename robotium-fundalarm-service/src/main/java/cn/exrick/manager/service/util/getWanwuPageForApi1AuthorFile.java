package cn.exrick.manager.service.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;

public class getWanwuPageForApi1AuthorFile {

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
		Path filePath = Paths.get("d:/m3u8ListTaoluzhuboapi2/source.txt");
		try {
			lines = Files.readAllLines(filePath);
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
				String pU = url + lines.get(k).split("\t")[0];

//				String res = HttpUtil.get(pU);

				// 提取res中的链接
				String docJson = Jsoup.connect(pU).ignoreContentType(true).execute().body();
				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
				JSONArray links = object.getJSONObject("data").getJSONArray("info");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
				if (links.size() == 0) {
					System.out.println("用户无信息:" + lines.get(k).split("\t")[0]);
					continue;
				} else {

					String name = links.getJSONObject(0).getStr("user_nicename");
					if (name.equals("用户不存在")) {
						System.out.println("用户不存在:" + lines.get(k).split("\t")[0]);
						continue;
					}
				}

				for (int j = 0; j < links.size(); j++) {

					cn.hutool.json.JSONObject link = links.getJSONObject(j);
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

					String uid = link.getStr("id");
					String userNicename = link.getStr("user_nicename");
					String avatar = link.getStr("avatar");
					String sex = link.getStr("sex");
					String signature = link.getStr("signature");
					String balance = link.getStr("balance");
					String province = link.getStr("province");
					String city = link.getStr("city");
					String userStatus = link.getStr("user_status");
					String location = link.getStr("location");
					String isauth = link.getStr("isauth");
					String issuper = link.getStr("issuper");
					String privateMessagePrice = link.getStr("private_message_price");
					String fans = link.getStr("fans");
					String follows = link.getStr("follows");
					String lastActiveTime = link.getStr("last_active_time");
					String islive = link.getStr("islive");
					String videonums = link.getStr("videonums");
					String livenums = link.getStr("livenums");

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

				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("-----------------url下载异常：" + url);
				FileUtil.appendUtf8String(lines.get(k).split("\t")[0] + "\t" + e.getMessage() + "\r\n",
						"d:/m3u8ListTaoluzhuboapi2/error.txt");
				// TODO: handle exception
			}

			FileUtil.appendUtf8String(m3u8Result.toString(), "d:/m3u8ListTaoluzhuboapi2/userList.txt");

		}

		// getWanwuM3u8ForApi.geneBat(args);

	}

}
