package eu.senla.service;

import eu.senla.dto.categoryDto.CategoryDto;
import eu.senla.dto.categoryDto.CreateCategoryDto;
import eu.senla.dto.categoryDto.ResponseCategoryDto;
import eu.senla.exception.NotFoundException;

import java.util.List;
import java.util.Map;

/**
 * The CategoryService interface provides methods for managing categories of products.
 */
public interface CategoryService {

    /**
     * Retrieves a list of all product categories, sorted and paginated according to the specified criteria.
     *
     * @param pageNo   The page number to retrieve. Defaults to 0 if not provided.
     * @param pageSize The number of orders to include per page. Defaults to 5 if not provided.
     * @param sortBy   The field to sort the orders by. Defaults to "id" if not provided.
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

    /**
     * Retrieves a list of categories, paginated as specified, with optional filtering based on provided parameters.
     *
     * @param pageNo       The page number to retrieve. Defaults to 0 if not provided.
     * @param pageSize     The number of orders to include per page. Defaults to 5 if not provided.
     * @param filterParams A Map of filter parameters to apply to the query, where each key represents a field to filter on and the corresponding value is the value to filter for.
     *                     Multiple filters can be applied at once by including multiple key-value pairs in the Map.
     *                     If the filter parameter is not provided, no filtering will be applied.
     * @return A list of ResponseItemDto objects containing information about the categories.
     */
    List<ResponseCategoryDto> getCategoriesWithFilters(Integer pageNo, Integer pageSize, Map<String, String> filterParams);
}
