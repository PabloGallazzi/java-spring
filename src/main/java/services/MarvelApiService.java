package services;

import com.pgallazzi.http.client.TacsRestClient;
import com.pgallazzi.http.utils.callbacks.JavaRestClientCallback;
import com.pgallazzi.http.utils.response.RestClientResponse;
import domain.vo.getmarvelcharacters.GetMarvelCharacters;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pgallazzi on 15/4/16.
 */
@Service
@Scope("singleton")
public class MarvelApiService {

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

    public static GetMarvelCharacters getCharacters(String nameStartsWith, String limit, String offset) throws Exception {
        final Map<String, GetMarvelCharacters> resp = new HashMap<>();
        String uri = BASE_URL;
        uri += "characters?" + "orderBy=name&limit=" + limit + "&offset=" + offset + "&" + generateAuthInfo();
        if (nameStartsWith.length() != 0) {
            uri += "&nameStartsWith=" + nameStartsWith;
        }
        restClient.get(uri, new JavaRestClientCallback<GetMarvelCharacters>(GetMarvelCharacters.class) {
            @Override
            public void handleSuccess(RestClientResponse it, GetMarvelCharacters data) {
                //En it está el estado, la data (como mapas, arrays, etc.. VER ABAJO), y los headers, además en data está la instancia si se la pudo parsear...
                resp.put("response", data);
            }

            @Override
            public void handleFailure(RestClientResponse it) {
                //Handle error
            }
        });
        return resp.get("response");
    }

}
