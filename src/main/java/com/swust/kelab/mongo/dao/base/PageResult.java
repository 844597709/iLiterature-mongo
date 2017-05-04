package com.swust.kelab.mongo.dao.base;

import java.io.Serializable;
import java.util.List;

/**
 * 结果模型:如果查询不分页，则{@link PageInfo}对象为空
 *
 */
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -3702567577801828263L;

    /**
     * 分页信息
     */
    private PageInfo          pageInfo;

    /**
     * 数据列表
     */
    private List<T>           contents;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }

}
