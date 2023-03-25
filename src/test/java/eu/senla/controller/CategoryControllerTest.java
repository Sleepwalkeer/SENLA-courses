package eu.senla.controller;

import eu.senla.PostgresTestContainer;
import eu.senla.RentalApplication;
import eu.senla.entity.Account;
import eu.senla.entity.Category;
import eu.senla.entity.Credentials;
import eu.senla.entity.Role;
import eu.senla.repository.AccountRepository;
import eu.senla.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {RentalApplication.class})
@AutoConfigureMockMvc
@Testcontainers
public class CategoryControllerTest {
    @Container
    public static PostgreSQLContainer container = PostgresTestContainer.getInstance()
            .withUsername("Sleepwalker")
            .withPassword("password")
            .withDatabaseName("TestBd");
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @PostConstruct
    public void SaveDummyAuthorizationData() {
        fillDummyAuthorizationData();
    }

    public void fillDummyAuthorizationData() {
        if (accountRepository.findByEmail("Admin@mail.ru").isEmpty()) {
            Account admin = Account.builder()
                    .firstName("Admin")
                    .secondName("Admin")
                    .phone("+3758232734")
                    .email("Admin@mail.ru")
                    .discount(new BigDecimal(25))
                    .balance(new BigDecimal(999999))
                    .credentials(Credentials.builder()
                            .username("Admin")
                            .password("escapism")
                            .role(Role.ADMIN)
                            .build())
                    .build();
            accountRepository.save(admin);
        }
        if (accountRepository.findByEmail("User2@mail.ru").isEmpty()) {
            Account user2 = Account.builder()
                    .firstName("User2")
                    .secondName("user2")
                    .phone("+375334323274")
                    .email("User2@mail.ru")
                    .credentials(Credentials.builder()
                            .username("User2")
                            .password("escapism2")
                            .build())
                    .build();
            accountRepository.save(user2);
        }
        if (accountRepository.findByEmail("User3@mail.ru").isEmpty()) {
            Account user3 = Account.builder()
                    .firstName("User3")
                    .secondName("user3")
                    .phone("+375293618345")
                    .email("User3@mail.ru")
                    .credentials(Credentials.builder()
                            .username("User3")
                            .password("escapism3")
                            .build())
                    .build();
            accountRepository.save(user3);
        }
    }

    @Test
    @WithUserDetails("Admin")
    public void createCategoryTest() throws Exception {
        String requestBody = "{\"name\": \"create\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("User2")
    public void createCategoryUnauthorizedTest() throws Exception {
        String requestBody = "{\"name\": \"create\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void getCategoryByIdTest() throws Exception {
        categoryRepository.save(Category.builder().name("ctgrctrlgetByIdtest").build());

        this.mockMvc.perform(get("/categories/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidCategoryTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateCategoryTest() throws Exception {
        categoryRepository.save(Category.builder().name("ctgrctrlUpdtest").build());

        String requestBody = "{\"id\":\"1\" ,\"name\":\"Apartments\",\"discount\":\"0\"}";

        this.mockMvc.perform(put("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("User3")
    public void updateCategoryUnauthorizedTest() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"Apartments\",\"discount\":\"0\"}";

        this.mockMvc.perform(put("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateNonexistentCategoryTest() throws Exception {
        String updateRequestBody = "{\"id\":7000,\"name\":\"Electronics\",\"discount\":\"0\"}";

        this.mockMvc.perform(put("/categories/{id}", 7000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteCategoryByIdTest() throws Exception {
        fillDeleteCategoryByIdDummyData();
        Long id = categoryRepository.findByName("ctgrctrldelByIdtest4").get().getId();
        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteCategoryByIdDummyData() throws Exception {
        categoryRepository.save(Category.builder().name("ctgrctrldelByIdtest1").build());
        categoryRepository.save(Category.builder().name("ctgrctrldelByIdtest2").build());
        categoryRepository.save(Category.builder().name("ctgrctrldelByIdtest3").build());
        categoryRepository.save(Category.builder().name("ctgrctrldelByIdtest4").build());
        categoryRepository.save(Category.builder().name("ctgrctrldelByIdtest5").build());
        categoryRepository.save(Category.builder().name("ctgrctrldelByIdtest6").build());
    }

    @Test
    @WithUserDetails("User3")
    public void deleteCategoryByIdUnauthorizedTest() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 7))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithUserDetails("Admin")
    public void deleteNonexistentCategoryByIdTest() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void getAllCategoriesTest() throws Exception {
        categoryRepository.save(Category.builder().name("ctgctrlgetalltest").build());

        mockMvc.perform(get("/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }


    @Test
    @WithUserDetails("User2")
    public void getCategoriesWithFiltersTest() throws Exception {
        categoryRepository.save(Category.builder().name("ctgctrlgetFilterstest").discount(new BigDecimal(20)).build());
        mockMvc.perform(get("/categories/fltr?nameLike=ctg%&discountMoreThan=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @WithUserDetails("User2")
    public void getCategoriesWithInvalidFiltersTest() throws Exception {
        mockMvc.perform(get("/categories/fltr?firstNamee=pet&discountLessThan=10"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}


