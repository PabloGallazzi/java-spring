package spring.controllers;

import domain.PocVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class AuthRestController {

    /* URLs a Mapear en el controller.
    * /user/authenticate POST
    *
    * */
    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> login(@RequestBody PocVo input) {
        return new ResponseEntity<>(input, null, HttpStatus.OK);
    }


}


