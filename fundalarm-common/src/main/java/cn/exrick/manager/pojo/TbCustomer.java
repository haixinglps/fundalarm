package cn.exrick.manager.pojo;

import java.util.Date;

public class TbCustomer implements java.io.Serializable {
    private Integer id;

    private String manname;

    private String phone;
    int customertag;

    public int getCustomertag() {
		return customertag;
	}

	public void setCustomertag(int customertag) {
		this.customertag = customertag;
	}

	private String systemnum;

    private String signpic;

    private String username;

    private String password;

    private String email;

    private Date created;

    private Date updated;

    private String sex;

    private Long orderid;

    private String creditcard;

    private String age;

    private String health;

    private String cardtype;

    private Integer updatetag;

    private Long userid;

    private String companyname;

    private String addr;

    private String addr2;

    private Integer type;

    private Integer status;

    private Integer buycount;

    private Integer recordcount;

    private Integer customertype;

    private Long sourceid;

    private String comment;

    private String addrcode;

    private Long companyid;

    private String hangye;

    private String guimo;

    private String zhizao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getManname() {
        return manname;
    }

    public void setManname(String manname) {
        this.manname = manname == null ? null : manname.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getSystemnum() {
        return systemnum;
    }

    public void setSystemnum(String systemnum) {
        this.systemnum = systemnum == null ? null : systemnum.trim();
    }

    public String getSignpic() {
        return signpic;
    }

    public void setSignpic(String signpic) {
        this.signpic = signpic == null ? null : signpic.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public String getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard == null ? null : creditcard.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health == null ? null : health.trim();
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype == null ? null : cardtype.trim();
    }

    public Integer getUpdatetag() {
        return updatetag;
    }

    public void setUpdatetag(Integer updatetag) {
        this.updatetag = updatetag;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname == null ? null : companyname.trim();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2 == null ? null : addr2.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBuycount() {
        return buycount;
    }

    public void setBuycount(Integer buycount) {
        this.buycount = buycount;
    }

    public Integer getRecordcount() {
        return recordcount;
    }

    public void setRecordcount(Integer recordcount) {
        this.recordcount = recordcount;
    }

    public Integer getCustomertype() {
        return customertype;
    }

    public void setCustomertype(Integer customertype) {
        this.customertype = customertype;
    }

    public Long getSourceid() {
        return sourceid;
    }

    public void setSourceid(Long sourceid) {
        this.sourceid = sourceid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getAddrcode() {
        return addrcode;
    }

    public void setAddrcode(String addrcode) {
        this.addrcode = addrcode == null ? null : addrcode.trim();
    }

    public Long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }

    public String getHangye() {
        return hangye;
    }

    public void setHangye(String hangye) {
        this.hangye = hangye == null ? null : hangye.trim();
    }

    public String getGuimo() {
        return guimo;
    }

    public void setGuimo(String guimo) {
        this.guimo = guimo == null ? null : guimo.trim();
    }

    public String getZhizao() {
        return zhizao;
    }

    public void setZhizao(String zhizao) {
        this.zhizao = zhizao == null ? null : zhizao.trim();
    }
}