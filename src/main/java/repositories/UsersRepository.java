package repositories;

import domain.User;
import exceptions.rest.NotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import services.DSMongoInterface;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

/**
 * Created by niko118 on 4/19/16.
 */
@Repository
@Scope("singleton")
public class UsersRepository {
    @Autowired
    private DSMongoInterface ds;

    public List<HashMap<String, Object>> getUsers() {
        List<User> users = ds.getDatastore().find(User.class).retrievedFields(false, "teams", "favorites").asList();
        List<HashMap<String, Object>> lightUsers = new LinkedList<>();
        for (User user : users) {
            HashMap<String, Object> lightUser = new HashMap<String, Object>();
            lightUser.put("user_id", user.getUserId().toString());
            lightUser.put("user_name", user.getUserName());
            lightUser.put("last_access", user.getLastAccess());
            lightUser.put("admin", user.isAdmin());
            lightUsers.add(lightUser);
        }
        return lightUsers;
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
