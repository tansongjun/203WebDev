package net.csd.website.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Room;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.RoomRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class DateTimeSlotController {
    private RoomRepository roomRepository;
    private DateTimeSlotRepository dateTimeSlotRepository;

    public DateTimeSlotController(RoomRepository roomRepository, DateTimeSlotRepository dateTimeSlotRepository){
        this.roomRepository = roomRepository;
        this.dateTimeSlotRepository = dateTimeSlotRepository;
    }

    @GetMapping("/datetimeslot")
    public Iterable<DateTimeSlot> getDateTimeSlot(){
        
        return dateTimeSlotRepository.findAll();
    }

    @GetMapping("/rooms/{id}/date/{date}/timeslots")
    public Iterable<DateTimeSlot> getAllTimeslotForRoom(@PathVariable Long id, @PathVariable LocalDate date) {
        Room room = roomRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Room not found for id: " + id));

        return dateTimeSlotRepository.findByRoomAndDate(room, date);

    }

    @PostMapping("/rooms/{id}/addslotsforDate/{date}")
    public ResponseEntity<Room> addDateAndTimeforRoom(@PathVariable Long id, @PathVariable LocalDate date){
        Room room = roomRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Room not found for id: " + id));        
        for (int i = 8; i < 17; i++){
            for (int j = 0; j < 60; j += 20){
                LocalTime currentTime = LocalTime.of(i, j);
                //Todo: check if there is no such date and time in the room.
                // if not available, return error message

                DateTimeSlot dateTimeSlot = new DateTimeSlot();
                dateTimeSlot.setDate(date);
                dateTimeSlot.setRoom(room);
                dateTimeSlot.setTime(currentTime);
                dateTimeSlotRepository.save(dateTimeSlot);
            }
        }

        Room updatedRoom = roomRepository.save(room);
        return ResponseEntity.ok(updatedRoom);
    }



}
