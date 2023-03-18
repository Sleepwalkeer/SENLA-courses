package eu.senla.service.implementation;

import eu.senla.dto.accountDto.CreateAccountDto;
import eu.senla.dto.accountDto.ResponseAccountDto;
import eu.senla.dto.accountDto.UpdateAccountDto;
import eu.senla.dto.credentialsDto.CredentialsDto;
import eu.senla.entity.Account;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.AccountRepository;
import eu.senla.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public ResponseAccountDto getById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No account with ID " + id + " was found"));
        return modelMapper.map(account, ResponseAccountDto.class);
    }

    @Transactional
    public ResponseAccountDto create(CreateAccountDto accountDto) {
        CredentialsDto credentials = accountDto.getCredentials();
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        Account account = modelMapper.map(accountDto, Account.class);
        accountRepository.save(account);
        return modelMapper.map(account, ResponseAccountDto.class);
    }

    @Transactional
    public ResponseAccountDto update(Long id, UpdateAccountDto accountDto) {
        if(accountRepository.existsById(id)){
            Account account = modelMapper.map(accountDto, Account.class);
            Account  updatedAccount = accountRepository.save(account);
            return modelMapper.map(updatedAccount, ResponseAccountDto.class);
        }
        else {
            throw  new NotFoundException("No account with ID " + id + " was found");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
        } else {
            throw new NotFoundException("No account with ID " + id + " was found");
        }
    }

    public List<ResponseAccountDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Account> accountPage = accountRepository.findAll(paging);

        return accountPage.getContent()
                .stream()
                .map(account -> modelMapper.map(account, ResponseAccountDto.class)).collect(Collectors.toList());
    }
}
