package exceptions.rest;

import org.springframework.http.HttpStatus;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class BadRequestException  extends RestBaseException {

    public BadRequestException(String message, String error, String[] internalCause) {
        super(message, error, internalCause);
        this.status = HttpStatus.BAD_REQUEST;
    }

}