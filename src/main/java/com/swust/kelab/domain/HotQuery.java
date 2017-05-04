package com.swust.kelab.domain;

import java.util.Date;

public class HotQuery {
	private int id;
    private String title;
    private String summ;
    private String url;
    private Date crawlDate;
    private Date pubDate;
    private String sCrawlDate;
    private String sPubDate;
    private int metaid;
    private int tid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSumm() {
		return summ;
	}
	public void setSumm(String summ) {
		this.summ = summ;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
    public Date getCrawlDate() {
        return crawlDate;
    }
    public void setCrawlDate(Date crawlDate) {
        this.crawlDate = crawlDate;
    }
    public Date getPubDate() {
        return pubDate;
    }
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
    public String getsCrawlDate() {
        return sCrawlDate;
    }
    public void setsCrawlDate(String sCrawlDate) {
        this.sCrawlDate = sCrawlDate;
    }
    public String getsPubDate() {
        return sPubDate;
    }
    public void setsPubDate(String sPubDate) {
        this.sPubDate = sPubDate;
    }
    public int getMetaid() {
		return metaid;
	}
	public void setMetaid(int metaid) {
		this.metaid = metaid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	
}
