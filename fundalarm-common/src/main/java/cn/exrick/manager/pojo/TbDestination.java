package cn.exrick.manager.pojo;

public class TbDestination implements java.io.Serializable {
    private Integer id;

    private String destination;

    private Integer travelindex;

    private String description;

    private Long createtime;

    private Integer routeid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination == null ? null : destination.trim();
    }

    public Integer getTravelindex() {
        return travelindex;
    }

    public void setTravelindex(Integer travelindex) {
        this.travelindex = travelindex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public Integer getRouteid() {
        return routeid;
    }

    public void setRouteid(Integer routeid) {
        this.routeid = routeid;
    }
}