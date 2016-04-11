package spring.controllers;

import domain.Character;
import domain.PocVo;
import domain.Team;
import domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class UsersRestController {

    /* URLs a Mapear en el controller.
    * /teams/commons/{id}/{id2} GET
    * /users POST
    * /users/{user} GET
    * /users/{user}/characters/favorites GET
    * /users/{user}/characters/favorites/  POST
    * /users/{user}/characters/favorites/{id} DELETE
    * /users/{user}/teams POST
    * /users/{user}/teams/{team} GET
    * /users/{user}/teams/{team}/characters/ POST
    * /users/{user}/teams/{team}/characters/{id} DELETE
    *
    * */

    @RequestMapping(value = "/teams/commons/{id}/{id2}", method = RequestMethod.GET)
    ResponseEntity<?> compareTeams(@PathVariable Integer id,
                                   @PathVariable Integer id2) {
        List<Map<String,Object>> output = new ArrayList<>();
        Map<String, Object> character = new LinkedHashMap<String, Object>();
        character.put("character_id",123);
        output.add(character);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    ResponseEntity<?> createUser(@RequestBody User input) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        output.put("user_name",input.getUsername());
        output.put("user_id",1234);
        return new ResponseEntity<>(output, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{user}", method = RequestMethod.GET)
    ResponseEntity<?> getUserInfo(@PathVariable Integer user,
                                  @RequestParam(value = "attributes", required = false) String attributes) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("user_id", user);
        output.put("teams","values_list");
        output.put("access","value");
        output.put("favorites","value_list");
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{user}/characters/favorites", method = RequestMethod.GET)
    ResponseEntity<?> getFavorites(@PathVariable Integer user) {
        List<Map<String, Object>> output = new ArrayList<>();
        Map<String, Object> character = new LinkedHashMap<String, Object>();
        character.put("character_id",123);
        character.put("more_info","");
        output.add(character);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{user}/characters/favorites", method = RequestMethod.POST)
    ResponseEntity<?> addFavorite(@PathVariable Integer user,
                                  @RequestBody Character input) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        output.put("character_id",input.getCharacter_id());
        return new ResponseEntity<>(input, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{user}/characters/favorites/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeFavorite(@PathVariable Integer user,
                                     @PathVariable Integer character) {
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/users/{user}/teams", method = RequestMethod.POST)
    ResponseEntity<?> createTeam(@PathVariable Integer user,
                                 @RequestBody Team input) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        output.put("team_id",123);
        output.put("team_name",input.getName());
        output.put("members",input.getMembers());
        return new ResponseEntity<>(output, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{user}/teams/{team}", method = RequestMethod.GET)
    ResponseEntity<?> getTeam(@PathVariable Integer user,
                              @PathVariable Integer team) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        output.put("team_id",123);
        output.put("team_name","someName");
        List<Map<String, Object>> characters = new ArrayList<>();
        Map<String, Object> character = new LinkedHashMap<String, Object>();
        character.put("character_id",123);
        character.put("more_info","");
        characters.add(character);
        output.put("characters",characters);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{user}/teams/{team}/characters", method = RequestMethod.POST)
    ResponseEntity<?> addToTeam(@PathVariable Integer user,
                                @PathVariable Integer team,
                                @RequestBody Character input) {
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        output.put("character_id",input.getCharacter_id());
        return new ResponseEntity<>(output, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{user}/teams/{team}/characters/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeFromTeam(@PathVariable Integer user,
                                     @PathVariable Integer team,
                                     @PathVariable Integer character) {
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

}


