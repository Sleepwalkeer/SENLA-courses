package eu.senla.dao;

import eu.senla.entities.Order;

import java.util.List;

public interface OrderDao {
    List<Order> findAll();

    Order findById(Integer id);

    Order findByIdEager(Integer id);

    Order update(Order passedOrder);

    Order save(Order passedOrder);

    void delete(Order passedOrder);

    void deleteById(Integer id);

    List<Order> getOrdersWithMoreItemsThan(int itemCount);
}
