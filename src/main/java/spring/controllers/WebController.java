package spring.controllers;

import domain.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import repositories.AuthRepository;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by pgallazzi on 9/5/16.
 */
@Controller
public class WebController {

    @Autowired
    private AuthRepository auth;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String login(HttpServletResponse httpServletResponse,
                        @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        if (aToken.isAdmin()) {
            //Es admin
        } else {
            //No es admin
        }
        httpServletResponse.setStatus(200);
        return "index_user";
    }

}
