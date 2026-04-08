package cn.exrick.manager.pojo;

import java.util.List;

public class TbRoute implements java.io.Serializable {
    private Integer id;

    private String routename;

    private String comment;

    private Integer travetype;

    private Long organid;

    private Long majororganid;

    private Integer days;

    private Integer status;

    private String destinations;
    
    List<TbDestination> destinationslist;

    public List<TbDestination> getDestinationslist() {
		return destinationslist;
	}

	public void setDestinationslist(List<TbDestination> destinationslist) {
		this.destinationslist = destinationslist;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename == null ? null : routename.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getTravetype() {
        return travetype;
    }

    public void setTravetype(Integer travetype) {
        this.travetype = travetype;
    }

    public Long getOrganid() {
        return organid;
    }

    public void setOrganid(Long organid) {
        this.organid = organid;
    }

    public Long getMajororganid() {
        return majororganid;
    }

    public void setMajororganid(Long majororganid) {
        this.majororganid = majororganid;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations == null ? null : destinations.trim();
    }
}