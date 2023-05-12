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
     * Sets the "is_deleted" flag to true for the item with the specified ID.
     *
     * @param itemId the ID of the item to delete
     */
    @Modifying
    @Query("UPDATE Item i SET i.deleted = true WHERE i.id = :itemId")
    void deleteById(@Param("itemId") Long itemId);

    /**
     * Sets the availability of the item with the specified ID to true.
     * Does nothing if the item was already available.
     *
     * @param itemId the ID of the item to replenish
     */
    @Modifying
    @Query("UPDATE Item i SET i.available = true WHERE i.id = :itemId")
    void restockItem(@Param("itemId") Long itemId);

    /**
     * Returns a page of items ordered by their popularity in descending order, and then by name in ascending order.
     *
     * @param pageable the pageable object specifying the page number and size
     * @return a page of item names and their corresponding popularity counts
     */
    @Query(value = "SELECT i.name, COUNT(oi.item_id) AS popularity " +
            "FROM order_item oi " +
            "JOIN Item i ON oi.item_id = i.id " +
            "GROUP BY i.name " +
            "ORDER BY popularity DESC, name ASC", nativeQuery = true)
    Page<Object[]> getItemsByPopularity(Pageable pageable);

}
