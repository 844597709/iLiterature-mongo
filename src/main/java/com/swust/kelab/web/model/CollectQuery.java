package com.swust.kelab.web.model;

public class CollectQuery {
    private Integer topic;
    private int[] pageArray; // 分页显示时页数
    private int recordPerPage; // 分页显示时每页限制条数
    public Integer getTopic() {
        return topic;
    }
    public void setTopic(Integer topic) {
        this.topic = topic;
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
    
}
