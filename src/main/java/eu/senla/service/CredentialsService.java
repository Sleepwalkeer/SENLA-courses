package eu.senla.service;

import eu.senla.dto.credentialsDto.CredentialsDto;
import eu.senla.dto.credentialsDto.ResponseCredentialsDto;
import eu.senla.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * The CredentialsService interface provides methods for managing user credentials.
 */
@Transactional
public interface CredentialsService {

    /**
     * Retrieves information about a specific set of user credentials.
     *
     * @param id The ID of the credentials to retrieve.
     * @return A ResponseCredentialsDto object containing information about the requested credentials.
     * @throws NotFoundException if no credentials are found with the specified ID.
     */
    ResponseCredentialsDto getById(Long id) throws NotFoundException;

    /**
     * Creates  new user credentials based on the provided data.
     *
     * @param credentialsDto A CredentialsDto object containing the data for the new credentials.
     */
    void create(CredentialsDto credentialsDto);

    /**
     * Updates an existing set of user credentials with the provided data.
     *
     * @param id             The ID of the credentials to update.
     * @param credentialsDto A CredentialsDto object containing the updated data for the credentials.
     * @throws NotFoundException If no credentials are found with the specified ID.
     */

    void update(Long id, CredentialsDto credentialsDto) throws NotFoundException;

    /**
     * Deletes a set of user credentials with the specified ID.
     *
     * @param id The ID of the credentials to delete.
     */
    void deleteById(Long id);
}
