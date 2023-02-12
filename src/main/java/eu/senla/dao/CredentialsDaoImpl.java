package eu.senla.dao;

import eu.senla.entities.Credentials;
import org.springframework.stereotype.Repository;

@Repository
public class CredentialsDaoImpl extends AbstractDAO<Integer, Credentials> implements CredentialsDao {

    @Override
    Class<Credentials> getEntityClass() {
        return Credentials.class;
    }
}
