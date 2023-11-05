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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.UseMainMethod;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.With;
import net.csd.website.model.Person;
import net.csd.website.repository.PersonRepository;
import net.csd.website.repository.QTicketRepository;
import net.csd.website.service.RoomService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = WebsiteApplication.class, useMainMethod = UseMainMethod.WHEN_AVAILABLE)

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

    @Autowired
    private RoomService roomService;

    private String baseUrl;

    // @BeforeAll
    // static void beforeAll() {
    // // Insert setup data or configurations if needed

    // }

    @BeforeEach
    void setUp() {

        baseUrl = "http://localhost:" + port + "/api/v1";
        // WebsiteApplication.intialSetUp(personRepository, encoder, roomService);

    }

    @AfterEach
    void tearDown() {
        WebsiteApplication.intialSetUp(personRepository, encoder, roomService);

    }

    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + " " + password;
                String authHeader = "Basic " + auth;
                set(AUTHORIZATION, authHeader);
            }
        };
    }

    // @Test
    // public void testAddPerson() {
    // Person person = new Person("testuser", "testlastname", "testemail@test.com",
    // LocalDate.now(), Person.Condition.MODERATE, "testusername",
    // encoder.encode("testpassword"), Person.Authority.ROLE_ADMIN);

    // HttpEntity<Person> request = new HttpEntity<>(person);
    // ResponseEntity<Person> response = restTemplate.postForEntity(baseUrl +
    // "/people", request, Person.class);

    // assertEquals(HttpStatus.CREATED, response.getStatusCode());
    // // assertNotNull(response.getBody().getId());
    // }

    // @Test
    // // @WithMockUser(username = "admin", password = "goodpass", roles =
    // // "ROLE_ADMIN")
    // public void testGetPerson() {
    //     String username = "testuser2";
    //     String password = "testpassword2";
    //     Person savedPerson = personRepository.save(
    //             new Person("testuser2", "testlastname2", "testemail2@test.com",
    //                     LocalDate.now(), Person.Condition.NONE, username, encoder.encode(password),
    //                     Person.Authority.ROLE_ADMIN));
    //     long personId = savedPerson.getId();
    //     System.out.println("personId: " + personId);

    //     // HttpEntity<Void> entity = new HttpEntity<>(createHeaders("admin", "goodpass"));

    //     String url = baseUrl + "/people/" + personId;

    //     // ResponseEntity<Person> response = restTemplate.exchange(
    //     //         url, HttpMethod.GET, entity, Person.class);

    //     Person result = restTemplate.withBasicAuth("admin", "goodpass")
    //             .getForObject(url, Person.class);

    //     assertEquals(HttpStatus.OK, result.getStatusCode());
    //     // assertEquals(savedPerson.getUsername(), response.getBody().getUsername());
    // }

    // Continue adding tests for other API endpoints in the same fashion...
}
