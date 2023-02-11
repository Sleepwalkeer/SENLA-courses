package eu.senla.dao;

import eu.senla.entities.Order;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl extends AbstractDAO<Integer, Order> implements OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    Class<Order> getEntityClass() {
        return Order.class;
    }

    @Override
    public Order findByIdEager(Integer id) {
        EntityGraph<?> itemsGraph = entityManager.getEntityGraph("graph.Order.itemsCategory");
        Map hints= new HashMap();
        hints.put("javax.persistence.fetchgraph",itemsGraph);
        return entityManager.find(Order.class, id, hints);
    }

    public List<Order> getOrdersWithMoreItemsThan(int itemCount) {
        String jpql = "SELECT o FROM Order o WHERE SIZE(o.items) > :itemCount";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("itemCount", itemCount);
        return query.getResultList();
    }
}
