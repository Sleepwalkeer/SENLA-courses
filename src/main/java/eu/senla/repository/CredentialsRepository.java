package eu.senla.repository;

import eu.senla.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing users credentials.
 */
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    /**
     * Finds credentials by username.
     *
     * @param username The username to search for.
     * @return An Optional containing the credentials if found, or empty if not.
     */
    Optional<Credentials> findByUsername(String username);
}
