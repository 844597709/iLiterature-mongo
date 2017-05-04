package com.swust.kelab.web.model;

public class TotalAttr {
	
	private String[] attrValue;
	
	public TotalAttr(int size){
		
		this.attrValue = new String[size];
		for(int i = 0 ; i < size ; i++){
			attrValue[i] = "";
		}
	}

	public String[] getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String[] attrValue) {
		this.attrValue = attrValue;
	}

}
