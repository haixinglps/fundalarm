package cn.exrick.manager.pojo;

import java.util.Date;

import javax.tools.JavaCompiler;

public class TbSubject implements java.io.Serializable {
    private Integer id;

    private String keyword;

    private Date dt;

    private Integer userid;

    private Integer organid;

    private Integer alarmstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getOrganid() {
        return organid;
    }

    public void setOrganid(Integer organid) {
        this.organid = organid;
    }

    public Integer getAlarmstatus() {
        return alarmstatus;
    }

    public void setAlarmstatus(Integer alarmstatus) {
        this.alarmstatus = alarmstatus;
    }
}