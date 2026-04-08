package cn.exrick.manager.isearch.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



import cn.exrick.manager.isearch.query.Search;
import cn.exrick.manager.isearch.query.SearchFactory;
import cn.exrick.manager.isearch.query.bean.CubeBean;
import cn.exrick.manager.isearch.query.bean.CubeBeans;
import cn.exrick.manager.isearch.query.bean.GroupBean;
import cn.exrick.manager.isearch.query.bean.GroupBeans;
import cn.exrick.manager.isearch.query.bean.SearchBean;
import cn.exrick.manager.isearch.query.bean.SearchBeans;
import cn.exrick.manager.isearch.util.common.Profile;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

/**
 * 查询条件工具类
 * 
 * @author guild
 * 
 */
public class AnalysisUtil {
	
	public static void main(String[] args) throws Exception {
		SearchFactory.init();
		Search search = SearchFactory.getSearch();
		search.andText("TX", "北京");
		search.setMaxResults(10);
		search.greatThan("RQ", System.currentTimeMillis() - 24 * 3600 * 1000l);
		System.out.println(search.query());
	}

	/**
	 * 分组
	 */
	public static Map<String, String> forGroup(GroupBeans beans, String step) {
		Map<String, String> map = new HashMap<String, String>();
		String xAxis = "";
		String series = "{name: '舆情总量', data: [";
		SimpleDateFormat dateFormat = null;
		if ("hourly".equals(step))
			dateFormat = new SimpleDateFormat(DateUtil.MINUTE_FORMAT);
		else
			dateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (beans.size() == 1 && "weekly".equals(step))
			step = "daily";
		for (int i = beans.size() - 1; i >= 0; i--) {
			GroupBean bean = beans.get(i);
			String key = dateFormat.format(new Date(Long.parseLong(bean.getKey())));
			if ("weekly".equals(step))
				key += " ~ " + dateFormat.format(new Date(Long.parseLong(bean.getKey()) + 604800000L - 1000l));
			if (i == 0) {
				xAxis += "'" + key + "'";
				series += bean.getNum() + "]}";
			} else {
				xAxis += "'" + key + "',";
				series += bean.getNum() + ",";
			}
		}
		int xAxisStep = beans.size() / 4;
		xAxisStep = xAxisStep == 0 ? 1 : xAxisStep;
		xAxisStep = (xAxisStep == 1 && beans.size() > 4) ? 2 : xAxisStep;
		if ("weekly".equals(step))
			step = "daily";
		else if ("daily".equals(step))
			step = "hourly";
		else if ("hourly".equals(step))
			step = "end";
		map.put("xAxis", xAxis);
		map.put("series", series);
		map.put("step", step);
		// x 轴间隔
		map.put("xAxisStep", xAxisStep + "");
		return map;
	}

	/**
	 * 分组，二字段只有一个值
	 */
	public static Map<String, String> forCube(CubeBeans beans, String step) {
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat dateFormat = null;
		if ("hourly".equals(step))
			dateFormat = new SimpleDateFormat(DateUtil.MINUTE_FORMAT);
		else
			dateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		// 第二字段分组
		List<GroupBean> subBeans = beans.getSubBeans();
		// x 坐标
		String xAxis = "";
		// 数据集
		String[][] series = new String[subBeans.size()][beans.size() + 1];
		// 二字段只有一个值时
		if ("weekly".equals(step))
			step = "daily";
		series[0][0] = subBeans.get(0).getKey();
		int j = 0;
		for (int i = beans.size() - 1; i >= 0; i--) {
			CubeBean bean = beans.get(i);
			xAxis += "'" + dateFormat.format(new Date(Long.parseLong(bean.getKey()))) + "',";
			try {
				series[0][++j] = String.valueOf(bean.subList().get(0).getNum());
			} catch (Exception e) {
				series[0][++j] = String.valueOf(0);
			}
		}
		xAxis = (xAxis == "") ? "" : xAxis.substring(0, xAxis.lastIndexOf(","));
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
		int xAxisStep = beans.size() / 4;
		if ("weekly".equals(step)) {
			step = "daily";
			xAxisStep = beans.size() / 2;
		} else if ("daily".equals(step))
			step = "hourly";
		else if ("hourly".equals(step))
			step = "end";
		xAxisStep = xAxisStep == 0 ? 1 : xAxisStep;
		map.put("xAxis", xAxis);
		map.put("series", seriesData);
		map.put("step", step);
		// x 轴间隔
		map.put("xAxisStep", xAxisStep + "");
		return map;
	}

	/**
	 * 分组，二字段多个值
	 */
	public static Map<String, String> forCube(CubeBeans cBeans, GroupBeans gBeans, String step) {
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat dateFormat = null;
		if ("hourly".equals(step))
			dateFormat = new SimpleDateFormat(DateUtil.MINUTE_FORMAT);
		else
			dateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		// 第二字段分组
		List<GroupBean> subBeans = cBeans.getSubBeans();
		// x 坐标
		String xAxis = "";
		// 数据集
		String[][] series = new String[subBeans.size()][cBeans.size() + 1];
		for (int i = 0; i < subBeans.size(); i++) {
			String subField = subBeans.get(i).getKey();
			series[i][0] = subField;
			int k = 0;
			for (int j = gBeans.size() - 1; j >= 0; j--) {
				GroupBean gBean = gBeans.get(j);
				// 获取 x 轴坐标
				if (i == 0) {
					String key = dateFormat.format(new Date(Long.parseLong(gBean.getKey())));
					if ("weekly".equals(step))
						key += " ~ " + dateFormat.format(new Date(Long.parseLong(gBean.getKey()) + 604800000L - 1000l));
					xAxis += "'" + key + "',";
				}
				int num = 0;
				CubeBean cBean = cBeans.get(j);
				for (GroupBean gSubBean : cBean.subList()) {
					if (subField.equals(gSubBean.getKey())) {
						num = gSubBean.getNum();
					}
				}
				series[i][++k] = String.valueOf(num);
			}
		}
		xAxis = (xAxis == "") ? "" : xAxis.substring(0, xAxis.lastIndexOf(","));
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
		int xAxisStep = cBeans.size() / 4;
		if ("weekly".equals(step)) {
			step = "daily";
			xAxisStep = cBeans.size() / 2;
		} else if ("daily".equals(step))
			step = "hourly";
		else if ("hourly".equals(step))
			step = "end";
		xAxisStep = xAxisStep == 0 ? 1 : xAxisStep;
		map.put("xAxis", xAxis);
		map.put("series", seriesData);
		map.put("step", step);
		// x 轴间隔
		map.put("xAxisStep", xAxisStep + "");
		return map;
	}

	/**
	 * 数据分组，数据来源数据库
	 */
	public static JSONArray forCombo(CubeBeans beans) {
		// combo 图形结果集
		JSONArray series = new JSONArray();
		// pie 饼图数据
		JSONArray pie = new JSONArray();
		// column 柱图数据
		JSONObject column = new JSONObject(), xi = new JSONObject();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 15);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		for (int i = 0; i < 15; i++) {
			c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
			xi.put(dateFormat.format(new Date(c.getTimeInMillis())), i);
		}
		// 二字段分组，饼图数据，柱图数据
		for (GroupBean bean : beans.getSubBeans()) {
			JSONObject item = new JSONObject();
			item.put("name", bean.getKey());
			item.put("y", bean.getNum());
			pie.add(item);
			column.put(bean.getKey(), new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
		}
		// 解析结果
		for (GroupBean gBean : beans.getSubBeans()) {
			String key = gBean.getKey();
			JSONArray item = column.getJSONArray(key);
			for (CubeBean bean : beans.list()) {
				if (!xi.containsKey(bean.getKey()))
					continue;
				for (GroupBean subBean : bean.subList()) {
					if (key.equals(subBean.getKey()))
						item.set(xi.getInt(bean.getKey()), subBean.getNum());
				}
			}
		}
		// 柱图数据
		for (Object key : column.keySet()) {
			JSONObject result = new JSONObject();
			result.put("name", (String) key);
			result.put("type", "column");
			result.put("data", column.getJSONArray((String) key));
			series.add(result);
		}
		// 饼图数据
		JSONObject result = new JSONObject();
		result.put("name", "舆情预警正负面分布");
		result.put("type", "pie");
		result.put("data", pie);
		result.put("center", new int[] { 120, 0 });
		result.put("size", 100);
		result.put("showInLegend", false);
		JSONObject obj = new JSONObject();
		obj.put("enabled", false);
		result.put("dataLabels", obj);
		series.add(result);
		return series;
	}

	/**
	 * 数据分组，数据来源数据库
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray combo(CubeBeans beans) {
		// series combo 图形结果集，pieData 饼图数据
		JSONArray series = new JSONArray(), pieData = new JSONArray();
		// 对应二字段柱图数据
		JSONObject column = new JSONObject();
		// 二字段分组，饼图数据
		for (GroupBean bean : beans.getSubBeans()) {
			JSONObject pieResult = new JSONObject();
			pieResult.put("name", bean.getKey());
			pieResult.put("y", bean.getNum());
			pieData.add(pieResult);
			JSONArray item = new JSONArray();
			column.put(bean.getKey(), item);
		}
		Set<String> set = column.keySet();
		for (CubeBean bean : beans.list()) {
			for (String key : set) {
				int num = 0;
				for (GroupBean gBean : bean.subList()) {
					if (key.equals(gBean.getKey()))
						num = gBean.getNum();
				}
				column.getJSONArray(key).add(num);
			}
		}
		// 柱图数据
		for (String key : set) {
			JSONArray item = column.getJSONArray(key);
			JSONObject result = new JSONObject();
			result.put("name", key);
			result.put("type", "column");
			result.put("data", item);
			series.add(result);
		}
		// 饼图数据
		JSONObject result = new JSONObject();
		result.put("name", "舆情预警正负面分布");
		result.put("type", "pie");
		result.put("data", pieData);
		result.put("center", new int[] { 120, 0 });
		result.put("size", 100);
		result.put("showInLegend", false);
		JSONObject obj = new JSONObject();
		obj.put("enabled", false);
		result.put("dataLabels", obj);
		series.add(result);
		return series;
	}

	/**
	 * 数据分组，数据来源数据库
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject combo(CubeBeans beans, long[] time) {
		// xAxis x轴，series 数据
		JSONArray xAxis = new JSONArray(), series = new JSONArray(), pie = new JSONArray();
		// 对应二字段数据
		JSONObject column = new JSONObject();
		// 二字段分组，饼图数据
		for (GroupBean bean : beans.getSubBeans()) {
			JSONObject pieResult = new JSONObject();
			pieResult.put("name", bean.getKey());
			pieResult.put("y", bean.getNum());
			pie.add(pieResult);
			JSONArray item = new JSONArray();
			column.put(bean.getKey(), item);
		}
		Set<String> set = column.keySet();
		for (CubeBean bean : beans.list()) {
			// x 轴数据
			xAxis.add(bean.getKey());
			for (String key : set) {
				for (GroupBean gBean : bean.subList()) {
					if (key.equals(gBean.getKey())) {
						JSONArray item = column.getJSONArray(key);
						item.add(gBean.getNum());
					}
				}
			}
		}
		// 柱图数据
		for (String key : set) {
			JSONArray item = column.getJSONArray(key);
			JSONObject result = new JSONObject();
			result.put("name", key);
			result.put("type", "column");
			result.put("data", item);
			series.add(result);
		}
		// 饼图数据
		JSONObject result = new JSONObject();
		result.put("name", "舆情预警正负面分布");
		result.put("type", "pie");
		result.put("data", pie);
		series.add(result);
		result.clear();
		result.put("xAxis", xAxis);
		result.put("series", series);
		return result;
	}

	/**
	 * 分组，数据来源于数据库
	 */
	public static Map<String, String> forCube(CubeBeans beans, long[] time) {
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		long day = (time[1] - time[0]) / (3600 * 24 * 1000l);
		String[] categories = new String[Integer.parseInt(day + "")];
		// x 轴数组
		for (int i = 0; i < categories.length; i++) {
			categories[i] = dateFormat.format(new Date(time[0] + 3600 * 24 * 1000l * i));
		}
		String[] types = new String[beans.getSubBeans().size()];
		// 二字段分组
		int m = 0;
		for (GroupBean bean : beans.getSubBeans()) {
			types[m++] = bean.getKey();
		}
		String xAxis = "";
		String[] series = new String[types.length];
		for (int i = 0; i < categories.length; i++) {
			String key = categories[i];
			List<GroupBean> subBeans = new ArrayList<GroupBean>();
			for (CubeBean bean : beans.list()) {
				if (key.equals(bean.getKey())) {
					subBeans = bean.subList();
				}
			}
			for (int j = 0; j < series.length; j++) {
				int num = 0;
				for (GroupBean subBean : subBeans) {
					if (subBean.getKey().equals(types[j]))
						num = subBean.getNum();
				}
				if (i == 0)
					series[j] = "{name: '" + types[j] + "', data: [" + num + ",";
				else if (i == categories.length - 1)
					series[j] += num + "]}";
				else
					series[j] += num + ",";
			}
		}
		for (String category : categories) {
			xAxis += "'" + category + "',";
		}
		xAxis = xAxis.indexOf(",") == -1 ? xAxis : xAxis.substring(0, xAxis.lastIndexOf(","));
		String seriesData = "";
		for (int i = 0; i < series.length; i++) {
			seriesData += series[i];
			if (i != series.length - 1)
				seriesData += ",";
		}
		map.put("xAxis", xAxis);
		map.put("series", seriesData);
		return map;
	}

	/**
	 * 列表
	 * @throws Exception 
	 */
	public static Map<String, String> forList(GroupBeans beans) throws Exception {
		Profile profile = new Profile(Profile.SOURCE);
 
		Map<String, String> map = new HashMap<String, String>();
		String xAxis = "";
		String series = "{name: '文章数', data: [";
		int size = beans.size();
		for (int i = 0; i < size; i++) {
			GroupBean bean = beans.get(i);
			String key = bean.getKey();
			// key = profile.getEncodeValue(key);

			if (key.length() == 0)
				continue;
			xAxis += "'" + key + "',";
			series += bean.getNum() + ",";
		}
		xAxis = xAxis.length() == 0 ? xAxis : xAxis.substring(0, xAxis.lastIndexOf(","));
		series = (series.lastIndexOf(",") != (series.length() - 1)) ? (series + "]}") : (series.substring(0, series
				.lastIndexOf(",")) + "]}");
		map.put("xAxis", xAxis);
		map.put("series", series);
		return map;
	}
	public static Map<String, String> forListweb(GroupBeans beans) throws Exception {
		Profile profile = new Profile(Profile.SOURCE);
 
		Map<String, String> map = new HashMap<String, String>();
		String xAxis = "";
		String series = "{name: '文章数', data: [";
		int size = beans.size();
		for (int i = 0; i < size; i++) {
			GroupBean bean = beans.get(i);
			String key = bean.getKey();
			 key = profile.getEncodeValue(key);

			if (key.length() == 0)
				continue;
			
			xAxis += "'" + key + "',";
			series += bean.getNum() + ",";
		}
		xAxis = xAxis.length() == 0 ? xAxis : xAxis.substring(0, xAxis.lastIndexOf(","));
		series = (series.lastIndexOf(",") != (series.length() - 1)) ? (series + "]}") : (series.substring(0, series
				.lastIndexOf(",")) + "]}");
		map.put("xAxis", xAxis);
		map.put("series", series);
		return map;
	}
	
	
	
	public static Map<String, String> forListLC(GroupBeans beans,String region) throws Exception {
	//	Profile profile = new Profile(Profile.SOURCE);
 
		Map<String, String> map = new HashMap<String, String>();
		String xAxis = "";
		String series = "{name: '文章数', data: [";
		int size = beans.size();
		int arrLen;
		if ("prov".equals(region))
			arrLen = 1;
		else
			arrLen = 2;
		for (int i = 0,j=0; i < size; i++) {
			
			GroupBean bean = beans.get(i);
			String key = bean.getKey();
			// key = profile.getEncodeValue(key);
 //System.out.println("bean key3 ："+bean.getKey());
 //System.out.println("bean num3 ："+bean.getNum());
//Thread.sleep(3600000);
			if (key.length() == 0)
				continue;
			String[] arr = key.split(":");
			if (arr.length == arrLen) {
				
				//bean.setNum(gHit.getNum());
				//String pre = String.valueOf((gHit.getNum() / 30.)
					//	/ (monthHits.get(i).getNum() / 60.));
				//if ((pre.length() - pre.lastIndexOf(".")) > 3) {
				//	pre = pre.substring(0, pre.lastIndexOf(".") + 3);
				//}
				//bean.setPre("1.00");
				//beanList.add(bean);
				xAxis += "'" + key + "',";
				series += bean.getNum() + ",";
				j++;
				 System.out.println("itme j ："+j);

				if(j>15)
					break;
			}
			
		}
		xAxis = xAxis.length() == 0 ? xAxis : xAxis.substring(0, xAxis.lastIndexOf(","));
		series = (series.lastIndexOf(",") != (series.length() - 1)) ? (series + "]}") : (series.substring(0, series
				.lastIndexOf(",")) + "]}");
		map.put("xAxis", xAxis);
		map.put("series", series);
		return map;
	}
	
	
	
	
	
	/**
	 * 结果集处理
	 */
	public static String forSeries(String field, String series) {
		if (series.contains("SNS"))
			series = series.replaceAll("SNS", "微博");
		else if (series.contains("新闻") && field.equals(Search.AUTHOR_TYPE))
			series = series.replaceAll("新闻", "新闻记者");
		return series;
	}

	/**
	 * 以相关度排序查询得到重复数最多的文章
	 */
	public static String forRepeat(SearchBeans beans) {
		int repeatNum = 0;
		// 重复数最多的文章
		SearchBean repeatBean = new SearchBean();
		int t=0;
		for (int i = 0; i < beans.size(); i++) {
			SearchBean bean = beans.get(i);
			System.out.println(i+":"+bean.getSubBeans().size());
			int subSize = bean.getSubBeans().size();
			if (i == 0) {
				repeatNum = subSize + 1;
				repeatBean = bean;
			} else {
				if (repeatNum < (subSize + 1)) {
					repeatNum = subSize + 1;
					repeatBean = bean;
					t=i;
				}
			}
		}
		String title = repeatBean.getTi();
		if (title != null)
			title = title.replaceAll("<em>", "").replaceAll("</em>", "");
		System.out.println(t+":"+title);

		return title;
	}

}