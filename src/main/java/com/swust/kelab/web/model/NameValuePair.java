package com.swust.kelab.web.model;

public class NameValuePair {

	private String name;
	private String value;
	private String value2;
	public NameValuePair(String name,String value){
		this.name=name;
		this.value=value;
	}
	
	public NameValuePair(String name,String value,String value2){
		this.name=name;
		this.value=value;
		this.value2=value2;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
}
