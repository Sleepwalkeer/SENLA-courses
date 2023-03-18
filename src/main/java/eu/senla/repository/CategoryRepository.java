package eu.senla.repository;

import eu.senla.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing categories in the system.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a category by its name.
     *
     * @param name The name to search for.
     * @return An Optional containing the category if found, or empty if not.
     */
    Optional<Category> findByName(String name);
}
