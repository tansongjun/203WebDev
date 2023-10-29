package net.csd.website.service;

import net.csd.website.exception.AllSlotsFilledException;
import net.csd.website.exception.InvalidDateException;
import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.Person;
import net.csd.website.model.QTicket;
import net.csd.website.model.Room;
import net.csd.website.model.WaitingQticket;
import net.csd.website.model.QTicket.QStatus;
import net.csd.website.repository.DateTimeSlotRepository;
import net.csd.website.repository.RoomRepository;
import net.csd.website.repository.WaitingQTicketRepository;
import net.csd.website.repository.QTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@Service
public class QueueService {

    private final QTicketRepository qTicketRepository;
    private final RoomRepository roomRepository;
    private final DateTimeSlotRepository dateTimeSlotRepository;
    private final WaitingQTicketRepository waitingQTicketRepository;

    private int[] queuePattern = { 3, 3, 2, 1 };
    private int queuePatternIndex = 0;

    // management of waiting no.
    private int currentWaitingNo = 1;
    private int processedWaitingNo = 0;
    private LocalDate dateForWaitingNo = LocalDate.now();

    @Autowired
    public QueueService(QTicketRepository qTicketRepository, RoomRepository roomRepository,
            DateTimeSlotRepository dateTimeSlotRepository, WaitingQTicketRepository waitingQTicketRepository) {
        this.qTicketRepository = qTicketRepository;
        this.roomRepository = roomRepository;
        this.dateTimeSlotRepository = dateTimeSlotRepository;
        this.waitingQTicketRepository = waitingQTicketRepository;
    }

    public List<DateTimeSlot> queryAvailableTimeSlot(LocalDate date) {

        // Retrieve all available rooms
        // List<Room> rooms = roomRepository.findAll();

        // Retrieve all time slots
        List<DateTimeSlot> allDateTimeSlots = dateTimeSlotRepository.findAll();

        // Filter time slots for the specified date and available slots
        List<DateTimeSlot> availableDateTimeSlots = allDateTimeSlots.stream()
                .filter(dateTimeSlot -> dateTimeSlot.getStartDateTime().toLocalDate().equals(date)
                        && dateTimeSlot.getQTicket() == null)
                .collect(Collectors.toList());

        // Create a Map to store the selected slots by LocalTime
        Map<LocalTime, DateTimeSlot> selectedSlots = new HashMap<>();

        // Iterate through the available slots and select the earliest room number for
        // each start time
        for (DateTimeSlot slot : availableDateTimeSlots) {
            LocalTime startTime = slot.getStartDateTime().toLocalTime();
            if (!selectedSlots.containsKey(startTime) ||
                    slot.getRoom().getRoomNumber() < selectedSlots.get(startTime).getRoom().getRoomNumber()) {
                selectedSlots.put(startTime, slot);
            }
        }

        // Convert the selected slots Map to a List
        List<DateTimeSlot> resultSlots = new ArrayList<>(selectedSlots.values());

        // Sort the result slots by start time (optional step)
        resultSlots.sort(Comparator.comparing(slot -> slot.getStartDateTime().toLocalTime()));

        return resultSlots;
    }

    public Boolean checkEnoughSlotsinWaitingList() {
        // Retrieve Available time slots for today
        List<DateTimeSlot> availableDateTimeSlots = queryAvailableTimeSlot(LocalDate.now());

        // Check if there is any available time slot for today and if there is enough
        // slots for the waiting queue
        int waitingListSize = currentWaitingNo - processedWaitingNo - 1;
        return availableDateTimeSlots.isEmpty() || waitingListSize == availableDateTimeSlots.size();

    }

    @Transactional
    public QTicket getNewQueueTicket(Person patient) {
        try {
            allocateWaitingQTicket();
        } catch (AllSlotsFilledException e) {
            return null;
        }

        if (checkEnoughSlotsinWaitingList()) {
            throw new RuntimeException("No available time slot for today");
        }
        // Check if the patient has already got a queue ticket for today
        List<QTicket> existingTickets = qTicketRepository.findLatestTicketByPersonId(patient.getId());
        if (!existingTickets.isEmpty()) {
            QTicket latestTicket = existingTickets.get(0);
            if (latestTicket.getCreatedAt().toLocalDate().equals(LocalDate.now())) {
                // Return null if the patient has already got a queue ticket for today
                throw new RuntimeException("Patient has already got a queue ticket for today");
            }
        }

        // Retrieve Available time slots for today
        List<DateTimeSlot> availableDateTimeSlots = queryAvailableTimeSlot(LocalDate.now());

        // Creation of either waitingQTicket or QTicket
        QTicket qTicket = null;
        DateTimeSlot dateTimeSlot = null;

        // if the patient is in the queue pattern, create a QTicket
        if (queuePattern[queuePatternIndex] == patient.getRiskLevel()) {
            queuePatternIndex = (queuePatternIndex + 1) % queuePattern.length;

            dateTimeSlot = availableDateTimeSlots.get(0); // set the dateTimeSlot to the first available slot
            qTicket = new QTicket();

            // else, create a WaitingQTicket
        } else {

            // Reset the no. for the next day
            if (dateForWaitingNo.isBefore(LocalDate.now())) {
                dateForWaitingNo = LocalDate.now();
                currentWaitingNo = 1;
                processedWaitingNo = 0;
            }
            qTicket = new WaitingQticket(currentWaitingNo++);
        }

        // Assign the patient, room, and time slot
        qTicket.setPerson(patient);
        qTicket.setDatetimeSlot(dateTimeSlot);
        qTicket.setCreatedAt(LocalDateTime.now());
        return qTicketRepository.save(qTicket);
    }

    public void allocateWaitingQTicket() {
        // Retrieve all waiting queue tickets
        List<WaitingQticket> waitingQtickets = waitingQTicketRepository.findAll();

        // Retrieve Available time slots for today
        List<DateTimeSlot> availableDateTimeSlots = queryAvailableTimeSlot(LocalDate.now());
        int availableDateTimeSlotsindex = 0;

        // Check if there is any available time slot for today and if there is enough
        // slots for the waiting queue
        int waitingListSize = currentWaitingNo - processedWaitingNo - 1;
        System.out.println("waitingListSize: " + waitingListSize);
        System.out.println("availableDateTimeSlots.size(): " + availableDateTimeSlots.size());

        if (availableDateTimeSlots.isEmpty()) {
            throw new ResourceNotFoundException("No available time slot for today");
        }
        boolean allocatedAllWaitingQticket = false;
        for (WaitingQticket waitingQticket : waitingQtickets) {
            if (waitingQticket.getDatetimeSlot() == null) {
                // if the waitingQticket is in the queue pattern, set the time slot
                if (waitingQticket.getPerson().getRiskLevel() == queuePattern[queuePatternIndex]) {

                    queuePatternIndex = (queuePatternIndex + 1) % queuePattern.length;

                    // Assign the first available time slot to the waiting queue ticket
                    waitingQticket.setDatetimeSlot(availableDateTimeSlots.get(availableDateTimeSlotsindex++));
                    processedWaitingNo++;

                    // if the WaitingListsize is equal to the availableDateTimeSlots.size(),
                    // set the time slot for all the remaining waitingQtickets.
                } else if (waitingListSize == availableDateTimeSlots.size()) {

                    // Assign the first available time slot to the waiting queue ticket
                    waitingQticket.setDatetimeSlot(availableDateTimeSlots.get(availableDateTimeSlotsindex++));
                    processedWaitingNo++;
                    System.out.println("allocated");
                    allocatedAllWaitingQticket = true;
                } else {
                    return;
                }
            }
        }
        if (allocatedAllWaitingQticket) {
            throw new AllSlotsFilledException("All slots are filled");
        }
    }

    public QTicket findLatestTicketByPersonId(Long personId) {
        List<QTicket> tickets = qTicketRepository.findLatestTicketByPersonId(personId);
        return tickets.isEmpty() ? null : tickets.get(0);
    }

    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.SECONDS, initialDelay = 30)
    public void handlePatientFlow(){
        handlePatientWaiting();

        handlePatientInProgress();
        
        System.out.println("handlePatientFlow() is running");
    }

    private void handlePatientWaiting(){
        List<QTicket> qTickets = qTicketRepository.findByQStatus(QStatus.WAITING);
        for (QTicket qTicket : qTickets) {
            if (qTicket.getDatetimeSlot().getStartDateTime().isBefore(LocalDateTime.now())) {
                qTicket.setQStatus(QStatus.IN_PROGRESS);
                qTicketRepository.save(qTicket);
                System.out.println("Patient " + qTicket.getPerson().getUsername() + " is now in progress");
            }
        }
    }

    public void handlePatientInProgress(){
        List<QTicket> qTickets = qTicketRepository.findByQStatus(QStatus.IN_PROGRESS);
        for (QTicket qTicket : qTickets) {
            if (qTicket.getDatetimeSlot().getEndDateTime().isBefore(LocalDateTime.now())) {
                qTicket.setQStatus(QStatus.AWAITINGPAYMENT);

                // generate random amount due
                double amountDue = new Random().nextDouble() * 100;
                qTicket.setAmountDue(amountDue);
                qTicketRepository.save(qTicket);
                System.out.println("Patient " + qTicket.getPerson().getUsername() + " is now awaiting payment");
            }
        }
    }

}
