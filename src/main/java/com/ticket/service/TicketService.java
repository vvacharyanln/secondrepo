package com.ticket.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.ticket.ModelClasss;
import com.ticket.RaisedOn;
import com.ticket.ResolvedOn;
import com.ticket.Resolvedall;
import com.ticket.Workload;
import com.ticket.entity.Ticket;
import com.ticket.repo.TicketRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.repo.ResourceInfo;

@Service
public class TicketService  {
	@Autowired
	private TicketRepository ticketRepository;
	
	List<Ticket> list=new ArrayList();

	public String exportReport(String reportFormat) throws FileNotFoundException, JRException {

		String path="C:\\Users\\DELL\\Desktop\\pdffiles";

		List<Ticket> ticket=ticketRepository.findAll();

		File file=ResourceUtils.getFile("C:\\Users\\DELL\\Documents\\workspace-spring-tool-suite-4-4.11.0.RELEASE\\ticket_bot\\src\\main\\resources\\t.jrxml");

		JasperReport jasperReport=JasperCompileManager.compileReport(file.getAbsolutePath());

		JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(ticket, false);

		Map<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("test", ticket);
	    parameters.put("DS1", ticket);

		JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, parameters,dataSource);
		if(reportFormat.equalsIgnoreCase("html")) {

			JasperExportManager.exportReportToHtmlFile(jasperPrint,"C:\\Users\\DELL\\Desktop\\pdffiles\\ticket.html");

		}
		if(reportFormat.equalsIgnoreCase("pdf")) {

			JasperExportManager.exportReportToPdfFile(jasperPrint,"C:\\Users\\DELL\\Desktop\\pdffiles\\ticket.pdf");

		}

		return "Report generated in path :"+path;
	}

	public TicketService() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	public TicketService(TicketRepository ticketRepository) {
		super();
		this.ticketRepository = ticketRepository;
	}

	public TicketRepository getTicketRepository() {
		return ticketRepository;
	}

	public void setTicketRepository(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}



	public List<Ticket> findAll() {

		return this.ticketRepository.findAll();
	}
	
	public Ticket updateTicket(Ticket ticket) {
		int ticketId=ticket.getTicket_id();
		Ticket t=ticketRepository.findById(ticketId).get();
		t.setResolver_id(ticketRepository.min());
		t.setRaised_by_id(ticket.getRaised_by_id());
		t.setType_of_issue(ticket.getType_of_issue());
		t.setResolved(ticket.isResolved());
		return ticketRepository.save(t);
		
	}

	public Optional<Ticket> findById(int ticket_id) {

		return this.ticketRepository.findById(ticket_id);
	}
	public void deleteById(int ticket_id) {
		this.ticketRepository.deleteById(ticket_id);
	}

	public List<Ticket>findAllOpenTicket(){
		return this.ticketRepository.findAllOpenTicket();
	}

	public List<Ticket>findAllClosedTicket(){
		return this.ticketRepository.findAllClosedTicket();
	}

	public List<Ticket>findResolvedByid(@Param("ticket") Ticket ticket){
		return this.ticketRepository.findResolvedByid(ticket);
	}

	public List<Ticket>findRaisedByid(@Param("ticket") Ticket ticket){
		return this.ticketRepository.findRaisedByid(ticket);
	}


	public Long countRaisedByid(@Param("raised_by_id") Ticket ticket){
		return this.ticketRepository.countRaisedByid(ticket);
	}

	public Long countResolvedByid(@Param("resolved_by_id") Ticket ticket){
		return this.ticketRepository.countResolvedByid(ticket);
	}

	public List<ModelClasss> countAllRaisedByid(){
		return this.ticketRepository.countAllRaisedByid();
	}

	public List<Resolvedall> countAllResolved(){
		return this.ticketRepository.countAllResolvedByid();
	}

	public List<RaisedOn> countAllResolvedOn(){
		return this.ticketRepository.countAllResolvedOn();
	}
	public List<ResolvedOn> countAllRaisedOn(){
		return this.ticketRepository.countAllRaisedOn();
	}

	public List<Ticket> findRaisedMonthAndYear(@Param("month")String month,@Param("year")int year){
		return this.ticketRepository.findRaisedMonthAndYear(month,year);
	}


	public List<Ticket> findResolvedMonthAndYear(@Param("month")String month,@Param("year")int year){
		return this.ticketRepository.findResolvedMonthAndYear(month,year);
	}

	public List<Ticket> findByResolvedOn(String date) {

		return this.ticketRepository.findByResolvedOn(date);
	}

	public List<Ticket> findByRaisedOn(String date) {

		return this.ticketRepository.findByRaisedOn(date);
	}

	public Ticket addTicket(Ticket ticket) {

		return this.ticketRepository.save(ticket);
	}

	public List<Workload> countWorkload(){
		return this.ticketRepository.countWorkload();
	}

	public Ticket addTicket1(Ticket ticket) {

		return this.ticketRepository.save(ticket);
	}

	public int min() {
		return this.ticketRepository.min();
	}

	public List<Ticket> findAnnualReport(@Param("year")int year){
		return this.ticketRepository.findAnnualReport(year);
	}

	public void updateTicket(Ticket ticket,int ticket_id) {
		list=list.stream().map(t->{
			if(t.getTicket_id()==ticket_id) {
				t.setRaised_by_id(ticket.getRaised_by_id());
				t.setType_of_issue(ticket.getType_of_issue());
				t.setRaised_on(ticket.getRaised_on());
				t.setResolved(ticket.isResolved());
				
				if(t.isResolved()) {
					t.setResolved_on(ticket.getResolved_on());
				}
				else {
					t.setResolved_on(null);
				}
			}
			
			return t;
		}).collect(Collectors.toList());
	}
}
