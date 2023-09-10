package net.csd.website.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Room {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomid;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DateTimeSlot> datetimeSlot;


}
