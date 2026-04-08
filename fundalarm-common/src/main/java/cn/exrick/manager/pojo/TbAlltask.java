package cn.exrick.manager.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class TbAlltask implements Serializable {
	private Integer id;

	@ApiModelProperty(value = "信息内容")
	private String content;

	@ApiModelProperty(value = "网址或小程序地址")
	private String url;

	@ApiModelProperty(value = "指令类型 0指令 1网址 2小程序")
	private Integer type;

	@ApiModelProperty(value = "机器人编号")
	private Integer robotid;

	private String scriptcode;

	@ApiModelProperty(value = "【状态： 可用0    不可用1    创建中3  解析完成2  已删除4 】")
	private Integer status;

	private String feekbackServer;

	private String robotname;

	@ApiModelProperty("最近的执行记录编号")
	int tasklogid;

	@ApiModelProperty(value = "时间")
	private Date dt;

	@ApiModelProperty(value = "启动时")
	private Integer starthour;

	@ApiModelProperty(value = "启动分  15的倍数")
	private Integer startminute;

	@ApiModelProperty(value = "执行次数")
	private Integer execucount;

	private Date updatetime;

	private Integer successtag;

	@ApiModelProperty(value = "ispublic")
	private String ispublic;

	private Integer paytag;

	private String feekbackRobot;

	private String title;

	private Date futuredate;

	@ApiModelProperty(value = "拒绝原因")
	private String reason;

	@ApiModelProperty(value = "价格")
	private BigDecimal price;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public Integer getRobotid() {
		return robotid;
	}

	public void setRobotid(Integer robotid) {
		this.robotid = robotid;
	}

	public String getScriptcode() {
		return scriptcode;
	}

	public void setScriptcode(String scriptcode) {
		this.scriptcode = scriptcode == null ? null : scriptcode.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFeekbackServer() {
		return feekbackServer;
	}

	public void setFeekbackServer(String feekbackServer) {
		this.feekbackServer = feekbackServer == null ? null : feekbackServer.trim();
	}

	public String getRobotname() {
		return robotname;
	}

	public void setRobotname(String robotname) {
		this.robotname = robotname == null ? null : robotname.trim();
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public Integer getStarthour() {
		return starthour;
	}

	public void setStarthour(Integer starthour) {
		this.starthour = starthour;
	}

	public Integer getStartminute() {
		return startminute;
	}

	public void setStartminute(Integer startminute) {
		this.startminute = startminute;
	}

	public Integer getExecucount() {
		return execucount;
	}

	public void setExecucount(Integer execucount) {
		this.execucount = execucount;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getSuccesstag() {
		return successtag;
	}

	public void setSuccesstag(Integer successtag) {
		this.successtag = successtag;
	}

	public String getIspublic() {
		return ispublic;
	}

	public void setIspublic(String ispublic) {
		this.ispublic = ispublic == null ? null : ispublic.trim();
	}

	public Integer getPaytag() {
		return paytag;
	}

	public void setPaytag(Integer paytag) {
		this.paytag = paytag;
	}

	public String getFeekbackRobot() {
		return feekbackRobot;
	}

	public void setFeekbackRobot(String feekbackRobot) {
		this.feekbackRobot = feekbackRobot == null ? null : feekbackRobot.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public Date getFuturedate() {
		return futuredate;
	}

	public void setFuturedate(Date futuredate) {
		this.futuredate = futuredate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getTasklogid() {
		return tasklogid;
	}

	public void setTasklogid(int tasklogid) {
		this.tasklogid = tasklogid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", content=").append(content);
		sb.append(", robotid=").append(robotid);
		sb.append(", scriptcode=").append(scriptcode);
		sb.append(", status=").append(status);
		sb.append(", feekbackServer=").append(feekbackServer);
		sb.append(", robotname=").append(robotname);
		sb.append(", dt=").append(dt);
		sb.append(", starthour=").append(starthour);
		sb.append(", startminute=").append(startminute);
		sb.append(", execucount=").append(execucount);
		sb.append(", updatetime=").append(updatetime);
		sb.append(", successtag=").append(successtag);
		sb.append(", ispublic=").append(ispublic);
		sb.append(", paytag=").append(paytag);
		sb.append(", feekbackRobot=").append(feekbackRobot);
		sb.append(", title=").append(title);
		sb.append(", futuredate=").append(futuredate);
		sb.append(", reason=").append(reason);
		sb.append(", price=").append(price);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}

}