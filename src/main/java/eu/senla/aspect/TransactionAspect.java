package eu.senla.aspect;

import eu.senla.utils.ConnectionHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Aspect
@Component
public class TransactionAspect {
    private final ConnectionHolder connectionHolder;
    private Connection connection;

    public TransactionAspect(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Around("@annotation(eu.senla.annotation.Transaction)")
    private Object executeTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnValue = null;
        try {
            connection = connectionHolder.getTransactionConnection();
            returnValue = joinPoint.proceed();
            connectionHolder.commit(connection);
        } catch (RuntimeException e) {
            connectionHolder.rollback(connection);
        } finally {
            connectionHolder.freeTransactionConnection();
        }
        return returnValue;
    }

}
