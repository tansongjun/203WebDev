package net.csd.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Room;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.RoomRepository;
import net.csd.website.service.RoomService;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {
    
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DateTimeSlotRepository dateTimeSlotRepository;

    @InjectMocks
    private RoomService roomService;

    private static final int EXPECTED_NUMBER_OF_TIME_SLOTS = 26;

    @Test
    public void testCreateRoom_Success() {
        // Arrange
        Room room = new Room();
        LocalDate currentDate = LocalDate.now();

        // Act
        roomService.createRoom(room, currentDate);

        // Assert
        // Verify if the room is saved in the repository
        verify(roomRepository, times(1)).save(room);
        // Verify if exactly 29 time slots are generated and saved
        verify(dateTimeSlotRepository, times(EXPECTED_NUMBER_OF_TIME_SLOTS)).save(any(DateTimeSlot.class));
    }

    @Test
    public void testCreateRoom_RoomCreationLimitReached() {
        // Arrange
        Room room = new Room();
        LocalDate currentDate = LocalDate.now();

        // Mock the behavior of roomRepository.countRoomsByCreationDate
        when(roomRepository.countRoomsByCreationDate(currentDate)).thenReturn(3L);

        // Act & Assert
        try {
            roomService.createRoom(room, currentDate);
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            assertEquals("Maximum limit of 3 rooms created for " + currentDate, e.getMessage());
        }
    }


}