package eu.senla.aspect;

import eu.senla.utils.ConnectionHolder;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Aspect
@Component
public class TransactionAspect {
    private final ConnectionHolder connectionHolder;
    private Connection connection;

    public TransactionAspect(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Pointcut("@annotation(eu.senla.annotation.Transaction)")
    public void transactionPointCut() {
    }

    @Before("transactionPointCut()")
    void getConnection() {
        try {
             connection = connectionHolder.getTransactionConnection();
        //connectionHolder.getTransactionConnection().setAutoCommit(false);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After("transactionPointCut()")
    void freeConnection() {
            connectionHolder.freeTransactionConnection();
    }

    @AfterReturning("transactionPointCut()")
    void commit() {
            connectionHolder.commit(connection);
          //  connection.commit();
    }

    @AfterThrowing(pointcut = "transactionPointCut()", throwing = "exception")
    void rollback(RuntimeException exception) {
            connectionHolder.rollback(connection);
        //    connection.rollback();
    }
}
