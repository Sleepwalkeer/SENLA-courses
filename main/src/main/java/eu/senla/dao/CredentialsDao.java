package eu.senla.dao;

import eu.senla.entities.Credentials;

import java.util.List;

public interface CredentialsDao {
    List<Credentials> getAll();

    Credentials getById(Credentials passedCredentials);

    Credentials update(Credentials passedCredentials, String newPassword);

    Credentials create(Credentials passedCredentials);

    void delete(Credentials passedCredentials);
}
