package eu.senla.services;

import eu.senla.dao.AccountDao;
import eu.senla.dto.AccountDto;
import eu.senla.entities.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class AccountService {

    private final AccountDao accountDao;
    private final ModelMapper modelMapper;

    public AccountService(AccountDao accountDao, ModelMapper modelMapper) {
        this.accountDao = accountDao;
        this.modelMapper = modelMapper;
    }

    public List<AccountDto> getAll() {
        List<AccountDto> accountDtoList = new ArrayList<>();
        List<Account> accounts = accountDao.getAll();

        for (Account account : accounts) {
            accountDtoList.add(fromEntityToDto(account));
        }
        return accountDtoList;
    }

    public AccountDto getById(AccountDto accountDto) {
        return fromEntityToDto(accountDao.getById(fromDtoToEntity(accountDto)));
    }

    public AccountDto update(AccountDto accountDto, String phoneCode) {
        return fromEntityToDto(accountDao.update(fromDtoToEntity(accountDto), phoneCode));
    }

    public AccountDto create(AccountDto accountDto) {
        return fromEntityToDto(accountDao.create(fromDtoToEntity(accountDto)));
    }

    public void delete(AccountDto accountDto) {
        accountDao.delete(fromDtoToEntity(accountDto));
    }

    private Account fromDtoToEntity(AccountDto accountDto) {
        return modelMapper.map(accountDto, Account.class);
    }

    private AccountDto fromEntityToDto(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }
}
