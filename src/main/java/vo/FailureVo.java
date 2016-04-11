package vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 4/11/16.
 */
public class FailureVo {
    private String message;
    private Integer status;
    private String error;
    private List<String> cause;

    public FailureVo(String message, Integer status, String error, List<String> cause) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.cause = cause;
    }

    public FailureVo() {
        this.cause = new ArrayList<String>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getCause() {
        return cause;
    }

    public void setCause(List<String> cause) {
        this.cause = cause;
    }

    public void addCause(String cause){
        this.cause.add(cause);
    }
}
