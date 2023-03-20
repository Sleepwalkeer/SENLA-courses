package eu.senla.service;

import eu.senla.dto.itemDto.CreateItemDto;
import eu.senla.dto.itemDto.ResponseItemDto;
import eu.senla.dto.itemDto.UpdateItemDto;
import eu.senla.entity.Item;
import eu.senla.exception.NotFoundException;

import java.util.List;

/**
 * The ItemService interface provides methods for managing items in the system.
 */
public interface ItemService {
    /**
     * Retrieves a list of all items, sorted and paginated as specified.
     *
     * @param pageNo   The page number to retrieve. Defaults to 0 if not provided.
     * @param pageSize The number of orders to include per page. Defaults to 5 if not provided.
     * @param sortBy   The field to sort the orders by. Defaults to "id" if not provided.
     * @return A list of ResponseItemDto objects containing information about the items.
     */
    List<ResponseItemDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Retrieves information about a specific item.
     *
     * @param id The ID of the item to retrieve.
     * @return A ResponseItemDto object containing information about the requested item.
     * @throws NotFoundException If no item is found with the specified ID.
     */
    ResponseItemDto getById(Long id) throws NotFoundException;

    /**
     * Creates a new item based on the provided data.
     *
     * @param itemDto A CreateItemDto object containing the data for the new item.
     * @return A ResponseItemDto object containing information about the newly created item.
     */
    ResponseItemDto create(CreateItemDto itemDto);

    /**
     * Updates an existing item with the provided data.
     *
     * @param id      The ID of the item to update.
     * @param itemDto An UpdateItemDto object containing the updated data for the item.
     * @return A ResponseItemDto object containing information about the updated item.
     * @throws NotFoundException If no item is found with the specified ID.
     */
    ResponseItemDto update(Long id, UpdateItemDto itemDto) throws NotFoundException;

    /**
     * Deletes an item with the specified ID.
     *
     * @param id The ID of the item to delete.
     * @throws NotFoundException If no item is found with the specified ID.
     */
    void deleteById(Long id) throws NotFoundException;

    /**

     Decrements the quantity of every item in the provided list by 1.
     @param items The list of Item objects to decrement the quantity for.
     */

    void decrementQuantityEveryItem(List<Item> items);

    /**

     Finds and returns a list of Item objects matching the provided list of item ids.
     @param itemIds The list of item ids to search for.
     @return A list of Item objects matching the provided item ids.
     */
    List<Item> findItemsByIds(List<Long> itemIds);
}
