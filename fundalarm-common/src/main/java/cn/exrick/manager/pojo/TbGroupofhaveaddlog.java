package cn.exrick.manager.pojo;

import java.util.Date;

public class TbGroupofhaveaddlog  implements java.io.Serializable{
    private Integer id;

    private Integer addcount;

    private Date starttime;

    private Date endtime;

    private Integer status;

    private String message;

    private Integer groupaddid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAddcount() {
        return addcount;
    }

    public void setAddcount(Integer addcount) {
        this.addcount = addcount;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Integer getGroupaddid() {
        return groupaddid;
    }

    public void setGroupaddid(Integer groupaddid) {
        this.groupaddid = groupaddid;
    }
}