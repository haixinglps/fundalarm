package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TbPolicys implements java.io.Serializable{
    private Integer id;

    private Long customerid;
    private BigDecimal fapiaopayment;
    private BigDecimal availablefapiaopayment;
    private BigDecimal haveusefapiaopayment;
    private Date begintime;
    String begintimeori;
    String endtimeori;
    String productname;
    public BigDecimal getFapiaopayment() {
		return fapiaopayment;
	}

	public void setFapiaopayment(BigDecimal fapiaopayment) {
		this.fapiaopayment = fapiaopayment;
	}

	public BigDecimal getAvailablefapiaopayment() {
		return availablefapiaopayment;
	}

	public void setAvailablefapiaopayment(BigDecimal availablefapiaopayment) {
		this.availablefapiaopayment = availablefapiaopayment;
	}

	public BigDecimal getHaveusefapiaopayment() {
		return haveusefapiaopayment;
	}

	public void setHaveusefapiaopayment(BigDecimal haveusefapiaopayment) {
		this.haveusefapiaopayment = haveusefapiaopayment;
	}

	String policycompany;
    String member;
    int fapiaotag;
    String pdfurl;
    int outdays;
    Long sourceid;
    int temptag;
    public void setCustomertype(Integer customertype) {
		this.customertype = customertype;
	}

	int version;
     String customername;
     Integer customertype;
    public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public Integer getCustomertype() {
		return customertype;
	}

	public void setCustomertype(int customertype) {
		this.customertype = customertype;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getTemptag() {
		return temptag;
	}

	public void setTemptag(int temptag) {
		this.temptag = temptag;
	}

	public Long getSourceid() {
		return sourceid;
	}

	public void setSourceid(Long sourceid) {
		this.sourceid = sourceid;
	}

	public int getOutdays() {
		return outdays;
	}

	public void setOutdays(int outdays) {
		this.outdays = outdays;
	}

	public String getPdfurl() {
		return pdfurl;
	}

	public void setPdfurl(String pdfurl) {
		this.pdfurl = pdfurl;
	}

	public int getFapiaotag() {
		return fapiaotag;
	}

	public void setFapiaotag(int fapiaotag) {
		this.fapiaotag = fapiaotag;
	}

	List<String> itemids;
    
    public String getPolicycompany() {
		return policycompany;
	}

	public void setPolicycompany(String policycompany) {
		this.policycompany = policycompany;
	}

	public List<String> getItemids() {
		return itemids;
	}

	public void setItemids(List<String> itemids) {
		this.itemids = itemids;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getBegintimeori() {
		return begintimeori;
	}

	public void setBegintimeori(String begintimeori) {
		this.begintimeori = begintimeori;
	}

	public String getEndtimeori() {
		return endtimeori;
	}

	public void setEndtimeori(String endtimeori) {
		this.endtimeori = endtimeori;
	}

	private Date endtime;
TbCustomer customer;
    public TbCustomer getCustomer() {
	return customer;
}

public void setCustomer(TbCustomer customer) {
	this.customer = customer;
}

	private Integer bzqx;

    private BigDecimal payment;

    private Integer peoplecount;

    private Integer paymenttype;

    private Integer havesendmessage;

    private Long userid;

    private Long majororganid;

    private Long organid;

    private Integer status;

    private Integer waitcounts;

    private Date dt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getBzqx() {
        return bzqx;
    }

    public void setBzqx(Integer bzqx) {
        this.bzqx = bzqx;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPeoplecount() {
        return peoplecount;
    }

    public void setPeoplecount(Integer peoplecount) {
        this.peoplecount = peoplecount;
    }

    public Integer getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(Integer paymenttype) {
        this.paymenttype = paymenttype;
    }

    public Integer getHavesendmessage() {
        return havesendmessage;
    }

    public void setHavesendmessage(Integer havesendmessage) {
        this.havesendmessage = havesendmessage;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWaitcounts() {
        return waitcounts;
    }

    public void setWaitcounts(Integer waitcounts) {
        this.waitcounts = waitcounts;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
}