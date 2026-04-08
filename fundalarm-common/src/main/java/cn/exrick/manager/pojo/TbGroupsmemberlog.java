package cn.exrick.manager.pojo;

public class TbGroupsmemberlog implements java.io.Serializable {
    private Integer id;

    private Integer groupid;

    private Integer dayitem;

    private Integer msgcount;

    private Integer organid;

    private Long cd;

    private Long ud;
    String groupname;

    public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public int getAlarmcount() {
		return alarmcount;
	}

	public void setAlarmcount(int alarmcount) {
		this.alarmcount = alarmcount;
	}

	private Integer robotid;

    private String username;
    
    int alarmcount;

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

    public Integer getOrganid() {
        return organid;
    }

    public void setOrganid(Integer organid) {
        this.organid = organid;
    }

    public Long getCd() {
        return cd;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setCd(Long cd) {
        this.cd = cd;
    }

    public Long getUd() {
        return ud;
    }

    public void setUd(Long ud) {
        this.ud = ud;
    }

    public Integer getRobotid() {
        return robotid;
    }

    public void setRobotid(Integer robotid) {
        this.robotid = robotid;
    }

     
}