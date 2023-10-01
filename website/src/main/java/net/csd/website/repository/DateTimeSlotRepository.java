package net.csd.website.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Room;

@Repository
public interface DateTimeSlotRepository extends JpaRepository<DateTimeSlot, Long> {
    List<DateTimeSlot> findByRoom(Room room);
    // List<DateTimeSlot> findByRoomAndDate(Room room, LocalDate date);
    // List<DateTimeSlot> findByDateAndRoomOrderByStartDateTimeAsc(LocalDate date, Room room);
    Optional<DateTimeSlot> findFirstByOrderByStartDateTimeAsc();

   
}


