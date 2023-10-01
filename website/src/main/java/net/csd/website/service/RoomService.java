package net.csd.website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Room;
import net.csd.website.repository.RoomRepository;

import java.time.Duration;
import net.csd.website.service.DateTimeSlotService;
import net.csd.website.repository.DateTimeSlotRepository; // If you have a DateTimeSlotRepository for database operations
import java.util.List; 

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final DateTimeSlotService dateTimeSlotService;

    @Autowired
    public RoomService(RoomRepository roomRepository, DateTimeSlotService dateTimeSlotService) {
        this.roomRepository = roomRepository;
        this.dateTimeSlotService = dateTimeSlotService;
    }

    public Room createRoom(Room room) {
        room.setCreationDate(LocalDate.now());
        // Get the count of rooms created on the current day
        long numberOfRoomsCreatedToday = roomRepository.countRoomsByCreationDate(LocalDate.now());

        // Check if the maximum limit of 3 rooms is reached for the current day
        if (numberOfRoomsCreatedToday >= 3) {
            throw new RuntimeException("Maximum limit of 3 rooms created for today");
        }

        // Save the room in the database
        //return roomRepository.save(room);
        Room savedRoom = roomRepository.save(room);

         // Generate time slots for the newly created room from 8am to 5pm with 20-minute intervals
        LocalTime startTime = LocalTime.of(8, 0); // 8:00 AM
        LocalTime endTime = LocalTime.of(17, 0); // 5:00 PM
        Duration slotDuration = Duration.ofMinutes(20); // 20 minutes interval

        LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), startTime);
        while (currentDateTime.plus(slotDuration).isBefore(LocalDateTime.of(LocalDate.now(), endTime))) {
            // Create a DateTimeSlot with the current start time and end time
            DateTimeSlot dateTimeSlot = new DateTimeSlot();
            dateTimeSlot.setStartDateTime(currentDateTime);
            dateTimeSlot.setEndDateTime(currentDateTime.plus(slotDuration));
            dateTimeSlot.setRoom(savedRoom);

            // Save the DateTimeSlot
            dateTimeSlotService.saveDateTimeSlot(dateTimeSlot);

            // Move to the next time slot
            currentDateTime = currentDateTime.plus(slotDuration);
        }

        return savedRoom;

    
    }
}
