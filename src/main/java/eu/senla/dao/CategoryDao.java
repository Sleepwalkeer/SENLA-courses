package eu.senla.dao;

import eu.senla.entities.Category;
import eu.senla.entities.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();

    Category findById(Integer id);

    Category update(Category category);

    Category save(Category category);

    void delete(Category category);

    void deleteById(Integer id);
}
