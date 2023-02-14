package eu.senla.dao;

import eu.senla.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoryDaoImpl extends AbstractDAO<Integer, Category> implements CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    Class<Category> getEntityClass() {
        return Category.class;
    }

    @Override
    public boolean delete(Category entity) {
        return deleteById(entity.getId());
    }
}
