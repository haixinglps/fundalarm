package cn.exrick.manager.isearch;

import java.util.HashMap;
import java.util.Map;


public class R {
	
	/**
	 * 来源
	 */
	public static Map<String, String> sources = new HashMap<String, String>();
	
	/**
	 * 元搜索配置信息
	 */
	public static Map<String, Map<String, T>> mspf = new HashMap<String, Map<String, T>>();

	/**
	 * 微博 cookies
	 */
	public static Map<String, Map<String, String>> msck = new HashMap<String, Map<String, String>>();

	/**
	 * 元搜索
	 */
	public static final String MS_ALL = "news,webpage,weibo,tieba,forum,wenda,weixin";

	/**
	 * 新闻
	 */
	public static final String MS_NEWS = "news";
	/**
	 * weixin
	 */
	public static final String MS_WEIXIN = "weixin";

	/**
	 * 网页
	 */
	public static final String MS_WEBPAGE = "webpage";

	/**
	 * 微博
	 */
	public static final String MS_WEIBO = "weibo";

	/**
	 * 贴吧
	 */
	public static final String MS_TIEBA = "tieba";

	/**
	 * 论坛
	 */
	public static final String MS_FORUM = "forum";

	/**
	 * 问答
	 */
	public static final String MS_WENDA = "wenda";

	public static final String MS_NEWS_ALL = "baidu,soso,qihu";
	public static final String MS_NEWS_BAIDU = "baidu";
	public static final String MS_NEWS_SOSO = "soso";
	public static final String MS_NEWS_QIHU = "qihu";

	
	public static final String MS_WEIXIN_ALL = "soso";
	public static final String MS_WEIXIN_SOSO = "soso";
	
	
	public static final String MS_WEBPAGE_ALL = "baidu,soso,qihu";
	public static final String MS_WEBPAGE_BAIDU = "baidu";
	public static final String MS_WEBPAGE_SOSO = "soso";
	public static final String MS_WEBPAGE_QIHU = "qihu";
	
	public static final String MS_WEIBO_ALL = "sina,tencent,sohu";
	public static final String MS_WEIBO_SINA = "sina";
	public static final String MS_WEIBO_TENCENT = "tencent";
	public static final String MS_WEIBO_SOHU = "sohu";

	public static final String MS_TIEBA_ALL = "baidu";
	public static final String MS_TIEBA_BAIDU = "baidu";

	public static final String MS_FORUM_ALL = "zhongsou,tianya";
	public static final String MS_FORUM_QIHOO = "qihoo";
	public static final String MS_FORUM_ZHONGSOU = "zhongsou";
	public static final String MS_FORUM_TIANYA = "tianya";

	public static final String MS_WENDA_ALL = "baidu,soso,qihu";
	public static final String MS_WENDA_BAIDU = "baidu";
	public static final String MS_WENDA_SOSO = "soso";
	public static final String MS_WENDA_QIHU = "qihu";

}
