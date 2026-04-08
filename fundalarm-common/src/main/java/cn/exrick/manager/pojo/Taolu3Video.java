package cn.exrick.manager.pojo;

import java.io.Serializable;

public class Taolu3Video implements Serializable {
	private Integer id;

	private String title;

	private String urlkey2;

	private String dt;

	private String url;

	private Integer vid;

	private String cover;

	int uptag3;

	private String tria;

	private Integer type;

	private Integer author;

	private String yunpankey;

	private Integer uptag;

	private Integer uptag13681594428;

	private Integer goodtag;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public Integer getVid() {
		return vid;
	}

	public void setVid(Integer vid) {
		this.vid = vid;
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

	public Integer getGoodtag() {
		return goodtag;
	}

	public void setGoodtag(Integer goodtag) {
		this.goodtag = goodtag;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getUptag3() {
		return uptag3;
	}

	public void setUptag3(int uptag3) {
		this.uptag3 = uptag3;
	}

	public String getUrlkey2() {
		return urlkey2;
	}

	public void setUrlkey2(String urlkey2) {
		this.urlkey2 = urlkey2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", title=").append(title);
		sb.append(", url=").append(url);
		sb.append(", vid=").append(vid);
		sb.append(", cover=").append(cover);
		sb.append(", tria=").append(tria);
		sb.append(", type=").append(type);
		sb.append(", author=").append(author);
		sb.append(", yunpankey=").append(yunpankey);
		sb.append(", uptag=").append(uptag);
		sb.append(", uptag13681594428=").append(uptag13681594428);
		sb.append(", goodtag=").append(goodtag);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}