package eu.senla.services;

import eu.senla.dao.AccountDao;
import eu.senla.dto.AccountDto;
import eu.senla.entities.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final ModelMapper modelMapper;

    public AccountServiceImpl(AccountDao accountDao, ModelMapper modelMapper) {
        this.accountDao = accountDao;
        this.modelMapper = modelMapper;
    }

    public List<AccountDto> getAll() {
        List<AccountDto> accountDtoList = new ArrayList<>();
        List<Account> accounts = accountDao.findAll();

        for (Account account : accounts) {
            accountDtoList.add(modelMapper.map(account, AccountDto.class));
        }
        return accountDtoList;
    }

    public AccountDto getById(AccountDto accountDto) {
        return modelMapper.map(accountDao.findById(accountDto.getId()), AccountDto.class);
    }

    public void create(AccountDto accountDto) {
        accountDao.save(modelMapper.map(accountDto, Account.class));
    }

    public AccountDto update(AccountDto accountDto) {
        return modelMapper.map(accountDao.update(modelMapper.map(accountDto, Account.class)), AccountDto.class);
    }

    public void delete(AccountDto accountDto) {
        accountDao.delete(modelMapper.map(accountDto, Account.class));
    }
}
