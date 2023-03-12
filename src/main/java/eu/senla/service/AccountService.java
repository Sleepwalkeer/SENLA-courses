package eu.senla.service;

import eu.senla.dto.AccountDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AccountService {
    List<AccountDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

    AccountDto getById(Long id);

    void create(AccountDto accountDto);

    AccountDto update(Long id, AccountDto accountDto);

    void deleteById(Long id);

    void delete(AccountDto accountDto);
}
