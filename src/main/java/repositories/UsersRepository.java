package repositories;

import domain.Character;
import domain.Team;
import domain.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import services.DSMongoInterface;

/**
 * Created by niko118 on 4/19/16.
 */
@Repository
@Scope("singleton")
public class UsersRepository {
    @Autowired
    private DSMongoInterface ds;

    public User findByUserNameAndPassword(String userName, String password) {
        User aUser = ds.getDatastore().find(User.class, "userName", userName).get();
        return aUser != null ? aUser.passwordIsCorrect(password) ? aUser : null : null;
    }

    public User findByUserId(String id) {
        return ds.getDatastore().find(User.class, "userId", new ObjectId(id)).get();
    }

    public User save(User newUser) {
        ObjectId objectId = (ObjectId) ds.getDatastore().save(newUser).getId();
        newUser.setUserId(objectId);
        return newUser;
    }

    public void saveWithCharacter(User newUser, Character newCharacter) {
        ds.getDatastore().save(newCharacter);
        ds.getDatastore().save(newUser);
    }

    public void saveWithTeam(User newUser, Team newTeam) {
        ds.getDatastore().save(newTeam);
        ds.getDatastore().save(newUser);
    }
}
