package com.swust.kelab.mongo.dao.query;

/**
 * Created by zengdan on 2017/2/13.
 */
public class AuthorQuery extends BaseQuery {
    private String authName;
    private String authArea;
    private Integer authWebsiteId;

    public Integer getAuthWebsiteId() {
        return authWebsiteId;
    }

    public void setAuthWebsiteId(Integer authWebsiteId) {
        this.authWebsiteId = authWebsiteId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthArea() {
        return authArea;
    }

    public void setAuthArea(String authArea) {
        this.authArea = authArea;
    }
}
