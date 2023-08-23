package net.csd.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.csd.website.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

}
