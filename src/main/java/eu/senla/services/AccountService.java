package eu.senla.services;

import eu.senla.dto.AccountDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AccountService {

    List<AccountDto> getAll();

    AccountDto getById(Long id);

    void create(AccountDto accountDto);

    AccountDto update(Long id, AccountDto accountDto);

    void deleteById(Long id);

    void delete(AccountDto accountDto);
}
