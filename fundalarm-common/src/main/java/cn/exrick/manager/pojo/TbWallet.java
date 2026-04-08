package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class TbWallet implements Serializable {
    private Integer id;

    @ApiModelProperty(value = "用户tg账户id")
    private String uid;

    @ApiModelProperty(value = "余额")
    private Integer balance;

    private Date created;

    private Date updated;

    private Integer version;

    private String nickname;

    @ApiModelProperty(value = "小飞机网盘用户名")
    private String feijiUsername;

    @ApiModelProperty(value = "小飞机网盘密码")
    private String feijiPassword;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getFeijiUsername() {
        return feijiUsername;
    }

    public void setFeijiUsername(String feijiUsername) {
        this.feijiUsername = feijiUsername == null ? null : feijiUsername.trim();
    }

    public String getFeijiPassword() {
        return feijiPassword;
    }

    public void setFeijiPassword(String feijiPassword) {
        this.feijiPassword = feijiPassword == null ? null : feijiPassword.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", balance=").append(balance);
        sb.append(", created=").append(created);
        sb.append(", updated=").append(updated);
        sb.append(", version=").append(version);
        sb.append(", nickname=").append(nickname);
        sb.append(", feijiUsername=").append(feijiUsername);
        sb.append(", feijiPassword=").append(feijiPassword != null ? "***" : null);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
