package net.csd.website.controller;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.csd.website.model.Room;
import net.csd.website.repository.RoomRepository;
import net.csd.website.service.DateTimeSlotService;
import net.csd.website.service.RoomService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class RoomController {

    private final DateTimeSlotService dateTimeSlotService;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    @Autowired
    public RoomController(DateTimeSlotService dateTimeSlotService, RoomRepository roomRepository, RoomService roomService) {
        this.dateTimeSlotService = dateTimeSlotService;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public Iterable<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // @PostMapping("/rooms")
    // public Room createRoom(@RequestBody Room room) {
    //     return roomRepository.save(room);
    // }

    // @PostMapping("/rooms/{id}")
    // public ResponseEntity<Room> createRoom(@PathVariable Long id, @RequestBody Room room) {
    //     // Set the room ID and room number
    //     room.setRoomNumber(id); // Assuming room number is stored as a string

    //     // Save the room in the database
    //     Room savedRoom = roomRepository.save(room);

    //     // Generate and save time slots for the newly created room
    //     dateTimeSlotService.generateAndSaveTimeSlots(LocalDate.now(), savedRoom);

    //     return ResponseEntity.ok(savedRoom);
    // }

     @PostMapping("/rooms")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        try {
            Room createdRoom = roomService.createRoom(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            // You can customize the response or handle the exception based on your requirements
        }
    }
}
