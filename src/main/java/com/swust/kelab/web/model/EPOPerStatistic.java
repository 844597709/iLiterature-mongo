package com.swust.kelab.web.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.EPOStatResult;

/**
 * 百分比格式的统计数据（主题、引擎）
 * 
 * @author longlongchang
 * 
 */
public class EPOPerStatistic {
    private int id;// 主题或者引擎的id
    private String indicator;// 统计指标
    private int count;// 数量

    public EPOPerStatistic() {
    }

    public EPOPerStatistic(int id, String indicator, int count) {
        this.id = id;
        this.indicator = indicator;
        this.count = count;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<EPOPerStatistic> format(List<EPOStatResult> results, int topNum) {
        List<EPOPerStatistic> perResultList = Lists.newArrayList();
        if (results == null || results.size() == 0) {
            return perResultList;
        }
        for (int i = 0; i < topNum && i < results.size(); i++) {
            EPOStatResult result = results.get(i);
            perResultList.add(new EPOPerStatistic(result.getId(), result.getIndicator(), result.getCount()));
        }
        return perResultList;
    }
}
