package com.swust.kelab.web.model;

public class AuthorWorkUpdate {
	private Integer totalUpdateAuthors;
	private Integer totalUpdateWorks;
	private String time;
	
	public AuthorWorkUpdate(){
	}
	
	public AuthorWorkUpdate(Integer totalUpdateAuthors, Integer totalUpdateWorks, String time){
		this.setTotalUpdateAuthors(totalUpdateAuthors);
		this.setTotalUpdateWorks(totalUpdateWorks);
		this.setTime(time);
	}

	public Integer getTotalUpdateAuthors() {
		return totalUpdateAuthors;
	}

	public void setTotalUpdateAuthors(Integer totalUpdateAuthors) {
		this.totalUpdateAuthors = totalUpdateAuthors;
	}

	public Integer getTotalUpdateWorks() {
		return totalUpdateWorks;
	}

	public void setTotalUpdateWorks(Integer totalUpdateWorks) {
		this.totalUpdateWorks = totalUpdateWorks;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
