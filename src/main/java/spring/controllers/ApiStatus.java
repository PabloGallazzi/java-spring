package spring.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pgallazzi on 26/4/16.
 */
@RestController
public class ApiStatus {

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    ResponseEntity<?> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "pong");
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }

}
