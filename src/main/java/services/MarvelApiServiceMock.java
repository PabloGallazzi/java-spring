package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.vo.getmarvelcharacters.GetMarvelCharacters;
import exceptions.rest.InternalServerError;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by pgallazzi on 15/4/16.
 */
@Service
@Scope("singleton")
@Profile("test")
public class MarvelApiServiceMock implements MarvelApiServiceInterface {

    public GetMarvelCharacters getCharacters(String nameStartsWith, String limit, String offset, String criteria, String sort) throws Exception {
        if(nameStartsWith.equals("throwException")){
            throw new InternalServerError();
        }
        String apiResponse = "{\"code\":200,\"status\":\"Ok\",\"data\":{\"offset\":" + offset + ",\"limit\":" + limit + ",\"total\":1485,\"results\":[{\"id\":1011334,\"name\":\"3-D Man\",\"description\":\"\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1011334\"},{\"id\":1017100,\"name\":\"A-Bomb (HAS)\",\"description\":\"Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction! \",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1017100\"},{\"id\":1009144,\"name\":\"A.I.M.\",\"description\":\"AIM is a terrorist organization bent on destroying the world.\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/6/20/52602f21f29ec\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1009144\"},{\"id\":1010699,\"name\":\"Aaron Stack\",\"description\":\"\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1010699\"},{\"id\":1009146,\"name\":\"Abomination (Emil Blonsky)\",\"description\":\"Formerly known as Emil Blonsky, a spy of Soviet Yugoslavian origin working for the KGB, the Abomination gained his powers after receiving a dose of gamma radiation similar to that which transformed Bruce Banner into the incredible Hulk.\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/9/50/4ce18691cbf04\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1009146\"},{\"id\":1016823,\"name\":\"Abomination (Ultimate)\",\"description\":\"\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1016823\"},{\"id\":1009148,\"name\":\"Absorbing Man\",\"description\":\"\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/1/b0/5269678709fb7\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1009148\"},{\"id\":1009149,\"name\":\"Abyss\",\"description\":\"\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/9/30/535feab462a64\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1009149\"},{\"id\":1010903,\"name\":\"Abyss (Age of Apocalypse)\",\"description\":\"\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/3/80/4c00358ec7548\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1010903\"},{\"id\":1011266,\"name\":\"Adam Destine\",\"description\":\"\",\"elected_times\":0,\"thumbnail\":{\"path\":\"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available\",\"extension\":\"jpg\"},\"resource_uri\":\"http://gateway.marvel.com/v1/public/characters/1011266\"}]}}";
        Gson gson = new GsonBuilder().create();
        GetMarvelCharacters getMarvelCharacters = gson.fromJson(apiResponse, GetMarvelCharacters.class);
        return getMarvelCharacters;
    }

}
