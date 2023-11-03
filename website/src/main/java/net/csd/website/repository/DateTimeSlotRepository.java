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

    // Add a method signature for custom query to find DateTimeSlots by date
    @Query("SELECT d FROM DateTimeSlot d WHERE DATE(d.startDateTime) = :date")
    List<DateTimeSlot> findAllByDate(@Param("date") LocalDate date);
}


