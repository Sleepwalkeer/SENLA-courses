package eu.senla.services;

import eu.senla.dao.AccountDao;
import eu.senla.dto.AccountDto;
import eu.senla.entities.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final ModelMapper modelMapper;



    public AccountDto getById(Integer id) {
        Account account = accountDao.findById(id).orElse(null);
        if (account == null) {
            return null;
        }
        return modelMapper.map(account, AccountDto.class);
    }

    public void create(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        accountDao.save(account);
    }

    public AccountDto update(Integer id, AccountDto accountDto) {
        Account account = accountDao.findById(id).orElse(null);
        if (account == null) {
            return null;
        }
        modelMapper.map(accountDto, account);
        Account updatedAccount = accountDao.update(account);
        return modelMapper.map(updatedAccount, AccountDto.class);
    }

    public boolean deleteById(Integer id) {
        return accountDao.deleteById(id);
    }

    @Override
    public boolean delete(AccountDto accountDto) {
        return accountDao.delete(modelMapper.map(accountDto, Account.class));
    }

    public List<AccountDto> getAll() {
        List<Account> accounts = accountDao.findAll();
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
    }
}
