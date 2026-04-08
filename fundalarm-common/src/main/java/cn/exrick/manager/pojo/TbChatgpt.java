package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class TbChatgpt implements Serializable {
    private Integer id;

    @ApiModelProperty(value = "模板URL")
    private String templateurl;

    @ApiModelProperty(value = "数据URL")
    private String dataurl;

    @ApiModelProperty(value = "创建时间")
    private Date dt;

    @ApiModelProperty(value = "0运行中 1已完成 2文件有异常执行失败")
    private Integer status;

    @ApiModelProperty(value = "0未支付 1已支付")
    private Integer paystatus;

    @ApiModelProperty(value = "结果地址")
    private String resulturl;

    @ApiModelProperty(value = "完成时间")
    private Date finisht;

    private String uid;

    @ApiModelProperty(value = "申请重新运行 1已申请 0未申请 2重新运行结束")
    private Integer repeattag;

    private String templatename;

    private String dataname;

    @ApiModelProperty(value = "轮次")
    private Integer cyclecount;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateurl() {
        return templateurl;
    }

    public void setTemplateurl(String templateurl) {
        this.templateurl = templateurl == null ? null : templateurl.trim();
    }

    public String getDataurl() {
        return dataurl;
    }

    public void setDataurl(String dataurl) {
        this.dataurl = dataurl == null ? null : dataurl.trim();
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(Integer paystatus) {
        this.paystatus = paystatus;
    }

    public String getResulturl() {
        return resulturl;
    }

    public void setResulturl(String resulturl) {
        this.resulturl = resulturl == null ? null : resulturl.trim();
    }

    public Date getFinisht() {
        return finisht;
    }

    public void setFinisht(Date finisht) {
        this.finisht = finisht;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public Integer getRepeattag() {
        return repeattag;
    }

    public void setRepeattag(Integer repeattag) {
        this.repeattag = repeattag;
    }

    public String getTemplatename() {
        return templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename == null ? null : templatename.trim();
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname == null ? null : dataname.trim();
    }

    public Integer getCyclecount() {
        return cyclecount;
    }

    public void setCyclecount(Integer cyclecount) {
        this.cyclecount = cyclecount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", templateurl=").append(templateurl);
        sb.append(", dataurl=").append(dataurl);
        sb.append(", dt=").append(dt);
        sb.append(", status=").append(status);
        sb.append(", paystatus=").append(paystatus);
        sb.append(", resulturl=").append(resulturl);
        sb.append(", finisht=").append(finisht);
        sb.append(", uid=").append(uid);
        sb.append(", repeattag=").append(repeattag);
        sb.append(", templatename=").append(templatename);
        sb.append(", dataname=").append(dataname);
        sb.append(", cyclecount=").append(cyclecount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}