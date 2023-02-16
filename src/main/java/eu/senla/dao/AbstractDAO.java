package eu.senla.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractDAO<K, T> {

    @PersistenceContext
    private EntityManager entityManager;


    public void save(T entity) {
        entityManager.persist(entity);
    }

    public T update(T entity) {
        return entityManager.merge(entity);
    }

   abstract public boolean delete(T entity);

    public boolean deleteById(K id) {
        Optional<T> optionalEntity = findById(id);
        if (optionalEntity.isPresent()) {
            entityManager.remove(optionalEntity.get());
            return true;
        }
        return false;
    }

    public List<T> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(getEntityClass());
        Root<T> root = query.from(getEntityClass());
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    public Optional<T> findById(K id) {
        T object = entityManager.find(getEntityClass(), id);
        return Optional.ofNullable(object);
    }

    abstract Class<T> getEntityClass();
}
