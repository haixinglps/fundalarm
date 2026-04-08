package cn.exrick.manager.pojo;

import java.io.Serializable;

public class WaiwangVideo implements Serializable {
	private Integer id;

	private String title;

	private Integer vid;

	private String dt;

	private String type;

	private String channel;

	private String duration;

	private String author;

	int friendindex;

	private String url;

	private Integer goodtag;

	private static final long serialVersionUID = 1L;

	public int getFriendindex() {
		return friendindex;
	}

	public void setFriendindex(int friendindex) {
		this.friendindex = friendindex;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public Integer getVid() {
		return vid;
	}

	public void setVid(Integer vid) {
		this.vid = vid;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt == null ? null : dt.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel == null ? null : channel.trim();
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration == null ? null : duration.trim();
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author == null ? null : author.trim();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public Integer getGoodtag() {
		return goodtag;
	}

	public void setGoodtag(Integer goodtag) {
		this.goodtag = goodtag;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", title=").append(title);
		sb.append(", vid=").append(vid);
		sb.append(", dt=").append(dt);
		sb.append(", type=").append(type);
		sb.append(", channel=").append(channel);
		sb.append(", duration=").append(duration);
		sb.append(", author=").append(author);
		sb.append(", url=").append(url);
		sb.append(", goodtag=").append(goodtag);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}