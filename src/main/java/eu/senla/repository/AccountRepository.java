package eu.senla.repository;

import eu.senla.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing user accounts.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an account by its email address.
     *
     * @param email The email address to search for.
     * @return An Optional containing the account if found, or empty if not.
     */
    Optional<Account> findByEmail(String email);
}
