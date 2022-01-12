package com.ticket;

import java.util.Date;

public class ResolvedOn {
	
	Date raised_on;

	long Raised_times;

	public ResolvedOn() {
		// TODO Auto-generated constructor stub
	}

	public ResolvedOn(Date raised_on, long raised_times) {
		super();
		this.raised_on = raised_on;
		Raised_times = raised_times;
	}

	public Date getRaised_on() {
		return raised_on;
	}

	public void setRaised_on(Date raised_on) {
		this.raised_on = raised_on;
	}

	public long getRaised_times() {
		return Raised_times;
	}

	public void setRaised_times(long raised_times) {
		Raised_times = raised_times;
	}
	

}
