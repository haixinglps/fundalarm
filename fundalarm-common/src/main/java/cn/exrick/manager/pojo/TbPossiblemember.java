package cn.exrick.manager.pojo;

import java.util.Date;

public class TbPossiblemember implements java.io.Serializable {
    private Long id;

    private String username;

    private String email;

    public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private Date created;

    private String creditcard;

    private String age;
    String comment;
    String addrcode;

    public String getAddrcode() {
		return addrcode;
	}

	public void setAddrcode(String addrcode) {
		this.addrcode = addrcode;
	}

	private String health;

    private String cardtype;

    private String companyname;

    private String addr;

    private String addr2;

    private Integer buycount;

    private Integer customertype;

    private String password;

    private String phone;

    private Integer updatetag;

    private Date updated;

    private String sex;

    private Long orderid;

    private Integer lzxiantag;

    private Integer ywxbj;

    private Long userid;

    private Integer type;

    private Integer status;

    private Integer recordcount;

    private Integer owntag;

    private Integer version;

    private Long createrid;

    private String manname;

    private String manname2;

    private String phone2;

    private String work;

    private String work2;

    private String weixinhao;

    private String weixinhao2;

    private String hangye;

    private String guimo;

    private String zhizao;

    private String yewu;

    private String gzh;

    private String manname3;

    private String phone3;

    private String work3;

    private String weixinhao3;

    private String website;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
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

    public Integer getBuycount() {
        return buycount;
    }

    public void setBuycount(Integer buycount) {
        this.buycount = buycount;
    }

    public Integer getCustomertype() {
        return customertype;
    }

    public void setCustomertype(Integer customertype) {
        this.customertype = customertype;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getUpdatetag() {
        return updatetag;
    }

    public void setUpdatetag(Integer updatetag) {
        this.updatetag = updatetag;
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

    public Integer getLzxiantag() {
        return lzxiantag;
    }

    public void setLzxiantag(Integer lzxiantag) {
        this.lzxiantag = lzxiantag;
    }

    public Integer getYwxbj() {
        return ywxbj;
    }

    public void setYwxbj(Integer ywxbj) {
        this.ywxbj = ywxbj;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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

    public Integer getRecordcount() {
        return recordcount;
    }

    public void setRecordcount(Integer recordcount) {
        this.recordcount = recordcount;
    }

    public Integer getOwntag() {
        return owntag;
    }

    public void setOwntag(Integer owntag) {
        this.owntag = owntag;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getCreaterid() {
        return createrid;
    }

    public void setCreaterid(Long createrid) {
        this.createrid = createrid;
    }

    public String getManname() {
        return manname;
    }

    public void setManname(String manname) {
        this.manname = manname == null ? null : manname.trim();
    }

    public String getManname2() {
        return manname2;
    }

    public void setManname2(String manname2) {
        this.manname2 = manname2 == null ? null : manname2.trim();
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2 == null ? null : phone2.trim();
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work == null ? null : work.trim();
    }

    public String getWork2() {
        return work2;
    }

    public void setWork2(String work2) {
        this.work2 = work2 == null ? null : work2.trim();
    }

    public String getWeixinhao() {
        return weixinhao;
    }

    public void setWeixinhao(String weixinhao) {
        this.weixinhao = weixinhao == null ? null : weixinhao.trim();
    }

    public String getWeixinhao2() {
        return weixinhao2;
    }

    public void setWeixinhao2(String weixinhao2) {
        this.weixinhao2 = weixinhao2 == null ? null : weixinhao2.trim();
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

    public String getYewu() {
        return yewu;
    }

    public void setYewu(String yewu) {
        this.yewu = yewu == null ? null : yewu.trim();
    }

    public String getGzh() {
        return gzh;
    }

    public void setGzh(String gzh) {
        this.gzh = gzh == null ? null : gzh.trim();
    }

    public String getManname3() {
        return manname3;
    }

    public void setManname3(String manname3) {
        this.manname3 = manname3 == null ? null : manname3.trim();
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3 == null ? null : phone3.trim();
    }

    public String getWork3() {
        return work3;
    }

    public void setWork3(String work3) {
        this.work3 = work3 == null ? null : work3.trim();
    }

    public String getWeixinhao3() {
        return weixinhao3;
    }

    public void setWeixinhao3(String weixinhao3) {
        this.weixinhao3 = weixinhao3 == null ? null : weixinhao3.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }
}