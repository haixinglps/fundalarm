package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class TbRechargeManage implements java.io.Serializable {
	private Long id;

	private Long userId;

	private Double rechargeNum;

	private Integer rechargeType;

	private String rechargeAccount;

	private Integer status;

	private BigDecimal balance;

	private Integer operator;

	private String description;

	private Date rechargeTime;

	private Integer operationType;

	private Date operationTime;

	private String outTradeNo;

	private Long organid;

	public TbOrganBankCard getBankCard() {
		return bankCard;
	}

	public void setBankCard(TbOrganBankCard bankCard) {
		this.bankCard = bankCard;
	}

	public TbOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(TbOrganization organization) {
		this.organization = organization;
	}

	private Long majororganid;
	TbOrganBankCard bankCard;
	TbOrganization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getRechargeNum() {
		return rechargeNum;
	}

	public void setRechargeNum(Double rechargeNum) {
		this.rechargeNum = rechargeNum;
	}

	public Integer getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(Integer rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getRechargeAccount() {
		return rechargeAccount;
	}

	public void setRechargeAccount(String rechargeAccount) {
		this.rechargeAccount = rechargeAccount == null ? null : rechargeAccount
				.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public Date getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Date rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
	}

	public Long getOrganid() {
		return organid;
	}

	public void setOrganid(Long organid) {
		this.organid = organid;
	}

	public Long getMajororganid() {
		return majororganid;
	}

	public void setMajororganid(Long majororganid) {
		this.majororganid = majororganid;
	}
}