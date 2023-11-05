package net.csd.website.controller;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import net.csd.website.exception.PatientNotVerifiedException;
import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.LoginForm;
import net.csd.website.model.Person;
import net.csd.website.model.Person.Authority;
import net.csd.website.model.Person.Condition;
import net.csd.website.repository.PersonRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class PersonController {
	@Autowired
	private PersonRepository persons;
	private BCryptPasswordEncoder encoder;

	public PersonController(PersonRepository persons, BCryptPasswordEncoder encoder) {
		this.persons = persons;
		this.encoder = encoder;
	}

	// API: GET(Read) ALL
	@GetMapping("/people")
	public List<Person> getAllPeople() {
		return persons.findAll();
	}

	// API: POST(Create) people
	@PostMapping("/people")
	public Person createPerson(@Valid @RequestBody Person person) {
		boolean checkUser = persons.findByUsername(person.getUsername()).isEmpty();
		if (!checkUser) {
			throw new ResourceNotFoundException("Username already exists");
		}
		person.setPasswordUnencryted(person.getPassword());
		person.setPassword(encoder.encode(person.getPassword()));
		
		return persons.save(person);
	}

	// API: GET(Read) people BY ID
	@GetMapping("/people/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
		Person person = persons.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));
		return ResponseEntity.ok(person);
	}

	@GetMapping("/login/{username}")
	public ResponseEntity<Person> login(@PathVariable String username) {
		Optional<Person> person = persons.findByUsername(username);
		if (!person.isPresent()) {
			throw new ResourceNotFoundException("Not Found! Contact Admin");
		}else{
			Person hasPerson = person.get();
			if (!hasPerson.getAuthorities().iterator().next().toString().equals("ROLE_PATIENT")) {
				throw new PatientNotVerifiedException("Patient not verified");
			}
			return ResponseEntity.ok(person.get());
		}
	}
	
	@GetMapping("/loginNRIC/{nric}")
	public ResponseEntity<Map<String, Object>> loginNRIC(@PathVariable String nric) {
		Optional<Person> person = persons.findByNric(nric);
		if (!person.isPresent()) {
			throw new ResourceNotFoundException("Not Found! Contact Admin");
		}else{
			Person hasPerson = person.get();
			if (hasPerson.getAuthorities().iterator().next().toString().equals("ROLE_PATIENT_UNVERIFIED")) {
				throw new PatientNotVerifiedException("Patient not verified");
			}
			Map<String, Object> response = new HashMap<>();
			response.put("person", hasPerson);
			response.put("password", hasPerson.getPasswordUnencryted(nric));
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping("/patient/registration")
	public Person registerPatient(
		@RequestBody
		@Valid Person person ) {

		person.setAuthorities(Authority.ROLE_PATIENT_UNVERIFIED);

		boolean checkUser = persons.findByUsername(person.getUsername()).isEmpty();
		if (!checkUser) {
			throw new ResourceNotFoundException("Username already exists");
		}

		person.setPasswordUnencryted(person.getPassword());
		person.setPassword(encoder.encode(person.getPassword()));
		return persons.save(person);
	}

	@PostMapping("/admin/person/verify/{id}")
	public ResponseEntity<Person> verifyPatient(@PathVariable Long id) {

		Person person = persons.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));

		person.setAuthorities(Authority.ROLE_PATIENT);

		Person updatedPerson = persons.save(person);
		return ResponseEntity.ok(updatedPerson);
	}

	@GetMapping("/admin/login/{username}")
	public ResponseEntity<Person> loginAdmin(@PathVariable String username) {
		Person person = persons.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Incorrect Username/Password"));
		if (person.getAuthorities().iterator().next().toString().equals("ROLE_ADMIN")) {
			return ResponseEntity.ok(person);
		} else {
			return ResponseEntity.status(403).build();
		}

	}

	// API: PUT(Update) people
	@PutMapping("/people/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable Long id, @Valid @RequestBody Person personDetails) {
		Person person = persons.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));

		person.setFirstName(personDetails.getFirstName());
		person.setLastName(personDetails.getLastName());
		person.setEmailId(personDetails.getEmailId());
		person.setBirthDate(personDetails.getBirthDate());
		// person.setAge(personDetails.getAge());
		person.setCondition(personDetails.getCondition());
		// person.setUsername(personDetails.getUsername());
		// person.setPassword(this.encoder.encode(personDetails.getPassword()));
		// person.setAuthorities(personDetails.getAuthorities().iterator().next().toString());

		Person updatedPerson = persons.save(person);
		return ResponseEntity.ok(updatedPerson);
	}

	// API: DELETE people
	@DeleteMapping("/people/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		Person person = persons.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));

		persons.delete(person);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
