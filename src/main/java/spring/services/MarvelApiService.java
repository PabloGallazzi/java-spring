package spring.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgallazzi.http.client.TacsRestClient;
import com.pgallazzi.http.utils.callbacks.JavaRestClientCallback;
import com.pgallazzi.http.utils.callbacks.RestClientCallback;
import com.pgallazzi.http.utils.response.RestClientResponse;
import domain.vo.getmarvelcharacters.GetMarvelCharacters;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pgallazzi on 15/4/16.
 */
public class MarvelApiService {

    private static TacsRestClient restClient = new TacsRestClient();

    private static final String BASE_URL = "http://gateway.marvel.com/v1/public/";

    private static String generateAuthInfo() throws Exception {
        //TODO: Change from here...
        String publicKey = "f3deeb5c7582067a1be1cc261d0a58b1";
        String privateKey = "2e913724a927c38382bfe92287b19792d3707d93";
        //To here...
        String timeStamp = String.valueOf(new Date().getTime());
        String stringToHash = timeStamp + privateKey + publicKey;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(stringToHash.getBytes());
        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return "ts=" + timeStamp + "&apikey=" + publicKey + "&hash=" + sb.toString();
    }

    public static GetMarvelCharacters getCharacters(String nameStartsWith, String limit, String offset) throws Exception {
        final Map<String, GetMarvelCharacters> resp = new HashMap<>();
        String uri = BASE_URL + "characters?nameStartsWith=" + nameStartsWith + "&orderBy=name&limit=" + limit + "&offset=" + offset + "&" + generateAuthInfo();
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

    public static GetMarvelCharacters getCharacters2(String nameStartsWith, String limit, String offset) throws Exception {
        final Map<String, Object> resp = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uri", BASE_URL + "characters?nameStartsWith=" + nameStartsWith + "&orderBy=name&limit=" + limit + "&offset=" + offset + "&" + generateAuthInfo());
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", " application/json; charset=UTF-8");
        map.put("headers", headers);
        map.put("success", new RestClientCallback() {
            @Override
            public void call(RestClientResponse response) {
                resp.put("response", response.getData());
                resp.put("status", response.getStatus().getStatusCode());
                resp.put("headers", response.getHeaders());
            }
        });
        map.put("failure", new RestClientCallback() {
            @Override
            public void call(RestClientResponse response) {
                if (response.getException() != null) {
                    resp.put("message", response.getException().getMessage());
                    resp.put("status", 500);
                } else {
                    resp.put("response", response.getData());
                    resp.put("status", response.getStatus().getStatusCode());
                    resp.put("headers", response.getHeaders());
                }
            }
        });
        restClient.get(map);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(gson.toJson(resp.get("response")), GetMarvelCharacters.class);
    }

}
