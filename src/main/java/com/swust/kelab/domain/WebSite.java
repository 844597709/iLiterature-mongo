package com.swust.kelab.domain;

public class WebSite {
    public int siteId;
    public String siteName;
    public String homePage;
    public String worksInfo;
    public String authorInfo;
    public int concernDegree;
    
    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getWorksInfo() {
        return worksInfo;
    }

    public void setWorksInfo(String worksInfo) {
        this.worksInfo = worksInfo;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public int getConcernDegree() {
        return concernDegree;
    }

    public void setConcernDegree(int concernDegree) {
        this.concernDegree = concernDegree;
    }
}
