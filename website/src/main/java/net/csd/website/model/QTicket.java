package net.csd.website.model;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // @OneToOne
    // @JoinColumn(name = "datetime_id", nullable = false)
    // private DateTimeSlot datetimeSlot;

    

}
