package com.swust.kelab.domain;

public class KeyWord {

	private int keywordId;//关键词id
	private String keywName;
	private String keywSyno;//该关键词对应的同义词
    private int keywInorex;//判断该关键词属于inkey还是exkey
    
	public KeyWord(){}
    public KeyWord(String keywName,String keywSyno,int keywInorex)
    {
    	this.keywName=keywName;
    	this.keywSyno=keywSyno;
    	this.keywInorex=keywInorex;
    }
    public String getKeywName() {
		return keywName;
	}
	public void setKeywName(String keywName) {
		this.keywName = keywName;
	}
	public String getKeywSyno() {
		return keywSyno;
	}
	public void setKeywSyno(String keywSyno) {
		this.keywSyno = keywSyno;
	}
	public int getKeywInorex() {
		return keywInorex;
	}
	public void setKeywInorex(int keywInorex) {
		this.keywInorex = keywInorex;
	}

	public int getKeywordId() {
		return keywordId;
	}
	public void setKeywordId(int keywordId) {
		this.keywordId = keywordId;
	}
	
}
