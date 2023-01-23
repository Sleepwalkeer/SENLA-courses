package eu.senla.dao;

import eu.senla.entities.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> getAll();

    Category getById(Category passedCategory);

    Category update(Category passedCategory, String newName);

    Category create(Category passedCategory);

    void delete(Category passedCategory);
}
