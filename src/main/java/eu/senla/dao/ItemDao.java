package eu.senla.dao;

import eu.senla.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDao  extends JpaRepository<Item,Long> {
//    List<Item> findAll();
//
//    Optional<Item> findById(Integer id);
//
//    Item findByIdEager(Integer id);
//
//    Item update(Item item);
//
//    void save(Item item);
//
//    boolean delete(Item item);
//
//    boolean deleteById(Integer id);
//
//    List<Item> GetItemsMoreExpensiveThan(BigDecimal bigDecimal);
}
