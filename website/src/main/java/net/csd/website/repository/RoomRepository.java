package net.csd.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.csd.website.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
}
