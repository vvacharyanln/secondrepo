package com.ticket.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.mapping.Collection;
import org.hibernate.query.internal.CollectionFilterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.codec.Base64.InputStream;
import com.sun.xml.txw2.Document;
import com.ticket.ModelClasss;
import com.ticket.RaisedOn;
import com.ticket.ResolvedOn;
import com.ticket.Resolvedall;
import com.ticket.Workload;
import com.ticket.entity.Ticket;
import com.ticket.repo.TicketRepository;
import com.ticket.service.TicketService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins="*")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@Autowired
	private TicketRepository ticketRepository;

	public void setAutoCommit (boolean autoCommit) {
		autoCommit=true;
	}
	final Logger log = LoggerFactory.getLogger(this.getClass());
	final ModelAndView model = new ModelAndView();

	@GetMapping("/report/{format}")
	public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException {
		return ticketService.exportReport(format);
	}

	ObjectMapper mapper = new ObjectMapper();


	public TicketController(TicketService ticketService) {
		super();
		this.ticketService = ticketService;
	}

	public TicketService getTicketService() {
		return ticketService;
	}

	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping("/findall")
	public String findAll() throws JsonProcessingException{
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.ticketService.findAll());
	}

	@GetMapping(value= "/welcome")
	public ModelAndView index() {
		log.info("Showing the welcome page.");
		model.setViewName("welcome");
		return model;
	}
	@GetMapping(value = "/view")
	public ModelAndView viewReport() {
		log.info("Preparing the pdf report via jasper.");
		try {
			createPdfReport(ticketService.findAll());
			log.info("File successfully saved at the given path.");
		} catch (final Exception e) {
			log.error("Some error has occurred while preparing the employee pdf report.");
			e.printStackTrace();
		}

		model.setViewName("welcome");
		return model;
	}

	private void createPdfReport(final List<Ticket> ticket) throws JRException {

		final java.io.InputStream stream = this.getClass().getResourceAsStream("/ticketbot1.jrxml");


		final JasperReport report = JasperCompileManager.compileReport(stream);


		final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(ticket);


		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "Vishnu Acharya");


		final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);

		final String filePath = "\\";

		JasperExportManager.exportReportToPdfFile(print, filePath + "Ticket_report.pdf");
	}


	@GetMapping("/delete/{ticket_id}")
	public void deleteById(@PathVariable int ticket_id) {
		this.ticketService.deleteById(ticket_id);
	}
	/*@PostMapping("/find/{ticket_id}")
	public String updateTicket(@RequestBody Ticket ticket) throws JsonProcessingException {
		ticket.setResolver_id(ticketRepository.min());

		if(ticket.isResolved()) {
			ticket.setResolved_on(Date.valueOf(LocalDate.now().plusDays(2)));
		}
		else {
			ticket.setResolved_on(null);
		}
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticketService.updateTicket(ticket));
	}*/
	@GetMapping("/closed")
	public String findAllActiveTicketNative() throws JsonProcessingException{
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.ticketService.findAllOpenTicket());
	}

	@GetMapping("/open")
	public String findAllInActiveTicketNative() throws JsonProcessingException{
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.ticketService.findAllClosedTicket());
	}

	@GetMapping("resolvedby/{resolver_id}")
	public List<Ticket> findResolvedByid(@Param("ticket") Ticket ticket) throws JsonProcessingException{
		return this.ticketService.findResolvedByid(ticket);
	}
	@GetMapping("raisedby/{raised_by_id}")
	public String findRaisedByid(@Param("ticket") Ticket ticket) throws JsonProcessingException{
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.ticketService.findRaisedByid(ticket));
	}
	@GetMapping("countraisedby/{raised_by_id}")
	public HashMap<String,String> countRaisedByid(@Param("ticket") Ticket ticket) throws JsonProcessingException{

		long count= ticketService.countRaisedByid(ticket);

		HashMap<String,String> hm=new HashMap<String,String>();

		hm.put("Raised by id",""+ticket.raised_by_id);
		hm.put("Raised", count+" Times");
		return hm;
	}
	@GetMapping("/countraisedby")
	public  List<ModelClasss> countAllRaisedByid() throws JsonProcessingException{

		List<ModelClasss> count= ticketService.countAllRaisedByid();

		return count;
	}
	@GetMapping("/countresolvedby")
	public  List<Resolvedall> countAllResolvedByid() {

		List<Resolvedall> count= ticketService.countAllResolved();

		return count;
	}
	@GetMapping("/countresolvedon")
	public  List<RaisedOn> countAllResolvedOn() {

		List<RaisedOn> count= ticketService.countAllResolvedOn();

		return count;
	}

	@GetMapping("/countraisedon")
	public  List<ResolvedOn> countAllRaisedOn() {

		List<ResolvedOn> count= ticketService.countAllRaisedOn();

		return count;
	}
	@GetMapping("countresolvedby/{resolver_id}")
	public HashMap<String,String> countResolvedByid(@Param("ticket") Ticket ticket) throws JsonProcessingException{

		long count= ticketService.countResolvedByid(ticket);

		HashMap<String,String> hm=new HashMap<String,String>();

		hm.put("Resolved by id",""+ticket.resolver_id);
		hm.put("Resolved", count+" Times");
		return hm;
	}


	@GetMapping("/raised")
	public String findRaisedMonthAndYear(@RequestParam(value="month")String month,@RequestParam(value="year")int year) throws JsonProcessingException {

		List<Ticket> result =new ArrayList();

		result=ticketService.findRaisedMonthAndYear(month,year);

		System.out.println(result);

		if(!result.isEmpty()) {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
		}
		else { 
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString("No Data available for Month: "+month+" and Year : "+year);
		}
	}

	@GetMapping("/resolved")
	public String findResolvedMonthAndYear(@RequestParam(value="month")String month,@RequestParam(value="year")int year) throws JsonProcessingException{

		List<Ticket> result1 =new ArrayList();

		result1= ticketService.findResolvedMonthAndYear(month,year);

		if(!result1.isEmpty()) {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result1);
		}
		else {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString("No Data available for month: "+month+" and Year: "+year);
		}
	}	
	@GetMapping("/resolvedon")
	public String findByResolvedOn(@RequestParam(value="date") String date) throws JsonProcessingException{

		return  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.ticketService.findByResolvedOn(date));

	}

	@GetMapping("/raisedon")
	public String findByRaisedOn(@RequestParam(value="date") String date) throws JsonProcessingException{

		return  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticketService.findByRaisedOn(date));

	}


	@PostMapping("/newticket")
	public Map<String,String> findByTicketId(@RequestBody Ticket ticket){

		ticket.setResolver_id(ticketService.min());
		
		ticket.setRaised_on(ticket.getRaised_on());

		if(ticket.isResolved()) {
			ticket.setResolved_on(ticket.getResolved_on());
		}
		else {
			ticket.setResolved_on(null);
		}
		ticketService.addTicket(ticket);

		HashMap<String,String> hm=new HashMap<String,String>();

		Logger logger=LoggerFactory.getLogger(Ticket.class);

		if(ticketService.findById(ticket.ticket_id).isPresent()) {
			logger.info("Ticket added successfully");
			hm.put("Status", "0");
			hm.put("Message", "Ticket has been added successfully");
			return hm;
		}
		else {
			logger.info("Failed to add ticket");
			logger.error("Error has occurred");
			hm.put("Status", "1");
			hm.put("Message", "Failed to add ticket");
			hm.put("Error","Error has occurred");
			return hm;

		}

	}
	@PutMapping("/update/{ticket_id}")
	public Ticket updateTicket(@RequestBody Ticket ticket,@PathVariable int ticket_id) throws JsonProcessingException {
		this.ticketService.updateTicket(ticket,ticket_id);
		System.out.println(ticket);
		return  ticket;
	}
	@GetMapping("/find/{ticket_id}")
	public Optional<Ticket> findById(@PathVariable int ticket_id) throws JsonProcessingException{
		Optional<Ticket> ticket=this.ticketService.findById(ticket_id);
			return ticket;
	}


@GetMapping("/workload")
public  List<Workload> countWorkload() throws JsonProcessingException{

	List<Workload> count= ticketService.countWorkload();

	return count;
}
@GetMapping("/willberesolved")
public String willBeResolved() throws JsonProcessingException{
	return  mapper.writerWithDefaultPrettyPrinter().writeValueAsString("Your ticket will be Resolved by "+LocalDate.now().plusDays(2));
}


@PostMapping("/newticket1")
public Ticket findByTicketId1(@RequestBody Ticket ticket){

	ticket.setResolver_id(ticketService.min());

	if(ticket.isResolved()) {

		ticket.setResolved_on(ticket.getResolved_on());
	}
	else {

		ticket.setResolved_on(null);
	}

	ticketService.addTicket1(ticket);

	return ticket;

}

@GetMapping("/annual_report")
public String findAnnualReport(@RequestParam(value="year")int year) throws JsonProcessingException{

	List<Ticket> result =new ArrayList();

	result= ticketService.findAnnualReport(year);

	if(!result.isEmpty()) {

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
	}
	else {

		return "Message: No data available for year :"+year;

	}
}
@RequestMapping("/pdf")
public void getReportsinPDF(HttpServletResponse response) throws JRException, IOException {

	InputStream jasperStream =(InputStream) this.getClass().getResourceAsStream("/ticketbot1.jasper");

	Map<String,Object> params=new HashMap();

	params.put("ticket_id", "ticket_id");
	params.put("raised_by_id", "raised_by_id");
	params.put("type_of_issue", "type_of_issue");
	params.put("raised_on", "raised_on");
	params.put("resolved", "resolved");
	params.put("resolved_on", "resolved_on");
	params.put("resolver_id", "resolver_id");

	final JRBeanCollectionDataSource source=new  JRBeanCollectionDataSource(ticketService.findAll());

	JasperReport jasperReport=(JasperReport) JRLoader.loadObject(jasperStream);
	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,source);

	response.setContentType("application/x-pdf");
	response.setHeader("Content-disposition","inline; filename=ticketbot1.pdf");

	final ServletOutputStream outStream =response.getOutputStream();
	JasperExportManager.exportReportToPdfStream(jasperPrint,outStream);

}




}





