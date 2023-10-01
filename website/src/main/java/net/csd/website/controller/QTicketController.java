package net.csd.website.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Patient;
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;
import net.csd.website.model.Room;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.PatientRepository;
import net.csd.website.repository.PersonRepository;
import net.csd.website.repository.QTicketRepository;
import net.csd.website.repository.RoomRepository;
import net.csd.website.service.QueueService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class QTicketController {
    private QTicketRepository qTicketRepository;
    private PersonRepository personRepository;
    private QueueService queueService;

    public QTicketController( QTicketRepository qTicketRepository, PersonRepository PersonRepository, QueueService queueService, RoomRepository roomRepository, DateTimeSlotRepository dateTimeSlotRepository) {
        this.qTicketRepository = qTicketRepository;
        this.personRepository = PersonRepository;
        this.queueService = queueService;
    }

    // @GetMapping("/getallQ")
    // public List<QTicket> getAllQ() {
    //     return qTicketRepository.findAll();
    // }

    @GetMapping("/getAllQ")
    public List<QTicket> getLatestQTickets() {
        return qTicketRepository.findLatestQTicketsForAllPatients();
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

    // @PostMapping("/patients/{id}/getnewQ")
    // public QTicket getnewQ(@PathVariable Long id) {
    //     Person patient = personRepository.findById(id)
    //             .orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + id));

    //     QTicket qTicket = new QTicket();

    //     qTicket.setPerson(patient);

    //     return qTicketRepository.save(qTicket);
    // }

    @PostMapping("/patients/{id}/getnewQ")
    public QTicket getnewQ(@PathVariable Long id) {
        Person patient = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + id));

        // Call the queueService to get a new queue ticket for the patient
        QTicket qTicket = queueService.getNewQueueTicket(patient);

        if (qTicket == null) {
            // Handle the case where no suitable room or time slot is found
            throw new ResourceNotFoundException("No suitable room or time slot found for patient id: " + id);
        }

        // Save the queue ticket to the database
        return qTicketRepository.save(qTicket);
    }

//     @PostMapping("/patients/{id}/getnewQ")
// public QTicket getnewQ(@PathVariable Long id) {
//     Person patient = personRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + id));

//     // Check if a QTicket already exists for this Person
//     Optional<QTicket> existingQTicketOptional = qTicketRepository.findById(id);

//     if (existingQTicketOptional.isPresent()) {
//         // If a QTicket already exists, return it or update it if needed
//         QTicket existingQTicket = existingQTicketOptional.get();
//         existingQTicket.setCreatedAt(LocalDateTime.now()); // Update creation timestamp if necessary
//         return qTicketRepository.save(existingQTicket);
//     } else {
//         // If no existing QTicket, create a new one
//         QTicket qTicket = new QTicket();
//         qTicket.setPerson(patient);

//         // Call the queueService to find the appropriate room and time slot
//         DateTimeSlot dateTimeSlot = queueService.findEarliestTimeSlot();
//         Room room = queueService.findLowestRoom();

//         if (dateTimeSlot != null && room != null) {
//             qTicket.setDatetimeSlot(dateTimeSlot);
//             // qTicket.setRoom(room);

//             // Save the queue ticket to the database
//             return qTicketRepository.save(qTicket);
//         } else {
//             // Handle the case where no suitable room or time slot is found
//             throw new ResourceNotFoundException("No suitable room or time slot found for patient id: " + id);
//         }
//     }
// }



    
}
