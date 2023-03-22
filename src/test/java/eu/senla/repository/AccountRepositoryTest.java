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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ContextConfigurationTest.class})
@WebAppConfiguration
public class AccountRepositoryTest extends ContainersEnvironment {
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findyByIdTest() {
        Account testDaoAcc = Account.builder()
                .firstName("testDaoAccFind")
                .secondName("testDaoAccFind")
                .phone("testDaoAccFind")
                .email("testDaoAccFind")
                .credentials(Credentials.builder()
                        .username("testDaoAccFind")
                        .password("testDaoAccFind")
                        .build())
                .build();
        accountRepository.save(testDaoAcc);
        Long id = accountRepository.findByEmail("testDaoAccFind").get().getId();

        Optional<Account> accountFromDb = accountRepository.findById(id);
        Assertions.assertEquals(id, accountFromDb.get().getId());
    }

    @Test
    public void findyByNonexistentIdTest() {
        Optional<Account> accountFromDb = accountRepository.findById(100L);
        Assertions.assertFalse(accountFromDb.isPresent());
    }

    @Test
    public void updateTest() {
        fillUpdateTestDummyData();
        Optional<Account> accountOptional = accountRepository.findByEmail("testDaoAccUpd");
        Account account = accountOptional.get();
        account.setPhone("+88005553535");
        account.setEmail("TestEmail");

        Account accountFromDb = accountRepository.save(account);

        Assertions.assertEquals(account.getPhone(), accountFromDb.getPhone());
        Assertions.assertEquals(account.getEmail(), accountFromDb.getEmail());
    }

    private void fillUpdateTestDummyData() {
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
    public void updateInvalidDataTest() {
        fillUpdateInvalidDataTestDummyData();
        Optional<Account> accountOptional = accountRepository.findByEmail("testDaoAccUpdInvld");
        Account account = accountOptional.get();
        account.setPhone("+880055535383475834759834789579345");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    private void fillUpdateInvalidDataTestDummyData() {
        Account testDaoAcc = Account.builder()
                .firstName("testDaoAccUpdInvld")
                .secondName("testDaoAccUpdInvld")
                .phone("testDaoAccUpdInvld")
                .email("testDaoAccUpdInvld")
                .credentials(Credentials.builder()
                        .username("testDaoAccUpdInvld")
                        .password("testDaoAccUpdInvld")
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
    public void createInvalidDataTest() {
        Account account = Account.builder()
                .firstName("Mallorysfdsdf")
                .secondName("Ksdfadfssfdssdsdy")
                .phone("+375298201846")
                .credentials(Credentials.builder()
                        .password("kz25bj2jk23r")
                        .role(Role.USER)
                        .build())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    public void getLazyAssociationsWithoutTransactionalTest() {

        Account testDaoAcc = Account.builder()
                .firstName("LazyDaoAcc")
                .secondName("LazyDaoAcc")
                .phone("LazyDaoAcc")
                .email("LazyDaoAcc")
                .credentials(Credentials.builder()
                        .username("LazyDaoAcc")
                        .password("LazyDaoAcc")
                        .build())
                .build();
        accountRepository.save(testDaoAcc);

        Long id = accountRepository.findByEmail("LazyDaoAcc").get().getId();

        Credentials credentials = accountRepository.findById(id).get().getCredentials();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(credentials));
    }

    @Test
    @Transactional
    public void updateAccountDiscountTest() {
        fillUpdateAccountDiscountTestDummyData();
        Account account = accountRepository.findByEmail("DaoAccUpdDsc").get();
        account.setDiscount(new BigDecimal(50));
        accountRepository.updateAccountBalance(account.getId(), account.getDiscount());
        Account updatedAccount = accountRepository.findByEmail("DaoAccUpdDsc").get();
        Assertions.assertEquals(updatedAccount.getDiscount(), new BigDecimal(50));
    }

    private void fillUpdateAccountDiscountTestDummyData() {
        Account testDaoAcc = Account.builder()
                .firstName("testDaoAccUpdDsc")
                .secondName("testDaoAccUpdDsc")
                .phone("DaoAccUpdDsc")
                .email("DaoAccUpdDsc")
                .credentials(Credentials.builder()
                        .username("testDaoAccUpdDsc")
                        .password("testDaoAccUpdDsc")
                        .build())
                .build();
        accountRepository.save(testDaoAcc);
    }

    @Test
    @Transactional
    public void updateInvalidAccountDiscountTest() {
        fillUpdateInvalidAccountDiscountTestDummyData();
        Account account = accountRepository.findByEmail("InvlDaoAccUpdDsc").get();
        account.setDiscount(new BigDecimal(-50));
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.updateAccountDiscount(account.getId(), account.getDiscount()));
    }

    private void fillUpdateInvalidAccountDiscountTestDummyData() {
        Account testDaoAcc = Account.builder()
                .firstName("InvlDaoAccUpdDsc")
                .secondName("InvlDaoAccUpdDsc")
                .phone("InvlAccUpdDsc")
                .email("InvlDaoAccUpdDsc")
                .credentials(Credentials.builder()
                        .username("InvlDaoAccUpdDsc")
                        .password("InvlDaoAccUpdDsc")
                        .build())
                .build();
        accountRepository.save(testDaoAcc);
    }


    @Test
    @Transactional
    public void updateAccountBalanceTest() {
        fillUpdateAccountBalanceTestDummyData();
        Account account = accountRepository.findByEmail("DaoAccUpdBln").get();
        account.setBalance(new BigDecimal(50));
        accountRepository.updateAccountBalance(account.getId(), account.getBalance());
        Account updatedAccount = accountRepository.findByEmail("DaoAccUpdBln").get();
        Assertions.assertEquals(updatedAccount.getBalance(), new BigDecimal(50));
    }

    private void fillUpdateAccountBalanceTestDummyData() {
        Account testDaoAcc = Account.builder()
                .firstName("testDaoAccUpdBln")
                .secondName("testDaoAccUpdBln")
                .phone("DaoAccUpdBln")
                .email("DaoAccUpdBln")
                .credentials(Credentials.builder()
                        .username("testDaoAccUpdBln")
                        .password("testDaoAccUpdBln")
                        .build())
                .build();
        accountRepository.save(testDaoAcc);
    }

    @Test
    @Transactional
    public void updateInvalidAccountBalanceTest() {
        fillUpdateInvalidAccountBalanceTestDummyData();
        Account account = accountRepository.findByEmail("InvlDaoAccUpdBln").get();
        account.setBalance(new BigDecimal(-50));
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.updateAccountBalance(account.getId(), account.getBalance()));
    }

    private void fillUpdateInvalidAccountBalanceTestDummyData() {
        Account testDaoAcc = Account.builder()
                .firstName("InvlDaoAccUpdBln")
                .secondName("InvlDaoAccUpdBln")
                .phone("InvlAccUpdBln")
                .email("InvlDaoAccUpdBln")
                .credentials(Credentials.builder()
                        .username("InvlDaoAccUpdBln")
                        .password("InvlDaoAccUpdBln")
                        .build())
                .build();
        accountRepository.save(testDaoAcc);
    }
}