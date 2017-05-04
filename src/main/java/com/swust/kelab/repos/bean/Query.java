package com.swust.kelab.repos.bean;

import java.util.List;
import java.util.Map;

public interface Query extends Map<Object, Object> {

    /**
     * 获取排序规则
     * 
     * @return
     */
    List<Sort> getSorts();

    /**
     * 添加排序规则
     * 
     * @param sort
     */
    Query addSort(Sort sort);

    /**
     * 新增参数
     * 
     * @param key 参数名
     * @param value 参数值
     * @return
     */
    Query fill(String key, Object value);
}
