package repositories;

import domain.Character;
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


}
