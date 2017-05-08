package com.swust.kelab.mongo.domain;

import com.swust.kelab.mongo.dao.query.BaseModel;

public class TempAuthorUpdate extends BaseModel {
    private Integer auupId;
    private Integer auupAuthId;
    private String auupTime;
    private String auupGrade;
    private String auupAttr1;
    private String auupAttr2;
    private String auupAttr3;
    private String auupAttr4;
    private String auupAttr5;

    public Integer getAuupId() {
        return auupId;
    }

    public void setAuupId(Integer auupId) {
        this.auupId = auupId;
    }

    public Integer getAuupAuthId() {
        return auupAuthId;
    }

    public void setAuupAuthId(Integer auupAuthId) {
        this.auupAuthId = auupAuthId;
    }

    public String getAuupTime() {
        return auupTime;
    }

    public void setAuupTime(String auupTime) {
        this.auupTime = auupTime;
    }

    public String getAuupGrade() {
        return auupGrade;
    }

    public void setAuupGrade(String auupGrade) {
        this.auupGrade = auupGrade;
    }

    public String getAuupAttr1() {
        return auupAttr1;
    }

    public void setAuupAttr1(String auupAttr1) {
        this.auupAttr1 = auupAttr1;
    }

    public String getAuupAttr2() {
        return auupAttr2;
    }

    public void setAuupAttr2(String auupAttr2) {
        this.auupAttr2 = auupAttr2;
    }

    public String getAuupAttr3() {
        return auupAttr3;
    }

    public void setAuupAttr3(String auupAttr3) {
        this.auupAttr3 = auupAttr3;
    }

    public String getAuupAttr4() {
        return auupAttr4;
    }

    public void setAuupAttr4(String auupAttr4) {
        this.auupAttr4 = auupAttr4;
    }

    public String getAuupAttr5() {
        return auupAttr5;
    }

    public void setAuupAttr5(String auupAttr5) {
        this.auupAttr5 = auupAttr5;
    }
}
