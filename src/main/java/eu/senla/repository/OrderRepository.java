package eu.senla.repository;

import eu.senla.entity.Item;
import eu.senla.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * This interface represents a repository for storing and retrieving {@link Order} objects in a database.
 * It extends the {@link JpaRepository} interface, which provides standard CRUD operations for JPA entities,
 * and the {@link JpaSpecificationExecutor} interface, which allows querying based on specifications.
 */
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

//    @EntityGraph(value = "graph.Order.allFields", type = EntityGraph.EntityGraphType.LOAD)
//    Order findOrderById(Long id);

    Page<Order> getAllByCustomer_Id(Long id, Pageable pageable);

}
