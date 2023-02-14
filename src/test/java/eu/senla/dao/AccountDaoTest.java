package eu.senla.dao;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.TestContainers;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;
import jakarta.persistence.PersistenceException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountDaoTest extends ContainersEnvironment {
    @Autowired
    AccountDao accountDao;

    @Test
    @Order(1)
    public void findyByIdTest() {
        fillWithDummyData();
        Account account = Account.builder().id(1).firstName("Mallory").secondName("Kay")
                .phone("+375298201846").email("malory.kay.p@gmail.com")
                .credentials(Credentials.builder().username("MalloryKayP").password("kz25bj2jk23r").build()).build();

        Optional<Account> accountFromDb = accountDao.findById(1);
        Assertions.assertEquals(account, accountFromDb.get());
    }

    @Test
    @Transactional
    public void updateTest() {
        Optional<Account> accountOptional = accountDao.findById(1);
        Account account = accountOptional.get();
        account.setPhone("+88005553535");
        account.setEmail("TestEmail");

        Account accountFromDb = accountDao.update(account);

        Assertions.assertEquals(account.getPhone(), accountFromDb.getPhone());
        Assertions.assertEquals(account.getEmail(), accountFromDb.getEmail());
    }

    @Test
    @Transactional
    public void deleteByIdTest() {
        accountDao.deleteById(1);
        Assertions.assertFalse(accountDao.findById(1).isPresent());
    }

    @Test
    public void addInvalidDataTest() {
        Account account = Account.builder().firstName("Mallory").secondName("Kay")
                .phone("+375298201846").credentials(Credentials.builder().username("MalloryKayP").password("kz25bj2jk23r").build()).build();
        account.setPhone("+88005553535");
        Assertions.assertThrows(PersistenceException.class, () -> accountDao.save(account));
        System.out.println();
    }

    @Test
    public void getLazyAccociationsWithoutTransactionalTest() {
        Credentials credentials = accountDao.findById(1).get().getCredentials();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(credentials));
    }

    private void fillWithDummyData() {
        Account customer = Account.builder().firstName("testDao").secondName("testDao")
                .phone("+375298201846").email("malory.kay.p@gmail.com")
                .credentials(Credentials.builder().username("testDao").password("testDao").build()).build();
        accountDao.save(customer);

        Account worker = Account.builder().firstName("Logan").secondName("Holmes")
                .phone("+375338182012").email("holmesloganp@gmail.com")
                .credentials(Credentials.builder().username("loganholmes").password("sdgkkfjdsg").build()).build();
        accountDao.save(worker);

        Account forDeletion = Account.builder().firstName("twets").secondName("tarts")
                .phone("+3752846").email("@gmail.com")
                .credentials(Credentials.builder().username("MalyP").password("eur2").build()).build();
        accountDao.save(forDeletion);
    }
}