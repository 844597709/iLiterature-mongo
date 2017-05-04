package com.swust.kelab.mongo.domain;

import com.swust.kelab.mongo.dao.query.BaseModel;

public class TempAuthor extends BaseModel{
	private Integer authId;
	private Integer authWebsiteId;
	private String authUrl;
	private String authName;
	private String authGender;
	private String authArea;
	private String authDesc;
	private String authInTime;
	private Integer authWorksNum;

	public Integer getAuthId() {
		return authId;
	}

	public void setAuthId(Integer authId) {
		this.authId = authId;
	}

	public Integer getAuthWebsiteId() {
		return authWebsiteId;
	}

	public void setAuthWebsiteId(Integer authWebsiteId) {
		this.authWebsiteId = authWebsiteId;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getAuthGender() {
		return authGender;
	}

	public void setAuthGender(String authGender) {
		this.authGender = authGender;
	}

	public String getAuthArea() {
		return authArea;
	}

	public void setAuthArea(String authArea) {
		this.authArea = authArea;
	}

	public String getAuthDesc() {
		return authDesc;
	}

	public void setAuthDesc(String authDesc) {
		this.authDesc = authDesc;
	}

	public String getAuthInTime() {
		return authInTime;
	}

	public void setAuthInTime(String authInTime) {
		this.authInTime = authInTime;
	}

	public Integer getAuthWorksNum() {
		return authWorksNum;
	}

	public void setAuthWorksNum(Integer authWorksNum) {
		this.authWorksNum = authWorksNum;
	}
}
