package eu.senla.repository;

import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ContextConfigurationTest;
import eu.senla.entity.Account;
import eu.senla.entity.Category;
import eu.senla.entity.Item;
import eu.senla.entity.Order;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ContextConfigurationTest.class})
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
    @Transactional
    public void updateTest() {
        fillUpdateDummyData();
        Optional<Order> orderOptional = orderRepository.findById(1L);
        Order order = orderOptional.get();
        order.setEndDateTime(LocalDateTime.of(2020,12,12,1,2));
        Order orderFromDb = orderRepository.save(order);
        Assertions.assertEquals(order.getEndDateTime(), orderFromDb.getEndDateTime());
    }

    private void fillUpdateDummyData() {
        Category category = Category.builder()
                .name("orderDaoUpd")
                .build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .name("Excavator2")
                .price(new BigDecimal(750))
                .quantity(8)
                .category(Category.builder().id(1L).build())
                .build();
        itemRepository.save(jackhammer);

        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019,12,12,1,2))
                .endDateTime(LocalDateTime.of(2022,12,12,1,2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order);
    }

    @Test
    public void findyByIdTest() {
        fillFindByIdDummyData();
        Optional<Order> orderFromDb = orderRepository.findById(1L);

        Assertions.assertEquals(1L, orderFromDb.get().getId());

    }

    private void fillFindByIdDummyData() {
        Category category = Category.builder()
                .name("orderDaoFind")
                .build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .category(Category.builder().id(1L).build())
                .name("Excavator1")
                .price(new BigDecimal(750))
                .quantity(8)
                .build();
        itemRepository.save(jackhammer);

        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2018,12,12,1,2))
                .endDateTime(LocalDateTime.of(2021,12,12,1,2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order);
    }

    @Test
    @Transactional
    public void deleteByIdTest() {
        fillDeleteByIdDummyData();
        orderRepository.deleteById(3L);
        Assertions.assertFalse(orderRepository.findById(3L).isPresent());
    }

    private void fillDeleteByIdDummyData() {
        Category category = Category.builder().name("orderDaoDel").build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .name("Excavator1")
                .price(new BigDecimal(750))
                .quantity(8)
                .category(Category.builder().id(1L).build())
                .build();

        itemRepository.save(jackhammer);
        Item angleGrinder = Item.builder()
                .name("Drilling machine")
                .price(new BigDecimal(600))
                .quantity(15)
                .category(Category.builder().id(1L).build())
                .build();
        itemRepository.save(angleGrinder);

        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2017,12,12,1,2))
                .endDateTime(LocalDateTime.of(2020,12,12,1,2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order);

        Order order1 = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2020,10,12,1,2))
                .endDateTime(LocalDateTime.of(2020,12,12,1,2))
                .totalPrice(new BigDecimal(13600))
                .build();
        orderRepository.save(order1);

        Order order2 = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2020,9,12,1,2))
                .endDateTime(LocalDateTime.of(2020,12,12,1,2))
                .totalPrice(new BigDecimal(14600))
                .build();
        orderRepository.save(order2);
    }

    @Test
    public void addInvalidDataTest() {
        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .endDateTime(LocalDateTime.of(2020,12,12,1,2))
                .totalPrice(new BigDecimal(11600))
                .build();
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
        Category category = Category.builder().name("ordDaoLazy").build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .category(Category.builder().id(1L).build())
                .name("Excavator3")
                .price(new BigDecimal(750))
                .quantity(8)
                .build();
        itemRepository.save(jackhammer);

        Item excavator = Item.builder()
                .category(Category.builder().id(1L).build())
                .name("Excavator")
                .price(new BigDecimal(750))
                .quantity(8)
                .build();
        itemRepository.save(excavator);

        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2020,12,2,1,2))
                .endDateTime(LocalDateTime.of(2020,12,12,1,2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order);
    }
}
