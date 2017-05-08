package com.swust.kelab.mongo.domain.model;

import com.swust.kelab.mongo.dao.query.BaseModel;

public class Area extends BaseModel{

	public Area() {
	}
	public Area(String name, int value) {
		this.value = value;
		this.name = name;
	}

	private String name;
	private int value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int compareTo(Area area){
		return (this.getValue() > area.getValue()) ? -1 : ((this.getValue() == area.getValue()) ? 0 : 1);
	}
}
