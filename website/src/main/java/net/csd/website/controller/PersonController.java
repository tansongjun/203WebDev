package net.csd.website.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.Person;
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
	public List<Person> getAllPeople(){
		return persons.findAll();
	}
	
	// API: POST(Create) people
	@PostMapping("/people")
	public Person createPerson(@Valid @RequestBody Person person) {
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
	
	// API: PUT(Update) people
	@PutMapping("/people/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personDetails){
		Person person = persons.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));
		
		person.setFirstName(personDetails.getFirstName());
		person.setLastName(personDetails.getLastName());
		person.setEmailId(personDetails.getEmailId());
		person.setBirthDate(personDetails.getBirthDate());
		person.setAge(personDetails.getAge());
		person.setCondition(personDetails.getCondition());
        person.setUsername(person.getUsername());
        person.setPassword(this.encoder.encode(person.getPassword()));
		
		Person updatedPerson = persons.save(person);
		return ResponseEntity.ok(updatedPerson);
	}
	
	// API: DELETE people
	@DeleteMapping("/people/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id){
		Person person = persons.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));
		
		persons.delete(person);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
