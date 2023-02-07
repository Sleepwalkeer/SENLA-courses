package eu.senla.dao;

import eu.senla.Config;
import eu.senla.entities.Account;
import eu.senla.entities.Category;
import eu.senla.entities.Credentials;

import eu.senla.entities.Item;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DaoTest {

    static AnnotationConfigApplicationContext context;
    static AccountDao accountDao;
    static CategoryDao categoryDao;
    static CredentialsDao credentialsDao;
    static OrderDao orderDao;
    static ItemDao itemDao;

    @BeforeAll
    public static void setUp() {
        context = new AnnotationConfigApplicationContext(Config.class);
        accountDao = context.getBean(AccountDao.class);
        categoryDao = context.getBean(CategoryDao.class);
        credentialsDao = context.getBean(CredentialsDao.class);
        orderDao = context.getBean(OrderDao.class);
        itemDao = context.getBean(ItemDao.class);

        Account customer = new Account("Mallory",
                "Kay", "+375298201846", "malory.kay.p@gmail.com",
                new Credentials("MalloryKayP", "kz25bj2jk23r"));
        accountDao.save(customer);

        Account worker = new Account("Logan",
                "Holmes", "+375338182012", "holmesloganp@gmail.com",
                new Credentials("loganholmes", "sdgkkfjdsg"));
        accountDao.save(worker);

        Account forDeletion = new Account("skfsfd",
                "Ksdfs", "+375298846", "@gmail.com",
                new Credentials("Mal", "sdsdfsdr"));
        accountDao.save(forDeletion);

        Category category = new Category("Construction equipment");
        categoryDao.save(category);

        Category category1 = new Category("Vehicles");
        categoryDao.save(category1);

        Category category2 = new Category("Real estate");
        categoryDao.save(category2);

        Category category3 = new Category("ForDeletion");
        categoryDao.save(category3);

        Item jackhammer = new Item(new Category(1, "Construction equipment"), "Jackhammer", new BigDecimal(750), 8);
        itemDao.save(jackhammer);
        Item angleGrinder = new Item(new Category(1, "Construction equipment"), "Angle grinder", new BigDecimal(600), 15);
        itemDao.save(angleGrinder);
        Item twoBedApp = new Item(new Category(3, "Real estate"), "2-bedroom app", new BigDecimal(3450), 2);
        itemDao.save(twoBedApp);
        Item lamborghini = new Item(new Category(2, "Vehicles"), "Lamborghini", new BigDecimal(6300), 1);
        itemDao.save(lamborghini);

        List<Item> items = itemDao.findAll();

        Item itemForDeletion = new Item(new Category(3, "Real estate"), "test", new BigDecimal(630022), 122);
        itemDao.save(itemForDeletion);

        eu.senla.entities.Order order = new eu.senla.entities.Order(new Account(1), new Account(2), items,
                new Timestamp(1665778114323L), new Timestamp(1675778114323L), new BigDecimal(12200));
        orderDao.save(order);

        items.remove(1);

        eu.senla.entities.Order order1 = new eu.senla.entities.Order(new Account(1), new Account(2), items,
                new Timestamp(1665733114323L), new Timestamp(1675278114323L), new BigDecimal(11600));
        orderDao.save(order1);

    }


    @Test
    @Order(1)
    public void accountDaoTest() {
        Account account = new Account(1, "Mallory",
                "Kay", "+375298201846", "malory.kay.p@gmail.com",
                new Credentials("MalloryKayP", "kz25bj2jk23r"));

        Account accountFromDb = accountDao.findById(1);
        Assertions.assertEquals(account, accountFromDb);


        accountFromDb.setPhone("+88005553535");
        accountFromDb = accountDao.update(accountFromDb);
        account.setPhone("+88005553535");
        Assertions.assertEquals(account.getPhone(), accountFromDb.getPhone());

        accountDao.deleteById(3);
        Assertions.assertNull(accountDao.findById(3));
    }

    @Test
    @Order(1)
    public void categoryDaoTest() {
        Category category = new Category(1, "Construction equipment");

        Category categoryFromDb = categoryDao.findById(1);
        Assertions.assertEquals(category, categoryFromDb);


        category.setName("Construction Tools");
        categoryFromDb = categoryDao.update(category);
        Assertions.assertEquals(category.getName(), categoryFromDb.getName());

        categoryDao.deleteById(4);
        Assertions.assertNull(categoryDao.findById(4));
    }

    @Test
    @Order(2)
    public void itemDaoTest() {
        Item item = new Item(4, new Category(2, "Vehicles"),
                "Lamborghini", new BigDecimal(6300), 1);

        Item itemFromDb = itemDao.findById(4);
        Assertions.assertEquals(item, itemFromDb);


        item.setName("Ferrari");
        itemFromDb = itemDao.update(item);
        Assertions.assertEquals(item.getName(), itemFromDb.getName());

        itemDao.deleteById(5);
        Assertions.assertNull(itemDao.findById(5));
    }

    @Test
    @Order(3)
    public void orderDaoTest() {

        List<Item> items = itemDao.findAll();

        Account customer = new Account(1);

        Account worker = new Account(2);

        eu.senla.entities.Order order = new eu.senla.entities.Order(1, customer, worker, items,
                new Timestamp(1665778114323L), new Timestamp(1675778114323L), new BigDecimal(12200));

        eu.senla.entities.Order orderFromDb = orderDao.findById(1);
        Assertions.assertEquals(order, orderFromDb);


        order.setEndDateTime(new Timestamp(1675778224323L));
        orderFromDb = orderDao.update(order);
        Assertions.assertEquals(order.getEndDateTime(), orderFromDb.getEndDateTime());

        orderDao.deleteById(2);
        Assertions.assertNull(orderDao.findById(5));

        eu.senla.entities.Order orderEagerFetch = orderDao.findByIdEager(1);
        List<Item> itemList = orderEagerFetch.getItems();

        for (int i = 0; i < itemList.size(); i++) {
            Assertions.assertEquals(itemList.get(i), items.get(i));

        }
    }
}