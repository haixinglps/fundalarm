package cn.exrick.manager.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class TaskDto implements java.io.Serializable {

	@ApiModelProperty("指令标题")
	String title;

	@ApiModelProperty("指令编号")
	Integer id;

	@ApiModelProperty("指令描述")
	String content;
	@ApiModelProperty("创建时间")
	java.util.Date created;
	@ApiModelProperty("用户名")
	String robotName;
	@ApiModelProperty("用户编号")
	String robotId;
	@ApiModelProperty("当前指令模板")
	String scriptCode;
	@ApiModelProperty("指令模板简介")
	String scriptDesc;
	@ApiModelProperty("【指令状态： 可用0    不可用1    创建中3  解析完成2  已删除4 】")
	int status;
	@ApiModelProperty("最新反馈")
	String feekbackServer;
	@ApiModelProperty("拒绝原因")
	String reason;
	@ApiModelProperty("价格")
	BigDecimal price;

	@ApiModelProperty("支付状态 0未支付 1已支付")
	int paytag;

	public java.util.Date getCreated() {
		return created;
	}

	public void setCreated(java.util.Date created) {
		this.created = created;
	}

	public String getRobotName() {
		return robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public String getRobotId() {
		return robotId;
	}

	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}

	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	public String getScriptDesc() {
		return scriptDesc;
	}

	public void setScriptDesc(String scriptDesc) {
		this.scriptDesc = scriptDesc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFeekbackServer() {
		return feekbackServer;
	}

	public void setFeekbackServer(String feekbackServer) {
		this.feekbackServer = feekbackServer;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getPaytag() {
		return paytag;
	}

	public void setPaytag(int paytag) {
		this.paytag = paytag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
