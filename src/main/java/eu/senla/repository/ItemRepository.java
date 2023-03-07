package eu.senla.repository;

import eu.senla.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
//    Item findByIdEager(Integer id);
//    List<Item> GetItemsMoreExpensiveThan(BigDecimal bigDecimal);
}
