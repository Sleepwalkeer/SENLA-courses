package eu.senla.utils;

import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.DatabaseCommitChangesException;
import eu.senla.exceptions.DatabaseConnectionException;
import eu.senla.exceptions.DatabaseRollbackChangesException;
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
        System.out.println(currentThreadName);
        if (transactionConnectionMap.containsKey(currentThreadName)) {
            return transactionConnectionMap.get(currentThreadName);
        } else {
            Connection newConnection = createConnection();
            try {
                newConnection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new DatabaseAccessException("Database access error occurred. Couldn't set auto commit to false.");
            }
            transactionConnectionMap.put(currentThreadName, newConnection);
            return newConnection;
        }
    }

    public Connection getConnection() {
        String currentThreadName = Thread.currentThread().getName();
        if (transactionConnectionMap.containsKey(currentThreadName)) {
            return transactionConnectionMap.get(currentThreadName);
        } else {
            for (Connection connection : connectionList) {
                if (!transactionConnectionMap.containsValue(connection)) {
                    return connection;
                }
            }
            return createConnection();
        }
    }

    public void freeTransactionConnection() {
        String currentThreadName = Thread.currentThread().getName();
        transactionConnectionMap.remove(currentThreadName);

    }

    public Connection createConnection() {
        try {
            Connection connection = dataSource.getConnection();
            connectionList.add(connection);
            return connection;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Couldn't obtain a connection from the data source");
        }
    }

    public void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseCommitChangesException("Couldn't commit changes");
        }
    }

    public void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DatabaseRollbackChangesException("Couldn't rollback changes");
        }
    }

    @PreDestroy
    private void onDestroy() {
        for (Connection connection : connectionList) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DatabaseAccessException("Database access error occured");
            }
        }
    }

}

