package com.swust.kelab.web.model;

import java.util.List;

public class QueryData {
    private int totalPage = 0;// 总页数
    private int totalCount = 0;// 总记录数
    private List<PageData> pageData = null;// 所有页的数据

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<PageData> getPageData() {
        return pageData;
    }

    public void setPageData(List<PageData> pageData) {
        this.pageData = pageData;
    }

    public static int computeTotalPage(int totalCount, int recordPerPage) {
        if (recordPerPage == 0) {
            return 0;
        }
        int totalPage = totalCount / recordPerPage;
        if (totalCount % recordPerPage != 0) {
            totalPage++;
        }
        return totalPage;
    }

    public static int computeStartIndex(int page, Integer maxCount) {
        return (page - 1) * maxCount;
    }
}
