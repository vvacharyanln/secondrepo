package com.ticket.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ticket.ModelClasss;
import com.ticket.RaisedOn;
import com.ticket.ResolvedOn;
import com.ticket.Resolvedall;
import com.ticket.Workload;
import com.ticket.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Integer> {

@Query(value="SELECT * FROM Ticket ticket WHERE ticket.resolved=1",nativeQuery=true) 
List<Ticket>findAllOpenTicket();

@Query(value="SELECT * FROM Ticket ticket WHERE ticket.resolved=0",nativeQuery=true) 
List<Ticket>findAllClosedTicket();

@Query(value="SELECT ticket FROM Ticket ticket WHERE ticket.resolver_id=?#{#ticket.resolver_id} AND ticket.resolved=1")
List<Ticket>findResolvedByid(@Param("ticket") Ticket ticket);

@Query(value="SELECT ticket FROM Ticket ticket WHERE ticket.raised_by_id=?#{#ticket.raised_by_id}")
List<Ticket>findRaisedByid(@Param("ticket") Ticket ticket);

@Query(value="SELECT count(ticket) FROM Ticket ticket WHERE ticket.raised_by_id=?#{#ticket.raised_by_id}")
Long countRaisedByid(@Param("ticket") Ticket ticket);

@Query(value="SELECT count(ticket) FROM Ticket ticket WHERE ticket.resolver_id=?#{#ticket.resolver_id} AND ticket.resolved=1")
Long countResolvedByid(@Param("ticket") Ticket ticket);


@Query(value="SELECT new com.ticket.ModelClasss(ticket.raised_by_id, COUNT(ticket.raised_by_id) AS Raised_times) FROM Ticket ticket group by raised_by_id")
List<ModelClasss> countAllRaisedByid();

@Query(value="SELECT new com.ticket.Resolvedall(ticket.resolver_id, COUNT(ticket.resolver_id) AS Resolved_times) FROM Ticket ticket group by resolver_id")
List<Resolvedall> countAllResolvedByid();

@Query(value="SELECT new com.ticket.Workload(ticket.resolver_id,COUNT(ticket.resolver_id) AS open_tickets) FROM Ticket ticket where ticket.resolved=0 group by resolver_id order by count(ticket.resolved) asc")
List<Workload> countWorkload();

@Query(value="SELECT * FROM ticket WHERE monthname(raised_on)=:month AND year(raised_on)=:year AND ticket.resolved=1",nativeQuery=true)
List<Ticket> findRaisedMonthAndYear(@Param("month")String month,@Param("year")int year);

@Query(value="SELECT * FROM ticket WHERE monthname(resolved_on)=:month AND year(resolved_on)=:year AND ticket.resolved=1",nativeQuery=true)
List<Ticket> findResolvedMonthAndYear(@Param("month")String month,@Param("year")int year);

@Query(value="SELECT * FROM ticket WHERE resolved_on=:date ",nativeQuery=true)
List<Ticket> findByResolvedOn(@Param("date") String date);

@Query(value="SELECT * FROM ticket WHERE raised_on=:date ",nativeQuery=true)
List<Ticket> findByRaisedOn(@Param("date") String date);

@Query(value="SELECT new com.ticket.RaisedOn(ticket.resolved_on, COUNT(ticket.resolved_on) AS Resolved_times) FROM Ticket ticket group by resolved_on")
List<RaisedOn> countAllResolvedOn();

@Query(value="SELECT new com.ticket.ResolvedOn(ticket.raised_on, COUNT(ticket.raised_on) AS Raised_times) FROM Ticket ticket group by raised_on")
List<ResolvedOn> countAllRaisedOn();

@Query(value="SELECT ticket.resolver_id FROM Ticket ticket where ticket.resolved=0 group by ticket.resolver_id order by count(resolved) limit 1",nativeQuery=true)
int min();

@Query(value="SELECT * FROM ticket WHERE year(raised_on)=:year",nativeQuery=true)
List<Ticket> findAnnualReport(@Param("year")int year);

Ticket save(Optional<Ticket> ticket);

}
