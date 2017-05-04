package com.swust.kelab.web.model;

public class AuthorWorkUpdate {
	private long totalUpdateAuthors;
	private long totalUpdateWorks;
	private String time;
	
	public AuthorWorkUpdate(){
	}
	
	public AuthorWorkUpdate(long totalUpdateAuthors,long totalUpdateWorks,String time){
		this.setTotalUpdateAuthors(totalUpdateAuthors);
		this.setTotalUpdateWorks(totalUpdateWorks);
		this.setTime(time);
	}

	public long getTotalUpdateAuthors() {
		return totalUpdateAuthors;
	}

	public void setTotalUpdateAuthors(long totalUpdateAuthors) {
		this.totalUpdateAuthors = totalUpdateAuthors;
	}

	public long getTotalUpdateWorks() {
		return totalUpdateWorks;
	}

	public void setTotalUpdateWorks(long totalUpdateWorks) {
		this.totalUpdateWorks = totalUpdateWorks;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
