package cn.exrick.manager.pojo;

import java.io.Serializable;

public class Category implements Serializable {
	private Integer id;

	java.util.List<Category> children;
	java.util.List<Stock> stocks;

	private String name;

	private Integer parentId;

	private Byte level;

	private String ancestors;

	private String createTime;

	private String updateTime;

	private Integer subjectid;

	private String bizkey;

	private String firstletter;

	private String imgurl;

	private String importance;

	private String leadtimes;

	private String limitupcount;

	private String limituptimes;

	private String pctchg;

	private String remark;

	private String selectreason;

	private Integer sort;

	private Integer status;

	private String stockcount;

	private String createby;

	private String detail;

	private String parentname;

	private String type;

	private String updateby;

	private String reason;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Byte getLevel() {
		return level;
	}

	public void setLevel(Byte level) {
		this.level = level;
	}

	public String getAncestors() {
		return ancestors;
	}

	public void setAncestors(String ancestors) {
		this.ancestors = ancestors == null ? null : ancestors.trim();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime == null ? null : createTime.trim();
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime == null ? null : updateTime.trim();
	}

	public Integer getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(Integer subjectid) {
		this.subjectid = subjectid;
	}

	public String getBizkey() {
		return bizkey;
	}

	public void setBizkey(String bizkey) {
		this.bizkey = bizkey == null ? null : bizkey.trim();
	}

	public String getFirstletter() {
		return firstletter;
	}

	public void setFirstletter(String firstletter) {
		this.firstletter = firstletter == null ? null : firstletter.trim();
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl == null ? null : imgurl.trim();
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance == null ? null : importance.trim();
	}

	public String getLeadtimes() {
		return leadtimes;
	}

	public void setLeadtimes(String leadtimes) {
		this.leadtimes = leadtimes == null ? null : leadtimes.trim();
	}

	public String getLimitupcount() {
		return limitupcount;
	}

	public void setLimitupcount(String limitupcount) {
		this.limitupcount = limitupcount == null ? null : limitupcount.trim();
	}

	public String getLimituptimes() {
		return limituptimes;
	}

	public void setLimituptimes(String limituptimes) {
		this.limituptimes = limituptimes == null ? null : limituptimes.trim();
	}

	public String getPctchg() {
		return pctchg;
	}

	public void setPctchg(String pctchg) {
		this.pctchg = pctchg == null ? null : pctchg.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getSelectreason() {
		return selectreason;
	}

	public void setSelectreason(String selectreason) {
		this.selectreason = selectreason == null ? null : selectreason.trim();
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStockcount() {
		return stockcount;
	}

	public void setStockcount(String stockcount) {
		this.stockcount = stockcount == null ? null : stockcount.trim();
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby == null ? null : createby.trim();
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail == null ? null : detail.trim();
	}

	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname == null ? null : parentname.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby == null ? null : updateby.trim();
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}

	public java.util.List<Category> getChildren() {
		return children;
	}

	public void setChildren(java.util.List<Category> children) {
		this.children = children;
	}

	public java.util.List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(java.util.List<Stock> stocks) {
		this.stocks = stocks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", name=").append(name);
		sb.append(", parentId=").append(parentId);
		sb.append(", level=").append(level);
		sb.append(", ancestors=").append(ancestors);
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", subjectid=").append(subjectid);
		sb.append(", bizkey=").append(bizkey);
		sb.append(", firstletter=").append(firstletter);
		sb.append(", imgurl=").append(imgurl);
		sb.append(", importance=").append(importance);
		sb.append(", leadtimes=").append(leadtimes);
		sb.append(", limitupcount=").append(limitupcount);
		sb.append(", limituptimes=").append(limituptimes);
		sb.append(", pctchg=").append(pctchg);
		sb.append(", remark=").append(remark);
		sb.append(", selectreason=").append(selectreason);
		sb.append(", sort=").append(sort);
		sb.append(", status=").append(status);
		sb.append(", stockcount=").append(stockcount);
		sb.append(", createby=").append(createby);
		sb.append(", detail=").append(detail);
		sb.append(", parentname=").append(parentname);
		sb.append(", type=").append(type);
		sb.append(", updateby=").append(updateby);
		sb.append(", reason=").append(reason);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}