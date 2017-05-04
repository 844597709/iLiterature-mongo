package com.swust.kelab.web.model;

import java.util.Date;

import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;

public class EPOQuery {
	private Integer sentiment;
	private Integer attentionLevel;
	private Integer topic;
	private Integer metaSearch;
	private int recentDays;// 显示最近N天
	private Date startTime;
	private Date endTime;
	private OrderFieldType orderField; // 排序依据的字段
	private int[] pageArray; // 分页显示时页数
	private int recordPerPage; // 分页显示时每页限制条数
	private Integer warn;
	private Integer searchType;// 搜索字段
	private String searchWord;// 搜索的关键字
	public int UserId;// 搜索时用户Id
	private Date collectDate;
	private String collectDetail;// 收藏的具体的内容
	private int collectFlag;// 标志是否编辑
	private int chooseEdit;// 判定显示编辑与否

	public int getChooseEdit() {
		return chooseEdit;
	}

	public void setChooseEdit(int chooseEdit) {
		this.chooseEdit = chooseEdit;
	}

	public String getCollectDetail() {
		return collectDetail;
	}

	public void setCollectDetail(String collectDetail) {
		this.collectDetail = collectDetail;
	}

	public int getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(int collectFlag) {
		this.collectFlag = collectFlag;
	}

	public Date getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public OrderFieldType getOrderField() {
		return orderField;
	}

	public void setOrderField(OrderFieldType orderField) {
		this.orderField = orderField;
	}

	public Integer getWarn() {
		return warn;
	}

	public void setWarn(Integer warn) {
		this.warn = warn;
	}

	public Integer getSentiment() {
		return sentiment;
	}

	public void setSentiment(Integer sentiment) {
		this.sentiment = sentiment;
	}

	public void setAttentionLevel(Integer attentionLevel) {
		this.attentionLevel = attentionLevel;
	}

	public void setTopic(Integer topic) {
		this.topic = topic;
	}

	public void setMetaSearch(Integer metaSearch) {
		this.metaSearch = metaSearch;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}

	public Integer getAttentionLevel() {
		return attentionLevel;
	}

	public void setAttentionLevel(int attentionLevel) {
		this.attentionLevel = attentionLevel;
	}

	public Integer getTopic() {
		return topic;
	}

	public void setTopic(int topic) {
		this.topic = topic;
	}

	public Integer getMetaSearch() {
		return metaSearch;
	}

	public void setMetaSearch(int metaSearch) {
		this.metaSearch = metaSearch;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int[] getPageArray() {
		return pageArray;
	}

	public void setPageArray(int[] pageArray) {
		this.pageArray = pageArray;
	}

	public int getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public int getRecentDays() {
		return recentDays;
	}

	public void setRecentDays(int recentDays) {
		this.recentDays = recentDays;
	}

	public ListQuery format() {
		ListQuery query = new GenericQuery();
		if (this.getSentiment() != null && this.getSentiment() >= -1) {
			query.fill("sentiment", this.getSentiment());
		}
		if (this.getAttentionLevel() != null && this.getAttentionLevel() > 0) {
			query.fill("attentionLevel", this.getAttentionLevel());
		}
		if (this.getMetaSearch() != null && this.getMetaSearch() > 0) {
			query.fill("metaSearch", this.getMetaSearch());
		}
		if (this.getTopic() != null && this.getTopic() > 0) {
			query.fill("topic", this.getTopic());
		}
		if (this.getStartTime() != null) {
			query.fill("startTime", this.getStartTime());
		}
		if (this.getEndTime() != null) {
			query.fill("endTime", this.getEndTime());
		}
		if (this.getCollectDate() != null) {
			query.fill("collectDate", this.getCollectDate());
		}
		if (this.getWarn() != null) {
			query.fill("warn", this.getWarn());
		}
		if (this.getSearchType() != null) {
			query.fill("searchType", searchType);
		}
		if (this.getUserId() != 0) {
			query.fill("userId", this.getUserId());
		}
		if (this.getSearchWord() != null) {
			query.fill("searchWord", searchWord);
		}
		if (this.getOrderField() != null) {
			query.fill("field", orderField.getField());
			query.fill("order", orderField.getOrder());
		}
		if (this.getChooseEdit() >= 0) {
			query.fill("chooseEdit", this.getChooseEdit());
		}
		return query;
	}

	public ListQuery weiboFormat() {
		ListQuery query = new GenericQuery();
		if (this.getSentiment() != null && this.getSentiment() >= -1) {
			query.fill("sentiment", this.getSentiment());
		}
		if (this.getAttentionLevel() != null && this.getAttentionLevel() > 0) {
			query.fill("attentionLevel", this.getAttentionLevel());
		}
		if (this.getMetaSearch() != null && this.getMetaSearch() > 0) {
			query.fill("metaSearch", this.getMetaSearch());
		}
		if (this.getTopic() != null && this.getTopic() > 0) {
			query.fill("topic", this.getTopic());
		}
		if (this.getStartTime() != null) {
			query.fill("startTime", this.getStartTime());
		}
		if (this.getEndTime() != null) {
			query.fill("endTime", this.getEndTime());
		}
		if (this.getCollectDate() != null) {
			query.fill("collectDate", this.getCollectDate());
		}
		if (this.getWarn() != null) {
			query.fill("warn", this.getWarn());
		}
		if (this.getSearchType() != null) {
			query.fill("searchType", searchType);
		}
		if (this.getUserId() != 0) {
			query.fill("userId", this.getUserId());
		}
		if (this.getSearchWord() != null) {
			query.fill("searchWord", searchWord);
		}
		return query;
	}

}
