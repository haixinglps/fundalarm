package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FundFangdichan1Ok implements Serializable {
    private Integer id;

    private String code;

    private String name;

    private String cate;

    private BigDecimal currentprice;

    private Integer level;

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

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate == null ? null : cate.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", cate=").append(cate);
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
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}