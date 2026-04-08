package cn.exrick.manager.pojo;

import java.util.Date;

public class TbUserinfo implements java.io.Serializable {
    private Integer id;

    private String username;

    private String phone;

    private String card;

    private String addrcode;

    private String addr;

    private Date dt;

    private Long userid;

    public String getBanklist() {
		return banklist;
	}

	public void setBanklist(String banklist) {
		this.banklist = banklist;
	}

	private String addr2;

    private String comment;

    private String cardname;
    String banklist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card == null ? null : card.trim();
    }

    public String getAddrcode() {
        return addrcode;
    }

    public void setAddrcode(String addrcode) {
        this.addrcode = addrcode == null ? null : addrcode.trim();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2 == null ? null : addr2.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname == null ? null : cardname.trim();
    }
}