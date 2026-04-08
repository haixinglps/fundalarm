package cn.exrick.manager.pojo;

import java.util.Date;

public class TbGroupofhavezan  implements java.io.Serializable{
    private Integer id;

    private Long robotid;

    private Integer execuatecount;

    private Date dt;

    private Date updatetime;

    private Integer zansuccesstag;

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

    public Integer getZansuccesstag() {
        return zansuccesstag;
    }

    public void setZansuccesstag(Integer zansuccesstag) {
        this.zansuccesstag = zansuccesstag;
    }
}