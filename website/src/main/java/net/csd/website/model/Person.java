package net.csd.website.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "people")
public class Person implements UserDetails {

    public enum Condition {
        NONE, MILD, MODERATE, SEVERE
    }

    public enum Authority {
        ROLE_ADMIN, ROLE_PATIENT, ROLE_PATIENT_UNVERIFIED
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "person_birthday")
    private LocalDate birthDate;

    @Column(name = "person_condition")
    @Enumerated(EnumType.STRING)
    private Condition condition = Condition.NONE;

    @NotNull(message = "Username should not be null")
    @Size(min = 5, max = 20, message = "Username should be between 5 and 20 characters")
    // @Column(name = "username")
    @Column(name = "username", unique = true)
    private String username;

    @NotNull(message = "Password should not be null")
    @Size(min = 8, message = "Password should be at least 8 characters")
    @Column(name = "password")
    private String password;
    
    @Column(name = "authorities")
    private Authority authorities = Authority.ROLE_PATIENT_UNVERIFIED;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    // Ignore the field in both JSON serialization and deserialization
    @JsonIgnore
    private List<QTicket> queueticket;

    public Person(String firstName, String lastName, String emailId,
            LocalDate birthDate, Condition condition, String username, String password,
            Authority authorities) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.birthDate = birthDate;
        this.condition = condition;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    // for user registration
    public Person(String firstName, String lastName, String emailId,
            LocalDate birthDate, Condition condition, String username, String password
            ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.birthDate = birthDate;
        this.condition = condition;
        this.username = username;
        this.password = password;
        this.authorities = Authority.ROLE_PATIENT;
    }


    /*
     * Return a collection of authorities (roles) granted to the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = authorities.toString();
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    /*
     * Return the age of the user.
     */
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public int getRiskLevel() {
        /*
         * - Low Risk: (1)
         * - Young age with mild conditions.
         * - Middle-aged with no or mild conditions.
         * 
         * - Medium Risk: (2)
         * - Young age with moderate conditions.
         * - Middle-aged with moderate conditions.
         * - Senior age with mild conditions.
         * 
         * - High Risk: (3)
         * - Young or Middle-aged with severe conditions.
         * - Senior age with moderate or severe conditions.
         * 
         *  - Age Groups:
         * - Young: 0-17
         * - Middle-aged: 18-59
         * - Senior: 60+
         * 
         * - 0 stands for unknown risk level, tag it as low risk.
         * 
         */
        if (getAge() < 18) {
            if (condition == Condition.MILD || condition == Condition.NONE) {
                return 1;
            } else if (condition == Condition.MODERATE) {
                return 2;
            } else if (condition == Condition.SEVERE) {
                return 3;
            }
        } else if (getAge() >= 18 && getAge() < 60) {
            if (condition == Condition.NONE || condition == Condition.MILD) {
                return 1;
            } else if (condition == Condition.MODERATE) {
                return 2;
            } else if (condition == Condition.SEVERE) {
                return 3;
            }
        } else if (getAge() >= 60) {
            if (condition == Condition.NONE || condition == Condition.MILD) {
                return 2;
            } else if (condition == Condition.MODERATE || condition == Condition.SEVERE) {
                return 3;
            }
        }
        return 0;
    }

    /*
     * The various is___Expired() methods return a boolean to indicate whether
     * or not the user’s account is enabled or expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
