package exceptions.rest;

import org.springframework.http.HttpStatus;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class NotFoundException extends RestBaseException {

    public NotFoundException(String message, String error, String[] internalCause) {
        super(message, error, internalCause);
        this.status = HttpStatus.NOT_FOUND;
    }

    public NotFoundException() {
        super("Unable to find resource", "not_found", new String[0]);
        this.status = HttpStatus.NOT_FOUND;
    }

}
