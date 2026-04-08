package cn.exrick.manager.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class Chatgpt implements java.io.Serializable {

	@ApiModelProperty("状态 0运行中 1已结束[成功]  2数据异常")
	int status;

	@ApiModelProperty("结果下载地址")
	String resulturl;

	@ApiModelProperty("创建日期")
	Date created;
	@ApiModelProperty("完成日期")
	Date finished;

	@ApiModelProperty("模板文件名称")
	String templateFileName;
	@ApiModelProperty("数据文件名称")
	String dataFileName;

	@ApiModelProperty("轮次")
	int cycleCount;

	public int getCycleCount() {
		return cycleCount;
	}

	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getResulturl() {
		return resulturl;
	}

	public void setResulturl(String resulturl) {
		this.resulturl = resulturl;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

}
