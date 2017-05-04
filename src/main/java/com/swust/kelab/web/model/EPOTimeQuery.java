package com.swust.kelab.web.model;

import java.util.Date;

public class EPOTimeQuery {
    private int recentDays;
    private Date startTime;
    private Date endTime;
    private int topNum;
    private int userId;//根据用户id查询
    

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRecentDays() {
        return recentDays;
    }

    public void setRecentDays(int recentDays) {
        this.recentDays = recentDays;
    }

    public int getTopNum() {
        return topNum;
    }

    public void setTopNum(int topNum) {
        this.topNum = topNum;
    }

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

}
