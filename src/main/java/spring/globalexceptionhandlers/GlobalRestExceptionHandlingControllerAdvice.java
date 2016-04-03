package spring.globalexceptionhandlers;

import exceptions.rest.ExceptionMapper;
import exceptions.rest.RestBaseException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by pgallazzi on 3/4/16.
 */
@ControllerAdvice
@RestController
public class GlobalRestExceptionHandlingControllerAdvice {

    @ExceptionHandler(RestBaseException.class)
    public ResponseEntity<?> handleError(HttpServletRequest req, Exception exception)
            throws Exception {

        // Rethrow annotated exceptions or they will be processed here instead.
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
            throw exception;
        }

        ExceptionMapper exceptionMapper = new ExceptionMapper((RestBaseException) exception);
        return new ResponseEntity<>(exceptionMapper, null, ((RestBaseException) exception).getStatus());

    }

}
