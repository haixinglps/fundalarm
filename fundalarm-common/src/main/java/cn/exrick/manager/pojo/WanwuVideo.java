package cn.exrick.manager.pojo;

import java.io.Serializable;

public class WanwuVideo implements Serializable {
	private Integer vid;

	private String title;

	private Integer price;

	private String addtime;

	private String url;

	private String urlkey2;

	private String duration;

	private String cover;

	int uptag3;

	private String tria;

	private Integer type;

	private Integer author;

	private String yunpankey;

	private Integer uptag;

	private Integer uptag13681594428;

	private String urlid;

	private Integer sales;

	private Integer goodtag;

	private static final long serialVersionUID = 1L;

	public Integer getVid() {
		return vid;
	}

	public void setVid(Integer vid) {
		this.vid = vid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime == null ? null : addtime.trim();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover == null ? null : cover.trim();
	}

	public String getTria() {
		return tria;
	}

	public void setTria(String tria) {
		this.tria = tria == null ? null : tria.trim();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAuthor() {
		return author;
	}

	public void setAuthor(Integer author) {
		this.author = author;
	}

	public String getYunpankey() {
		return yunpankey;
	}

	public void setYunpankey(String yunpankey) {
		this.yunpankey = yunpankey == null ? null : yunpankey.trim();
	}

	public Integer getUptag() {
		return uptag;
	}

	public void setUptag(Integer uptag) {
		this.uptag = uptag;
	}

	public Integer getUptag13681594428() {
		return uptag13681594428;
	}

	public void setUptag13681594428(Integer uptag13681594428) {
		this.uptag13681594428 = uptag13681594428;
	}

	public String getUrlid() {
		return urlid;
	}

	public void setUrlid(String urlid) {
		this.urlid = urlid == null ? null : urlid.trim();
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
	}

	public Integer getGoodtag() {
		return goodtag;
	}

	public void setGoodtag(Integer goodtag) {
		this.goodtag = goodtag;
	}

	public int getUptag3() {
		return uptag3;
	}

	public void setUptag3(int uptag3) {
		this.uptag3 = uptag3;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUrlkey2() {
		return urlkey2;
	}

	public void setUrlkey2(String urlkey2) {
		this.urlkey2 = urlkey2;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", vid=").append(vid);
		sb.append(", title=").append(title);
		sb.append(", price=").append(price);
		sb.append(", addtime=").append(addtime);
		sb.append(", url=").append(url);
		sb.append(", cover=").append(cover);
		sb.append(", tria=").append(tria);
		sb.append(", type=").append(type);
		sb.append(", author=").append(author);
		sb.append(", yunpankey=").append(yunpankey);
		sb.append(", uptag=").append(uptag);
		sb.append(", uptag13681594428=").append(uptag13681594428);
		sb.append(", urlid=").append(urlid);
		sb.append(", sales=").append(sales);
		sb.append(", goodtag=").append(goodtag);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}