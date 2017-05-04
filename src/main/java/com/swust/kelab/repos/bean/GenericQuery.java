package com.swust.kelab.repos.bean;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class GenericQuery extends LinkedHashMap<Object, Object> implements ListQuery {

    private static final long serialVersionUID = -3543245108645093168L;

    @SuppressWarnings("rawtypes")
    protected Class<? extends Enum> paramType;

    protected Integer startIndex;

    protected Integer maxCount;
    
    private static final String SORTS_KEY = "sorts";

    public GenericQuery() {
    }

    public GenericQuery(Integer startIndex, Integer maxCount) {
        this(null, startIndex, maxCount);
    }

    public GenericQuery(Class<? extends Enum<?>> paramType) {
        this(paramType, null, null);
    }

    public GenericQuery(Class<? extends Enum<?>> paramType, Integer startIndex, Integer maxCount) {
        this.startIndex = startIndex;
        this.maxCount = maxCount;
        this.paramType = paramType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {

        // 忽略无效参数
        if ("~".equals(key) || value == null) {
            return null;
        }

        // 排序参数不做检查
        if (SORTS_KEY.equals(key)) {
            return super.put(key, value);
        }

        // 检查参数有效性
        if (paramType != null) {
            Enum.valueOf(paramType, (String) key); // role
        }

        return super.put(key, value);
    }

    public GenericQuery fill(String key, Object value) {
        put(key, value);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Sort> getSorts() {

        List<Sort> sorts = (List<Sort>) get(SORTS_KEY);

        if (sorts == null) {
            sorts = new LinkedList<Sort>();
            super.put(SORTS_KEY, sorts);
        }

        return sorts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GenericQuery addSort(Sort sort) {

        if (paramType != null) {
            Enum.valueOf(paramType, (String) sort.getName());
        }

        getSorts().add(sort);
        return this;
    }

    @Override
    public Integer getMaxCount() {
        return maxCount;
    }

    @Override
    public Integer getStartIndex() {
        return startIndex;
    }

    @Override
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
