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
        //TODO: ojo que no se que hace al hacer el parseoen el requestBody. Si usa el setter, la pass esta en SHA.
        //TODO: y el usuario en el metodo passwordIsCorrect da por hecho que recibe el string.
        //Checkear que haya traido informacion
        Token token = auths.login(input);
        //Validar que no haya devuelto null
        return new ResponseEntity<>(token, null, HttpStatus.OK);
    }


}


