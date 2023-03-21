package eu.senla.repository;

import eu.senla.entity.Category;
import eu.senla.entity.Credentials;
import eu.senla.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * This interface represents a repository for storing and retrieving {@link Category} objects in a database.
 * It extends the {@link JpaRepository} interface, which provides standard CRUD operations for JPA entities,
 * and the {@link JpaSpecificationExecutor} interface, which allows querying based on specifications.
 */
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    /**
     * Finds a category by its name.
     *
     * @param name The name to search for.
     * @return An Optional containing the category if found, or empty if not.
     */
    Optional<Category> findByName(String name);
}
