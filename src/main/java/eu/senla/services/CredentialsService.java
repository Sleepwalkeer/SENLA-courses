package eu.senla.services;

import eu.senla.dto.CredentialsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CredentialsService {
    List<CredentialsDto> getAll();

    CredentialsDto getById(Integer id);

    void create(CredentialsDto credentialsDto);

    CredentialsDto update(Integer id, CredentialsDto credentialsDto);

    boolean delete(CredentialsDto credentialsDto);

    boolean deleteById(Integer id);
}
