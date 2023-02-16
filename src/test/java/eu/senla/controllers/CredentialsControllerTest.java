package eu.senla.controllers;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ServletConfigurationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class})
@WebAppConfiguration
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
        fillGetCredentialsDummyData();
        int credentialsId = 1;
        this.mockMvc.perform(get("/credentials/{id}", credentialsId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credentialsId));
    }

    private void fillGetCredentialsDummyData() throws Exception {
        String dummyCredentialsData = "{\"firstName\":\"get\",\"secondName\":\"get\",\"phone\":\"get\"," +
                "\"email\":\"get\",\"credentials\":{ \"username\": \"get\"," +
                " \"password\": \"get\" }}";
        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dummyCredentialsData))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createCredentialsTest() throws Exception {

        String malloryKay = "{ \"username\": \"MalloryCreds\", \"password\": \"youhavebeenimmortalizedhere\" }";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malloryKay))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createInvalidCredentialsTest() throws Exception {
        String requestBody = "{ \"id\": 1, \"username\": \"\", \"password\": \"stellas\" }";
        this.mockMvc.perform(post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateCredentialsTest() throws Exception {

        fillUpdateCredentialsDummyData();
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

    private void fillUpdateCredentialsDummyData() throws Exception {
        String dummyCredentialsData = "{\"firstName\":\"upd\",\"secondName\":\"upd\",\"phone\":\"upd\"," +
                "\"email\":\"upd\",\"credentials\":{ \"username\": \"upd\"," +
                " \"password\": \"upd\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCredentialsData));
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
    public void deleteCredentialsByIdTest() throws Exception {
        fillDeleteByIdCredentialsDummyData();
        mockMvc.perform(delete("/credentials/{id}", 6))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteByIdCredentialsDummyData() throws Exception {
        String[] dummyCredentialsData = {
                "{\"firstName\":\"deleteid\",\"secondName\":\"deleteid\",\"phone\":\"deleteid\"," +
                        "\"email\":\"deleteid\",\"credentials\":{ \"username\": \"deleteid\"," +
                        " \"password\": \"deleteid\" }}",
                "{\"firstName\":\"deleteid11\",\"secondName\":\"deleteid11\",\"phone\":\"deleteid11\"," +
                        "\"email\":\"deleteid11\",\"credentials\":{ \"username\": \"deleteid11\"," +
                        " \"password\": \"deleteid11\" }}",
                "{\"firstName\":\"deleteid22\",\"secondName\":\"deleteid22\",\"phone\":\"deleteid22\"," +
                        "\"email\":\"deleteid22\",\"credentials\":{ \"username\": \"deleteid22\"," +
                        " \"password\": \"deleteid22\" }}",
                "{\"firstName\":\"deleteid33\",\"secondName\":\"deleteid33\",\"phone\":\"deleteid33\"," +
                        "\"email\":\"deleteid33\",\"credentials\":{ \"username\": \"deleteid33\"," +
                        " \"password\": \"deleteid33\" }}",
                "{\"firstName\":\"deleteid44\",\"secondName\":\"deleteid44\",\"phone\":\"deleteid44\"," +
                        "\"email\":\"deleteid44\",\"credentials\":{ \"username\": \"deleteid44\"," +
                        " \"password\": \"deleteid44\" }}",
                "{\"firstName\":\"deleteid55\",\"secondName\":\"deleteid55\",\"phone\":\"deleteid55\"," +
                        "\"email\":\"deleteid55\",\"credentials\":{ \"username\": \"deleteid55\"," +
                        " \"password\": \"deleteid55\" }}"
        };
        for (String dummyDatum : dummyCredentialsData) {
            this.mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
    }

    @Test
    public void deleteCredentialsByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/credentials/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteCredentialsTest() throws Exception {

        fillDeleteCredentialsDummyData();
        String requestBody = "{ \"id\": \"5\" }";
        mockMvc.perform(delete("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteCredentialsDummyData() throws Exception {
        String[] dummyCredentialsData = {
                "{\"firstName\":\"delete\",\"secondName\":\"delete\",\"phone\":\"delete\"," +
                        "\"email\":\"delete\",\"credentials\":{ \"username\": \"delete\"," +
                        " \"password\": \"delete\" }}",
                "{\"firstName\":\"delete1\",\"secondName\":\"delete1\",\"phone\":\"delete1\"," +
                        "\"email\":\"delete1\",\"credentials\":{ \"username\": \"delete1\"," +
                        " \"password\": \"delete1\" }}",
                "{\"firstName\":\"delete2\",\"secondName\":\"delete2\",\"phone\":\"delete2\"," +
                        "\"email\":\"delete2\",\"credentials\":{ \"username\": \"delete2\"," +
                        " \"password\": \"delete2\" }}",
                "{\"firstName\":\"delete3\",\"secondName\":\"delete3\",\"phone\":\"delete3\"," +
                        "\"email\":\"delete3\",\"credentials\":{ \"username\": \"delete3\"," +
                        " \"password\": \"delete3\" }}",
                "{\"firstName\":\"delete4\",\"secondName\":\"delete4\",\"phone\":\"delete4\"," +
                        "\"email\":\"delete4\",\"credentials\":{ \"username\": \"delete4\"," +
                        " \"password\": \"delete4\" }}",
                "{\"firstName\":\"delete5\",\"secondName\":\"delete5\",\"phone\":\"delete5\"," +
                        "\"email\":\"delete5\",\"credentials\":{ \"username\": \"delete5\"," +
                        " \"password\": \"delete5\" }}"
        };
        for (String dummyDatum : dummyCredentialsData) {
            this.mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Test
    public void deleteInvalidCredentialsTest() throws Exception {

        String requestBody = "{ \"id\": 100000, \"username\": \"stella_ickerton\", \"password\": \"stellas\" }";
        mockMvc.perform(delete("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllCredentialsTest() throws Exception {
        fillGetAllCredentialsDummyData();
        mockMvc.perform(get("/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    private void fillGetAllCredentialsDummyData() throws Exception {
        String dummyCredentialsData = "{\"firstName\":\"getAll\",\"secondName\":\"getAll\",\"phone\":\"getAll\"," +
                "\"email\":\"getAll\",\"credentials\":{ \"username\": \"getAll\"," +
                " \"password\": \"getAll\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCredentialsData));
    }

}
