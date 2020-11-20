package org.example.repository;

import org.example.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = "Select count(*) from Recipe where id_patient = :id", nativeQuery = true)
    long countRecipeOfPatient(@Param("id") long id);
}
