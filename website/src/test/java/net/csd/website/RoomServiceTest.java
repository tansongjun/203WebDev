package net.csd.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
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
import org.mockito.ArgumentCaptor;
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
    void createRoom_WhenRoomIsCreated_SavesRoomAndGeneratesTimeSlots() {
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
    void createRoom_ExceedsDailyLimit_ThrowsRuntimeException() {
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

    @Test
    void createRoom_WithNewRoom_GeneratesExpectedNumberOfTimeSlots() {
        // Arrange
        Room room = new Room();
        room.setRoomNumber(101);
        LocalDate creationDate = LocalDate.now();
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        Room createdRoom = roomService.createRoom(room, creationDate);

        // Assert
        verify(dateTimeSlotRepository, times(EXPECTED_NUMBER_OF_TIME_SLOTS)).save(any(DateTimeSlot.class));
    }

    @Test
    void createRoom_NullRoomInput_ThrowsNullPointerException() {
        // Arrange
        LocalDate currentDate = LocalDate.now();

        // Act & Assert
        try {
            roomService.createRoom(null, currentDate);
            fail("Creating a room with null should throw an exception.");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    void createRoom_RoomCreation_ChecksTimeSlotStartAndEndTimes() {
        // Arrange
        Room room = new Room();
        LocalDate creationDate = LocalDate.now();
        LocalTime expectedStartTime = LocalTime.of(8, 0); // Start time of first slot
        LocalTime expectedEndTime = LocalTime.of(17, 0); // End time should be before this
        List<DateTimeSlot> savedTimeSlots = new ArrayList<>();

        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(dateTimeSlotRepository.save(any(DateTimeSlot.class))).then(invocation -> {
            DateTimeSlot slot = invocation.getArgument(0);
            savedTimeSlots.add(slot);
            return slot;
        });

        // Act
        roomService.createRoom(room, creationDate);

        // Assert
        for (DateTimeSlot slot : savedTimeSlots) {
            assertTrue(slot.getStartDateTime().toLocalTime().isAfter(expectedStartTime.minusMinutes(1)));
            assertTrue(slot.getEndDateTime().toLocalTime().isBefore(expectedEndTime.plusMinutes(1)));
        }
    }

    @Test
    void createRoom_SingleRoomCreation_CreatesExpectedNumberOfTimeSlots() {
        // Arrange
        Room room = new Room();
        LocalDate creationDate = LocalDate.now();

        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        roomService.createRoom(room, creationDate);

        // Assert
        verify(dateTimeSlotRepository, times(EXPECTED_NUMBER_OF_TIME_SLOTS)).save(any(DateTimeSlot.class));
    }

    @Test
    void createRoom_ExcessRoomCreationOnSameDay_ThrowsRuntimeException() {
        // Arrange
        LocalDate creationDate = LocalDate.now();
        when(roomRepository.countRoomsByCreationDate(creationDate)).thenReturn(3L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> roomService.createRoom(new Room(), creationDate),
                    "Should throw an exception when trying to create more than 3 rooms on the same day");
    }

    @Test
    void createRoom_WithCreationDate_SetsCorrectCreationDateInRoom() {
        // Arrange
        Room room = new Room();
        LocalDate creationDate = LocalDate.now();
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Room savedRoom = roomService.createRoom(room, creationDate);

        // Assert
        assertEquals(creationDate, savedRoom.getCreationDate(), "Creation date should be set correctly in the saved room");
    }

    @Test
    void createRoom_OnRoomCreation_VerifiesTimeSlotDurations() {
        // Arrange
        Room room = new Room();
        room.setRoomNumber(101);
        LocalDate creationDate = LocalDate.now();

        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        roomService.createRoom(room, creationDate);

        // Assert
        ArgumentCaptor<DateTimeSlot> slotCaptor = ArgumentCaptor.forClass(DateTimeSlot.class);
        verify(dateTimeSlotRepository, times(EXPECTED_NUMBER_OF_TIME_SLOTS)).save(slotCaptor.capture());
        for (DateTimeSlot capturedSlot : slotCaptor.getAllValues()) {
            Duration slotDuration = Duration.between(capturedSlot.getStartDateTime(), capturedSlot.getEndDateTime());
            assertEquals(Duration.ofMinutes(20), slotDuration, "Each time slot should be exactly 20 minutes long");
        }
    }

    @Test
    void createNewRoom_SuccessfulCreation_CreatesRoomWithGivenNumber() {
        LocalDate creationDate = LocalDate.now();
        int roomNumber = 101;

        when(roomRepository.countRoomsByCreationDate(creationDate)).thenReturn(2L); // Assuming the limit is 3
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Room createdRoom = roomService.createNewRoom(roomNumber, creationDate);

        assertNotNull(createdRoom);
        assertEquals(roomNumber, createdRoom.getRoomNumber(), "Room should have the correct number");
        assertEquals(creationDate, createdRoom.getCreationDate(), "Room should have the correct creation date");
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void createNewRoom_ExceedsDailyLimit_ThrowsException() {
        LocalDate creationDate = LocalDate.now();
        int roomNumber = 102;

        when(roomRepository.countRoomsByCreationDate(creationDate)).thenReturn(3L); // Limit is reached

        assertThrows(RuntimeException.class, () -> roomService.createNewRoom(roomNumber, creationDate),
                    "Should throw an exception when trying to exceed daily room creation limit");
    }

    @Test
    void createNewRoomforMonth_MultipleRoomsEachDay_SavesRoomsForEntireMonth() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 11, 1);
        int noOfRooms = 3;

        // Define mock behavior
        // Assuming a countRoomsByCreationDate method exists in roomRepository to check room creation limit per day.
        when(roomRepository.countRoomsByCreationDate(any(LocalDate.class))).thenReturn(0L);

        // When
        roomService.createNewRoomforMonth(noOfRooms, startDate);

        // Then
        // Verify roomRepository.save is called the correct number of times.
        // This number should be the number of rooms times the number of days in the month
        LocalDate lastDayOfMonth = startDate.withDayOfMonth(startDate.lengthOfMonth());
        int daysInMonth = lastDayOfMonth.getDayOfMonth() - startDate.getDayOfMonth() + 1;
        verify(roomRepository, times(noOfRooms * daysInMonth)).save(any(Room.class));

        // You may also verify that countRoomsByCreationDate is called correctly
        verify(roomRepository, times(noOfRooms * daysInMonth)).countRoomsByCreationDate(any(LocalDate.class));
    }

    @Test
    void createNewRoomforMonth_ExceedsDailyLimit_ThrowsException() {
        LocalDate startDate = LocalDate.of(2023, 11, 1);
        int noOfRooms = 4; // Assuming the daily limit is 3

        when(roomRepository.countRoomsByCreationDate(any(LocalDate.class))).thenReturn(4L);

        assertThrows(RuntimeException.class, () -> {
            roomService.createNewRoomforMonth(noOfRooms, startDate);
        }, "Should throw an exception when daily limit is exceeded");
    }

}