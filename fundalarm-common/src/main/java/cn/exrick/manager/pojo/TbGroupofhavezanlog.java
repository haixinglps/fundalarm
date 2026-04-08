package cn.exrick.manager.pojo;

import java.util.Date;

public class TbGroupofhavezanlog  implements java.io.Serializable{
    private Integer id;

    private Integer postcount;

    private Date starttime;

    private Date endtime;

    private Integer status;

    private String message;

    private Integer groupzanid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostcount() {
        return postcount;
    }

    public void setPostcount(Integer postcount) {
        this.postcount = postcount;
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

    public Integer getGroupzanid() {
        return groupzanid;
    }

    public void setGroupzanid(Integer groupzanid) {
        this.groupzanid = groupzanid;
    }
}