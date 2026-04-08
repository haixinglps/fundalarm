package cn.exrick.manager.pojo;

import java.io.Serializable;

public class Waiwang2Video implements Serializable {
	private Integer id;

	private String title;

	private Integer vid;

	private String dt;

	private String type;

	private String channel;

	private String duration;

	private String author;

	private String url;

	private Integer goodtag;

	private String nickname;

	private String photo;

	private String cover;

	private String uid;
	private String reg;
	private String province;
	private String city;
	private String locationcity;
	private String bio;
	private String phone;
	String pantag;

	private String urkey2;

	private static final long serialVersionUID = 1L;

	public String getPantag() {
		return pantag;
	}

	public void setPantag(String pantag) {
		this.pantag = pantag;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocationcity() {
		return locationcity;
	}

	public void setLocationcity(String locationcity) {
		this.locationcity = locationcity;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname == null ? null : nickname.trim();
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo == null ? null : photo.trim();
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover == null ? null : cover.trim();
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
		sb.append(", nickname=").append(nickname);
		sb.append(", photo=").append(photo);
		sb.append(", cover=").append(cover);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}