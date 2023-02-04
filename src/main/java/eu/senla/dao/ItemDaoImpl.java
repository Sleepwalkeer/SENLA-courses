package eu.senla.dao;

import eu.senla.entities.Category;
import eu.senla.entities.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemDaoImpl extends AbstractDAO<Integer, Item> implements ItemDao {

    @Override
    Class<Item> getEntityClass() {
        return Item.class;
    }
}
