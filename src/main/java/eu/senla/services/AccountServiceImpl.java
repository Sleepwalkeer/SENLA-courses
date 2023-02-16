package eu.senla.services;

import eu.senla.dao.AccountDao;
import eu.senla.dto.AccountDto;
import eu.senla.entities.Account;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.NotFoundException;
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
        Account account = accountDao.findById(id).orElseThrow(() ->
                new NotFoundException("No account with ID " + id + " was found"));
        return modelMapper.map(account, AccountDto.class);
    }

    public void create(AccountDto accountDto) {
        if (accountDto.getCredentials() == null) {
            throw new BadRequestException("Credentials are required");
        }
        if (accountDto.getFirstName() == null || accountDto.getSecondName() == null) {
            throw new BadRequestException("First and second names are required");
        }
        Account account = modelMapper.map(accountDto, Account.class);
        accountDao.save(account);
    }

    public AccountDto update(Integer id, AccountDto accountDto) {
        Account account = accountDao.findById(id).orElseThrow(() ->
                new NotFoundException("No account with ID " + id + " was found"));
        modelMapper.map(accountDto, account);
        Account updatedAccount = accountDao.update(account);
        return modelMapper.map(updatedAccount, AccountDto.class);
    }

    public boolean deleteById(Integer id) {
        if (accountDao.deleteById(id)){
            return true;
        }
        else throw new NotFoundException("No account with ID " + id + " was found");
    }

    @Override
    public boolean delete(AccountDto accountDto) {
        if (accountDao.delete(modelMapper.map(accountDto, Account.class))){
            return true;
        }
        else throw new NotFoundException("No such account was found");
    }
    public List<AccountDto> getAll() {
        try {
            List<Account> accounts = accountDao.findAll();
            return accounts.stream()
                    .map(account -> modelMapper.map(account, AccountDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
