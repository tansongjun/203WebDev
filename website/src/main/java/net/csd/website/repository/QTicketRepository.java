package net.csd.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import net.csd.website.model.Patient; // deprecated
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;

@Repository
public interface QTicketRepository extends JpaRepository<QTicket, Long>{
    List<QTicket> findByPerson(Person person);
}
