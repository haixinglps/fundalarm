package cn.exrick.manager.pojo;

import java.util.Date;

public class TbMember  implements java.io.Serializable{
    private Long id;

    private String username;
    
    Long policymemberid;

    public Long getPolicymemberid() {
		return policymemberid;
	}

	public void setPolicymemberid(Long policymemberid) {
		this.policymemberid = policymemberid;
	}

	private String password;

    private String phone;

    private String email;

    private Date created;

    private Date updated;

    private String sex;

    private Long orderid;

    private String creditcard;

    private Integer lzxiantag;

    private Integer ywxbj;

    private String age;

    private String health;

    private String cardtype;

    private Integer updatetag;

    private Long userid;

    private String comment;

    private Long parentid;

    private Integer relation;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Long getParentid() {
        return parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }
}