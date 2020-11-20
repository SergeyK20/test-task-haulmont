package org.example;

import org.example.dao.DoctorDAO;
import org.example.dao.PatientDAO;
import org.example.dao.RecipeDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.text.ParseException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private DoctorDAO doctorDAO;

    @Autowired
    private PatientDAO patientDAO;

    /**
     * Тест проверяющий если ли хотя бы одно значение в каждой таблицы БД
     */
    @Test
    public void shouldAnswerWithTrue() {
        assert (recipeDAO.findAll() != null);
        assert (doctorDAO.findAll() != null);
        assert (patientDAO.findAll() != null);
    }
}
