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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ContextConfigurationTest.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
public class ItemControllerTest extends ContainersEnvironment {
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
            Account admin = Account.builder()
                    .firstName("Admin")
                    .secondName("Admin")
                    .phone("+3758232734")
                    .email("kfgkzsf")
                    .credentials(Credentials.builder()
                            .username("Admin")
                            .password("escapism")
                            .role(Role.ADMIN)
                            .build())
                    .build();
            accountRepository.save(admin);
        }
        if (accountRepository.findByEmail("kfgkzsfdf").isEmpty()) {
            Account user2 = Account.builder()
                    .firstName("User2")
                    .secondName("user2")
                    .phone("+375823274")
                    .email("kfgkzsfdf")
                    .credentials(Credentials.builder()
                            .username("User2")
                            .password("escapism2")
                            .build())
                    .build();
            accountRepository.save(user2);
        }
        if (accountRepository.findByEmail("kfgkzsddgd").isEmpty()) {
            Account user3 = Account.builder()
                    .firstName("User3")
                    .secondName("user3")
                    .phone("+375823wer")
                    .email("kfgkzsddgd")
                    .credentials(Credentials.builder()
                            .username("User3")
                            .password("escapism3")
                            .build())
                    .build();
            accountRepository.save(user3);
        }
    }

    @Test
    @WithUserDetails("Admin")
    public void getItemByIdTest() throws Exception {
        fillGetItemByIdDummyData();
        int itemId = 1;
        this.mockMvc.perform(get("/items/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillGetItemByIdDummyData() throws Exception {
        String dummyCategoryData = "{\"name\": \"getByIddata112\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String dummyItemData = "{\"category\":{\"id\":\"1\"},\"name\":\"getItemByIddata\"" +
                ",\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dummyItemData))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithUserDetails("Admin")
    public void createItemTest() throws Exception {
        String dummyCategoryData = "{\"name\": \"createdata11\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String requestBody = "{\"category\":{\"id\":\"1\"},\"name\":\"createItemdata\"," +
                "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithUserDetails("User2")
    public void createItemWithUnauthorizedIdTest() throws Exception {
        String requestBody = "{\"category\":{\"id\":\"1\"},\"name\":\"createItemdata\",\"price\":\"1\",\"quantity\":\"1\"}";
        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidItemTest() throws Exception {
        String requestBody = "{\"name\": \"\"}";
        this.mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateItemTest() throws Exception {
        fillUpdateItemTestDummyData();
        String requestBody = "{\"id\" : \"1\",\"category\":{\"id\":\"1\",\"name\":\"cataup\",\"discount\":\"0\"}," +
                "\"name\":\"updateItemData\",\"price\":\"12\",\"quantity\":\"1\",\"discount\":\"0\"}";
        this.mockMvc.perform(put("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("updateItemData"));
    }

    private void fillUpdateItemTestDummyData() throws Exception {
        String dummyCategoryData = "{\"name\": \"updateDdata11\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));
        String dummyItemData = "{\"category\":{\"id\":\"1\"},\"name\":\"updateItem\"" +
                ",\"price\":\"12\",\"quantity\":\"1\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyItemData));
    }

    @Test
    @WithUserDetails("User2")
    public void updateItemWithUnauthorizedUserTest() throws Exception {
        String requestBody = "{\"id\" : \"1\",\"category\":{\"id\":\"1\",\"name\":\"cataup\"},\"name\":\"updateItemData\",\"price\":\"12\",\"quantity\":\"1\"}";
        this.mockMvc.perform(put("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithUserDetails("Admin")
    public void updateInvalidItemTest() throws Exception {
        String requestBody = "{\"category\":{\"id\":\"1000\"},\"name\":\"updateInvalidData\",\"price\":\"1\",\"quantity\":\"1\"}";
        this.mockMvc.perform(put("/items/{id}", 1000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteItemByIdTest() throws Exception {
        fillDeleteItemByIdDummyData();

        mockMvc.perform(delete("/items/{id}", 4))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("User3")
    public void deleteItemByIdWithUnauthorizedUserTest() throws Exception {
        mockMvc.perform(delete("/items/{id}", 4))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private void fillDeleteItemByIdDummyData() throws Exception {
        String dummyCategoryData = "{\"name\": \"deleteByIdDdata11\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemByIddata\"," +
                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}",
                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemByIddata1\"," +
                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}",
                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemByIddata2\"," +
                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}",
                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemByIddata3\"," +
                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}",
                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemByIddata4\"," +
                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}"
        };

        for (String dummyItemDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyItemDatum));
        }
    }

//    @Test
//    @WithUserDetails("Admin")
//    public void deleteItemByInvalidIdTest() throws Exception {
//        mockMvc.perform(delete("/items/{id}", 500000)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

//    private void fillDeleteItemDummyData() throws Exception {
//        String dummyCategoryData = "{\"name\": \"deleteDdata11\",\"discount\":\"0\"}";
//        this.mockMvc.perform(post("/categories")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(dummyCategoryData));
//
//        String[] dummyItemData = {
//                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemdata\"," +
//                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}",
//                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemdata1\"," +
//                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}",
//                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemdata2\"," +
//                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}",
//                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemdata3\"," +
//                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}",
//                "{\"category\":{\"id\":\"1\"},\"name\":\"deleteItemdata4\"," +
//                        "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\"}"
//        };
//
//        for (String dummyItemDatum : dummyItemData) {
//            this.mockMvc.perform(post("/items")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(dummyItemDatum));
//        }
//    }

//    @Test
//    @WithUserDetails("Admin")
//    public void deleteInvalidItemTest() throws Exception {
//
//        String deleteRequestBody = "{\"id\":\"10000\"}";
//        mockMvc.perform(delete("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(deleteRequestBody))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

    @Test
    @WithUserDetails("Admin")
    public void getAllItemsTest() throws Exception {
        fillGetAllItemsWithDummyData();

        mockMvc.perform(get("/items"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    private void fillGetAllItemsWithDummyData() throws Exception {
        String dummyCategoryData = "{\"name\": \"allitemscat\",\"discount\":\"0\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String dummyItemData = "{\"category\":{\"id\":\"1\"},\"name\":\"getallItems\"," +
                "\"price\":\"1\",\"quantity\":\"1\",\"discount\":\"0\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyItemData));
    }
}
