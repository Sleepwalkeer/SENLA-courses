package eu.senla.repository;

import eu.senla.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * This interface represents a repository for storing and retrieving {@link Account} objects in a database.
 * It extends the {@link JpaRepository} interface, which provides standard CRUD operations for JPA entities,
 * and the {@link JpaSpecificationExecutor} interface, which allows querying based on specifications.
 */
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    /**
     * Finds an account by its email address.
     *
     * @param email The email address to search for.
     * @return An Optional containing the account if found, or empty if not.
     */
    Optional<Account> findByEmail(String email);

    /**
     * Sets the "is_deleted" flag to true for the account with the specified ID.
     *
     * @param accountId the ID of the account to delete
     */
    @Modifying
    @Query("UPDATE Account a SET a.deleted = true WHERE a.id = :accountId")
    void deleteById(@Param("accountId") Long accountId);

    /**
     * Updates the discount value of the account with the specified ID.
     *
     * @param accountId the ID of the account to update
     * @param discount  the new discount value
     */
    @Modifying
    @Query("UPDATE Account a SET a.discount = :discount WHERE a.id = :accountId")
    void updateAccountDiscount(@Param("accountId") Long accountId, @Param("discount") BigDecimal discount);

    /**
     * Updates the balance value of the account with the specified ID.
     *
     * @param accountId the ID of the account to update
     * @param balance   the new balance value
     */
    @Modifying
    @Query("UPDATE Account a SET a.balance = :balance WHERE a.id = :accountId")
    void updateAccountBalance(@Param("accountId") Long accountId, @Param("balance") BigDecimal balance);
}
