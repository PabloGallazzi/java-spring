package exceptions.rest;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class ExceptionMapper {

    String message;
    int status;
    String error;
    String[] cause;

    public String[] getCause() {
        return cause;
    }

    public void setCause(String[] internalCause) {
        this.cause = internalCause;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ExceptionMapper(RestBaseException e){
        this.message = e.getMessage();
        this.status = e.getStatus().value();
        this.error = e.getError();
        this.cause = e.getInternalCause();
    }

}
