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

            System.out.println("взяли транзакшн коннект для потока" + Thread.currentThread().getName());
        //connectionHolder.getTransactionConnection().setAutoCommit(false);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After("transactionPointCut()")
    void freeConnection() {
        System.out.println("пытаемся освободить транзакшн коннект для потока" + Thread.currentThread().getName());
            connectionHolder.freeTransactionConnection();
    }

    @AfterReturning("transactionPointCut()")
    void commit() {
        System.out.println("коммитим коннект для потока" + Thread.currentThread().getName());
            connectionHolder.commit(connection);
          //  connection.commit();
    }

    @AfterThrowing(pointcut = "transactionPointCut()", throwing = "exception")
    void rollback(RuntimeException exception) {
        System.out.println("откатываем коннект для потока" + Thread.currentThread().getName());
            connectionHolder.rollback(connection);
        //    connection.rollback();
    }
}
