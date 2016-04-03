package exceptions.web;

import org.springframework.http.HttpStatus;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class WebBaseException extends RuntimeException {

    HttpStatus status;
    String error;

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

    public WebBaseException(HttpStatus status, String message, String error) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public WebBaseException(String message, String error) {
        super(message);
        this.error = error;
    }

}
