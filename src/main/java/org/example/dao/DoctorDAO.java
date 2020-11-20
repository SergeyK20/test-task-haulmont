package org.example.dao;

import org.example.entity.Doctor;
import org.example.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class DoctorDAO implements InterfaceDAO<Doctor> {

    private DoctorRepository doctorRepository;

    @Autowired
    public DoctorDAO(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findById(long id) {
        if (doctorRepository.findById(id).isPresent()) {
            return doctorRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public long countRecipe(long id) {
        return doctorRepository.countRecipeOfDoctor(id);
    }

    @Override
    public void delete(long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public void update(long id, Doctor newDoctor) {
        Doctor doctor = findById(id);
        doctor.setName(newDoctor.getName());
        doctor.setSurname(newDoctor.getSurname());
        doctor.setPatronymic(newDoctor.getPatronymic());
        doctor.setSpecialization(newDoctor.getSpecialization());
        save(doctor);
    }

    @Override
    public void save(Doctor doctor) throws NullPointerException {
        Objects.requireNonNull(doctor, "doctor is null on save");

        doctor.setRecipes(new HashSet<>());
        doctorRepository.save(doctor);
    }

}
