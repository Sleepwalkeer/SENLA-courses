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
public class CategoryControllerTest extends ContainersEnvironment {
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
                    .credentials(Credentials.builder().username("Admin").password("escapism").role(Role.ADMIN).build()).build();
            accountDao.save(admin);
        }
        if (accountDao.findByEmail("kfgkzsfdf").isEmpty()) {
            Account user2 = Account.builder().firstName("User2").secondName("user2")
                    .phone("+375823274").email("kfgkzsfdf")
                    .credentials(Credentials.builder().username("User2").password("escapism2").role(Role.USER).build()).build();
            accountDao.save(user2);
        }
        if (accountDao.findByEmail("kfgkzsddgd").isEmpty()) {
            Account user3 = Account.builder().firstName("User3").secondName("user3")
                    .phone("+375823wer").email("kfgkzsddgd")
                    .credentials(Credentials.builder().username("User3").password("escapism3").role(Role.USER).build()).build();
            accountDao.save(user3);
        }
    }

    @Test
    @WithUserDetails("Admin")
    public void createCategoryTest() throws Exception {
        String requestBody = "{\"name\": \"create\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithUserDetails("User2")
    public void createCategoryWithUnauthorizedUserTest() throws Exception {
        String requestBody = "{\"name\": \"create\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    @WithUserDetails("Admin")
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
    @WithUserDetails("Admin")
    public void createInvalidCategoryTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
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
    @WithUserDetails("User3")
    public void updateCategoryWithUnauthorizedUserTest() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"Apartments\"}";

        this.mockMvc.perform(put("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateInvalidCategoryTest() throws Exception {
        String updateRequestBody = "{\"id\":7000,\"name\":\"Electronics\"}";

        this.mockMvc.perform(put("/categories/{id}", 7000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
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
    @WithUserDetails("User3")
    public void deleteCategoryByIdWithUnauthorizedUserTest() throws Exception {

        fillDeleteCategoryByIdDummyData();
        mockMvc.perform(delete("/categories/{id}", 7))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


//    @Test
//    @WithUserDetails("Admin")
//    public void deleteCategoryByInvalidIdTest() throws Exception {
//        mockMvc.perform(delete("/categories/{id}", 500000)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

    @Test
    @WithUserDetails("Admin")
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
                    .content(category))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

//    @Test
//    @WithUserDetails("Admin")
//    public void deleteInvalidCategoryTest() throws Exception {
//
//        String deleteRequestBody = "{\"id\":100000000,\"name\":\"Apartments\"}";
//        mockMvc.perform(delete("/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(deleteRequestBody))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

    @Test
    @WithUserDetails("Admin")
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


