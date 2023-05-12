package eu.senla.repository;

import eu.senla.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * Sets the "is_deleted" flag to true for the category with the specified ID.
     *
     * @param categoryId the ID of the category to delete
     */
    @Modifying
    @Query("UPDATE Category c SET c.deleted = true WHERE c.id = :categoryId")
    void deleteById(@Param("categoryId") Long categoryId);
}
