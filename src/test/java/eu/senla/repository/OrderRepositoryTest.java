package eu.senla.repository;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entity.*;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class OrderRepositoryTest extends ContainersEnvironment {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void updateTest() {
        fillUpdateDummyData();
        Optional<Order> orderOptional = orderRepository.findById(1L);
        Order order = orderOptional.get();
        order.setEndDateTime(new Timestamp(1675855790625L));
        Order orderFromDb = orderRepository.save(order);
        Assertions.assertEquals(order.getEndDateTime(), orderFromDb.getEndDateTime());
    }

    private void fillUpdateDummyData() {
        Account customer = Account.builder().firstName("orderDaoUpd").secondName("orderDaoUpd")
                .phone("orderDaoUpd").email("orderDaoUpd")
                .credentials(Credentials.builder().username("orderDaoUpd").password("orderDaoUpd").role(Role.USER)
                        .build())
                .build();
        accountRepository.save(customer);

        Account customer1 = Account.builder().firstName("orderDaoUpd1").secondName("orderDaoUpd1")
                .phone("orderDaoUpd1").email("orderDaoUpd1")
                .credentials(Credentials.builder().username("orderDaoUpd1").password("orderDaoUpd1").role(Role.USER)
                        .build())
                .build();
        accountRepository.save(customer1);

        Category category = Category.builder().name("orderDaoUpd").build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .category(categoryRepository.findById(1L).get())
                .name("Excavator2").price(new BigDecimal(750)).quantity(8).build();
        itemRepository.save(jackhammer);

        List<Item> items = itemRepository.findAll();

        Order order = Order.builder().customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderRepository.save(order);
    }

    @Test
    public void findyByIdTest() {
        fillFindByIdDummyData();
        List<Item> items = itemRepository.findAll();
        Order order = Order.builder().id(1L).customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();

        Optional<Order> orderFromDb = orderRepository.findById(1L);

        Assertions.assertEquals(order.getId(), orderFromDb.get().getId());

    }

    private void fillFindByIdDummyData() {
        Account customer = Account.builder().firstName("orderDaoFind").secondName("orderDaoFind")
                .phone("orderDaoFind").email("orderDaoFind")
                .credentials(Credentials.builder().username("orderDaoFind").password("orderDaoFind").role(Role.USER)
                        .build())
                .build();
        accountRepository.save(customer);

        Account customer1 = Account.builder().firstName("orderDaoFind1").secondName("orderDaoFind1")
                .phone("orderDaoFind1").email("orderDaoFind1")
                .credentials(Credentials.builder().username("orderDaoFind1").password("orderDaoFind1").role(Role.USER)
                        .build())
                .build();
        accountRepository.save(customer1);

        Category category = Category.builder().name("orderDaoFind").build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .category(categoryRepository.findById(1L).get())
                .name("Excavator1").price(new BigDecimal(750)).quantity(8).build();
        itemRepository.save(jackhammer);

        List<Item> items = itemRepository.findAll();

        Order order = Order.builder().customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderRepository.save(order);
    }

    @Test
    @Transactional
    public void deleteByIdTest() {
        fillDeleteByIdDummyData();
        orderRepository.deleteById(6L);
        Assertions.assertFalse(orderRepository.findById(6L).isPresent());
    }

    private void fillDeleteByIdDummyData() {
        Account customer = Account.builder().firstName("orderDaoDel").secondName("orderDaoDel")
                .phone("orderDaoDel").email("orderDaoDel")
                .credentials(Credentials.builder().username("orderDaoDel").password("orderDaoDel").role(Role.USER)
                        .build())
                .build();
        accountRepository.save(customer);

        Account customer1 = Account.builder().firstName("orderDaoDel1").secondName("orderDaoDel1")
                .phone("orderDaoDel1").email("orderDaoDel1")
                .credentials(Credentials.builder().username("orderDaoDel1").password("orderDaoDel1").role(Role.USER)
                        .build())
                .build();
        accountRepository.save(customer1);

        Category category = Category.builder().name("orderDaoDel").build();
        categoryRepository.save(category);

        Category category1 = Category.builder().name("orderDaoDel1").build();
        categoryRepository.save(category1);

        Item jackhammer = Item.builder()
                .category(categoryRepository.findById(1L).get())
                .name("Excavator").price(new BigDecimal(750)).quantity(8).build();
        itemRepository.save(jackhammer);
        Item angleGrinder = Item.builder()
                .category(categoryRepository.findById(1L).get())
                .name("Drilling machine").price(new BigDecimal(600)).quantity(15).build();
        itemRepository.save(angleGrinder);

        Item twoBedApp = Item.builder()
                .category(categoryRepository.findById(1L).get())
                .name("4-bedroom app").price(new BigDecimal(4235)).quantity(2).build();
        itemRepository.save(twoBedApp);

        Item lamborghini = Item.builder()
                .category(categoryRepository.findById(2L).get())
                .name("Porsche").price(new BigDecimal(7200)).quantity(1).build();
        itemRepository.save(lamborghini);

        List<Item> items = itemRepository.findAll();

        Order order = Order.builder().customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderRepository.save(order);

        Order order1 = Order.builder().customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(13600)).build();
        orderRepository.save(order1);

        Order order2 = Order.builder().customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(14600)).build();
        orderRepository.save(order2);

        Order order3 = Order.builder().customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(15600)).build();
        orderRepository.save(order3);

        Order order4 = Order.builder().customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(16600)).build();
        orderRepository.save(order4);

        Order order5 = Order.builder().customer(accountRepository.findById(1L).get())
                .worker(accountRepository.findById(2L).get()).items(items).startDateTime(new Timestamp(1665733114323L))
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(21600)).build();
        orderRepository.save(order5);
    }

    @Test
    public void addInvalidDataTest() {
        Order order = Order.builder().customer(Account.builder().id(1L).build()).worker(Account.builder().id(2L).build())
                .endDateTime(new Timestamp(1675278114323L)).totalPrice(new BigDecimal(11600)).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> orderRepository.save(order));
    }

    @Test
    public void findByInvalidIdTest() {
        Assertions.assertFalse(orderRepository.findById(50L).isPresent());
    }

    @Test
    public void getLazyAssociationsWithoutTransactionalTest() {
        fillGetLazyDummyData();
        Optional<Order> orderOptional = orderRepository.findById(1L);
        Order order = orderOptional.get();
        List<Item> items = order.getItems();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(items));
    }

    private void fillGetLazyDummyData() {
        Account customer = Account.builder().firstName("ordDaoLazy").secondName("ordDaoLazy")
                .phone("ordDaoLazy").email("ordDaoLazy")
                .credentials(Credentials.builder().username("ordDaoLazy").password("ordDaoLazy").role(Role.USER)
                        .build())
                .build();
        accountRepository.save(customer);

        Account customer1 = Account.builder().firstName("ordDaoLazy1").secondName("ordDaoLazy1")
                .phone("ordDaoLazy1").email("ordDaoLazy1")
                .credentials(Credentials.builder().username("ordDaoLazy1").password("ordDaoLazy1").role(Role.USER)
                        .build())
                .build();
        accountRepository.save(customer1);

        Category category = Category.builder().name("ordDaoLazy").build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .category(categoryRepository.findById(1L).get())
                .name("Excavator3").price(new BigDecimal(750)).quantity(8).build();
        itemRepository.save(jackhammer);

        List<Item> items = itemRepository.findAll();

        Account account = accountRepository.findById(1L).get();
        Account account2 = accountRepository.findById(2L).get();
        Order order = Order.builder().customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build()).items(items).startDateTime(new Timestamp(1665778114323L))
                .endDateTime(new Timestamp(1675778114323L)).totalPrice(new BigDecimal(12200)).build();
        orderRepository.save(order);
        System.out.println("xttrrr");
    }
}
