package eu.senla.service;

import eu.senla.dto.accountDto.CreateAccountDto;
import eu.senla.dto.accountDto.ResponseAccountDto;
import eu.senla.dto.accountDto.UpdateAccountDto;
import eu.senla.dto.credentialsDto.CredentialsDto;
import eu.senla.entity.Account;
import eu.senla.entity.Credentials;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.AccountRepository;
import eu.senla.service.implementation.AccountServiceImpl;
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
        Account account = Account.builder()
                .id(1L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark")
                .credentials(Credentials.builder()
                        .password("test")
                        .username("test")
                        .build())
                .build();
        CreateAccountDto accountDto = CreateAccountDto.builder()
                .id(1L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark")
                .credentials(CredentialsDto.builder()
                        .password("test")
                        .username("test")
                        .build())
                .build();

        when(accountRepository.save(account)).thenReturn(account);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);
        when(passwordEncoder.encode(accountDto.getCredentials().getPassword())).thenReturn("test");

        accountService.create(accountDto);

        verify(accountRepository).save(account);
    }

//    @Test
//    public void createWithInvalidDataTest() {
//        CreateAccountDto accountDto = CreateAccountDto.builder().id(1L).firstName("Bill").secondName("Stark").build();
//        Assertions.assertThrows(BadRequestException.class, () -> accountService.create(accountDto));
//    }

    @Test
    public void getByIdTest() {
        Account account = Account.builder()
                .id(1L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark").build();

        ResponseAccountDto accountDto = ResponseAccountDto.builder()
                .id(1L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark").build();

        when(accountRepository.findById(1L)).thenReturn(Optional.ofNullable(account));
        when(modelMapper.map(account, ResponseAccountDto.class)).thenReturn(accountDto);

        ResponseAccountDto accountDtoRetrieved = accountService.getById(1L);

        verify(accountRepository).findById(1L);
        Assertions.assertNotNull(accountDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> accountService.getById(1L));
        verify(accountRepository).findById(1L);
    }

    @Test
    public void updateTest() {
        Account account = Account.builder()
                .id(1L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark")
                .build();

        UpdateAccountDto accountDto = UpdateAccountDto.builder()
                .id(1L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark")
                .build();

        ResponseAccountDto responseAccountDto = ResponseAccountDto
                .builder()
                .email("test@gmail.com")
                .firstName("Bill")
                .secondName("Stark")
                .id(1L)
                .phone("+375331234124")
                .build();

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(account, ResponseAccountDto.class)).thenReturn(responseAccountDto);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        ResponseAccountDto accountDtoRetrieved = accountService.update(1L, accountDto);

        Assertions.assertNotNull(accountDtoRetrieved);
        verify(accountRepository).existsById(1L);
        verify(accountRepository).save(account);
    }

    @Test
    public void updateNonExistentAccountTest() {
        Account account = Account.builder()
                .id(1L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark")
                .build();

        UpdateAccountDto accountDto = UpdateAccountDto.builder()
                .id(1L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark")
                .build();

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.existsById(1L)).thenReturn(false);
        when(modelMapper.map(account, UpdateAccountDto.class)).thenReturn(accountDto);
        when(modelMapper.map(accountDto, Account.class)).thenReturn(account);

        Assertions.assertThrows(NotFoundException.class, () -> accountService.update(1L, accountDto));
        verify(accountRepository).existsById(1L);
    }


    @Test
    public void deleteByIdTest() {
        doNothing().when(accountRepository).deleteById(1L);
        when(accountRepository.existsById(1L)).thenReturn(true);
        accountService.deleteById(1L);
        verify(accountRepository).deleteById(1L);
    }

    @Test
    public void getAllTest() {
        ResponseAccountDto accountDto1 = ResponseAccountDto.builder()
                .id(1L)
                .email("blablacar@gmail.com")
                .phone("+375331234")
                .firstName("Billy")
                .secondName("StarkWall")
                .build();

        ResponseAccountDto accountDto2 = ResponseAccountDto.builder()
                .id(2L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark")
                .build();
        List<ResponseAccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(accountDto1);
        accountDtos.add(accountDto2);

        Account account1 = Account.builder()
                .id(1L)
                .email("blablacar@gmail.com")
                .phone("+375331234")
                .firstName("Billy")
                .secondName("StarkWall")
                .build();

        Account account2 = Account.builder()
                .id(2L)
                .email("blabla@gmail.com")
                .phone("+375331234124")
                .firstName("Bill")
                .secondName("Stark")
                .build();

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        Page<Account> accountPage = new PageImpl<>(accounts, paging, accounts.size());

        when(accountRepository.findAll(paging)).thenReturn(accountPage);
        when(modelMapper.map(eq(account1), eq(ResponseAccountDto.class)))
                .thenReturn(accountDto1);
        when(modelMapper.map(eq(account2), eq(ResponseAccountDto.class)))
                .thenReturn(accountDto2);

        List<ResponseAccountDto> retrievedAccountDtos = accountService.getAll(1, 2, "id");

        verify(accountRepository).findAll(paging);
    }
}
