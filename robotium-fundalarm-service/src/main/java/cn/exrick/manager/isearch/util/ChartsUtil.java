package cn.exrick.manager.isearch.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;



//import cn.exrick.manager.isearch.entities.Subject;
import cn.exrick.manager.isearch.query.bean.CubeBean;
import cn.exrick.manager.isearch.query.bean.CubeBeans;

public class ChartsUtil {

//	public static JSONObject getTrend(Map<Subject, CubeBeans> beansMap) {
//		if (beansMap.size() == 0)
//			return null;
//		// X 轴坐标排重，且按从小到大排序
//		Set<String> set = new TreeSet<String>();
//		for (Map.Entry<Subject, CubeBeans> entrySet : beansMap.entrySet()) {
//			CubeBeans cBeans = entrySet.getValue();
//
//			for (int i = 0; i < cBeans.size(); i++) {
//				//System.out.println(cBeans.get(i).getKey()+"cbean(i).key:"+i+" "+cBeans.get(i).getNum());
//
//				set.add(cBeans.get(i).getKey());
//			}
//		}
//		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT);
//		Iterator<String> it = set.iterator();
//		// highcharts x轴坐标
//		JSONArray xAxis = new JSONArray();
//		while (it.hasNext()) {
//			xAxis.add(dateFormat.format(new Date(Long.parseLong(it.next()))));
//		}
//		// highcharts 值
//		JSONArray series = new JSONArray();
//		Set<Subject> subjects = beansMap.keySet();
//		String nowdate=dateFormat.format(new Date());
//		Calendar calendar = Calendar.getInstance();
//		int nowhour=calendar.get(11);
//
//		for (Subject subject : subjects) {
//			JSONObject item = new JSONObject();
//			int id = subject.getId();
//			item.put("id", id);
//			item.put("name", subject.getName());
//			CubeBeans beans = beansMap.get(subject);
//			// highcharts 数据
//			JSONArray data = new JSONArray();
//			for (int j = 0; j < xAxis.size(); j++) {
//				String x = xAxis.getString(j);
//				int num = 0;
//				for (CubeBean bean : beans.list()) {
//					String key = bean.getKey();
//					key = dateFormat.format(new Date(Long.parseLong(key)));
//					if (x.equals(key))
//					{
//						if(!key.equals(nowdate))
//						num = bean.getNum()/24;
//						else
//							num = bean.getNum()/nowhour;
//
//							
//					}
//				}
//				data.add(num);
//			}
//			item.put("data", data);
//			series.add(item);
//		}
//		JSONObject result = new JSONObject();
//		result.put("xAxis", xAxis);
//		result.put("series", series);
//		return result;
//	}

	/*public static Map<String, String> getTrend(Map<String, CubeBeans> beansMap) {
		if (beansMap.size() == 0)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		// key 排重且按从小到大排序
		Set<String> set = new TreeSet<String>();
		for (Map.Entry<String, CubeBeans> entrySet : beansMap.entrySet()) {
			CubeBeans cBeans = entrySet.getValue();
			for (int i = 0; i < cBeans.size(); i++) {
				set.add(cBeans.get(i).getKey());
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		Iterator<String> it = set.iterator();
		// highcharts x轴坐标
		String xAxis = "";
		while (it.hasNext()) {
			xAxis += "'" + dateFormat.format(new Date(Long.parseLong(it.next()))) + "',";
		}
		xAxis = (xAxis == "") ? "" : xAxis.substring(0, xAxis.lastIndexOf(","));
		map.put("xAxis", xAxis);
		// 数据集
		String[][] series = new String[beansMap.size()][set.size() + 1];
		int j = 0;
		for (Map.Entry<String, CubeBeans> entrySet : beansMap.entrySet()) {
			CubeBeans cBeans = entrySet.getValue();
			// name
			series[j][0] = entrySet.getKey();
			// data
			it = set.iterator();
			int z = 1;
			while (it.hasNext()) {
				String key = it.next();
				int num = 0;
				for (int i = 0; i < cBeans.size(); i++) {
					if (key.equals(cBeans.get(i).getKey()))
						num = cBeans.get(i).getNum();
				}
				series[j][z++] = String.valueOf(num);
			}
			j++;
		}
		// highcharts 数据集
		String seriesData = "";
		for (int m = 0; m < series.length; m++) {
			for (int n = 0; n < series[m].length; n++) {
				if (n == 0) {
					seriesData += "{name: '" + series[m][n] + "', data: [";
				} else if (n == series[m].length - 1) {
					if (m == series.length - 1)
						seriesData += series[m][n] + "]}";
					else
						seriesData += series[m][n] + "]},";
				} else {
					seriesData += series[m][n] + ",";
				}
			}
		}
		map.put("series", seriesData);
		return map;
	}*/

}
