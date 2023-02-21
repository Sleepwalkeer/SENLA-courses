package eu.senla.services;

import eu.senla.dto.AccountDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AccountService {
    @PreAuthorize("hasAuthority('write')")
    List<AccountDto> getAll();

    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    AccountDto getById(Integer id);

    @PreAuthorize("hasAuthority('write')")
    void create(AccountDto accountDto);

    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    AccountDto update(Integer id, AccountDto accountDto);

    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    boolean deleteById(Integer id);

    @PreAuthorize("hasAuthority('write')|| #accountDto.id == authentication.principal.id")
    boolean delete(AccountDto accountDto);
}
