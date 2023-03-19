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
public class OrderControllerTest extends ContainersEnvironment {
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
    public void getOrderByIdTest() throws Exception {
        fillGetOrderByIdDummyData();
        this.mockMvc.perform(get("/orders/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    private void fillGetOrderByIdDummyData() throws Exception {
        String dummyAccountData = "{\"firstName\":\"getord\",\"secondName\":\"getord\",\"phone\":\"getord\",\"email\":\"getord\"," +
                "\"credentials\":{ \"username\": \"getord\", \"password\": \"getord\" , \"role\" : \"USER\"  },\"discount\":\"0\"}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyAccountData));
        String dummyCategoryData = "{\"name\": \"getord1\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"getord\",\"price\":1,\"quantity\":1,\"discount\":\"0\"}",
                "{\"category\":{\"id\":1\"},\"name\":\"getord\",\"price\":1,\"quantity\":1,\"discount\":\"0\"}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }


    @Test
    @WithUserDetails("Admin")
    public void createOrderTest() throws Exception {

        String dummyCategoryData = "{\"name\": \"getord\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":\"1\"},\"name\":\"getordit1\"," +
                        "\"price\":\"100\",\"quantity\":\"10\"}",
                "{\"category\":{\"id\":\"1\"},\"name\":\"getorditem1\"," +
                        "\"price\":\"100\",\"quantity\":\"10\",\"discount\":\"25\"}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }

        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("User2")
    public void createOrderUnauthorizedIdTest() throws Exception {
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void createInvalidOrderTest() throws Exception {
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateOrderTest() throws Exception {
        fillUpdateOrderDummyData();
        String requestBody = "{\"id\": 1,\"customer\":{\"id\":\"1\"}" +
                ",\"worker\":{\"id\":\"1\"}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(put("/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value("525.0"));
    }

    private void fillUpdateOrderDummyData() throws Exception {
        String dummyAccountData = "{\"firstName\":\"updordc\",\"secondName\":\"updordc\"," +
                "\"phone\":\"updordc\",\"email\":\"updordc\"," +
                "\"credentials\":{\"username\":\"updordc\",\"password\":\"updordc\",\"role\":\"USER\"},\"discount\":\"0\"}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyAccountData));
        String dummyCategoryData = "{\"name\": \"updord\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"updord\",\"price\":1,\"quantity\":1,\"discount\":\"0\"}",
                "{\"category\":{\"id\":1\"},\"name\":\"updord\",\"price\":1,\"quantity\":1,\"discount\":\"0\"}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    @Test
    @WithUserDetails("User3")
    public void updateOrderWithUnauthorizedUserTest() throws Exception {
        String requestBody = "{\"id\": 1,\"customer\":{\"id\":\"1\"}" +
                ",\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(put("/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails("Admin")
    public void updateInvalidOrderTest() throws Exception {
        String requestBody = "{\"id\": 25,\"customer\":{\"id\":1},\"worker\":{\"id\":2}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(put("/orders/{id}", 25)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("Admin")
    public void deleteOrderByIdTest() throws Exception {
        fillDeleteOrderByIdDummyData();
        mockMvc.perform(delete("/orders/{id}", 4))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteOrderByIdDummyData() throws Exception {
        String dummyCategoryData = "{\"name\": \"delidordor\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":\"1\"},\"name\":\"delidordit1\",\"price\":\"1\",\"quantity\":\"10\",\"discount\":\"0\"}",
                "{\"category\":{\"id\":\"1\"},\"name\":\"delidordit2\",\"price\":\"1\",\"quantity\":\"10\",\"discount\":\"0\"}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }

        String[] orders = {
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}"
        };
        for (String order : orders) {
            this.mockMvc.perform(post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(order));
        }
    }

//    @Test
//    @WithUserDetails("Admin")
//    public void deleteOrderByInvalidIdTest() throws Exception {
//        mockMvc.perform(delete("/orders/{id}", 500000)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }


//    @WithUserDetails("Admin")
//    private void fillDeleteOrderDummyData() throws Exception {
//        String dummyAccountData = "{\"firstName\":\"delord\",\"secondName\":\"delord\"" +
//                ",\"phone\":\"delord\",\"email\":\"delord\"," +
//                "\"credentials\":{ \"username\": \"delord\", \"password\": \"delord\"" +
//                ", \"role\" : \"USER\"  },\"discount\":\"0\"}";
//        this.mockMvc.perform(post("/accounts")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(dummyAccountData));
//        String dummyCategoryData = "{\"name\": \"delord\",\"discount\":\"0\"}";
//        this.mockMvc.perform(post("/categories")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(dummyCategoryData));
//
//        String[] dummyItemData = {
//                "{\"category\":{\"id\":1\"},\"name\":\"delord\",\"price\":1,\"quantity\":1,\"discount\":\"0\"}",
//                "{\"category\":{\"id\":1\"},\"name\":\"delord\",\"price\":1,\"quantity\":1,\"discount\":\"0\"}"
//        };
//        for (String dummyDatum : dummyItemData) {
//            this.mockMvc.perform(post("/items")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(dummyDatum));
//        }
//
//        String[] orders = {
//                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
//                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
//                        "\"startDateTime\":1666778124325,\"endDateTime\":1675778124325,\"totalPrice\":12302}",
//                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
//                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
//                        "\"startDateTime\":1667778124325,\"endDateTime\":1675778124325,\"totalPrice\":12312}",
//                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
//                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
//                        "\"startDateTime\":1668778124325,\"endDateTime\":1675778124325,\"totalPrice\":12313}",
//                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
//                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
//                        "\"startDateTime\":1669778124325,\"endDateTime\":1675778124325,\"totalPrice\":12314}",
//                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
//                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
//                        "\"startDateTime\":1675778124325,\"endDateTime\":1675788124325,\"totalPrice\":12315}"
//        };
//        for (String order : orders) {
//            this.mockMvc.perform(post("/orders")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(order));
//        }
//    }

//    @Test
//    @WithUserDetails("Admin")
//    public void deleteInvalidOrderTest() throws Exception {
//
//        String requestBody = "{\"id\": 150,\"customer\":{\"id\":1},\"worker\":{\"id\":2}," +
//                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
//                "\"startDateTime\":1665778114325,\"endDateTime\":1675778114325,\"totalPrice\":12300}";
//        mockMvc.perform(delete("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

    @Test
    @WithUserDetails("Admin")
    public void getAllOrdersTest() throws Exception {
        fillGetAllOrderDummyData();
        mockMvc.perform(get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    private void fillGetAllOrderDummyData() throws Exception {
        String dummyAccountData = "{\"firstName\":\"getAllord\",\"secondName\":\"getAllord\"," +
                "\"phone\":\"getAllord\",\"email\":\"getAllord\"," +
                "\"credentials\":{ \"username\":\"getAllord\",\"password\":\"getAllord\",\"role\":\"USER\"},\"discount\":\"0\"}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyAccountData));
        String dummyCategoryData = "{\"name\": \"getAllord\",\"discount\":\"0\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"getAllord\",\"price\":1,\"quantity\":1,\"discount\":\"0\"}",
                "{\"category\":{\"id\":1\"},\"name\":\"getAllord\",\"price\":1,\"quantity\":1,\"discount\":\"0\"}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":[2023,3,19,16,11,1],\"endDateTime\":[2023,3,22,16,11,1]}";
        this.mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }
}