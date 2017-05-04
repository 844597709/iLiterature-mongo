package com.swust.kelab.service.analysis;

import java.util.Map;

import com.swust.kelab.domain.KEArticle;

/**
 * 该接口定义了语言智能分析的功能
 * @author longlongchang
 *
 */
public interface NLPAnalysis {
    /**
     * 通过智能分析内容获取相关分析结果
     * @param content 分析的内容
     * @return 返回的结果，key为结果的说明，value为结果的值
     */
    public Map<String,String> analysis(String content);
    
    /**
     * 对一个篇章进行分析，分析结果直接设置在篇章的属性analysisResult中
     * @param aritcle
     */
    public void analysis(KEArticle aritcle);
}
