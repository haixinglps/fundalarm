package cn.exrick.manager.isearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.exrick.manager.isearch.query.Search;
import cn.exrick.manager.isearch.query.bean.CubeBean;
import cn.exrick.manager.isearch.query.bean.CubeBeans;
import cn.exrick.manager.isearch.query.bean.GroupBean;
import cn.exrick.manager.isearch.query.bean.GroupBeans;
import cn.exrick.manager.isearch.query.bean.RelationBean;
import cn.exrick.manager.isearch.query.bean.RelationBeans;
import cn.exrick.manager.isearch.query.bean.SearchBean;
import cn.exrick.manager.isearch.query.bean.SearchBeans;
import cn.exrick.manager.isearch.query.bean.WamBeans;
import cn.exrick.manager.isearch.query.bean.WordBean;
import cn.exrick.manager.isearch.query.bean.WordCloudBeans;
import cn.exrick.manager.isearch.util.DateUtil;
import cn.exrick.manager.isearch.util.common.Profile;
import com.zhongsou.search.core.SearchSystem;
import com.zhongsou.search.core.article.Article;
import com.zhongsou.search.core.query.ClusterGroup;
import com.zhongsou.search.core.query.ClusterHits;
import com.zhongsou.search.core.query.CubeHit;
import com.zhongsou.search.core.query.CubeHits;
import com.zhongsou.search.core.query.DmkWord;
import com.zhongsou.search.core.query.GroupHit;
import com.zhongsou.search.core.query.GroupHits;
import com.zhongsou.search.core.query.Hit;
import com.zhongsou.search.core.query.Hits;
import com.zhongsou.search.core.query.WamHits;
import com.zhongsou.search.core.query.WordCloudHits;
import com.zhongsou.search.core.query.draw.WamCNode;
import com.zhongsou.search.core.query.draw.WamCalculator;
import com.zhongsou.search.core.query.draw.WamHtml;
import com.zhongsou.search.core.util.HitsUtil;

/**
 * Isearch 检索结果转换为包装类
 * 
 * @author guiliangdong
 * 
 */
public class IsearchUtil {

	/*
	 * 简单查询，转载查询
	 */
	protected static SearchBeans hitsToBeans(Hits hits, String word) throws Exception {
		SearchBeans beans = new SearchBeans();
		if (hits == null)
			return beans;
		// 删除相同 url和标题
	//	hits = HitsUtil.removeRepeat(hits, Search.URL);
		//System.out.println("histszie: "+hits.size());
		//hits = HitsUtil.removeRepeat(hits, Search.TITLE);
 //System.out.println("hits is not null"+hits.size());
		Profile profile = new Profile(Profile.SOURCE);
		Highlight hl = new Highlight();
		for (int i = 0; i < hits.size(); i++) {
			Hit hit = hits.get(i);
			SearchBean bean = hitToBean(hit, profile, word, hl);
			if (bean == null)
				continue;
			beans.add(bean);
		}
		//beans=rRepeat(beans.list());
		beans.setTotal(hits.getTotal());
		return beans;
	}

	public static SearchBeans rRepeat(List<SearchBean> articleList) {
		SearchBeans articles = new SearchBeans ();
		Set<String> tiSet = new HashSet<String>();
		for (SearchBean article : articleList) {
			String ti = extChinese(article.getTi());
			if (tiSet.add(ti)||ti.equals("")) {
				articles.add(article);
				continue;
			}
			for (SearchBean art : articles.list()) {
				if (extChinese(art.getTi()).equals(ti)) {
					if(ti.length()>=20)
						article.setTi(ti.substring(0,20));
					
					art.getSubBeans().add(article);
					break;
				}
			}
		}
		
		return articles;
	}
	
	public static String extChinese(String source) {
		String regex="([\u4e00-\u9fa5]+)";
		Matcher matcher = Pattern.compile(regex).matcher(source);
		String result = "";
		while (matcher.find()) {
			result += matcher.group(0);
		}
	//	System.out.println("提取有效汉字:"+source+"("+result+")");
		return result;
	}
	protected static SearchBeans hitsToBeans2(Hits hits, String word) throws Exception {
		SearchBeans beans = new SearchBeans();
		if (hits == null)
			return beans;
		// 删除相同 url和标题
//		hits = HitsUtil.removeRepeat(hits, Search.TITLE);
		//System.out.println("histszie: "+hits.size());
		//hits = HitsUtil.removeRepeat(hits, Search.TITLE);
// System.out.println("已经安装标题排重了");
		beans.setTotal(hits.getTotal());
		Profile profile = new Profile(Profile.SOURCE);
		Highlight hl = new Highlight();
		for (int i = 0; i < hits.size(); i++) {
			Hit hit = hits.get(i);
			SearchBean bean = hitToBean(hit, profile, word, hl);
			if (bean == null)
				continue;
			beans.add(bean);
		}
		return beans;
	}
	
	
	
	
	
	/*
	 * 组查询
	 */
	protected static GroupBeans hitsToBeans(GroupHits hits) {
		GroupBeans beans = new GroupBeans();
		if (hits == null)
			return beans;
		beans.setTotal(hits.getTotalOfArticle());
		for (int i = 0; i < hits.size(); i++) {
			GroupHit hit = hits.get(i);
			if (hit == null)
				continue;
			GroupBean bean = new GroupBean();
			bean.setKey(hit.getKey());
			bean.setNum(hit.getNum());
			beans.add(bean);
		}
		return beans;
	}

	/*
	 * 组查询
	 */
	protected static CubeBeans hitsToBeans(CubeHits hits) {
		CubeBeans beans = new CubeBeans();
		if (hits == null)
			return beans;
		
		List<GroupHit> groupHitList = hits.getSumHitsOf2ndField();
		List<GroupBean> groupBeanList = new ArrayList<GroupBean>();
		for (GroupHit hit : groupHitList) {
			GroupBean bean = new GroupBean();
			bean.setKey(hit.getKey());
			bean.setNum(hit.getNum());
			groupBeanList.add(bean);
		}
		beans.setSubBeans(groupBeanList);
		for (int i = 0; i < hits.size(); i++) {
			CubeHit hit = hits.get(i);
			if (hit == null)
				continue;
			CubeBean bean = new CubeBean();
			bean.setKey(hit.getKey());
			bean.setNum(hit.getNum());
			List<GroupHit> subHitList = hit.getSubHitList();
			List<GroupBean> subBeans = new ArrayList<GroupBean>();
			for (GroupHit gHit : subHitList) {
				GroupBean gBean = new GroupBean();
				gBean.setKey(gHit.getKey());
				gBean.setNum(gHit.getNum());
				subBeans.add(gBean);
			}
			bean.setSubBeans(subBeans);
			beans.add(bean);
		}
		beans.setTotal(hits.getTotalOfArticle());
		//System.out.println("发现文章数"+hits.getTotalOfArticle()+" "+beans.getTotal());
		return beans;
	}

	/*
	 * 云关键词查询
	 */
	protected static WordCloudBeans hitsToBeans(WordCloudHits hits) {
		WordCloudBeans beans = new WordCloudBeans();
		if (hits == null)
			return beans;
		for (int i = 0; i < hits.getWordlist().size(); i++) {
			DmkWord dmkWord = hits.getWord(i);
			WordBean bean = new WordBean();
			bean.setWord(dmkWord.getWord());
			bean.setMark(dmkWord.getMark());
			
			bean.setIdf(dmkWord.getIdf());
			bean.setDocnum(dmkWord.getDocnum());
			beans.add(bean);
		}
		beans.setDocNum(hits.getDocNum());
		beans.setDocTotal(hits.getDocTotal());
		return beans;
	}

	/*
	 * 聚类查询
	 */
	protected static SearchBeans hitsToBeans(ClusterHits hits, String word) throws Exception {
		SearchBeans beans = new SearchBeans();
		if (hits == null)
			return beans;
		beans.setTotal(hits.getTotalDocNum());
		Profile profile = new Profile(Profile.SOURCE);
		Highlight hl = new Highlight();
		List<ClusterGroup> groupList = hits.getGroupList();
		if (groupList == null)
			return beans;
		System.out.println("grouplist size:"+groupList.size());
		for (ClusterGroup group : groupList) {
			List<Hit> hitList = group.getHitList();
			
			if (hitList.size() == 0)
				continue;
			//Hits hits2=new Hits(SearchSystem.getInstance());
			// hits2 = HitsUtil.removeRepeat(hits2, Search.TITLE);
            
			Hit hit = hitList.get(0);
			SearchBean bean = hitToBean(hit, profile, word, hl);
			bean.setTi(bean.getTi().replaceAll("<[/]?em>", ""));
			bean.setWord(group.getWordListStr());
			if(bean.getTi().length()>20)
				bean.setTi(bean.getTi().substring(0,20));
			bean.setTi(bean.getTi());
			if (bean == null)
				continue;
			if (hitList.size() > 1) {
				SearchBeans subBeans = new SearchBeans();
				for (int i = 1; i < hitList.size(); i++) {
					
					SearchBean subBean = hitToBean(hitList.get(i), profile, word, hl);
					if (subBean != null)
						subBeans.add(subBean);
				}
				bean.setSubBeans(subBeans);
			}
			beans.add(bean);
		}
		return beans;
	}

	/*
	 * 话题关系查询
	 */
	protected static WamBeans hitsToBeans(WamHits hits) {
		WamBeans beans = new WamBeans();
		if (hits == null)
			return beans;
		WamCalculator wamCalculator = new WamCalculator();
		wamCalculator.fillIn(hits, null);
		WamHtml wamHtml = new WamHtml(100);
		wamHtml.fillIn(wamCalculator.getResult());
		try {
			beans.setRelation(wamHtml.output_html());
		} catch (Exception e) {

		}
		for (int i = 0; i < hits.getWordlist().size(); i++) {
			DmkWord dmkWord = hits.getWord(i);
			WordBean bean = new WordBean();
			bean.setWord(dmkWord.getWord());
			bean.setMark(dmkWord.getMark());
			bean.setIdf(dmkWord.getIdf());
			bean.setDocnum(dmkWord.getDocnum());
			beans.add(bean);
		}
		beans.setDocNum(hits.getDocNum());
		beans.setDocTotal(hits.getDocTotal());
		return beans;
	}

	private static SearchBean hitToBean(Hit hit, Profile profile, String word, Highlight hl) {
		
		if (hit == null)
			return null;
		SearchBean bean = new SearchBean();
		Article article = hit.getArticle();
		bean.setAt(article.getString(Search.AUTHOR_TYPE));
		String author="";
		author=article.getString(Search.AUTHOR);
		//if(author.length()>4)
		//	author=author.substring(0,4)+"..";
		bean.setAu(author);
		bean.setCc(article.getString(Search.CHANNEL_CATEGORY));
		bean.setCh(article.getString(Search.CHANNEL));
		bean.setDl(article.getString(Search.DOMAIN_LEVEL));
		String dm = article.getString(Search.DOMAIN);
		
		bean.setDm(dm);
		String []bankuai=article.getString(Search.CHANNEL).split("@");
		String ds = profile.getEncodeValue(dm);
		
		String ds2="";
		if(bankuai.length>=2)
		ds2=bankuai[1]+"_"+bankuai[0];
		bean.setDs2(ds2);
      //  System.out.println(hit.stringValue("01"+Search.TEXT));
		bean.setDs(ds);
		
		String sv=hit.stringValue("01"+Search.TEXT);
		bean.setSv(sv);
		
		
		
		bean.setEm(article.getString(Search.EMOTION));
		bean.setId(hit.getId());
		bean.setSearchId(hit.getId() + "");
		bean.setLc(article.getString(Search.LOCATION));
		bean.setMd(article.getString(Search.MEDIA_TYPE));
		bean.setMt(article.getString(Search.MESSAGE_TYPE));
		bean.setRepeatNum(hit.getTotalDupNum());
		bean.setRq(article.getLong(Search.TIME));
		bean.setPubdate(DateUtil.long2Str(article.getLong(Search.TIME), "MM月dd日 HH:mm"));
		bean.setPubdate2(DateUtil.long2Str(article.getLong(Search.TIME), "yyyy-MM-dd HH:mm:ss"));

		String ti = article.getString(Search.TITLE);
		if(ti.length()>20)
			ti=ti.substring(0,20)+".";
		//System.out.println("ti:"+ti);
		
		String tx = article.getString(Search.TEXT);
		bean.setSm(hl.highlight(tx, word, 200));
		//if (word != null && word.contains("FUZZY")) 
		//	word = word.substring(word.indexOf("(") + 1, word.lastIndexOf(")"));
		ti = hl.highlight(ti, word, ti.length() * 2);
		if (ti.length() == 0)
			ti = hl.highlight(tx, word, 20);
		
		bean.setTi(ti.replaceAll("<.*?>", "").replace("<", "").replace(">", ""));
		tx = hl.highlight(tx, word);
		bean.setTx(tx);
		bean.setUr(article.getString(Search.URL));
		//System.out.println("---------------------------------------ti:"+ti);
		
		return bean;
	}

	/**
	 * 话题关系转换
	 */
	@SuppressWarnings("unchecked")
	protected static RelationBeans hitsToRelations(WamHits hits) {
		WamCalculator calculator = new WamCalculator();
		calculator.fillIn(hits, new String[] {"http", "url", "2013", "2012"});
		List<WamCNode> wamCNodes = calculator.getResult();
		RelationBeans beans = new RelationBeans();
		if (wamCNodes == null)
			return beans;
		for (int i = 0; i < wamCNodes.size(); i++) {
			WamCNode wamCNode = wamCNodes.get(i);
			RelationBean bean = new RelationBean();
			String word = wamCNode.getWord();
			bean.setId(i);
			bean.setWord(word);
			bean.setDocnum(wamCNode.getDocnum());
			bean.setIdf(wamCNode.getDocnum());
			bean.setMark(wamCNode.getMark());
			bean.setRadius(wamCNode.getRadius());
			bean.setAngle(wamCNode.getAngle());
			if (wamCNode.getParent() != null) {
				String parentWord = wamCNode.getParent().getWord();
				for (RelationBean relationBean : beans.list()) {
					if (relationBean.getWord().equals(parentWord)) {
						bean.setParent(relationBean);
						break;
					}
				}
			}
			beans.add(bean);
		}
		return beans;
	}

}
