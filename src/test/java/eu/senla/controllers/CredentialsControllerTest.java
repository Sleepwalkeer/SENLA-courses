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
public class CredentialsControllerTest extends ContainersEnvironment {
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
                    .credentials(Credentials.builder().username("Sleepwalker").password("escapism").role(Role.ADMIN).build()).build();
            accountDao.save(admin);
        }
        if (accountDao.findByEmail("kfgkzsfdf").isEmpty()) {
            Account user2 = Account.builder().firstName("User2").secondName("user2")
                    .phone("+375823274").email("kfgkzsfdf")
                    .credentials(Credentials.builder().username("Sleepwalker2").password("escapism2").role(Role.USER).build()).build();
            accountDao.save(user2);
        }
        if (accountDao.findByEmail("kfgkzsddgd").isEmpty()) {
            Account user3 = Account.builder().firstName("User3").secondName("user3")
                    .phone("+375823wer").email("kfgkzsddgd")
                    .credentials(Credentials.builder().username("Sleepwalker3").password("escapism3").role(Role.USER).build()).build();
            accountDao.save(user3);
        }
        if (accountDao.findByEmail("updaccAuth11").isEmpty()) {
            Account updateAccAuth = Account.builder().firstName("updaccAuth11").secondName("updaccAuth11")
                    .phone("updaccAuth11").email("updaccAuth11")
                    .credentials(Credentials.builder().username("updaccAuth11").password("updaccAuth11").role(Role.USER).build()).build();
            accountDao.save(updateAccAuth);
        }
        if (accountDao.findByEmail("delaccAuth11").isEmpty()) {
            Account dellAccAuth = Account.builder().firstName("delaccAuth11").secondName("delaccAuth11")
                    .phone("delaccAuth11").email("delaccAuth11")
                    .credentials(Credentials.builder().username("delaccAuth11").password("delaccAuth11").role(Role.USER).build()).build();
            accountDao.save(dellAccAuth);
        }
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void getCredentialsByIdTest() throws Exception {
        int credentialsId = 1;
        this.mockMvc.perform(get("/credentials/{id}", credentialsId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credentialsId));
    }

    @Test
    @WithUserDetails("Sleepwalker2")
    public void getCredentialsByUnauthorizedIdTest() throws Exception {
        this.mockMvc.perform(get("/credentials/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Sleepwalker3")
    public void getCredentialsByAuthorizedIdTest() throws Exception {
        this.mockMvc.perform(get("/credentials/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3));
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void createCredentialsTest() throws Exception {

        String malloryKay = "{ \"username\": \"MalloryCreds\", \"password\": \"youhavebeenimmortalizedhere\"," +
                " \"role\" : \"USER\" }";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malloryKay))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void createInvalidCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 1, \"username\": \"\", \"password\": \"stellas\" , \"role\" : \"USER\" }";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void updateCredentialsTest() throws Exception {
        fillUpdateCredentialsDummyData();
        String requestBody = "{ \"id\": 6, \"username\": \"stella_ickerton\", \"password\": \"stellas\" , \"role\" : \"USER\" }";

        this.mockMvc.perform(put("/credentials/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("stella_ickerton"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("stellas"));
    }

    private void fillUpdateCredentialsDummyData() throws Exception {
        Account dummyCredentialsData = Account.builder().firstName("updCreds").secondName("updCreds")
                .phone("updCreds").email("updCreds")
                .credentials(Credentials.builder().username("updCreds").password("updCreds").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData);
    }

    @Test
    @WithUserDetails("Sleepwalker2")
    public void updateCredentialsByUnauthorizedUserTest() throws Exception {
        String requestBody = "{ \"id\": 4, \"username\": \"stella_ickerton\", \"password\": \"stellas\" , \"role\" : \"USER\" }";

        this.mockMvc.perform(put("/accounts/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("updaccAuth11")
    public void updateAccountByAuthorizedUserTest() throws Exception {
        Credentials credsForUpdate = accountDao.findByEmail("updaccAuth11").get().getCredentials();
        String requestBody = "{\"id\":\"" + credsForUpdate.getId() +
                "\", \"username\": \"Slee2\", \"password\": \"Slee\" , \"role\" : \"USER\" }";

        this.mockMvc.perform(put("/credentials/{id}", credsForUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credsForUpdate.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Slee2"));
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void updateInvalidCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 7000000, \"username\": \"ssgsg\", \"password\": \"sw5elr\" , \"role\" : \"USER\" }";

        this.mockMvc.perform(put("/credentials/{id}", 700000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void deleteCredentialsByIdTest() throws Exception {
        fillDeleteByIdCredentialsDummyData();
        mockMvc.perform(delete("/credentials/{id}", 7))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("delaccAuth11")
    public void deleteCredentialsByAuthorizedIdTest() throws Exception {
        Credentials credsForDeletion = accountDao.findByEmail("delaccAuth11").get().getCredentials();
        mockMvc.perform(delete("/accounts/{id}", credsForDeletion.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteByIdCredentialsDummyData() throws Exception {
        Account dummyCredentialsData = Account.builder().firstName("deleteCredsid").secondName("deleteCredsid")
                .phone("deleteCredsid").email("deleteCredsid")
                .credentials(Credentials.builder().username("deleteCredsid").password("deleteCredsid").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData);
        Account dummyCredentialsData1 = Account.builder().firstName("deleteCredsid11").secondName("deleteCredsid11")
                .phone("deleteCredsid11").email("deleteCredsid11")
                .credentials(Credentials.builder().username("deleteCredsid11").password("deleteCredsid11").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData1);
        Account dummyCredentialsData2 = Account.builder().firstName("deleteCredsid22").secondName("deleteCredsid22")
                .phone("deleteCredsid22").email("deleteCredsid22")
                .credentials(Credentials.builder().username("deleteCredsid22").password("deleteCredsid22").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData2);
        Account dummyCredentialsData3 = Account.builder().firstName("deleteCredsid33").secondName("deleteCredsid33")
                .phone("deleteCredsid33").email("deleteCredsid33")
                .credentials(Credentials.builder().username("deleteCredsid33").password("deleteCredsid33").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData3);
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void deleteCredentialsByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/credentials/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void deleteCredentialsTest() throws Exception {
        fillDeleteCredentialsDummyData();
        String requestBody = "{ \"id\": \"8\" }";
        mockMvc.perform(delete("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteCredentialsDummyData() throws Exception {
        Account dummyCredentialsData = Account.builder().firstName("deleteCreds").secondName("deleteCreds")
                .phone("deleteCreds").email("deleteCreds")
                .credentials(Credentials.builder().username("deleteCreds").password("deleteCreds").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData);
        Account dummyCredentialsData1 = Account.builder().firstName("deleteCreds11").secondName("deleteCreds11")
                .phone("deleteCreds11").email("deleteCreds11")
                .credentials(Credentials.builder().username("deleteCreds11").password("deleteCreds11").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData1);
        Account dummyCredentialsData2 = Account.builder().firstName("deleteCreds22").secondName("deleteCreds22")
                .phone("deleteCreds22").email("deleteCreds22")
                .credentials(Credentials.builder().username("deleteCreds22").password("deleteCreds22").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData2);
        Account dummyCredentialsData3 = Account.builder().firstName("deleteCreds33").secondName("deleteCreds33")
                .phone("deleteCreds33").email("deleteCreds33")
                .credentials(Credentials.builder().username("deleteCreds33").password("deleteCreds33").role(Role.USER).build()).build();
        accountDao.save(dummyCredentialsData3);
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void deleteInvalidCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 100000, \"username\": \"stella_ickerton\", \"password\": \"stellas\" }";
        mockMvc.perform(delete("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Sleepwalker")
    public void getAllCredentialsTest() throws Exception {
        mockMvc.perform(get("/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }
}
