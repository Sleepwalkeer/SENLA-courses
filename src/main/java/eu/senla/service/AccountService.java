package eu.senla.service;

import eu.senla.dto.accountDto.CreateAccountDto;
import eu.senla.dto.accountDto.ResponseAccountDto;
import eu.senla.dto.accountDto.UpdateAccountDto;
import eu.senla.entity.Account;
import eu.senla.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * The AccountService interface provides methods for managing user accounts.
 */
public interface AccountService {

    /**
     * Retrieves a list of all user accounts, sorted and paginated according to the specified criteria.
     *
     * @param pageNo   The page number to retrieve. Defaults to 0 if not provided.
     * @param pageSize The number of orders to include per page. Defaults to 5 if not provided.
     * @param sortBy   The field to sort the orders by. Defaults to "id" if not provided.
     * @return A List of ResponseAccountDto objects representing the user accounts that match the specified criteria.
     */
    List<ResponseAccountDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Retrieves information about a specific user account.
     *
     * @param id The ID of the account to retrieve.
     * @return A ResponseAccountDto object containing information about the requested account.
     * @throws NotFoundException if no account is found with the specified ID
     */
    ResponseAccountDto getById(Long id) throws NotFoundException;

    /**
     * Creates a new user account based on the provided account data.
     *
     * @param accountDto A CreateAccountDto object containing the data for the new account.
     * @return A ResponseAccountDto object containing information about the newly created account.
     */
    ResponseAccountDto create(CreateAccountDto accountDto);

    /**
     * Updates an existing user account with the provided data.
     *
     * @param id         The ID of the account to update.
     * @param accountDto An UpdateAccountDto object containing the updated data for the account.
     * @return A ResponseAccountDto object containing information about the updated account.
     * @throws NotFoundException if no account is found with the specified ID
     */
    ResponseAccountDto update(Long id, UpdateAccountDto accountDto) throws NotFoundException;

    /**
     * Deletes a user account with the specified ID.
     *
     * @param id The ID of the account to delete.
     * @throws NotFoundException if no account is found with the specified ID
     */
    void deleteById(Long id) throws NotFoundException;

    /**
     * Increases the discount percentage for a customer account by 1, if the
     * account's current discount is less than the threshold.
     *
     * @param account the account for which to increase the discount
     */
    void incrementCustomerDiscount(Account account);

    /**
     * Retrieves a list of accounts, paginated as specified, with optional filtering based on provided parameters.
     *
     * @param pageNo       The page number to retrieve. Defaults to 0 if not provided.
     * @param pageSize     The number of orders to include per page. Defaults to 5 if not provided.
     * @param filterParams A Map of filter parameters to apply to the query, where each key represents a field to filter on and the corresponding value is the value to filter for.
     *                     Multiple filters can be applied at once by including multiple key-value pairs in the Map.
     *                     If the filter parameter is not provided, no filtering will be applied.
     * @return A list of ResponseItemDto objects containing information about the accounts.
     */
    List<ResponseAccountDto> getAccountsWithFilters(Integer pageNo, Integer pageSize, Map<String, String> filterParams);

    void withdrawBalance(Account account, BigDecimal balance);
}
