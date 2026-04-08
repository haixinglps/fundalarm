package cn.exrick.manager.pojo;

import java.math.BigDecimal;

public class TbRecord  implements java.io.Serializable{
    private Integer id;

    private String title;

    private String safetype;

    private Integer policyid;

    private String reason;

    String eventtimestr;
    
    String accidentProvinceCode;
    String accidentCityCode;
    String accidentCountyCode;
    String accidentCauseLevel1;
    String policytype;
    String sourcerplatform;
    
    String orderid;
   
    
    
    public String getPolicytype() {
		return policytype;
	}

	public void setPolicytype(String policytype) {
		this.policytype = policytype;
	}

	public String getSourcerplatform() {
		return sourcerplatform;
	}

	public void setSourcerplatform(String sourcerplatform) {
		this.sourcerplatform = sourcerplatform;
	}

	public String getAccidentProvinceCode() {
		return accidentProvinceCode;
	}

	public void setAccidentProvinceCode(String accidentProvinceCode) {
		this.accidentProvinceCode = accidentProvinceCode;
	}

	public String getAccidentCityCode() {
		return accidentCityCode;
	}

	public void setAccidentCityCode(String accidentCityCode) {
		this.accidentCityCode = accidentCityCode;
	}

	public String getAccidentCountyCode() {
		return accidentCountyCode;
	}

	public void setAccidentCountyCode(String accidentCountyCode) {
		this.accidentCountyCode = accidentCountyCode;
	}

	public String getAccidentCauseLevel1() {
		return accidentCauseLevel1;
	}

	public void setAccidentCauseLevel1(String accidentCauseLevel1) {
		this.accidentCauseLevel1 = accidentCauseLevel1;
	}

	public String getAccidentCauseLevel2() {
		return accidentCauseLevel2;
	}

	public void setAccidentCauseLevel2(String accidentCauseLevel2) {
		this.accidentCauseLevel2 = accidentCauseLevel2;
	}

	public String getClientBankCode() {
		return clientBankCode;
	}

	public void setClientBankCode(String clientBankCode) {
		this.clientBankCode = clientBankCode;
	}

	String accidentCauseLevel2;
    String clientBankCode;
    public String getEventtimestr() {
		return eventtimestr;
	}

	public void setEventtimestr(String eventtimestr) {
		this.eventtimestr = eventtimestr;
	}

	private Long eventtime;

    private Integer eventaddress;

    private String detailaddress;

    private String detailaddress2;

    private String description;

    private BigDecimal sunshi;

    private Long userid;

    private Integer unitofshunshi;

    private String huming;

    private String zhanghu;

    private String kaihuhang;

    private String phone;

    private String images;

    private String zhuanyuan;

    public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	private String zhuanyuanphone;

    private String status;

    private Long createtime;

    private Long updatetime;

    private String username;

    private Long begintime;

    private Long endtime;

    private String people;

    private String anjianid;

    private String merchantpoclicyid;

    private String personpolicyid;

    private String jigouhao;

    private String serverdetail;

    private String provincecode;

    private String citycode;

    private String addcode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSafetype() {
        return safetype;
    }

    public void setSafetype(String safetype) {
        this.safetype = safetype == null ? null : safetype.trim();
    }

    public Integer getPolicyid() {
        return policyid;
    }

    public void setPolicyid(Integer policyid) {
        this.policyid = policyid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Long getEventtime() {
        return eventtime;
    }

    public void setEventtime(Long eventtime) {
        this.eventtime = eventtime;
    }

    public Integer getEventaddress() {
        return eventaddress;
    }

    public void setEventaddress(Integer eventaddress) {
        this.eventaddress = eventaddress;
    }

    public String getDetailaddress() {
        return detailaddress;
    }

    public void setDetailaddress(String detailaddress) {
        this.detailaddress = detailaddress == null ? null : detailaddress.trim();
    }

    public String getDetailaddress2() {
        return detailaddress2;
    }

    public void setDetailaddress2(String detailaddress2) {
        this.detailaddress2 = detailaddress2 == null ? null : detailaddress2.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public BigDecimal getSunshi() {
        return sunshi;
    }

    public void setSunshi(BigDecimal sunshi) {
        this.sunshi = sunshi;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getUnitofshunshi() {
        return unitofshunshi;
    }

    public void setUnitofshunshi(Integer unitofshunshi) {
        this.unitofshunshi = unitofshunshi;
    }

    public String getHuming() {
        return huming;
    }

    public void setHuming(String huming) {
        this.huming = huming == null ? null : huming.trim();
    }

    public String getZhanghu() {
        return zhanghu;
    }

    public void setZhanghu(String zhanghu) {
        this.zhanghu = zhanghu == null ? null : zhanghu.trim();
    }

    public String getKaihuhang() {
        return kaihuhang;
    }

    public void setKaihuhang(String kaihuhang) {
        this.kaihuhang = kaihuhang == null ? null : kaihuhang.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images == null ? null : images.trim();
    }

    public String getZhuanyuan() {
        return zhuanyuan;
    }

    public void setZhuanyuan(String zhuanyuan) {
        this.zhuanyuan = zhuanyuan == null ? null : zhuanyuan.trim();
    }

    public String getZhuanyuanphone() {
        return zhuanyuanphone;
    }

    public void setZhuanyuanphone(String zhuanyuanphone) {
        this.zhuanyuanphone = zhuanyuanphone == null ? null : zhuanyuanphone.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Long getBegintime() {
        return begintime;
    }

    public void setBegintime(Long begintime) {
        this.begintime = begintime;
    }

    public Long getEndtime() {
        return endtime;
    }

    public void setEndtime(Long endtime) {
        this.endtime = endtime;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people == null ? null : people.trim();
    }

    public String getAnjianid() {
        return anjianid;
    }

    public void setAnjianid(String anjianid) {
        this.anjianid = anjianid == null ? null : anjianid.trim();
    }

    public String getMerchantpoclicyid() {
        return merchantpoclicyid;
    }

    public void setMerchantpoclicyid(String merchantpoclicyid) {
        this.merchantpoclicyid = merchantpoclicyid == null ? null : merchantpoclicyid.trim();
    }

    public String getPersonpolicyid() {
        return personpolicyid;
    }

    public void setPersonpolicyid(String personpolicyid) {
        this.personpolicyid = personpolicyid == null ? null : personpolicyid.trim();
    }

    public String getJigouhao() {
        return jigouhao;
    }

    public void setJigouhao(String jigouhao) {
        this.jigouhao = jigouhao == null ? null : jigouhao.trim();
    }

    public String getServerdetail() {
        return serverdetail;
    }

    public void setServerdetail(String serverdetail) {
        this.serverdetail = serverdetail == null ? null : serverdetail.trim();
    }

    public String getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(String provincecode) {
        this.provincecode = provincecode == null ? null : provincecode.trim();
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode == null ? null : citycode.trim();
    }

    public String getAddcode() {
        return addcode;
    }

    public void setAddcode(String addcode) {
        this.addcode = addcode == null ? null : addcode.trim();
    }
}