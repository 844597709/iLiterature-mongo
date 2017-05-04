package com.swust.kelab.web.model;

import java.util.Date;

public class FullTextQuery {
    private String category;
    private String query;
    private Integer sentiment;
    private Integer metaSearch;
    private IndexLocation indexLoc;
    private boolean isPubDate;
    private int recentDays;
    private Date startTime;
    private Date endTime;
    private OrderFieldType orderField;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private int[] pageArray; // 分页显示时页数
    private int recordPerPage; // 分页显示时每页限制条数

    public OrderFieldType getOrderField() {
        return orderField;
    }

    public void setOrderField(OrderFieldType orderField) {
        this.orderField = orderField;
    }

    public String getQuery() {
        return query;
    }

    public int getRecentDays() {
        return recentDays;
    }

    public void setRecentDays(int recentDays) {
        this.recentDays = recentDays;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getMetaSearch() {
        return metaSearch;
    }

    public void setMetaSearch(Integer metaSearch) {
        this.metaSearch = metaSearch;
    }

    public IndexLocation getIndexLoc() {
        return indexLoc;
    }

    public void setIndexLoc(IndexLocation indexLoc) {
        this.indexLoc = indexLoc;
    }

    public boolean isPubDate() {
        return isPubDate;
    }

    public void setPubDate(boolean isPubDate) {
        this.isPubDate = isPubDate;
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

    public int[] getPageArray() {
        return pageArray;
    }

    public void setPageArray(int[] pageArray) {
        this.pageArray = pageArray;
    }

    public int getRecordPerPage() {
        return recordPerPage;
    }

    public void setRecordPerPage(int recordPerPage) {
        this.recordPerPage = recordPerPage;
    }

    public Integer getSentiment() {
        return sentiment;
    }

    public void setSentiment(Integer sentiment) {
        this.sentiment = sentiment;
    }
}
