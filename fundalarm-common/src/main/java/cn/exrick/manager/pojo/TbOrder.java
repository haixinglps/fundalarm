package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class TbOrder implements java.io.Serializable {
	private Long id;

	private String orderId;

	private BigDecimal payment;

	private BigDecimal lzxFee;

	private Integer status;

	private Integer paymentType;
	private BigDecimal newpayment;
	String paymentusername;
	
	int policyid;
	int version;
	int temptag;
	
	public int getTemptag() {
		return temptag;
	}

	public void setTemptag(int temptag) {
		this.temptag = temptag;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getPolicyid() {
		return policyid;
	}

	public void setPolicyid(int policyid) {
		this.policyid = policyid;
	}

	int routeid;
	 
	public int getNeedsynctag() {
		return needsynctag;
	}

	public void setNeedsynctag(int needsynctag) {
		this.needsynctag = needsynctag;
	}

	int needsynctag;
	
	String provincehome;
	String cityhome;
	
	
	String routefrom;
	public String getProvincehome() {
		return provincehome;
	}

	public void setProvincehome(String provincehome) {
		this.provincehome = provincehome;
	}

	public String getCityhome() {
		return cityhome;
	}

	public void setCityhome(String cityhome) {
		this.cityhome = cityhome;
	}

	public String getRoutefrom() {
		return routefrom;
	}

	public void setRoutefrom(String routefrom) {
		this.routefrom = routefrom;
	}

	public String getRouteto() {
		return routeto;
	}

	public void setRouteto(String routeto) {
		this.routeto = routeto;
	}

	public String getTraveltotool() {
		return traveltotool;
	}

	public void setTraveltotool(String traveltotool) {
		this.traveltotool = traveltotool;
	}

	public String getTravelfromtool() {
		return travelfromtool;
	}

	public void setTravelfromtool(String travelfromtool) {
		this.travelfromtool = travelfromtool;
	}

	public String getTuancode() {
		return tuancode;
	}

	public void setTuancode(String tuancode) {
		this.tuancode = tuancode;
	}

	public String getJidiaouser() {
		return jidiaouser;
	}

	public void setJidiaouser(String jidiaouser) {
		this.jidiaouser = jidiaouser;
	}

	public int getTraveldays() {
		return traveldays;
	}

	public void setTraveldays(int traveldays) {
		this.traveldays = traveldays;
	}

	String routeto;
	
	String traveltotool;
	String travelfromtool;
	
	String tuancode;
	String jidiaouser;
	
	int traveldays;
	
	
	
	
	
	
	
	
	
	
	
	public int getRouteid() {
		return routeid;
	}

	public void setRouteid(int routeid) {
		this.routeid = routeid;
	}

	int havesendmessage;
	
	public BigDecimal getWalletpayment() {
		return walletpayment;
	}

	public void setWalletpayment(BigDecimal walletpayment) {
		this.walletpayment = walletpayment;
	}

	public BigDecimal getTichengpayment() {
		return tichengpayment;
	}

	public void setTichengpayment(BigDecimal tichengpayment) {
		this.tichengpayment = tichengpayment;
	}

	BigDecimal walletpayment;
	BigDecimal tichengpayment;
	

	public int getHavesendmessage() {
		return havesendmessage;
	}

	public void setHavesendmessage(int havesendmessage) {
		this.havesendmessage = havesendmessage;
	}

	public String getPaymentusername() {
		return paymentusername;
	}

	public void setPaymentusername(String paymentusername) {
		this.paymentusername = paymentusername;
	}

	public String getTicketnumber() {
		return ticketnumber;
	}

	public void setTicketnumber(String ticketnumber) {
		this.ticketnumber = ticketnumber;
	}

	private String ticketnumber;

	public BigDecimal getNewpayment() {
		return newpayment;
	}

	public void setNewpayment(BigDecimal newpayment) {
		this.newpayment = newpayment;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	private Date createTime;

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

	private Long organid;

	private Long majororganid;

	private Integer lzxcount;

	private Integer ywxcount;

	private BigDecimal ywxper;

	private BigDecimal lzxper;
	String member;
	String begintimeori;

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	String endtimeori;
	int paytag;
	String route;

	public BigDecimal getChaju() {
		return chaju;
	}

	public void setChaju(BigDecimal chaju) {
		this.chaju = chaju;
	}

	BigDecimal chaju;

	public int getPaytag() {
		return paytag;
	}

	public void setPaytag(int paytag) {
		this.paytag = paytag;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
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

	public Long getOrganid() {
		return organid;
	}

	public void setOrganid(Long organid) {
		this.organid = organid;
	}

	public Long getMajororganid() {
		return majororganid;
	}

	public void setMajororganid(Long majororganid) {
		this.majororganid = majororganid;
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
}