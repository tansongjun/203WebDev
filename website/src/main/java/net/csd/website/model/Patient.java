package net.csd.website.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Patient extends Person {

    @Column(name = "person_condition_temp")
    private String[] condition_temp;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    // Ignore the field in both JSON serialization and deserialization
    private List<QTicket> queueticket;

    public Patient setPatient(Patient patient) {
        this.setFirstName(patient.getFirstName());
        this.setLastName(patient.getLastName());
        this.setEmailId(patient.getEmailId());
        this.setAge(patient.getAge());
        // this.setCondition(patient.getCondition());
        // this.setUserType(patient.getUserType());
        this.setCondition_temp(patient.getCondition_temp());
        return this;
    }

    

    
}
