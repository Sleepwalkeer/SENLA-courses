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

    public TransactionAspect(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Pointcut("@annotation(eu.senla.annotation.Transaction)")
    public void transactionPointCut() {
    }

    @Before("transactionPointCut()")
    void getConnection() {
        try {
        connectionHolder.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After("transactionPointCut()")
    void closeConnection() {
        try {
            connectionHolder.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterReturning("transactionPointCut()")
    void commit() {
        try {
            connectionHolder.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterThrowing(pointcut = "transactionPointCut()", throwing = "exception")
    void rollback(RuntimeException exception) {
        try {
            connectionHolder.getConnection().rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
