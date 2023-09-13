package net.csd.website.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.csd.website.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
    // define a derived query to find user by username
    Optional<Person> findByUsername(String username);
}
