package eu.senla.repository;

import eu.senla.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents a repository for storing and retrieving {@link Item} objects in a database.
 * It extends the {@link JpaRepository} interface, which provides standard CRUD operations for JPA entities,
 * and the {@link JpaSpecificationExecutor} interface, which allows querying based on specifications.
 */
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

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

    /**
     * Increases the quantity of the item with the specified ID by the specified quantity.
     *
     * @param itemId    the ID of the item to replenish
     * @param quantity  the quantity to add to the item's current quantity
     */
    @Modifying
    @Query("UPDATE Item i SET i.quantity = i.quantity + :quantity WHERE i.id = :itemId")
    void replenishItem(@Param("itemId") Long itemId, @Param("quantity") int quantity);

    /**
     * Returns a page of items ordered by their popularity in descending order, and then by name in ascending order.
     *
     * @param pageable  the pageable object specifying the page number and size
     * @return          a page of item names and their corresponding popularity counts
     */
    @Query(value = "SELECT i.name, COUNT(oi.item_id) AS popularity " +
            "FROM order_item oi " +
            "JOIN Item i ON oi.item_id = i.id " +
            "GROUP BY i.name " +
            "ORDER BY popularity DESC, name ASC", nativeQuery = true)
    Page<Object[]> getItemsByPopularity(Pageable pageable);

}
