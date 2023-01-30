package eu.senla.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConnectionHolder {

    private final DataSource dataSource;
    private final List<Connection> connectionList = new ArrayList<>();
    private final Map<String, Connection> transactionConnectionMap = new HashMap<>();

    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getTransactionConnection() {
        String currentThreadName = Thread.currentThread().getName();
        if (transactionConnectionMap.containsKey(currentThreadName)) {
            System.out.println("такой траназкшн коннект уже есть для потока, отдаем" + Thread.currentThread().getName());
            return transactionConnectionMap.get(currentThreadName);
        } else {
            Connection newConnection = createConnection();
            System.out.println("для этого потока нет транзакшн коннекта, создаем" + Thread.currentThread().getName());
            transactionConnectionMap.put(currentThreadName, newConnection);
            return newConnection;
        }
    }

    public Connection getConnection() {
        String currentThreadName = Thread.currentThread().getName();
        if (transactionConnectionMap.containsKey(currentThreadName)) {
            System.out.println("отдаем потоку его же коннект, он есть в транзакции" + Thread.currentThread().getName());
            return transactionConnectionMap.get(currentThreadName);
        } else {
            for (Connection connection : connectionList) {
                if (!transactionConnectionMap.containsValue(connection)) {
                    System.out.println("отдаем любой не транзакционный поток из списка" + Thread.currentThread().getName());
                    return connection;
                }
            }
            System.out.println("ни одного открытого потока нет, создаем новый");
            return createConnection();
        }
    }

    public void freeTransactionConnection() {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println("транзакция отработала, разрешаем поток брать всем");
        transactionConnectionMap.remove(currentThreadName);

    }

    public Connection createConnection() {
        try {
            Connection connection = dataSource.getConnection();
            connectionList.add(connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void onDestroy() {
        for (Connection connection : connectionList) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

