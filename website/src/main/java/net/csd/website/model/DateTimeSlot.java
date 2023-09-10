package net.csd.website.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DateTimeSlot {
    
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private LocalDate date;
    private LocalTime time;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) 
    private Room room;

    // @OneToOne(mappedBy = "datetimeSlot", cascade = CascadeType.ALL)
    // private QTicket queueticket;

}
