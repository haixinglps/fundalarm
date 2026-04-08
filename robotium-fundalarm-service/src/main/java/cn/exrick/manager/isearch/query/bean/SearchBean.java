package cn.exrick.manager.isearch.query.bean;

/**
 * 检索结果
 * 
 * @author guild
 * 
 */
public class SearchBean {

	private long id; // 全文库 Id
	private String searchId;
	private String word;
	private String ti; // 标题
	private String au; // 作者
	private String sm; // 摘要
	private String sv; // 摘要

	private String tx; // 正文
	private long rq; // 发布时间
	private String pubdate;
	private String pubdate2;

	private String ur; // 链接
	private String dm; // 网站
	private String ds; // 来源
	private String ds2; // 来源频道

	private String dl; // 网站级别
	private String md; // 媒体类型
	private String ch; // 频道 (cc + "@" + ds)
	private String cc; // 频道分类
	private String mt; // 信息类型
	private String lc; // 地区
	private String em; // 正负面
	private String at; // 作者类型
	private int repeatNum; // 转载量
	private int replyNum; // 回复量
	private int viewNum; // 浏览量
	private SearchBeans subBeans = new SearchBeans();

	public SearchBean() {
		subBeans = new SearchBeans();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getTi() {
		return ti;
	}

	public void setTi(String ti) {
		this.ti = ti;
	}

	public String getAu() {
		return au;
	}

	public void setAu(String au) {
		this.au = au;
	}

	public String getSm() {
		return sm;
	}

	public void setSm(String sm) {
		this.sm = sm;
	}

	public String getTx() {
		return tx;
	}

	public void setTx(String tx) {
		this.tx = tx;
	}

	public long getRq() {
		return rq;
	}

	public void setRq(long rq) {
		this.rq = rq;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	
	
	
	public String getPubdate2() {
		return pubdate2;
	}

	public void setPubdate2(String pubdate2) {
		this.pubdate2 = pubdate2;
	}
	
	
	
	
	

	public String getUr() {
		return ur;
	}

	public void setUr(String ur) {
		this.ur = ur;
	}

	public String getDm() {
		return dm;
	}

	public void setDm(String dm) {
		this.dm = dm;
	}

	public String getDs() {
		return ds;
	}

	public void setDs(String ds) {
		this.ds = ds;
	}
	public void setDs2(String ds2) {
		this.ds2 = ds2;
	}
	public String getDs2() {
		return ds2;
	}

	public String getDl() {
		return dl;
	}

	public void setDl(String dl) {
		this.dl = dl;
	}
	public String getSv() {
		return sv;
	}

	public void setSv(String sv) {
		this.sv= sv;
	}

	public String getMd() {
		return md;
	}

	public void setMd(String md) {
		this.md = md;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getMt() {
		return mt;
	}

	public void setMt(String mt) {
		this.mt = mt;
	}

	public String getLc() {
		return lc;
	}

	public void setLc(String lc) {
		this.lc = lc;
	}

	public String getEm() {
		return em;
	}

	public void setEm(String em) {
		this.em = em;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public int getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(int repeatNum) {
		this.repeatNum = repeatNum;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	public SearchBeans getSubBeans() {
		return subBeans;
	}

	public void setSubBeans(SearchBeans subBeans) {
		this.subBeans = subBeans;
	}

}
