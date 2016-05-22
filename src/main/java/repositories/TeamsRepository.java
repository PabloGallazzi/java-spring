package repositories;

import domain.Character;
import domain.Team;
import exceptions.rest.BadRequestException;
import exceptions.rest.NotFoundException;
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
        Team team;
        try {
            team = ds.getDatastore().find(Team.class, "teamId", new ObjectId(id)).get();
        } catch (Exception e) {
            throw new BadRequestException("Invalid id " + id);
        }
        if (team == null) {
            throw new NotFoundException("Team with id " + id + " was not found");
        }
        return team;
    }

    public Team save(Team team) {
        iterateCharacters(team.getMembers());
        ObjectId objectId = (ObjectId) ds.getDatastore().save(team).getId();
        team.setTeamId(objectId);
        return team;
    }

    public void update(Team team) {
        ds.getDatastore().delete(team);
        ds.getDatastore().save(team).getId();
    }

    private void iterateCharacters(List<Character> characters){
        for (Character character : characters){
            charactersRepository.save(character);
        }
    }
}
