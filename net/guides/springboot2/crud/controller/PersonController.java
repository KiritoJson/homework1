package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Person;
import net.guides.springboot2.crud.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/person")
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable(value = "id") Long personId)
            throws ResourceNotFoundException {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for this id :: " + personId));
        return ResponseEntity.ok().body(person);
    }

    @PostMapping("/person")
    public Person createPerson(@Validated @RequestBody Person person) {
        return personRepository.save(person);
    }

    @PutMapping("/person/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id") Long personId,
                                               @Validated @RequestBody Person personDetails) throws ResourceNotFoundException {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for this id :: " + personId));

        person.setDateOfBirth(personDetails.getDateOfBirth());
        person.setLastName(personDetails.getLastName());
        person.setFirstName(personDetails.getFirstName());
        final Person updatedPerson = personRepository.save(person);
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping("/person/{id}")
    public Map<String, Boolean> deletePerson(@PathVariable(value = "id") Long personId)
            throws ResourceNotFoundException {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for this id :: " + personId));

        personRepository.delete(person);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}