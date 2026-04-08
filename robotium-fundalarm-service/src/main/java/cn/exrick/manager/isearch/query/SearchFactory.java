package cn.exrick.manager.isearch.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.exrick.manager.isearch.Isearch;

/**
 * 检索系统工厂
 * 
 * @author guild
 * 
 */
public class SearchFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchFactory.class);
	
	private static volatile boolean isearchAvailable = false;
	private static volatile String errorMessage = null;
	
	public static void init() {
		try {
			Isearch.init();
			isearchAvailable = true;
			logger.info("搜索引擎初始化成功");
		} catch (Exception e) {
			isearchAvailable = false;
			errorMessage = e.getMessage();
			logger.error("搜索引擎初始化失败，将使用数据库搜索降级: {}", e.getMessage());
			// 不抛出异常，让应用继续启动
		}
	}
	
	public static boolean isIsearchAvailable() {
		return isearchAvailable;
	}
	
	public static String getErrorMessage() {
		return errorMessage;
	}

	public static Search getSearch() throws Exception {
		if (!isearchAvailable) {
			throw new RuntimeException("搜索引擎不可用: " + errorMessage + "，请使用数据库搜索");
		}
		return new Isearch();
	}

	public static Search getSearch(String sort, int mode) throws Exception {
		if (!isearchAvailable) {
			throw new RuntimeException("搜索引擎不可用: " + errorMessage + "，请使用数据库搜索");
		}
		return new Isearch(sort, mode);
	}

}
