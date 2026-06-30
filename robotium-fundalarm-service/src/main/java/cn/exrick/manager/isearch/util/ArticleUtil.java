package cn.exrick.manager.isearch.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhongsou.search.core.article.Article;

//import cn.exrick.manager.isearch.entities.Article;
//import cn.exrick.manager.isearch.entities.Subject;
import cn.exrick.manager.isearch.query.bean.SearchBean;
import cn.exrick.manager.isearch.query.bean.SearchBeans;

public class ArticleUtil {

	/**
	 * 转为预警文章
	 * 
	 * @param beans
	 * @return
	 */
//	public static List<Article> parseAlarm(SearchBeans beans, Subject subject, Date date) {
//		if (beans == null || beans.size() == 0)
//			return null;
//		List<Article> articles = new ArrayList<Article>();
//		for (int i = 0; i < beans.size(); i++) {
//			SearchBean bean = beans.get(i);
//			if (bean == null)
//				continue;
//			Article article = parse(bean);
//			article.setDate(date);
//			article.setSubject(subject);
//			article.setUser(subject.getUser());
//			article.setOrgan(subject.getOrgan());
//			articles.add(article);
//		}
//		return articles;
//	}
//
//	private static Article parse(SearchBean bean) {
//		if (bean == null)
//			return null;
//		Article article = new Article();
//		article.setSearchId(bean.getSearchId());
//		article.setWord(bean.getWord());
//		article.setTitle(bean.getTi());
//		article.setAuthor(bean.getAu());
//		article.setSummary(bean.getSm());
//		article.setPubdate(bean.getPubdate());
//		article.setUrl(bean.getUr());
//		article.setDomain(bean.getDm());
//		article.setDomainSource(bean.getDs());
//		article.setDomainLevel(bean.getDl());
//		article.setMediaType(bean.getMd());
//		article.setChannel(bean.getCh());
//		article.setChannelCategory(bean.getCc());
//		article.setMessageType(bean.getMt());
//		// article.setLocation(bean.getLc());
//		article.setEmotion(bean.getEm());
//		article.setAuthorType(bean.getAt());
//		article.setRepeatNum(bean.getRepeatNum());
//		article.setReplyNum(bean.getReplyNum());
//		article.setViewNum(bean.getViewNum());
//		return article;
//	}

	/**
	 * 构造 ch 频道搜索展示标题：去掉 TI 中的频道名称《...》，并拼接 TX 中的标签/描述，
	 * 使搜索结果包含关键词且不暴露数据来源频道名。
	 */
	public static String buildChannelTitle(Article article) {
		if (article == null) {
			return "";
		}
		String ti = article.getString("TI");
		String tx = article.getString("TX");
		String tiPart = stripChannelName(ti);
		String txPart = extractTags(tx);
		if (tiPart != null && !tiPart.trim().isEmpty()) {
			if (txPart != null && !txPart.trim().isEmpty()) {
				return clean(tiPart + " " + txPart);
			}
			return clean(tiPart);
		}
		return clean(txPart != null ? txPart : tx);
	}

	private static String stripChannelName(String ti) {
		if (ti == null) {
			return null;
		}
		return ti.replaceFirst("^《[^》]+》\\s*", "").trim();
	}

	private static String extractTags(String tx) {
		if (tx == null) {
			return null;
		}
		String s = tx.replace("!$CDATA$", "").replace("![CDATA[", "").replace("]]", "");
		// 去掉开头的 *.mp4 文件名前缀
		s = s.replaceFirst("^.*?\\.mp4\\s+", "");
		// 去掉末尾的文件编号（如 250925-3）及其后的空格
		s = s.replaceAll("\\s+\\d{6}-\\d+(\\s+|$)", " ");
		return s.trim();
	}

	private static String clean(String s) {
		if (s == null) {
			return "";
		}
		s = s.replace("\r", "").replace("\n", "").replace("#", "");
		s = s.replace("!$CDATA$", "").replace("![CDATA[", "").replace("]]", "");
		s = s.replace("【视频】", "").replace("【图片】", "");
		return s.trim();
	}

	public static void main(String[] args) {

	}

}
