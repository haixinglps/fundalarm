package cn.exrick.manager.pojo;

import java.io.Serializable;
import java.util.Date;

public class TbUser implements Serializable {
	private Long id;

	private String username;

	private String password;

	private String phone;

	private String email;

	private Date created;

	private Date updated;
	private long organid;
	private long creatorid;
	int infotag;
	int autolisten;

	public int getAutolisten() {
		return autolisten;
	}

	public void setAutolisten(int autolisten) {
		this.autolisten = autolisten;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	int onlinetag;
	String oldpassword;

	public int getOnlinetag() {
		return onlinetag;
	}

	public void setOnlinetag(int onlinetag) {
		this.onlinetag = onlinetag;
	}

	public int getInfotag() {
		return infotag;
	}

	public void setInfotag(int infotag) {
		this.infotag = infotag;
	}

	public long getCreatorid() {
		return creatorid;
	}

	public void setCreatorid(long creatorid) {
		this.creatorid = creatorid;
	}

	public long getOrganid() {
		return organid;
	}

	public void setOrganid(long organid) {
		this.organid = organid;
	}

	public String getOrganname() {
		return organname;
	}

	public void setOrganname(String organname) {
		this.organname = organname;
	}

	public String getGateid() {
		return gateid;
	}

	public void setGateid(String gateid) {
		this.gateid = gateid;
	}

	private String sex;

	private String address;

	private Integer state;

	private String description;

	private String organname;

	private String gateid;

	private Integer roleId;

	private String file;

	private String roleNames;

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file == null ? null : file.trim();
	}
}