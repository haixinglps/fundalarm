package cn.exrick.manager.dto.front;

import io.swagger.annotations.ApiModelProperty;

public class DouyinComment implements java.io.Serializable {

	@ApiModelProperty("第一条评论")
	String firstMessage;

	@ApiModelProperty("第二条评论")
	String secondMessage;

	@ApiModelProperty("第三条评论")
	String thirdMessage;

	public String getFirstMessage() {
		return firstMessage;
	}

	public void setFirstMessage(String firstMessage) {
		this.firstMessage = firstMessage;
	}

	public String getSecondMessage() {
		return secondMessage;
	}

	public void setSecondMessage(String secondMessage) {
		this.secondMessage = secondMessage;
	}

	public String getThirdMessage() {
		return thirdMessage;
	}

	public void setThirdMessage(String thirdMessage) {
		this.thirdMessage = thirdMessage;
	}

}
