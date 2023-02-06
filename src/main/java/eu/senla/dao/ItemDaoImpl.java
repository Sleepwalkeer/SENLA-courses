package eu.senla.dao;

import eu.senla.dto.ItemDto;
import eu.senla.entities.Category;
import eu.senla.entities.Item;
import eu.senla.entities.Item_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
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
        root.fetch("category", JoinType.INNER);
        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Item item = (Item) entityManager.createQuery(query).getSingleResult();
        return item;
    }

    @Override
    public List<Item> GetItemsMoreExpensiveThan(BigDecimal price) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = builder.createQuery(Item.class);
        Root<Item> item = query.from(Item.class);

        query.select(item).where(builder.greaterThan(item.get("price"), price));

        return entityManager.createQuery(query).getResultList();
    }
}

