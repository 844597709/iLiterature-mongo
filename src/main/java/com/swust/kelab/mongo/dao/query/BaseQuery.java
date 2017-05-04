package com.swust.kelab.mongo.dao.query;

import com.mongodb.DBObject;
import com.swust.kelab.mongo.dao.base.PageInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 查询基础类
 * <p>
 * 如果查询结果需要使用分页，则请设置{@link PageInfo},默认不使用分页查询
 */
public class BaseQuery implements Serializable {
    private static final long serialVersionUID = -2379671566690754406L;

    /**
     * 记录开始时间
     */
    private Date startDate;

    /**
     * 记录结束时间
     */
    private Date endDate;

    /**
     * 操作者名称
     */
    private String operatorName;

    /**
     * 分页信息
     */
    private PageInfo pageInfo;
    /**
     * 排序信息，注意add顺序
     */
    private LinkedHashMap<String, Integer> sortInfo = new LinkedHashMap<String, Integer>();

    /**
     * 模糊查询多个字段
     */
    private DBObject searchFields;

    /**
     * 模糊查询的value
     */
    private String searchValue;

    /**
     * 查询条件
     */
    private DBObject query;

    public DBObject getQuery() {
        return query;
    }

    public void setQuery(DBObject query) {
        this.query = query;
    }

    public DBObject getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(DBObject searchFields) {
        this.searchFields = searchFields;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    /**
     * 添加排序参数 注意：如果有多个排序条件，请把主排序条件先add,默认以创建时间倒序排列
     *
     * @param key
     * @param sort -1为倒序，1为正序
     */
    protected void addSortInfo(String key, Integer sort) {
        if (sort != 1 && sort != -1) {
            return;
        }

        sortInfo.put(key, sort);
    }

    public LinkedHashMap<String, Integer> getSortInfo() {
        return sortInfo;
    }
}
