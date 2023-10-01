package net.csd.website.utility;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Room;

public class TimeSlotGenerator {

    public static List<DateTimeSlot> generateTimeSlots(LocalDate date, LocalTime startTime, LocalTime endTime, Duration slotDuration, Room room) {
        List<DateTimeSlot> timeSlots = new ArrayList<>();
        LocalTime currentTime = startTime;

        while (currentTime.isBefore(endTime)) {
            DateTimeSlot timeSlot = new DateTimeSlot();
            // timeSlot.setDate(date);
            // timeSlot.setFixedTime(currentTime);
            timeSlot.setRoom(room);
            timeSlots.add(timeSlot);

            currentTime = currentTime.plusMinutes(slotDuration.toMinutes());
        }

        return timeSlots;
    }
}

