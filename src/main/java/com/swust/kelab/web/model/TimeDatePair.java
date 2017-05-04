package com.swust.kelab.web.model;

public class TimeDatePair {
	
	public String wouptime;
	public String data;
	
	public TimeDatePair(String wouptime,String data){
		this.wouptime = wouptime;
		this.data = data;
	}
	
	public String getWouptime() {
		return wouptime;
	}
	public void setWouptime(String wouptime) {
		this.wouptime = wouptime;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}
