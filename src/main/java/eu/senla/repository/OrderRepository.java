package eu.senla.repository;

import eu.senla.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing orders.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

//    @EntityGraph(value = "graph.Order.allFields", type = EntityGraph.EntityGraphType.LOAD)
//    Order findOrderById(Long id);
}
