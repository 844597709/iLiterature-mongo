package com.swust.kelab.mongo.dao.base;

import java.io.Serializable;

/**
 * 分页模型
 */
public class PageInfo implements Serializable {

    private static final long serialVersionUID = 5411611139257476208L;

    private static final int  PAGE_SIZE        = 20;

    /**
     * 每页的数据条数
     */
    private Integer           pageSize         = PAGE_SIZE;

    /**
     * 当前页数
     */
    private Integer           currentPage      = 1;

    /**
     * 数据总数
     */
    private Integer           total            = 0;

    /**
     * 
     * @param currentPage 当前页
     * @param pageSize 页码大小
     */
    public PageInfo(Integer currentPage, Integer pageSize) {
        setCurrentPage(currentPage);
        setPageSize(pageSize);
    }

    public PageInfo(Integer currentPage) {
        setCurrentPage(currentPage);
    }

    public PageInfo() {
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = (currentPage == null || currentPage < 1) ? 1 : currentPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
