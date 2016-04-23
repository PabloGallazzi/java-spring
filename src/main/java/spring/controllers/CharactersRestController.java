package spring.controllers;

import domain.Character;
import domain.vo.getmarvelcharacters.GetMarvelCharacters;
import exceptions.rest.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.MarvelApiService;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private MarvelApiService marvelApiService;

    @RequestMapping(value = "/characters", method = RequestMethod.GET)
    ResponseEntity<?> getCharacters(@RequestParam(value = "sort", required = false, defaultValue = "name") String sort,
                                    @RequestParam(value = "criteria", required = false, defaultValue = "asc") String criteria,
                                    @RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                    @RequestParam(value = "limit", required = false, defaultValue = "100") String limit,
                                    @RequestParam(value = "name_starts_with", required = false, defaultValue = "") String nameStartsWith) throws Exception {
        if ((!sort.equals("modified") && !sort.equals("name"))) {
            throw new BadRequestException("Invalid value for parameter sort, the valid ones are modified and name");
        }
        if ((!criteria.equals("desc") && !criteria.equals("asc"))) {
            throw new BadRequestException("Invalid value for parameter criteria, the valid ones are desc and asc");
        }
        Integer limitFromQuery;
        try {
            limitFromQuery = Integer.valueOf(limit);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid value for parameter limit, must be a number between 1 and 100");
        }
        if (limitFromQuery > 100 || limitFromQuery < 1) {
            throw new BadRequestException("Invalid value for parameter limit, must be a number between 1 and 100");
        }
        Integer offsetFromQuery;
        try {
            offsetFromQuery = Integer.valueOf(offset);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid value for parameter offset, must be a number greater than 0");
        }
        if ((offsetFromQuery < 0)) {
            throw new BadRequestException("Invalid value for parameter offset, must be a number greater than 0");
        }
        GetMarvelCharacters resp = marvelApiService.getCharacters(nameStartsWith, limit, offset, criteria, sort);
        return new ResponseEntity<>(resp, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/characters/ranking", method = RequestMethod.GET)
    ResponseEntity<?> getRanking() {
        List<Character> output = new ArrayList<>();
        Character character = new Character(2, "Dr. Strange", "Tira magias.");
        character.setElectedTimes(10);
        Character character2 = new Character(3, "Hulk", "SMASH SMASH SMASH");
        character2.setElectedTimes(9);
        output.add(character);
        output.add(character2);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

}


