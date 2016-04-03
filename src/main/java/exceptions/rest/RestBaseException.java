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

    public void setCause(String[] internalCause) {
        this.internalCause = internalCause;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public RestBaseException(HttpStatus status, String message, String error, String[] internalCause) {
        super(message);
        this.status = status;
        this.error = error;
        this.internalCause = internalCause;
    }

    public RestBaseException(String message, String error, String[] cause) {
        super(message);
        this.error = error;
        this.internalCause = cause;
    }

}
