package spring.controllers;

import domain.Character;
import domain.Team;
import domain.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.UsersRepository;

import java.util.*;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class UsersRestController {

    @Autowired
    private UsersRepository users;

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
        List<Character> output = new ArrayList<>();
        Character character = new Character(1, "TACS", "Description");
        output.add(character);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    ResponseEntity<?> createUser(@RequestBody User input) {

        return new ResponseEntity<>(input, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getUserInfo(@PathVariable Integer id,
                                  @RequestParam(value = "attributes", required = false) String attributes) {
        User user = new User();
        user.setUserName("Pablo" + String.valueOf(new Date().getTime()));
        user.setUserPassword("123456");
        user.setLastAccess(new Date());
        /*Team team = new Team("Pablito");
        team.setTeamId(1);
        List<Character> teamItems = new ArrayList<>();
        Character character = new Character(1,"TACS","TACSDescription");
        teamItems.add(character);
        team.setMembers(teamItems);
        List<Team> teams = new ArrayList<Team>();
        teams.add(team);
        user.setTeams(teams);
        List<Character> characters = new ArrayList<Character>();
        characters.add(character);
        user.setFavorites(characters);*/
        String idString = String.format("%1$" + "24" + "s", String.valueOf(new Date().getTime())).replaceAll(" ", "a");
        //TODO: Remove this
        ObjectId objectId = new ObjectId(idString);
        user.setUserId(objectId);
        users.save(user);
        user = users.findByUserId(idString);
        return new ResponseEntity<>(user, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{user}/characters/favorites", method = RequestMethod.GET)
    ResponseEntity<?> getFavorites(@PathVariable Integer user) {
        List<Character> output = new ArrayList<>();
        Character character = new Character(123, "TestName", "TestDescription");
        output.add(character);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{user}/characters/favorites", method = RequestMethod.POST)
    ResponseEntity<?> addFavorite(@PathVariable Integer user,
                                  @RequestBody Character input) {
        input.selectedAsFavorite();
        return new ResponseEntity<>(input, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{user}/characters/favorites/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeFavorite(@PathVariable Integer user,
                                     @PathVariable Integer id) {
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/users/{user}/teams", method = RequestMethod.POST)
    ResponseEntity<?> createTeam(@PathVariable Integer user,
                                 @RequestBody Team input) {
        //TODO: Remove this
        input.setTeamId(new ObjectId("123456789123456789123456"));
        return new ResponseEntity<>(input, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{user}/teams/{team}", method = RequestMethod.GET)
    ResponseEntity<?> getTeam(@PathVariable Integer user,
                              @PathVariable Integer team) {
        Team output = new Team("TeamName");
        output.setTeamId(new ObjectId("123456789123456789123456"));
        Character character = new Character(1, "CharacterName", "DescriptionTest");
        output.getMembers().add(character);
        return new ResponseEntity<>(output, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{user}/teams/{team}/characters", method = RequestMethod.POST)
    ResponseEntity<?> addToTeam(@PathVariable Integer user,
                                @PathVariable Integer team,
                                @RequestBody Character input) {
        input.setId(1);
        return new ResponseEntity<>(input, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{user}/teams/{team}/characters/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeFromTeam(@PathVariable Integer user,
                                     @PathVariable Integer team,
                                     @PathVariable Integer id) {
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

}


