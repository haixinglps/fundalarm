package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class TbOrdercombine implements java.io.Serializable {
    private Long id;

    private String childids;

    private Date createtime;

    private Date paymenttime;

    private Integer status;

    private BigDecimal payment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChildids() {
        return childids;
    }

    public void setChildids(String childids) {
        this.childids = childids == null ? null : childids.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getPaymenttime() {
        return paymenttime;
    }

    public void setPaymenttime(Date paymenttime) {
        this.paymenttime = paymenttime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }
}