package eu.senla.service;

import eu.senla.dto.categoryDto.CategoryDto;
import eu.senla.dto.categoryDto.CreateCategoryDto;
import eu.senla.dto.categoryDto.ResponseCategoryDto;
import eu.senla.exception.NotFoundException;

import java.util.List;

/**
 * The CategoryService interface provides methods for managing categories of products.
 */
public interface CategoryService {

    /**
     * Retrieves a list of all product categories, sorted and paginated according to the specified criteria.
     *
     * @param pageNo   The page number of the results to retrieve.
     * @param pageSize The number of results to include per page.
     * @param sortBy   The field to sort the results by.
     * @return A List of ResponseCategoryDto objects representing the product categories that match the specified criteria.
     */
    List<ResponseCategoryDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Retrieves information about a specific product category.
     *
     * @param id The ID of the category to retrieve.
     * @return A ResponseCategoryDto object containing information about the requested category.
     * @throws NotFoundException if no category is found with the specified ID
     */
    ResponseCategoryDto getById(Long id) throws NotFoundException;

    /**
     * Creates a new product category based on the provided data.
     *
     * @param categoryDto A CreateCategoryDto object containing the data for the new category.
     * @return A ResponseCategoryDto object containing information about the newly created category.
     */
    ResponseCategoryDto create(CreateCategoryDto categoryDto);

    /**
     * Updates an existing product category with the provided data.
     *
     * @param id          The ID of the category to update.
     * @param categoryDto A CategoryDto object containing the updated data for the category.
     * @return A ResponseCategoryDto object containing information about the updated category.
     * @throws NotFoundException if no category is found with the specified ID
     */
    ResponseCategoryDto update(Long id, CategoryDto categoryDto) throws NotFoundException;


    /**
     * Deletes a product category with the specified ID.
     *
     * @param id The ID of the category to delete.
     * @throws NotFoundException if no category is found with the specified ID
     */
    void deleteById(Long id) throws NotFoundException;
}
