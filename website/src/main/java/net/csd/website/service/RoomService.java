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
import net.csd.website.repository.DateTimeSlotRepository; // If you have a DateTimeSlotRepository for database operations

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final DateTimeSlotRepository dateTimeSlotRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, DateTimeSlotRepository dateTimeSlotRepository) {
        this.roomRepository = roomRepository;
        this.dateTimeSlotRepository = dateTimeSlotRepository;
    }

    public Room createRoom(Room room, LocalDate creationDate) {
        // Check if the maximum limit of 3 rooms is reached for the given creation date
        long numberOfRoomsCreatedToday = roomRepository.countRoomsByCreationDate(creationDate);
        if (numberOfRoomsCreatedToday >= 3) {
            throw new RuntimeException("Maximum limit of 3 rooms created for " + creationDate);
        }
    
        // Set the creation date
        room.setCreationDate(creationDate);
    
        // Save the room in the database
        Room savedRoom = roomRepository.save(room);
    
        // Generate time slots for the newly created room from 8am to 5pm with 20-minute intervals
        LocalTime startTime = LocalTime.of(8, 0); // 8:00 AM
        LocalTime endTime = LocalTime.of(17, 0); // 5:00 PM
        Duration slotDuration = Duration.ofMinutes(20); // 20 minutes interval
    
        LocalDateTime currentDateTime = LocalDateTime.of(creationDate, startTime);
        while (currentDateTime.plus(slotDuration).isBefore(LocalDateTime.of(creationDate, endTime))) {
            // Create a DateTimeSlot with the current start time and end time
            DateTimeSlot dateTimeSlot = new DateTimeSlot();
            dateTimeSlot.setStartDateTime(currentDateTime);
            dateTimeSlot.setEndDateTime(currentDateTime.plus(slotDuration));
            dateTimeSlot.setRoom(savedRoom);
    
            // Save the DateTimeSlot
            dateTimeSlotRepository.save(dateTimeSlot);
    
            // Move to the next time slot
            currentDateTime = currentDateTime.plus(slotDuration);
        }
    
        return savedRoom;
    }
    
}
