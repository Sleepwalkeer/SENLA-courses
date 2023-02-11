package eu.senla.services;

import eu.senla.dto.CredentialsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public interface CredentialsService {
    List<CredentialsDto> getAll();

    CredentialsDto getById(Integer id);

    void create(CredentialsDto accountDto);

    CredentialsDto update(CredentialsDto accountDto);

    void delete(CredentialsDto accountDto);
}
