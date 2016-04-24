package repositories;

import domain.Character;
import domain.Team;
import org.bson.types.ObjectId;
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
public class TeamsRepository {
    @Autowired
    private DSMongoInterface ds;
    @Autowired
    private CharactersRepository charactersRepository;

    public Team findByTeamId(String id) {
        return ds.getDatastore().find(Team.class, "teamId", new ObjectId(id)).get();
    }

    public Team save(Team team) {
        iterateCharacters(team.getMembers());
        ObjectId objectId = (ObjectId) ds.getDatastore().save(team).getId();
        team.setTeamId(objectId);
        return team;
    }

    private void iterateCharacters(List<Character> characters){
        for (Character character : characters){
            charactersRepository.save(character);
        }
    }
}
