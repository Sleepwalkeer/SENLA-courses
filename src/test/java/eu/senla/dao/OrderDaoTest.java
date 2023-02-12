package eu.senla.dao;

import eu.senla.Config;
import eu.senla.entities.*;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = {Config.class})
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderDaoTest {
    @Autowired
    OrderDao orderDao;
    @Autowired
    ItemDao itemDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    CategoryDao categoryDao;

    @BeforeAll
    public void setUp() {
        fillDatabaseWithDummyData();
    }


    @Test
    public void findyByIdEagerTest() {

        List<Item> items = itemDao.findAll();
        Order order = Order.builder().id(1).customer(Account.builder().id(1).build())
                .worker(Account.builder().id(1).build()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderDao.save(order);

        Order orderFromDb = orderDao.findByIdEager(1);

        Assertions.assertEquals(order, orderFromDb);
        Assertions.assertEquals(order.getWorker(), orderFromDb.getWorker());
        Assertions.assertEquals(order.getCustomer(), orderFromDb.getCustomer());
        Assertions.assertArrayEquals(order.getItems().toArray(), orderFromDb.getItems().toArray());
    }


    @Test
    public void updateTest() {
        Optional<Order> orderOptional = orderDao.findById(1);
        Order order = orderOptional.get();
        order.setEndDateTime(new Timestamp(1675855790625L));
        Order orderFromDb = orderDao.update(order);
        Assertions.assertEquals(order.getEndDateTime(), orderFromDb.getEndDateTime());
    }

    @Test
    public void deleteByIdTest() {
        orderDao.deleteById(2);
        Assertions.assertNull(orderDao.findById(2));
    }

    @Test
    public void addInvalidDataTest() {
        Order order = Order.builder().customer(Account.builder().id(1).build()).worker(Account.builder().id(2).build())
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(11600)).build();
        Assertions.assertThrows(PersistenceException.class, () -> orderDao.save(order));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertNull(orderDao.findById(5));
    }

    @Test
    public void getLazyAssociationsWithoutTransactionalTest() {
        Optional<Order> orderOptional = orderDao.findById(1);
        Order order = orderOptional.get();
        List<Item> items = order.getItems();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(items));
    }

    private void fillDatabaseWithDummyData() {
        Account customer = Account.builder().firstName("Lorence").secondName("Spall")
                .phone("+375298201276").email("spalllorence@mail.ru")
                .credentials(Credentials.builder().username("spalll").password("karjtkl").build()).build();
        accountDao.save(customer);

        Account worker = Account.builder().firstName("Nigel").secondName("Swift")
                .phone("+375331812012").email("swiftnigelswift@gmail.com")
                .credentials(Credentials.builder().username("swifty").password("kl4jlk34r").build()).build();
        accountDao.save(worker);

        Category category = Category.builder().name("Construction tools").build();
        categoryDao.save(category);

        Category category1 = Category.builder().name("Banking items").build();
        categoryDao.save(category1);

        Category category2 = Category.builder().name("Storages").build();
        categoryDao.save(category2);

        Item jackhammer = Item.builder()
                .category(categoryDao.findById(1).get())
                .name("Excavator").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);
        Item angleGrinder = Item.builder()
                .category(categoryDao.findById(1).get())
                .name("Drilling machine").price(new BigDecimal(600)).quantity(15).build();
        itemDao.save(angleGrinder);

        Item twoBedApp = Item.builder()
                .category(categoryDao.findById(3).get())
                .name("4-bedroom app").price(new BigDecimal(4235)).quantity(2).build();
        itemDao.save(twoBedApp);

        Item lamborghini = Item.builder()
                .category(categoryDao.findById(2).get())
                .name("Porsche").price(new BigDecimal(7200)).quantity(1).build();
        itemDao.save(lamborghini);

        List<Item> items = itemDao.findAll();

        Order order = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(1).build()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderDao.save(order);

        items.remove(1);

        Order order1 = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(11600)).build();
        orderDao.save(order1);
    }
}
