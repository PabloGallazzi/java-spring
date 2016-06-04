package spring.controllers;

import domain.Character;
import domain.Team;
import domain.Token;
import domain.User;
import exceptions.rest.BadRequestException;
import exceptions.rest.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.AuthRepository;
import repositories.CharactersRepository;
import repositories.TeamsRepository;
import repositories.UsersRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class UsersRestController {

    private static final Logger logger = Logger.getLogger(UsersRestController.class);

    @Autowired
    private UsersRepository users;
    @Autowired
    private TeamsRepository teams;
    @Autowired
    private AuthRepository auth;
    @Autowired
    private CharactersRepository charactersRepository;
    @Autowired
    Environment environment;


    /* URLs a Mapear en el controller.
    * /teams/commons/{id}/{id2} GET
    * /users GET
    * /users POST
    * /users/{user} GET
    * /users/{user}/characters/favorites GET
    * /users/{user}/characters/favorites/  POST
    * /users/{user}/characters/favorites/{id} DELETE
    * /users/{user}/teams POST
    * /users/{user}/teams GET
    * /users/{user}/teams/{team} GET
    * /users/{user}/teams/{team} DELETE
    * /users/{user}/teams/{team}/characters/ POST
    * /users/{user}/teams/{team}/characters/{id} DELETE
    *
    * */

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    ResponseEntity<?> getUsers(@RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users get performed by user: " + aToken.getUserId().toString());
        aToken.validateAdminCredentials();
        return new ResponseEntity<>(users.getUsers(), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/teams/commons/{id}/{id2}", method = RequestMethod.GET)
    ResponseEntity<?> compareTeams(@PathVariable String id,
                                   @PathVariable String id2,
                                   @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Teams intersection get with parameters: " + id + " " + id2 + " requested by user: " + aToken.getUserId().toString());
        aToken.validateAdminCredentials();
        Team team1 = teams.findByTeamId(id);
        Team team2 = teams.findByTeamId(id2);
        List<Character> characters = findIntersection(team1.getMembers(), team2.getMembers());
        return new ResponseEntity<>(characters, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    ResponseEntity<?> createUser(@RequestBody User userBody) {
        if (userBody.getUserName() == null) {
            String[] cause = new String[1];
            cause[0] = "must_provide_a_user_name";
            throw new BadRequestException("Unable to create user", "Validation error", cause);
        }
        User.validateUser(userBody);
        try {
            if (!Arrays.asList(environment.getActiveProfiles()).contains("production") && userBody.getUserName().equals("Admin")) {
                userBody.setIsAdmin(true);
            }
            users.save(userBody);
        } catch (com.mongodb.DuplicateKeyException e) {
            String[] cause = new String[1];
            cause[0] = "user_name_already_used";
            throw new BadRequestException("Unable to create user", "Validation error", cause);
        }
        logger.info("New user created: " + userBody.getUserName());
        return new ResponseEntity<>(userBody, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getUserInfo(@PathVariable String id,
                                  @RequestParam(value = "attributes", required = false) String attributes,
                                  @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users get with parameter: " + attributes + " requested by user: " + aToken.getUserId().toString());
        aToken.validateAdminCredentials();
        return new ResponseEntity<>(users.findByUserId(id), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{user}/characters/favorites", method = RequestMethod.GET)
    ResponseEntity<?> getFavorites(@PathVariable String user,
                                   @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        aToken.validateUserCredentials(user);
        logger.info("Users favorites requested by user: " + aToken.getUserId().toString());
        User thisUser = users.findByUserId(user);
        return new ResponseEntity<>(thisUser.getFavorites(), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}/characters/favorites", method = RequestMethod.POST)
    ResponseEntity<?> addFavorite(@PathVariable String userId,
                                  @RequestBody Character character,
                                  @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users favorites post requested by user: " + aToken.getUserId().toString() + " " + character.getId().toString());
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

    @RequestMapping(value = "/users/{userId}/characters/favorites/{id}", method = RequestMethod.GET)
    ResponseEntity<?> isFavorite(@PathVariable String userId,
                                 @PathVariable String id,
                                 @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users favorites get single requested by user: " + aToken.getUserId().toString() + " " + id);
        aToken.validateUserCredentials(userId);
        User thisUser = users.findByUserId(userId);
        Integer charId;
        try {
            charId = Integer.valueOf(id);
        } catch (NumberFormatException exception) {
            throw new BadRequestException("Character id must be a positive number");
        }
        if (charId < 0) {
            throw new BadRequestException("Character id must be a positive number");
        }
        Character character = thisUser.getFavoriteCharacter(charId);
        if (character == null) {
            throw new NotFoundException("That character is not a favorite of this user", "character_not_found", new String[0]);
        }
        return new ResponseEntity<>(character, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}/characters/favorites/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeFavorite(@PathVariable String userId,
                                     @PathVariable String id,
                                     @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users favorites delete requested by user: " + aToken.getUserId().toString() + " " + id);
        aToken.validateUserCredentials(userId);
        User thisUser = users.findByUserId(userId);
        Integer integerId = validateCharacterId(id);
        Character character = thisUser.deleteFavorite(integerId);
        charactersRepository.update(character);
        users.update(thisUser);
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/users/{userId}/teams", method = RequestMethod.POST)
    ResponseEntity<?> createTeam(@PathVariable String userId,
                                 @RequestBody Team team,
                                 @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users team post requested by user: " + aToken.getUserId().toString() + " " + team.getTeamName());
        aToken.validateUserCredentials(userId);
        User thisUser = users.findByUserId(userId);
        team.setTeamId(null);
        team = teams.save(team);
        thisUser.addNewTeam(team);
        users.update(thisUser);
        return new ResponseEntity<>(team, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{userId}/teams", method = RequestMethod.GET)
    ResponseEntity<?> getTeams(@PathVariable String userId,
                                 @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users team get all requested by user: " + aToken.getUserId().toString());
        aToken.validateUserCredentials(userId);
        User thisUser = users.findByUserId(userId);
        return new ResponseEntity<>(thisUser.getTeams(), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}/teams/{teamId}", method = RequestMethod.GET)
    ResponseEntity<?> getTeam(@PathVariable String userId,
                              @PathVariable String teamId,
                              @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users team get requested by user: " + aToken.getUserId().toString() + " " + teamId);
        aToken.validateUserCredentials(userId);
        Team team = teams.findByTeamId(teamId);
        User thisUser = users.findByUserId(userId);
        validateTeamBelongsToUser(thisUser, team);
        return new ResponseEntity<>(team, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}/teams/{teamId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteTeam(@PathVariable String userId,
                                 @PathVariable String teamId,
                                 @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("User team delete requested by user: " + aToken.getUserId().toString() + " " + teamId);
        aToken.validateUserCredentials(userId);
        Team team = teams.findByTeamId(teamId);
        User thisUser = users.findByUserId(userId);
        validateTeamBelongsToUser(thisUser, team);
        thisUser.deleteTeam(team.getTeamId());
        users.update(thisUser);
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/users/{userId}/teams/{teamId}/characters", method = RequestMethod.POST)
    ResponseEntity<?> addToTeam(@PathVariable String userId,
                                @PathVariable String teamId,
                                @RequestBody Character character,
                                @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users character post requested by user: " + aToken.getUserId().toString() + teamId + " " + character.getId());
        aToken.validateUserCredentials(userId);
        Team team = teams.findByTeamId(teamId);
        User thisUser = users.findByUserId(userId);
        validateTeamBelongsToUser(thisUser, team);
        Character character1 = charactersRepository.findById(character.getId());
        if (character1 == null) {
            character.setElectedTimes(0);
            charactersRepository.save(character);
        }
        team.addMember(character);
        teams.update(team);
        return new ResponseEntity<>(character, null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{userId}/teams/{teamId}/characters/{characterId}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeFromTeam(@PathVariable String userId,
                                     @PathVariable String teamId,
                                     @PathVariable String characterId,
                                     @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Users character delete requested by user: " + aToken.getUserId().toString() + teamId + " " + characterId);
        aToken.validateUserCredentials(userId);
        Team team = teams.findByTeamId(teamId);
        User thisUser = users.findByUserId(userId);
        validateTeamBelongsToUser(thisUser, team);
        Integer charId = validateCharacterId(characterId);
        team.removeMember(charId);
        teams.update(team);
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    ResponseEntity<?> getAllTeams(@RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("All teams get requested by user: " + aToken.getUserId().toString());
        aToken.validateAdminCredentials();
        return new ResponseEntity<>(teams.getTeams(), null, HttpStatus.OK);
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

    private void validateTeamBelongsToUser(User user, Team team) {
        List<Team> userTeams = user.getTeams();
        for (Team team1 : userTeams) {
            if (team.getTeamId().toString().equals(team1.getTeamId().toString())) {
                return;
            }
        }
        throw new NotFoundException("Team with id " + team.getTeamId().toString() + " was not found");
    }

}


