package cn.exrick.manager.dto.front;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class CommentGptLog implements java.io.Serializable {

	@ApiModelProperty("作者")
	String title;

	@ApiModelProperty("简介")
	String desc;

	@ApiModelProperty("抖音评论")
	DouyinComment douyinComment;

	@ApiModelProperty("chatgpt评论")
	java.util.List<RobotComment> robotComment;

	@ApiModelProperty("生成时间")
	Date dt;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public DouyinComment getDouyinComment() {
		return douyinComment;
	}

	public void setDouyinComment(DouyinComment douyinComment) {
		this.douyinComment = douyinComment;
	}

	public java.util.List<RobotComment> getRobotComment() {
		return robotComment;
	}

	public void setRobotComment(java.util.List<RobotComment> robotComments) {
		this.robotComment = robotComments;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

}
