package net.csd.website.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.csd.website.exception.ResourceNotFoundException;
import net.csd.website.model.Person;
import net.csd.website.repository.PersonRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1/")
public class PersonController {

	@Autowired
	private PersonRepository personRepository;
	
	// get all People
	@GetMapping("/people")
	public List<Person> getAllPeople(){
		return personRepository.findAll();
	}		
	
	// create people rest api
	@PostMapping("/people")
	public Person createPerson(@RequestBody Person person) {
		return personRepository.save(person);
	}
	
	// get person by id rest api
	@GetMapping("/people/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
		Person person = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));
		return ResponseEntity.ok(person);
	}
	
	// update person rest api
	@PutMapping("/people/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personDetails){
		Person person = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));
		
		person.setFirstName(personDetails.getFirstName());
		person.setLastName(personDetails.getLastName());
		person.setEmailId(personDetails.getEmailId());
		
		Person updatedPerson = personRepository.save(person);
		return ResponseEntity.ok(updatedPerson);
	}
	
	// delete person rest api
	@DeleteMapping("/people/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id){
		Person person = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person don't exist with id :" + id));
		
		personRepository.delete(person);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
