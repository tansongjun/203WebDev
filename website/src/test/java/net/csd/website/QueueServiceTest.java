package net.csd.website;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import net.csd.website.response.QueueResponse;
import net.csd.website.service.QueueService;
import net.csd.website.service.RoomService;
import net.csd.website.model.Person.Authority;


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

    @InjectMocks
    private RoomService roomService;
    
    @InjectMocks
    private QTicketController qTicketController;

    private static final int EXPECTED_NUMBER_OF_TIME_SLOTS = 26;

    @Test
    public void whenQueryAvailableTimeSlot_thenReturnNonEmptyList() {
        // Arrange
        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.of(9, 0));
        LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.of(10, 0));

        // Mocked DateTimeSlots for the given date
        DateTimeSlot slot1 = new DateTimeSlot();
        slot1.setId(1L);
        slot1.setStartDateTime(startDateTime);
        slot1.setEndDateTime(endDateTime);
        slot1.setQTicket(null); // This slot is available

        DateTimeSlot slot2 = new DateTimeSlot();
        slot2.setId(2L);
        slot2.setStartDateTime(startDateTime.plusHours(1));
        slot2.setEndDateTime(endDateTime.plusHours(1));
        slot2.setQTicket(new QTicket()); // This slot is taken

        List<DateTimeSlot> mockSlots = Arrays.asList(slot1, slot2);
        when(dateTimeSlotRepository.findAll()).thenReturn(mockSlots);

        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAvailableTimeSlot(date);

        // Assert
        assertFalse(resultSlots.isEmpty(), "The available time slots should not be empty.");
        assertEquals(1, resultSlots.size(), "There should be only one available time slot.");
        assertNull(resultSlots.get(0).getQTicket(), "The available time slot should not have a ticket associated with it.");
    }

    @Test
    public void whenQueryAvailableTimeSlotOnEmptyRepository_thenReturnEmptyList() {
        // Arrange
        LocalDate date = LocalDate.now();
        when(dateTimeSlotRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAvailableTimeSlot(date);

        // Assert
        assertTrue(resultSlots.isEmpty(), "The available time slots should be empty.");
    }

    @Test
    public void whenQueryAvailableTimeSlotOnFullyBookedDay_thenReturnEmptyList() {
        // Arrange
        LocalDate date = LocalDate.now();
        List<DateTimeSlot> mockSlots = IntStream.range(0, 10) // assume 10 time slots for simplicity
            .mapToObj(i -> {
                DateTimeSlot slot = new DateTimeSlot();
                slot.setStartDateTime(date.atStartOfDay().plusHours(i)); // sets the slot start time
                slot.setEndDateTime(date.atStartOfDay().plusHours(i + 1)); // sets the slot end time
                slot.setQTicket(new QTicket()); // Assume a new QTicket indicates a booked slot
                // ... set other necessary properties of DateTimeSlot, like Room
                return slot;
            })
            .collect(Collectors.toList());
    
        DateTimeSlotRepository dateTimeSlotRepository = Mockito.mock(DateTimeSlotRepository.class);
        // QueueService queueService = new QueueService(dateTimeSlotRepository); // Assuming QueueService takes a DateTimeSlotRepository in its constructor
    
        lenient().when(dateTimeSlotRepository.findAllByDate(date)).thenReturn(mockSlots);
    
        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAvailableTimeSlot(date);
    
        // Assert
        assertTrue(resultSlots.isEmpty(), "The list of available time slots should be empty.");
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

    @Test
    public void whenGetNewQueueTicket_thenCreateTicketSuccessfully() {
        // Arrange
        LocalDate testDate = LocalDate.now(); // Specify a date within the next 3 days from today
        List<DateTimeSlot> mockDateTimeSlots = new ArrayList<>();

        // Create mock DateTimeSlots from 8am to 5pm with 20 minutes interval
        LocalTime startTime = LocalTime.of(8, 0);
        for (int i = 0; i < 26; i++) {
            DateTimeSlot mockDateTimeSlot = new DateTimeSlot();
            LocalDateTime slotTime = LocalDateTime.of(testDate, startTime).plusMinutes(i * 20);
            mockDateTimeSlot.setStartDateTime(slotTime); // Set start time with 20 minutes interval
            mockDateTimeSlot.setEndDateTime(slotTime.plusMinutes(20)); // End time is 20 minutes after start time
            mockDateTimeSlot.setQTicket(null); // No assigned QTicket, indicating it's available
            mockDateTimeSlots.add(mockDateTimeSlot);
        }

        // Mock the repository call to return these available slots
        when(dateTimeSlotRepository.findAll()).thenReturn(mockDateTimeSlots);

        Person patient = new Person(
            "sam", 
            "tom", 
            "sdds", 
            LocalDate.of(1992,Month.APRIL,1), 
            net.csd.website.model.Person.Condition.NONE, 
            "admin", "goodpass", 
            Authority.ROLE_ADMIN);
        patient.setId(1L);
        
        QTicket newTicket = new QTicket();
        // Set additional properties on newTicket if necessary
        when(qTicketRepository.save(any(QTicket.class))).thenReturn(newTicket);
        when(queueService).thenReturn(queueService);

        // Act
        QTicket ticket = queueService.getNewQueueTicket(patient);
        System.out.println(ticket.getDatetimeSlot());

        // Assert
        WaitingQticket waiting = null;
        if (ticket instanceof WaitingQticket) {
            System.out.println("Waiting ticket");
            waiting = (WaitingQticket) ticket;
            assertEquals(waiting.getWaitingNo(), 1, "The waiting number should be 1");

        } else {
            System.out.println("Appointment ticket");
        }
        
        assertNotNull(ticket, "A new QTicket should be created without exception");
        assertNull(ticket.getDatetimeSlot(), "A new QTicket should be created without exception");
    }

    // @Test
    // public void whenGetNewQueueTicket2_thenCreateTicketSuccessfully() {
    //     // Arrange
    //     LocalDate testDate = LocalDate.now(); // Specify a date within the next 3 days from today
    //     List<DateTimeSlot> mockDateTimeSlots = new ArrayList<>();

    //     // Create mock DateTimeSlots from 8am to 5pm with 20 minutes interval
    //     LocalTime startTime = LocalTime.of(8, 0);
    //     for (int i = 0; i < 26; i++) {
    //         DateTimeSlot mockDateTimeSlot = new DateTimeSlot();
    //         LocalDateTime slotTime = LocalDateTime.of(testDate, startTime).plusMinutes(i * 20);
    //         mockDateTimeSlot.setStartDateTime(slotTime); // Set start time with 20 minutes interval
    //         mockDateTimeSlot.setEndDateTime(slotTime.plusMinutes(20)); // End time is 20 minutes after start time
    //         mockDateTimeSlot.setQTicket(null); // No assigned QTicket, indicating it's available
    //         mockDateTimeSlots.add(mockDateTimeSlot);
    //     }

    //     // Mock the repository call to return these available slots
    //     when(dateTimeSlotRepository.findAll()).thenReturn(mockDateTimeSlots);
    //     Person patient = new Person(
    //         "sam", 
    //         "tom", 
    //         "sdds", 
    //         LocalDate.of(1950,Month.APRIL,1), 
    //         net.csd.website.model.Person.Condition.SEVERE, 
    //         "admin", "goodpass", 
    //         Authority.ROLE_ADMIN);
    //     when(personRepository.findById(anyLong())).thenReturn(Optional.of(patient));
    //     when(waitingQTicketRepository.findAll()).thenReturn(Collections.emptyList());
        
    //     WaitingQticket newWaitingQticket = new WaitingQticket();
    //     when(waitingQTicketRepository.save(any(WaitingQticket.class))).thenReturn(newWaitingQticket);
        
    //     when(qTicketRepository.findLatestTicketByPersonId(anyLong())).thenReturn(Collections.emptyList());

    //     QTicket newTicket = new QTicket();
    //     // Set additional properties on newTicket if necessary
    //     when(qTicketRepository.save(any(QTicket.class))).thenReturn(newTicket);
        
    //     // Act
    //     ResponseEntity<QueueResponse> response = qTicketController.getNewQ(patient.getId());
    //     QTicket ticket = response.getBody().getqTicket();
    //     DateTimeSlot slot = response.getBody().getDateTimeSlot();


    //     // Assert
    //     assertNotNull(ticket, "A new QTicket should be created without exception");
    //     assertNotNull(slot, "A new QTicket should be created without exception");
    //     // assertEquals(ticket.getTicketno(), 1, "The queue ticket no. should be 1");
    // }
    


}