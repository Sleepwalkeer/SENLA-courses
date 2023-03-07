package eu.senla.dao;

import eu.senla.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
//    List<Category> findAll();
//
//    Optional<Category> findById(Integer id);
//
//    Category update(Category category);
//
//    void save(Category category);
//
//    boolean delete(Category category);
//
//    boolean deleteById(Integer id);
}
