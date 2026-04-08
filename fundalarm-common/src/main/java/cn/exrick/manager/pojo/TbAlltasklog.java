package cn.exrick.manager.pojo;

import java.util.Date;

public class TbAlltasklog implements java.io.Serializable {
    private Integer id;

    private Integer taskid;

    private Date workstarttime;

    private Date workendtime;

    private Integer result;

    private String message;

    private Integer sendcount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public Date getWorkstarttime() {
        return workstarttime;
    }

    public void setWorkstarttime(Date workstarttime) {
        this.workstarttime = workstarttime;
    }

    public Date getWorkendtime() {
        return workendtime;
    }

    public void setWorkendtime(Date workendtime) {
        this.workendtime = workendtime;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Integer getSendcount() {
        return sendcount;
    }

    public void setSendcount(Integer sendcount) {
        this.sendcount = sendcount;
    }
}