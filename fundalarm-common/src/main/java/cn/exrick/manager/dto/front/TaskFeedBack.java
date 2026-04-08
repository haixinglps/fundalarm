package cn.exrick.manager.dto.front;

import io.swagger.annotations.ApiModelProperty;

public class TaskFeedBack implements java.io.Serializable {

	@ApiModelProperty("指令编号")
	Integer id;

	@ApiModelProperty("服务端反馈")
	String feedbackServer;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFeedbackServer() {
		return feedbackServer;
	}

	public void setFeedbackServer(String feedbackServer) {
		this.feedbackServer = feedbackServer;
	}

}
