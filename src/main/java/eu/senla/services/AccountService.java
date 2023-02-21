package eu.senla.services;

import eu.senla.dto.AccountDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AccountService {
    @PreAuthorize("hasAuthority('read')")
    List<AccountDto> getAll();
    @PreAuthorize("hasAuthority('read')")
    AccountDto getById(Integer id);

    @PreAuthorize("hasAuthority('write')")
    void create(AccountDto accountDto);

    @PreAuthorize("hasAuthority('write')")
    AccountDto update(Integer id, AccountDto accountDto);

    @PreAuthorize("hasAuthority('write')")
    boolean deleteById(Integer id);

    @PreAuthorize("hasAuthority('write')")
    boolean delete(AccountDto accountDto);
}
