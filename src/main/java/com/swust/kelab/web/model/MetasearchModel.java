package com.swust.kelab.web.model;

public class MetasearchModel{

    private int id;
	private String metaName;
	private String cate;
	private  String prefix;
	private  String postfix;
	private String pageTag;
	private String codeFormat;
	private String divTag;
	private String urlTag;
	private String titleTag;
	private String summaryTag;
	private String dateTag;
	private String siteNameTag;
	public String getSiteNameTag() {
        return siteNameTag;
    }
    public void setSiteNameTag(String siteNameTag) {
        this.siteNameTag = siteNameTag;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    private String titleAd;
	private String urlAd;
	private String summaryAd;
	private int sleepTime;
	private int threadNum;
	private int highSearch;
	private int pageNum;
    public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getCate() {
        return cate;
    }
    public void setCate(String cate) {
        this.cate = cate;
    }
	public String getPostfix() {
		return postfix;
	}
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	public String getPageTag() {
		return pageTag;
	}
	public void setPageTag(String pageTag) {
		this.pageTag = pageTag;
	}
	public String getCodeFormat() {
		return codeFormat;
	}
	public void setCodeFormat(String codeFormat) {
		this.codeFormat = codeFormat;
	}
	public String getDivTag() {
		return divTag;
	}
	public void setDivTag(String divTag) {
		this.divTag = divTag;
	}
	public String getUrlTag() {
		return urlTag;
	}
	public void setUrlTag(String urlTag) {
		this.urlTag = urlTag;
	}
	public String getTitleTag() {
		return titleTag;
	}
	public void setTitleTag(String titleTag) {
		this.titleTag = titleTag;
	}
	public String getSummaryTag() {
		return summaryTag;
	}
	public void setSummaryTag(String summaryTag) {
		this.summaryTag = summaryTag;
	}
	public String getDateTag() {
		return dateTag;
	}
	public void setDateTag(String dateTag) {
		this.dateTag = dateTag;
	}
	public String getTitleAd() {
		return titleAd;
	}
	public void setTitleAd(String titleAd) {
		this.titleAd = titleAd;
	}
	public String getSummaryAd() {
		return summaryAd;
	}
	public void setSummaryAd(String summaryAd) {
		this.summaryAd = summaryAd;
	}
	public int getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public int getHighSearch() {
		return highSearch;
	}
	public void setHighSearch(int highSearch) {
		this.highSearch = highSearch;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public String getMetaName() {
        return metaName;
    }
    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }
    public String getUrlAd() {
		return urlAd;
	}
	public void setUrlAd(String urlAd) {
		this.urlAd = urlAd;
	}
}
