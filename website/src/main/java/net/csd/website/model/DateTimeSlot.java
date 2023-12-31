package net.csd.website.model;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class DateTimeSlot {
    
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) 
    private Room room;

    @OneToOne(mappedBy = "datetimeSlot")
    private QTicket qTicket;

    // Constructors
    public DateTimeSlot(long id) {
        this.id = id;
    }

}
