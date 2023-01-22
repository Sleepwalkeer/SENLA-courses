package eu.senla.dao;

import eu.senla.entities.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Component
public class OrderDao {
    private final List<Order> orders = new LinkedList<>();

    public List<Order> getOrders() {
        return orders;
    }

    public List<Order> getAll() {
        return getOrders();
    }

    public Order getById(Order passedOrder) {
        for (Order order : orders) {
            if (passedOrder.getId() == order.getId()) {
                return order;
            }
        }
        return null;
    }

    public Order update(Order passedOrder, BigDecimal newTotalPrice) {
        for (Order order : orders) {
            if (passedOrder.getId() == order.getId()) {
                order.setTotalPrice(newTotalPrice);
                return order;
            }
        }
        return null;
    }

    public Order create(Order passedOrder) {
        orders.add(passedOrder);
        return passedOrder;
    }

    public void delete(Order passedOrder) {
        for (int i = 0; i < orders.size(); i++) {
            if (passedOrder.getId() == orders.get(i).getId()) {
                orders.remove(i);
                return;
            }
        }
    }
}
