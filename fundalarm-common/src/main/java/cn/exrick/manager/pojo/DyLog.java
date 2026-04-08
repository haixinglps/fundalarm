package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class DyLog implements Serializable {
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "时分秒")
    private String dt;

    @ApiModelProperty(value = "人数")
    private String people;

    @ApiModelProperty(value = "日志时间")
    private String oridt;

    @ApiModelProperty(value = "0 成功 2 5分钟无红包 1 连续6次抢不到")
    private Integer type;

    private String ip;

    private String agents;

    @ApiModelProperty(value = "0未分析 1已分析")
    private Integer status;

    private String username;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt == null ? null : dt.trim();
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people == null ? null : people.trim();
    }

    public String getOridt() {
        return oridt;
    }

    public void setOridt(String oridt) {
        this.oridt = oridt == null ? null : oridt.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getAgents() {
        return agents;
    }

    public void setAgents(String agents) {
        this.agents = agents == null ? null : agents.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", dt=").append(dt);
        sb.append(", people=").append(people);
        sb.append(", oridt=").append(oridt);
        sb.append(", type=").append(type);
        sb.append(", ip=").append(ip);
        sb.append(", agents=").append(agents);
        sb.append(", status=").append(status);
        sb.append(", username=").append(username);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}