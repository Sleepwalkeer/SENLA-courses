package eu.senla.dao;

import eu.senla.exceptions.DatabaseQueryExecutionException;
import eu.senla.entities.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDaoImpl implements OrderDao {


    private final static String CREATE_ORDER = "INSERT INTO rent_order" +
            "(customer_id, worker_id, start_datetime, end_datetime, total_price) VALUES (?,?,?,?,?)";

    private final static String CREATE_ORDER_ITEM = "INSERT INTO order_item VALUES (?,?);";

    private final static String SELECT_ALL_ORDER_CUSTOMER_WORKER_CREDS = """
            select rent_order.id, rent_order.start_datetime, rent_order.end_datetime, rent_order.total_price,
                   cstm.id as ci, cstm.first_name as cfn, cstm.second_name as csn, cstm.phone as cp, cstm.email as ce,
                   cCreds.id as cci, cCreds.username as ccu, cCreds.password as ccp,
                   wrkr.id as wi, wrkr.first_name AS wfn, wrkr.second_name as wsn, wrkr.phone as wp, wrkr.email as we,
                   wCreds.id as wci, wCreds.username as wcu, wCreds.password as wcp
            from rent_order
               inner join account cstm on rent_order.customer_id = cstm.id
               inner join credentials cCreds on cstm.id = cCreds.id
               inner join account wrkr on wrkr.id = rent_order.worker_id
               inner join credentials wCreds on wrkr.id = wCreds.id;
            """;

    private final static String SELECT_BY_ORDERID_ITEMS_CATEGORIES = """
                        select rent_order.id,
                   item.id       as ii,
                   item.name     as iname,
                   item.price    as ip,
                   item.quantity as iq,
                   category.id   as ci,
                   category.name as cn
            from rent_order
                     inner join order_item ON rent_order.id = order_item.order_id
                     inner join item ON order_item.item_id = item.id
                     inner join category on category.id = item.category_id
            where rent_order.id = ?;""";

    private final static String SELECT_BY_ORDERID_ORDER_CUSTOMER_WORKER_CREDS = """
            select rent_order.id, rent_order.start_datetime, rent_order.end_datetime, rent_order.total_price,
                   cstm.id as ci, cstm.first_name as cfn, cstm.second_name as csn, cstm.phone as cp, cstm.email as ce,
                   cCreds.id as cci, cCreds.username as ccu, cCreds.password as ccp,
                   wrkr.id as wi, wrkr.first_name AS wfn, wrkr.second_name as wsn, wrkr.phone as wp, wrkr.email as we,
                   wCreds.id as wci, wCreds.username as wcu, wCreds.password as wcp
            from rent_order
                     inner join account cstm on rent_order.customer_id = cstm.id
                     inner join credentials cCreds on cstm.id = cCreds.id
                     inner join account wrkr on wrkr.id = rent_order.worker_id
                     inner join credentials wCreds on wrkr.id = wCreds.id
            where rent_order.id = ?;""";

    private final static String DELETE_BY_ID = "delete from rent_order  where rent_order.id = ? ";

    private final static String UPDATE_TOTAL_PRICE = """
            UPDATE rent_order
            set total_price = (select sum(item.price)
                               from order_item,
                                    item
                               where rent_order.id = order_item.order_id
                                 AND order_item.item_id = item.id)
            where id = ?;""";


    public List<Order> getAll() {
        List<Order> orderList = new ArrayList<>();
        return orderList;
    }

    public Order getById(Order passedOrder) {
        Order order = new Order();

        return order;
    }

    public Order update(Order passedOrder) {

        return  getById(passedOrder);
    }

    public Order create(Order passedOrder) {

        return passedOrder;
    }

    public void delete(Order passedOrder) {

    }
}
