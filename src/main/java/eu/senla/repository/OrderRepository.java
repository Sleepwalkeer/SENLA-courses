package eu.senla.repository;

import eu.senla.entity.Account;
import eu.senla.entity.Item;
import eu.senla.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @EntityGraph(value = "graph.Order.allFields", type = EntityGraph.EntityGraphType.LOAD)
    Order findOrderById(Long id);
}
