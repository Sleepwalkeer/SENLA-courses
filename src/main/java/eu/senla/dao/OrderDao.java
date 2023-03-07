package eu.senla.dao;

import eu.senla.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDao  extends JpaRepository<Order,Long> {
//    List<Order> findAll();
//
//    Optional<Order> findById(Integer id);
//
//    Order findByIdEager(Integer id);
//
//    Order update(Order passedOrder);
//
//    void save(Order passedOrder);
//
//    boolean delete(Order passedOrder);
//
//    boolean deleteById(Integer id);
//
//    List<Order> getOrdersWithMoreItemsThan(int itemCount);
}
