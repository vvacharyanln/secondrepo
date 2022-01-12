package com.ticket.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
@Data
@Entity
@Table(name = "ticket")
public class Ticket {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ticket_id")
	public
	int ticket_id;
	
	@Column(name="raisedby_employee_id")
	public
	int raised_by_id;
	
	@Column(name="type_of_issue")
	String type_of_issue;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name="raised_on")
	Timestamp raised_on=Timestamp.valueOf(LocalDateTime.now());
	
	@Column(name="resolved")
	boolean resolved;
	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name="resolved_on")
	public
	Timestamp resolved_on;
	
	@Column(name="resolver_id")
	public
	int resolver_id;
	

	public Ticket() {
		// TODO Auto-generated constructor stub
	}


	public Ticket(int ticket_id, int raised_by_id, String type_of_issue, Timestamp raised_on, boolean resolved,
			Timestamp resolved_on, int resolver_id) {
		super();
		this.ticket_id = ticket_id;
		this.raised_by_id = raised_by_id;
		this.type_of_issue = type_of_issue;
		this.raised_on = raised_on;
		this.resolved = resolved;
		this.resolved_on = resolved_on;
		this.resolver_id = resolver_id;
	}


	public int getTicket_id() {
		return ticket_id;
	}


	public void setTicket_id(int ticket_id) {
		this.ticket_id = ticket_id;
	}


	public int getRaised_by_id() {
		return raised_by_id;
	}


	public void setRaised_by_id(int raised_by_id) {
		this.raised_by_id = raised_by_id;
	}


	public String getType_of_issue() {
		return type_of_issue;
	}


	public void setType_of_issue(String type_of_issue) {
		this.type_of_issue = type_of_issue;
	}


	public Timestamp getRaised_on() {
		return raised_on;
	}


	public void setRaised_on(Timestamp raised_on) {
		this.raised_on = raised_on;
	}


	public boolean isResolved() {
		return resolved;
	}


	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}


	public Timestamp getResolved_on() {
		return resolved_on;
	}


	public void setResolved_on(Timestamp resolved_on) {
		this.resolved_on = resolved_on;
	}


	public int getResolver_id() {
		return resolver_id;
	}


	public void setResolver_id(int resolver_id) {
		this.resolver_id = resolver_id;
	}


}
