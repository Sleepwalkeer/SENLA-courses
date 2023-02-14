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
public class OrderControllerTest extends ContainersEnvironment {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    @Order(1)
    public void testCreateOrder() throws Exception {
        fillWithDummyData();

        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":1665778114323,\"endDateTime\":1675778114323,\"totalPrice\":12200}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createInvalidOrderTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateOrderTest() throws Exception {
        String requestBody = "{\"id\": 1,\"customer\":{\"id\":1},\"worker\":{\"id\":2}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":1665778114325,\"endDateTime\":1675778114325,\"totalPrice\":12300}";
        this.mockMvc.perform(put("/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value("12300"));
    }

    @Test
    public void updateInvalidOrderTest() throws Exception {
        String requestBody = "{\"id\": 25,\"customer\":{\"id\":1},\"worker\":{\"id\":2}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":1665778114325,\"endDateTime\":1675778114325,\"totalPrice\":12300}";
        this.mockMvc.perform(put("/orders/{id}", 25)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteOrderByIdTest() throws Exception {
        mockMvc.perform(delete("/orders/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteOrderByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/orders/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteOrderTest() throws Exception {
        String deleteRequestBody = "{\"id\":\"1\"}";
        mockMvc.perform(delete("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteInvalidOrderTest() throws Exception {

        String requestBody = "{\"id\": 150,\"customer\":{\"id\":1},\"worker\":{\"id\":2}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":1665778114325,\"endDateTime\":1675778114325,\"totalPrice\":12300}";
        mockMvc.perform(delete("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllOrdersTest() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    private void fillWithDummyData() throws Exception {

        String[] dummyAccountData = {
                "{\"firstName\":\"name11\",\"secondName\":\"surname12\",\"phone\":\"12\",\"email\":\"12\"," +
                        "\"credentials\":{ \"username\": \"user12\", \"password\": \"pass12\" }}",
                "{\"firstName\":\"name21\",\"secondName\":\"surname22\",\"phone\":\"22\",\"email\":\"22\"," +
                        "\"credentials\":{ \"username\": \"user22\", \"password\": \"pass22\" }}"
        };
        for (String dummyDatum : dummyAccountData) {
            this.mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
        String[] dummyCategoryData = {
                "{\"name\": \"data12\"}",
                "{\"name\": \"data22\"}",
                "{\"name\": \"data32\"}"
        };
        for (String dummyDatum : dummyCategoryData) {
            this.mockMvc.perform(post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"name12\",\"price\":1,\"quantity\":1}",
                "{\"category\":{\"id\":1\"},\"name\":\"name22\",\"price\":1,\"quantity\":1}",
                "{\"category\":{\"id\":1\"},\"name\":\"name32\",\"price\":1,\"quantity\":1}"
        };

        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
    }
}