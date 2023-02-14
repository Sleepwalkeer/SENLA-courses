package eu.senla.controllers;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ServletConfigurationTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class})
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialsControllerTest extends ContainersEnvironment {

    @Autowired

    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void getCredentialsByIdTest() throws Exception {
        int credentialsId = 2;
        this.mockMvc.perform(get("/credentials/{id}", credentialsId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credentialsId));
    }

    @Test
    @Order(1)
    public void createCredentialsTest() throws Exception {

        fillWithDummyData();
        String malloryKay = "{ \"username\": \"MalloryCreds\", \"password\": \"youhavebeenimmortalizedhere\" }";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malloryKay))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createInvalidCredentialsTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 1, \"username\": \"stella_ickerton\", \"password\": \"stellas\" }";

        this.mockMvc.perform(put("/credentials/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("stella_ickerton"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("stellas"));
    }

    @Test
    public void updateInvalidCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 7000000, \"username\": \"ssgsg\", \"password\": \"sw5elr\" }";

        this.mockMvc.perform(put("/credentials/{id}", 700000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteCredentialsByIdTest() throws Exception {
        mockMvc.perform(delete("/credentials/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteAccountByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/credentials/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": \"1\" }";
        mockMvc.perform(delete("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteInvalidCredentialsTest() throws Exception {

        String deleteRequestBody = "{\"id\":100000000,\"name\":\"Apartments\"}";
        mockMvc.perform(delete("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllCredentialsTest() throws Exception {
        mockMvc.perform(get("/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    private void fillWithDummyData() throws Exception {
        String[] dummyCredentialsData = {
                "{ \"username\": \"user1\", \"password\": \"password123\" }",
        "{ \"username\": \"user2\", \"password\": \"dfsgfdsgsfdg\" }",
        "{ \"username\": \"user3\", \"password\": \"122345\" }",
        "{ \"username\": \"user4\", \"password\": \"qwerty143524\" }",
        "{ \"username\": \"user5\", \"password\": \"pass\" }"
        };

        for (String dummyDatum : dummyCredentialsData) {
            this.mockMvc.perform(post("/credentials")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
    }
}
