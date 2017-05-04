package com.swust.kelab.domain;

public class MetaSearch {

	private int metaId;
	private String cate;
	private String metaName;
	private String prefix; 
	private  String postfix;
	private String pageTag;
	private String codeFormate;
	
	private String divTag;
	private String urlTag;
	private String titleTag;
	private String summTag;
	
	private String dateTag;
	private String titleAd;
	private String urlAd;
	private String summAd;
	private int sleep;//休眠时间
	private int thread;//线程数
	
	private String siteNameTag;

	public String getSiteNameTag() {
        return siteNameTag;
    }
    public void setSiteNameTag(String siteNameTag) {
        this.siteNameTag = siteNameTag;
    }
    private int highSearch;
	private int pageNum;
	public int getMetaId() {
		return metaId;
	}
	public void setMetaId(int metaId) {
		this.metaId = metaId;
	}
	public String getCate() {
		return cate;
	}
	public void setCate(String cate) {
		this.cate = cate;
	}
	public String getMetaName() {
		return metaName;
	}
	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
	public String getCodeFormate() {
		return codeFormate;
	}
	public void setCodeFormate(String codeFormate) {
		this.codeFormate = codeFormate;
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
	public String getSummTag() {
		return summTag;
	}
	public void setSummTag(String summTag) {
		this.summTag = summTag;
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
	public String getUrlAd() {
		return urlAd;
	}
	public void setUrlAd(String urlAd) {
		this.urlAd = urlAd;
	}
	public String getSummAd() {
		return summAd;
	}
	public void setSummAd(String summAd) {
		this.summAd = summAd;
	}
	public int getSleep() {
		return sleep;
	}
	public void setSleep(int sleep) {
		this.sleep = sleep;
	}
	public int getThread() {
		return thread;
	}
	public void setThread(int thread) {
		this.thread = thread;
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
	public MetaSearch()
	{}
}
