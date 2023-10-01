package net.csd.website.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketno;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @OneToOne
    @JoinColumn(name = "datetime_slot_id", nullable = true)
    private DateTimeSlot datetimeSlot;

    // @OneToOne
    // @JoinColumn(name = "datetime_id", nullable = false)
    // private DateTimeSlot datetimeSlot;

    

}
