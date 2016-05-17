package spring.controllers;

import domain.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
                        @CookieValue(value = "access_token", required = true, defaultValue = "") String accessToken) {
        try {
            Token.validateNonEmptyToken(accessToken);
            Token aToken = auth.findById(accessToken);
            httpServletResponse.setStatus(200);
            if (aToken.isAdmin()) {
                return "home_admin";
            } else {
                return "home_user";
            }
        } catch (Exception ex) {
            return "error";
        }
    }
}