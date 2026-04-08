package cn.exrick.manager.isearch.query;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class SearchUtil {

	public static final String BASE_QUERY = "{\"" + Search.QUERY_TEXT + "\":{\"" + Search.TITLE + "\":\"\",\""
			+ Search.DOMAIN + "\":\"\",\"" + Search.AUTHOR + "\":\"\"},\"" + Search.QUERY_CATEGORY + "\":{\""
			+ Search.EMOTION + "\":[],\"" + Search.MEDIA_TYPE + "\":[],\"" + Search.DOMAIN_LEVEL + "\":[],\""
			+ Search.AUTHOR_TYPE + "\":[],\"" + Search.MESSAGE_TYPE + "\":[]}}";

	public static String changeQuery(String query, String field, String[] values) {
		JSONObject series;
		try {
			series =new JSONObject(query);
		} catch (Exception e) {
			return null;
		}
		if (!series.containsKey(Search.QUERY_CATEGORY))
			return null;
		JSONObject category = series.getJSONObject(Search.QUERY_CATEGORY);
		if (category.containsKey(field))
			category.remove(field);
		category.put(field, values);
		series.put(Search.QUERY_CATEGORY, category);
		return series.toString();
	}

	/*
	 * JSONArray to String array
	 */
	public static String[] toStrArr(JSONArray objArr) {
		if (objArr.size() == 0)
			return null;
		String[] strArr = new String[objArr.size()];
		for (int i = 0; i < objArr.size(); i++)
			strArr[i] = objArr.getStr(i);
		return strArr;
	}
	
}
