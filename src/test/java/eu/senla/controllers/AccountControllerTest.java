package eu.senla.controllers;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.dao.AccountDao;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;
import eu.senla.entities.Role;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class AccountControllerTest extends ContainersEnvironment {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountDao accountDao;
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
        if (accountDao.findByEmail("kfgkzsf").isEmpty()) {
            Account admin = Account.builder().firstName("Admin").secondName("Admin")
                    .phone("+3758232734").email("kfgkzsf")
                    .credentials(Credentials.builder().username("Admin").password("escapism").role(Role.ADMIN).build()).build();
            accountDao.save(admin);
        }
        if (accountDao.findByEmail("kfgkzsfdf").isEmpty()) {
            Account user2 = Account.builder().firstName("User2").secondName("user2")
                    .phone("+375823274").email("kfgkzsfdf")
                    .credentials(Credentials.builder().username("User2").password("escapism2").role(Role.USER).build()).build();
            accountDao.save(user2);
        }
        if (accountDao.findByEmail("kfgkzsddgd").isEmpty()) {
            Account user3 = Account.builder().firstName("User3").secondName("user3")
                    .phone("+375823wer").email("kfgkzsddgd")
                    .credentials(Credentials.builder().username("User3").password("escapism3").role(Role.USER).build()).build();
            accountDao.save(user3);
        }
        if (accountDao.findByEmail("updaccAuth1").isEmpty()) {
            Account updateAccAuth = Account.builder().firstName("updaccAuth1").secondName("updaccAuth1")
                    .phone("updaccAuth1").email("updaccAuth1")
                    .credentials(Credentials.builder().username("updaccAuth1").password("updaccAuth1").role(Role.USER).build()).build();
            accountDao.save(updateAccAuth);
        }
        if (accountDao.findByEmail("delaccAuth1").isEmpty()) {
            Account dellAccAuth = Account.builder().firstName("delaccAuth1").secondName("delaccAuth1")
                    .phone("delaccAuth1").email("delaccAuth1")
                    .credentials(Credentials.builder().username("delaccAuth1").password("delaccAuth1").role(Role.USER).build()).build();
            accountDao.save(dellAccAuth);
        }
    }

    @Test
    @WithUserDetails("Admin")
    public void getAccountByIdTest() throws Exception {
        int credentialsId = 1;
        this.mockMvc.perform(get("/accounts/{id}", credentialsId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credentialsId));
    }

    @Test
    @WithUserDetails("User2")
    public void getAccountByUnauthorizedIdTest() throws Exception {
        this.mockMvc.perform(get("/accounts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("User3")
    public void getAccountByAuthorizedIdTest() throws Exception {
        this.mockMvc.perform(get("/accounts/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3));
    }

    @Test
    @WithUserDetails("Admin")
    public void CreateAccountTest() throws Exception {
        String requestBody = "{\"firstName\":\"Mallory\",\"secondName\":\"Cyber\",\"phone\":\"113\",\"email\":\"113\"" +
                ",\"credentials\":{ \"username\": \"Mallory\", \"password\": \"ultrasuperpass\" , \"role\" : \"USER\" }}";

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
    public void updateAccountTest() throws Exception {
        fillUpdateAccountDummyData();

        String requestBody = "{\"id\":\"4\",\"firstName\":\"updaccnew\",\"secondName\":\"updaccnew\",\"phone\":\"updaccnew\"," +
                "\"email\":\"updaccnew\",\"credentials\":{\"id\":\"4\", \"username\": \"updaccnew\"," +
                " \"password\": \"updaccnew\" , \"role\" : \"USER\" }}";
        this.mockMvc.perform(put("/accounts/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("updaccnew"));
    }

    private void fillUpdateAccountDummyData() throws Exception {
        Account dummyCredentialsData = Account.builder().firstName("updacc").secondName("updacc")
                .phone("updacc").email("updacc")
                .credentials(Credentials.builder().username("updacc").password("updacc").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData);
    }

    @Test
    @WithUserDetails("User2")
    public void updateAccountByUnauthorizedUserTest() throws Exception {
        String requestBody = "{\"id\":\"4\",\"firstName\":\"updaccnew\",\"secondName\":\"updaccnew\",\"phone\":\"updaccnew\"," +
                "\"email\":\"updaccnew\",\"credentials\":{\"id\":\"4\", \"username\": \"updaccnew\"," +
                " \"password\": \"updaccnew\" , \"role\" : \"USER\" }}";

        this.mockMvc.perform(put("/accounts/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("updaccAuth1")
    public void updateAccountByAuthorizedUserTest() throws Exception {
        Account accountForUpdate = accountDao.findByEmail("updaccAuth1").get();
        String requestBody = "{\"id\":\"" + accountForUpdate.getId() + "\",\"firstName\":\"updaccnewAuth1\"," +
                "\"secondName\":\"updaccnewAuth1\",\"phone\":\"updaccAuth1\"," +
                "\"email\":\"updaccAuth1\",\"credentials\":{\"id\":\"" + accountForUpdate.getId() +
                "\", \"username\": \"Sleep2\", \"password\": \"Sleep\" , \"role\" : \"USER\" }}";

        this.mockMvc.perform(put("/accounts/{id}", accountForUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountForUpdate.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("updaccnewAuth1"));
    }


    @Test
    @WithUserDetails("Admin")
    public void updateInvalidAccountTest() throws Exception {
        String requestBody = "{\"id\":\"1000000\",\"firstName\":\"chlfdfh\",\"secondName\":\"dsfgdfg\",\"phone\":\"14234\",\"email\":\"1sdafasdf13\",\"credentials\":{\"id\":\"100000\"}}";

        this.mockMvc.perform(put("/accounts/{id}", 1000000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteAccountByIdTest() throws Exception {
        fillDeleteAccountByIdDummyData();
        mockMvc.perform(delete("/accounts/{id}", 6))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("delaccAuth1")
    public void deleteAccountByAuthorizedIdTest() throws Exception {
        Account accountForDeletion = accountDao.findByEmail("delaccAuth1").get();
        mockMvc.perform(delete("/accounts/{id}", accountForDeletion.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteAccountByIdDummyData() throws Exception {
        Account dummyCredentialsData = Account.builder().firstName("deleteaccid").secondName("deleteaccid")
                .phone("deleteaccid").email("deleteaccid")
                .credentials(Credentials.builder().username("deleteaccid").password("deleteaccid").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData);
        Account dummyCredentialsData1 = Account.builder().firstName("deleteaccid11").secondName("deleteaccid11")
                .phone("deleteaccid11").email("deleteaccid11")
                .credentials(Credentials.builder().username("deleteaccid11").password("deleteaccid11").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData1);
        Account dummyCredentialsData2 = Account.builder().firstName("deleteaccid22").secondName("deleteaccid22")
                .phone("deleteaccid22").email("deleteaccid22")
                .credentials(Credentials.builder().username("deleteaccid22").password("deleteaccid22").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData2);
        Account dummyCredentialsData3 = Account.builder().firstName("deleteaccid33").secondName("deleteaccid33")
                .phone("deleteaccid33").email("deleteaccid33")
                .credentials(Credentials.builder().username("deleteaccid33").password("deleteaccid33").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData3);
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteAccountByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/accounts/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteAccountTest() throws Exception {
        fillDeleteAccountDummyData();
        Account account = accountDao.findByEmail("deleteacc22").get();

        String deleteRequestBody = "{\"id\":\"" + account.getId() + "\"}";
        mockMvc.perform(delete("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteAccountDummyData() throws Exception {
        Account dummyCredentialsData = Account.builder().firstName("deleteacc").secondName("deleteacc")
                .phone("deleteacc").email("deleteacc")
                .credentials(Credentials.builder().username("deleteacc").password("deleteacc").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData);
        Account dummyCredentialsData1 = Account.builder().firstName("deleteacc11").secondName("deleteacc11")
                .phone("deleteacc11").email("deleteacc11")
                .credentials(Credentials.builder().username("deleteacc11").password("deleteacc11").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData1);
        Account dummyCredentialsData2 = Account.builder().firstName("deleteacc22").secondName("deleteacc22")
                .phone("deleteacc22").email("deleteacc22")
                .credentials(Credentials.builder().username("deleteacc22").password("deleteacc22").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData2);
        Account dummyCredentialsData3 = Account.builder().firstName("deleteacc33").secondName("deleteacc33")
                .phone("deleteacc33").email("deleteacc33")
                .credentials(Credentials.builder().username("deleteacc33").password("deleteacc33").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData3);
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteInvalidAccountTest() throws Exception {
        String requestBody = "{\"id\":\"1000000\",\"firstName\":\"chlfdfh\",\"secondName\":\"dsfgdfg\"," +
                "\"phone\":\"14234\",\"email\":\"1sdafasdf13\",\"credentials\":{\"id\":\"100000\"}}";

        mockMvc.perform(delete("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void getAllAccountsTest() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }
}
