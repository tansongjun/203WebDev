package net.csd.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.csd.website.model.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
  List<Room> findAllByOrderByRoomNumberAsc();
  long countRoomsByCreationDate(LocalDate creationDate);
  Optional<Room> findFirstByOrderByRoomNumberAsc();

}
