package cn.exrick.manager.pojo;

import java.math.BigDecimal;

public class TbUsersAccountChangeTicheng implements java.io.Serializable {
    private Long id;

    private Long userId;

    private BigDecimal income;

    private BigDecimal expend;

    private Long tday;

    private BigDecimal yuebefore;

    private BigDecimal yuealter;

    private Integer type;

    private String orderid;

    private Integer rechargeid;

    private Integer model;

    private String comment;

    private Integer yuedirection;

    private String description;

    private BigDecimal dongjieafter;

    private BigDecimal dongjiebefore;

    private Integer dongjiedirection;

    private Long majororganid;

    private Long organid;

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

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpend() {
        return expend;
    }

    public void setExpend(BigDecimal expend) {
        this.expend = expend;
    }

    public Long getTday() {
        return tday;
    }

    public void setTday(Long tday) {
        this.tday = tday;
    }

    public BigDecimal getYuebefore() {
        return yuebefore;
    }

    public void setYuebefore(BigDecimal yuebefore) {
        this.yuebefore = yuebefore;
    }

    public BigDecimal getYuealter() {
        return yuealter;
    }

    public void setYuealter(BigDecimal yuealter) {
        this.yuealter = yuealter;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Integer getRechargeid() {
        return rechargeid;
    }

    public void setRechargeid(Integer rechargeid) {
        this.rechargeid = rechargeid;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getYuedirection() {
        return yuedirection;
    }

    public void setYuedirection(Integer yuedirection) {
        this.yuedirection = yuedirection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public BigDecimal getDongjieafter() {
        return dongjieafter;
    }

    public void setDongjieafter(BigDecimal dongjieafter) {
        this.dongjieafter = dongjieafter;
    }

    public BigDecimal getDongjiebefore() {
        return dongjiebefore;
    }

    public void setDongjiebefore(BigDecimal dongjiebefore) {
        this.dongjiebefore = dongjiebefore;
    }

    public Integer getDongjiedirection() {
        return dongjiedirection;
    }

    public void setDongjiedirection(Integer dongjiedirection) {
        this.dongjiedirection = dongjiedirection;
    }

    public Long getMajororganid() {
        return majororganid;
    }

    public void setMajororganid(Long majororganid) {
        this.majororganid = majororganid;
    }

    public Long getOrganid() {
        return organid;
    }

    public void setOrganid(Long organid) {
        this.organid = organid;
    }
}