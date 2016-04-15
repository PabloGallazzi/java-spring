package spring.controllers;

import com.pgallazzi.http.client.TacsRestClient;
import com.pgallazzi.http.utils.callbacks.JavaRestClientCallback;
import com.pgallazzi.http.utils.response.RestClientResponse;
import domain.Character;
import domain.User;
import domain.vo.getmarvelcharacters.GetMarvelCharacters;
import exceptions.rest.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.services.MarvelApiService;

import java.security.MessageDigest;
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
                                    @RequestParam(value = "offset", required = false) int offset,
                                    @RequestParam(value = "limit", required = false) int limit) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        Map<String, Object> paging = new LinkedHashMap<String, Object>();
        paging.put("total", 123);
        paging.put("limit", limit);
        paging.put("offset", offset);
        output.put("paging", paging);
        List<Character> characterList = new ArrayList<>();
        Character character = new Character(1, "Daredevil", "Un ciego adepto a las artes marciales.");
        characterList.add(character);
        output.put("results", characterList);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
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

    //This is an example to use the restClient
    @RequestMapping(value = "/characters/mervel", method = RequestMethod.GET)
    ResponseEntity<?> getCharacterMarvel(@RequestParam(value = "limit", required = false) String limit,
                                         @RequestParam(value = "offset", required = false) String offset,
                                         @RequestParam(value = "name_starts_with", required = false) String nameStartsWith) throws Exception {
        if (limit == null || offset == null || nameStartsWith == null) {
            throw new BadRequestException("Parameters limit, offset and name_starts_with must be provided");
        }
        GetMarvelCharacters resp = MarvelApiService.getCharacters(nameStartsWith, limit, offset);
        //See also:
        //GetMarvelCharacters resp = MarvelApiService.getCharacters(nameStartsWith, limit, offset);
        return new ResponseEntity<>(resp, null, HttpStatus.OK);
    }

}


