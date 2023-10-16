package net.csd.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.csd.website.model.QTicket;
import net.csd.website.model.WaitingQticket;

@Repository
public interface WaitingQTicketRepository extends JpaRepository<WaitingQticket, Long>{

}
