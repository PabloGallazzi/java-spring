package repositories;

import domain.User;
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
public class UsersRepository {
    @Autowired
    private DSMongoInterface ds;

    public List<User> getUsers() {
        return ds.getDatastore().find(User.class).retrievedFields(false, "teams", "favorites").asList();
    }

    public User findByUserNameAndPassword(String userName, String password) {
        User aUser = ds.getDatastore().find(User.class, "userName", userName).get();
        return aUser != null ? aUser.passwordIsCorrect(password) ? aUser : null : null;
    }

    public User findByUserId(String id) {
        User user;
        try {
            user = ds.getDatastore().find(User.class, "userId", new ObjectId(id)).get();
        } catch (Exception e) {
            throw new NotFoundException();
        }
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    public User save(User newUser) {
        ObjectId objectId = (ObjectId) ds.getDatastore().save(newUser).getId();
        newUser.setUserId(objectId);
        return newUser;
    }

    public void update(User user) {
        ds.getDatastore().delete(user);
        ds.getDatastore().save(user);
    }

}
