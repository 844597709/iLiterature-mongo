package com.swust.kelab.domain;

import org.ansj.splitWord.analysis.ToAnalysis;

public class WorkDescription {
	public int workId;
	public int authorId;
	public String description;
	public String[] Words;
	public String word;
	
	public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    
    public int getworkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }
    
    public void setDescription(String description) {
    	String temp_words=ToAnalysis.parse(description).toString().replaceAll("[a-zA-Z0-9]", "");
    	temp_words=temp_words.replace('[', ' ').replace(']', ' ').replace('(', ' ');
    	temp_words=temp_words.replace(')', ' ').replace('-', ' ').replace(',', ' ');
    	temp_words=temp_words.replace('。', ' ').replace('！', ' ').replace('!', ' ');
    	temp_words=temp_words.replace('，', ' ').replace('《', ' ').replace('》', ' ');
    	temp_words=temp_words.replace('/', ' ').replace('？', ' ').replace('.', ' ').replace('—', ' ');
    	temp_words=temp_words.replace(':', ' ').replace('：', ' ').replace('、', ' ').replace('…', ' ');
    	this.Words=temp_words.split("\\s+");
    }
}
