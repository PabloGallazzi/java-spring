package exceptions.rest;

import org.springframework.http.HttpStatus;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class InternalServerError extends RestBaseException {

    public InternalServerError() {
        super("Somethig went wrong!", "internal_error", new String[0]);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
