package eu.senla.dao;

import eu.senla.entities.Credentials;

import java.util.List;

public interface CredentialsDao {
    List<Credentials> findAll();

    Credentials findById(Integer id);

    Credentials update(Credentials credentials);

    Credentials save(Credentials credentials);

    void delete(Credentials credentials);

    void deleteById(Integer id);
}
