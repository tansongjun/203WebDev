package net.csd.website.controller;

import java.util.List;

import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.Patient;
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;
import net.csd.website.repository.PatientRepository;
import net.csd.website.repository.PersonRepository;
import net.csd.website.repository.QTicketRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class QTicketController {
    private QTicketRepository qTicketRepository;
    private PersonRepository personRepository;

    public QTicketController( QTicketRepository qTicketRepository, PersonRepository PersonRepository) {
        this.qTicketRepository = qTicketRepository;
        this.personRepository = PersonRepository;
    }

    @GetMapping("/getallQ")
    public List<QTicket> getAllQ() {
        return qTicketRepository.findAll();
    }

    @GetMapping("/patients/{id}/getQ")
    public QTicket getQ(@PathVariable Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for id: " + id));

        QTicket returnedqTicket = null;
        long qTicketNo = 0;
        for (QTicket qTicket : qTicketRepository.findByPerson(person)) {
            if (qTicket.getTicketno() > qTicketNo) {
                returnedqTicket = qTicket;
                qTicketNo = qTicket.getTicketno();
            }
        }
        if (returnedqTicket == null) {
            throw new ResourceNotFoundException("No Queue ticket found for Patient id: " + id);
        }

        return returnedqTicket;
    }

    @PostMapping("/patients/{id}/getnewQ")
    public QTicket getnewQ(@PathVariable Long id) {
        Person patient = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + id));

        QTicket qTicket = new QTicket();

        qTicket.setPerson(patient);

        return qTicketRepository.save(qTicket);
    }

}
