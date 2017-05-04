package com.swust.kelab.web.model;

import java.util.Date;

public class TopicModel {
    private int id;
	private String inkw;
	private String exkw;
	private String name;
	private String atte;
	private String syno;
	public String getSyno() {
        return syno;
    }
    public void setSyno(String syno) {
        this.syno = syno;
    }
    private Date startTime;
	private Date endTime;
	public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public String getInkw() {
		return inkw;
	}
	public void setInkw(String inkw) {
		this.inkw = inkw;
	}
	public String getExkw() {
		return exkw;
	}
	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setExkw(String exkw) {
		this.exkw = exkw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAtte() {
		return atte;
	}
	public void setAtte(String atte) {
		this.atte = atte;
	}
}
