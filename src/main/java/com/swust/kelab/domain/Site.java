package com.swust.kelab.domain;

public class Site {
    public int siteId;
    public String siteName;
    public String encode;
    public String domainLimit;
    public String seedUrl;
    public int crawlStyle;
    public int updateStyle;
    public int maxThread;
    public String currencyUnit;
    public String authorUpdate;
    public String worksUpdate;
    public String extraWorksInfo;
    public String innerFilter;
    public String authorFilter;
    public String worksFilter;
    public String commentFilter;
    public String authorExtractRule;
    public String worksExtractRule;
    public String commentExtractRule;
    public int authorJsHandle;
    public int worksJsHandle;
    public int commentJsHandle;
    public int enable;

    //--zd--
    public long totalAuthors;
    public long totalWorks;
    
    public long getTotalAuthors(){
    	return totalAuthors;
    }
    public void setTotalAuthors(long totalAuthors){
    	this.totalAuthors = totalAuthors;
    }
    
    public long getTotalWorks(){
    	return totalWorks;
    }
    public void setTotalWorks(long totalWorks){
    	this.totalWorks = totalWorks;
    }
    //--至此--
    
    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getSeedUrl() {
        return seedUrl;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    public int getCrawlStyle() {
        return crawlStyle;
    }

    public void setCrawlStyle(int crawlStyle) {
        this.crawlStyle = crawlStyle;
    }

    public int getUpdateStyle() {
        return updateStyle;
    }

    public void setUpdateStyle(int updateStyle) {
        this.updateStyle = updateStyle;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public String getAuthorUpdate() {
        return authorUpdate;
    }

    public void setAuthorUpdate(String authorUpdate) {
        this.authorUpdate = authorUpdate;
    }

    public String getWorksUpdate() {
        return worksUpdate;
    }

    public void setWorksUpdate(String worksUpdate) {
        this.worksUpdate = worksUpdate;
    }

    public String getExtraWorksInfo() {
        return extraWorksInfo;
    }

    public void setExtraWorksInfo(String extraWorksInfo) {
        this.extraWorksInfo = extraWorksInfo;
    }

    public String getInnerFilter() {
        return innerFilter;
    }

    public void setInnerFilter(String innerFilter) {
        this.innerFilter = innerFilter;
    }

    public String getWorksFilter() {
        return worksFilter;
    }

    public void setWorksFilter(String worksFilter) {
        this.worksFilter = worksFilter;
    }

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

    public int getAuthorJsHandle() {
        return authorJsHandle;
    }

    public void setAuthorJsHandle(int authorJsHandle) {
        this.authorJsHandle = authorJsHandle;
    }

    public int getWorksJsHandle() {
        return worksJsHandle;
    }

    public void setWorksJsHandle(int worksJsHandle) {
        this.worksJsHandle = worksJsHandle;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getDomainLimit() {
        return domainLimit;
    }

    public void setDomainLimit(String domainLimit) {
        this.domainLimit = domainLimit;
    }

    public String getAuthorFilter() {
        return authorFilter;
    }

    public void setAuthorFilter(String authorFilter) {
        this.authorFilter = authorFilter;
    }

    public String getCommentFilter() {
        return commentFilter;
    }

    public void setCommentFilter(String commentFilter) {
        this.commentFilter = commentFilter;
    }

    public String getAuthorExtractRule() {
        return authorExtractRule;
    }

    public void setAuthorExtractRule(String authorExtractRule) {
        this.authorExtractRule = authorExtractRule;
    }

    public String getWorksExtractRule() {
        return worksExtractRule;
    }

    public void setWorksExtractRule(String worksExtractRule) {
        this.worksExtractRule = worksExtractRule;
    }

    public String getCommentExtractRule() {
        return commentExtractRule;
    }

    public void setCommentExtractRule(String commentExtractRule) {
        this.commentExtractRule = commentExtractRule;
    }

    public int getCommentJsHandle() {
        return commentJsHandle;
    }

    public void setCommentJsHandle(int commentJsHandle) {
        this.commentJsHandle = commentJsHandle;
    }

}
