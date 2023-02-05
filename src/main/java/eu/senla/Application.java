package eu.senla;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.senla.configuration.Config;
import eu.senla.controllers.OrderController;
import eu.senla.dao.CategoryDao;
import eu.senla.entities.Category;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.List;


public class Application {
    public static void main(String[] args) throws JsonProcessingException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        OrderController orderController = context.getBean(OrderController.class);
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        CategoryDao categoryDao = context.getBean(CategoryDao.class);
       // Category category = new Category(6,"Construction");
       // categoryDao.save(category);
        System.out.println(categoryDao.findAll());
        //orderDemo(orderController);

        //transactionDemo(orderController);

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
}
