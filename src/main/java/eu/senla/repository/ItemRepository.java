package eu.senla.repository;

import eu.senla.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByName(String name);

    @Override
    Page<Item> findAll(@NonNull Pageable pageable);
    //    Item findByIdEager(Integer id);
//    List<Item> GetItemsMoreExpensiveThan(BigDecimal bigDecimal);
}
