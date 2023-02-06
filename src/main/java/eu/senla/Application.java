package eu.senla;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.senla.configuration.Config;
import eu.senla.controllers.OrderController;
import eu.senla.dao.AccountDao;
import eu.senla.dao.CategoryDao;
import eu.senla.dao.CredentialsDao;
import eu.senla.dao.ItemDao;
import eu.senla.entities.Account;
import eu.senla.entities.Category;
import eu.senla.entities.Credentials;
import eu.senla.entities.Item;
import eu.senla.services.ItemService;
import eu.senla.services.ItemServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.math.BigDecimal;
import java.util.List;


public class Application {
    public static void main(String[] args) throws JsonProcessingException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//        AccountDao accountDao = context.getBean(AccountDao.class);
//        Account account = new Account("Mallory","Kay","+375331431430","MalloryKay@gmail.com");
//        Credentials credentials = new Credentials(1, "MalloryKayP", "ultrasuperstrongpass");
//        account.setCredentials(credentials);
//        accountDao.save(account);

//        CredentialsDao credentialsDao = context.getBean(CredentialsDao.class);
//        Credentials credentials = new Credentials(2, "Jake", "pass");
//        credentialsDao.save(credentials);

//        CategoryDao categoryDao = context.getBean(CategoryDao.class);
//        ItemService itemService = context.getBean(ItemService.class);
//           itemDao.save(item);
//        System.out.println(categoryDao.findAll());
//        System.out.println(itemService.getAll());
//        item1.setCategory(itemDao.getCategory(item1));
//        System.out.println(itemDao.findAll());
        context.close();
    }

}
