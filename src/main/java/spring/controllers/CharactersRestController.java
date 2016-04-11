package spring.controllers;

import domain.PocVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class CharactersRestController {

    /* URLs a Mapear en el controller.
    * /characters GET
    * /characters/ranking GET
    *
    * */

    @RequestMapping(value = "/characters", method = RequestMethod.GET)
    ResponseEntity<?> getCharacters(@RequestParam(value = "sort", required = false) String sort,
                                    @RequestParam(value = "criteria", required = false) String criteria,
                                    @RequestParam(value = "offset", required = false) String offset,
                                    @RequestParam(value = "limit", required = false) String limit) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        Map<String, Object> paging = new LinkedHashMap<String, Object>();
        paging.put("total", 123);
        paging.put("limit", limit);
        paging.put("offset", offset);
        output.put("paging",paging);
        List<Map<String, Object>> characterList = new ArrayList<>();
        Map<String, Object> characters = new LinkedHashMap<String, Object>();
        characters.put("character_id",123);
        characters.put("more_info","");
        characterList.add(characters);
        output.put("result",characterList);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/characters/ranking", method = RequestMethod.GET)
    ResponseEntity<?> getRanking() {
        List<Map<String, Object>> output = new ArrayList<>();
        Map<String, Object> characters = new LinkedHashMap<String, Object>();
        characters.put("character_id",123);
        characters.put("elected_times",15);
        output.add(characters);
        characters.clear();
        characters.put("character_id",124);
        characters.put("elected_times",10);
        output.add(characters);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }


}


