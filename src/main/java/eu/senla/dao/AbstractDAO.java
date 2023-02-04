package eu.senla.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public abstract class AbstractDAO<K, T> {

    @PersistenceContext
    private EntityManager entityManager;


    public void save(T entity) {
        entityManager.persist(entity);
    }

    public T update(T entity) {
        return entityManager.merge(entity);
    }

    public void delete(T entity) {
        entityManager.remove(entity);
    }

    public List<T> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(getEntityClass());
        Root<T> root = query.from(getEntityClass());
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    public T findById(K id) {
        return entityManager.find(getEntityClass(), id);
    }

    abstract Class<T> getEntityClass();
}
