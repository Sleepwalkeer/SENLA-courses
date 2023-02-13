package eu.senla.dao;

import eu.senla.Config;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;
import jakarta.persistence.PersistenceException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ContextConfiguration(classes = {Config.class})
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDaoTest {
    @Autowired
    AccountDao accountDao;

    @BeforeAll
    public void setUp() {
        fillDatabaseWithDummyData();
    }


    @Test
    public void findyByIdTest() {
        Account account = Account.builder().id(1).firstName("Mallory").secondName("Kay")
                .phone("+375298201846").email("malory.kay.p@gmail.com")
                .credentials(Credentials.builder().username("MalloryKayP").password("kz25bj2jk23r").build()).build();

        Optional<Account> accountFromDb = accountDao.findById(1);
        Assertions.assertEquals(account, accountFromDb.get());
    }

    @Test
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
    public void deleteByIdTest() {
        accountDao.deleteById(3);
        Assertions.assertNull(accountDao.findById(3));
    }

    @Test
    public void addInvalidDataTest() {
        Account account = Account.builder().firstName("Mallory").secondName("Kay")
                .phone("+375298201846").email("malory.kay.p@gmail.com")
                .credentials(Credentials.builder().username("MalloryKayP").password("kz25bj2jk23r").build()).build();
        account.setPhone("+88005553535");
        Assertions.assertThrows(PersistenceException.class, () -> accountDao.save(account));
        System.out.println();
    }

    @Test
    public void getLazyAccociationsWithoutTransactionalTest() {
        Credentials credentials = accountDao.findById(1).get().getCredentials();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(credentials));
    }

    private void fillDatabaseWithDummyData() {
        Account customer = Account.builder().firstName("Mallory").secondName("Kay")
                .phone("+375298201846").email("malory.kay.p@gmail.com")
                .credentials(Credentials.builder().username("MalloryKayP").password("eurr2t4pr2").build()).build();
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