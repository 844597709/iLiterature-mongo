package com.swust.kelab.domain;

import javax.xml.crypto.Data;

public class WorksInfo {
    public int workId;
    public String author;
    public String title;
    public String url;
    public String type;
    public String num;
    public String mark;
    public String nature;
    public String authorization;
    public String otherInfo;
    public String description;
    public long totalHits;
    public String sTime;
    public Data time;

    //--zd--
    public long totalWorks;
    public long totalRecoms;
    public long commentsNum;
    public int crwsSiteId;
    public String crwsSiteName;
    
    public long getTotalWorks() {
        return totalWorks;
    }
    public void setTotalWorks(long totalWorks) {
        this.totalWorks = totalWorks;
    }
    
    public long getTotalRecoms() {
        return totalRecoms;
    }
    public void setTotalRecoms(long totalRecoms) {
        this.totalRecoms = totalRecoms;
    }
    
    public long getCommentsNum() {
        return commentsNum;
    }
    public void setCommentsNum(long commentsNum) {
        this.commentsNum = commentsNum;
    }
    
    public int getCrwsSiteId() {
        return crwsSiteId;
    }
    public void setCrwsSiteId(int crwsSiteId) {
        this.crwsSiteId = crwsSiteId;
    }
    
    public String getCrwsSiteName() {
        return crwsSiteName;
    }
    public void setCrwsSiteName(String crwsSiteName) {
        this.crwsSiteName = crwsSiteName;
    }
    //--至此--
    
    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public Data getTime() {
        return time;
    }

    public void setTime(Data time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

}
