package com.swust.kelab.domain;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 篇章级别的类，包含若干句子，以及分析结果
 * 
 * @author longlongchang
 * 
 */
public class KEArticle {
    private String content;
    private List<KESentence> sentenceList;
    private Map<String, String> analysisResult;

    public KEArticle() {
        this.sentenceList = Lists.newArrayList();
        this.analysisResult = Maps.newHashMap();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<KESentence> getSentenceList() {
        if (sentenceList == null) {
            this.sentenceList = Lists.newArrayList();
        }
        return sentenceList;
    }

    public void setSentenceList(List<KESentence> sentenceList) {
        this.sentenceList = sentenceList;
    }

    public Map<String, String> getAnalysisResult() {
        if (analysisResult == null) {
            analysisResult = Maps.newHashMap();
        }
        return analysisResult;
    }

    public void setAnalysisResult(Map<String, String> analysisResult) {
        this.analysisResult = analysisResult;
    }
}
