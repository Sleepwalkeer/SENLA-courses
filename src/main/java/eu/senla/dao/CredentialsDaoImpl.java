package eu.senla.dao;

import eu.senla.entities.Credentials;
import eu.senla.entities.Credentials;
import eu.senla.entities.Credentials_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CredentialsDaoImpl extends AbstractDAO<Integer, Credentials> implements CredentialsDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    Class<Credentials> getEntityClass() {
        return Credentials.class;
    }

    @Override
    public Optional<Credentials> findByUsername(String username) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Credentials> query = builder.createQuery(Credentials.class);
        Root<Credentials> root = query.from(Credentials.class);
        query.select(root);
        query.where(builder.equal(root.get(Credentials_.username), username));
        TypedQuery<Credentials> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList().stream().findFirst();
    }

    @Override
    public boolean delete(Credentials entity) {
        return deleteById(entity.getId());
    }
}
