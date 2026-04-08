package cn.exrick.manager.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Fund implements Serializable {
	private Integer id;

	private String name;

	private String comments;

	private String qushi;

	private String sinacode;

	private String code;

	private BigDecimal zhangdiefu;

	private BigDecimal zhenfureal;
	private BigDecimal diefureal;

	private BigDecimal bodonglv;
	private BigDecimal bodonglv2;
	private BigDecimal bodonglv3;

	private BigDecimal shouyilv;
	private BigDecimal huiche;

	private BigDecimal zhenfurealyear;
	private BigDecimal diefurealyear;

	private BigDecimal bodonglvyear;
	private BigDecimal bodonglv2year;
	private BigDecimal bodonglv3year;

	private BigDecimal shouyilvyear;
	private BigDecimal huicheyear;

	private String cate;

	private Integer ischangwai;

	private Integer bodongshu;

	private Date created;

	private BigDecimal wendu;

	private String plantable;

	private Integer cansell;
	
	private BigDecimal money;

	private static final long serialVersionUID = 1L;
	
	
	
	

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getZhenfureal() {
		return zhenfureal;
	}

	public void setZhenfureal(BigDecimal zhenfureal) {
		this.zhenfureal = zhenfureal;
	}

	public BigDecimal getDiefureal() {
		return diefureal;
	}

	public BigDecimal getZhenfurealyear() {
		return zhenfurealyear;
	}

	public void setZhenfurealyear(BigDecimal zhenfurealyear) {
		this.zhenfurealyear = zhenfurealyear;
	}

	public BigDecimal getDiefurealyear() {
		return diefurealyear;
	}

	public void setDiefurealyear(BigDecimal diefurealyear) {
		this.diefurealyear = diefurealyear;
	}

	public BigDecimal getBodonglvyear() {
		return bodonglvyear;
	}

	public void setBodonglvyear(BigDecimal bodonglvyear) {
		this.bodonglvyear = bodonglvyear;
	}

	public BigDecimal getBodonglv2year() {
		return bodonglv2year;
	}

	public void setBodonglv2year(BigDecimal bodonglv2year) {
		this.bodonglv2year = bodonglv2year;
	}

	public BigDecimal getBodonglv3year() {
		return bodonglv3year;
	}

	public void setBodonglv3year(BigDecimal bodonglv3year) {
		this.bodonglv3year = bodonglv3year;
	}

	public BigDecimal getShouyilvyear() {
		return shouyilvyear;
	}

	public void setShouyilvyear(BigDecimal shouyilvyear) {
		this.shouyilvyear = shouyilvyear;
	}

	public BigDecimal getHuicheyear() {
		return huicheyear;
	}

	public void setHuicheyear(BigDecimal huicheyear) {
		this.huicheyear = huicheyear;
	}

	public void setDiefureal(BigDecimal diefureal) {
		this.diefureal = diefureal;
	}

	public BigDecimal getBodonglv() {
		return bodonglv;
	}

	public void setBodonglv(BigDecimal bodonglv) {
		this.bodonglv = bodonglv;
	}

	public BigDecimal getBodonglv2() {
		return bodonglv2;
	}

	public void setBodonglv2(BigDecimal bodonglv2) {
		this.bodonglv2 = bodonglv2;
	}

	public BigDecimal getBodonglv3() {
		return bodonglv3;
	}

	public void setBodonglv3(BigDecimal bodonglv3) {
		this.bodonglv3 = bodonglv3;
	}

	public BigDecimal getShouyilv() {
		return shouyilv;
	}

	public void setShouyilv(BigDecimal shouyilv) {
		this.shouyilv = shouyilv;
	}

	public BigDecimal getHuiche() {
		return huiche;
	}

	public void setHuiche(BigDecimal huiche) {
		this.huiche = huiche;
	}

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate == null ? null : cate.trim();
	}

	public Integer getIschangwai() {
		return ischangwai;
	}

	public void setIschangwai(Integer ischangwai) {
		this.ischangwai = ischangwai;
	}

	public Integer getBodongshu() {
		return bodongshu;
	}

	public void setBodongshu(Integer bodongshu) {
		this.bodongshu = bodongshu;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getWendu() {
		return wendu;
	}

	public BigDecimal getZhangdiefu() {
		return zhangdiefu;
	}

	public void setZhangdiefu(BigDecimal zhangdiefu) {
		this.zhangdiefu = zhangdiefu;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setWendu(BigDecimal wendu) {
		this.wendu = wendu;
	}

	public String getPlantable() {
		return plantable;
	}

	public void setPlantable(String plantable) {
		this.plantable = plantable == null ? null : plantable.trim();
	}

	public Integer getCansell() {
		return cansell;
	}

	public void setCansell(Integer cansell) {
		this.cansell = cansell;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getQushi() {
		return qushi;
	}

	public void setQushi(String qushi) {
		this.qushi = qushi;
	}

	public String getSinacode() {
		return sinacode;
	}

	public void setSinacode(String sinacode) {
		this.sinacode = sinacode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", name=").append(name);
		sb.append(", code=").append(code);
		sb.append(", cate=").append(cate);
		sb.append(", ischangwai=").append(ischangwai);
		sb.append(", bodongshu=").append(bodongshu);
		sb.append(", created=").append(created);
		sb.append(", wendu=").append(wendu);
		sb.append(", plantable=").append(plantable);
		sb.append(", cansell=").append(cansell);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}