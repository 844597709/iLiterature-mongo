package com.swust.kelab.domain;

import java.util.HashMap;
import java.util.Map;


public class Attr {
	
	public String parameterName;
	public Map<String, String> timeDataPair;
	
	public Attr(){
		parameterName = "";
		timeDataPair = new HashMap<String, String>();
	}
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	public Map<String, String> getTimeDataPair() {
		return timeDataPair;
	}

	public void setTimeDataPair(Map<String, String> timeDataPair) {
		this.timeDataPair = timeDataPair;
	}

	public void fillTimeDataPair(String wouptime,String attr){
		timeDataPair.put(wouptime, attr);
		
	}

}
