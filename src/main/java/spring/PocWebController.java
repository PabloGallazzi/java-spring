package spring;

import exceptions.web.WebBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import domain.PocVo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pgallazzi on 31/3/16.
 */
@Controller
public class PocWebController {

    //This is a POC to render simple html
    @RequestMapping("/greeting")
    public String greeting(Model model) {
        List<String> pocHobbies = Arrays.asList("dormir", "comer", "nada");
        PocVo pocInstance = new PocVo(1, "Pablo", "Gallazzi", 23, pocHobbies);
        String title = "POC WEB";
        Map<String, Object> modelToView = new HashMap<String, Object>();
        modelToView.put("instance", pocInstance);
        modelToView.put("title", title);
        model.addAllAttributes(modelToView);
        return "greeting";
    }

    //This is a POC to handle custom web exceptions
    @RequestMapping("/greetingError")
    public String greetingError(Model model) throws Exception {
        throw new WebBaseException(HttpStatus.NOT_FOUND, "Test", "Testing web error");
    }

}
