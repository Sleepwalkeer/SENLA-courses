package eu.senla.dao;

import eu.senla.entities.Category;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDaoImpl extends AbstractDAO<Integer, Category> implements CategoryDao {

    @Override
    Class<Category> getEntityClass() {
        return Category.class;
    }
}
