package cn.exrick.manager.dto.front;

import io.swagger.annotations.ApiModelProperty;

public class RobotComment implements java.io.Serializable {
	@ApiModelProperty("chat评论内容")
	String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
