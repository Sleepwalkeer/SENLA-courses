package eu.senla.services;

import eu.senla.dto.CredentialsDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CredentialsService {
    @PreAuthorize("hasAuthority('write')")
    List<CredentialsDto> getAll();

    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    CredentialsDto getById(Integer id);

    @PreAuthorize("hasAuthority('write')")
    void create(CredentialsDto credentialsDto);

    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    CredentialsDto update(Integer id, CredentialsDto credentialsDto);

    @PreAuthorize("hasAuthority('write')|| #credentialsDto.id == authentication.principal.id")
    boolean delete(CredentialsDto credentialsDto);

    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    boolean deleteById(Integer id);
}
