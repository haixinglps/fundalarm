package cn.exrick.manager.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class TbItemPrice implements java.io.Serializable {
    private Integer id;

    private Integer traveltypeid;

    private Integer agemin;

    private Date dt;

    private BigDecimal price;

    private Integer type;

    private Integer agemax;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTraveltypeid() {
        return traveltypeid;
    }

    public void setTraveltypeid(Integer traveltypeid) {
        this.traveltypeid = traveltypeid;
    }

    public Integer getAgemin() {
        return agemin;
    }

    public void setAgemin(Integer agemin) {
        this.agemin = agemin;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAgemax() {
        return agemax;
    }

    public void setAgemax(Integer agemax) {
        this.agemax = agemax;
    }
}