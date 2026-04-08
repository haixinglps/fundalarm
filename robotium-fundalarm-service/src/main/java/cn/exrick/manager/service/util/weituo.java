package cn.exrick.manager.service.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONObject;

public class weituo {

	public static void main(String[] args) {

		File file = new File("d:\\data.txt");
		List<String> lines = FileUtil.readLines(file, CharsetUtil.CHARSET_UTF_8);
		String dtString = lines.get(0);
		JSONObject jObject = new JSONObject(dtString);

		cn.hutool.json.JSONArray liString = jObject.getJSONArray("data");

		int cc = 0;
		List<BigDecimal> sellList = new ArrayList<BigDecimal>();
		List<BigDecimal> buyList = new ArrayList<BigDecimal>();
		for (int i = 0; i < liString.size(); i++) {
			JSONObject item = liString.getJSONObject(i);

			BigDecimal num = item.getBigDecimal("fillSz");
			Long tm = item.getLong("fillTime");
			Date dt = new Date(tm);
			String side = item.getStr("side");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BigDecimal price = item.getBigDecimal("fillPx");
			BigDecimal chaju = new BigDecimal("0.0001"); // 设定一个阈值，比如10的-6次方

			if (tm > 1711671410000l) {
				cc = cc + 1;
				if (side.equals("buy")) {
					num = num.multiply(new BigDecimal("0.999")).setScale(6, BigDecimal.ROUND_DOWN);
				}
				String dtString2 = sdf.format(dt);
//				System.out.println(side + "\t" + num + "\t" + price + "\t" + dtString2);

				if (side.equals("buy")) {
					// 删除卖方集合中匹配的元素，进行删除，删除一个即可。如果没有找到，加入buy集合；
					int findtag = 0;

					for (int j = 0; j < sellList.size(); j++) {
						BigDecimal wts = sellList.get(j);
						BigDecimal realChaju = wts.subtract(num);
//						System.out.println("差距：" + realChaju.abs().toString());
						if (wts.compareTo(num) == 0 || realChaju.abs().compareTo(chaju) < 0) {
//							System.out.println("发现一个匹配数据量-卖方：" + num);
							findtag = 1;
							sellList.remove(j);
							break;
						}

					}
					if (findtag == 0) {
						buyList.add(num);
					}

				} else {
					// 删除买方集合中匹配的元素，进行删除，删除一个即可。如果没有找到，加入sell集合。
					int findtag = 0;
					for (int j = 0; j < buyList.size(); j++) {
						BigDecimal wts = buyList.get(j);
						BigDecimal realChaju = wts.subtract(num);
//						System.out.println("差距：" + realChaju.abs().toString());
						if (wts.compareTo(num) == 0 || realChaju.abs().compareTo(chaju) < 0) {
//							System.out.println("发现一个匹配数据量-买方：" + num);
							findtag = 1;
							buyList.remove(j);
							break;
						}

					}
					if (findtag == 0) {
						sellList.add(num);
					}
				}

			} else {
//				System.out.println("异常数据：");
			}

		}
		// 打印买方，卖方

		for (int j = 0; j < sellList.size(); j++) {
			System.out.println("sell\t" + sellList.get(j));
		}
		for (int k = 0; k < buyList.size(); k++) {
			System.out.println("buy\t" + buyList.get(k));
		}

//		System.out.println("条数：" + cc);

	}

}
