package com.ticket;

public class Resolvedall {
	
	Integer resolved_by_id;

	Long Resolved_times;

	
	public Resolvedall(Integer resolved_by_id, Long resolved_times) {
		super();
		this.resolved_by_id = resolved_by_id;
		Resolved_times = resolved_times;
	}

	public Integer getResolved_by_id() {
		return resolved_by_id;
	}

	public void setResolved_by_id(Integer resolved_by_id) {
		this.resolved_by_id = resolved_by_id;
	}

	public Long getResolved_times() {
		return Resolved_times;
	}





	public void setResolved_times(Long resolved_times) {
		Resolved_times = resolved_times;
	}





	public Resolvedall() {
		// TODO Auto-generated constructor stub
	}

}
