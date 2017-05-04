package com.swust.kelab.mongo.domain;

import com.swust.kelab.mongo.dao.query.BaseModel;

public class TempWorksComment extends BaseModel{
	private Integer wocoId;
	private Integer wocoWorkId;
	private Integer wocoParentId;
	private String wocoCritic;
	private String wocoTitle;
	private String wocoContent;
	private String wocoTime;

	public Integer getWocoId() {
		return wocoId;
	}

	public void setWocoId(Integer wocoId) {
		this.wocoId = wocoId;
	}

	public Integer getWocoWorkId() {
		return wocoWorkId;
	}

	public void setWocoWorkId(Integer wocoWorkId) {
		this.wocoWorkId = wocoWorkId;
	}

	public Integer getWocoParentId() {
		return wocoParentId;
	}

	public void setWocoParentId(Integer wocoParentId) {
		this.wocoParentId = wocoParentId;
	}

	public String getWocoCritic() {
		return wocoCritic;
	}

	public void setWocoCritic(String wocoCritic) {
		this.wocoCritic = wocoCritic;
	}

	public String getWocoTitle() {
		return wocoTitle;
	}

	public void setWocoTitle(String wocoTitle) {
		this.wocoTitle = wocoTitle;
	}

	public String getWocoContent() {
		return wocoContent;
	}

	public void setWocoContent(String wocoContent) {
		this.wocoContent = wocoContent;
	}

	public String getWocoTime() {
		return wocoTime;
	}

	public void setWocoTime(String wocoTime) {
		this.wocoTime = wocoTime;
	}
}
