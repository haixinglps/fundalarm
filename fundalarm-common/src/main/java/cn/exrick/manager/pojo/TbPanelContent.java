package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TbPanelContent implements Serializable {
    private Integer id;

    @ApiModelProperty(value = "所属板块id")
    private Integer panelId;

    @ApiModelProperty(value = "类型 0关联商品 1其他链接")
    private Integer type;

    @ApiModelProperty(value = "关联商品id")
    private Long productId;

    private Integer sortOrder;

    @ApiModelProperty(value = "其他链接")
    private String fullUrl;

    private String picUrl;

    @ApiModelProperty(value = "3d轮播图备用")
    private String picUrl2;

    @ApiModelProperty(value = "3d轮播图备用")
    private String picUrl3;

    private Date created;

    private Date updated;

    @ApiModelProperty(value = "购买数")
    private BigDecimal clickingRate;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal nowprice;

    @ApiModelProperty(value = "商品价格波动")
    private BigDecimal priceFluctuation;

    @ApiModelProperty(value = "成交量")
    private Integer volume;

    @ApiModelProperty(value = "商户编号")
    private Integer circulation;

    private String indextext;

    private BigDecimal marketprice;

    private BigDecimal recommonemark;

    @ApiModelProperty(value = "2报废 1正常")
    private Integer status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPanelId() {
        return panelId;
    }

    public void setPanelId(Integer panelId) {
        this.panelId = panelId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl == null ? null : fullUrl.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getPicUrl2() {
        return picUrl2;
    }

    public void setPicUrl2(String picUrl2) {
        this.picUrl2 = picUrl2 == null ? null : picUrl2.trim();
    }

    public String getPicUrl3() {
        return picUrl3;
    }

    public void setPicUrl3(String picUrl3) {
        this.picUrl3 = picUrl3 == null ? null : picUrl3.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public BigDecimal getClickingRate() {
        return clickingRate;
    }

    public void setClickingRate(BigDecimal clickingRate) {
        this.clickingRate = clickingRate;
    }

    public BigDecimal getNowprice() {
        return nowprice;
    }

    public void setNowprice(BigDecimal nowprice) {
        this.nowprice = nowprice;
    }

    public BigDecimal getPriceFluctuation() {
        return priceFluctuation;
    }

    public void setPriceFluctuation(BigDecimal priceFluctuation) {
        this.priceFluctuation = priceFluctuation;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getCirculation() {
        return circulation;
    }

    public void setCirculation(Integer circulation) {
        this.circulation = circulation;
    }

    public String getIndextext() {
        return indextext;
    }

    public void setIndextext(String indextext) {
        this.indextext = indextext == null ? null : indextext.trim();
    }

    public BigDecimal getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(BigDecimal marketprice) {
        this.marketprice = marketprice;
    }

    public BigDecimal getRecommonemark() {
        return recommonemark;
    }

    public void setRecommonemark(BigDecimal recommonemark) {
        this.recommonemark = recommonemark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", panelId=").append(panelId);
        sb.append(", type=").append(type);
        sb.append(", productId=").append(productId);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append(", fullUrl=").append(fullUrl);
        sb.append(", picUrl=").append(picUrl);
        sb.append(", picUrl2=").append(picUrl2);
        sb.append(", picUrl3=").append(picUrl3);
        sb.append(", created=").append(created);
        sb.append(", updated=").append(updated);
        sb.append(", clickingRate=").append(clickingRate);
        sb.append(", nowprice=").append(nowprice);
        sb.append(", priceFluctuation=").append(priceFluctuation);
        sb.append(", volume=").append(volume);
        sb.append(", circulation=").append(circulation);
        sb.append(", indextext=").append(indextext);
        sb.append(", marketprice=").append(marketprice);
        sb.append(", recommonemark=").append(recommonemark);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}