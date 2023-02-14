package eu.senla.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class})
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest extends ContainersEnvironment {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    @Order(1)
    public void testCreateAccount() throws Exception{
        fillWithDummyData();
        String requestBody = "{\"firstName\":\"Mallory\",\"secondName\":\"Cyber\",\"phone\":\"113\",\"email\":\"113\"" +
                ",\"credentials\":{ \"username\": \"Mallory\", \"password\": \"ultrasuerpass\" }}";
        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void createInvalidAccountTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateAccountTest() throws Exception {
        String requestBody = "{\"id\":\"1\",\"firstName\":\"chlfdfh\",\"secondName\":\"dsfgdfg\",\"phone\":\"14234\",\"email\":\"1sdafasdf13\",\"credentials\":{\"id\":\"1\"}}";
        this.mockMvc.perform(put("/accounts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("chlfdfh"));
    }

    @Test
    public void updateInvalidAccountTest() throws Exception {
        String requestBody = "{\"id\":\"1000000\",\"firstName\":\"chlfdfh\",\"secondName\":\"dsfgdfg\",\"phone\":\"14234\",\"email\":\"1sdafasdf13\",\"credentials\":{\"id\":\"100000\"}}";


        this.mockMvc.perform(put("/accounts/{id}", 1000000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteAccountByIdTest() throws Exception {
        mockMvc.perform(delete("/accounts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteAccountByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/accounts/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteAccountTest() throws Exception {
        String deleteRequestBody = "{\"id\":\"1\"}";
        mockMvc.perform(delete("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteInvalidAccountTest() throws Exception {

        String requestBody = "{\"id\":\"1000000\",\"firstName\":\"chlfdfh\",\"secondName\":\"dsfgdfg\",\"phone\":\"14234\",\"email\":\"1sdafasdf13\",\"credentials\":{\"id\":\"100000\"}}";

        mockMvc.perform(delete("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllAccountsTest() throws  Exception{
        mockMvc.perform(get("/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }
    private void fillWithDummyData() throws Exception {

        String[] dummyAccountData = {
                "{\"firstName\":\"name11\",\"secondName\":\"surname11\",\"phone\":\"11\",\"email\":\"11\"," +
                        "\"credentials\":{ \"username\": \"user11\", \"password\": \"pass11\" }}"

        };
        for (String dummyDatum : dummyAccountData) {
            this.mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
    }
}
