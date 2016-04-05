package spring.globalexceptionhandlers;

import exceptions.web.WebBaseException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by pgallazzi on 3/4/16.
 */
@ControllerAdvice
public class GlobalWebExceptionHandlingControllerAdvice {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(WebBaseException.class)
    public ModelAndView handleError(HttpServletRequest req, Exception exception)
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
        return mav;
    }
}
