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
public class CategoryControllerTest extends ContainersEnvironment {

    @Autowired

    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void createCategoryTest() throws Exception {
        String requestBody = "{\"name\": \"create\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void getCategoryByIdTest() throws Exception {
        String dummyData = "{\"name\": \"getByIdData\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyData));

        int categoryId = 1;
        this.mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoryId));
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
    public void updateCategoryTest() throws Exception {
        String dummyData = "{\"name\": \"updateData\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyData));

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
    public void deleteCategoryByIdTest() throws Exception {

        fillDeleteCategoryByIdDummyData();
        mockMvc.perform(delete("/categories/{id}", 6))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteCategoryByIdDummyData() throws Exception {

        String[] deleteCategories = {
                "{\"name\": \"deleteByIdData\"}",
                "{\"name\": \"deleteByIdData1\"}",
                "{\"name\": \"deleteByIdData2\"}",
                "{\"name\": \"deleteByIdData3\"}",
                "{\"name\": \"deleteByIdData4\"}",
                "{\"name\": \"deleteByIdData5\"}"
        };
        for (String category : deleteCategories) {
            this.mockMvc.perform(post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(category));
        }
    }

    @Test
    public void deleteCategoryByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteCategoryTest() throws Exception {

        fillDeleteCategoryDummyData();

        String deleteRequestBody = "{\"id\":\"5\"}";
        mockMvc.perform(delete("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteCategoryDummyData() throws Exception {

        String[] deleteCategories = {
                "{\"name\": \"deleteByData\"}",
                "{\"name\": \"deleteByData1\"}",
                "{\"name\": \"deleteByData2\"}",
                "{\"name\": \"deleteByData3\"}",
                "{\"name\": \"deleteByData4\"}",
                "{\"name\": \"deleteByData5\"}"
        };
        for (String category : deleteCategories) {
            this.mockMvc.perform(post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(category));
        }
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
    public void getAllCategoriesTest() throws Exception {
        fillGetALlCategoriesDummyData();
        String dummyData = "{\"name\": \"getAllData\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyData));

        mockMvc.perform(get("/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    private void fillGetALlCategoriesDummyData() throws Exception {
        String dummyData = "{\"name\": \"getAll\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyData));
    }
}


