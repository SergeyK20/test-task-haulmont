package org.example.dao;

import org.example.entity.Patient;
import org.example.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;


@Service
public class PatientDAO implements InterfaceDAO<Patient> {

    private PatientRepository patientRepository;

    @Autowired
    public PatientDAO(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findById(long id) {
        if (patientRepository.findById(id).isPresent()) {
            return patientRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public long countRecipe(long id) {
        return patientRepository.countRecipeOfPatient(id);
    }

    @Override
    public void delete(long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public void update(long id, Patient newPatient) {
        Patient patient = findById(id);
        patient.setName(newPatient.getName());
        patient.setSurname(newPatient.getSurname());
        patient.setPatronymic(newPatient.getPatronymic());
        patient.setPhoneNumber(newPatient.getPhoneNumber());
        save(patient);
    }

    @Override
    public void save(Patient patient) throws NullPointerException {
        Objects.requireNonNull(patient, "patient is null on save");

        patient.setRecipes(new HashSet<>());
        patientRepository.save(patient);
    }
}
