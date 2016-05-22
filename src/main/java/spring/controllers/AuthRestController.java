package spring.controllers;

import domain.User;
import exceptions.rest.BadRequestException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.AuthRepository;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class AuthRestController {

    private static final Logger logger = Logger.getLogger(AuthRestController.class);

    @Autowired
    private AuthRepository auths;

    /* URLs a Mapear en el controller.
    * /user/authenticate POST
    *
    * */

    @RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> login(@RequestBody User input) {
        if (input.getUserName() == null || input.getUserName().isEmpty() || input.getUserPassword() == null || input.getUserPassword().isEmpty()) {
            throw new BadRequestException("Unable to authenticate user", "invalid_credentials");
        }
        logger.info("Authentication for user " + input.getUserName() + " requested");
        return new ResponseEntity<>(auths.login(input), null, HttpStatus.OK);
    }


}


