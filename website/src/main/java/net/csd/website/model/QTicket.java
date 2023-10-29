package net.csd.website.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QTicket {
    public enum QStatus {
        WAITING, // Waiting for a doctor
        IN_PROGRESS, // Doctor is seeing the patient
        AWAITINGPAYMENT, // Patient has been seen, but payment is not yet made
        COMPLETED, // Patient has been seen and payment has been made
        CANCELLED // Patient has cancelled the appointment
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketno;

    @Enumerated(EnumType.STRING)
    private QStatus qStatus = QStatus.WAITING;

    private double amountDue = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "datetime_slot_id", nullable = true)
    private DateTimeSlot datetimeSlot;

    

    

}
