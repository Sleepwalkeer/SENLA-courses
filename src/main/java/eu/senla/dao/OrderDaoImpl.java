package eu.senla.dao;

import eu.senla.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDaoImpl extends AbstractDAO<Integer, Order> implements OrderDao {

    @Override
    Class<Order> getEntityClass() {
        return Order.class;
    }
}
