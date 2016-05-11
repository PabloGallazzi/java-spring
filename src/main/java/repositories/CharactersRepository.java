package repositories;

import domain.Character;
import domain.Thumbnail;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import services.DSMongoInterface;

import java.util.List;

/**
 * Created by niko118 on 4/19/16.
 */
@Repository
@Scope("singleton")
public class CharactersRepository {
    @Autowired
    private DSMongoInterface ds;


    public List<Character> getCharactersOrderedByTimesElectedDesc(int limit) {
        Query q = ds.getDatastore().createQuery(Character.class).order("-electedTimes").limit(limit);
        return q.asList();
    }

    public Character findById(Integer characterId){
        return ds.getDatastore().find(Character.class, "id", characterId).get();
    }

    //Add this in case we need to verify existence
    /*public Character findByIdVerifiedExistence(Integer characterId){
        Character character = findById(characterId);
        if (character == null){
            throw new BadRequestException("Character not found");
        }
        return character;
    }*/

    public Character save(Character character) {
        if (ds.getDatastore().find(Character.class, "id", character.getId()).get() == null){
            if (ds.getDatastore().find(Thumbnail.class, "path", character.getThumbnail().getPath()).get() == null){
                ds.getDatastore().save(character.getThumbnail());
            }
            Integer objectId = (Integer) ds.getDatastore().save(character).getId();
            character.setId(objectId);
            return character;
        }
        return character;
    }

    public void update(Character character){
        //TODO: This is ugly...
        ds.getDatastore().delete(character);
        ds.getDatastore().save(character);
    }
}
