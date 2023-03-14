package eu.senla.service;

import eu.senla.repository.AccountRepository;
import eu.senla.dto.AccountDto;
import eu.senla.dto.CredentialsDto;
import eu.senla.entity.Account;
import eu.senla.entity.Credentials;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTest() {
        Account account = Account.builder().id(1L).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375 33 123 41 24").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1L).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375 33 123 41 24").firstName("Bill").secondName("Stark").build();

        when(accountRepository.save(account)).thenReturn(account);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);
        when(passwordEncoder.encode(accountDto.getCredentials().getPassword())).thenReturn("test");

        accountService.create(accountDto);

        verify(accountRepository).save(account);
    }

    @Test
    public void createWithInvalidDataTest() {
        AccountDto accountDto = AccountDto.builder().id(1L).firstName("Bill").secondName("Stark").build();
        Assertions.assertThrows(BadRequestException.class, () -> accountService.create(accountDto));
    }

    @Test
    public void getByIdTest() {
        Account account = Account.builder().id(1L).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1L).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        when(accountRepository.findById(1L)).thenReturn(Optional.ofNullable(account));
        when(modelMapper.map(account, AccountDto.class)).thenReturn(accountDto);

        AccountDto accountDtoRetrieved = accountService.getById(1L);

        verify(accountRepository).findById(1L);
        Assertions.assertNotNull(accountDto);
        Assertions.assertEquals(accountDto, accountDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> accountService.getById(1L));
        verify(accountRepository).findById(1L);
    }

    @Test
    public void updateTest() {
        Account account = Account.builder().id(1L).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1L).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.ofNullable(account));
        when(modelMapper.map(account, AccountDto.class)).thenReturn(accountDto);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        AccountDto accountDtoRetrieved = accountService.update(1L, accountDto);


        verify(accountRepository).findById(1L);
        verify(accountRepository).save(account);
        Assertions.assertEquals(accountDto, accountDtoRetrieved);
    }

    @Test
    public void updateNonExistentAccountTest() {
        Account account = Account.builder().id(1L).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1L).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        when(modelMapper.map(account, AccountDto.class)).thenReturn(accountDto);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        Assertions.assertThrows(NotFoundException.class, () -> accountService.update(1L, accountDto));
        verify(accountRepository).findById(1L);
    }


    @Test
    public void deleteTest() {
        Account account = Account.builder().id(1L).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1L).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        doNothing().when(accountRepository).delete(account);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        accountService.delete(accountDto);
        verify(accountRepository).delete(account);
    }

    @Test
    public void deleteByIdTest() {
        doNothing().when(accountRepository).deleteById(1L);
        accountService.deleteById(1L);
        verify(accountRepository).deleteById(1L);
    }

    @Test
    public void getAllTest() {
        AccountDto accountDto1 = AccountDto.builder().id(1L).credentials(CredentialsDto.builder().password("tost").username("tost").build())
                .email("blablacar@gmail.com").phone("+375331234").firstName("Billy").secondName("StarkWall").build();
        AccountDto accountDto2 = AccountDto.builder().id(2L).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(accountDto1);
        accountDtos.add(accountDto2);

        Account account1 = Account.builder().id(1L).credentials(Credentials.builder().password("tost").username("tost").build())
                .email("blablacar@gmail.com").phone("+375331234").firstName("Billy").secondName("StarkWall").build();
        Account account2 = Account.builder().id(2L).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        Page<Account> accountPage = new PageImpl<>(accounts, paging, accounts.size());

        when(accountRepository.findAll(paging)).thenReturn(accountPage);
        when(modelMapper.map(eq(account1), eq(AccountDto.class)))
                .thenReturn(accountDto1);
        when(modelMapper.map(eq(account2), eq(AccountDto.class)))
                .thenReturn(accountDto2);

        List<AccountDto> retrievedAccountDtos = accountService.getAll(1, 2, "id");

        verify(accountRepository).findAll(paging);
        Assertions.assertIterableEquals(accountDtos, retrievedAccountDtos);
    }

    @Test
    public void getAllWithDatabaseAccessExceptionTest() {
        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        when(accountRepository.findAll(paging)).thenThrow(new RuntimeException());
        Assertions.assertThrows(DatabaseAccessException.class, () -> accountService.getAll(1, 2, "id"));
        verify(accountRepository).findAll(paging);
    }
}
