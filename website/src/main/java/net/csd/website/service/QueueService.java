package net.csd.website.service;

import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;
import net.csd.website.model.Room;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.RoomRepository;
import net.csd.website.repository.QTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Comparator;

@Service
public class QueueService {

    private final QTicketRepository qTicketRepository;
    private final RoomRepository roomRepository;
    private final DateTimeSlotRepository dateTimeSlotRepository;

    @Autowired
    public QueueService(QTicketRepository qTicketRepository, RoomRepository roomRepository, DateTimeSlotRepository dateTimeSlotRepository) {
        this.qTicketRepository = qTicketRepository;
        this.roomRepository = roomRepository;
        this.dateTimeSlotRepository = dateTimeSlotRepository;
    }

    @Transactional
    public QTicket getNewQueueTicket(Person patient) {
        // Retrieve all available rooms
        List<Room> rooms = roomRepository.findAll();

        // Retrieve all time slots
        List<DateTimeSlot> allDateTimeSlots = dateTimeSlotRepository.findAll();

        // Retrieve time slots that are not occupied
        List<DateTimeSlot> availableDateTimeSlots = allDateTimeSlots.stream()
                .filter(dateTimeSlot -> dateTimeSlot.getQTicket() == null)
                .collect(Collectors.toList());

        // Find the room with the lowest room number
        Room lowestRoom = rooms.stream()
                .min(Comparator.comparing(Room::getRoomNumber))
                .orElse(null);

        // Find the earliest available time slot
        DateTimeSlot earliestAvailableTimeSlot = availableDateTimeSlots.stream()
                .min(Comparator.comparing(DateTimeSlot::getStartDateTime))
                .orElse(null);

        if (lowestRoom != null && earliestAvailableTimeSlot != null) {
            // Create a new queue ticket and assign the patient, room, and time slot
            QTicket qTicket = new QTicket();
            qTicket.setPerson(patient);
            // qTicket.setRoom(lowestRoom); // Uncomment this if you want to set the room
            qTicket.setDatetimeSlot(earliestAvailableTimeSlot);
            qTicket.setCreatedAt(LocalDateTime.now());

            // Save the queue ticket to the database
            return qTicketRepository.save(qTicket);
        } else {
            // Handle the case where no suitable room or available time slot is found
            // You can throw an exception or return null based on your requirement
            return null;
        }
    }

    public QTicket findLatestTicketByPersonId(Long personId) {
        List<QTicket> tickets = qTicketRepository.findLatestTicketByPersonId(personId);
        return tickets.isEmpty() ? null : tickets.get(0);
    }

}
