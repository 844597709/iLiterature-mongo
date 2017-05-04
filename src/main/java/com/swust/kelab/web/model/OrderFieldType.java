package com.swust.kelab.web.model;

public enum OrderFieldType {
	SENTIMENT(1, "web_stat_sentiment", "asc"), PUBDATE(2, "web_pub_date", "desc"), CRAWLDATE(3, "web_crawl_date",
			"desc"), HOT(4, "web_stat_hot", "desc"), RELATION(5, "web_stat_rela", "desc");
	private int no;
	private String field;
	private String order;

	private OrderFieldType(int no, String field, String order) {
		this.no = no;
		this.field = field;
		this.order = order;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public static OrderFieldType codeOf(int no) {
		for (OrderFieldType fieldType : OrderFieldType.values()) {
			if (fieldType.getNo() == no) {
				return fieldType;
			}
		}
		return null;
	}
}