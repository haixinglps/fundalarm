package cn.exrick.manager.pojo;

public class TbOrganization implements java.io.Serializable {
    private Long id;

    private String organname;

    private Integer parentorganid;

    private Integer status;

    private String policyid;
    String policyidyw;

    private String merchantid;

    private Integer agree1;

    public String getPolicyidyw() {
		return policyidyw;
	}

	public void setPolicyidyw(String policyidyw) {
		this.policyidyw = policyidyw;
	}

	private Integer agree2;

    private String title;

    private String code;

    private String address;
    String card;
    String username;
    String bank;

    public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname == null ? null : organname.trim();
    }

    public Integer getParentorganid() {
        return parentorganid;
    }

    public void setParentorganid(Integer parentorganid) {
        this.parentorganid = parentorganid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPolicyid() {
        return policyid;
    }

    public void setPolicyid(String policyid) {
        this.policyid = policyid == null ? null : policyid.trim();
    }

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid == null ? null : merchantid.trim();
    }

    public Integer getAgree1() {
        return agree1;
    }

    public void setAgree1(Integer agree1) {
        this.agree1 = agree1;
    }

    public Integer getAgree2() {
        return agree2;
    }

    public void setAgree2(Integer agree2) {
        this.agree2 = agree2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}