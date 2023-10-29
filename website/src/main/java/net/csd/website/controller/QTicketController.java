package net.csd.website.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.csd.website.exception.InvalidDateException;
import net.csd.website.exception.InvalidRequestException;
import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;
import net.csd.website.model.QTicket.QStatus;
import net.csd.website.model.Room;
import net.csd.website.model.WaitingQticket;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.PersonRepository;
import net.csd.website.repository.QTicketRepository;
import net.csd.website.repository.RoomRepository;
import net.csd.website.response.QueueResponse;
import net.csd.website.service.QueueService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class QTicketController {
    private QTicketRepository qTicketRepository;
    private PersonRepository personRepository;
    private QueueService queueService;
    private RoomRepository roomRepository;
    private DateTimeSlotRepository dateTimeSlotRepository;

    public QTicketController(QTicketRepository qTicketRepository, PersonRepository PersonRepository,
            QueueService queueService, RoomRepository roomRepository, DateTimeSlotRepository dateTimeSlotRepository) {
        this.qTicketRepository = qTicketRepository;
        this.personRepository = PersonRepository;
        this.queueService = queueService;
        this.roomRepository = roomRepository;
        this.dateTimeSlotRepository = dateTimeSlotRepository;
    }

    // problem with this method/api
    @GetMapping("/getAllQ")
    public List<QueueResponse> getLatestQTickets() {
        List<QueueResponse> queueResponses = new ArrayList<>();

        List<QTicket> qTickets = qTicketRepository.findAll();
        for (QTicket qTicket : qTickets) {
            QueueResponse queueResponse = new QueueResponse(qTicket, qTicket.getDatetimeSlot());
            queueResponses.add(queueResponse);
        }
        return queueResponses;
    }

    // problem with this method/api
    @GetMapping("/patients/{id}/getQ")
    public QueueResponse getLatestQTicket(@PathVariable Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for id: " + id));
        QueueResponse queueResponse = new QueueResponse(
            queueService.findLatestTicketByPersonId(id) , 
            queueService.findLatestTicketByPersonId(id).getDatetimeSlot()
        );
        return queueResponse;
    }

    @GetMapping("/getWaitingList")
    public List<QueueResponse> getWaitingList() {
        List<QueueResponse> waitingList = new ArrayList<>();
        List<QTicket> qTickets = qTicketRepository.findAll();
        for (QTicket qTicket : qTickets) {
            if (qTicket instanceof WaitingQticket) {
                QueueResponse queueResponse = new QueueResponse((WaitingQticket) qTicket, qTicket.getDatetimeSlot());
                waitingList.add(queueResponse);
            }
        }
        return waitingList;
    }

    // For Walk in patients, to register for a new queue ticket
    @PostMapping("/patients/{id}/getnewQ")
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<QueueResponse> getNewQ(@PathVariable Long id) {
        Person patient = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + id));

        // Call the queueService to get a new queue ticket for the patient
        QTicket qTicket = queueService.getNewQueueTicket(patient);

        if (qTicket == null) {
            // Handle the case where no suitable room or time slot is found
            throw new ResourceNotFoundException("No suitable room or time slot found for patient id: " + id);
        }

        // Save the queue ticket to the database
        qTicket = qTicketRepository.save(qTicket);

        // Construct the QueueResponse object with the newly created QTicket and
        // associated DateTimeSlot
        QueueResponse queueResponse = new QueueResponse(qTicket, qTicket.getDatetimeSlot());

        // Return the QueueResponse object in the response entity
        return ResponseEntity.ok(queueResponse);
    }

    @GetMapping("/appointment/queryAvailableTimeSlot/{date}")
    public List<DateTimeSlot> queryAvailableTimeSlotController(@PathVariable LocalDate date) {
        // Shifted to QueueService
        // Calculate the date 3 days from today
        LocalDate threeDaysLater = LocalDate.now().plusDays(3);

        // Ensure the provided date is after the next 3 days from today
        if (date.isBefore(threeDaysLater)) {
            // Handle the case where the provided date is more than 3 days from today
            throw new InvalidDateException("Invalid date. Please provide a date after the next 3 days from today.");
        }

        return queueService.queryAvailableTimeSlot(date);
    }

    @PostMapping("/appointment/bookNewAppointment/{patientId}/{dateTimeSlotId}")
    public ResponseEntity<QueueResponse> bookNewAppointment(
            @PathVariable long patientId,
            @PathVariable long dateTimeSlotId) {

        Person patient = personRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + patientId));

        DateTimeSlot dateTimeSlot = dateTimeSlotRepository.findById(dateTimeSlotId)
                .orElseThrow(() -> new ResourceNotFoundException("DateTimeSlot not found for id: " + dateTimeSlotId));

        // Check if the DateTimeSlot is available (i.e., not occupied)
        if (dateTimeSlot.getQTicket() != null) {
            throw new InvalidRequestException("The time slot is not available. Please choose another time slot.");
        }

        // Check if the startDateTime of the DateTimeSlot is after 3 days from today
        LocalDate threeDaysLater = LocalDate.now().plusDays(3);
        System.out.println("threeDaysLater: " + threeDaysLater);
        System.out.println("dateTimeSlot.getStartDateTime(): " + dateTimeSlot.getStartDateTime().toLocalDate());
        if (dateTimeSlot.getStartDateTime().toLocalDate().isBefore(threeDaysLater)) {
            throw new InvalidRequestException("Invalid date. Please provide a date after the next 3 days from today.");
        }

        // Create a new QTicket and set the patient and DateTimeSlot
        QTicket qTicket = new QTicket();
        qTicket.setPerson(patient);
        qTicket.setDatetimeSlot(dateTimeSlot);
        qTicket.setCreatedAt(LocalDateTime.now());

        // Save the QTicket in the database
        qTicketRepository.save(qTicket);

        // Construct the QueueResponse object with the newly created QTicket and
        // associated DateTimeSlot
        QueueResponse queueResponse = new QueueResponse(qTicket, qTicket.getDatetimeSlot());

        // Return the QueueResponse object in the response entity
        return ResponseEntity.ok(queueResponse);
    }

    @GetMapping("/patient/{personId}/getAwaitingPayment")
    public List<QTicket> getAwaitingPayment(@PathVariable Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for id: " + personId));
        List<QTicket> qTickets = qTicketRepository.findByQStatusAndPersonId(QStatus.AWAITINGPAYMENT, personId);
        return qTickets;
    }

    @PutMapping("/patient/{id}/confirmPayment/{ticketno}")
    public ResponseEntity<String> confirmPayment(@PathVariable Long id, @PathVariable Long ticketno) {
        Optional<QTicket> qTickets = qTicketRepository.findById(ticketno);
        if (!qTickets.isPresent()) {
            throw new ResourceNotFoundException("Ticket not found for id: " + ticketno);
        }

        QTicket qTicket = qTickets.get();

        if (qTicket.getQStatus() == QStatus.WAITING || qTicket.getQStatus() == QStatus.IN_PROGRESS || qTicket.getQStatus() == QStatus.CANCELLED) {
            throw new InvalidRequestException("Payment cannot be confirmed for ticket " + ticketno +". Please check the status again.");
        } else if (qTicket.getQStatus() == QStatus.COMPLETED) {
            throw new InvalidRequestException("Payment has already been made for ticket " + ticketno +".");
        }

        qTicket.setQStatus(QStatus.COMPLETED);
        qTicketRepository.save(qTicket);
        return ResponseEntity.ok("Payment confirmed.");
    }

    @PutMapping("/patient/{id}/hasSeenDoctor/{ticketno}")
    public ResponseEntity<String> hasSeenDoctor(@PathVariable Long id, @PathVariable Long ticketno) {
        Optional<QTicket> qTickets = qTicketRepository.findById(ticketno);
        if (!qTickets.isPresent()) {
            throw new ResourceNotFoundException("Ticket not found for id: " + ticketno);
        }
        QTicket qTicket = qTickets.get();

        if (qTicket.getQStatus() == QStatus.COMPLETED || qTicket.getQStatus() == QStatus.CANCELLED) {
            throw new InvalidRequestException("Invalid Request for ticket " + ticketno +". Please check the status again. Only tickets with status 'Waiting' or 'In Progress' can be updated.");
        }

        qTicket.setQStatus(QStatus.AWAITINGPAYMENT);
        qTicketRepository.save(qTicket);
        return ResponseEntity.ok("Patient "+id+" Awaiting payment");
    }

}
