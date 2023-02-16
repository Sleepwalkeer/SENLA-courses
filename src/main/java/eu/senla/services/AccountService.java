package eu.senla.services;

import eu.senla.dto.AccountDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AccountService {
    List<AccountDto> getAll();

    AccountDto getById(Integer id);

    void create(AccountDto accountDto);

    AccountDto update(Integer id, AccountDto accountDto);

    boolean deleteById(Integer id);

    boolean delete(AccountDto accountDto);
}
