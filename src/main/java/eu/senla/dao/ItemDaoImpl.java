package eu.senla.dao;

import eu.senla.entities.Category;
import eu.senla.entities.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ItemDaoImpl extends AbstractDAO<Integer, Item> implements ItemDao {

    @Override
    Class<Item> getEntityClass() {
        return Item.class;
    }

}
