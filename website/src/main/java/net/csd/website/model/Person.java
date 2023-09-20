package net.csd.website.model;

import java.time.LocalDate;
// import java.time.Period; // for calculating age

import jakarta.persistence.*;

@Entity
@Table(name = "people")
public class Person {

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

    @Column(name = "person_age")
    private int age;
    
    @Column(name = "person_condition")
    private String condition;

    @Column(name = "person_user_type")
    private String userType;

    public Person() {
        
    }

    public Person(long id, String firstName, String lastName, String emailId, int age, String condition, String userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.age = age;
        this.condition = condition;
        this.userType = userType;
    }

    public Person(long id, String firstName, String lastName, String emailId, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.age = age;
    }

    public Person(String firstName, String lastName, String emailId, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.birthDate = birthDate;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // public int getCurrentAge() {
    //     LocalDate curDate = LocalDate.now();
    //     LocalDate birthDate = this.birthDate;

    //     return Period.between(curDate, birthDate).getYears();
    // }

    // public LocalDate getBirthDate() {
    //     return birthDate;
    // }

    // public void setBirthDate(LocalDate birthDate) {
    //     this.birthDate = birthDate;
    // }
}
