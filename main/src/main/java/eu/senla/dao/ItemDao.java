package eu.senla.dao;

import eu.senla.entities.Item;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ItemDao {
    private final List<Item> items = new LinkedList<>();

    public List<Item> getItems() {
        return items;
    }

    public List<Item> getAll() {
        return getItems();
    }

    public Item getById(Item passedItem) {
        for (Item item : items) {
            if (passedItem.getId() == item.getId()) {
                return item;
            }
        }
        return null;
    }

    public Item update(Item passedItem, int newQuantity) {
        for (Item item : items) {
            if (passedItem.getId() == item.getId()) {
                item.setQuantity(newQuantity);
                return item;
            }
        }
        return null;
    }

    public Item create(Item passedItem) {
        items.add(passedItem);
        return passedItem;
    }

    public void delete(Item passedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (passedItem.getId() == items.get(i).getId()) {
                items.remove(i);
                return;
            }
        }
    }
}
