package eu.senla.dao;

import eu.senla.entities.Account;
import eu.senla.entities.Category;
import eu.senla.entities.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CategoryDaoImpl extends AbstractDAO<Integer, Category> implements CategoryDao {

    @Override
    Class<Category> getEntityClass() {
        return Category.class;
    }
}
