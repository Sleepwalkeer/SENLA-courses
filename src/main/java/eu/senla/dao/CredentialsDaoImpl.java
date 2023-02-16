package eu.senla.dao;

import eu.senla.entities.Credentials;
import eu.senla.entities.Credentials;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public boolean delete(Credentials entity) {
        return deleteById(entity.getId());
    }
}
