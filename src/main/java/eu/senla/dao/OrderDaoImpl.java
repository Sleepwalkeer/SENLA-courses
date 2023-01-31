package eu.senla.dao;

import eu.senla.exceptions.DatabaseQueryExecutionException;
import eu.senla.utils.ConnectionHolder;
import eu.senla.entities.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDaoImpl implements OrderDao {

    private final ConnectionHolder connectionHolder;

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

    public OrderDaoImpl(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public List<Order> getAll() {
        List<Order> orderList = new ArrayList<>();

        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDER_CUSTOMER_WORKER_CREDS)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setStartDateTime(resultSet.getTimestamp("start_datetime").getTime());
                order.setEndDateTime(resultSet.getTimestamp("end_datetime").getTime());
                order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                Account customer = new Account();
                customer.setId(resultSet.getInt("ci"));
                customer.setFirstName(resultSet.getString("cfn"));
                customer.setSecondName(resultSet.getString("csn"));
                customer.setPhone(resultSet.getString("cp"));
                customer.setEmail(resultSet.getString("ce"));

                Credentials customerCreds = new Credentials();
                customerCreds.setId(resultSet.getInt("cci"));
                customerCreds.setUsername(resultSet.getString("ccu"));
                customerCreds.setPassword(resultSet.getString("ccp"));
                customer.setCredentials(customerCreds);
                order.setCustomer(customer);

                Account worker = new Account();
                worker.setId(resultSet.getInt("wi"));
                worker.setFirstName(resultSet.getString("wfn"));
                worker.setSecondName(resultSet.getString("wsn"));
                worker.setPhone(resultSet.getString("wp"));
                worker.setEmail(resultSet.getString("we"));

                Credentials workerCreds = new Credentials();
                workerCreds.setId(resultSet.getInt("wci"));
                workerCreds.setUsername(resultSet.getString("wcu"));
                workerCreds.setPassword(resultSet.getString("wcp"));
                worker.setCredentials(workerCreds);
                order.setWorker(worker);
                order.setItemList(new ArrayList<>());
                orderList.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ORDERID_ITEMS_CATEGORIES)) {

            for (Order order : orderList) {
                preparedStatement.setInt(1, order.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Item item = new Item();
                    item.setId(resultSet.getInt("ii"));
                    item.setName(resultSet.getString("iname"));
                    item.setPrice(resultSet.getBigDecimal("ip"));
                    item.setQuantity(resultSet.getInt("iq"));
                    Category category = new Category();
                    category.setId(resultSet.getInt("ci"));
                    category.setName(resultSet.getString("cn"));
                    item.setCategory(category);
                    order.getItemList().add(item);
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseQueryExecutionException("Couldn't fetch data from database");
        }
        return orderList;
    }

    public Order getById(Order passedOrder) {
        Order order = new Order();
        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ORDERID_ORDER_CUSTOMER_WORKER_CREDS)) {

            preparedStatement.setInt(1, passedOrder.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                order.setId(resultSet.getInt("id"));
                order.setStartDateTime(resultSet.getTimestamp("start_datetime").getTime());
                order.setEndDateTime(resultSet.getTimestamp("end_datetime").getTime());
                order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                Account customer = new Account();
                customer.setId(resultSet.getInt("ci"));
                customer.setFirstName(resultSet.getString("cfn"));
                customer.setSecondName(resultSet.getString("csn"));
                customer.setPhone(resultSet.getString("cp"));
                customer.setEmail(resultSet.getString("ce"));

                Credentials customerCreds = new Credentials();
                customerCreds.setId(resultSet.getInt("cci"));
                customerCreds.setUsername(resultSet.getString("ccu"));
                customerCreds.setPassword(resultSet.getString("ccp"));
                customer.setCredentials(customerCreds);
                order.setCustomer(customer);

                Account worker = new Account();
                worker.setId(resultSet.getInt("wi"));
                worker.setFirstName(resultSet.getString("wfn"));
                worker.setSecondName(resultSet.getString("wsn"));
                worker.setPhone(resultSet.getString("wp"));
                worker.setEmail(resultSet.getString("we"));

                Credentials workerCreds = new Credentials();
                workerCreds.setId(resultSet.getInt("wci"));
                workerCreds.setUsername(resultSet.getString("wcu"));
                workerCreds.setPassword(resultSet.getString("wcp"));
                worker.setCredentials(workerCreds);
                order.setWorker(worker);
                order.setItemList(new ArrayList<>());
            }
        } catch (SQLException e) {
            throw new DatabaseQueryExecutionException("Couldn't fetch data from database");
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ORDERID_ITEMS_CATEGORIES)) {
            preparedStatement.setInt(1, order.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getInt("ii"));
                item.setName(resultSet.getString("iname"));
                item.setPrice(resultSet.getBigDecimal("ip"));
                item.setQuantity(resultSet.getInt("iq"));
                Category category = new Category();
                category.setId(resultSet.getInt("ci"));
                category.setName(resultSet.getString("cn"));
                item.setCategory(category);
                order.getItemList().add(item);
            }
        } catch (SQLException e) {
            throw new DatabaseQueryExecutionException("Couldn't fetch data from database");
        }
        return order;
    }

    public Order update(Order passedOrder) {
        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TOTAL_PRICE)) {
            preparedStatement.setInt(1, passedOrder.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseQueryExecutionException("Couldn't update row");
        }
        getById(passedOrder);
        return passedOrder;
    }

    public Order create(Order passedOrder) {

        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER)) {

            preparedStatement.setInt(1, passedOrder.getCustomer().getId());
            preparedStatement.setInt(2, passedOrder.getWorker().getId());
            preparedStatement.setTimestamp(3, new Timestamp(passedOrder.getStartDateTime()));
            preparedStatement.setTimestamp(4, new Timestamp(passedOrder.getEndDateTime()));
            preparedStatement.setBigDecimal(5, passedOrder.getTotalPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseQueryExecutionException("Couldn't create row");
        }
        try (PreparedStatement createOrderItems = connection.prepareStatement(CREATE_ORDER_ITEM)) {
            for (Item item : passedOrder.getItemList()) {
                createOrderItems.setInt(1, passedOrder.getId());
                createOrderItems.setInt(2, item.getId());
                createOrderItems.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseQueryExecutionException("Couldn't create row");
        }
        return passedOrder;
    }

    public void delete(Order passedOrder) {
        Connection connection = connectionHolder.getConnection();
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
                preparedStatement.setInt(1, passedOrder.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseQueryExecutionException("Couldn't delete row");
            }
        }
    }
}
