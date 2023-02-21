package eu.senla.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

@Getter
public class InsufficientPrivilegesException extends AccessDeniedException {

    private HttpStatus httpStatus;

    public InsufficientPrivilegesException(String msg) {
        super(msg);
    }

    public InsufficientPrivilegesException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}