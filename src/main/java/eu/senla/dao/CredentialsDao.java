package eu.senla.dao;

import eu.senla.entities.Credentials;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialsDao {
    List<Credentials> findAll();

    Optional<Credentials> findById(Integer id);

    Optional<Credentials> findByUsername(String username);

    Credentials update(Credentials credentials);

    void save(Credentials credentials);

    boolean delete(Credentials credentials);

    boolean deleteById(Integer id);
}
