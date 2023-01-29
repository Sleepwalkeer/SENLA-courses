package eu.senla.utils;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConnectionHolder {

    private final DataSource dataSource;
    private final Map<String, Connection> connectionMap = new HashMap<>();

    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        String currentThreadName = Thread.currentThread().getName();
        if (connectionMap.containsKey(currentThreadName)) {
            Connection connection = connectionMap.get(currentThreadName);
            try {
                if (!connection.isClosed()) {
                    System.out.println("Это коннект из мапы для потока " + currentThreadName);
                    return connection;
                } else {
                    System.out.println("Коннект для потока " + currentThreadName + "  был закрыт, нужен новый");
                    connectionMap.remove(currentThreadName);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        Connection newConnection = createConnection();
        connectionMap.put(currentThreadName, newConnection);
        System.out.println("Создан новый коннект для потока " + currentThreadName);
        return newConnection;
    }

    private Connection createConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

