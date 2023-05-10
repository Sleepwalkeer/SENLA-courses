package eu.senla.service.implementation;

import eu.senla.dto.accountDto.CreateAccountDto;
import eu.senla.dto.accountDto.ResponseAccountDto;
import eu.senla.dto.accountDto.UpdateAccountDto;
import eu.senla.dto.credentialsDto.CredentialsDto;
import eu.senla.entity.Account;
import eu.senla.exception.InsufficientFundsException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.AccountRepository;
import eu.senla.service.AccountService;
import eu.senla.utils.specification.account.AccountSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

//    @Value("${customer_increment_discount_threshold}")
//    private BigDecimal CUSTOMER_INCREMENT_DISCOUNT_THRESHOLD;

    private final BigDecimal CUSTOMER_INCREMENT_DISCOUNT_THRESHOLD = new BigDecimal(30);

    public ResponseAccountDto getById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No account with ID " + id + " was found"));
        if (account.isDeleted()){
            throw new NotFoundException("The account with ID " + id + "has been deleted");
        }
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
        accountRepository.findById(id)
                .filter(acc-> !acc.isDeleted())
                .orElseThrow(() -> new NotFoundException("No account with ID " + id + " was found"));
                Account account = modelMapper.map(accountDto, Account.class);
                Account updatedAccount = accountRepository.save(account);
                return modelMapper.map(updatedAccount, ResponseAccountDto.class);
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

    @Transactional
    public void incrementCustomerDiscount(Account account) {
        BigDecimal discount = account.getDiscount();
        if (discount.compareTo(CUSTOMER_INCREMENT_DISCOUNT_THRESHOLD) < 0) {
            discount = discount.add(BigDecimal.ONE);
            accountRepository.updateAccountDiscount(account.getId(), discount);
        }
    }

    public List<ResponseAccountDto> getAccountsWithFilters(Integer pageNo, Integer pageSize, Map<String, String> filterParams) {
        Page<Account> accountPage;
        Pageable paging = PageRequest.of(pageNo, pageSize);
        if (!filterParams.isEmpty()) {
            Specification<Account> spec = AccountSpecifications.createSpecificationFromFilters(filterParams);
            accountPage = accountRepository.findAll(spec, paging);
        } else {
            accountPage = accountRepository.findAll(paging);
        }
        return accountPage.getContent()
                .stream()
                .map(account -> modelMapper.map(account, ResponseAccountDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void withdrawBalance(Account account, BigDecimal balance) {
        if (account.getBalance().compareTo(balance) >= 0) {
            accountRepository.updateAccountBalance(account.getId(), balance);
        } else {
            throw new InsufficientFundsException("Insufficient funds. The account does not have enough funds to process this order.");
        }
    }
}

