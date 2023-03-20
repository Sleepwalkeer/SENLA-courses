package eu.senla.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        log.error("Not Found Exception " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<String> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        log.error("Access denied " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        log.error("Bad Request Exception " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {

        log.error("Access Denied Exception" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You do not have sufficient privileges to perform this operation. " + ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticatonException(AuthenticationException ex) {
        System.out.println(getClass().getResource("/logback.xml"));
        log.info("Hello, world!");
        log.error("Authentication Exception " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Login failed. Invalid username or password. " + ex.getMessage());
    }

    @ExceptionHandler(ItemOutOfStockException.class)
    public ResponseEntity<String> handleItemOutOfStockException(ItemOutOfStockException ex) {
        log.error("ItemOutOfStockException " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage() + " Please, remove this item from your order");
    }

    @ExceptionHandler(InvalidFilterKeyException.class)
    public ResponseEntity<String> handleInvalidFilterKeyException(InvalidFilterKeyException ex) {
        log.error("Invalid filter key exception " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        log.error("Unknown error occured " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage() + "An internal error occurred");
    }
}
