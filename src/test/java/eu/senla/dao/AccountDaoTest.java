package eu.senla.dao;

import eu.senla.Config;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class AccountDaoTest {

    static AnnotationConfigApplicationContext context;
    static AccountDao accountDao;
//    static CategoryDao categoryDao;
//    static CredentialsDao credentialsDao;
//    static OrderDao orderDao;
//    static  ItemDao itemDao;

    @BeforeAll
    static void setUp() {
        context = new AnnotationConfigApplicationContext(Config.class);
        accountDao = context.getBean(AccountDao.class);
//        categoryDao = context.getBean(CategoryDao.class);
//        credentialsDao = context.getBean(CredentialsDao.class);
//        orderDao = context.getBean(OrderDao.class);
//        itemDao = context.getBean(ItemDao.class);
    }


    @Test
    void CreateAccountTest(){
        Account account = new Account(1,"Mallory",
                "Kay","+375298201846","malory.kay.p@gmail.com",
                new Credentials(1,"MalloryKayP","kz25bj2jk23r"));

        accountDao.save(account);

        Account accountFromDb = accountDao.findById(1);
        account.setPhone("3");
        Assertions.assertEquals(account, accountFromDb);
    }
}