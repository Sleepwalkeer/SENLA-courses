package eu.senla.dao;

import eu.senla.entities.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryDao {
    List<Category> findAll();

    Category findById(Integer id);

    Category update(Category category);

    Category save(Category category);

    void delete(Category category);

    void deleteById(Integer id);
}
