package cn.exrick.manager.pojo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class StockDataWithBLOBs extends StockData implements Serializable {
    @ApiModelProperty(value = "五档行情")
    private String tradefive;

    @ApiModelProperty(value = "主题")
    private String subject;

    @ApiModelProperty(value = "主题缓存")
    private String subjectcachevos;

    private static final long serialVersionUID = 1L;

    public String getTradefive() {
        return tradefive;
    }

    public void setTradefive(String tradefive) {
        this.tradefive = tradefive == null ? null : tradefive.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getSubjectcachevos() {
        return subjectcachevos;
    }

    public void setSubjectcachevos(String subjectcachevos) {
        this.subjectcachevos = subjectcachevos == null ? null : subjectcachevos.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tradefive=").append(tradefive);
        sb.append(", subject=").append(subject);
        sb.append(", subjectcachevos=").append(subjectcachevos);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}