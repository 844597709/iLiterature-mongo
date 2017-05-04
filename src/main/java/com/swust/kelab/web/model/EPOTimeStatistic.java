package com.swust.kelab.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 时间段的统计数据格式
 * 
 * @author longlongchang
 * 
 */
public class EPOTimeStatistic {
    private int order;
    private int count;
    private double sentiment;
    private String timeDesc;

    public EPOTimeStatistic() {
    }

    public EPOTimeStatistic(int order, int count, String timeDesc) {
        this.order = order;
        this.count = count;
        this.timeDesc = timeDesc;
    }
    public EPOTimeStatistic(int order,double sentiment, String timeDesc) {
        this.order = order;
        this.sentiment =  sentiment;
        this.timeDesc = timeDesc;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public double getSentiment() {
        return sentiment;
    }

    public void setSentiment(double sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    
}
