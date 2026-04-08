package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class TbCommandsDesc implements Serializable {
    private Integer id;

    private String scriptcode;

    private String longDesc;

    private Date created;

    @ApiModelProperty(value = "示例")
    private String example;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScriptcode() {
        return scriptcode;
    }

    public void setScriptcode(String scriptcode) {
        this.scriptcode = scriptcode == null ? null : scriptcode.trim();
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc == null ? null : longDesc.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example == null ? null : example.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", scriptcode=").append(scriptcode);
        sb.append(", longDesc=").append(longDesc);
        sb.append(", created=").append(created);
        sb.append(", example=").append(example);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}