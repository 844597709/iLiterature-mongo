package com.swust.kelab.service.preprocess.parser;


public class HtmlModel {
	private String tagName;
	private String attrName;
	private String value;
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public HtmlModel(String tag,String attr,String val)
	{
		this.tagName=tag;
		this.attrName=attr;
		this.value=val;
	}
}
