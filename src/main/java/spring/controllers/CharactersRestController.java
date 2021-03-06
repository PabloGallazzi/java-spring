package spring.controllers;

import domain.Token;
import domain.vo.getmarvelcharacters.GetMarvelCharacters;
import exceptions.rest.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repositories.AuthRepository;
import repositories.CharactersRepository;
import services.MarvelApiServiceInterface;

/**
 * Created by niko118 on 11/4/16.
 */
@RestController
public class CharactersRestController {

    private static final Logger logger = LoggerFactory.getLogger(CharactersRestController.class);

    /* URLs a Mapear en el controller.
    * /characters GET
    * /characters/ranking GET
    *
    * */

    @Autowired
    private MarvelApiServiceInterface marvelApiService;
    @Autowired
    private CharactersRepository charactersRepository;
    @Autowired
    private AuthRepository auth;

    @RequestMapping(value = "/characters", method = RequestMethod.GET)
    ResponseEntity<?> getCharacters(@RequestParam(value = "sort", required = false, defaultValue = "name") String sort,
                                    @RequestParam(value = "criteria", required = false, defaultValue = "asc") String criteria,
                                    @RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                    @RequestParam(value = "limit", required = false, defaultValue = "100") String limit,
                                    @RequestParam(value = "name_starts_with", required = false, defaultValue = "") String nameStartsWith) throws Exception {
        logger.info("Characters get with parameters: sort " + sort + " criteria " + criteria + " offset " + offset + " limit " + limit + " nameStartsWith " + nameStartsWith);
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
        if (offsetFromQuery < 0) {
            throw new BadRequestException("Invalid value for parameter offset, must be a number greater than 0");
        }
        GetMarvelCharacters resp = marvelApiService.getCharacters(nameStartsWith, limit, offset, criteria, sort);
        return new ResponseEntity<>(resp, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/characters/ranking", method = RequestMethod.GET)
    ResponseEntity<?> getRanking(@RequestParam(value = "limit", required = false, defaultValue = "10") String limit,
                                 @RequestParam(value = "access_token", required = false, defaultValue = "") String accessToken) {
        Token.validateNonEmptyToken(accessToken);
        Token aToken = auth.findById(accessToken);
        logger.info("Characters ranking get with limit parameter: " + limit + " requested by user: " + aToken.getUserId().toString());
        aToken.validateAdminCredentials();
        Integer limitFromRequest;
        try {
            limitFromRequest = Integer.valueOf(limit);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid value for parameter limit, must be a number greater than 0");
        }
        if (limitFromRequest < 1 || limitFromRequest > 100) {
            throw new BadRequestException("Invalid value for parameter limit, must be a number greater than 0");
        }
        return new ResponseEntity<>(charactersRepository.getCharactersOrderedByTimesElectedDesc(limitFromRequest), null, HttpStatus.OK);
    }

}