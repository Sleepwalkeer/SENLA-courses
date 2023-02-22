package eu.senla.controllers;

import eu.senla.configuration.Config;
import eu.senla.configuration.ContainersEnvironment;
import eu.senla.configuration.SecurityConfigurationTest;
import eu.senla.configuration.ServletConfigurationTest;
import eu.senla.dao.AccountDao;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;
import eu.senla.entities.Role;
import org.junit.jupiter.api.*;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class, ServletConfigurationTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest extends ContainersEnvironment {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private AccountDao accountDao;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }




    @Test
    @Order(1)
    public void fillDummyData(){
        Account admin = Account.builder().firstName("Admin").secondName("Admin")
                .phone("+3758232734").email("kfgkzsf")
                .credentials(Credentials.builder().username("Sleepwalker").password("escapism").role(Role.ADMIN).build()).build();
        accountDao.save(admin);
        Account user2 =  Account.builder().firstName("User2").secondName("user2")
                .phone("+375823274").email("kfgkzsfdf")
                .credentials(Credentials.builder().username("Sleepwalker2").password("escapism2").role(Role.USER).build()).build();
        accountDao.save(user2);
        Account user3 =  Account.builder().firstName("User3").secondName("user3")
                .phone("+375823wer").email("kfgkzsgd")
                .credentials(Credentials.builder().username("Sleepwalker3").password("escapism3").role(Role.USER).build()).build();
        accountDao.save(user3);
    }



    @Test
    @WithUserDetails("Sleepwalker")
    public void getAccountByIdTest() throws Exception {
        //fillGetAccountDummyData();
        int credentialsId = 1;
        this.mockMvc.perform(get("/accounts/{id}", credentialsId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(credentialsId));
    }

    private void fillGetAccountDummyData() throws Exception {
//        String dummyCredentialsData = "{\"firstName\":\"getacc\",\"secondName\":\"getacc\",\"phone\":\"getacc\"," +
//                "\"email\":\"getacc\",\"credentials\":{ \"username\": \"getacc\"," +
//                " \"password\": \"getacc\" }}";
//        this.mockMvc.perform(post("/accounts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(dummyCredentialsData))
//                .andExpect(MockMvcResultMatchers.status().isOk());
        Account testDaoAcc = Account.builder().firstName("testDaoAccFind2").secondName("testDaoAccFind2")
                .phone("testDaoAccFind2").email("testDaoAccFin2d")
                .credentials(Credentials.builder().username("Sleepwalker").password("escapism").role(Role.ADMIN).build()).build();
        accountDao.save(testDaoAcc);
    }

    @Test
    public void CreateAccountTest() throws Exception {
        String requestBody = "{\"firstName\":\"Mallory\",\"secondName\":\"Cyber\",\"phone\":\"113\",\"email\":\"113\"" +
                ",\"credentials\":{ \"username\": \"Mallory\", \"password\": \"ultrasuperpass\" }}";
        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createInvalidAccountTest() throws Exception {
        String requestBody = "{\"secondName\":\"Cyber\",\"phone\":\"113\",\"email\":\"113\"" +
                ",\"credentials\":{ \"username\": \"Mallory\", \"password\": \"ultrasuerpass\" }}";
        this.mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateAccountTest() throws Exception {
        fillUpdateAccountDummyData();

        String requestBody = "{\"id\":\"1\",\"firstName\":\"updaccnew\",\"secondName\":\"updaccnew\",\"phone\":\"updaccnew\"," +
                "\"email\":\"updaccnew\",\"credentials\":{\"id\":\"1\", \"username\": \"updaccnew\"," +
                " \"password\": \"updaccnew\" }}";
        this.mockMvc.perform(put("/accounts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("updaccnew"));
    }

    private void fillUpdateAccountDummyData() throws Exception {
        String dummyCredentialsData = "{\"firstName\":\"updacc\",\"secondName\":\"updacc\",\"phone\":\"updacc\"," +
                "\"email\":\"updacc\",\"credentials\":{ \"username\": \"updacc\"," +
                " \"password\": \"updacc\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCredentialsData));
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
    public void deleteAccountByIdTest() throws Exception {
        fillDeleteAccountByIdDummyData();
        mockMvc.perform(delete("/accounts/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteAccountByIdDummyData() throws Exception {
        String[] dummyCredentialsData = {
                "{\"firstName\":\"deleteteaccid\",\"secondName\":\"deleteteaccid\",\"phone\":\"deleteteaccid\"," +
                        "\"email\":\"deleteteaccid\",\"credentials\":{ \"username\": \"deleteteaccid\"," +
                        " \"password\": \"deleteteaccid\" }}",
                "{\"firstName\":\"deleteteaccid11\",\"secondName\":\"deleteteaccid11\",\"phone\":\"deleteteaccid11\"," +
                        "\"email\":\"deleteteaccid11\",\"credentials\":{ \"username\": \"deleteteaccid11\"," +
                        " \"password\": \"deleteteaccid11\" }}",
                "{\"firstName\":\"deleteteaccid22\",\"secondName\":\"deleteteaccid22\",\"phone\":\"deleteteaccid22\"," +
                        "\"email\":\"deleteteaccid22\",\"credentials\":{ \"username\": \"deleteteaccid22\"," +
                        " \"password\": \"deleteteaccid22\" }}",
                "{\"firstName\":\"deleteteaccid33\",\"secondName\":\"deleteteaccid33\",\"phone\":\"deleteteaccid33\"," +
                        "\"email\":\"deleteteaccid33\",\"credentials\":{ \"username\": \"deleteteaccid33\"," +
                        " \"password\": \"deleteteaccid33\" }}",
        };
        for (String dummyDatum : dummyCredentialsData) {
            this.mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
    }

    @Test
    public void deleteAccountByInvalidIdTest() throws Exception {
        mockMvc.perform(delete("/accounts/{id}", 500000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteAccountTest() throws Exception {
        fillDeleteAccountDummyData();
        String deleteRequestBody = "{\"id\":\"4\"}";
        mockMvc.perform(delete("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void fillDeleteAccountDummyData() throws Exception {
        String[] dummyCredentialsData = {
                "{\"firstName\":\"deleteacc\",\"secondName\":\"deleteacc\",\"phone\":\"deleteacc\"," +
                        "\"email\":\"deleteacc\",\"credentials\":{ \"username\": \"deleteacc\"," +
                        " \"password\": \"deleteacc\" }}",
                "{\"firstName\":\"deleteacc11\",\"secondName\":\"deleteacc11\",\"phone\":\"deleteacc11\"," +
                        "\"email\":\"deleteacc11\",\"credentials\":{ \"username\": \"deleteacc11\"," +
                        " \"password\": \"deleteacc11\" }}",
                "{\"firstName\":\"deleteacc22\",\"secondName\":\"deleteacc22\",\"phone\":\"deleteacc22\"," +
                        "\"email\":\"deleteacc22\",\"credentials\":{ \"username\": \"deleteacc22\"," +
                        " \"password\": \"deleteacc22\" }}",
                "{\"firstName\":\"deleteacc33\",\"secondName\":\"deleteacc33\",\"phone\":\"deleteacc33\"," +
                        "\"email\":\"deleteacc33\",\"credentials\":{ \"username\": \"deleteacc33\"," +
                        " \"password\": \"deleteacc33\" }}",
        };
        for (String dummyDatum : dummyCredentialsData) {
            this.mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dummyDatum));
        }
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
    public void getAllAccountsTest() throws Exception {
        fillGetAllAccountsDummyData();
        mockMvc.perform(get("/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))));
    }

    private void fillGetAllAccountsDummyData() throws Exception {
        String dummyCredentialsData = "{\"firstName\":\"getAllacc\",\"secondName\":\"getAllacc\",\"phone\":\"getAllacc\"," +
                "\"email\":\"getAllacc\",\"credentials\":{ \"username\": \"getAllacc\"," +
                " \"password\": \"getAllacc\" }}";
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyCredentialsData));
    }
}
