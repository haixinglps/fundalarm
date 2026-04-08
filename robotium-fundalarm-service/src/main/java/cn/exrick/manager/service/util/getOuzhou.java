package cn.exrick.manager.service.util;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.hutool.core.io.FileUtil;

public class getOuzhou {

	public static void main(String[] args) throws Exception {
//		List<String> resultList = new ArrayList<String>();
		String[] keywords = new String[] { "子衿", "南一", "初九", "洛芸", "扶桑", "玉娘", "希雨" };
//		int page = 0;
		for (String key : keywords) {

//			page += 1;

//		String url = "https://www.92mj4.com/list-9-";
			String url = "https://www.92mj4.com/index.php?m=search&c=index&a=init&typeid=53&siteid=1&q=" + key
					+ "&searchsubmit=true&page=";
			HttpsUrlValidator.trustAllHttpsCertificates();
			Set<String> urlSet = new HashSet<String>();
			for (int k = 1; k < 1086; k++) {
				StringBuffer m3u8Result = new StringBuffer();
				try {
					String pU = url + k;// + ".html";
//				String res = HttpUtil.get(pU);

					// 提取res中的链接
					Document doc = Jsoup.connect(pU).get();

					Elements links = doc.select("a[href^=\"/show-\"]");
					if (links.size() == 0)
						break;
					int j = 0;
					for (Element link : links) {
						j++;
//					System.out.println("\nlink: " + link.attr("abs:href")); // 输出绝对URL
//					System.out.println("text: " + link.text()); // 输出链接文本

						String articleUrlString = link.attr("abs:href").replace("show", "points");

						Document article = Jsoup.connect(articleUrlString).get();

						String sourceString = article.outerHtml();
						String jsonString = sourceString.substring(sourceString.indexOf("video:'"));
						int startIndex = jsonString.lastIndexOf("video:'") + "video:'".length();
						int endIndex = jsonString.indexOf("'", startIndex);

						// 提取URL
						String videoUrl = jsonString.substring(startIndex, endIndex);
						if (link.text() != null && !link.text().equals("")) {
							if (urlSet.add(videoUrl)) {
								System.out.println(
										key + "-" + k + "-" + j + "-m3u8:\t" + videoUrl + "\t" + articleUrlString);

								m3u8Result.append(link.text() + "\t" + videoUrl + "\n");
							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("-----------------url下载异常：" + url);
					// TODO: handle exception
				}
				FileUtil.writeUtf8String(m3u8Result.toString(), "d:/m3u8ListOuZhou/page_" + key + "_" + k + ".txt");

			}

		}

	}

}
