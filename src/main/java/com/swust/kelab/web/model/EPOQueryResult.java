package com.swust.kelab.web.model;

import com.swust.kelab.domain.enums.AttentionLevel;
import com.swust.kelab.domain.enums.Sentiment;

public class EPOQueryResult {
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private int id;
    private String title;
    private String url;
    private String summerize;
    private Sentiment sentiment;
    private AttentionLevel attentionLevel;
    private String source;
    private String originalSource;
    private int read;
    private int collect;
    private int UserId;
    private boolean same;
   
    
    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getOriginalSource() {
        return originalSource;
    }

    public void setOriginalSource(String originalSource) {
        this.originalSource = originalSource;
    }

    private String pubTime;
    private String crawlTime;
    private String topic;
    private int comment;
    private int progation;

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getProgation() {
        return progation;
    }

    public void setProgation(int progation) {
        this.progation = progation;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummerize() {
        return summerize;
    }

    public void setSummerize(String summerize) {
        this.summerize = summerize;
    }

    public String getSentiment() {
        return sentiment.getDesc();
    }

    public String getAttentionLevel() {
        return attentionLevel.getDesc();
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public void setAttentionLevel(AttentionLevel attentionLevel) {
        this.attentionLevel = attentionLevel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(String crawlTime) {
        this.crawlTime = crawlTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isSame() {
        return same;
    }

    public void setSame(boolean same) {
        this.same = same;
    }
}
