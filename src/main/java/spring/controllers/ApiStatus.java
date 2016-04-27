package spring.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pgallazzi on 26/4/16.
 */
@RestController
public class ApiStatus {

    //Ya está terminado y testeado
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    ResponseEntity<?> getUserInfo() {
        return new ResponseEntity<>("pong", null, HttpStatus.OK);
    }

}
