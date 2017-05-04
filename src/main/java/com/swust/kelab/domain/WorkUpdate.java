package com.swust.kelab.domain;

import java.util.HashMap;
import java.util.Map;

public class WorkUpdate {
	
	public int workID;
	public int websiteID;
	public String wouptime;
	public Map<String, String> paraMap;
	public String parameter;
	public String attr1;
	public String attr2;
	public String attr3;
	public String attr4;
	public String attr5;
	public String attr6;
	public String attr7;
	public String attr8;
	public String attr9;
	public String getAttr1() {
		return attr1;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public String getAttr2() {
		return attr2;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public String getAttr3() {
		return attr3;
	}
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	public String getAttr4() {
		return attr4;
	}
	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}
	public String getAttr5() {
		return attr5;
	}
	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}
	public String getAttr6() {
	
		return attr6;
	}
	public void setAttr6(String attr6) {
		this.attr6 = attr6;
	}
	public String getAttr7() {
	
		return attr7;
	}
	public void setAttr7(String attr7) {
		this.attr7 = attr7;
	}
	public String getAttr8() {
		
		return attr8;
	}
	public void setAttr8(String attr8) {
		this.attr8 = attr8;
	}
	public String getAttr9() {
		
		return attr9;
	}
	public void setAttr9(String attr9) {
		this.attr9 = attr9;
	}
	public int getWorkID() {
		return workID;
	}
	public void setWorkID(int workID) {
		this.workID = workID;
	}
	public String getWouptime() {
		return wouptime;
	}
	public void setWouptime(String wouptime) {
		this.wouptime = wouptime;
	}
	public Map<String, String> getParaMap() {
		return paraMap;
	}
	public void setParaMap(Map<String, String> paraMap) {
		this.paraMap = paraMap;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public int getWebsiteID() {
		return websiteID;
	}
	public void setWebsiteID(int websiteID) {
		this.websiteID = websiteID;
	}
	public void fillMap(String[] para){
		this.paraMap = new HashMap<String, String>();
		this.paraMap.put(para[0], attr1);
		this.paraMap.put(para[1], attr2);
		this.paraMap.put(para[2], attr3);
		this.paraMap.put(para[3], attr4);
		this.paraMap.put(para[4], attr5);
		this.paraMap.put(para[5], attr6);
		this.paraMap.put(para[6], attr7);
		this.paraMap.put(para[7], attr8);
		this.paraMap.put(para[8], attr9);
	}
}
