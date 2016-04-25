package exceptions.rest;

import org.springframework.http.HttpStatus;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class RestBaseException extends RuntimeException {

    HttpStatus status;
    String error;
    String[] internalCause;

    public String[] getInternalCause() {
        return internalCause;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public RestBaseException(String message, String error, String[] cause) {
        super(message);
        this.error = error;
        this.internalCause = cause;
    }

}
