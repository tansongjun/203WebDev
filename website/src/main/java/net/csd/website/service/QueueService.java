package net.csd.website.service;

import net.csd.website.exception.InvalidDateException;
import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;
import net.csd.website.model.Room;
import net.csd.website.model.WaitingQticket;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.RoomRepository;
import net.csd.website.repository.QTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@Service
public class QueueService {

    private final QTicketRepository qTicketRepository;
    private final RoomRepository roomRepository;
    private final DateTimeSlotRepository dateTimeSlotRepository;

    private int[] queuePattern = { 3, 3, 2, 1 };
    private int queuePatternIndex = 0;

    @Autowired
    public QueueService(QTicketRepository qTicketRepository, RoomRepository roomRepository,
            DateTimeSlotRepository dateTimeSlotRepository) {
        this.qTicketRepository = qTicketRepository;
        this.roomRepository = roomRepository;
        this.dateTimeSlotRepository = dateTimeSlotRepository;
    }

    public List<DateTimeSlot> queryAvailableTimeSlot(LocalDate date) {
        // Calculate the date 3 days from today
        LocalDate threeDaysLater = LocalDate.now().plusDays(3);

        // Ensure the provided date is within the next 3 days from today
        if (date.isAfter(threeDaysLater)) {
            // Handle the case where the provided date is more than 3 days from today
            throw new InvalidDateException("Invalid date. Please provide a date within the next 3 days from today.");
        }

        // Retrieve all available rooms
        // List<Room> rooms = roomRepository.findAll();

        // Retrieve all time slots
        List<DateTimeSlot> allDateTimeSlots = dateTimeSlotRepository.findAll();

        // Filter time slots for the specified date and available slots
        List<DateTimeSlot> availableDateTimeSlots = allDateTimeSlots.stream()
                .filter(dateTimeSlot -> dateTimeSlot.getStartDateTime().toLocalDate().equals(date)
                        && dateTimeSlot.getQTicket() == null)
                .collect(Collectors.toList());

        // Create a Map to store the selected slots by LocalTime
        Map<LocalTime, DateTimeSlot> selectedSlots = new HashMap<>();

        // Iterate through the available slots and select the earliest room number for
        // each start time
        for (DateTimeSlot slot : availableDateTimeSlots) {
            LocalTime startTime = slot.getStartDateTime().toLocalTime();
            if (!selectedSlots.containsKey(startTime) ||
                    slot.getRoom().getRoomNumber() < selectedSlots.get(startTime).getRoom().getRoomNumber()) {
                selectedSlots.put(startTime, slot);
            }
        }

        // Convert the selected slots Map to a List
        List<DateTimeSlot> resultSlots = new ArrayList<>(selectedSlots.values());

        // Sort the result slots by start time (optional step)
        resultSlots.sort(Comparator.comparing(slot -> slot.getStartDateTime().toLocalTime()));

        return resultSlots;
    }

    @Transactional
    public QTicket getNewQueueTicket(Person patient) {
        // Check if the patient has already got a queue ticket for today
        List<QTicket> existingTickets = qTicketRepository.findLatestTicketByPersonId(patient.getId());
        if (!existingTickets.isEmpty()) {
            QTicket latestTicket = existingTickets.get(0);
            if (latestTicket.getCreatedAt().toLocalDate().equals(LocalDate.now())) {
                // Return null if the patient has already got a queue ticket for today
                throw new RuntimeException("Patient has already got a queue ticket for today");
            }
        }

        // Creation of either waitingQTicket or QTicket
        QTicket qTicket = null;
        DateTimeSlot dateTimeSlot = null;

        // if the patient is in the queue pattern, create a QTicket
        if (queuePattern[queuePatternIndex] == patient.getRiskLevel()) {
            queuePatternIndex = (queuePatternIndex + 1) % queuePattern.length;

            // Retrieve Available time slots for today
            List<DateTimeSlot> availableDateTimeSlots = queryAvailableTimeSlot(LocalDate.now());
            if (availableDateTimeSlots.isEmpty()) {
                throw new ResourceNotFoundException("No available time slot for today");
            }

            dateTimeSlot = availableDateTimeSlots.get(0);
            qTicket = new QTicket();

        // else, create a WaitingQTicket
        } else {
            qTicket = new WaitingQticket();
        }

        //Assign the patient, room, and time slot
        qTicket.setPerson(patient);
        qTicket.setDatetimeSlot(dateTimeSlot);
        qTicket.setCreatedAt(LocalDateTime.now());

        return qTicketRepository.save(qTicket);
    }

    public QTicket findLatestTicketByPersonId(Long personId) {
        List<QTicket> tickets = qTicketRepository.findLatestTicketByPersonId(personId);
        return tickets.isEmpty() ? null : tickets.get(0);
    }

}
