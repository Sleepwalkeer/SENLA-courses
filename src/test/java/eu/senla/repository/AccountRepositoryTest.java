package eu.senla.repository;

import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ContextConfigurationTest;
import eu.senla.entity.Account;
import eu.senla.entity.Credentials;
import eu.senla.entity.Role;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ContextConfigurationTest.class})
@WebAppConfiguration
public class AccountRepositoryTest extends ContainersEnvironment {
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findyByIdTest() {
        Optional<Account> accountFromDb = accountRepository.findById(1L);
        Assertions.assertEquals(1L, accountFromDb.get().getId());
    }

    @Test
    public void updateTest() {
        fillUpdateDummyData();
        Optional<Account> accountOptional = accountRepository.findByEmail("testDaoAccUpd");
        Account account = accountOptional.get();
        account.setPhone("+88005553535");
        account.setEmail("TestEmail");

        Account accountFromDb = accountRepository.save(account);

        Assertions.assertEquals(account.getPhone(), accountFromDb.getPhone());
        Assertions.assertEquals(account.getEmail(), accountFromDb.getEmail());
    }

    private void fillUpdateDummyData() {
        Account testDaoAcc = Account.builder()
                .firstName("testDaoAccUpd")
                .secondName("testDaoAccUpd")
                .phone("testDaoAccUpd")
                .email("testDaoAccUpd")
                .credentials(Credentials.builder()
                        .username("testDaoAccUpd")
                        .password("testDaoAccUpd")
                        .build())
                .build();
        accountRepository.save(testDaoAcc);
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteByIdDummyData();
        Long deleteId = accountRepository.findByEmail("testDaoAcc").get().getId();
        accountRepository.deleteById(deleteId);
        Assertions.assertFalse(accountRepository.findById(deleteId).isPresent());
    }

    private void fillDeleteByIdDummyData() {
        Account testDaoAcc = Account.builder()
                .firstName("testDaoAcc")
                .secondName("testDaoAcc")
                .phone("testDaoAcc")
                .email("testDaoAcc")
                .credentials(Credentials.builder()
                        .username("testDaoAcc")
                        .password("testDaoAcc")
                        .build())
                .build();
        accountRepository.save(testDaoAcc);
    }

    @Test
    public void addInvalidDataTest() {
        Account account = Account.builder()
                .firstName("Mallory")
                .secondName("Kay")
                .phone("+375298201846")
                .credentials(Credentials.builder()
                        .password("kz25bj2jk23r")
                        .role(Role.USER)
                        .build())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    public void getLazyAccociationsWithoutTransactionalTest() {
        Credentials credentials = accountRepository.findById(1L).get().getCredentials();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(credentials));
    }
}