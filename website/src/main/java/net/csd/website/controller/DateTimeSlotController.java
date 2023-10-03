package net.csd.website.controller;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<DateTimeSlot> getDateTimeSlot(){
        
       return dateTimeSlotRepository.findAll();
    }

}
