
package cn.exrick.manager.service.util;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.hutool.core.io.FileUtil;

public class getWanwuPageForApi3vid {

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();

		int type = 3;
		int diskNum = 2;// 0 k盘 1m盘 2i盘
		String customerQQ = "";
		// 178600,188052,188170,198723,198716,204483,156539,57616,171197,206385
		String vids = "51991,109090,16726,190409,118954,79663,111042,96100,128422,75656,100052,2513,144496,1001,8036,94604,118770,144587,169563,93681,201756,103176,95044,74333,217825,205933,118160,168701,103832,171902,192025,218547,141213,104937,140827,213924,141215,155870,171527,187144,151049,151056,187163,178472,187179,158260,172105,155211,154958,182863,193362,140886,193370,173917,186220,75129,143225,180602,155018,92558,180626,140954,215197,92578,186286,173488,142513,186304,130257,180440,159195,132830,173538,180450,218597,84460,180464,180469";//
		// String vids =
		// "170243,170475,174233,174338,174396,174454,175620,175640,175770,176448,177120,179341,180006,180064,181292,181450,181981,183345,183826,183962,185257,185305,185404,185419,186072,186081,186624,186731,187447,188002,188112,188166,189513,190256,190977,191055,192369,192433,192508,192587,193866,194016,194292,194463,194542,194597,196073,196683,196733,196752,196815,198089,198138,198288,198913,200162,200212,200927,201493,202191,203571,204093,204181,204281,205481,205609,207620,208311,209590,209648,209745,210265,210418,211850,212467,213684,213748,214298,214405,215646,218707,218754,218958,219008,219061";

		int write = 1;
		// 142881,142450,142150,141429,128311,127576,64514
		// 143266,139099,136129,13061,29768,29696,28890,46491,46488,59391,131071,130245,143209,143174,145457,145390,144716
		// 146240,133640,68486,145233,145231,145229,144544

		if (type == 1) {
			args = new String[] { "" };
		}
		String url = "http://127.0.0.1:54545/appapi/?service=Collect.list&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&type="
				+ type + "&p=mypage&sign=05285abd75763e1ad24c8974e625958b";
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> urlSet = new HashSet<String>();
		// for (int k = page; k <= page; k++) {// 1910
		StringBuffer m3u8Result = new StringBuffer();
		try {
			// String pU = url.replace("mypage", k + "");
			// System.out.println(pU);

//				String res = HttpUtil.get(pU);

			// 提取res中的链接
//				String docJson = Jsoup.connect(pU).ignoreContentType(true).execute().body();
//				cn.hutool.json.JSONObject object = new cn.hutool.json.JSONObject(docJson);
//				JSONArray links = object.getJSONObject("data").getJSONArray("info");
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
			String[] links = vids.split(",");
			if (links.length == 0)
				return;

			for (int j = 0; j < links.length; j++) {

//					cn.hutool.json.JSONObject link = links[j];
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

				String articleUrlString = links[j];
				// attr("href").replace("/cn/movie/", "");
				articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
						+ articleUrlString;
				url = articleUrlString;
				Response articleRes = Jsoup.connect(articleUrlString)
						.header("Content-Type", "application/json; charset=utf-8")
//							.header("Cookie",
//									"hls=s%3Aj9h_seF6e_ES1cK24p8h1WZUGx5Ny-Ba.Ds7igDStVbbGpFkVG0YYnMCJokUn6Lp3k49tqdjR9pE")
						.header("User-Agent",
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
						.header("Accept", "application/json, text/javascript, */*; q=0.01").ignoreContentType(true)
						.execute();

				String sourceString = articleRes.body();
				System.out.println(sourceString);
				JSONObject jsonObject = new JSONObject(sourceString).getJSONObject("data").getJSONObject("info");
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);

				// 提取URL
				String videoUrl = jsonObject.getString("url");
				String sk = jsonObject.getString("trial_url");

				JSONObject ui = jsonObject.getJSONObject("userinfo");
				String vid = "作品id--" + jsonObject.getString("id") + "_用户id--" + jsonObject.getString("uid") + "_昵称--"
						+ jsonObject.getString("nickname") + "_拍摄地--" + jsonObject.getString("city") + "_纬度--"
						+ jsonObject.getString("lat") + "_经度--" + jsonObject.getString("lng") + "_常住省份--"
						+ ui.getString("province") + "_常住城市--" + ui.getString("city") + "_常住地址--"
						+ ui.getString("location") + "_余额--" + (ui.has("balance") ? ui.getString("balance") : "")
						+ "_粉丝数--" + ui.getString("fans") + "_" + customerQQ;

				String img = jsonObject.getString("cover_pic");
//					if (link.getStr("originalname") != null && !link.getStr("originalname").equals("")) {
				if (urlSet.add(videoUrl)) {
					System.out.println(j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

					m3u8Result.append(jsonObject.getString("title") + "_" + vid + "\t" + videoUrl + "\t"
							+ jsonObject.getString("id") + "\t" + img + "\t" + sk + "\n");
				}
//					}

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("-----------------url下载异常：" + url);
			// TODO: handle exception
		}

		FileUtil.writeUtf8String(m3u8Result.toString(),
				"d:/m3u8ListTaoluzhuboapi/self_" + type + "_" + 0 + "_" + System.currentTimeMillis() + ".txt");

		getWanwuM3u8ForApi.geneBat(args, diskNum, write);

	}

}
