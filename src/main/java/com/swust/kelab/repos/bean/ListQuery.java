package com.swust.kelab.repos.bean;

public interface ListQuery extends Query {

    /**
     * 获取查询起始点
     * 
     * @return
     */
    public Integer getStartIndex();

    /**
     * 获取查询结果条数限制
     * 
     * @return
     */
    public Integer getMaxCount();

    public void setStartIndex(int startIndex);

    public void setMaxCount(int maxCount);
}
