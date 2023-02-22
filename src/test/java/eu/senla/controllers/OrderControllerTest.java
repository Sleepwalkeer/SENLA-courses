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
public class OrderControllerTest extends ContainersEnvironment {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountDao accountDao;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
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
                    .credentials(Credentials.builder().username("Sleepwalker").password("escapism").role(Role.ADMIN).build()).build();
            accountDao.save(admin);
        }
        if (accountDao.findByEmail("kfgkzsfdf").isEmpty()) {
            Account user2 = Account.builder().firstName("User2").secondName("user2")
                    .phone("+375823274").email("kfgkzsfdf")
                    .credentials(Credentials.builder().username("Sleepwalker2").password("escapism2").role(Role.USER).build()).build();
            accountDao.save(user2);
        }
        if (accountDao.findByEmail("kfgkzsddgd").isEmpty()) {
            Account user3 = Account.builder().firstName("User3").secondName("user3")
                    .phone("+375823wer").email("kfgkzsddgd")
                    .credentials(Credentials.builder().username("Sleepwalker3").password("escapism3").role(Role.USER).build()).build();
            accountDao.save(user3);
        }
    }

    @Test
    public void getOrderByIdTest() throws Exception {
        fillGetOrderByIdDummyData();
        int credentialsId = 1;
        this.mockMvc.perform(get("/credentials/{id}", credentialsId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credentialsId));
    }

    private void fillGetOrderByIdDummyData() throws Exception {
        String dummyAccountData = "{\"firstName\":\"getord\",\"secondName\":\"getord\",\"phone\":\"getord\",\"email\":\"getord\"," +
                "\"credentials\":{ \"username\": \"getord\", \"password\": \"getord\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyAccountData));
        String dummyCategoryData = "{\"name\": \"getord\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"getord\",\"price\":1,\"quantity\":1}",
                "{\"category\":{\"id\":1\"},\"name\":\"getord\",\"price\":1,\"quantity\":1}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":1665778114325,\"endDateTime\":1675778114325,\"totalPrice\":12300}";
        this.mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }


    @Test
    public void testCreateOrder() throws Exception {
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
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":1667778114323,\"endDateTime\":1665778114323,\"totalPrice\":12200}";
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateOrderTest() throws Exception {
        fillUpdateOrderDummyData();
        String requestBody = "{\"id\": 1,\"customer\":{\"id\":\"1\",\"firstName\":\"updor\",\"secondName\":\"updord\",\"phone\":\"updor\"," +
                "\"email\":\"updor\",\"credentials\":{\"id\":\"1\", \"username\": \"updor\", \"password\": \"updor\" }}" +
                ",\"worker\":{\"id\":1,\"firstName\":\"updord\",\"secondName\":\"updord\",\"phone\":\"updord\",\"email\":\"updord\"," +
                "\"credentials\":{\"id\":\"1\", \"username\": \"updord\", \"password\": \"updord\" }}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":2}}]," +
                "\"startDateTime\":1665778114200,\"endDateTime\":1675778114300,\"totalPrice\":17300}";
        this.mockMvc.perform(put("/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value("17300"));
    }

    private void fillUpdateOrderDummyData() throws Exception {
        String dummyAccountData = "{\"firstName\":\"updordc\",\"secondName\":\"updordc\",\"phone\":\"updordc\",\"email\":\"updordc\"," +
                "\"credentials\":{ \"username\": \"updordc\", \"password\": \"updordc\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyAccountData));
        String dummyCategoryData = "{\"name\": \"updord\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"updord\",\"price\":1,\"quantity\":1}",
                "{\"category\":{\"id\":1\"},\"name\":\"updord\",\"price\":1,\"quantity\":1}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":1665778124325,\"endDateTime\":1675778124325,\"totalPrice\":12300}";
        this.mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
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
    public void deleteOrderByIdTest() throws Exception {
        fillDeleteOrderByIdDummyData();
        mockMvc.perform(delete("/orders/{id}", 4))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteOrderByIdDummyData() throws Exception {
        String dummyAccountData = "{\"firstName\":\"delidord\",\"secondName\":\"delidord\",\"phone\":\"delidord\",\"email\":\"delidord\"," +
                "\"credentials\":{ \"username\": \"delidord\", \"password\": \"delidord\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyAccountData));
        String dummyCategoryData = "{\"name\": \"delidord\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"delidord\",\"price\":1,\"quantity\":1}",
                "{\"category\":{\"id\":1\"},\"name\":\"delidord\",\"price\":1,\"quantity\":1}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }

        String[] orders = {
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1666778124325,\"endDateTime\":1675778124325,\"totalPrice\":12301}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1667778124325,\"endDateTime\":1675778124325,\"totalPrice\":12302}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1668778124325,\"endDateTime\":1675778124325,\"totalPrice\":12303}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1669778124325,\"endDateTime\":1675778124325,\"totalPrice\":12304}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1675778124325,\"endDateTime\":1675788124325,\"totalPrice\":12305}"
        };
        for (String order : orders) {
            this.mockMvc.perform(post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(order));
        }
    }

    @Test
    public void deleteOrderByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/orders/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteOrderTest() throws Exception {
        fillDeleteOrderDummyData();
        String deleteRequestBody = "{\"id\":\"5\"}";
        mockMvc.perform(delete("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteOrderDummyData() throws Exception {
        String dummyAccountData = "{\"firstName\":\"delord\",\"secondName\":\"delord\",\"phone\":\"delord\",\"email\":\"delord\"," +
                "\"credentials\":{ \"username\": \"delord\", \"password\": \"delord\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyAccountData));
        String dummyCategoryData = "{\"name\": \"delord\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"delord\",\"price\":1,\"quantity\":1}",
                "{\"category\":{\"id\":1\"},\"name\":\"delord\",\"price\":1,\"quantity\":1}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }

        String[] orders = {
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1666778124325,\"endDateTime\":1675778124325,\"totalPrice\":12302}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1667778124325,\"endDateTime\":1675778124325,\"totalPrice\":12312}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1668778124325,\"endDateTime\":1675778124325,\"totalPrice\":12313}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1669778124325,\"endDateTime\":1675778124325,\"totalPrice\":12314}",
                "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                        "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                        "\"startDateTime\":1675778124325,\"endDateTime\":1675788124325,\"totalPrice\":12315}"
        };
        for (String order : orders) {
            this.mockMvc.perform(post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(order));
        }
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
        fillGetAllOrderDummyData();
        mockMvc.perform(get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    private void fillGetAllOrderDummyData() throws Exception {
        String dummyAccountData = "{\"firstName\":\"getAllord\",\"secondName\":\"getAllord\",\"phone\":\"getAllord\",\"email\":\"getAllord\"," +
                "\"credentials\":{ \"username\": \"getAllord\", \"password\": \"getAllord\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyAccountData));
        String dummyCategoryData = "{\"name\": \"getAllord\"}";
        this.mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCategoryData));

        String[] dummyItemData = {
                "{\"category\":{\"id\":1\"},\"name\":\"getAllord\",\"price\":1,\"quantity\":1}",
                "{\"category\":{\"id\":1\"},\"name\":\"getAllord\",\"price\":1,\"quantity\":1}"
        };
        for (String dummyDatum : dummyItemData) {
            this.mockMvc.perform(post("/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
        String requestBody = "{\"customer\":{\"id\":1},\"worker\":{\"id\":1}," +
                "\"items\":[{\"id\":1,\"category\":{\"id\":1}},{\"id\":2,\"category\":{\"id\":1}}]," +
                "\"startDateTime\":1665778124325,\"endDateTime\":1675778124325,\"totalPrice\":12600}";
        this.mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }
}