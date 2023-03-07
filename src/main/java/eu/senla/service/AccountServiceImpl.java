package eu.senla.service;

import eu.senla.repository.AccountRepository;
import eu.senla.dto.AccountDto;
import eu.senla.entity.Account;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    //TODO разберись с тем будешь ли проверять на invalidIdDelete или нет
    public AccountDto getById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
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
        accountRepository.save(account);
    }

    public AccountDto update(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No account with ID " + id + " was found"));
        modelMapper.map(accountDto, account);
        Account updatedAccount = accountRepository.save(account);
        return modelMapper.map(updatedAccount, AccountDto.class);
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }


    public void delete(AccountDto accountDto) {
        accountRepository.delete(modelMapper.map(accountDto, Account.class));

    }

    public List<AccountDto> getAll() {
        try {
            List<Account> accounts = accountRepository.findAll();
            return accounts.stream()
                    .map(account -> modelMapper.map(account, AccountDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
