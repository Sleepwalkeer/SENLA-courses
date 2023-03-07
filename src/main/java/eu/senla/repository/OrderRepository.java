package eu.senla.repository;

import eu.senla.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    Order findByIdEager(Integer id);
//    List<Order> getOrdersWithMoreItemsThan(int itemCount);
}
