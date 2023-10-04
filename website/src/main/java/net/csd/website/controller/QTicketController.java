package net.csd.website.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public QTicketController( QTicketRepository qTicketRepository, PersonRepository PersonRepository, QueueService queueService, RoomRepository roomRepository, DateTimeSlotRepository dateTimeSlotRepository) {
        this.qTicketRepository = qTicketRepository;
        this.personRepository = PersonRepository;
        this.queueService = queueService;
        this.roomRepository = roomRepository;
        this.dateTimeSlotRepository = dateTimeSlotRepository;
    }

    // problem with this method/api
    @GetMapping("/getAllQ")
    public List<QTicket> getLatestQTickets() {
        return qTicketRepository.findAll();
    }

    // problem with this method/api
    @GetMapping("/patients/{id}/getQ")
    public DateTimeSlot getLatestQTicket(@PathVariable Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for id: " + id));

        return queueService.findLatestTicketByPersonId(id).getDatetimeSlot();
    }

    @PostMapping("/patients/{id}/getnewQ")
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

        // Construct the QueueResponse object with the newly created QTicket and associated DateTimeSlot
        QueueResponse queueResponse = new QueueResponse(qTicket, qTicket.getDatetimeSlot());

        // Return the QueueResponse object in the response entity
        return ResponseEntity.ok(queueResponse);
    }


    @GetMapping("/appointment/queryAvailableTimeSlot/{date}")
    public List<DateTimeSlot> queryAvailableTimeSlot(@PathVariable LocalDate date) {
        // Retrieve all available rooms
        List<Room> rooms = roomRepository.findAll();

        // Retrieve all time slots
        List<DateTimeSlot> allDateTimeSlots = dateTimeSlotRepository.findAll();

        // Filter time slots for the specified date
        List<DateTimeSlot> availableDateTimeSlots = allDateTimeSlots.stream()
                .filter(dateTimeSlot -> dateTimeSlot.getStartDateTime().toLocalDate().equals(date)
                        && dateTimeSlot.getQTicket() == null)
                .collect(Collectors.toList());

        // Sort available time slots by start time (optional step)
        availableDateTimeSlots.sort(Comparator.comparing(DateTimeSlot::getStartDateTime));

        // Limit the number of results (optional step)
        // availableDateTimeSlots = availableDateTimeSlots.stream().limit(10).collect(Collectors.toList());

        return availableDateTimeSlots;
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Create a new QTicket and set the patient and DateTimeSlot
        QTicket qTicket = new QTicket();
        qTicket.setPerson(patient);
        qTicket.setDatetimeSlot(dateTimeSlot);
        qTicket.setCreatedAt(LocalDateTime.now());

        // Save the QTicket in the database
        qTicketRepository.save(qTicket);

        // Construct the QueueResponse object with the newly created QTicket and associated DateTimeSlot
        QueueResponse queueResponse = new QueueResponse(qTicket, qTicket.getDatetimeSlot());

        // Return the QueueResponse object in the response entity
        return ResponseEntity.ok(queueResponse);
    }
    
}
