package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.tools.JavaCompiler;

public class TbOrderchildofyoubao implements java.io.Serializable{
    private Long id;
    String begintimestr;
    String endtimestr;
    int foroutertag;

    public int getForoutertag() {
		return foroutertag;
	}

	public void setForoutertag(int foroutertag) {
		this.foroutertag = foroutertag;
	}










	private BigDecimal payment;

    private BigDecimal lzxFee;

    private Integer status;

    private Date createTime;

    public String getBegintimestr() {
		return begintimestr;
	}

	public void setBegintimestr(String begintimestr) {
		this.begintimestr = begintimestr;
	}

	public String getEndtimestr() {
		return endtimestr;
	}

	public void setEndtimestr(String endtimestr) {
		this.endtimestr = endtimestr;
	}










	private Date updateTime;

    private Date paymentTime;

    private Date consignTime;

    private Date endTime;

    private String shippingCode;

    private Long userId;

    private String userName;

    private BigDecimal oripayment;

    private BigDecimal ywxFee;

    private Integer ywxtype;

    private Integer selftraveltag;

    private Integer specialservertag;

    private Integer lzxtype;

    private String locationtype;

    private String safetype;

    private String majororganid;

    private Integer lzxcount;

    private Integer ywxcount;

    private BigDecimal ywxper;

    private BigDecimal lzxper;

    private String route;

    private String orderid;

    private Integer isown;

    private String username;

    private String card;

    private String phone;

    private String dailiusername;

    private String dailicard;

    private String dailiphone;

    private String requireid;

    private String cardtype;

    private String sourceplatform;

    private String ordertype;

    private String sourcenumber;

    private String companytype;

    private String policytype;

    private Integer merchantpolicyid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getLzxFee() {
        return lzxFee;
    }

    public void setLzxFee(BigDecimal lzxFee) {
        this.lzxFee = lzxFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode == null ? null : shippingCode.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public BigDecimal getOripayment() {
        return oripayment;
    }

    public void setOripayment(BigDecimal oripayment) {
        this.oripayment = oripayment;
    }

    public BigDecimal getYwxFee() {
        return ywxFee;
    }

    public void setYwxFee(BigDecimal ywxFee) {
        this.ywxFee = ywxFee;
    }

    public Integer getYwxtype() {
        return ywxtype;
    }

    public void setYwxtype(Integer ywxtype) {
        this.ywxtype = ywxtype;
    }

    public Integer getSelftraveltag() {
        return selftraveltag;
    }

    public void setSelftraveltag(Integer selftraveltag) {
        this.selftraveltag = selftraveltag;
    }

    public Integer getSpecialservertag() {
        return specialservertag;
    }

    public void setSpecialservertag(Integer specialservertag) {
        this.specialservertag = specialservertag;
    }

    public Integer getLzxtype() {
        return lzxtype;
    }

    public void setLzxtype(Integer lzxtype) {
        this.lzxtype = lzxtype;
    }

    public String getLocationtype() {
        return locationtype;
    }

    public void setLocationtype(String locationtype) {
        this.locationtype = locationtype == null ? null : locationtype.trim();
    }

    public String getSafetype() {
        return safetype;
    }

    public void setSafetype(String safetype) {
        this.safetype = safetype == null ? null : safetype.trim();
    }

    public String getMajororganid() {
        return majororganid;
    }

    public void setMajororganid(String majororganid) {
        this.majororganid = majororganid == null ? null : majororganid.trim();
    }

    public Integer getLzxcount() {
        return lzxcount;
    }

    public void setLzxcount(Integer lzxcount) {
        this.lzxcount = lzxcount;
    }

    public Integer getYwxcount() {
        return ywxcount;
    }

    public void setYwxcount(Integer ywxcount) {
        this.ywxcount = ywxcount;
    }

    public BigDecimal getYwxper() {
        return ywxper;
    }

    public void setYwxper(BigDecimal ywxper) {
        this.ywxper = ywxper;
    }

    public BigDecimal getLzxper() {
        return lzxper;
    }

    public void setLzxper(BigDecimal lzxper) {
        this.lzxper = lzxper;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route == null ? null : route.trim();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Integer getIsown() {
        return isown;
    }

    public void setIsown(Integer isown) {
        this.isown = isown;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card == null ? null : card.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getDailiusername() {
        return dailiusername;
    }

    public void setDailiusername(String dailiusername) {
        this.dailiusername = dailiusername == null ? null : dailiusername.trim();
    }

    public String getDailicard() {
        return dailicard;
    }

    public void setDailicard(String dailicard) {
        this.dailicard = dailicard == null ? null : dailicard.trim();
    }

    public String getDailiphone() {
        return dailiphone;
    }

    public void setDailiphone(String dailiphone) {
        this.dailiphone = dailiphone == null ? null : dailiphone.trim();
    }

    public String getRequireid() {
        return requireid;
    }

    public void setRequireid(String requireid) {
        this.requireid = requireid == null ? null : requireid.trim();
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype == null ? null : cardtype.trim();
    }

    public String getSourceplatform() {
        return sourceplatform;
    }

    public void setSourceplatform(String sourceplatform) {
        this.sourceplatform = sourceplatform == null ? null : sourceplatform.trim();
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype == null ? null : ordertype.trim();
    }

    public String getSourcenumber() {
        return sourcenumber;
    }

    public void setSourcenumber(String sourcenumber) {
        this.sourcenumber = sourcenumber == null ? null : sourcenumber.trim();
    }

    public String getCompanytype() {
        return companytype;
    }

    public void setCompanytype(String companytype) {
        this.companytype = companytype == null ? null : companytype.trim();
    }

    public String getPolicytype() {
        return policytype;
    }

    public void setPolicytype(String policytype) {
        this.policytype = policytype == null ? null : policytype.trim();
    }

    public Integer getMerchantpolicyid() {
        return merchantpolicyid;
    }

    public void setMerchantpolicyid(Integer merchantpolicyid) {
        this.merchantpolicyid = merchantpolicyid;
    }
    
    
    
    
    
    
    
    
 
    
    String partnerCode;
    String productCode;
    String transferCode;
    String  transSerialNo;
    String ShopBasPolicyNo;
    String ShopAddress;
    String ShopManName;
    String ShopmobileTelephone;
    String ShopcertificateNo;
    public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTransferCode() {
		return transferCode;
	}

	public void setTransferCode(String transferCode) {
		this.transferCode = transferCode;
	}

	public String getTransSerialNo() {
		return transSerialNo;
	}

	public void setTransSerialNo(String transSerialNo) {
		this.transSerialNo = transSerialNo;
	}

	public String getShopBasPolicyNo() {
		return ShopBasPolicyNo;
	}

	public void setShopBasPolicyNo(String shopBasPolicyNo) {
		ShopBasPolicyNo = shopBasPolicyNo;
	}

	public String getShopAddress() {
		return ShopAddress;
	}

	public void setShopAddress(String shopAddress) {
		ShopAddress = shopAddress;
	}

	public String getShopManName() {
		return ShopManName;
	}

	public void setShopManName(String shopManName) {
		ShopManName = shopManName;
	}

	public String getShopmobileTelephone() {
		return ShopmobileTelephone;
	}

	public void setShopmobileTelephone(String shopmobileTelephone) {
		ShopmobileTelephone = shopmobileTelephone;
	}

	public String getShopcertificateNo() {
		return ShopcertificateNo;
	}

	public void setShopcertificateNo(String shopcertificateNo) {
		ShopcertificateNo = shopcertificateNo;
	}

	public String getShopSystemNo() {
		return ShopSystemNo;
	}

	public void setShopSystemNo(String shopSystemNo) {
		ShopSystemNo = shopSystemNo;
	}

	public String getInsuranceBeginDate() {
		return insuranceBeginDate;
	}

	public void setInsuranceBeginDate(String insuranceBeginDate) {
		this.insuranceBeginDate = insuranceBeginDate;
	}

	public String getInsuranceEndDate() {
		return insuranceEndDate;
	}

	public void setInsuranceEndDate(String insuranceEndDate) {
		this.insuranceEndDate = insuranceEndDate;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public String getActualPremium() {
		return ActualPremium;
	}

	public void setActualPremium(String actualPremium) {
		this.ActualPremium = actualPremium;
	}

	 






	String ShopSystemNo;
    String  insuranceBeginDate;
    String insuranceEndDate;
    String insuranceNumber;
   


 


	String  ActualPremium;
 





	List<TbMember> members;

	public List<TbMember> getMembers() {
		return members;
	}

	public void setMembers(List<TbMember> members) {
		this.members = members;
	}

    
    
    
    
    
    
    
    
    
    
    
    
    
}