package eu.senla.repository;

import eu.senla.PostgresTestContainer;
import eu.senla.RentalApplication;
import eu.senla.entity.Account;
import eu.senla.entity.Category;
import eu.senla.entity.Credentials;
import eu.senla.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {RentalApplication.class})
@Testcontainers
public class CategoryRepositoryTest {
    @Container
    public static PostgreSQLContainer container = PostgresTestContainer.getInstance()
            .withUsername("Sleepwalker")
            .withPassword("password")
            .withDatabaseName("TestBd");
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
        Category dummyData = Category.builder()
                .name("catDaoFindById")
                .build();
        categoryRepository.save(dummyData);

        Long id = categoryRepository.findByName("catDaoFindById").get().getId();
        Optional<Category> categoryFromDb = categoryRepository.findById(id);
        Assertions.assertEquals(id, categoryFromDb.get().getId());
    }

    @Test
    public void updateTest() {
        Category category = Category.builder()
                .name("ctgupd")
                .build();
        categoryRepository.save(category);

        Category category2 = Category.builder()
                .id(categoryRepository.findByName("ctgupd").get().getId())
                .name("ctgupdnew")
                .build();
        Category categoryFromDb = categoryRepository.save(category2);

        Assertions.assertEquals(category2.getName(), categoryFromDb.getName());
    }

    @Test
    public void updateInvalidTest() {
        Category category = Category.builder()
                .id(1L)
                .name("ctgupdinvld")
                .build();
        categoryRepository.save(category);

        Category category2 = Category.builder()
                .id(1L)
                .name("test")
                .discount(new BigDecimal(-50))
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> categoryRepository.save(category2));
    }

    @Test
    public void CreateTest() {
        Category category = Category.builder().name("crtTst").build();
        Category categoryFromDb = categoryRepository.save(category);
        Assertions.assertEquals(category.getName(), categoryFromDb.getName());

    }

    @Test
    public void CreateInvalidTest() {
        Category category = Category.builder().discount(new BigDecimal(-50)).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(category));
    }

    @Test
    public void findByNameTest() {
        Category category = Category.builder().name("fndByNameTest").build();
        Category categoryFromDb = categoryRepository.save(category);
        Category findByNameCategory = categoryRepository.findByName("fndByNameTest").get();
        Assertions.assertEquals(categoryFromDb.getName(), findByNameCategory.getName());
    }

}