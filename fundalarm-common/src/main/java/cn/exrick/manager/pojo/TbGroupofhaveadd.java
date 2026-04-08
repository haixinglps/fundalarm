package cn.exrick.manager.pojo;

import java.util.Date;

public class TbGroupofhaveadd implements java.io.Serializable{
    private Integer id;

    private Long robotid;

    private Integer groupid;

    private Integer execuatecount;

    private Date dt;

    private Date updatetime;

    private Integer successtag;

    private String groupname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRobotid() {
        return robotid;
    }

    public void setRobotid(Long robotid) {
        this.robotid = robotid;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getExecuatecount() {
        return execuatecount;
    }

    public void setExecuatecount(Integer execuatecount) {
        this.execuatecount = execuatecount;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getSuccesstag() {
        return successtag;
    }

    public void setSuccesstag(Integer successtag) {
        this.successtag = successtag;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname == null ? null : groupname.trim();
    }
}