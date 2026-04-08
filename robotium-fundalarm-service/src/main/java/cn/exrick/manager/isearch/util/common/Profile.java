package cn.exrick.manager.isearch.util.common;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Profile {

	public static final String ESOU_CONF = "esou.conf";
	public static final String SOURCE = "source.conf";
	public static final String CHART_SUBTITLE_SOURCE = "chart.subtitle.source";
	public static final String CHART_CREDITS_HREF = "chart.credits.href";
	public static final String CHART_CREDITS_TEXT = "chart.credits.text";
	public static final String ITEXT_BASEFONT = "itext.base.font";

	private Properties p;

	/**
	 * 配置文件数据
	 * 
	 * @param fileName
	 *            配置文件名称
	 */
	public Profile(String fileName) throws Exception {
		/*
		 * // %Tomcat_Home%/webapps File file = new File(getAppsPath() +
		 * fileName); if (!file.exists()) // classes file = new
		 * File(getClassPath() + fileName); if (!file.exists()) throw new
		 * Exception("系统配置文件 " + fileName + " 丢失");
		 */
		InputStream in = null;
		try {
			// in = new FileInputStream(file);
			in = this.getClass().getClassLoader().getResourceAsStream(fileName);
			p = new Properties();
			p.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public String getValue(String key) {
		return getValue(key, "");
	}

	public String getValue(String key, String defaultValue) {
		if (key == null)
			return defaultValue;
		String value = p.getProperty(key, defaultValue);
		return value;
	}

	/**
	 * 返回值为中文时编码，配置文件无信息时，返回 key
	 */
	public String getEncodeValue(String key) {
		if (key == null)
			return key;
		String value = p.getProperty(key, key);
		try {
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * key 为中文时，编译
	 */
	public String getEncodeKey(String value) {
		if (value == null)
			return value;
		String temp = value;
		try {
			value = new String(value.getBytes("UTF-8"), "ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String key = p.getProperty(value, temp);
		return key;
	}

	public List<String> keys() {
		List<String> list = new ArrayList<String>();
		Set<Object> keys = p.keySet();
		for (Object key : keys) {
			list.add((String) key);
		}
		return list;
	}

	public Map<String, String> values() {
		Map<String, String> map = new HashMap<String, String>();
		Set<Object> keys = p.keySet();
		for (Object keyObj : keys) {
			String key = (String) keyObj, value = p.getProperty(key);
			try {
				value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put(key, value);
		}
		return map;
	}

	/*
	 * private String getAppsPath() { String path =
	 * System.getProperty("user.dir").replace("bin", "webapps"); return path +
	 * "\\conf\\"; }
	 * 
	 * private String getClassPath() { ClassLoader loader =
	 * Thread.currentThread().getContextClassLoader(); if (loader == null)
	 * loader = ClassLoader.getSystemClassLoader(); return
	 * loader.getResource("").getPath() + "\\"; }
	 */

	public static void main(String[] args) throws Exception {
		Profile profile = new Profile(SOURCE);
		System.out.println(profile.getEncodeKey("新浪"));
	}

}
