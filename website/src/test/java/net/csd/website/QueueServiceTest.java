package net.csd.website;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.csd.website.controller.QTicketController;
import net.csd.website.exception.InvalidDateException;
import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;
import net.csd.website.model.Room;
import net.csd.website.model.WaitingQticket;
import net.csd.website.model.Person.Condition;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.PersonRepository;
import net.csd.website.repository.QTicketRepository;
import net.csd.website.repository.RoomRepository;
import net.csd.website.repository.WaitingQTicketRepository;
import net.csd.website.service.QueueService;

@ExtendWith(MockitoExtension.class)
public class QueueServiceTest {

    @Mock
    private PersonRepository personRepository;


    @Mock
    private ResourceNotFoundException resourceNotFoundException;
    
    @Mock
    private QTicketRepository qTicketRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DateTimeSlotRepository dateTimeSlotRepository;

    @Mock
    private WaitingQTicketRepository waitingQTicketRepository;

    @InjectMocks
    private QueueService queueService;

    private static final int EXPECTED_NUMBER_OF_TIME_SLOTS = 26;

    // Still have to implement testGetNewQueueTicket_Success

    @Test
    void testAllocateWaitingQTicket_NoAvailableTimeSlots() {
        // Arrange
        List<WaitingQticket> waitingQtickets = new ArrayList<>();
        waitingQtickets.add(new WaitingQticket());
        when(waitingQTicketRepository.findAll()).thenReturn(waitingQtickets);
        when(dateTimeSlotRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            queueService.allocateWaitingQTicket();
        });
        // You can also assert other conditions based on your logic
    }

    @Test
    void testAllocateWaitingQTicket_Success() {
        // Arrange
        Person person = new Person(); // Create a mock Person object
        WaitingQticket waitingQticket = new WaitingQticket();
        waitingQticket.setPerson(person); // Set the Person object for the WaitingQticket
        List<WaitingQticket> waitingQtickets = Collections.singletonList(waitingQticket);
        when(waitingQTicketRepository.findAll()).thenReturn(waitingQtickets);

        // Prepare available time slots
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeSlot availableSlot = new DateTimeSlot();
        availableSlot.setStartDateTime(currentTime); // Set the startDateTime property
        List<DateTimeSlot> availableSlots = Collections.singletonList(availableSlot);
        when(dateTimeSlotRepository.findAll()).thenReturn(availableSlots);

        // Act & Assert
        assertDoesNotThrow(() -> queueService.allocateWaitingQTicket());
        // You can also assert other conditions based on your logic
    }

    @Test
    void testQueryAvailableTimeSlot() {
        // Arrange
        LocalDate testDate = LocalDate.now().plusDays(1); // Specify a date within the next 3 days from today
        List<DateTimeSlot> mockDateTimeSlots = new ArrayList<>();

        // Create mock DateTimeSlots from 8am to 5pm (26 time slots) with 20 minutes interval
        LocalTime startTime = LocalTime.of(8, 0);
        for (int i = 0; i < 26; i++) {
            DateTimeSlot mockDateTimeSlot = new DateTimeSlot();
            mockDateTimeSlot.setStartDateTime(LocalDateTime.of(testDate, startTime.plusMinutes(i * 20))); // Set start times with 20 minutes interval
            mockDateTimeSlot.setQTicket(null); // No assigned QTicket, indicating it's available
            mockDateTimeSlots.add(mockDateTimeSlot);
        }

        when(dateTimeSlotRepository.findAll()).thenReturn(mockDateTimeSlots);

        // Act
        List<DateTimeSlot> availableSlots = queueService.queryAvailableTimeSlot(testDate);

        // Assert
        assertEquals(EXPECTED_NUMBER_OF_TIME_SLOTS, availableSlots.size()); // Expecting 26 available slots from 8am to 5pm with 20 minutes interval
        // Additional assertions based on your specific logic
    }

}