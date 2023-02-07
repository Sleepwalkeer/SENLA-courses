package eu.senla;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.senla.configuration.Config;
import eu.senla.controllers.OrderController;
import eu.senla.dao.*;
import eu.senla.dto.OrderDto;
import eu.senla.entities.*;
import eu.senla.services.ItemService;
import eu.senla.services.ItemServiceImpl;
import eu.senla.services.OrderService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Application {
    public static void main(String[] args) throws JsonProcessingException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        context.close();
    }

}
