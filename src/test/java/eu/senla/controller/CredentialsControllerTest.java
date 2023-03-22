package eu.senla.controller;

import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ContextConfigurationTest;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entity.Account;
import eu.senla.entity.Credentials;
import eu.senla.entity.Role;
import eu.senla.repository.AccountRepository;
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
        if (accountRepository.findByEmail("updCredTest1").isEmpty()) {
            Account updateAccAuth = Account.builder()
                    .firstName("updCredTest1")
                    .secondName("updCredTest1")
                    .phone("updCredTest1")
                    .email("updCredTest1")
                    .credentials(Credentials.builder()
                            .username("updCredTest1")
                            .password("updCredTest1")
                            .build())
                    .build();
            accountRepository.save(updateAccAuth);
        }
        if (accountRepository.findByEmail("updCredTest2").isEmpty()) {
            Account updateAccAuth = Account.builder()
                    .firstName("updCredTest2")
                    .secondName("updCredTest2")
                    .phone("updCredTest2")
                    .email("updCredTest2")
                    .credentials(Credentials.builder()
                            .username("updCredTest2")
                            .password("updCredTest2")
                            .build())
                    .build();
            accountRepository.save(updateAccAuth);
        }
        if (accountRepository.findByEmail("delCredTest@mail.ru").isEmpty()) {
            Account dellAccAuth = Account.builder()
                    .firstName("delCredTest")
                    .secondName("delCredTest")
                    .phone("80295723738")
                    .email("delCredTest@mail.ru")
                    .credentials(Credentials.builder()
                            .username("delCredTest")
                            .password("delCredTest")
                            .build())
                    .build();
            accountRepository.save(dellAccAuth);
        }
    }

    @Test
    @WithUserDetails("User2")
    public void getCredentialsUnauthorizedTest() throws Exception {
        this.mockMvc.perform(get("/credentials/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("User3")
    public void getCredentialsAuthorizedTest() throws Exception {
        this.mockMvc.perform(get("/credentials/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void createCredentialsTest() throws Exception {
        String malloryKay = "{ \"username\": \"MalloryCreds\", \"password\": \"amlktewr43awk5\"}";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malloryKay))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 1, \"username\": \"b\", \"password\": \"stellas\" , \"role\" : \"USER\" }";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("updCredTest1")
    public void updateCredentialsTest() throws Exception {
        Credentials credsForUpdate = accountRepository.findByEmail("updCredTest1").get().getCredentials();
        String requestBody = "{\"id\":\"" + credsForUpdate.getId() +
                "\", \"username\": \"Ssdfs2\", \"password\": \"Sldsfee\" , \"role\" : \"USER\" }";
        this.mockMvc.perform(put("/credentials/{id}", credsForUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateCredentialsAdminTest() throws Exception {
        Credentials credsForUpdate = accountRepository.findByEmail("updCredTest2").get().getCredentials();
        String requestBody = "{\"id\":\"" + credsForUpdate.getId() +
                "\", \"username\": \"Ssdfsv2\", \"password\": \"Sldsvfee\" , \"role\" : \"USER\" }";
        this.mockMvc.perform(put("/credentials/{id}", credsForUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("User2")
    public void updateCredentialsUnathorizedTest() throws Exception {
        String requestBody = "{ \"id\": 4, \"username\": \"stella_ickerton\", \"password\": \"stellas\" , \"role\" : \"USER\" }";

        this.mockMvc.perform(put("/credentials/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteCredentialsByIdAdminTest() throws Exception {
        fillDeleteByIdCredentialsDummyData();
        mockMvc.perform(delete("/credentials/{id}", 7))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private void fillDeleteByIdCredentialsDummyData() {
        Account dummyCredentialsData = Account.builder()
                .firstName("deleteCredsid")
                .secondName("deleteCredsid")
                .phone("deleteCredsid")
                .email("deleteCredsid")
                .credentials(Credentials.builder()
                        .username("deleteCredsid")
                        .password("deleteCredsid")
                        .build())
                .build();
        accountRepository.save(dummyCredentialsData);
        Account dummyCredentialsData1 = Account.builder()
                .firstName("deleteCredsid11")
                .secondName("deleteCredsid11")
                .phone("deleteCredsid11")
                .email("deleteCredsid11")
                .credentials(Credentials.builder()
                        .username("deleteCredsid11")
                        .password("deleteCredsid11")
                        .build())
                .build();
        accountRepository.save(dummyCredentialsData1);
        Account dummyCredentialsData2 = Account.builder()
                .firstName("deleteCredsid22")
                .secondName("deleteCredsid22")
                .phone("deleteCredsid22")
                .email("deleteCredsid22")
                .credentials(Credentials.builder()
                        .username("deleteCredsid22")
                        .password("deleteCredsid22")
                        .build())
                .build();
        accountRepository.save(dummyCredentialsData2);
        Account dummyCredentialsData3 = Account.builder()
                .firstName("deleteCredsid33")
                .secondName("deleteCredsid33")
                .phone("deleteCredsid33")
                .email("deleteCredsid33")
                .credentials(Credentials.builder()
                        .username("deleteCredsid33")
                        .password("deleteCredsid33")
                        .build())
                .build();
        accountRepository.save(dummyCredentialsData3);
    }

    @Test
    @WithUserDetails("delCredTest")
    public void deleteCredentialsByIdAuthorizedTest() throws Exception {
        Credentials credsForDeletion = accountRepository.findByEmail("delCredTest@mail.ru").get().getCredentials();
        mockMvc.perform(delete("/credentials/{id}", credsForDeletion.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
