package exceptions.rest;

import org.springframework.http.HttpStatus;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class InvalidTokenException extends RestBaseException {

    public InvalidTokenException(String message) {
        super(message, "unauthorized", new String[0]);
        this.status = HttpStatus.UNAUTHORIZED;
    }

}