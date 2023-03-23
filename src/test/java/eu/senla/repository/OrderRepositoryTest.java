package eu.senla.repository;

import eu.senla.PostgresTestContainer;
import eu.senla.RentalApplication;
import eu.senla.entity.*;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {RentalApplication.class})
@Testcontainers
public class OrderRepositoryTest {
    @Container
    public static PostgreSQLContainer container = PostgresTestContainer.getInstance()
            .withUsername("Sleepwalker")
            .withPassword("password")
            .withDatabaseName("TestBd");
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }


    @PostConstruct
    public void SaveDummyAuthorizationData() {
        fillDummyData();
    }


    private void fillDummyData() {
        if (accountRepository.findByEmail("RepoAllId@mail.ru").isEmpty()) {
            Account admin = Account.builder()
                    .firstName("RepoAllId")
                    .secondName("RepoAllId")
                    .phone("+37583232734")
                    .email("RepoAllId@mail.ru")
                    .discount(new BigDecimal(25))
                    .balance(new BigDecimal(999999))
                    .credentials(Credentials.builder()
                            .username("RepoAllId")
                            .password("RepoAllId")
                            .role(Role.ADMIN)
                            .build())
                    .build();
            accountRepository.save(admin);
        }
        if (categoryRepository.findByName("orderDao").isEmpty()) {
            Category category = Category.builder()
                    .name("orderDao")
                    .build();
            categoryRepository.save(category);
        }

        if (itemRepository.findByName("Excavator2").isEmpty()) {
            Item jackhammer = Item.builder()
                    .name("Excavator2")
                    .price(new BigDecimal(750))
                    .quantity(8)
                    .category(categoryRepository.findByName("orderDao").get())
                    .build();
            itemRepository.save(jackhammer);
        }
    }

    @Test
    @Transactional
    public void updateTest() {
        fillUpdateDummyData();
        Optional<Order> orderOptional = orderRepository.findById(1L);
        Order order = orderOptional.get();
        order.setEndDateTime(LocalDateTime.of(2020, 12, 12, 1, 2));
        Order orderFromDb = orderRepository.save(order);
        Assertions.assertEquals(order.getEndDateTime(), orderFromDb.getEndDateTime());
    }

    private void fillUpdateDummyData() {
        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .worker(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
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
        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .worker(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .items(items)
                .startDateTime(LocalDateTime.of(2018, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2021, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order);
    }

    @Test
    public void createInvalidDataTest() {
        Order order = Order.builder()
                .customer(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .worker(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(11600))
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> orderRepository.save(order));
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
        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .worker(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .items(items)
                .startDateTime(LocalDateTime.of(2020, 12, 2, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order);
    }

    @Test
    public void getAllByCustomerIdTest() {
        fillGetAllByCustomerIdTestDummyData();
        Pageable paging = PageRequest.of(0, 2);
        Long id = accountRepository.findByEmail("RepoAllId@mail.ru").get().getId();
        Page<Order> orderPage = orderRepository.getAllByCustomer_Id(id, paging);

        Assertions.assertFalse(orderPage.isEmpty());
    }

    private void fillGetAllByCustomerIdTestDummyData() {
        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .worker(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .items(items)
                .startDateTime(LocalDateTime.of(2020, 12, 2, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order);

        Order order2 = Order.builder()
                .customer(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .worker(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .items(items)
                .startDateTime(LocalDateTime.of(2020, 12, 2, 1, 2))
                .endDateTime(LocalDateTime.of(2021, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order2);

        Order order3 = Order.builder()
                .customer(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .worker(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .items(items)
                .startDateTime(LocalDateTime.of(2020, 12, 2, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order3);
    }


    @Test
    public void getEagerOrderTest() {
        fillGetEagerOrderTestDummyData();
        Order eagerOrder = orderRepository.findOrderById(1L);
        Assertions.assertDoesNotThrow(() -> System.out.println(eagerOrder.getItems()));
    }

    private void fillGetEagerOrderTestDummyData() {
        List<Item> items = itemRepository.findAll();

        Order order = Order.builder()
                .customer(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .worker(accountRepository.findByEmail("RepoAllId@mail.ru").get())
                .items(items)
                .startDateTime(LocalDateTime.of(2020, 12, 2, 1, 2))
                .endDateTime(LocalDateTime.of(2020, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build();
        orderRepository.save(order);
    }
}
