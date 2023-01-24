package eu.senla.services;

import eu.senla.dto.CredentialsDto;

import java.util.List;

public interface CredentialsService {
    List<CredentialsDto> getAll();

    CredentialsDto getById(CredentialsDto credentialsDto);

    CredentialsDto create(CredentialsDto accountDto);

    CredentialsDto update(CredentialsDto accountDto, String newPassword);

    void delete(CredentialsDto accountDto);
}
