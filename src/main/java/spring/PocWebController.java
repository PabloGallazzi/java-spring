package spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.PocVo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pgallazzi on 31/3/16.
 */
@Controller
public class PocWebController {

    @RequestMapping("/greeting")
    public String greeting(Model model) {
        List<String> pungaHobbies = Arrays.asList("dormir", "comer", "nada");
        PocVo pungaInstance = new PocVo(1, "Pablo", "Gallazzi", 23, pungaHobbies);
        String title = "POC WEB";
        Map<String,Object> modelToView = new HashMap<String,Object>();
        modelToView.put("instance",pungaInstance);
        modelToView.put("title", title);
        model.addAllAttributes(modelToView);
        return "greeting";
    }

}
