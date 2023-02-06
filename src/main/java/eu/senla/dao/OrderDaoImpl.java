package eu.senla.dao;

import eu.senla.entities.Account;
import eu.senla.entities.Order;
import eu.senla.entities.Order_;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl extends AbstractDAO<Integer, Order> implements OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    Class<Order> getEntityClass() {
        return Order.class;
    }

    @Override
    public Order findByIdEager(Integer id) {
        EntityGraph itemsGraph = entityManager.getEntityGraph("graph.Order.itemsCategory");
        Map hints= new HashMap();
        hints.put("javax.persistence.fetchgraph",itemsGraph);
        Order order = entityManager.find(Order.class, id, hints);
        return order;
    }

    public List<Order> getOrdersWithMoreItemsThan(int itemCount) {
        String jpql = "SELECT o FROM Order o WHERE SIZE(o.items) > :itemCount";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("itemCount", itemCount);
        return query.getResultList();
    }
}
