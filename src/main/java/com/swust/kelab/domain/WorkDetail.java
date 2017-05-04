package com.swust.kelab.domain;

public class WorkDetail {
    public int workId;
    public int wordsNum;
    public String totalHits; //点击数
    public String totalRecoms; //推荐数
    public int collectNum;
    public int mTickets;
    public int flowersNum;
    public String commentsNum; //评论数
    public int grade;
    public String updateTime;
    public String sTime; // 采集时间

    //--zd--
    public String title;
    public String author;
    public String type;
    public long totalUpdateWorks;
    public long totalWorks;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public long getTotalWorks(){
    	return totalWorks;
    }
    public void setTotalWorks(long  totalWorks){
    	this.totalWorks = totalWorks;
    }
    
    public long getTotalUpdateWorks(){
    	return totalUpdateWorks;
    }
    public void setTotalUpdateWorks(long  totalUpdateWorks){
    	this.totalUpdateWorks = totalUpdateWorks;
    }
    //--至此--
    
    
    
	public int getWorkId() {
		return workId;
	}
	public void setWorkId(int workId) {
		this.workId = workId;
	}
	public int getWordsNum() {
		return wordsNum;
	}
	public void setWordsNum(int wordsNum) {
		this.wordsNum = wordsNum;
	}
	public String getTotalHits() {
		return totalHits;
	}
	public void setTotalHits(String totalHits) {
		this.totalHits = totalHits;
	}
	public String getTotalRecoms() {
		return totalRecoms;
	}
	public void setTotalRecoms(String totalRecoms) {
		this.totalRecoms = totalRecoms;
	}
	public int getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}
	public int getmTickets() {
		return mTickets;
	}
	public void setmTickets(int mTickets) {
		this.mTickets = mTickets;
	}
	public int getFlowersNum() {
		return flowersNum;
	}
	public void setFlowersNum(int flowersNum) {
		this.flowersNum = flowersNum;
	}
	public String getCommentsNum() {
		return commentsNum;
	}
	public void setCommentsNum(String commentsNum) {
		this.commentsNum = commentsNum;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

    
}
