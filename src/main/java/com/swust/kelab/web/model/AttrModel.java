package com.swust.kelab.web.model;

public class AttrModel {
	
	private String attrName;
	private String[] attrValue;
	private String attrAverage;
	
	public String getAttrAverage() {
		return attrAverage;
	}

	public void setAttrAverage(String attrAverage) {
		this.attrAverage = attrAverage;
	}

	public AttrModel(int size){
		this.attrName = "";
		this.attrValue = new String[size];
		for(int i = 0 ; i < size ; i++){
			attrValue[i] = "";
		}
	}

	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String[] getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String[] attrValue) {
		this.attrValue = attrValue;
	}

}
