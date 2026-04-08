package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class TbDouyinMessage implements Serializable {
    private Integer id;

    private String robotid;

    private String douyinid;

    private String zhuboid;

    private Date gztime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRobotid() {
        return robotid;
    }

    public void setRobotid(String robotid) {
        this.robotid = robotid == null ? null : robotid.trim();
    }

    public String getDouyinid() {
        return douyinid;
    }

    public void setDouyinid(String douyinid) {
        this.douyinid = douyinid == null ? null : douyinid.trim();
    }

    public String getZhuboid() {
        return zhuboid;
    }

    public void setZhuboid(String zhuboid) {
        this.zhuboid = zhuboid == null ? null : zhuboid.trim();
    }

    public Date getGztime() {
        return gztime;
    }

    public void setGztime(Date gztime) {
        this.gztime = gztime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", robotid=").append(robotid);
        sb.append(", douyinid=").append(douyinid);
        sb.append(", zhuboid=").append(zhuboid);
        sb.append(", gztime=").append(gztime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}