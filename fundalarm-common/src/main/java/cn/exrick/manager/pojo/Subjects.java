package cn.exrick.manager.pojo;

import java.io.Serializable;

public class Subjects implements Serializable {
	private Integer id;
	java.util.List<Subjects> children;
	java.util.List<Stock> stocks;

	private String ancestors;

	private String date;

	private String fullname;

	private String hassubsubject;

	private String level;

	private String name;

	private String parentid;

	private String pctchg;

	private String ranktimes;

	private String subjectid;

	private String type;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAncestors() {
		return ancestors;
	}

	public void setAncestors(String ancestors) {
		this.ancestors = ancestors == null ? null : ancestors.trim();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date == null ? null : date.trim();
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname == null ? null : fullname.trim();
	}

	public String getHassubsubject() {
		return hassubsubject;
	}

	public void setHassubsubject(String hassubsubject) {
		this.hassubsubject = hassubsubject == null ? null : hassubsubject.trim();
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level == null ? null : level.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid == null ? null : parentid.trim();
	}

	public String getPctchg() {
		return pctchg;
	}

	public void setPctchg(String pctchg) {
		this.pctchg = pctchg == null ? null : pctchg.trim();
	}

	public String getRanktimes() {
		return ranktimes;
	}

	public void setRanktimes(String ranktimes) {
		this.ranktimes = ranktimes == null ? null : ranktimes.trim();
	}

	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid == null ? null : subjectid.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public java.util.List<Subjects> getChildren() {
		return children;
	}

	public void setChildren(java.util.List<Subjects> children) {
		this.children = children;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public java.util.List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(java.util.List<Stock> stocks) {
		this.stocks = stocks;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", ancestors=").append(ancestors);
		sb.append(", date=").append(date);
		sb.append(", fullname=").append(fullname);
		sb.append(", hassubsubject=").append(hassubsubject);
		sb.append(", level=").append(level);
		sb.append(", name=").append(name);
		sb.append(", parentid=").append(parentid);
		sb.append(", pctchg=").append(pctchg);
		sb.append(", ranktimes=").append(ranktimes);
		sb.append(", subjectid=").append(subjectid);
		sb.append(", type=").append(type);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}