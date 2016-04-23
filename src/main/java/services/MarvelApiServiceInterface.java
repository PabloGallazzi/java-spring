package services;

import domain.vo.getmarvelcharacters.GetMarvelCharacters;

/**
 * Created by pgallazzi on 23/4/16.
 */
public interface MarvelApiServiceInterface {
    GetMarvelCharacters getCharacters(String nameStartsWith, String limit, String offset, String criteria, String sort) throws Exception;
}
