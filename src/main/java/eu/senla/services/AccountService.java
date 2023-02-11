package eu.senla.services;

import eu.senla.dto.AccountDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public interface AccountService {
    List<AccountDto> getAll();

    AccountDto getById(Integer id);

    void create(AccountDto accountDto);

    AccountDto update(AccountDto accountDto);

    void delete(AccountDto accountDto);
}
