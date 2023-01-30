package eu.senla;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.senla.configuration.Config;
import eu.senla.controllers.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;


public class Application {
    public static void main(String[] args) throws JsonProcessingException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        OrderController orderController = context.getBean(OrderController.class);

        orderDemo(orderController);

        transactionDemo(orderController);

        context.close();
    }

    public static void transactionDemo(OrderController orderController) throws JsonProcessingException {
        System.out.println(orderController.transactionTest());

        Runnable task = () -> {
            try {
                orderController.transactionTest();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }

    public static void orderDemo(OrderController orderController) throws JsonProcessingException {
        String order1 = "{\"id\":1,\"customer\":{\"id\":3,\"firstName\":\"Peter\",\"secondName\":\"Stumps\"," +
                "\"phone\":\"7221220\",\"email\":\"peters@gmail.com\",\"credentials\":{\"id\":10," +
                "\"username\":\"peter\",\"password\":\"peterspassword\"}},\"worker\":{\"id\":2,\"firstName\":\"Hannah\"," +
                "\"secondName\":\"Montana\",\"phone\":\"88005553535\",\"email\":\"hannahs@gmail.com\"," +
                "\"credentials\":{\"id\":20,\"username\":\"hannah\",\"password\":\"hannahspassword\"}}," +
                "\"itemList\":[{\"id\":3,\"category\":{\"id\":10,\"name\":\"toys\"},\"name\":\"Donkey Kong\"," +
                "\"price\":123,\"quantity\":10},{\"id\":2,\"category\":{\"id\":15,\"name\":\"furniture\"}," +
                "\"name\":\"sofa\",\"price\":2500,\"quantity\":3}],\"startDateTime\":1674745816000," +
                "\"endDateTime\":1674918622000,\"totalPrice\":2623}";
        String order2 = "{\"id\":2,\"customer\":{\"id\":3,\"firstName\":\"Peter\",\"secondName\":\"Stumps\"," +
                "\"phone\":\"7221220\",\"email\":\"peters@gmail.com\",\"credentials\":{\"id\":10," +
                "\"username\":\"peter\",\"password\":\"peterspassword\"}},\"worker\":{\"id\":2,\"firstName\":\"Hannah\"," +
                "\"secondName\":\"Montana\",\"phone\":\"88005553535\",\"email\":\"hannahs@gmail.com\"," +
                "\"credentials\":{\"id\":20,\"username\":\"hannah\",\"password\":\"hannahspassword\"}}," +
                "\"itemList\":[{\"id\":3,\"category\":{\"id\":10,\"name\":\"toys\"},\"name\":\"Donkey Kong\"," +
                "\"price\":123,\"quantity\":10},{\"id\":2,\"category\":{\"id\":15,\"name\":\"furniture\"}," +
                "\"name\":\"sofa\",\"price\":2500,\"quantity\":3}],\"startDateTime\":1674745816000," +
                "\"endDateTime\":1674918622000,\"totalPrice\":2623}";
        String order3 = "{\"id\":3,\"customer\":{\"id\":3,\"firstName\":\"Peter\",\"secondName\":\"Stumps\"," +
                "\"phone\":\"7221220\",\"email\":\"peters@gmail.com\",\"credentials\":{\"id\":10," +
                "\"username\":\"peter\",\"password\":\"peterspassword\"}},\"worker\":{\"id\":2,\"firstName\":\"Hannah\"," +
                "\"secondName\":\"Montana\",\"phone\":\"88005553535\",\"email\":\"hannahs@gmail.com\"," +
                "\"credentials\":{\"id\":20,\"username\":\"hannah\",\"password\":\"hannahspassword\"}}," +
                "\"itemList\":[{\"id\":3,\"category\":{\"id\":10,\"name\":\"toys\"},\"name\":\"Donkey Kong\"," +
                "\"price\":123,\"quantity\":10},{\"id\":2,\"category\":{\"id\":15,\"name\":\"furniture\"}," +
                "\"name\":\"sofa\",\"price\":2500,\"quantity\":3}],\"startDateTime\":1674745816000," +
                "\"endDateTime\":1674918622000,\"totalPrice\":2623}";

        System.out.println("order Demo");
        System.out.println("Create order #1");
        System.out.println(orderController.create(order1));
        System.out.println("Create order #2");
        System.out.println(orderController.create(order2));
        System.out.println("Create order #3");
        System.out.println(orderController.create(order3));
        System.out.println("Read order #2");
        System.out.println(orderController.getById(order2));
        System.out.println("Update order #2");
        System.out.println(orderController.update(order2));
        System.out.println("Delete order #3");
        orderController.delete(order3);
        System.out.println("Read all orders");
        List<String> orders = orderController.getAll();
        for (String order : orders) {
            System.out.println(order);
        }
        System.out.println("___________________________________________________________________________________");
    }
}
