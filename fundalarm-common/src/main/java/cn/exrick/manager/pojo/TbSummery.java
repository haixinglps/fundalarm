package cn.exrick.manager.pojo;

import java.math.BigDecimal;

public class TbSummery implements java.io.Serializable {
    private Integer id;

    private String begindate;

    private String enddate;

    private BigDecimal lzx;

    private BigDecimal ywx;

    private BigDecimal summery;

    private BigDecimal paysummery;

    private Integer status;

    private Long userid;

    private String username;

    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate == null ? null : begindate.trim();
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate == null ? null : enddate.trim();
    }

    public BigDecimal getLzx() {
        return lzx;
    }

    public void setLzx(BigDecimal lzx) {
        this.lzx = lzx;
    }

    public BigDecimal getYwx() {
        return ywx;
    }

    public void setYwx(BigDecimal ywx) {
        this.ywx = ywx;
    }

    public BigDecimal getSummery() {
        return summery;
    }

    public void setSummery(BigDecimal summery) {
        this.summery = summery;
    }

    public BigDecimal getPaysummery() {
        return paysummery;
    }

    public void setPaysummery(BigDecimal paysummery) {
        this.paysummery = paysummery;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}