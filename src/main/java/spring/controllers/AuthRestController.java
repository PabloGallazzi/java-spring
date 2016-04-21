package spring.controllers;

import domain.Token;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.AuthRepository;
import repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class AuthRestController {

    @Autowired
    private AuthRepository auths;

    /* URLs a Mapear en el controller.
    * /user/authenticate POST
    *
    * */
    @RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> login(@RequestBody User input) {
        List<String> scopes = new ArrayList<>();
        scopes.add("admin");
        scopes.add("read");
        scopes.add("write");
        Token token = new Token(scopes,1);
        return new ResponseEntity<>(token, null, HttpStatus.OK);
    }


}


