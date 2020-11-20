package org.example.repository;

import org.example.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query(value = "Select Recipe from Recipe where description = :description", nativeQuery = true)
    List<Recipe> findAllByDescription(@Param("description") String description);

    @Query(value = "Select Recipe from Recipe where priority = :priority", nativeQuery = true)
    List<Recipe> findAllByPriority(@Param("priority") String priority);

    @Query(value = "Select Recipe from Recipe where id_patient = :id", nativeQuery = true)
    List<Recipe> findAllByPatient(@Param("id") long id);

    @Query(value = "Select count(*) from Recipe where id_doctor = :id", nativeQuery = true)
    long findAllCountRecipeByIdDoctor(@Param("id") long id);
}
