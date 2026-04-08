package cn.exrick.manager.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class Fund1Gaoduanzhuangbei2Ok implements Serializable {
	private Integer id;

	private String code;

	private String name;

	private String upcount;
	private int buytag;

	private BigDecimal currentprice;

	private Integer level;

	private int zhisun;

	@ApiModelProperty(value = "1是当前  0否")
	private Integer iscurrent;

	private BigDecimal buyprice;

	private BigDecimal buypriceReal;

	private Date dt;

	private BigDecimal sellprice;

	private BigDecimal sellpriceReal;

	private Date dtSell;

	private BigDecimal wangge;

	private BigDecimal wanggeprice;

	private String comment;

	private BigDecimal minprice;

	private BigDecimal minpricepercent;

	private BigDecimal fene;

	private BigDecimal money;

	private Integer successcount;

	private String cate;

	private String catedown;
	private BigDecimal minprice5;
	private BigDecimal maxprice5;
	private BigDecimal maxzhangfu5;
	private BigDecimal maxdiefu5;
	private BigDecimal maxpriceniu;
	private BigDecimal zhiying;
	private String firsttime; // 【双向持仓】存储OKX posId，用于指定平仓避免FIFO
	int yangbencount;

	public int getYangbencount() {
		return yangbencount;
	}

	public void setYangbencount(int yangbencount) {
		this.yangbencount = yangbencount;
	}
	
	public String getFirsttime() {
		return firsttime;
	}
	
	public void setFirsttime(String firsttime) {
		this.firsttime = firsttime;
	}

	public BigDecimal getMinprice5() {
		return minprice5;
	}

	public void setMinprice5(BigDecimal minprice5) {
		this.minprice5 = minprice5;
	}

	public BigDecimal getMaxprice5() {
		return maxprice5;
	}

	public void setMaxprice5(BigDecimal maxprice5) {
		this.maxprice5 = maxprice5;
	}

	public BigDecimal getMaxzhangfu5() {
		return maxzhangfu5;
	}

	public void setMaxzhangfu5(BigDecimal maxzhangfu5) {
		this.maxzhangfu5 = maxzhangfu5;
	}

	public BigDecimal getMaxdiefu5() {
		return maxdiefu5;
	}

	public void setMaxdiefu5(BigDecimal maxdiefu5) {
		this.maxdiefu5 = maxdiefu5;
	}

	public BigDecimal getMaxpriceniu() {
		return maxpriceniu;
	}

	public void setMaxpriceniu(BigDecimal maxpriceniu) {
		this.maxpriceniu = maxpriceniu;
	}

	public BigDecimal getZhiying() {
		return zhiying;
	}

	public void setZhiying(BigDecimal zhiying) {
		this.zhiying = zhiying;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public BigDecimal getCurrentprice() {
		return currentprice;
	}

	public void setCurrentprice(BigDecimal currentprice) {
		this.currentprice = currentprice;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIscurrent() {
		return iscurrent;
	}

	public void setIscurrent(Integer iscurrent) {
		this.iscurrent = iscurrent;
	}

	public BigDecimal getBuyprice() {
		return buyprice;
	}

	public void setBuyprice(BigDecimal buyprice) {
		this.buyprice = buyprice;
	}

	public BigDecimal getBuypriceReal() {
		return buypriceReal;
	}

	public void setBuypriceReal(BigDecimal buypriceReal) {
		this.buypriceReal = buypriceReal;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public BigDecimal getSellprice() {
		return sellprice;
	}

	public void setSellprice(BigDecimal sellprice) {
		this.sellprice = sellprice;
	}

	public BigDecimal getSellpriceReal() {
		return sellpriceReal;
	}

	public void setSellpriceReal(BigDecimal sellpriceReal) {
		this.sellpriceReal = sellpriceReal;
	}

	public Date getDtSell() {
		return dtSell;
	}

	public void setDtSell(Date dtSell) {
		this.dtSell = dtSell;
	}

	public BigDecimal getWangge() {
		return wangge;
	}

	public void setWangge(BigDecimal wangge) {
		this.wangge = wangge;
	}

	public BigDecimal getWanggeprice() {
		return wanggeprice;
	}

	public void setWanggeprice(BigDecimal wanggeprice) {
		this.wanggeprice = wanggeprice;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment == null ? null : comment.trim();
	}

	public BigDecimal getMinprice() {
		return minprice;
	}

	public void setMinprice(BigDecimal minprice) {
		this.minprice = minprice;
	}

	public BigDecimal getMinpricepercent() {
		return minpricepercent;
	}

	public void setMinpricepercent(BigDecimal minpricepercent) {
		this.minpricepercent = minpricepercent;
	}

	public BigDecimal getFene() {
		return fene;
	}

	public void setFene(BigDecimal fene) {
		this.fene = fene;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getSuccesscount() {
		return successcount;
	}

	public void setSuccesscount(Integer successcount) {
		this.successcount = successcount;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate == null ? null : cate.trim();
	}

	public String getCatedown() {
		return catedown;
	}

	public void setCatedown(String catedown) {
		this.catedown = catedown == null ? null : catedown.trim();
	}

	public int getZhisun() {
		return zhisun;
	}

	public void setZhisun(int zhisun) {
		this.zhisun = zhisun;
	}

	public String getUpcount() {
		return upcount;
	}

	public void setUpcount(String upcount) {
		this.upcount = upcount;
	}

	public int getBuytag() {
		return buytag;
	}

	public void setBuytag(int buytag) {
		this.buytag = buytag;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", code=").append(code);
		sb.append(", name=").append(name);
		sb.append(", currentprice=").append(currentprice);
		sb.append(", level=").append(level);
		sb.append(", iscurrent=").append(iscurrent);
		sb.append(", buyprice=").append(buyprice);
		sb.append(", buypriceReal=").append(buypriceReal);
		sb.append(", dt=").append(dt);
		sb.append(", sellprice=").append(sellprice);
		sb.append(", sellpriceReal=").append(sellpriceReal);
		sb.append(", dtSell=").append(dtSell);
		sb.append(", wangge=").append(wangge);
		sb.append(", wanggeprice=").append(wanggeprice);
		sb.append(", comment=").append(comment);
		sb.append(", minprice=").append(minprice);
		sb.append(", minpricepercent=").append(minpricepercent);
		sb.append(", fene=").append(fene);
		sb.append(", money=").append(money);
		sb.append(", successcount=").append(successcount);
		sb.append(", cate=").append(cate);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}