package eu.senla.controller;

import eu.senla.PostgresTestContainer;
import eu.senla.RentalApplication;
import eu.senla.entity.*;
import eu.senla.repository.AccountRepository;
import eu.senla.repository.CategoryRepository;
import eu.senla.repository.ItemRepository;
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
public class ItemControllerTest {
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
    @Autowired
    private ItemRepository itemRepository;
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
    public void getItemByIdTest() throws Exception {
        fillGetItemByIdTestDummyData();
        this.mockMvc.perform(get("/items/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillGetItemByIdTestDummyData() {
        categoryRepository.save(Category.builder().name("itemctrlgetByIdtest").build());

        itemRepository.save(Item.builder()
                .name("itemctrlgetByIdtest")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());
    }

    @Test
    @WithUserDetails("Admin")
    public void createItemTest() throws Exception {
        categoryRepository.save(Category.builder().name("itemctrlcreatetest").build());

        String requestBody = "{\"category\":{\"id\":\"1\"},\"name\":\"createItemdata\"," +
                "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}";

        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("User2")
    public void createItemUnauthorizedTest() throws Exception {
        String requestBody = "{\"category\":{\"id\":\"1\"},\"name\":\"createItemdata\",\"price\":\"1\",\"quantity\":\"1\"}";
        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidItemTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateItemTest() throws Exception {
        fillUpdateItemTestDummyData();
        String requestBody = "{\"id\":\"1\",\"category\":{\"id\":\"1\",\"name\":\"cataup\",\"discount\":\"0\"}," +
                "\"name\":\"updateItemData\",\"price\":\"12\",\"quantity\":\"1\",\"discount\":\"30\"}";
        this.mockMvc.perform(put("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillUpdateItemTestDummyData() {
        categoryRepository.save(Category.builder().name("itemctrlUpdtest").build());

        itemRepository.save(Item.builder()
                .name("itemctrlUpdtest")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());
    }

    @Test
    @WithUserDetails("User2")
    public void updateItemUnauthorizedTest() throws Exception {
        String requestBody = "{\"id\":\"1\",\"category\":{\"id\":\"1\",\"name\":\"cataup\"}" +
                ",\"name\":\"updateItemData\",\"price\":\"12\",\"quantity\":\"1\"}";
        this.mockMvc.perform(put("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithUserDetails("Admin")
    public void updateNonexistentItemTest() throws Exception {
        String requestBody = "{\"category\":{\"id\":\"1000\"}," +
                "\"name\":\"updateInvalidData\",\"price\":\"1\",\"quantity\":\"1\"}";
        this.mockMvc.perform(put("/items/{id}", 1000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteItemByIdTest() throws Exception {
        fillDeleteItemByIdTestDummyData();

        Long id = itemRepository.findByName("itemctrldelByIdtest3").get().getId();
        mockMvc.perform(delete("/items/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteItemByIdTestDummyData() {
        categoryRepository.save(Category.builder().name("itemctrldelByIdtest").build());

        itemRepository.save(Item.builder()
                .name("itemctrldelByIdtest1")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        itemRepository.save(Item.builder()
                .name("itemctrldelByIdtest2")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        itemRepository.save(Item.builder()
                .name("itemctrldelByIdtest3")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        itemRepository.save(Item.builder()
                .name("itemctrldelByIdtest4")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());
    }

    @Test
    @WithUserDetails("User3")
    public void deleteItemByIdUnauthorizedTest() throws Exception {
        mockMvc.perform(delete("/items/{id}", 4))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithUserDetails("Admin")
    public void deleteNonexistentItemByIdTest() throws Exception {
        mockMvc.perform(delete("/items/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void getAllItemsTest() throws Exception {
        fillGetAllItemsTestDummyData();

        mockMvc.perform(get("/items"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }


    private void fillGetAllItemsTestDummyData() {
        categoryRepository.save(Category.builder().name("itemctrlgetalltest").build());

        itemRepository.save(Item.builder()
                .name("itemctrlgetalltest")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());
    }

    @Test
    @WithUserDetails("User2")
    public void getItemsWithFiltersTest() throws Exception {
        fillgetItemsWithFiltersTestDummyData();
        mockMvc.perform(get("/items/fltr?nameLike=itemctrl%&discountMoreThan=30"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(1))));
    }

    private void fillgetItemsWithFiltersTestDummyData() {
        categoryRepository.save(Category.builder().name("itemctrlgetallfilterstest").build());

        itemRepository.save(Item.builder()
                .name("itemctrlgetallfilterstest1")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        itemRepository.save(Item.builder()
                .name("itemctrlgetallfilterstest2")
                .price(new BigDecimal(300))
                .discount(new BigDecimal(50))
                .category(Category.builder().id(1L).build())
                .build());

        itemRepository.save(Item.builder()
                .name("itemctrlgetallfilterstest3")
                .price(new BigDecimal(300))
                .discount(new BigDecimal(50))
                .category(Category.builder().id(1L).build())
                .build());
    }

    @Test
    @WithUserDetails("User2")
    public void getItemsWithInvalidFiltersTest() throws Exception {
        mockMvc.perform(get("/items/fltr?firstNamee=pet&discountLessThan=10"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

//    @Test
//    @WithUserDetails("Admin")
//    public void replenishItemTest() throws Exception {
//        String requestBody = "{\"quantity\":\"1000\"}";
//        mockMvc.perform(put("/items/{id}/replenish", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }

//    @Test
//    @WithUserDetails("User2")
//    public void replenishItemUnauthorizedTest() throws Exception {
//        String requestBody = "{\"quantity\":\"1000\"}";
//        mockMvc.perform(put("/items/{id}/replenish", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isForbidden());
//    }

//    @Test
//    @WithUserDetails("Admin")
//    public void replenishItemInvalidQuantityTest() throws Exception {
//        String requestBody = "{\"quantity\":\"0\"}";
//        mockMvc.perform(put("/items/{id}/replenish", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }

    @Test
    @WithUserDetails("Admin")
    public void getItemsByPopularityTest() throws Exception {
        mockMvc.perform(get("/items/popularity"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
