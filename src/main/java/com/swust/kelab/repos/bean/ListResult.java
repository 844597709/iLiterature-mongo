package com.swust.kelab.repos.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ListResult<T> implements Serializable {

    private static final long serialVersionUID = 8094453137329730867L;

    /** 列表数据 */
    private List<T> list;
    /** 总数据量 */
    private int totalCount;

    public ListResult(List<T> list) {
        this.list = (list != null) ? list : new LinkedList<T>();
    }

    public ListResult(List<T> list, int totalCount) {
        this(list);
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
