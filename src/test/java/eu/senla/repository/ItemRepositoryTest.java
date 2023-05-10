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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {RentalApplication.class})
@Testcontainers
public class ItemRepositoryTest {
    @Container
    public static PostgreSQLContainer container = PostgresTestContainer.getInstance()
            .withUsername("Sleepwalker")
            .withPassword("password")
            .withDatabaseName("TestBd");
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    AccountRepository accountRepository;

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
    }

    @Test
    public void findyByIdTest() {
        fillFindByIdItemDummyData();
        Optional<Item> itemFromDb = itemRepository.findById(1L);
        Assertions.assertEquals(1L, itemFromDb.get().getId());
    }

    private void fillFindByIdItemDummyData() {
        Category category = Category.builder()
                .name("findItemDao")
                .build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .name("findItemDao")
                .price(new BigDecimal(750))
                //.quantity(8)
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(jackhammer);
    }

    @Test
    public void updateTest() {
        fillUpdateItemDummyData();
        Optional<Item> itemOptional = itemRepository.findById(1L);
        Item item = itemOptional.get();

        item.setName("Ferrari");
        Item itemFromDb = itemRepository.save(item);
        Assertions.assertEquals(item.getName(), itemFromDb.getName());
    }

    private void fillUpdateItemDummyData() {
        Category category = Category.builder()
                .name("updItemDao")
                .build();
        categoryRepository.save(category);

        Item jackhammer = Item.builder()
                .name("updItemDao")
                .price(new BigDecimal(750))
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(jackhammer);
    }

    @Test
    public void deleteByIdTest() {
        fillDeleteItemByIdDummyData();
        Long id = itemRepository.findByName("itemDelById15").get().getId();
        itemRepository.deleteById(id);
        Assertions.assertTrue(itemRepository.findById(id).get().isDeleted());
    }

    private void fillDeleteItemByIdDummyData() {
        Category category = Category.builder()
                .name("deleteByIdDaoItem")
                .build();
        categoryRepository.save(category);

        Item item = Item.builder()
                .name("itemDelById15")
                .price(new BigDecimal(345032))
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item);
    }

    @Test
    public void createInvalidTest() {
        Item item = Item.builder()
                .price(new BigDecimal(3450))
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    public void getLazyAssociationsWithoutTransactionalTest() {
        fillLazyAssociationsTestDummyData();

        Long id = itemRepository.findByName("LazyAssoc").get().getId();

        Category category = itemRepository.findById(id).get().getCategory();
        Assertions.assertThrows(LazyInitializationException.class, () -> System.out.println(category));
    }

    private void fillLazyAssociationsTestDummyData() {
        Category category = Category.builder()
                .name("LazyAssoc")
                .build();
        categoryRepository.save(category);

        Item item = Item.builder()
                .name("LazyAssoc")
                .price(new BigDecimal(3450))
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item);
    }

    @Test
    public void findByIdInTest() {
        findByIdInTestDummyData();
        Item item1 = itemRepository.findByName("findByIdIn").get();
        Item item2 = itemRepository.findByName("findByIdIn2").get();

        List<Long> ids = List.of(item1.getId(), item2.getId());
        List<Item> items = itemRepository.findByIdIn(ids);
        Assertions.assertEquals(items.get(0).getName(), item1.getName());
        Assertions.assertEquals(items.get(1).getName(), item2.getName());
    }

    private void findByIdInTestDummyData() {
        Category category = Category.builder()
                .name("findByIdIn")
                .build();
        categoryRepository.save(category);

        Item item = Item.builder()
                .name("findByIdIn")
                .price(new BigDecimal(3450))
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item);

        Item item2 = Item.builder()
                .name("findByIdIn2")
                .price(new BigDecimal(3450))
                .category(Category.builder()
                        .id(1L)
                        .build())
                .build();
        itemRepository.save(item2);
    }

}
