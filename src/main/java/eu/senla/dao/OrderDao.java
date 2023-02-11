package eu.senla.dao;

import eu.senla.entities.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {
    List<Order> findAll();

    Order findById(Integer id);

    Order findByIdEager(Integer id);

    Order update(Order passedOrder);

    void save(Order passedOrder);

    void delete(Order passedOrder);

    void deleteById(Integer id);

    List<Order> getOrdersWithMoreItemsThan(int itemCount);
}
