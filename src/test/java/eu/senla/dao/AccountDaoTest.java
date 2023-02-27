package eu.senla.dao;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;
import eu.senla.entities.Role;
import jakarta.persistence.PersistenceException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class AccountDaoTest extends ContainersEnvironment {
    @Autowired
    AccountDao accountDao;

    @Test
    public void findyByIdTest() {
        fillFindByIdDummyData();
        Account account = Account.builder().id(1).firstName("Mallory").secondName("Kay")
                .phone("+375298201846").email("malory.kay.p@gmail.com")
                .credentials(Credentials.builder().username("MalloryKayP").password("kz25bj2jk23r").role(Role.USER)
                        .build())
                .build();

        Optional<Account> accountFromDb = accountDao.findById(1);
        Assertions.assertEquals(account, accountFromDb.get());
    }

    private void fillFindByIdDummyData() {
        Account testDaoAcc = Account.builder().firstName("testDaoAccFind").secondName("testDaoAccFind")
                .phone("testDaoAccFind").email("testDaoAccFind")
                .credentials(Credentials.builder().username("testDaoAccFind").password("testDaoAccFind").role(Role.USER)
                        .build())
                .build();
        accountDao.save(testDaoAcc);
    }

    @Test
    public void updateTest() {
        fillUpdateDummyData();
        Optional<Account> accountOptional = accountDao.findByEmail("testDaoAccUpd");
        Account account = accountOptional.get();
        account.setPhone("+88005553535");
        account.setEmail("TestEmail");

        Account accountFromDb = accountDao.update(account);

        Assertions.assertEquals(account.getPhone(), accountFromDb.getPhone());
        Assertions.assertEquals(account.getEmail(), accountFromDb.getEmail());
    }

    private void fillUpdateDummyData() {
        Account testDaoAcc = Account.builder().firstName("testDaoAccUpd").secondName("testDaoAccUpd")
                .phone("testDaoAccUpd").email("testDaoAccUpd")
                .credentials(Credentials.builder().username("testDaoAccUpd").password("testDaoAccUpd").role(Role.USER)
                        .build())
                .build();
        accountDao.save(testDaoAcc);
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteByIdDummyData();
        int deleteId = accountDao.findByEmail("testDaoAcc1").get().getId();
        accountDao.deleteById(deleteId);
        Assertions.assertFalse(accountDao.findById(deleteId).isPresent());
    }

    private void fillDeleteByIdDummyData() {
        Account testDaoAcc = Account.builder().firstName("testDaoAcc").secondName("testDaoAcc")
                .phone("testDaoAcc").email("testDaoAcc")
                .credentials(Credentials.builder().username("testDaoAcc").password("testDaoAcc").role(Role.USER).build())
                .build();
        accountDao.save(testDaoAcc);

        Account testDaoAcc1 = Account.builder().firstName("testDaoAcc1").secondName("testDaoAcc1")
                .phone("testDaoAcc1").email("testDaoAcc1")
                .credentials(Credentials.builder().username("testDaoAcc1").password("testDaoAcc1").role(Role.USER).build())
                .build();
        accountDao.save(testDaoAcc1);
        Account testDaoAcc2 = Account.builder().firstName("testDaoAcc2").secondName("testDaoAcc2")
                .phone("testDaoAcc2").email("testDaoAcc2")
                .credentials(Credentials.builder().username("testDaoAcc2").password("testDaoAcc2").role(Role.USER).build())
                .build();
        accountDao.save(testDaoAcc2);
        Account testDaoAcc3 = Account.builder().firstName("testDaoAcc3").secondName("testDaoAcc3")
                .phone("testDaoAcc3").email("testDaoAcc3")
                .credentials(Credentials.builder().username("testDaoAcc3").password("testDaoAcc3").role(Role.USER).build())
                .build();
        accountDao.save(testDaoAcc3);
        Account testDaoAcc4 = Account.builder().firstName("testDaoAcc4").secondName("testDaoAcc4")
                .phone("testDaoAcc4").email("testDaoAcc4")
                .credentials(Credentials.builder().username("testDaoAcc4").password("testDaoAcc4").role(Role.USER).build())
                .build();
        accountDao.save(testDaoAcc4);
        Account testDaoAcc5 = Account.builder().firstName("testDaoAcc5").secondName("testDaoAcc5")
                .phone("testDaoAcc5").email("testDaoAcc5")
                .credentials(Credentials.builder().username("testDaoAcc5").password("testDaoAcc5").role(Role.USER).build())
                .build();
        accountDao.save(testDaoAcc5);
        Account testDaoAcc6 = Account.builder().firstName("testDaoAcc6").secondName("testDaoAcc6")
                .phone("testDaoAcc6").email("testDaoAcc6")
                .credentials(Credentials.builder().username("testDaoAcc6").password("testDaoAcc6").role(Role.USER).build())
                .build();
        accountDao.save(testDaoAcc6);
    }

    @Test
    public void addInvalidDataTest() {
        Account account = Account.builder().firstName("Mallory").secondName("Kay")
                .phone("+375298201846").credentials(Credentials.builder().password("kz25bj2jk23r").role(Role.USER).build())
                .build();
        account.setPhone("+88005553535");
        Assertions.assertThrows(PersistenceException.class, () -> accountDao.save(account));
        System.out.println();
    }

    @Test
    public void getLazyAccociationsWithoutTransactionalTest() {
        Credentials credentials = accountDao.findById(1).get().getCredentials();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(credentials));
    }

}