package eu.senla.dao;

import eu.senla.entities.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDao {
    List<Order> getAll();

    Order getById(Order passedOrder);

    Order update(Order passedOrder,  BigDecimal newTotalPrice);

    Order create(Order passedOrder);

    void delete(Order passedOrder);
}