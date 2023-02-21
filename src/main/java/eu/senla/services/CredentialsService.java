package eu.senla.services;

import eu.senla.dto.CredentialsDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CredentialsService {
    @PreAuthorize("hasAuthority('read')")
    List<CredentialsDto> getAll();
    @PreAuthorize("hasAuthority('read')")
    CredentialsDto getById(Integer id);

    @PreAuthorize("hasAuthority('write')")
    void create(CredentialsDto credentialsDto);

    @PreAuthorize("hasAuthority('write')")
    CredentialsDto update(Integer id, CredentialsDto credentialsDto);

    @PreAuthorize("hasAuthority('write')")
    boolean delete(CredentialsDto credentialsDto);

    @PreAuthorize("hasAuthority('write')")
    boolean deleteById(Integer id);
}
