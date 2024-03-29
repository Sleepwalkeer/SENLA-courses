package eu.senla.service;

import eu.senla.dto.orderDto.CreateOrderDto;
import eu.senla.dto.orderDto.ResponseOrderDto;
import eu.senla.dto.orderDto.UpdateOrderDto;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.NotFoundException;

import java.util.List;
import java.util.Map;

/**
 * The OrderService interface provides methods for managing orders in the system.
 */
public interface OrderService {

    /**
     * Retrieves a list of all orders, sorted and paginated as specified.
     *
     * @param pageNo   The page number to retrieve. Defaults to 0 if not provided.
     * @param pageSize The number of orders to include per page. Defaults to 5 if not provided.
     * @param sortBy   The field to sort the orders by. Defaults to "id" if not provided.
     * @return A list of ResponseOrderDto objects containing information about the orders.
     */
    List<ResponseOrderDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Retrieves information about a specific order.
     *
     * @param id The ID of the order to retrieve.
     * @return A ResponseOrderDto object containing information about the requested order.
     * @throws NotFoundException If no order is found with the specified ID.
     */
    ResponseOrderDto getById(Long id) throws NotFoundException;

    /**
     * Creates a new order based on the provided data. Automatically counts total price of the order.
     *
     * @param orderDto A CreateOrderDto object containing the data for the new order.
     * @return A ResponseOrderDto object containing information about the newly created order.
     * @throws BadRequestException If the startDateTime is later than endDateTime.
     */
    ResponseOrderDto create(CreateOrderDto orderDto) throws BadRequestException;

    /**
     * Updates an existing order with the provided data. Automatically recounts total price of the order.
     *
     * @param id       The ID of the order to update.
     * @param orderDto An UpdateOrderDto object containing the updated data for the order.
     * @return A ResponseOrderDto object containing information about the updated order.
     * @throws NotFoundException   If no order is found with the specified ID.
     * @throws BadRequestException If the startDateTime is later than endDateTime.
     */
    ResponseOrderDto update(Long id, UpdateOrderDto orderDto) throws NotFoundException, BadRequestException;

    /**
     * Deletes an order with the specified ID.
     *
     * @param id The ID of the order to delete.
     * @throws NotFoundException If no order is found with the specified ID.
     */
    void deleteById(Long id) throws NotFoundException;

    /**
     * Retrieves a list of orders, paginated as specified, with optional filtering based on provided parameters.
     *
     * @param pageNo       The page number to retrieve. Defaults to 0 if not provided.
     * @param pageSize     The number of orders to include per page. Defaults to 5 if not provided.
     * @param filterParams A Map of filter parameters to apply to the query, where each key represents a field to filter on and the corresponding value is the value to filter for.
     *                     Multiple filters can be applied at once by including multiple key-value pairs in the Map.
     *                     If the filter parameter is not provided, no filtering will be applied.
     * @return A list of ResponseOrderDto objects containing information about the orders.
     */
    List<ResponseOrderDto> getOrdersWithFilters(Integer pageNo, Integer pageSize, Map<String, String> filterParams);

    /**
     * Retrieves a list of order DTOs for the customer with the specified id, sorted and paginated as specified.
     *
     * @param id       the ID of the customer whose orders to retrieve
     * @param pageNo   The page number to retrieve. Defaults to 0 if not provided.
     * @param pageSize The number of orders to include per page. Defaults to 5 if not provided.
     * @param sortBy   The field to sort the orders by. Defaults to "id" if not provided.
     * @return A list of order DTOs for the customer, or an empty list if no orders are found.
     */
    List<ResponseOrderDto> getOrdersByCustomerId(Long id, Integer pageNo, Integer pageSize, String sortBy);
}
