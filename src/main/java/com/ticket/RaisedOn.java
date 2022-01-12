package com.ticket;

import java.util.Date;

public class RaisedOn {
	
	Date resolved_on;
	
	long Raised_times;

	public RaisedOn() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RaisedOn(Date resolved_on, long raised_times) {
		super();
		this.resolved_on = resolved_on;
		Raised_times = raised_times;
	}

	public Date getResolved_on() {
		return resolved_on;
	}

	public void setResolved_on(Date resolved_on) {
		this.resolved_on = resolved_on;
	}

	public long getRaised_times() {
		return Raised_times;
	}

	public void setRaised_times(long raised_times) {
		Raised_times = raised_times;
	}

	
}
