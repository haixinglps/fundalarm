package cn.exrick.manager.isearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.isearch.cache.SearchCache;
//import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.zhongsou.search.core.SearchSystem;
import com.zhongsou.search.core.article.Article;
import com.zhongsou.search.core.conf.Configuration;
import com.zhongsou.search.core.net.control.QueryConn;
import com.zhongsou.search.core.query.ClusterHits;
import com.zhongsou.search.core.query.CubeHits;
import com.zhongsou.search.core.query.GroupHits;
import com.zhongsou.search.core.query.Hits;
import com.zhongsou.search.core.query.Term;
import com.zhongsou.search.core.query.WamHits;
import com.zhongsou.search.core.query.WordCloudHits;

import cn.exrick.manager.isearch.query.Search;
import cn.exrick.manager.isearch.query.SearchUtil;
import cn.exrick.manager.isearch.query.bean.CubeBeans;
import cn.exrick.manager.isearch.query.bean.GroupBeans;
import cn.exrick.manager.isearch.query.bean.RelationBeans;
import cn.exrick.manager.isearch.query.bean.SearchBeans;
import cn.exrick.manager.isearch.query.bean.WamBeans;
import cn.exrick.manager.isearch.query.bean.WordCloudBeans;
import cn.exrick.manager.isearch.util.DateUtil;
import cn.exrick.manager.isearch.util.common.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

/**
 * Isearch 全文检索系统
 * 
 * @author guiliangdong
 * 
 */
public class Isearch implements Search {

	// cache name
	public static final String name = "default";
	// cache path
	public static final String path = "client\\cache\\default";

	public static void init() throws Exception {

		Properties props = new Properties();

		InputStream input = null;
		try {
			input = Thread.currentThread().getContextClassLoader().getResourceAsStream("SearchSystem.conf");
			props.load(input);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SearchSystem.init(new Configuration(props));
		SearchSystem ss = SearchSystem.getInstance();
		SearchCache.init(name, ss, path, 5);
	}

	private SearchSystem ss;
	private String word;
	private Term term;
	private Term.Sorter sorter;
	private final Term.Counter counter = Term.Counter.exact;

	public Isearch() throws Exception {
		this(Search.SORT_DATE, Search.CACHE_MODE_BASE);
	}

	public Isearch(String sort, int mode) throws Exception {
		switch (mode) {
		case Search.CACHE_MODE_READ:
		case Search.CACHE_MODE_FRESH:
		case Search.CACHE_MODE_UPDATE:
			ss = SearchCache.getSsIns(name, mode);
			break;

		default:
			ss = SearchSystem.getInstance();
			// System.out.println(ss.feedinfo);
			// System.out.println(ss.descDb().toString());
			break;
		}
		if (Search.SORT_RELA.equals(sort))
			sorter = Term.Sorter.rela;
		else if (Search.SORT_RANDOM.equals(sort))
			sorter = Term.Sorter.RANDOM;
		else if (Search.SORT_DATE_ASC.equals(sort))
			sorter = Term.Sorter.date_asc;
		else if (Search.SORT_COPIES.equals(sort))
			sorter = Term.Sorter.COPIES;
		else
			sorter = Term.Sorter.date;
		term = new Term(ss, sorter, counter);
	}

	// ID
	public void in(String field, int[] values) throws Exception {
		if (values != null && values.length != 0) {
			term.in(field, values);
			term.setAskNum(values.length);
		}
	}

	// ID
	public void in(String field, long[] values) throws Exception {
		if (values != null && values.length != 0) {
			term.in(field, values);
			term.setAskNum(values.length);
		}
	}

	// 添加标题或正文关键词
	public void andText(String field, String value) throws Exception {
		if (value != null && value.trim().length() != 0) {
			value = value.trim();
			this.word = value;
			term.andText(field, value);
			// System.out.println("sql command:"+term.toString());
		}
	}

	// 添加查询字段
	public void andNotIndexedText(String field, String value) throws Exception {
		if (value != null && value.trim().length() != 0) {
			value = value.trim();
			if (!value.contains(","))
				term.andNotIndexedText(field, value);
			else
				term.andNotIndexedText(field, Convert.split(value, ","));
		}
	}

	// 添加查询字段
	public void andNotIndexedText(String field, String[] values) throws Exception {
		if (values != null && values.length != 0)
			term.andNotIndexedText(field, values);
	}

	// 添加分类字段
	public void andCategory(String field, String value) throws Exception {
		if (value != null && value.trim().length() != 0)
			if (!value.contains(","))
				term.andCategory(field, value);
			else
				term.andCategory(field, Convert.split(value, ","));
	}

	// 添加分类字段
	public void andCategory(String field, String[] values) throws Exception {
		if (values != null && values.length != 0)
			term.andCategory(field, values);
	}

	// 分组
	public void setGroup(String field) throws Exception {
		term.setGroup(field);
	}

	// 分组
	public void setGroup(String field, long begin, long step) throws Exception {
		term.setGroup(field, begin, step);
	}

	// 多字段分组
	public void setCube(String field1, String field2) throws Exception {
		term.setCube(field1, field2);
	}

	// 多字段分组
	public void setCube(String field1, long begin, long step, String field2) throws Exception {
		term.setCube(field1, begin, step, field2);
	}

	// 云关键词
	public void setWordCloud(String field, int maxWord) {
		term.setWordCloud(field, maxWord);
	}

	// 话题关系
	public void setWam(String field, int maxWord) {
		term.setWAM(field, maxWord);
	}

	// 聚类
	public void setCluster(String field) {
		term.setCluster(field);
	}

	// 聚类
	public void setCluster(String field, double distance, int maxloop, int minwordnum) {
		term.setCluster(field, distance, maxloop, minwordnum, 20, 40, 20);
	}

	// 转载
	public void setDistinct(String field) {
		term.setDistinct(field);
	}

	// 转载
	public void setDistinct(String field, int maxArticleInGroup, boolean useLastDateAsGroupDate) {
		term.setDistinct(field, maxArticleInGroup, useLastDateAsGroupDate);
	}

	// 大于[one]、小于[two]
	public void between(String field, int one, int two) throws Exception {
		term.between(field, one, two);
	}

	// 大于[one]、小于[two]
	public void between(String field, long one, long two) throws Exception {
		term.between(field, one, two);
	}

	// 等于
	public void equals(String field, int num) throws Exception {
		term.equal(field, num);
	}

	// 等于
	public void equals(String field, long num) throws Exception {
		term.equal(field, num);
	}

	// 大于等于
	public void greatEqual(String field, int num) throws Exception {
		term.greatEqual(field, num);
	}

	// 大于等于
	public void greatEqual(String field, long num) throws Exception {
		term.greatEqual(field, num);
	}

	// 大于
	public void greatThan(String field, int num) throws Exception {
		term.greatThan(field, num);
	}

	// 大于
	public void greatThan(String field, long num) throws Exception {
		term.greatThan(field, num);
	}

	// 小于等于
	public void lessEqual(String field, int num) throws Exception {
		term.LessEqual(field, num);
	}

	// 小于等于
	public void lessEqual(String field, long num) throws Exception {
		term.LessEqual(field, num);
	}

	// 小于
	public void lessThan(String field, int num) throws Exception {
		term.lessThan(field, num);
	}

	// 小于
	public void lessThan(String field, long num) throws Exception {
		term.lessThan(field, num);
	}

	// 结果返回起点
	public void setFirstResult(int start) {
		term.setStart(start);
	}

	// 结果返回数量
	public void setMaxResults(int num) {
		term.setAskNum(num);
	}

	// 统计
	public int count() throws Exception {
		System.out.println("服务器返回的数据量*3是：" + ss.count(term) * 3);

		QueryConn conn = null;
		// return ss.count(conn, term);
		return ss.count(term);
	}

	// 查询
	public SearchBeans query() throws Exception {
		Hits hits = ss.query(term);
		System.out.println(hits);
//	System.out.println(hits.get(0).getArticle().getString("UR"));
		return IsearchUtil.hitsToBeans(hits, word);
	}

	public Hits queryHits() throws Exception {

		Hits hits = ss.query(term);
		return hits;
	}

	// 查询
	public SearchBeans query2() throws Exception {
		Hits hits = ss.query(term);
		return IsearchUtil.hitsToBeans2(hits, word);
	}

	// 分组
	public GroupBeans group() throws Exception {
		GroupHits hits = ss.group(term);
		return IsearchUtil.hitsToBeans(hits);
	}

	// 多字段分组
	public CubeBeans cube() throws Exception {
		CubeHits hits = ss.cube(term);
		return IsearchUtil.hitsToBeans(hits);
	}

	// 关键词云
	public WordCloudBeans wordCloud() throws Exception {
		WordCloudHits hits = ss.wordcloud(term);
		return IsearchUtil.hitsToBeans(hits);
	}

	// 话题关系
	public WamBeans wam() throws Exception {
		WamHits hits = ss.wam(term);
		return IsearchUtil.hitsToBeans(hits);
	}

	// 话题关系
	public RelationBeans relation() throws Exception {
		WamHits hits = ss.wam(term);
		return IsearchUtil.hitsToRelations(hits);
	}

	// 聚类
	public SearchBeans cluster() throws Exception {
		ClusterHits hits = ss.cluster(term);
		return IsearchUtil.hitsToBeans(hits, word);
	}

	// 热门转载文章
	public SearchBeans distinct() throws Exception {
		Hits hits = ss.distinct(term);
		SearchBeans beans = IsearchUtil.hitsToBeans(hits, word);
		return beans;
	}

//	// 添加文章
//	public void addarticle(NewsContent wb, String domain, String emotion)
//			throws Exception {
//
//		Article art = new Article(ss);
//		// art.setValue("AU", );
//		art.setValue("TX", wb.getText());
//		art.setValue("UR", wb.getUrl());
//		art.setValue("TI", wb.getTitle());
//		art.setValue("RQ", wb.getPubdate());
//		art.setValue("DM", domain);
//		art.setValue("EM", emotion);
//		art.setValue("MD", "新闻");
//		// art.setValue("AL", wb.getRelay());
//		art.setValue("MT", "一般新闻");
//		art.setValue("CC", "综合");
//		art.setValue("AT", "新闻");
//		art.setValue("DL", "商业");
//		art.setValue("CH", "频道@网站@" + domain);
//		// art.setValue("TH", wb.getImg());
//
//		ss.addArticle(art);
//		ss.commit();
//
//		// ss.commit();
//	}

	public void addarticle2(String channel, String title, String txt, String url, String author, String location,
			String authortype, String messagetype, String mediatype, String channel_category, Date pubdate,
			String domainlevel, String domain, String emotion) throws Exception {

		// List<Article> list=new ArrayList<Article>();
		Article art = new Article(ss);
		art.setValue("AU", author);
		art.setValue("TX", txt);
		art.setValue("UR", url);
		art.setValue("TI", title);
		art.setValue("RQ", pubdate);
		art.setValue("DM", domain);
		art.setValue("EM", emotion);
		art.setValue("MD", mediatype);
		// art.setValue("AL", wb.getRelay());
		art.setValue("MT", messagetype);
		art.setValue("CC", channel_category);
		art.setValue("AT", authortype);
		art.setValue("DL", domainlevel);
		art.setValue("CH", channel + "@" + domain);
		// art.setValue("TH", wb.getImg());

		// ss.addArticle(art);
		// list.add(art);
		// ss.addArticle(list);

		ss.addArticle(art);
		ss.commit();
		// System.out.println("信息提交成功");

	}

	// 参数
	public void addQuery(String query) throws Exception {
		// System.out.println("解析客户端query："+query);
		JSONObject series = null;
		try {
			series = new JSONObject(query);
		} catch (Exception e) {
			return;
		}
		/*
		 * ti, au, dm
		 */
		if (series.containsKey(Search.QUERY_TEXT)) {
			try {
				JSONObject text = series.getJSONObject(Search.QUERY_TEXT);
				for (Object obj : text.keySet()) {
					String key = (String) obj;
					String value = text.getStr(key);
					if (value == null || value.length() == 0)
						continue;
					if (Search.TITLE.equals(key)) {
						term.andText(Search.TITLE, value);
					} else if (Search.DOMAIN.equals(key)) {

						Pattern p = Pattern.compile("[\u4E00-\u9FA5]+");

						Matcher m = p.matcher(value);

						if (value.indexOf(",") != -1) {

							String[] weblist = Convert.split(value, ",");
							if (m.find()) {
								String[] namelist = new String[weblist.length];
								for (int i = 0; i < weblist.length; i++) {
									Matcher m2 = p.matcher(weblist[i]);

									if (m2.find())
										namelist[i] = R.sources.get(weblist[i]);
									else
										namelist[i] = weblist[i];
									if (namelist[i] == null)
										namelist[i] = "";
								}
								term.andCategory(Search.DOMAIN, namelist);
							} else {
								// boolean dmflag=true;
								// for (String s : weblist) {
								// if (!R.sources.containsKey(s)) {
								// dmflag = false;
								// break;
								// }
								// }
								// if(dmflag)
								term.andCategory(Search.DOMAIN, weblist);
								// else
								// {
								// for (int j = 0; j < weblist.length; j++)
								// weblist[j] = "%" + weblist[j] + "%";
								// term.andNotIndexedText("HO", weblist);
								// }
							}

						} else {

							if (m.find())
								value = R.sources.get(value);
							if (value == null)
								value = "";

							// if(R.sources.containsKey(value))
							term.andCategory(Search.DOMAIN, value);
							// else
							// term.andNotIndexedText("HO", "%" + value + "%");

							// term.andNotIndexedText("HO", "%" + value + "%");
							// System.out.println("hotest:"+value);

						}
					} else if (Search.AUTHOR.equals(key)) {
						value = ((value.indexOf("@") == -1) ? "%" : "") + value + "%";
						// System.out.println("author:"+value);
						term.andNotIndexedText(Search.AUTHOR, value);
					}
				}
			} catch (Exception e) {

			}
		}
		/*
		 * em, md, at, dl, mt
		 */
		if (series.containsKey(Search.QUERY_CATEGORY)) {
			try {
				JSONObject category = series.getJSONObject(Search.QUERY_CATEGORY);
				for (Object obj : category.keySet()) {
					String key = (String) obj;
					JSONArray values = category.getJSONArray(key);
					if (values.size() == 0)
						continue;
					// System.out.println("key:"+key);
					// System.out.println("values:"+values);

					term.andCategory(key, SearchUtil.toStrArr(values));
				}
			} catch (Exception e) {

			}
		}
	}

	// 添加查询条件
	public void andQuery(String query) throws Exception {
		System.out.println(query);
		if (query == null || query.length() == 0)
			return;
		String[] array = Convert.split(query, "[|]|,");
		for (int i = 0; i < array.length; i++) {
			String value;
			try {
				value = array[i + 1];
				if (value.length() == 0)
					continue;
			} catch (Exception e) {
				return;
			}
			String field = array[i];
			// System.out.println(" 查询条件追踪0：field:"+field);

			if ("MD,EM,DL,AT,MT,CC".contains(field)) {
				System.out.println(" 查询条件追踪1：field:" + field);
				int count = Convert.getInt(value);
				if (count == 0)
					continue;
				String[] values = new String[count];
				for (int j = 0; j < count; j++) {
					values[j] = array[i + j + 2];
				}
				if ("MD".equals(field))
					field = Search.MEDIA_TYPE;
				else if ("EM".equals(field))
					field = Search.EMOTION;
				else if ("DL".equals(field))
					field = Search.DOMAIN_LEVEL;
				else if ("AT".equals(field))
					field = Search.AUTHOR_TYPE;
				else if ("MT".equals(field))
					field = Search.MESSAGE_TYPE;
				else if ("CC".equals(field))
					field = Search.CHANNEL_CATEGORY;
				term.andCategory(field, values);
				System.out.println(" 查询条件追踪：field:" + field + " --- " + values + ":" + values);
			} else if ("TI".equals(field))
				term.andText(Search.TITLE, value);
			else if ("AU".equals(field))
				term.andNotIndexedText(Search.AUTHOR, value);
			else if ("DM".equals(field)) {
				term.andCategory(Search.DOMAIN, value);
			}
		}
	}

	// 添加查询条件
	public void andQueryWithTime(String query) throws Exception {
		if (query == null || query.length() == 0)
			return;
		String[] array = Convert.split(query, "[|]|,");
		for (int i = 0; i < array.length; i++) {
			String value;
			try {
				value = array[i + 1];
				if (value.length() == 0)
					continue;
			} catch (Exception e) {
				return;
			}
			String field = array[i];
			if ("MD,EM,DL,AT,MT".contains(field)) {
				int count = Convert.getInt(value);
				if (count == 0)
					continue;
				String[] values = new String[count];
				for (int j = 0; j < count; j++) {
					values[j] = array[i + j + 2];
				}
				if ("MD".equals(field))
					field = Search.MEDIA_TYPE;
				else if ("EM".equals(field))
					field = Search.EMOTION;
				else if ("DL".equals(field))
					field = Search.DOMAIN_LEVEL;
				else if ("AT".equals(field))
					field = Search.AUTHOR_TYPE;
				else if ("MT".equals(field))
					field = Search.MESSAGE_TYPE;
				term.andCategory(field, values);
			} else if ("TI".equals(field)) {
				term.andText(Search.TITLE, value);
			} else if ("AU".equals(field)) {
				if (value.indexOf("@") == -1)
					value += "%";
				term.andNotIndexedText(Search.AUTHOR, value);
			} else if ("DM".equals(field)) {
//				String regex = "[\u4E00-\u9FA5]+";
//				Pattern p = Pattern.compile(regex);
//				Matcher m = p.matcher(value);
//				if (m.matches())
//					value = new Profile(Profile.SOURCE).getEncodeKey(value);
				term.andCategory(Search.DOMAIN, value);
			} else if ("RQ".equals(field)) {
				long[] time = new long[2];
				if (value.length() > 2)
					time = DateUtil.getSearchTime(value + "," + array[i + 2]);
				else
					time = DateUtil.getSearchTime(value);
				term.between(Search.TIME, time[0], time[1]);
			}
		}
	}

	// 清理 Term
	public void clear() {
		term = new Term(ss, sorter, counter);
	}

	public static void main(String[] args) {
		String query = "[[MD:新闻|论坛|博客|SNS],[DL:中央媒体|地方媒体|商业全国|商业地方|商业区域],"
				+ "[CC:国内|国际|财经|时政|军事|娱乐|体育|房产|汽车|医疗|科技|教育|社会|互联网|微博|博客],"
				+ "[AT:偶尔发帖|一般参与者|活跃分子|新闻记者|新注册],[EM:正面|负面|无正负|有正有负]," + "[MT:微博正文|博文|一般新闻|主贴|回帖]]";
		String[] categories = Convert.split(query, ",");
		for (String category : categories) {
			try {
				String[] array = Convert.split(category, "\\[\\]\\:");
				String field = array[0];
				String value = array[1];
				String[] values = Convert.split(value, "|");
				System.out.println(field + " : " + values.length);
			} catch (Exception e) {

			}
		}
	}

}
