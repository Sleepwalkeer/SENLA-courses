package eu.senla.dao;

import eu.senla.entities.Item;
import eu.senla.entities.Item_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;


public class ItemDaoImpl extends AbstractDAO<Integer, Item> implements ItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    Class<Item> getEntityClass() {
        return Item.class;
    }

    @Override
    public Item findByIdEager(Integer id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = criteriaBuilder.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        root.fetch(Item_.category, JoinType.INNER);
        query.select(root).where(criteriaBuilder.equal(root.get(Item_.id), id));

        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Item> GetItemsMoreExpensiveThan(BigDecimal price) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = builder.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root).where(builder.greaterThan(root.get(Item_.PRICE), price));

        return entityManager.createQuery(query).getResultList();
    }
}

