package eu.senla.controller;

import eu.senla.PostgresTestContainer;
import eu.senla.RentalApplication;
import eu.senla.entity.Account;
import eu.senla.entity.Credentials;
import eu.senla.entity.Role;
import eu.senla.repository.AccountRepository;
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
public class AccountControllerTest {
    @Container
    public static PostgreSQLContainer container = PostgresTestContainer.getInstance()
            .withUsername("Sleepwalker")
            .withPassword("password")
            .withDatabaseName("TestBd");
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountRepository accountRepository;
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
        if (accountRepository.findByEmail("updaccAuth1@mail.ru").isEmpty()) {
            Account updateAccAuth = Account.builder()
                    .firstName("updaccUser1")
                    .secondName("updaccAuth1")
                    .phone("updaccAuth1")
                    .email("updaccAuth1@mail.ru")
                    .credentials(Credentials.builder()
                            .username("updaccUser1")
                            .password("updaccAuth1")
                            .build())
                    .build();
            accountRepository.save(updateAccAuth);
        }
        if (accountRepository.findByEmail("delaccAuth1").isEmpty()) {
            Account dellAccAuth = Account.builder()
                    .firstName("delaccAuth1")
                    .secondName("delaccAuth1")
                    .phone("delaccAuth1")
                    .email("delaccAuth1")
                    .credentials(Credentials.builder()
                            .username("delaccUser")
                            .password("delaccAuth1")
                            .build())
                    .build();
            accountRepository.save(dellAccAuth);
        }
    }

    @Test
    @WithUserDetails("Admin")
    public void getAccountByIdAdminTest() throws Exception {
        this.mockMvc.perform(get("/accounts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void getOrdersByAccountIdTest() throws Exception {
        this.mockMvc.perform(get("/accounts/{id}/orders", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("User2")
    public void getOrdersByAccountIdUnauthorizedTest() throws Exception {
        this.mockMvc.perform(get("/accounts/{1}/orders", 1))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("User2")
    public void getAccountByIdUnauthorizedTest() throws Exception {
        this.mockMvc.perform(get("/accounts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("User3")
    public void getAccountByIdAuthorizedTest() throws Exception {
        Long id = accountRepository.findByEmail("User3@mail.ru").get().getId();
        this.mockMvc.perform(get("/accounts/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void CreateAccountTest() throws Exception {
        String requestBody = "{\"firstName\":\"Mallory\",\"secondName\":\"Cyber\"," +
                "\"phone\":\"+375296338624\",\"email\":\"testmail@mail.ru\",\"credentials\":" +
                "{ \"username\": \"Mallory\", \"password\": \"ultrasuperpass\"}}";

        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidAccountTest() throws Exception {
        String requestBody = "{\"secondName\":\"Cyber\",\"phone\":\"113\",\"email\":\"113\"" +
                ",\"credentials\":{ \"username\": \"Mallory\", \"password\": \"ultrasuerpass\" }}";
        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateAccountAdminTest() throws Exception {
        fillUpdateAccountDummyData();
        Long id = accountRepository.findByEmail("updacc").get().getId();
        String requestBody = "{\"id\":\"" + id + "\",\"firstName\":\"updaccnew\"," +
                "\"secondName\":\"updaccnew\",\"phone\":\"80295673434\",\"email\":\"updaccnew@mail.ru\"}";
        this.mockMvc.perform(put("/accounts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillUpdateAccountDummyData() {
        Account dummyCredentialsData = Account.builder()
                .firstName("updacc")
                .secondName("updacc")
                .phone("updacc")
                .email("updacc")
                .credentials(Credentials.builder()
                        .username("updacc")
                        .password("updacc")
                        .build())
                .build();
        accountRepository.save(dummyCredentialsData);
    }

    @Test
    @WithUserDetails("User2")
    public void updateAccountUnauthorizedTest() throws Exception {
        String requestBody = "{\"id\":\"4\",\"firstName\":\"updaccnew\",\"secondName\":\"updaccnew\"" +
                ",\"phone\":\"+375331225432\",\"email\":\"updaccnew@mail.ru\"}";

        this.mockMvc.perform(put("/accounts/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("updaccUser1")
    public void updateAccountAuthorizedTest() throws Exception {
        Account accountForUpdate = accountRepository.findByEmail("updaccAuth1@mail.ru").get();
        String requestBody = "{\"id\":\"" + accountForUpdate.getId() + "\",\"firstName\":\"updaccnewAuth1\"," +
                "\"secondName\":\"updaccnewAuth1\",\"phone\":\"+375331234564\"," +
                "\"email\":\"updaccAuth1@mail.ru\"}";

        this.mockMvc.perform(put("/accounts/{id}", accountForUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateNonexistentAccountTest() throws Exception {
        String requestBody = "{\"id\":\"1000000\",\"firstName\":\"chlfdfh\",\"secondName\":\"dsfgdfg\"" +
                ",\"phone\":\"+375291423423\",\"email\":\"1sdafasdf13@mail.ru\"}";

        this.mockMvc.perform(put("/accounts/{id}", 1000000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteAccountByIdAdminTest() throws Exception {
        fillDeleteAccountByIdDummyData();
        Long id = accountRepository.findByEmail("deleteaccid11").get().getId();
        mockMvc.perform(delete("/accounts/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteAccountByIdDummyData() {
        Account dummyCredentialsData = Account.builder()
                .firstName("deleteaccid")
                .secondName("deleteaccid")
                .phone("deleteaccid")
                .email("deleteaccid")
                .credentials(Credentials.builder()
                        .username("deleteaccid")
                        .password("deleteaccid")
                        .build())
                .build();
        accountRepository.save(dummyCredentialsData);

        Account dummyCredentialsData1 = Account.builder()
                .firstName("deleteaccid11")
                .secondName("deleteaccid11")
                .phone("deleteaccid11")
                .email("deleteaccid11")
                .credentials(Credentials.builder()
                        .username("deleteaccid11")
                        .password("deleteaccid11")
                        .build())
                .build();
        accountRepository.save(dummyCredentialsData1);

        Account dummyCredentialsData2 = Account.builder()
                .firstName("deleteaccid22")
                .secondName("deleteaccid22")
                .phone("deleteaccid22")
                .email("deleteaccid22")
                .credentials(Credentials.builder()
                        .username("deleteaccid22")
                        .password("deleteaccid22")
                        .build())
                .build();
        accountRepository.save(dummyCredentialsData2);
    }

    @Test
    @WithUserDetails("delaccUser")
    public void deleteAccountByIdAuthorizedTest() throws Exception {
        Account accountForDeletion = accountRepository.findByEmail("delaccAuth1").get();
        mockMvc.perform(delete("/accounts/{id}", accountForDeletion.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteNonexistentAccountByIdTest() throws Exception {
        mockMvc.perform(delete("/accounts/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void getAllAccountsTest() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @WithUserDetails("User2")
    public void getAccountsWithFiltersUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/accounts/fltr?firstNameLike=pet&discountLessThan=10"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void getAccountsWithFiltersTest() throws Exception {
        mockMvc.perform(get("/accounts/fltr?firstNameLike=Us%&discountLessThan=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @WithUserDetails("Admin")
    public void getAccountsWithInvalidFiltersTest() throws Exception {
        mockMvc.perform(get("/accounts/fltr?firstNamee=pet&discountLessThan=10"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
