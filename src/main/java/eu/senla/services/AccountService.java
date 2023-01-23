package eu.senla.services;

import eu.senla.dto.AccountDto;
import java.util.List;

public interface AccountService {
    List<AccountDto> getAll();

    AccountDto getById(AccountDto accountDto);

    AccountDto create(AccountDto accountDto);

    AccountDto update(AccountDto accountDto, String phoneCode);

    void delete(AccountDto accountDto);
}
