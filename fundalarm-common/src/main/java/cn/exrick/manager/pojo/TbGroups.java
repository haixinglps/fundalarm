package cn.exrick.manager.pojo;

import java.util.Date;

public class TbGroups implements java.io.Serializable{
    private Integer id;

    private String groupname;

    private Integer robotid;

    private Integer organid;

    private Date dt;

    private Integer status;

    private Integer type;
    private String categoryname;
    
    int allpeoplecount;
    int hushu;

    public int getCategoryid() {
		return categoryid;
	}

	public int getAllpeoplecount() {
		return allpeoplecount;
	}

	public void setAllpeoplecount(int allpeoplecount) {
		this.allpeoplecount = allpeoplecount;
	}

	public int getHushu() {
		return hushu;
	}

	public void setHushu(int hushu) {
		this.hushu = hushu;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	private Integer allcount;

    private String logo;
    int categoryid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname == null ? null : groupname.trim();
    }

    public Integer getRobotid() {
        return robotid;
    }

    public void setRobotid(Integer robotid) {
        this.robotid = robotid;
    }

    public Integer getOrganid() {
        return organid;
    }

    public void setOrganid(Integer organid) {
        this.organid = organid;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAllcount() {
        return allcount;
    }

    public void setAllcount(Integer allcount) {
        this.allcount = allcount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }
}