package eu.senla.repository;

import eu.senla.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing items in the system.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Finds an item by its name.
     *
     * @param name The name to search for.
     * @return An Optional containing the item if found, or empty if not.
     */
    Optional<Item> findByName(String name);

    /**
     * Finds all items with the given IDs.
     *
     * @param itemIds A list of item IDs to search for.
     * @return A list of items with the given IDs.
     */
    List<Item> findByIdIn(List<Long> itemIds);

    /**
     * Decrements the quantity of items with the given IDs by 1.
     *
     * @param itemIds A list of item IDs to update.
     */
    @Modifying
    @Query("UPDATE Item i SET i.quantity = i.quantity - 1 WHERE i.id IN (:ids)")
    void decrementQuantityForItems(@Param("ids") List<Long> itemIds);
}
