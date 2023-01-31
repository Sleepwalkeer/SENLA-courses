package eu.senla.aspect;

import eu.senla.utils.ConnectionHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Slf4j
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

        try {
            connection = connectionHolder.getTransactionConnection();
            Object returnValue = joinPoint.proceed();
            connectionHolder.commit(connection);
            return returnValue;
        } catch (RuntimeException e) {
            connectionHolder.rollback(connection);
            throw e;
        } catch (Exception e) {
            connectionHolder.commit(connection);
            log.error("Non-SQL exception encountered during transaction execution");
            throw new RuntimeException("Non-SQL exception encountered during transaction execution");
        } finally {
            connectionHolder.freeTransactionConnection();
        }
    }

}
