package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class TbGroupsforpolicy implements java.io.Serializable {
    private Integer id;

    private String companyname;

    private Integer peoplecount;

    private Integer policyid;

    private BigDecimal bili;

    private String manname;

    private String phone;

    private Date dt;

    private String sourceip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname == null ? null : companyname.trim();
    }

    public Integer getPeoplecount() {
        return peoplecount;
    }

    public void setPeoplecount(Integer peoplecount) {
        this.peoplecount = peoplecount;
    }

    public Integer getPolicyid() {
        return policyid;
    }

    public void setPolicyid(Integer policyid) {
        this.policyid = policyid;
    }

    public BigDecimal getBili() {
        return bili;
    }

    public void setBili(BigDecimal bili) {
        this.bili = bili;
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

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getSourceip() {
        return sourceip;
    }

    public void setSourceip(String sourceip) {
        this.sourceip = sourceip == null ? null : sourceip.trim();
    }
}