package cn.exrick.manager.service.util;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;

public class getTaoluPageForZhubo {

	public static void main(String[] args) {
//		List<String> resultList = new ArrayList<String>();

		// 橙宝；若依；仙女殿下；小X大王；一诺大小姐；司宁青；暖狐；不知名；美丝大大；川野尤娜；万灵；万黛；汐月;楠熙;南星；木秋夜深了；欣然莫；
		// 可爱鬼；病娇；樱桃大大；成都叶子；栗子；梓晨；槿；芊兮殿下；司野；阿月；紫琳；路子
		// 爱可；麋鹿；银儿；狗哥；哈宝；弯弯小阿姨
		// 臀榨，牛仔裤，坐脸
//		String[] keywords = new String[] { "橙宝", "若依", "仙女殿下", "小X大王", "一诺大小姐", "司宁青", "暖狐", "不知名", "美丝大大", "川野尤娜",
//				"万灵", "万黛", "汐月", "楠熙", "南星", "木秋夜深了", "欣然莫", "可爱鬼", "病娇", "樱桃大大", "成都叶子", "栗子", "梓晨", "槿", "芊兮殿下",
//				"司野；阿月", "紫琳", "路子", "爱可",  "弯弯小阿姨", "臀榨", "牛仔裤", "坐脸" };
		// "脚深喉", "妆主"

		// "子衿", "南一", "初九", "洛芸", "扶桑", "玉娘", "希雨"

		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "7890");
		System.setProperty("https.proxyHost", "localhost");
		System.setProperty("https.proxyPort", "7890");
		// "简一", "甜依", "妆主"
		String[] keywords = new String[] { "路子" };
		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String keyword : keywords) {

			String url = "https://www.tlhf01.com/api/getcontents?size=48&q=" + keyword + "&type=movie,tv&page=";

			Set<String> urlSet = new HashSet<String>();
			for (int k = 1; k < 1000; k++) {// 1910
				StringBuffer m3u8Result = new StringBuffer();
				try {
					String pU = url + k;

//				String res = HttpUtil.get(pU);

					// 提取res中的链接
					String docJson = Jsoup.connect(pU).ignoreContentType(true).execute().body();
					JSONArray links = new JSONArray(docJson);
//				Elements links = doc.select("a[href^=\"/cn/movie\"]");
					if (links.size() == 0)
						break;

					for (int j = 0; j < links.size(); j++) {

						cn.hutool.json.JSONObject link = links.getJSONObject(j);
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

						String articleUrlString = link.getStr("_id");
						// attr("href").replace("/cn/movie/", "");
						articleUrlString = "https://www.tlhf01.com/api/getmovie?type=20000&id=" + articleUrlString;

						Response articleRes = Jsoup.connect(articleUrlString)
								.header("Content-Type", "application/json; charset=utf-8")
								.header("Cookie",
										"hls=s%3Aj9h_seF6e_ES1cK24p8h1WZUGx5Ny-Ba.Ds7igDStVbbGpFkVG0YYnMCJokUn6Lp3k49tqdjR9pE")
								.header("User-Agent",
										"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
								.header("Accept", "application/json, text/javascript, */*; q=0.01")
								.ignoreContentType(true).execute();

						String sourceString = articleRes.body();
						JSONObject jsonObject = new JSONObject(sourceString);
//					int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
//					int endIndex = jsonString.indexOf("'", startIndex);

						// 提取URL
						String videoUrl = "https://www.tlhf01.com" + jsonObject.getString("m3u8");
						String img = "https://www.tlhf01.com" + link.getStr("previewvideo");
						if (link.getStr("originalname") != null && !link.getStr("originalname").equals("")) {
							if (urlSet.add(videoUrl)) {
								System.out.println(k + "-" + j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

								m3u8Result.append(link.getStr("originalname") + "\t" + videoUrl + "\t" + img + "\n");
							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("-----------------url下载异常：" + url);
					// TODO: handle exception
				}
				FileUtil.writeUtf8String(m3u8Result.toString(),
						"d:/m3u8ListTaoluzhuboallkeyword" + "/" + keyword + "_" + k + ".txt");

			}

		}

	}

}
