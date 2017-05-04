package com.swust.kelab.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 引擎总数，内容更新的引擎数量 主题数量，内容更新的主题数量 舆情总数 重点关注，比较关注，一般关注的舆情数量 预警舆情数量 负面舆情数量
 * 
 * @author longlongchang
 * 
 */
public class EPOSumStatistic {
    private int metaSearchCount;// 引擎总数
    private int updateMetaSearchCount;// 更新的引擎数量
    private int topicCount;// 主题总数
    private int updateTopicCount;// 更新的主题数量
    private int totalCount;// 舆情总数
    private int attSpecialCount;// 特别关注舆情数量
    private int attComparCount;// 比较关注舆情数量
    private int attGeneralCount;// 一般关注舆情数量
    private int negCount;// 负面舆情数量
    private int warningCount;// 预警舆情数量

    public int getMetaSearchCount() {
        return metaSearchCount;
    }

    public void setMetaSearchCount(int metaSearchCount) {
        this.metaSearchCount = metaSearchCount;
    }

    public int getUpdateMetaSearchCount() {
        return updateMetaSearchCount;
    }

    public void setUpdateMetaSearchCount(int updateMetaSearchCount) {
        this.updateMetaSearchCount = updateMetaSearchCount;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }

    public int getUpdateTopicCount() {
        return updateTopicCount;
    }

    public void setUpdateTopicCount(int updateTopicCount) {
        this.updateTopicCount = updateTopicCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getAttSpecialCount() {
        return attSpecialCount;
    }

    public void setAttSpecialCount(int attSpecialCount) {
        this.attSpecialCount = attSpecialCount;
    }

    public int getAttComparCount() {
        return attComparCount;
    }

    public void setAttComparCount(int attComparCount) {
        this.attComparCount = attComparCount;
    }

    public int getAttGeneralCount() {
        return attGeneralCount;
    }

    public void setAttGeneralCount(int attGeneralCount) {
        this.attGeneralCount = attGeneralCount;
    }

    public int getNegCount() {
        return negCount;
    }

    public void setNegCount(int negCount) {
        this.negCount = negCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
    }

}
