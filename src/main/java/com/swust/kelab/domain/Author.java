package com.swust.kelab.domain;

public class Author {
	public int authorId;
	public int wesiId;
	public String wesiName;
	public String authorName;
	public String url;
	public String gender;
	public String area;
	public String description;
	public String inTime;
	public int worksCount;
	public long totalHits;
	public long commentsNum;
	public long totalRecoms;

	public String getWesiName() {
		return wesiName;
	}

	public void setWesiName(String wesiName) {
		this.wesiName = wesiName;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public int getWesiId() {
		return wesiId;
	}

	public void setWesiId(int wesiId) {
		this.wesiId = wesiId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWorksCount() {
		return worksCount;
	}

	public void setWorksCount(int worksCount) {
		this.worksCount = worksCount;
	}

	public long getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(long totalHits) {
		this.totalHits = totalHits;
	}

	public long getCommentsNum() {
		return commentsNum;
	}

	public void setCommentsNum(long commentsNum) {
		this.commentsNum = commentsNum;
	}

	public long getTotalRecoms() {
		return totalRecoms;
	}

	public void setTotalRecoms(long totalRecoms) {
		this.totalRecoms = totalRecoms;
	}

	/**
	 * 获取该作者所在省份名称
	 * 
	 * @return 作者所在省份名称
	 */
	public String getAreaProvince() {
		if (area == null || area.length() == 0)
			return null;
		String province = null;
		if (wesiId == 1) {
			int a = area.indexOf(" ");
			if (a == -1)
				return null;
			province = area.substring(0, a);
		} else if (wesiId == 2) {
			province = null;// 该网站的描述里面几乎没有提到作者家乡且省市难分
		}

		if (province == null || province.length() == 0)
			return null;
		int end = province.indexOf("省");
		if (end != -1) {
			return province.substring(0, end);
		}
		end = province.indexOf("市");
		if (end != -1) {
			return province.substring(0, end);
		}
		return province;
	}

	public long getNum(int type) {

		if (type == 1)
			return totalHits;
		if (type == 2)
			return commentsNum;
		if (type == 3)
			return totalRecoms;
		if (type == 4)
			return worksCount;
		return -1;

		/*
		 * long num = 0; if (type == 1) num = totalHits; else if (type == 2) num
		 * = commentsNum; else if (type == 3) num = totalRecoms; else if (type
		 * == 4) num = worksCount; if (num < 0) return 0; return num;
		 */
	}
}
