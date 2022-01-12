package com.ticket;

public class Workload {
	
	int resolved_by_id;
	
	long open_tickets;
	

	public Workload() {
		// TODO Auto-generated constructor stub
	}


	public Workload(int resolved_by_id, long open_tickets) {
		super();
		this.resolved_by_id = resolved_by_id;
		this.open_tickets = open_tickets;
	}


	public int getResolved_by_id() {
		return resolved_by_id;
	}


	public void setResolved_by_id(int resolved_by_id) {
		this.resolved_by_id = resolved_by_id;
	}


	public long getOpen_tickets() {
		return open_tickets;
	}


	public void setOpen_tickets(long open_tickets) {
		this.open_tickets = open_tickets;
	}

	
	

}
