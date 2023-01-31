package eu.senla.exceptions;

public class DatabaseCommitChangesException extends RuntimeException{
    public DatabaseCommitChangesException(String message) {
        super(message);
    }
}
