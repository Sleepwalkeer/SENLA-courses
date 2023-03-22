package eu.senla.controller;

import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ContextConfigurationTest;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entity.*;
import eu.senla.repository.AccountRepository;
import eu.senla.repository.CategoryRepository;
import eu.senla.repository.ItemRepository;
import eu.senla.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ContextConfigurationTest.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class OrderControllerTest extends ContainersEnvironment {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderRepository orderRepository;
    private MockMvc mockMvc;

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
    public void getOrderByIdTest() throws Exception {
        fillGetOrderByIdDummyData();
        this.mockMvc.perform(get("/orders/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillGetOrderByIdDummyData() {
        categoryRepository.save(Category.builder().name("orderctrlgetbyidtest").build());

        itemRepository.save(Item.builder()
                .name("orderctrlgetbyidtest1")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        List<Item> items = itemRepository.findAll();

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());
    }

    @Test
    @WithUserDetails("User2")
    public void getOrderByIdUnauthorizedTest() throws Exception {
        this.mockMvc.perform(get("/orders/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithUserDetails("Admin")
    public void createOrderTest() throws Exception {
        fillcreateOrderTest();
        Long itemId1 = itemRepository.findByName("orderctrlcreatetest1").get().getId();
        Long itemId2 = itemRepository.findByName("orderctrlcreatetest2").get().getId();
        Long itemId3 = itemRepository.findByName("orderctrlcreatetest3").get().getId();
        String requestBody = "{\"customer\":{\"id\":\"1\"},\"worker\":{\"id\":\"1\"}," +
                "\"items\":[{\"id\":\"" + itemId1 + "\"}, {\"id\":\"" + itemId2 + "\"}, {\"id\":\"" + itemId3 + "\"}]," +
                "\"startDateTime\":[2023,3,12,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value("5350.0"));
    }


    private void fillcreateOrderTest() {
        categoryRepository.save(Category.builder().name("orderctrlcreatetest").build());
        categoryRepository.save(Category.builder().name("orderctrlcreatetest1").discount(new BigDecimal(30)).build());
        Long id = categoryRepository.findByName("orderctrlcreatetest1").get().getId();
        Long id2 = categoryRepository.findByName("orderctrlcreatetest").get().getId();
        itemRepository.save(Item.builder()
                .name("orderctrlcreatetest1")
                .price(new BigDecimal(500))
                .quantity(10)
                .discount(new BigDecimal(50))
                .category(Category.builder().id(1L).build())
                .build());

        itemRepository.save(Item.builder()
                .name("orderctrlcreatetest2")
                .price(new BigDecimal(300))
                .quantity(10)
                .category(Category.builder().id(id).build())
                .build());

        itemRepository.save(Item.builder()
                .name("orderctrlcreatetest3")
                .price(new BigDecimal(100))
                .quantity(10)
                .category(Category.builder().id(id2).build())
                .build());
    }


    @Test
    @WithUserDetails("Admin")
    public void createOrderInsufficientFundsTest() throws Exception {
        fillcreateOrderInsufficientFundsTest();
        Long itemId1 = itemRepository.findByName("orderctrlcreatebalancetest1").get().getId();
        Long itemId2 = itemRepository.findByName("orderctrlcreatebalancetest2").get().getId();
        Long itemId3 = itemRepository.findByName("orderctrlcreatebalancetest3").get().getId();
        String requestBody = "{\"customer\":{\"id\":\"2\"},\"worker\":{\"id\":\"1\"}," +
                "\"items\":[{\"id\":\"" + itemId1 + "\"}, {\"id\":\"" + itemId2 + "\"}, {\"id\":\"" + itemId3 + "\"}]," +
                "\"startDateTime\":[2023,3,12,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private void fillcreateOrderInsufficientFundsTest() {
        categoryRepository.save(Category.builder().name("orderctrlcreatebalancetest").build());
        categoryRepository.save(Category.builder().name("orderctrlcreatebalancetest1").discount(new BigDecimal(30)).build());
        Long id = categoryRepository.findByName("orderctrlcreatetest1").get().getId();
        itemRepository.save(Item.builder()
                .name("orderctrlcreatebalancetest1")
                .price(new BigDecimal(500))
                .quantity(10)
                .discount(new BigDecimal(50))
                .category(Category.builder().id(1L).build())
                .build());

        itemRepository.save(Item.builder()
                .name("orderctrlcreatebalancetest2")
                .price(new BigDecimal(300))
                .quantity(10)
                .category(Category.builder().id(id).build())
                .build());

        itemRepository.save(Item.builder()
                .name("orderctrlcreatebalancetest3")
                .price(new BigDecimal(100))
                .quantity(10)
                .category(Category.builder().id(id).build())
                .build());
    }


    @Test
    @WithUserDetails("User2")
    public void createOrderUnauthorizedTest() throws Exception {
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidOrderTest() throws Exception {
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidTimeOrderTest() throws Exception {
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":[2024,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateOrderTest() throws Exception {
        fillUpdateOrderDummyData();
        String requestBody = "{\"id\": 1,\"customer\":{\"id\":\"1\"}" +
                ",\"worker\":{\"id\":\"1\"}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(put("/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillUpdateOrderDummyData() {
        categoryRepository.save(Category.builder().name("orderctrlupdtest").build());

        itemRepository.save(Item.builder()
                .name("orderctrlupdtest1")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        List<Item> items = itemRepository.findAll();

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());
    }

    @Test
    @WithUserDetails("User3")
    public void updateOrderUnauthorizedTest() throws Exception {
        String requestBody = "{\"id\": 1,\"customer\":{\"id\":\"1\"}" +
                ",\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(put("/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateInvalidOrderTest() throws Exception {
        String requestBody = "{\"id\": 25,\"customer\":{\"id\":1},\"worker\":{\"id\":2}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(put("/orders/{id}", 25)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteOrderByIdAdminTest() throws Exception {
        fillDeleteOrderByIdDummyData();
        mockMvc.perform(delete("/orders/{id}", 4))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteOrderByIdDummyData() {
        categoryRepository.save(Category.builder().name("orderctrldelbyIdtest").build());

        itemRepository.save(Item.builder()
                .name("orderctrldelbyIdtest1")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        List<Item> items = itemRepository.findAll();

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());
    }

    @Test
    @WithUserDetails("User3")
    public void deleteOrderByIdUnauthorizedTest() throws Exception {
        mockMvc.perform(delete("/orders/{id}", 5))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteNonexistentOrderByTest() throws Exception {
        mockMvc.perform(delete("/orders/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void getAllOrdersTest() throws Exception {
        fillGetAllOrderDummyData();
        mockMvc.perform(get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @WithUserDetails("User2")
    public void getAllOrdersUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private void fillGetAllOrderDummyData() {
        categoryRepository.save(Category.builder().name("orderctrlGetAlltest").build());

        itemRepository.save(Item.builder()
                .name("orderctrlGetAlltest")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        List<Item> items = itemRepository.findAll();

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2023, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());
    }

    @Test
    @WithUserDetails("Admin")
    public void getOrdersWithFiltersTest() throws Exception {
        fillgetOrdersWithFiltersTestDummyData();
        mockMvc.perform(get("/orders/fltr?startDateEarlierThan=2025-03-20T13:45:30" +
                        "&endDateEarlierThan=2025-03-19T13:45:30&moreItemsThan=0&totalLessThan=1300000"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(1))));
    }

    private void fillgetOrdersWithFiltersTestDummyData() {
        categoryRepository.save(Category.builder().name("orderctrlgetwithfltrtest").build());

        itemRepository.save(Item.builder()
                .name("orderctrlgetwithfltrtest1")
                .price(new BigDecimal(300))
                .category(Category.builder().id(1L).build())
                .build());

        List<Item> items = itemRepository.findAll();

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());

        orderRepository.save(Order.builder()
                .customer(Account.builder().id(1L).build())
                .worker(Account.builder().id(2L).build())
                .items(items)
                .startDateTime(LocalDateTime.of(2019, 12, 12, 1, 2))
                .endDateTime(LocalDateTime.of(2022, 12, 12, 1, 2))
                .totalPrice(new BigDecimal(12200))
                .build());
    }

    @Test
    @WithUserDetails("Admin")
    public void getOrdersWithInvalidFiltersTest() throws Exception {
        mockMvc.perform(get("/orders/fltr?firstNamee=pet&discountLessThan=10"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}