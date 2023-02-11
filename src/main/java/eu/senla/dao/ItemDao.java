package eu.senla.dao;

import eu.senla.entities.Item;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ItemDao {
    List<Item> findAll();

    Item findById(Integer id);

    Item findByIdEager(Integer id);

    Item update(Item item);

    Item save(Item item);

    void delete(Item item);

    void deleteById(Integer id);
    List<Item> GetItemsMoreExpensiveThan(BigDecimal bigDecimal);
}
