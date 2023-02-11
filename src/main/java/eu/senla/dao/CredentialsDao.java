package eu.senla.dao;

import eu.senla.entities.Credentials;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CredentialsDao {
    List<Credentials> findAll();

    Credentials findById(Integer id);

    Credentials update(Credentials credentials);

    void save(Credentials credentials);

    void delete(Credentials credentials);

    void deleteById(Integer id);
}
