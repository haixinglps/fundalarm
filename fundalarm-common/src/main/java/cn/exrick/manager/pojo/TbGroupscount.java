package cn.exrick.manager.pojo;

public class TbGroupscount  implements java.io.Serializable{
    private Integer id;

    private Integer groupid;

    private Integer dayitem;

    private Integer msgcount;
    int alarmcount;
    int alarmpeoplecount;
    int organid;
    long cd;
    long ud;
    String groupname;
    
    public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public long getCd() {
		return cd;
	}

	public void setCd(long cd) {
		this.cd = cd;
	}

	public long getUd() {
		return ud;
	}

	public void setUd(long ud) {
		this.ud = ud;
	}

	public int getOrganid() {
		return organid;
	}

	public int getAlarmcount() {
		return alarmcount;
	}

	public void setAlarmcount(int alarmcount) {
		this.alarmcount = alarmcount;
	}

	public int getAlarmpeoplecount() {
		return alarmpeoplecount;
	}

	public void setAlarmpeoplecount(int alarmpeoplecount) {
		this.alarmpeoplecount = alarmpeoplecount;
	}

	public void setOrganid(int organid) {
		this.organid = organid;
	}

	public int getPeoplecount() {
		return peoplecount;
	}

	public void setPeoplecount(int peoplecount) {
		this.peoplecount = peoplecount;
	}

	int peoplecount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getDayitem() {
        return dayitem;
    }

    public void setDayitem(Integer dayitem) {
        this.dayitem = dayitem;
    }

    public Integer getMsgcount() {
        return msgcount;
    }

    public void setMsgcount(Integer msgcount) {
        this.msgcount = msgcount;
    }
}