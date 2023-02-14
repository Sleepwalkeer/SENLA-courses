package eu.senla.dao;

import eu.senla.entities.Item;
import eu.senla.entities.Item_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
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

    @Override
    public boolean delete(Item entity) {
        return deleteById(entity.getId());
    }
}

