// package net.csd.website;

// import static org.junit.jupiter.api.Assertions.*;

// import java.net.URI;
// import java.time.LocalDate;
// import java.time.Month;
// import java.util.Optional;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import net.csd.website.model.Person;
// import net.csd.website.model.QTicket;
// import net.csd.website.repository.PersonRepository;
// import net.csd.website.repository.QTicketRepository;

// /** Start an actual HTTP server listening at a random port*/
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// public class QueueIntegratedTest {
//     @LocalServerPort
//     private int port;

//     // private final String baseUrl = "http://localhost:"; http://localhost:8080/api/v1
//     private final String baseUrl = "http://localhost:" + port + "/api/v1";

//     @Autowired
//     /**
//      * Use TestRestTemplate for testing a real instance of your application as an external actor.
//      * TestRestTemplate is just a convenient subclass of RestTemplate that is suitable for integration tests.
//      * It is fault tolerant, and optionally can carry Basic authentication headers.
//     */
//     private TestRestTemplate restTemplate;

//     @Autowired
//     private QTicketRepository qTicketRepository;

//     @Autowired
//     private PersonRepository personRepository;

//     @Autowired
//     private BCryptPasswordEncoder encoder;

//     @AfterEach
//     void tearDown() {
//       // clear the database after each test
//       qTicketRepository.deleteAll();
//       personRepository.deleteAll();
//     }

//     private Person createTestPerson() {
//     Person person = new Person("testuser", "testpass", "test@eml.com", LocalDate.of(2000, Month.JANUARY, 1),
//             Person.Condition.MODERATE, "testuser", encoder.encode("testpassword"), Person.Authority.ROLE_PATIENT);
//     return personRepository.save(person);
// }

// private QTicket createTestQTicket(Person person) {
//     QTicket qTicket = new QTicket();
//     // set qTicket attributes here, e.g., qTicket.setPerson(person);
//     return qTicketRepository.save(qTicket);
// }

// @Test
// public void testAddPerson() {
//     Person person = new Person("JohnDoe", "testpass", "johndoe@eml.com", LocalDate.of(1990, Month.MARCH, 15),
//             Person.Condition.MODERATE, "johndoe", encoder.encode("testpassword"), Person.Authority.ROLE_PATIENT);
//     HttpEntity<Person> request = new HttpEntity<>(person);
//     ResponseEntity<Person> response = restTemplate.postForEntity(baseUrl + "/people", request, Person.class);

//     assertEquals(200, response.getStatusCodeValue());
//     assertNotNull(response.getBody().getUsername());
// }

// @Test
// public void testGetPerson() {
//     Person savedPerson = createTestPerson();
//     ResponseEntity<Person> response = restTemplate.getForEntity(baseUrl + "/people/" + savedPerson.getId(), Person.class);

//     assertEquals(200, response.getStatusCodeValue());
//     assertEquals(savedPerson.getUsername(), response.getBody().getUsername());
// }

// // @Test
// // public void testAddQTicket() {
// //     Person person = createTestPerson();
// //     QTicket qTicket = new QTicket();
// //     // set qTicket attributes here, e.g., qTicket.setPerson(person);
    
// //     HttpEntity<QTicket> request = new HttpEntity<>(qTicket);
// //     ResponseEntity<QTicket> response = restTemplate.postForEntity(baseUrl + "/qtickets", request, QTicket.class);

// //     assertEquals(200, response.getStatusCodeValue());
// //     assertNotNull(response.getBody().getId());
// // }

// // @Test
// // public void testGetQTicket() {
// //     Person person = createTestPerson();
// //     QTicket savedQTicket = createTestQTicket(person);
// //     ResponseEntity<QTicket> response = restTemplate.getForEntity(baseUrl + "/qtickets/" + savedQTicket.getId(), QTicket.class);

// //     assertEquals(200, response.getStatusCodeValue());
// //     assertEquals(savedQTicket.getId(), response.getBody().getId());
// // }



// }

package net.csd.website;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Optional;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.csd.website.model.Person;
import net.csd.website.repository.PersonRepository;
import net.csd.website.repository.QTicketRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class QueueIntegratedTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private QTicketRepository qTicketRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1";
        
        // Insert setup data or configurations if needed
    }

    @AfterEach
    void tearDown() {
        qTicketRepository.deleteAll();
        personRepository.deleteAll();
    }

    // @Test
    // public void testAddPerson() {
    //     Person person = new Person("testuser", "testlastname", "testemail@test.com", LocalDate.now(), Person.Condition.MODERATE, "testusername", encoder.encode("testpassword"), Person.Authority.ROLE_ADMIN);
        
    //     HttpEntity<Person> request = new HttpEntity<>(person);
    //     ResponseEntity<Person> response = restTemplate.postForEntity(baseUrl + "/people", request, Person.class);

    //     assertEquals(HttpStatus.CREATED, response.getStatusCode());
    //     // assertNotNull(response.getBody().getId());
    // }

    @Test
    public void testGetPerson() {
        String username = "testuser2";
        String password = "testpassword2";
        Person savedPerson = personRepository.save(
            new Person("testuser2", "testlastname2", "testemail2@test.com", 
            LocalDate.now(), Person.Condition.NONE, username, encoder.encode(password), 
            Person.Authority.ROLE_ADMIN));
        long personId = savedPerson.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + username + " " + password);
        // headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String url = baseUrl + "/people/"+ personId;

        ResponseEntity<Person> response = restTemplate.exchange(
            url, HttpMethod.GET, entity, Person.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedPerson.getUsername(), response.getBody().getUsername());
    }

    // Continue adding tests for other API endpoints in the same fashion...
}
