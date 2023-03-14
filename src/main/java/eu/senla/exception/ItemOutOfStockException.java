package eu.senla.exception;

public class ItemOutOfStockException extends RuntimeException {

    public ItemOutOfStockException(String message) {
        super(message);
    }
}
