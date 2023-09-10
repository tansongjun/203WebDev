package net.csd.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.csd.website.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{

}
