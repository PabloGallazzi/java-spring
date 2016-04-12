package spring.controllers.poc;

import exceptions.web.WebBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import domain.poc.PocVo;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pgallazzi on 31/3/16.
 */
@Controller
public class PocWebController {

    //This is a POC to render simple html, and specify custom http status code
    @RequestMapping("/greeting")
    public String greeting(Model model, HttpServletResponse httpServletResponse) {
        List<String> pocHobbies = Arrays.asList("dormir", "comer", "nada");
        PocVo pocInstance = new PocVo(1, "Pablo", "Gallazzi", 23, pocHobbies);
        String title = "POC WEB";
        Map<String, Object> modelToView = new HashMap<String, Object>();
        modelToView.put("instance", pocInstance);
        modelToView.put("title", title);
        model.addAllAttributes(modelToView);
        httpServletResponse.setStatus(200);
        return "greeting";
    }

    //This is a POC to handle custom web exceptions
    @RequestMapping("/greetingError")
    public String greetingError(Model model) throws Exception {
        throw new WebBaseException(HttpStatus.NOT_FOUND, "Test", "Testing web error");
    }

}