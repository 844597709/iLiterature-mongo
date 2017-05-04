package com.swust.kelab.domain;

import org.ansj.splitWord.analysis.ToAnalysis;

public class Comment {
    public int workId;
    public String[] rawwords;
    public int[] wordFreqs;
    public String word;
    public int freq;
    public String path;

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }



    public void setContent(String content) {
    	String temp_words=ToAnalysis.parse(content).toString().replaceAll("[a-zA-Z0-9]", "");
    	temp_words=temp_words.replace('[', ' ').replace(']', ' ').replace('(', ' ');
    	temp_words=temp_words.replace(')', ' ').replace('-', ' ').replace(',', ' ');
    	temp_words=temp_words.replace('。', ' ').replace('！', ' ').replace('!', ' ');
    	temp_words=temp_words.replace('，', ' ').replace('《', ' ').replace('》', ' ');
    	temp_words=temp_words.replace('/', ' ').replace('？', ' ').replace('.', ' ').replace('—', ' ');
    	temp_words=temp_words.replace(':', ' ').replace('：', ' ').replace('、', ' ').replace('…', ' ');
    	this.rawwords=temp_words.split("\\s+");
    }

}
