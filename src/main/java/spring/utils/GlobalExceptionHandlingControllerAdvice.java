package spring.utils;

import exceptions.rest.ExceptionMapper;
import exceptions.rest.InternalServerError;
import exceptions.rest.RestBaseException;
import exceptions.web.WebBaseException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by pgallazzi on 3/4/16.
 */
@ControllerAdvice
public class GlobalExceptionHandlingControllerAdvice {

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExceptionError(HttpServletRequest req, Exception exception)
            throws Exception {

        // Rethrow annotated exceptions or they will be processed here instead.
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
            throw exception;
        }

        // Rethrow custom exceptions or they will be processed here instead.
        if (exception instanceof RestBaseException || exception instanceof WebBaseException) {
            throw exception;
        }

        return new ResponseEntity<>(new ExceptionMapper(new InternalServerError()), null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(WebBaseException.class)
    public ModelAndView handleError(HttpServletRequest req, Exception exception, HttpServletResponse httpServletResponse)
            throws Exception {

        // Rethrow annotated exceptions or they will be processed here instead.
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
            throw exception;
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("error", ((WebBaseException) exception).getError());
        mav.addObject("url", req.getRequestURL().toString());
        mav.addObject("timestamp", new Date().toString());
        mav.addObject("status", ((WebBaseException) exception).getStatus().value());

        mav.setViewName("error");
        httpServletResponse.setStatus(((WebBaseException) exception).getStatus().value());
        return mav;
    }

}
