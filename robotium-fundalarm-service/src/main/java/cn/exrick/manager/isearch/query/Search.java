package cn.exrick.manager.isearch.query;

//import com.cnxunao.esou.query.bean.CubeBeans;
//import com.cnxunao.esou.query.bean.GroupBeans;
//import com.cnxunao.esou.query.bean.RelationBeans;
//import com.cnxunao.esou.query.bean.SearchBeans;
//import com.cnxunao.esou.query.bean.WamBeans;
//import com.cnxunao.esou.query.bean.WordCloudBeans;
import com.isearch.cache.SearchCache;

import cn.exrick.manager.isearch.query.bean.CubeBeans;
import cn.exrick.manager.isearch.query.bean.GroupBeans;
import cn.exrick.manager.isearch.query.bean.RelationBeans;
import cn.exrick.manager.isearch.query.bean.SearchBeans;
import cn.exrick.manager.isearch.query.bean.WamBeans;
import cn.exrick.manager.isearch.query.bean.WordCloudBeans;

/**
 * 检索系统接口
 * 
 * @author guild
 * 
 */
public interface Search {

	/**
	 * ID
	 * 
	 * @param field
	 *            IField ISEARCH_ID
	 * @param values
	 * @throws Exception
	 */
	public void in(String field, int[] values) throws Exception;

	/**
	 * ID
	 * 
	 * @param field
	 *            IField ISEARCH_ID
	 * @param values
	 * @throws Exception
	 */
	public void in(String field, long[] values) throws Exception;

	/**
	 * 添加标题或正文关键词
	 * 
	 * @param field
	 *            IField TITLE, TEXT
	 * @param value
	 * @throws Exception
	 */
	public void andText(String field, String value) throws Exception;

	/**
	 * 添加查询条件
	 * 
	 * @param field
	 *            IField AUTHOR
	 * @param value
	 * @throws Exception
	 */
	public void andNotIndexedText(String field, String value) throws Exception;

	/**
	 * 添加查询条件
	 * 
	 * @param field
	 *            IField AUTHOR
	 * @param values
	 * @throws Exception
	 */
	public void andNotIndexedText(String field, String[] values)
			throws Exception;

	/**
	 * 添加分类条件
	 * 
	 * @param field
	 *            IField DOMAIN, EMOTION, DOMAIN_LEVEL, AUTHOR_TYPE, MEDIA_TYPE,
	 *            MESSAGE_TYPE, CHANNEL_CATEGORY, LOCATION...
	 * @param value
	 * @throws Exception
	 */
	public void andCategory(String field, String value) throws Exception;

	/**
	 * 添加分类条件
	 * 
	 * @param field
	 *            IField DOMAIN, EMOTION, DOMAIN_LEVEL, AUTHOR_TYPE, MEDIA_TYPE,
	 *            MESSAGE_TYPE, CHANNEL_CATEGORY, LOCATION...
	 * @param values
	 * @throws Exception
	 */
	public void andCategory(String field, String[] values) throws Exception;

	/**
	 * 分组
	 */
	public void setGroup(String field) throws Exception;

	/**
	 * 分组
	 */
	public void setGroup(String field, long begin, long step) throws Exception;

	/**
	 * 多字段分组
	 */
	public void setCube(String field1, String field2) throws Exception;

	/**
	 * 多字段分组
	 */
	public void setCube(String field1, long begin, long step, String field2)
			throws Exception;

	/**
	 * 云关键词
	 */
	public void setWordCloud(String field, int maxWord);
	
	/**
	 * 话题关系
	 */
	public void setWam(String field, int maxWord);

	/**
	 * 聚类
	 */
	public void setCluster(String field);

	/**
	 * 聚类
	 */
	public void setCluster(String field, double distance, int maxloop,
			int minwordnum);

	/**
	 * 转载
	 */
	public void setDistinct(String field);

	/**
	 * 转载
	 */
	public void setDistinct(String field, int maxArticleInGroup,
			boolean useLastDateAsGroupDate);

	/**
	 * 大于[one]、小于[two]
	 */
	public void between(String field, int one, int two) throws Exception;

	/**
	 * 大于[one]、小于[two]
	 */
	public void between(String field, long one, long two) throws Exception;

	/**
	 * 等于
	 */
	public void equals(String field, int num) throws Exception;

	/**
	 * 等于
	 */
	public void equals(String field, long num) throws Exception;

	/**
	 * 大于等于
	 */
	public void greatEqual(String field, int num) throws Exception;

	/**
	 * 大于等于
	 */
	public void greatEqual(String field, long num) throws Exception;

	/**
	 * 大于
	 */
	public void greatThan(String field, int num) throws Exception;

	/**
	 * 大于
	 */
	public void greatThan(String field, long num) throws Exception;

	/**
	 * 小于等于
	 */
	public void lessEqual(String field, int num) throws Exception;

	/**
	 * 小于等于
	 */
	public void lessEqual(String field, long num) throws Exception;

	/**
	 * 小于
	 */
	public void lessThan(String field, int num) throws Exception;

	/**
	 * 小于
	 */
	public void lessThan(String field, long num) throws Exception;

	/**
	 * 结果返回起点
	 */
	public void setFirstResult(int start);

	/**
	 * 结果返回数量
	 */
	public void setMaxResults(int num);

	/**
	 * 统计
	 */
	public int count() throws Exception;
	//public Long count2() throws Exception;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * 查询
	 */
	public SearchBeans query() throws Exception;
	/**
	 * 查询
	 */
	public SearchBeans query2() throws Exception;

	/**
	 * 分组
	 */
	public GroupBeans group() throws Exception;

	/**
	 * 多字段分组
	 */
	public CubeBeans cube() throws Exception;

	/**
	 * 云关键字
	 */
	public WordCloudBeans wordCloud() throws Exception;

	/**
	 * 话题关系
	 */
	public WamBeans wam() throws Exception;
	
	/**
	 * 话题关系
	 */
	public RelationBeans relation() throws Exception;
	
	/**
	 * 聚类
	 */
	public SearchBeans cluster() throws Exception;
	
	/**
	 * 转载
	 */
	public SearchBeans distinct() throws Exception;
	
	/**
	 * 添加查询条件
	 */
	public void addQuery(String query) throws Exception;

	/**
	 * 添加查询条件
	 */
	public void andQuery(String query) throws Exception;

	/**
	 * 添加查询条件
	 */
	public void andQueryWithTime(String query) throws Exception;

	/**
	 * 清理 Isearch
	 */
	public void clear();

	/**
	 * 时间排序
	 */
	public static final String SORT_DATE = "date";

	/**
	 * 相关度排序
	 */
	public static final String SORT_RELA = "rela";

	/**
	 * 随机排序
	 */
	public static final String SORT_RANDOM = "random";

	/**
	 * 时间反序排序
	 */
	public static final String SORT_DATE_ASC = "date_asc";
	
	/**
	 * 转载排序
	 */
	public static final String SORT_COPIES = "copies";

	/**
	 * SearchSystem init
	 */
	public static final int CACHE_MODE_BASE = 0;

	/**
	 * CACHE_READ 只读
	 */
	public static final int CACHE_MODE_READ = SearchCache.CACHE_READ;

	/**
	 * CACHE_UPDATE 更新
	 */
	public static final int CACHE_MODE_UPDATE = SearchCache.CACHE_UPDATE;

	/**
	 * CACHE_FRESH 刷新
	 */
	public static final int CACHE_MODE_FRESH = SearchCache.CACHE_FRESH;

	/**
	 * 全文检索 ID
	 */
	public static final String ISEARCH_ID = "ID";

	/**
	 * 标题
	 */
	public static final String TITLE = "TI";

	/**
	 * 正文
	 */
	public static final String TEXT = "TX";

	/**
	 * 参与人
	 */
	public static final String AUTHOR = "AU";

	/**
	 * 参与人类型(偶尔发帖、一般参与者、活跃分子、新闻记者、新注册)
	 */
	public static final String AUTHOR_TYPE = "AT";

	/**
	 * 频道(媒体类型@频道分类)
	 */
	public static final String CHANNEL = "CH";

	/**
	 * 频道类型(国内、国际、财经、时政、军事、娱乐、体育、房产、汽车、医疗、科技、教育、社会、互联网、微博、博客)
	 */
	public static final String CHANNEL_CATEGORY = "CC";

	/**
	 * 倾向(正面、负面、无正负、有正有负)
	 */
	public static final String EMOTION = "EM";

	/**
	 * 媒体类型 (新闻、论坛、博客、SNS)
	 */
	public static final String MEDIA_TYPE = "MD";

	/**
	 * 信息类型 (微博正文、博文、一般新闻、主题帖、回帖)
	 */
	public static final String MESSAGE_TYPE = "MT";

	/**
	 * 网站
	 */
	public static final String DOMAIN = "DM";

	/**
	 * 网站级别 (中央媒体、地方媒体、商业全国、商业行业、商业区域)
	 */
	public static final String DOMAIN_LEVEL = "DL";

	/**
	 * 地域信息
	 */
	public static final String LOCATION = "LC";

	/**
	 * 链接网址
	 */
	public static final String URL = "UR";

	/**
	 * 发布时间
	 */
	public static final String TIME = "RQ";

	/**
	 * 浏览数
	 */
	public static final String VIEW_NUM = "";

	/**
	 * 回复数
	 */
	public static final String REPLY_NUM = "";

	/**
	 * 转载数
	 */
	public static final String RESHIP_NUM = "";
	
	/**
	 * 日期格式
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	
	public static final String QUERY_TEXT = "text";
	public static final String QUERY_CATEGORY = "category";

}
