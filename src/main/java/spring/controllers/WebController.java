package spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by pgallazzi on 9/5/16.
 */
@Controller
public class WebController {

    @RequestMapping("/home")
    public String login(HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(200);
        return "index_user";
    }

}
