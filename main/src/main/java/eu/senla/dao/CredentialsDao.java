package eu.senla.dao;

import eu.senla.entities.Credentials;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CredentialsDao {
    private final List<Credentials> credentialsList = new LinkedList<>();

    public List<Credentials> getCredentials() {
        return credentialsList;
    }

    public List<Credentials> getAll() {
        return getCredentials();
    }

    public Credentials getById(Credentials passedCredentials) {
        for (Credentials credentials : credentialsList) {
            if (passedCredentials.getId() == credentials.getId()) {
                return credentials;
            }
        }
        return null;
    }

    public Credentials update(Credentials passedCredentials, String newPassword) {
        for (Credentials credentials : credentialsList) {
            if (passedCredentials.getId() == credentials.getId()) {
                credentials.setPassword(newPassword);
                return credentials;
            }
        }
        return null;
    }

    public Credentials create(Credentials passedCredentials) {
        credentialsList.add(passedCredentials);
        return passedCredentials;
    }

    public void delete(Credentials passedCredentials) {
        for (int i = 0; i < credentialsList.size(); i++) {
            if (passedCredentials.getId() == credentialsList.get(i).getId()) {
                credentialsList.remove(i);
                return;
            }
        }
    }
}
