package net.csd.website.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import net.csd.website.model.Patient;
import net.csd.website.model.QTicket;
import net.csd.website.repository.PatientRepository;
import net.csd.website.repository.QTicketRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/")
public class QTicketController {
    private PatientRepository patientRepository;
    private QTicketRepository qTicketRepository;

    public QTicketController(PatientRepository patientRepository, QTicketRepository qTicketRepository) {
        this.patientRepository = patientRepository;
        this.qTicketRepository = qTicketRepository;
    }

    @GetMapping("/getallQ")
    public List<QTicket> getAllQ() {
        return qTicketRepository.findAll();
    }

    @GetMapping("/patients/{id}/getQ")
    public QTicket getQ(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found for id: " + id));

        QTicket returnedqTicket = null;
        long qTicketNo = 0;
        for (QTicket qTicket : qTicketRepository.findByPatient(patient)) {
            if (qTicket.getTicketno() > qTicketNo) {
                returnedqTicket = qTicket;
                qTicketNo = qTicket.getTicketno();
            }
        }
        if (returnedqTicket == null) {
            throw new RuntimeException("No Queue ticket found for Patient id: " + id);
        }

        return returnedqTicket;
    }

    @PostMapping("/patients/{id}/getnewQ")
    public QTicket getnewQ(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found for id: " + id));

        QTicket qTicket = new QTicket();

        qTicket.setPatient(patient);

        return qTicketRepository.save(qTicket);
    }

}
