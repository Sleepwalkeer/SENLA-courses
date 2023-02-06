package eu.senla.dao;

import eu.senla.entities.Category;
import eu.senla.entities.Item;
import eu.senla.entities.Item;

import java.util.List;

public interface ItemDao {
    List<Item> findAll();

    Item findById(Integer id);

    Item update(Item item);

    Item save(Item item);

    void delete(Item item);

    void deleteById(Integer id);
}
