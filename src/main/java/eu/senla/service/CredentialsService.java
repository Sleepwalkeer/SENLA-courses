package eu.senla.service;

import eu.senla.dto.CredentialsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CredentialsService {
    List<CredentialsDto> getAll();

    CredentialsDto getById(Long id);

    void create(CredentialsDto credentialsDto);

    CredentialsDto update(Long id, CredentialsDto credentialsDto);

    void delete(CredentialsDto credentialsDto);

    void deleteById(Long id);
}
