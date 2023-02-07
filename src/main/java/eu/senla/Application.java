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
        AccountDao accountDao = context.getBean(AccountDao.class);
        CategoryDao categoryDao = context.getBean(CategoryDao.class);
        CredentialsDao credentialsDao = context.getBean(CredentialsDao.class);
        OrderDao orderDao = context.getBean(OrderDao.class);
        ItemDao itemDao = context.getBean(ItemDao.class);

//        Category category = new Category("Construction equipment");
//        Category category1 = new Category("Real estate");
//        Category category2 = new Category("Vehicles");
//        Category category3 = new Category("ForDeletion");
//
//        categoryDao.save(category);
//        categoryDao.save(category1);
//        categoryDao.save(category2);
//        categoryDao.save(category3);
//        categoryDao.deleteById(4);
//       List<Category> categories =  categoryDao.findAll();
//        Item jackhammer = new Item(categories.get(0), "Jackhammer", new BigDecimal(750), 8);
//        Item angle_grinder = new Item(categories.get(0), "Angle grinder", new BigDecimal(600), 15);
//        Item item = new Item(categories.get(1), "2-bedroom app", new BigDecimal(3450), 2);
//        Item lamborghini = new Item(categories.get(2), "Lamborghini", new BigDecimal(6300), 1);
//
//        itemDao.save(jackhammer);
//        itemDao.save(angle_grinder);
//        itemDao.save(item);
//        itemDao.save(lamborghini);

        Account account = new Account("Mallory",
                "Kay","+375298201846","malory.kay.p@gmail.com",
                new Credentials("MalloryKayP","kz25bj2jk23r"));
        accountDao.save(account);

        Account account3 = new Account("bla",
                "blya","+375323924","@gmail.com",
                new Credentials("esculap","ngfdspds453q2f"));


        List<Item>  itemList = itemDao.findAll();

        Order order1 = new Order(new Account(1),new Account(2), itemList,
                new Timestamp(1675681474095L),new Timestamp(1675711474095L),new BigDecimal(50));
        orderDao.save(order1);

        Order order = orderDao.findByIdEager(1);
       List<Item>  test = order.getItems();
        System.out.println(test);
 //       System.out.println(orderDao.findByIdEager(1));
//        List<Item> itemList = itemDao.findAll();
//        Set<Item> itemSet  = new HashSet<>();
//        itemSet.addAll(itemList);
//
//        List<Item> itemList2 = itemDao.findAll();
//        itemList2.remove(2);
//        Set<Item> itemSet2  = new HashSet<>();
//        itemSet2.addAll(itemList2);
//
//        List<Account> accountList = accountDao.findAll();

//
//        Order order2  = new Order(accountList.get(0),accountList.get(1),itemSet2,
//                new Timestamp(1675661474095L),new Timestamp(1675710474095L),new BigDecimal(50));
//        orderDao.save(order2);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
//        Order orderTest = orderDao.findById(1);
//        System.out.println(orderTest);
//        OrderService orderService = context.getBean(OrderService.class);
//        System.out.println(orderService.getById(orderDto));
//        System.out.println();

//        Item item1 = new Item(1);
//        Item item2 = new Item(2);
//        Item item3 = new Item(3);
//        Item item4 = new Item(4);
//        List<Item> itemList = itemDao.findAll();
//        Account account = new Account(2);
//        Account account1 = new Account(6);
//
//        Set<Item> itemSet = new HashSet<>();
//        itemSet.addAll(itemList);
//
//        Order order = new Order(account,account1,itemSet,new Timestamp(1675704792064L),new Timestamp(1675706592064L),new BigDecimal(500));
//orderDao.save(order);
//        //orderDao.deleteById(4);
//       // System.out.println();
//        Account account = accountDao.findById(2);
//        System.out.println(account);
//
//        CredentialsDao credentialsDao = context.getBean(CredentialsDao.class);
//        Credentials credentials = new Credentials(2, "Jake", "pass");
//        credentialsDao.save(credentials);
//
//        CategoryDao categoryDao = context.getBean(CategoryDao.class);
//        ItemService itemService = context.getBean(ItemService.class);
//           itemDao.save(item);
//        System.out.println(categoryDao.findAll());
//        System.out.println(itemService.getAll());
//        item1.setCategory(itemDao.getCategory(item1));
//        System.out.println(itemDao.findAll());
        context.close();
    }

}
