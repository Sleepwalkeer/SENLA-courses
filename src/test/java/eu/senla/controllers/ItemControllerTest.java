package eu.senla.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.entities.Item;
import eu.senla.entities.Item;
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

import java.math.BigDecimal;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class})
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemControllerTest extends ContainersEnvironment {

    @Autowired

    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void getItemByIdTest() throws Exception {
        int itemId = 1;
        this.mockMvc.perform(get("/items/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemId));
    }

    @Test
    @Order(1)
    public void createItemTest() throws Exception {
        fillWithDummyData();
        String requestBody = "{\"category\":{\"id\":\"1\"},\"name\":\"JackHammer\",\"price\":5,\"quantity\":5}";
        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void createInvalidItemTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateItemTest() throws Exception {
        String requestBody = "{\"id\" : \"1\",\"category\":{\"id\":\"1\"},\"name\":\"update\",\"price\":10,\"quantity\":10}";


        this.mockMvc.perform(put("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("update"));
    }

    @Test
    public void updateInvalidItemTest() throws Exception {
        String requestBody = "{\"id\" : \"1000\",\"category\":{\"id\":\"1\"},\"name\":\"update\",\"price\":10,\"quantity\":10}";

        this.mockMvc.perform(put("/items/{id}", 1000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteItemByIdTest() throws Exception {
        mockMvc.perform(delete("/items/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteItemByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/items/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteItemTest() throws Exception {
        String deleteRequestBody = "{\"id\":\"1\"}";
        mockMvc.perform(delete("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteInvalidItemTest() throws Exception {

        String deleteRequestBody = "{\"id\":\"10000\"}";
        mockMvc.perform(delete("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllItemsTest() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }
    private void fillWithDummyData() throws Exception {
        String[] dummyCategoryData = {
                "{\"name\": \"data11\"}",
        };
        for (String dummyDatum : dummyCategoryData) {
            this.mockMvc.perform(post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"name1\",\"price\":1,\"quantity\":1}",

        };

        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
    }
}
