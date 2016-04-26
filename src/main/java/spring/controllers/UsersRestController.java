package spring.controllers;

import domain.Character;
import domain.Team;
import domain.Token;
import domain.User;
import exceptions.rest.BadRequestException;
import exceptions.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.AuthRepository;
import repositories.CharactersRepository;
import repositories.TeamsRepository;
import repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class UsersRestController {

    @Autowired
    private UsersRepository users;
    @Autowired
    private TeamsRepository teams;
    @Autowired
    private AuthRepository auth;
    @Autowired
    private CharactersRepository charactersRepository;


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

    //Ya está terminado y testeado
    @RequestMapping(value = "/teams/commons/{id}/{id2}", method = RequestMethod.GET)
    ResponseEntity<?> compareTeams(@PathVariable String id,
                                   @PathVariable String id2,
                                   @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateAdminCredentials();
        Team team1 = teams.findByTeamId(id);
        Team team2 = teams.findByTeamId(id2);
        List<Character> characters = findIntersection(team1.getMembers(), team2.getMembers());
        return new ResponseEntity<>(characters, null, HttpStatus.OK);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    ResponseEntity<?> createUser(@RequestBody User userBody) {
        if (userBody.getUserName() == null) {
            String[] cause = new String[1];
            cause[0] = "must_provide_a_user_name";
            throw new BadRequestException("Unable to create user", "Validation error", cause);
        }
        User.validateUser(userBody);
        try {
            users.save(userBody);
        } catch (com.mongodb.DuplicateKeyException e) {
            String[] cause = new String[1];
            cause[0] = "user_name_already_used";
            throw new BadRequestException("Unable to create user", "Validation error", cause);
        }
        return new ResponseEntity<>(userBody, null, HttpStatus.CREATED);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getUserInfo(@PathVariable String id,
                                  @RequestParam(value = "attributes", required = false) String attributes,
                                  @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateAdminCredentials();
        //TODO: Filter by attributes
        return new ResponseEntity<>(users.findByUserId(id), null, HttpStatus.OK);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users/{user}/characters/favorites", method = RequestMethod.GET)
    ResponseEntity<?> getFavorites(@PathVariable String user,
                                   @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateUserCredentials(user);
        User thisUser = users.findByUserId(user);
        return new ResponseEntity<>(thisUser.getFavorites(), null, HttpStatus.OK);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users/{userId}/characters/favorites", method = RequestMethod.POST)
    ResponseEntity<?> addFavorite(@PathVariable String userId,
                                  @RequestBody Character character,
                                  @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateUserCredentials(userId);
        User thisUser = users.findByUserId(userId);
        Character character1 = charactersRepository.findById(character.getId());
        if (character1 != null) {
            character = character1;
            character.selectedAsFavorite();
            charactersRepository.update(character);
        } else {
            character.setElectedTimes(1);
            charactersRepository.save(character);
        }
        thisUser.addAsFavorite(character);
        users.update(thisUser);
        return new ResponseEntity<>(character, null, HttpStatus.CREATED);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users/{userId}/characters/favorites/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeFavorite(@PathVariable String userId,
                                     @PathVariable String id,
                                     @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateUserCredentials(userId);
        User thisUser = users.findByUserId(userId);
        Integer integerId = validateCharacterId(id);
        Character character = thisUser.deleteFavorite(integerId);
        charactersRepository.update(character);
        users.update(thisUser);
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users/{userId}/teams", method = RequestMethod.POST)
    ResponseEntity<?> createTeam(@PathVariable String userId,
                                 @RequestBody Team team,
                                 @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateUserCredentials(userId);
        User thisUser = users.findByUserId(userId);
        team.setTeamId(null);
        team = teams.save(team);
        thisUser.addNewTeam(team);
        users.update(thisUser);
        return new ResponseEntity<>(team, null, HttpStatus.CREATED);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users/{userId}/teams/{teamId}", method = RequestMethod.GET)
    ResponseEntity<?> getTeam(@PathVariable String userId,
                              @PathVariable String teamId,
                              @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateUserCredentials(userId);
        Team team = teams.findByTeamId(teamId);
        User thisUser = users.findByUserId(userId);
        validateTeamBelongsToUser(thisUser, team);
        return new ResponseEntity<>(team, null, HttpStatus.OK);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users/{userId}/teams/{teamId}/characters", method = RequestMethod.POST)
    ResponseEntity<?> addToTeam(@PathVariable String userId,
                                @PathVariable String teamId,
                                @RequestBody Character character,
                                @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateUserCredentials(userId);
        Team team = teams.findByTeamId(teamId);
        User thisUser = users.findByUserId(userId);
        validateTeamBelongsToUser(thisUser, team);
        Character character1 = charactersRepository.findById(character.getId());
        if (character1 == null) {
            charactersRepository.save(character);
        }
        team.addMember(character);
        teams.update(team);
        return new ResponseEntity<>(character, null, HttpStatus.CREATED);
    }

    //Ya está terminado y testeado
    @RequestMapping(value = "/users/{userId}/teams/{teamId}/characters/{characterId}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeFromTeam(@PathVariable String userId,
                                     @PathVariable String teamId,
                                     @PathVariable String characterId,
                                     @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token aToken = auth.findById(accessToken);
        aToken.validateUserCredentials(userId);
        Team team = teams.findByTeamId(teamId);
        User thisUser = users.findByUserId(userId);
        validateTeamBelongsToUser(thisUser, team);
        Integer charId = validateCharacterId(characterId);
        team.removeMember(charId);
        teams.update(team);
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }


    //Auxiliary methods
    private List<Character> findIntersection(List<Character> team1, List<Character> team2) {
        List<Character> charactersToReturn = new ArrayList<Character>();
        for (Character character : team1) {
            for (Character character2 : team2) {
                if (character.getId().equals(character2.getId())) {
                    charactersToReturn.add(character);
                }
            }
        }
        return charactersToReturn;
    }

    private Integer validateCharacterId(String id) {
        Integer charId = null;
        try {
            charId = Integer.valueOf(id);
        } catch (NumberFormatException e) {
            String[] cause = new String[1];
            cause[0] = "character_id_must_be_a_natural_number";
            throw new BadRequestException("Unable to remove character", "bad_id", cause);
        }
        return charId;
    }

    private void validateTeamBelongsToUser(User user, Team team){
        List<Team> userTeams = user.getTeams();
        for (Team team1 : userTeams){
            if (team.getTeamId().toString().equals(team1.getTeamId().toString())){
                return;
            }
        }
        throw new NotFoundException("Team with id " + team.getTeamId().toString() + " was not found");
    }

}


