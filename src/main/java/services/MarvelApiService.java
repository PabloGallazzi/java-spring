package services;

import com.pgallazzi.http.client.TacsRestClient;
import com.pgallazzi.http.utils.callbacks.JavaRestClientCallback;
import com.pgallazzi.http.utils.response.RestClientResponse;
import domain.Character;
import domain.vo.getmarvelcharacters.GetMarvelCharacters;
import exceptions.rest.ServiceUnavailableException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by pgallazzi on 15/4/16.
 */
@Service
@Scope("singleton")
@Profile({"openshift", "develop", "mongo"})
public class MarvelApiService implements MarvelApiServiceInterface {

    @Autowired
    private CacheProvider cacheProvider;

    private static final Logger logger = Logger.getLogger(MarvelApiService.class);

    private static TacsRestClient restClient = new TacsRestClient();

    private static final String BASE_URL = "http://gateway.marvel.com/v1/public/";

    private static String generateAuthInfo() throws Exception {
        //Keys from Pablo's user... from here
        String publicKey = "f3deeb5c7582067a1be1cc261d0a58b1";
        String privateKey = "2e913724a927c38382bfe92287b19792d3707d93";
        //To here.
        String timeStamp = String.valueOf(new Date().getTime());
        String stringToHash = timeStamp + privateKey + publicKey;
        return "ts=" + timeStamp + "&apikey=" + publicKey + "&hash=" + DigestUtils.md5Hex(stringToHash);
    }

    public GetMarvelCharacters getCharacters(String nameStartsWith, String limit, String offset, String criteria, String sort) throws Exception {
        final String key = nameStartsWith + limit + offset + criteria + sort;
        GetMarvelCharacters result = cacheProvider.getCharactersFromCache(key);
        if (result != null){
            return result;
        }
        final Map<String, GetMarvelCharacters> resp = new HashMap<>();
        String realSort = sort;
        if (criteria.equals("desc")) {
            realSort = "-" + sort;
        }
        String uri = BASE_URL;
        uri += "characters?" + "orderBy=" + realSort + "&limit=" + limit + "&offset=" + offset + "&" + generateAuthInfo();
        if (!nameStartsWith.isEmpty()) {
            uri += "&nameStartsWith=" + nameStartsWith;
        }
        restClient.get(uri, new JavaRestClientCallback<GetMarvelCharacters>(GetMarvelCharacters.class) {
            @Override
            public void handleSuccess(RestClientResponse it, GetMarvelCharacters data) {
                //En it está el estado, la data (como mapas, arrays, etc.. VER ABAJO), y los headers, además en data está la instancia si se la pudo parsear...
                cacheProvider.saveCharactersToCache(key, data);
                resp.put("response", data);
            }

            @Override
            public void handleFailure(RestClientResponse it) {
                logger.error("Get to marvel's api failed:", it.getException());
                throw new ServiceUnavailableException();
            }
        });
        return resp.get("response");
    }

}
