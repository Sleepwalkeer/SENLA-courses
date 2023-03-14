package eu.senla.repository;

import eu.senla.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {

    Optional<Item> findByName(String name);

    List<Item> findByIdIn(List<Long> itemIds);

    @Modifying
    @Query("UPDATE Item i SET i.quantity = i.quantity - 1 WHERE i.id IN (:ids)")
    void decrementQuantityForItems(@Param("ids") List<Long> itemIds);
}
