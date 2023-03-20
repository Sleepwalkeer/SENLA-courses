package eu.senla.service;

import eu.senla.dto.accountDto.CreateAccountDto;
import eu.senla.dto.accountDto.ResponseAccountDto;
import eu.senla.dto.accountDto.UpdateAccountDto;
import eu.senla.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
