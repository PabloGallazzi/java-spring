package services;

import domain.vo.getmarvelcharacters.GetMarvelCharacters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import spring.utils.Cacheable;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PabloGallazzi on 2/7/16.
 */
@Service
@Scope("singleton")
@Profile({"test", "develop", "mongo"})
public class LocalCacheProvider implements CacheProvider {

    private static final Logger logger = LoggerFactory.getLogger(LocalCacheProvider.class);

    Map<String, Cacheable> cache;


    @PostConstruct
    public void initialize() throws UnknownHostException {
        cache = new HashMap<>();
    }

    @Override
    public void saveCharactersToCache(String key, GetMarvelCharacters characters) {
        cache.put(key, new Cacheable(characters));
    }

    @Override
    public GetMarvelCharacters getCharactersFromCache(String key) {
        Cacheable cacheable = cache.get(key);
        if (cacheable != null && cacheable.isValid()) {
            logger.debug("Cache found");
            return  (GetMarvelCharacters) cacheable.getData();
        }
        logger.debug("Cache NOT found");
        return null;
    }
}