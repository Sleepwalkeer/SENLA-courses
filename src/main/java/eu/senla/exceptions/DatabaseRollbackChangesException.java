package eu.senla.exceptions;

public class DatabaseRollbackChangesException extends RuntimeException {
    public DatabaseRollbackChangesException(String message) {
        super(message);
    }
}
