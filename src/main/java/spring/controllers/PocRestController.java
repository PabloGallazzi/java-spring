package spring.controllers;

import exceptions.rest.BadRequestException;
import exceptions.rest.InternalServerError;
import exceptions.rest.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import domain.PocVo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by pgallazzi on 31/3/16.
 */
@RestController
public class PocRestController {

    //This is a POC to use a custom handler for exceptions
    @RequestMapping(value = "/poc/exception", method = RequestMethod.GET)
    public PocVo throwException(@RequestParam(value = "id", required = false, defaultValue = "3") Integer id) {
        if (id == 1) {
            throw new NotFoundException();
        } else if (id == 2) {
            throw new InternalServerError();
        }
        String[] cause = new String[2];
        cause[0] = "invalid_id";
        cause[1] = "anotherTestCause";
        throw new BadRequestException("POC for bad request", "bad_request", cause);
    }

    //This is a POC to use the QUERY STRING params
    @RequestMapping(value = "/poc", method = RequestMethod.GET)
    public PocVo getSingleItemByQuery(@RequestParam(value = "id", required = false, defaultValue = "1") String id) {
        return getPoc(Integer.valueOf(id));
    }

    //This is a POC to use the PATH params
    @RequestMapping(value = "/poc/{id}", method = RequestMethod.GET)
    public PocVo getSingleItemByPath(@PathVariable Integer id) {
        return getPoc(id);
    }

    //This is a POC to return an array of items
    @RequestMapping(value = "/poc/all", method = RequestMethod.GET)
    public List<PocVo> getAllItems() {
        return getAll();
    }

    //This is a POC to use the POST Method, and parse the posted body to the corresponding entity
    //It also shows how to specify a return body, an HTTP STATUS, and headers (though in this case the headers are the defaults)
    @RequestMapping(value = "/poc", method = RequestMethod.POST)
    ResponseEntity<?> post(@RequestBody PocVo input) {
        return new ResponseEntity<>(input, null, HttpStatus.CREATED);
    }

    //This is a POC to use the PUT Method, and parse the posted body to the corresponding entity
    //It also shows how to specify a return body, an HTTP STATUS, and headers
    @RequestMapping(value = "/poc/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> put(@RequestBody PocVo input, @PathVariable Integer id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setDate(new Date().getTime());
        PocVo pocVo = getPoc(id);
        pocVo.setAge(input.getAge());
        pocVo.setHobbies(input.getHobbies());
        pocVo.setName(input.getName());
        pocVo.setSurname(input.getSurname());
        return new ResponseEntity<>(pocVo, httpHeaders, HttpStatus.OK);
    }

    //This is a POC to use the DELETE Method
    @RequestMapping(value = "/poc/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@PathVariable Integer id) {
        return new ResponseEntity<>(getPoc(id), null, HttpStatus.OK);
    }

    //Auxiliary methods
    List<PocVo> getAll() {
        List<String> pocHobbies = Arrays.asList("dormir", "comer", "nada");
        PocVo pocInstance = new PocVo(1, "Pablo", "Gallazzi", 23, pocHobbies);
        PocVo pocInstance2 = new PocVo(2, "Nico", "Garcia", 23, pocHobbies);
        return Arrays.asList(pocInstance, pocInstance2);
    }

    PocVo getPoc(Integer id) {
        List<PocVo> all = getAll();
        for (PocVo poc : all) {
            if (poc.getId() == id) {
                return poc;
            }
        }
        return null;
    }

}
