package eu.senla.controller;

import eu.senla.configuration.ContextConfigurationTest;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.repository.AccountRepository;
import eu.senla.entity.Account;
import eu.senla.entity.Credentials;
import eu.senla.entity.Role;
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
@ContextConfiguration(classes = {ContextConfigurationTest.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class CredentialsControllerTest extends ContainersEnvironment {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountRepository accountRepository;
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
        if (accountRepository.findByEmail("kfgkzsf").isEmpty()) {
            Account admin = Account.builder().firstName("Admin").secondName("Admin")
                    .phone("+3758232734").email("kfgkzsf")
                    .credentials(Credentials.builder().username("Admin").password("escapism").role(Role.ADMIN).build()).build();
            accountRepository.save(admin);
        }
        if (accountRepository.findByEmail("kfgkzsfdf").isEmpty()) {
            Account user2 = Account.builder().firstName("User2").secondName("user2")
                    .phone("+375823274").email("kfgkzsfdf")
                    .credentials(Credentials.builder().username("User2").password("escapism2").role(Role.USER).build()).build();
            accountRepository.save(user2);
        }
        if (accountRepository.findByEmail("kfgkzsddgd").isEmpty()) {
            Account user3 = Account.builder().firstName("User3").secondName("user3")
                    .phone("+375823wer").email("kfgkzsddgd")
                    .credentials(Credentials.builder().username("User3").password("escapism3").role(Role.USER).build()).build();
            accountRepository.save(user3);
        }
        if (accountRepository.findByEmail("updaccAuth11").isEmpty()) {
            Account updateAccAuth = Account.builder().firstName("updaccAuth11").secondName("updaccAuth11")
                    .phone("updaccAuth11").email("updaccAuth11")
                    .credentials(Credentials.builder().username("updaccAuth11").password("updaccAuth11").role(Role.USER).build()).build();
            accountRepository.save(updateAccAuth);
        }
        if (accountRepository.findByEmail("delaccAuth11").isEmpty()) {
            Account dellAccAuth = Account.builder().firstName("delaccAuth11").secondName("delaccAuth11")
                    .phone("delaccAuth11").email("delaccAuth11")
                    .credentials(Credentials.builder().username("delaccAuth11").password("delaccAuth11").role(Role.USER).build()).build();
            accountRepository.save(dellAccAuth);
        }
    }

    @Test
    @WithUserDetails("Admin")
    public void getCredentialsByIdTest() throws Exception {
        int credentialsId = 1;
        this.mockMvc.perform(get("/credentials/{id}", credentialsId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credentialsId));
    }

    @Test
    @WithUserDetails("User2")
    public void getCredentialsByUnauthorizedIdTest() throws Exception {
        this.mockMvc.perform(get("/credentials/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("User3")
    public void getCredentialsByAuthorizedIdTest() throws Exception {
        this.mockMvc.perform(get("/credentials/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3));
    }

    @Test
    @WithUserDetails("Admin")
    public void createCredentialsTest() throws Exception {

        String malloryKay = "{ \"username\": \"MalloryCreds\", \"password\": \"youhavebeenimmortalizedhere\"," +
                " \"role\" : \"USER\" }";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malloryKay))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 1, \"username\": \"\", \"password\": \"stellas\" , \"role\" : \"USER\" }";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateCredentialsTest() throws Exception {
        fillUpdateCredentialsDummyData();
        Credentials credsForUpdate = accountRepository.findByEmail("updCredsTest").get().getCredentials();
        String requestBody = "{\"id\":\"" + credsForUpdate.getId() +
                "\", \"username\": \"Ssdfs2\", \"password\": \"Sldsfee\" , \"role\" : \"USER\" }";
        this.mockMvc.perform(put("/credentials/{id}", credsForUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credsForUpdate.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Ssdfs2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Sldsfee"));
    }

    private void fillUpdateCredentialsDummyData() {
        Account dummyCredentialsData = Account.builder().firstName("updCredsTest").secondName("updCredsTest")
                .phone("updCredsTest").email("updCredsTest")
                .credentials(Credentials.builder().username("updCredsTest").password("updCredsTest").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData);
    }

    @Test
    @WithUserDetails("User2")
    public void updateCredentialsByUnauthorizedUserTest() throws Exception {
        String requestBody = "{ \"id\": 4, \"username\": \"stella_ickerton\", \"password\": \"stellas\" , \"role\" : \"USER\" }";

        this.mockMvc.perform(put("/credentials/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("updaccAuth11")
    public void updateAccountByAuthorizedUserTest() throws Exception {
        Credentials credsForUpdate = accountRepository.findByEmail("updaccAuth11").get().getCredentials();
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
    @WithUserDetails("Admin")
    public void updateInvalidCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 7000000, \"username\": \"ssgsg\", \"password\": \"sw5elr\" , \"role\" : \"USER\" }";

        this.mockMvc.perform(put("/credentials/{id}", 700000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteCredentialsByIdTest() throws Exception {
        fillDeleteByIdCredentialsDummyData();
        mockMvc.perform(delete("/credentials/{id}", 7))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("delaccAuth11")
    public void deleteCredentialsByAuthorizedIdTest() throws Exception {
        Credentials credsForDeletion = accountRepository.findByEmail("delaccAuth11").get().getCredentials();
        mockMvc.perform(delete("/credentials/{id}", credsForDeletion.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteByIdCredentialsDummyData() {
        Account dummyCredentialsData = Account.builder().firstName("deleteCredsid").secondName("deleteCredsid")
                .phone("deleteCredsid").email("deleteCredsid")
                .credentials(Credentials.builder().username("deleteCredsid").password("deleteCredsid").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData);
        Account dummyCredentialsData1 = Account.builder().firstName("deleteCredsid11").secondName("deleteCredsid11")
                .phone("deleteCredsid11").email("deleteCredsid11")
                .credentials(Credentials.builder().username("deleteCredsid11").password("deleteCredsid11").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData1);
        Account dummyCredentialsData2 = Account.builder().firstName("deleteCredsid22").secondName("deleteCredsid22")
                .phone("deleteCredsid22").email("deleteCredsid22")
                .credentials(Credentials.builder().username("deleteCredsid22").password("deleteCredsid22").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData2);
        Account dummyCredentialsData3 = Account.builder().firstName("deleteCredsid33").secondName("deleteCredsid33")
                .phone("deleteCredsid33").email("deleteCredsid33")
                .credentials(Credentials.builder().username("deleteCredsid33").password("deleteCredsid33").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData3);
    }

//    @Test
//    @WithUserDetails("Admin")
//    public void deleteCredentialsByInvalidIdTest() throws Exception {
//        mockMvc.perform(delete("/credentials/{id}", 500000)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

    @Test
    @WithUserDetails("Admin")
    public void deleteCredentialsTest() throws Exception {
        fillDeleteCredentialsDummyData();
        String requestBody = "{ \"id\": \"8\" }";
        mockMvc.perform(delete("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteCredentialsDummyData() {
        Account dummyCredentialsData = Account.builder().firstName("deleteCreds").secondName("deleteCreds")
                .phone("deleteCreds").email("deleteCreds")
                .credentials(Credentials.builder().username("deleteCreds").password("deleteCreds").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData);
        Account dummyCredentialsData1 = Account.builder().firstName("deleteCreds11").secondName("deleteCreds11")
                .phone("deleteCreds11").email("deleteCreds11")
                .credentials(Credentials.builder().username("deleteCreds11").password("deleteCreds11").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData1);
        Account dummyCredentialsData2 = Account.builder().firstName("deleteCreds22").secondName("deleteCreds22")
                .phone("deleteCreds22").email("deleteCreds22")
                .credentials(Credentials.builder().username("deleteCreds22").password("deleteCreds22").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData2);
        Account dummyCredentialsData3 = Account.builder().firstName("deleteCreds33").secondName("deleteCreds33")
                .phone("deleteCreds33").email("deleteCreds33")
                .credentials(Credentials.builder().username("deleteCreds33").password("deleteCreds33").role(Role.USER).build()).build();
        accountRepository.save(dummyCredentialsData3);
    }

//    @Test
//    @WithUserDetails("Admin")
//    public void deleteInvalidCredentialsTest() throws Exception {
//        String requestBody = "{ \"id\": 100000, \"username\": \"stella_ickerton\", \"password\": \"stellas\" }";
//        mockMvc.perform(delete("/credentials")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

    @Test
    @WithUserDetails("Admin")
    public void getAllCredentialsTest() throws Exception {
        mockMvc.perform(get("/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }
}
