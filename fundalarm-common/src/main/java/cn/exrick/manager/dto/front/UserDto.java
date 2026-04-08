package cn.exrick.manager.dto.front;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class UserDto implements java.io.Serializable {

	@ApiModelProperty("注册日期")
	Date created;
	@ApiModelProperty("最近登录日期")
	Date updated;
	@ApiModelProperty("用户名")
	String userName;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
