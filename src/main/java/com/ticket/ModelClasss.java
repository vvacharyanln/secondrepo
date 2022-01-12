package com.ticket;

import java.sql.Date;

public class ModelClasss {
	int raised_by_id;
	
	long Raised_times;

	public ModelClasss() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ModelClasss(int raised_by_id, long raised_times) {
		super();
		this.raised_by_id = raised_by_id;
		Raised_times = raised_times;
	}

	public int getRaised_by_id() {
		return raised_by_id;
	}

	public void setRaised_by_id(int raised_by_id) {
		this.raised_by_id = raised_by_id;
	}

	public long getRaised_times() {
		return Raised_times;
	}

	public void setRaised_times(long raised_times) {
		Raised_times = raised_times;
	}

}
