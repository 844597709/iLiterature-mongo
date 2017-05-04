package com.swust.kelab.mongo.domain;

import com.swust.kelab.mongo.dao.query.BaseModel;

/**
 * Created by zengdan on 2017/1/6.
 */
public class TempWorks extends BaseModel {
    private Integer workId;
    private Integer workWebsiteId;
    private String workUrl;
    private String workTitle;
    private String workAuthor;
    private Integer workAuthId;
    private String workDesc;
    private String workNum;
    private String workType;
    private String workMark;
    private String workNature;
    private String workAuthorization;
    private String workWriteProcess;
    private String workIntime;
    private String workOtherInfo;
    private Integer workTotalHits;
    private Integer workTotalRecoms;
    private Integer workCommentsNum;

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public Integer getWorkWebsiteId() {
        return workWebsiteId;
    }

    public void setWorkWebsiteId(Integer workWebsiteId) {
        this.workWebsiteId = workWebsiteId;
    }

    public String getWorkUrl() {
        return workUrl;
    }

    public void setWorkUrl(String workUrl) {
        this.workUrl = workUrl;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public String getWorkAuthor() {
        return workAuthor;
    }

    public void setWorkAuthor(String workAuthor) {
        this.workAuthor = workAuthor;
    }

    public Integer getWorkAuthId() {
        return workAuthId;
    }

    public void setWorkAuthId(Integer workAuthId) {
        this.workAuthId = workAuthId;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkMark() {
        return workMark;
    }

    public void setWorkMark(String workMark) {
        this.workMark = workMark;
    }

    public String getWorkNature() {
        return workNature;
    }

    public void setWorkNature(String workNature) {
        this.workNature = workNature;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public String getWorkWriteProcess() {
        return workWriteProcess;
    }

    public void setWorkWriteProcess(String workWriteProcess) {
        this.workWriteProcess = workWriteProcess;
    }

    public String getWorkIntime() {
        return workIntime;
    }

    public void setWorkIntime(String workIntime) {
        this.workIntime = workIntime;
    }

    public String getWorkOtherInfo() {
        return workOtherInfo;
    }

    public void setWorkOtherInfo(String workOtherInfo) {
        this.workOtherInfo = workOtherInfo;
    }

    public Integer getWorkTotalHits() {
        return workTotalHits;
    }

    public void setWorkTotalHits(Integer workTotalHits) {
        this.workTotalHits = workTotalHits;
    }

    public Integer getWorkTotalRecoms() {
        return workTotalRecoms;
    }

    public void setWorkTotalRecoms(Integer workTotalRecoms) {
        this.workTotalRecoms = workTotalRecoms;
    }

    public Integer getWorkCommentsNum() {
        return workCommentsNum;
    }

    public void setWorkCommentsNum(Integer workCommentsNum) {
        this.workCommentsNum = workCommentsNum;
    }
}
