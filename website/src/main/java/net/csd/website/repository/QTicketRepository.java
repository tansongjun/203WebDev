package net.csd.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// import net.csd.website.model.Patient; // deprecated
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;

@Repository
public interface QTicketRepository extends JpaRepository<QTicket, Long>{
    List<QTicket> findByPerson(Person person);

    @Query("SELECT qt FROM QTicket qt WHERE (qt.person.id, qt.ticketno) IN (SELECT qt2.person.id, MAX(qt2.ticketno) FROM QTicket qt2 GROUP BY qt2.person.id)")
    List<QTicket> findLatestQTicketsForAllPatients();

     @Query("SELECT qt FROM QTicket qt WHERE qt.person.id = :personId ORDER BY qt.createdAt DESC")
    QTicket findLatestTicketByPersonId(@Param("personId") Long personId);
}
