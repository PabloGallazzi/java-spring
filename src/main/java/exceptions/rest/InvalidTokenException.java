package exceptions.rest;

import org.springframework.http.HttpStatus;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class InvalidTokenException extends RestBaseException {

    public InvalidTokenException(String message, String error, String[] internalCause) {
        super(message, error, internalCause);
        this.status = HttpStatus.UNAUTHORIZED;
    }
    public InvalidTokenException(String message, String error) {
        super(message, error, new String[0]);
        this.status = HttpStatus.UNAUTHORIZED;
    }

    public InvalidTokenException(String message, String[] internalCause) {
        super(message, "unauthorized", internalCause);
        this.status = HttpStatus.UNAUTHORIZED;
    }

    public InvalidTokenException(String message) {
        super(message, "unauthorized", new String[0]);
        this.status = HttpStatus.UNAUTHORIZED;
    }

}