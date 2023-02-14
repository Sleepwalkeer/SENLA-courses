package eu.senla.dao;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.entities.*;
import jakarta.persistence.PersistenceException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
public class OrderDaoTest extends ContainersEnvironment {
    @Autowired
    OrderDao orderDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    CategoryDao categoryDao;


    @Test
    public void updateTest() {
        fillUpdateDummyData();
        Optional<Order> orderOptional = orderDao.findById(1);
        Order order = orderOptional.get();
        order.setEndDateTime(new Timestamp(1675855790625L));
        Order orderFromDb = orderDao.update(order);
        Assertions.assertEquals(order.getEndDateTime(), orderFromDb.getEndDateTime());
    }

    private void fillUpdateDummyData() {
        Account customer = Account.builder().firstName("orderDaoUpd").secondName("orderDaoUpd")
                .phone("orderDaoUpd").email("orderDaoUpd")
                .credentials(Credentials.builder().username("orderDaoUpd").password("orderDaoUpd").build()).build();
        accountDao.save(customer);

        Account customer1 = Account.builder().firstName("orderDaoUpd1").secondName("orderDaoUpd1")
                .phone("orderDaoUpd1").email("orderDaoUpd1")
                .credentials(Credentials.builder().username("orderDaoUpd1").password("orderDaoUpd1").build()).build();
        accountDao.save(customer1);

        Category category = Category.builder().name("orderDaoUpd").build();
        categoryDao.save(category);

        Item jackhammer = Item.builder()
                .category(categoryDao.findById(1).get())
                .name("Excavator2").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);

        List<Item> items = itemDao.findAll();

        Order order = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderDao.save(order);
    }

    @Test
    public void findyByIdTest() {
        fillFindByIdDummyData();
        List<Item> items = itemDao.findAll();
        Order order = Order.builder().id(1).customer(Account.builder().id(1).build())
                .worker(Account.builder().id(1).build()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        Optional<Order> orderFromDb = orderDao.findById(1);

        Assertions.assertEquals(order.getId(), orderFromDb.get().getId());

    }


    private void fillFindByIdDummyData() {
        Account customer = Account.builder().firstName("orderDaoFind").secondName("orderDaoFind")
                .phone("orderDaoFind").email("orderDaoFind")
                .credentials(Credentials.builder().username("orderDaoFind").password("orderDaoFind").build()).build();
        accountDao.save(customer);

        Account customer1 = Account.builder().firstName("orderDaoFind1").secondName("orderDaoFind1")
                .phone("orderDaoFind1").email("orderDaoFind1")
                .credentials(Credentials.builder().username("orderDaoFind1").password("orderDaoFind1").build()).build();
        accountDao.save(customer1);

        Category category = Category.builder().name("orderDaoFind").build();
        categoryDao.save(category);

        Item jackhammer = Item.builder()
                .category(categoryDao.findById(1).get())
                .name("Excavator1").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);

        List<Item> items = itemDao.findAll();

        Order order = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderDao.save(order);
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteByIdDummyData();
        orderDao.deleteById(6);
        Assertions.assertFalse(orderDao.findById(6).isPresent());
    }

    private void fillDeleteByIdDummyData() {
        Account customer = Account.builder().firstName("orderDaoDel").secondName("orderDaoDel")
                .phone("orderDaoDel").email("orderDaoDel")
                .credentials(Credentials.builder().username("orderDaoDel").password("orderDaoDel").build()).build();
        accountDao.save(customer);

        Account customer1 = Account.builder().firstName("orderDaoDel1").secondName("orderDaoDel1")
                .phone("orderDaoDel1").email("orderDaoDel1")
                .credentials(Credentials.builder().username("orderDaoDel1").password("orderDaoDel1").build()).build();
        accountDao.save(customer1);

        Category category = Category.builder().name("orderDaoDel").build();
        categoryDao.save(category);

        Category category1 = Category.builder().name("orderDaoDel1").build();
        categoryDao.save(category1);

        Item jackhammer = Item.builder()
                .category(categoryDao.findById(1).get())
                .name("Excavator").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);
        Item angleGrinder = Item.builder()
                .category(categoryDao.findById(1).get())
                .name("Drilling machine").price(new BigDecimal(600)).quantity(15).build();
        itemDao.save(angleGrinder);

        Item twoBedApp = Item.builder()
                .category(categoryDao.findById(1).get())
                .name("4-bedroom app").price(new BigDecimal(4235)).quantity(2).build();
        itemDao.save(twoBedApp);

        Item lamborghini = Item.builder()
                .category(categoryDao.findById(2).get())
                .name("Porsche").price(new BigDecimal(7200)).quantity(1).build();
        itemDao.save(lamborghini);

        List<Item> items = itemDao.findAll();

        Order order = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderDao.save(order);

        Order order1 = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(13600)).build();
        orderDao.save(order1);

        Order order2 = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(14600)).build();
        orderDao.save(order2);

        Order order3 = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(15600)).build();
        orderDao.save(order3);

        Order order4 = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(16600)).build();
        orderDao.save(order4);

        Order order5 = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(21600)).build();
        orderDao.save(order5);
    }

    @Test
    public void addInvalidDataTest() {
        Order order = Order.builder().customer(Account.builder().id(1).build()).worker(Account.builder().id(2).build())
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(11600)).build();
        Assertions.assertThrows(PersistenceException.class, () -> orderDao.save(order));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(orderDao.findById(50).isPresent());
    }

    @Test
    public void getLazyAssociationsWithoutTransactionalTest() {
        fillGetLazyDummyData();
        Optional<Order> orderOptional = orderDao.findById(1);
        Order order = orderOptional.get();
        List<Item> items = order.getItems();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(items));
    }

    private void fillGetLazyDummyData() {
        Account customer = Account.builder().firstName("ordDaoLazy").secondName("ordDaoLazy")
                .phone("ordDaoLazy").email("ordDaoLazy")
                .credentials(Credentials.builder().username("ordDaoLazy").password("ordDaoLazy").build()).build();
        accountDao.save(customer);

        Account customer1 = Account.builder().firstName("ordDaoLazy1").secondName("ordDaoLazy1")
                .phone("ordDaoLazy1").email("ordDaoLazy1")
                .credentials(Credentials.builder().username("ordDaoLazy1").password("ordDaoLazy1").build()).build();
        accountDao.save(customer1);

        Category category = Category.builder().name("ordDaoLazy").build();
        categoryDao.save(category);

        Item jackhammer = Item.builder()
                .category(categoryDao.findById(1).get())
                .name("Excavator3").price(new BigDecimal(750)).quantity(8).build();
        itemDao.save(jackhammer);

        List<Item> items = itemDao.findAll();

        Order order = Order.builder().customer(Account.builder().id(1).build())
                .worker(Account.builder().id(2).build()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderDao.save(order);
    }
}
