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
import java.lang.reflect.Field;

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
import net.csd.website.model.QTicket.QStatus;
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

    private LocalDate testDate;
    private LocalDate todayDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDate.of(2023, Month.NOVEMBER, 25);
        todayDate = LocalDate.now();
    }

    private List<DateTimeSlot> createMockSlotsToday(int numberOfSlots, boolean areAllSlotsAvailable) {
        List<DateTimeSlot> slots = new ArrayList<>();
        for (int i = 0; i < numberOfSlots; i++) {
            DateTimeSlot slot = new DateTimeSlot();
            slot.setStartDateTime(todayDate.atTime(LocalTime.of(9, 0).plusMinutes(i * 20)));
            slot.setEndDateTime(todayDate.atTime(LocalTime.of(9, 20).plusMinutes(i * 20)));
            slot.setQTicket(areAllSlotsAvailable ? null : new QTicket());
            slots.add(slot);
        }
        return slots;
    }

    private List<DateTimeSlot> createMockSlotsInAdvance(int numberOfSlots, boolean areAllSlotsAvailable) {
        List<DateTimeSlot> slots = new ArrayList<>();
        for (int i = 0; i < numberOfSlots; i++) {
            DateTimeSlot slot = new DateTimeSlot();
            slot.setStartDateTime(testDate.atTime(LocalTime.of(9, 0).plusMinutes(i * 20)));
            slot.setEndDateTime(testDate.atTime(LocalTime.of(9, 20).plusMinutes(i * 20)));
            slot.setQTicket(areAllSlotsAvailable ? null : new QTicket());
            slots.add(slot);
        }
        return slots;
    }

    private List<DateTimeSlot> createMockSlotsWithMixedAvailability(int numberOfSlots) {
        List<DateTimeSlot> slots = new ArrayList<>();
        for (int i = 0; i < numberOfSlots; i++) {
            DateTimeSlot slot = new DateTimeSlot();
            slot.setStartDateTime(testDate.atTime(LocalTime.of(9, 0).plusMinutes(i * 20)));
            slot.setEndDateTime(testDate.atTime(LocalTime.of(9, 20).plusMinutes(i * 20)));
            // Alternate between available and booked slots
            if (i % 2 == 0) {
                slot.setQTicket(null); // Available slot
            } else {
                slot.setQTicket(new QTicket()); // Booked slot
            }
            slots.add(slot);
        }

        return slots;
    }

    @Test
    public void queryAvailableTimeSlot_AllSlotsAvailable_ReturnsAllSlots() {
        // Arrange
        int numOfSlots = 10;
        List<DateTimeSlot> allAvailableSlots = createMockSlotsInAdvance(numOfSlots, true);
        when(dateTimeSlotRepository.findAll()).thenReturn(allAvailableSlots);

        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAvailableTimeSlot(testDate);

        // Assert
        assertNotNull(resultSlots, "The result should not be null");
        assertEquals(10, resultSlots.size(), "All available time slots should be returned");
    }

    @Test
    public void queryAvailableTimeSlot_MixedSlotsInIntervals_ReturnsOnlyAvailableSlots() {
        // Arrange
        int numOfSlots = 10;
        List<DateTimeSlot> allAvailableSlots = createMockSlotsWithMixedAvailability(numOfSlots);
        when(dateTimeSlotRepository.findAll()).thenReturn(allAvailableSlots);

        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAvailableTimeSlot(testDate);

        // Assert
        assertNotNull(resultSlots, "The result should not be null");
        assertEquals((numOfSlots + 1) / 2, resultSlots.size(), "There should be " + (numOfSlots + 1) / 2 + "available time slots");

        // Check that all returned slots are indeed available (no QTicket assigned)
        for (DateTimeSlot slot : resultSlots) {
            assertNull(slot.getQTicket(), "Available slots should not have a QTicket associated");
        }
    }

    @Test
    public void queryAvailableTimeSlot_EmptyRepository_ReturnsEmptyList() {
        // Arrange
        when(dateTimeSlotRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAvailableTimeSlot(testDate);

        // Assert
        assertTrue(resultSlots.isEmpty(), "The available time slots should be empty.");
    }

    @Test
    public void queryAvailableTimeSlot_AllSlotsBooked_ReturnsEmptyList() {
        // Arrange
        int numOfSlots = 10;
        List<DateTimeSlot> allAvailableSlots = createMockSlotsInAdvance(numOfSlots, false);
        when(dateTimeSlotRepository.findAll()).thenReturn(allAvailableSlots);

        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAvailableTimeSlot(testDate);

        // Assert
        assertTrue(resultSlots.isEmpty(), "The list of available time slots should be empty.");
    }

    @Test
    void queryAllAvailableTimeSlot_MixedAvailability_ReturnsOnlyAvailableSlots() {
        // Arrange
        int numOfSlots = 10;
        List<DateTimeSlot> allAvailableSlots = createMockSlotsWithMixedAvailability(numOfSlots);
        when(dateTimeSlotRepository.findAll()).thenReturn(allAvailableSlots);

        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAllAvailableTimeSlot(testDate);

        // Assert
        verify(dateTimeSlotRepository).findAll(); // Verify repository was called
        assertNotNull(resultSlots); // Check that the result is not null
        assertEquals(5, resultSlots.size()); // Check that only the available slots are returned

        // Check all returned slots are available and match the specified date
        for (DateTimeSlot slot : resultSlots) {
            assertNull(slot.getQTicket(), "Available slots should not have a QTicket associated");
            assertEquals(testDate, slot.getStartDateTime().toLocalDate(), "Slot date should match the specified date");
        }
    }

    @Test
    void queryAllAvailableTimeSlot_AllSlotsBooked_ReturnsEmptyList() {
        // Arrange
        int numOfSlots = 10;
        List<DateTimeSlot> allAvailableSlots = createMockSlotsInAdvance(numOfSlots, false);
        when(dateTimeSlotRepository.findAll()).thenReturn(allAvailableSlots);

        // Act
        List<DateTimeSlot> resultSlots = queueService.queryAllAvailableTimeSlot(testDate);

        // Assert
        assertTrue(resultSlots.isEmpty(), "The result should be an empty list for no available slots");
    }

    @Test
    public void getNewQueueTicket_AvailableSlots_IssuesNewTicket() {
        // Arrange
        int numOfSlots = 10;
        List<DateTimeSlot> allAvailableSlots = createMockSlotsToday(numOfSlots, true);
        when(dateTimeSlotRepository.findAll()).thenReturn(allAvailableSlots);

        Person patient = new Person(
                "sam",
                "tom",
                "tom@gmail.com",
                LocalDate.of(1992, Month.APRIL, 1),
                net.csd.website.model.Person.Condition.NONE,
                "admin", "goodpass", 
                "goodpass", "T2232232F",
                Authority.ROLE_ADMIN);
        patient.setId(1L);

        QTicket newTicket = new QTicket();
        // Set additional properties on newTicket if necessary
        when(qTicketRepository.save(any(QTicket.class))).thenReturn(newTicket);
        // when(queueService).thenReturn(queueService);

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

    @Test
    void getNewQueueTicket_AllSlotsBooked_ThrowsNoAvailableSlotException() {
        // Arrange
        Person patient = new Person(); // Set up the patient object with necessary details

        int numOfSlots = 10;
        List<DateTimeSlot> allAvailableSlots = createMockSlotsToday(numOfSlots, false);
        when(dateTimeSlotRepository.findAll()).thenReturn(allAvailableSlots);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            queueService.getNewQueueTicket(patient);
        });

        String expectedMessage = "No available time slot for today"; // Assuming this is your exception message
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getNewQueueTicket_PatientHasExistingTicketForToday_ThrowsException() {
        // Arrange
        Person patient = new Person();
        patient.setId(1L); // Set a mock patient ID
        // Other necessary patient properties can be set here

        LocalDate today = LocalDate.now();
        QTicket existingTicket = new QTicket();
        existingTicket.setCreatedAt(LocalDateTime.of(today, LocalTime.now()));
        existingTicket.setPerson(patient);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            queueService.getNewQueueTicket(patient);
        }, "Expected getNewQueueTicket to throw, but it didn't");
    }

    @Test
    void findLatestTicketByPersonId_WithNonEmptyList_ReturnsFirstTicket() {
        // Arrange
        Long personId = 1L;
        QTicket oldestTicket = new QTicket(); // assuming QTicket has a default constructor
        QTicket latestTicket = new QTicket(); // this should be the ticket returned
        List<QTicket> ticketList = Arrays.asList(oldestTicket, latestTicket);

        when(qTicketRepository.findLatestTicketByPersonId(personId)).thenReturn(ticketList);

        // Act
        QTicket result = queueService.findLatestTicketByPersonId(personId);

        // Assert
        assertEquals(latestTicket, result, "The method should return the first QTicket in the list");
    }

    @Test
    void findLatestTicketByPersonId_EmptyList_ReturnsNull() {
        // Arrange
        Long personId = 1L;
        when(qTicketRepository.findLatestTicketByPersonId(personId)).thenReturn(Collections.emptyList());

        // Act
        QTicket result = queueService.findLatestTicketByPersonId(personId);

        // Assert
        assertNull(result, "The method should return null when the list is empty");
    }

    @Test
    void handlePatientWaiting_EmptyWaitingQueue_NoActionTaken() {
        // Arrange
        when(qTicketRepository.findByQStatus(QStatus.WAITING)).thenReturn(Collections.emptyList());

        // Act
        queueService.handlePatientWaiting();

        // Assert
        verify(qTicketRepository, never()).save(any(QTicket.class));
    }

    @Test
    void handlePatientWaiting_TicketsWithPastDateTime_SetToInProgress() {
        // Arrange
        LocalDateTime pastDateTime = LocalDateTime.now().minusHours(1);

        Person person1 = new Person();
        person1.setUsername("JohnDoe");

        DateTimeSlot dateTimeSlot1 = new DateTimeSlot();
        dateTimeSlot1.setStartDateTime(pastDateTime);

        QTicket ticket1 = new QTicket();
        ticket1.setPerson(person1);
        ticket1.setDatetimeSlot(dateTimeSlot1);
        ticket1.setQStatus(QStatus.WAITING);

        List<QTicket> waitingTickets = Arrays.asList(ticket1);

        when(qTicketRepository.findByQStatus(QStatus.WAITING)).thenReturn(waitingTickets);

        // Act
        queueService.handlePatientWaiting();

        // Assert
        verify(qTicketRepository).save(ticket1); // Verify that the ticket has been saved
        assertEquals(QStatus.IN_PROGRESS, ticket1.getQStatus()); // Assert that the status has been updated
    }

    @Test
    void handlePatientWaiting_TicketsWithFutureDateTime_RemainInWaiting() {
        // Arrange
        QTicket qTicketWaiting = mock(QTicket.class);
        DateTimeSlot futureDateTimeSlot = mock(DateTimeSlot.class);

        when(futureDateTimeSlot.getStartDateTime()).thenReturn(LocalDateTime.now().plusHours(1));
        when(qTicketWaiting.getDatetimeSlot()).thenReturn(futureDateTimeSlot);
        when(qTicketRepository.findByQStatus(QStatus.WAITING)).thenReturn(Arrays.asList(qTicketWaiting));

        // Act
        queueService.handlePatientWaiting();

        // Assert
        verify(qTicketWaiting, never()).setQStatus(QStatus.IN_PROGRESS);
        verify(qTicketRepository, never()).save(qTicketWaiting);
    }

    @Test
    void handlePatientInProgress_EndedAppointments_UpdateToAwaitingPayment() {
        // Arrange
        QTicket inProgressTicket = new QTicket();
        inProgressTicket.setQStatus(QStatus.IN_PROGRESS);
        DateTimeSlot pastDateTimeSlot = new DateTimeSlot();
        pastDateTimeSlot.setEndDateTime(LocalDateTime.now().minusHours(1)); // assuming a past end date
        inProgressTicket.setDatetimeSlot(pastDateTimeSlot);

        Person person = new Person();
        person.setUsername("JohnDoe");
        inProgressTicket.setPerson(person);

        List<QTicket> inProgressTickets = Collections.singletonList(inProgressTicket);

        when(qTicketRepository.findByQStatus(QStatus.IN_PROGRESS)).thenReturn(inProgressTickets);

        // Act
        queueService.handlePatientInProgress();

        // Assert
        // Verify that save was called on the repository
        verify(qTicketRepository, times(1)).save(inProgressTicket);

        // Assert the QStatus was changed
        assertEquals(QStatus.AWAITINGPAYMENT, inProgressTicket.getQStatus());

        // Since the amount is random, you can't assert a specific value, but you can
        // assert it's within a range
        assertTrue(inProgressTicket.getAmountDue() >= 0 && inProgressTicket.getAmountDue() <= 100);
    }
}
