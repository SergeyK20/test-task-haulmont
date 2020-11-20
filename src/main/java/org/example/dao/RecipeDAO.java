package org.example.dao;

import org.example.entity.Recipe;
import org.example.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RecipeDAO implements InterfaceDAO<Recipe> {

    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeDAO(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe findById(long id) {
        if (recipeRepository.findById(id).isPresent()) {
            return recipeRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public long countRecipe(long id) {
        return 0;
    }

    @Override
    public void delete(long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public void update(long id, Recipe newRecipe) {
        Recipe recipe = findById(id);
        recipe.setDoctor(newRecipe.getDoctor());
        recipe.setPatient(newRecipe.getPatient());
        recipe.setDateCreate(newRecipe.getDateCreate());
        recipe.setValidity(newRecipe.getValidity());
        recipe.setPriority(newRecipe.getPriority());
        recipe.setDescription(newRecipe.getDescription());
        save(recipe);
    }

    @Override
    public void save(Recipe recipe) throws NullPointerException {
        Objects.requireNonNull(recipe, "recipe is null on save");

        recipeRepository.save(recipe);
    }

    public List<Recipe> findAllByDescription(String description) {
        return recipeRepository.findAllByDescription(description);
    }

    public List<Recipe> findAllByPriority(String priority) {
        return recipeRepository.findAllByPriority(priority);
    }

    public List<Recipe> findAllByPatient(long id) {
        return recipeRepository.findAllByPatient(id);
    }
}
