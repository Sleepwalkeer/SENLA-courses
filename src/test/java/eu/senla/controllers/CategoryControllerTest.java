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
public class CategoryControllerTest extends ContainersEnvironment {

    @Autowired

    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void getCategoryByIdTest() throws Exception {
        int categoryId = 2;
        this.mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoryId));
    }

    @Test
    @Order(1)
    public void createCategoryTest() throws Exception {
       fillWithDummyData();
        String requestBody = "{\"name\": \"data\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    public void createInvalidCategoryTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateCategoryTest() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"Apartments\"}";

        this.mockMvc.perform(put("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Apartments"));
    }

    @Test
    public void updateInvalidCategoryTest() throws Exception {
        String updateRequestBody = "{\"id\":7000,\"name\":\"Electronics\"}";

        this.mockMvc.perform(put("/categories/{id}", 7000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteCategoryByIdTest() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteAccountByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteCategoryTest() throws Exception {
        String deleteRequestBody = "{\"id\":\"1\"}";
        mockMvc.perform(delete("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteInvalidCategoryTest() throws Exception {

        String deleteRequestBody = "{\"id\":100000000,\"name\":\"Apartments\"}";
        mockMvc.perform(delete("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllCategoriesTest() throws  Exception{
        mockMvc.perform(get("/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }
    private void fillWithDummyData() throws Exception {
        String[] dummyData = {
                "{\"name\": \"data1\"}",

        };
        for (String dummyDatum : dummyData) {
            this.mockMvc.perform(post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
    }
}

