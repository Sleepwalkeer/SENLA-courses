package eu.senla.exceptions;

public class DatabaseTransactionException extends RuntimeException{
    public DatabaseTransactionException(String message) {
        super(message);
    }
}
