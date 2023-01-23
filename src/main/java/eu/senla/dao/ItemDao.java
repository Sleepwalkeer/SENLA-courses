package eu.senla.dao;

import eu.senla.entities.Item;

import java.util.List;

public interface ItemDao {
    List<Item> getAll();

    Item getById(Item passedItem);

    Item update(Item passedItem, int newQuantity);

    Item create(Item passedItem);

    void delete(Item passedItem);
}
