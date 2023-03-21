package eu.senla.repository;

import eu.senla.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * This interface represents a repository for storing and retrieving {@link Credentials} objects in a database.
 * It extends the {@link JpaRepository} interface, which provides standard CRUD operations for JPA entities,
 * and the {@link JpaSpecificationExecutor} interface, which allows querying based on specifications.
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
