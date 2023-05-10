package eu.senla.repository;

import eu.senla.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This interface represents a repository for storing and retrieving {@link Order} objects in a database.
 * It extends the {@link JpaRepository} interface, which provides standard CRUD operations for JPA entities,
 * and the {@link JpaSpecificationExecutor} interface, which allows querying based on specifications.
 */
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * Finds an Order entity by its identifier and eagerly fetches all its dependent entities.
     *
     * @param id the identifier of the Order entity to be retrieved.
     * @return the Order entity with the specified identifier, along with all its eagerly fetched dependent entities.
     */
    @EntityGraph(value = "graph.Order.allFields", type = EntityGraph.EntityGraphType.LOAD)
    Order findOrderById(Long id);

    /**
     * Sets the "is_deleted" flag to true for the order with the specified ID.
     *
     * @param orderId the ID of the order to delete
     */
    @Modifying
    @Query("UPDATE Order o SET o.deleted = true WHERE o.id = :orderId")
    void deleteById(@Param("orderId") Long orderId);


    /**
     * Finds an order by its StartDateTime.
     *
     * @param startDateTime The startDateTime to search for.
     * @return An Optional containing the order if found, or empty if not.
     */
    Optional<Order> findOrderByStartDateTime(LocalDateTime startDateTime);

    /**
     * Retrieves a Page of Order made by the customer with the specified id.
     *
     * @param id       the customer identifier to match.
     * @param pageable the pageable object specifying the page number and size
     * @return a Page object containing the Order entities  made by the customer with the specified Ii.
     */
    Page<Order> getAllByCustomer_Id(Long id, Pageable pageable);

}
