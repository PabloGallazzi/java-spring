package services;

import domain.vo.getmarvelcharacters.GetMarvelCharacters;

/**
 * Created by pgallazzi on 2/7/16.
 */
public interface CacheProvider {
    void saveCharactersToCache(String key, GetMarvelCharacters character);
    GetMarvelCharacters getCharactersFromCache(String key);
}
