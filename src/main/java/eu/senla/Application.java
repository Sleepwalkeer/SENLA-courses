package eu.senla;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.senla.configuration.Config;
import eu.senla.controllers.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;


public class Application {
    public static void main(String[] args) throws JsonProcessingException {

        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        AccountController accountController = context.getBean(AccountController.class);
        accountDemo(accountController);

        CategoryController categoryController = context.getBean(CategoryController.class);
        categoryDemo(categoryController);

        CredentialsController credentialsController = context.getBean(CredentialsController.class);
        credentialsDemo(credentialsController);

        ItemController itemController = context.getBean(ItemController.class);
        itemDemo(itemController);

        OrderController orderController = context.getBean(OrderController.class);
        orderDemo(orderController);


    }

    public static void accountDemo(AccountController accountController) throws JsonProcessingException {
        String account1 = "{\"id\":1,\"firstName\":\"Spencer\",\"secondName\":\"Muddock\"," +
                "\"phone\":\"331732945\",\"email\":\"stabtest@gmail.com\",\"credentials\":{\"id\":1," +
                "\"username\":\"Spencer1982\",\"password\":\"12345\"}}";
        String account2 = "{\"id\":2,\"firstName\":\"Anne\",\"secondName\":\"Hathaway\"," +
                "\"phone\":\"291332105\",\"email\":\"brandnewemail@gmail.com\",\"credentials\":{\"id\":2," +
                "\"username\":\"HathGoneAway\",\"password\":\"ultrasuperstrongpassword\"}}";
        System.out.println("account Demo");
        System.out.println("Create account #1");
        System.out.println(accountController.create(account1));
        System.out.println("Create account #2");
        System.out.println(accountController.create(account2));
        System.out.println("Read account #1");
        System.out.println(accountController.getById(account1));
        System.out.println("Update account #1");
        System.out.println(accountController.update(account1, "+375"));
        System.out.println("Delete account #2");
        accountController.delete(account2);
        System.out.println("Read all accounts");
        System.out.println(accountController.getAll());
        System.out.println("____________________________________________________________");
        System.out.println();
    }

    public static void categoryDemo(CategoryController categoryController) throws JsonProcessingException {
        String category1 = "{\"id\":1,\"name\":\"office suppliers\"}";
        String category2 = "{\"id\":2,\"name\":\"auto parts\"}";
        System.out.println("Category Demo");
        System.out.println("Create category #1");
        System.out.println(categoryController.create(category1));
        System.out.println("Create category #2");
        System.out.println(categoryController.create(category2));
        System.out.println("Read category #2");
        System.out.println(categoryController.getById(category2));
        System.out.println("Update category #2");
        System.out.println(categoryController.update(category2, "car parts"));
        System.out.println("Delete category #1");
        categoryController.delete(category1);
        System.out.println("Read all categories");
        System.out.println(categoryController.getAll());
        System.out.println("____________________________________________________________");
        System.out.println();
    }

    public static void credentialsDemo(CredentialsController credentialsController) throws JsonProcessingException {
        String credentials1 = "{\"id\":1,\"username\":\"user1\",\"password\":\"pass1\"}";
        String credentials2 = "{\"id\":2,\"username\":\"user2\",\"password\":\"pass2\"}";
        System.out.println("Credentials Demo");
        System.out.println("Create credentials #1");
        System.out.println(credentialsController.create(credentials1));
        System.out.println("Create credentials #2");
        System.out.println(credentialsController.create(credentials2));
        System.out.println("Read credentials #2");
        System.out.println(credentialsController.getById(credentials2));
        System.out.println("Update credentials #2");
        System.out.println(credentialsController.update(credentials2, "brandNewPassword"));
        System.out.println("Delete credentials #1");
        credentialsController.delete(credentials1);
        System.out.println("Read all credentials");
        System.out.println(credentialsController.getAll());
        System.out.println("____________________________________________________________");
        System.out.println();
    }

    public static void itemDemo(ItemController itemController) throws JsonProcessingException {
        String item1 = "{\"id\":1,\"category\":{\"id\":1,\"name\":\"clothes\"}," +
                "\"name\":\"Mallory's pants\",\"price\":10000000,\"quantity\":3}";
        String item2 = "{\"id\":2,\"category\":{\"id\":1,\"name\":\"footwear\"}," +
                "\"name\":\"Mallory's boots\",\"price\":3252,\"quantity\":1}";
        System.out.println("Item Demo");
        System.out.println("Create Item #1");
        System.out.println(itemController.create(item1));
        System.out.println("Create Item #2");
        System.out.println(itemController.create(item2));
        System.out.println("Read Item #2");
        System.out.println(itemController.getById(item2));
        System.out.println("Update Item #2");
        System.out.println(itemController.update(item2, 22222));
        System.out.println("Delete Item #1");
        itemController.delete(item1);
        System.out.println("Read all Items");
        System.out.println(itemController.getAll());
        System.out.println("____________________________________________________________");
        System.out.println();
    }

    public static void orderDemo(OrderController orderController) throws JsonProcessingException {
        String order1 = "{\"id\":1,\"customer\":{\"id\":10,\"firstName\":\"Peter\",\"secondName\":\"Stumps\"," +
                "\"phone\":\"7221220\",\"email\":\"peters@gmail.com\",\"credentials\":{\"id\":10," +
                "\"username\":\"peter\",\"password\":\"peterspassword\"}},\"worker\":{\"id\":20,\"firstName\":\"Hannah\"," +
                "\"secondName\":\"Montana\",\"phone\":\"88005553535\",\"email\":\"hannahs@gmail.com\"," +
                "\"credentials\":{\"id\":20,\"username\":\"hannah\",\"password\":\"hannahspassword\"}}," +
                "\"itemList\":[{\"id\":5,\"category\":{\"id\":10,\"name\":\"toys\"},\"name\":\"Donkey Kong\"," +
                "\"price\":123,\"quantity\":10},{\"id\":6,\"category\":{\"id\":15,\"name\":\"furniture\"}," +
                "\"name\":\"sofa\",\"price\":2500,\"quantity\":3}],\"startDateTime\":12124124114341," +
                "\"endDateTime\":2002492382343243244,\"totalPrice\":2623}";
        String order2 = "{\"id\":2,\"customer\":{\"id\":11,\"firstName\":\"Jim\",\"secondName\":\"Carver\"," +
                "\"phone\":\"1234567\",\"email\":\"@mail.ru\",\"credentials\":{\"id\":11,\"username\":\"jim\"," +
                "\"password\":\"jimspassword\"}},\"worker\":{\"id\":21,\"firstName\":\"Mozie\",\"secondName\":\"Brasthle\"," +
                "\"phone\":\"8803535\",\"email\":\"@gmail.com\",\"credentials\":{\"id\":21,\"username\":\"mozie\"," +
                "\"password\":\"moziespassword\"}},\"itemList\":[{\"id\":6,\"category\":{\"id\":11,\"name\":\"industrial goods\"}," +
                "\"name\":\"excavator\",\"price\":12300,\"quantity\":1},{\"id\":7,\"category\":{\"id\":12," +
                "\"name\":\"houses\"},\"name\":\"3-bedroom aps\",\"price\":250000,\"quantity\":1}]," +
                "\"startDateTime\":23424124114341,\"endDateTime\":20492382343243244,\"totalPrice\":262300}";
        System.out.println("order Demo");
        System.out.println("Create order #1");
        System.out.println(orderController.create(order1));
        System.out.println("Create order #2");
        System.out.println(orderController.create(order2));
        System.out.println("Read order #2");
        System.out.println(orderController.getById(order2));
        System.out.println("Update order #2");
        System.out.println(orderController.update(order2, new BigDecimal(0)));
        System.out.println("Delete order #1");
        orderController.delete(order1);
        System.out.println("Read all orders");
        System.out.println(orderController.getAll());
        System.out.println("____________________________________________________________");
        System.out.println();
    }

}