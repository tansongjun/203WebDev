package net.csd.website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.QTicket;
import net.csd.website.model.Room;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.QTicketRepository;
import net.csd.website.repository.RoomRepository;
import net.csd.website.utility.TimeSlotGenerator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

@Service
public class DateTimeSlotService {

    private final DateTimeSlotRepository dateTimeSlotRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public DateTimeSlotService(DateTimeSlotRepository dateTimeSlotRepository, RoomRepository roomRepository) {
        this.dateTimeSlotRepository = dateTimeSlotRepository;
        this.roomRepository = roomRepository;
    }

    public DateTimeSlot saveDateTimeSlot(DateTimeSlot dateTimeSlot) {
        return dateTimeSlotRepository.save(dateTimeSlot);
    }

    public void generateAndSaveTimeSlots(LocalDate date, Room room) {
        LocalTime startTime = LocalTime.of(8, 0); // Start time: 8:00 AM
        LocalTime endTime = LocalTime.of(17, 0);  // End time: 5:00 PM
        Duration slotDuration = Duration.ofMinutes(20); // 20-minute intervals

        // Generate time slots for the given date, start time, end time, and room
        List<DateTimeSlot> timeSlots = TimeSlotGenerator.generateTimeSlots(date, startTime, endTime, slotDuration, room);

        // Save the generated time slots in the database
        dateTimeSlotRepository.saveAll(timeSlots);
    }
}
