package spring.controllers;

import domain.PocVo;
import domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class AuthRestController {

    /* URLs a Mapear en el controller.
    * /user/authenticate POST
    *
    * */
    @RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> login(@RequestBody User input) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        output.put("access_token", "LONG_STRING");
        output.put("user_id",123);
        output.put("more_info","");
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }


}


