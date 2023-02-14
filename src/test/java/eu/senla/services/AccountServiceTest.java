package eu.senla.services;

import eu.senla.dao.AccountDao;
import eu.senla.dao.CredentialsDao;
import eu.senla.dto.AccountDto;
import eu.senla.dto.CredentialsDto;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountDao accountDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTest() {
        Account account = Account.builder().id(1).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        doNothing().when(accountDao).save(account);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        accountService.create(accountDto);

        verify(accountDao).save(account);
    }

    @Test
    public void createWithInvalidDataTest() {
        AccountDto accountDto = AccountDto.builder().id(1).firstName("Bill").secondName("Stark").build();
        Assertions.assertThrows(BadRequestException.class, () -> accountService.create(accountDto));
    }

    @Test
    public void getByIdTest() {
        Account account = Account.builder().id(1).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        when(accountDao.findById(1)).thenReturn(Optional.ofNullable(account));
        when(modelMapper.map(account, AccountDto.class)).thenReturn(accountDto);

        AccountDto accountDtoRetrieved = accountService.getById(1);

        verify(accountDao).findById(1);
        Assertions.assertNotNull(accountDto);
        Assertions.assertEquals(accountDto, accountDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(accountDao.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> accountService.getById(1));
        verify(accountDao).findById(1);
    }

    @Test
    public void updateTest() {
        Account account = Account.builder().id(1).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        when(accountDao.update(account)).thenReturn(account);
        when(accountDao.findById(1)).thenReturn(Optional.ofNullable(account));
        when(modelMapper.map(account, AccountDto.class)).thenReturn(accountDto);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        AccountDto accountDtoRetrieved = accountService.update(1, accountDto);


        verify(accountDao).findById(1);
        verify(accountDao).update(account);
        Assertions.assertEquals(accountDto, accountDtoRetrieved);
    }

    @Test
    public void updateNonExistentAccountTest() {
        Account account = Account.builder().id(1).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        when(accountDao.update(account)).thenReturn(account);
        when(accountDao.findById(1)).thenReturn(Optional.empty());
        when(modelMapper.map(account, AccountDto.class)).thenReturn(accountDto);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        Assertions.assertThrows(NotFoundException.class, () -> accountService.update(1, accountDto));
        verify(accountDao).findById(1);
    }


    @Test
    public void deleteTest() {
        Account account = Account.builder().id(1).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        when(accountDao.delete(account)).thenReturn(true);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        Assertions.assertTrue(accountService.delete(accountDto));
        verify(accountDao).delete(account);
    }

    @Test
    public void deleteNonExistentAccountTest() {
        Account account = Account.builder().id(1).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        AccountDto accountDto = AccountDto.builder().id(1).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();

        when(accountDao.delete(account)).thenReturn(false);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        Assertions.assertThrows(NotFoundException.class,()-> accountService.delete(accountDto));
        verify(accountDao).delete(account);
    }

    @Test
    @Transactional
    public void deleteByIdTest() {
        when(accountDao.deleteById(1)).thenReturn(true);

        Assertions.assertTrue(accountService.deleteById(1));
        verify(accountDao).deleteById(1);
    }

    @Test
    public void deleteByNonExistentIdTest() {
        when(accountDao.deleteById(1)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class,()-> accountService.deleteById(1));
        verify(accountDao).deleteById(1);
    }

    @Test
    public void getAllTest() {
        AccountDto accountDto1 = AccountDto.builder().id(1).credentials(CredentialsDto.builder().password("tost").username("tost").build())
                .email("blablacar@gmail.com").phone("+375331234").firstName("Billy").secondName("StarkWall").build();
        AccountDto accountDto2 = AccountDto.builder().id(2).credentials(CredentialsDto.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(accountDto1);
        accountDtos.add(accountDto2);

        Account account1 = Account.builder().id(1).credentials(Credentials.builder().password("tost").username("tost").build())
                .email("blablacar@gmail.com").phone("+375331234").firstName("Billy").secondName("StarkWall").build();
        Account account2 = Account.builder().id(2).credentials(Credentials.builder().password("test").username("test").build())
                .email("blabla@gmail.com").phone("+375331234124").firstName("Bill").secondName("Stark").build();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        when(accountDao.findAll()).thenReturn(accounts);
        when(modelMapper.map(eq(account1), eq(AccountDto.class)))
                .thenReturn(accountDto1);
        when(modelMapper.map(eq(account2), eq(AccountDto.class)))
                .thenReturn(accountDto2);

        List<AccountDto> retrievedAccountDtos = accountService.getAll();

        verify(accountDao).findAll();
        Assertions.assertIterableEquals(accountDtos, retrievedAccountDtos);
    }

    @Test
    public void getAllWithDatabaseAccessExceptionTest() {
        when(accountDao.findAll()).thenThrow(new RuntimeException());
        Assertions.assertThrows(DatabaseAccessException.class, () -> accountService.getAll());
        verify(accountDao).findAll();
    }
}
