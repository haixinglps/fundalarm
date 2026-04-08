package cn.exrick.manager.dto.front;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class TaskFrontDto implements java.io.Serializable {

	@ApiModelProperty("当前指令模板 ，为空代表不修改此字段")
	String scriptCode;

	@ApiModelProperty("指令编号，列表中的id属性,不能为空")
	Integer id;

	@ApiModelProperty("【指令状态： 可用0    不可用1    创建中3  解析完成2  已删除4  ，为空代表不修改此字段】")
	Integer status;

	@ApiModelProperty("拒绝原因 ，为空代表不修改此字段")
	String reason;
	@ApiModelProperty("价格 ，为空代表不修改此字段")
	BigDecimal price;

	@ApiModelProperty("支付状态 0未支付 1已支付 ，为空代表不修改此字段")
	Integer paytag;

	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getPaytag() {
		return paytag;
	}

	public void setPaytag(Integer paytag) {
		this.paytag = paytag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
