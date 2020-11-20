package org.example.dao;

import java.util.List;

public interface InterfaceDAO<T> {
    List<T> findAll();

    T findById(long id);

    long countRecipe(long id);

    void delete(long id);

    void update(long id, T t);

    void save(T t);
}
