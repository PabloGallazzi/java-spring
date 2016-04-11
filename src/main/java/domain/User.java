package domain;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexDirection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("users")
public class User {
    @Id
    private ObjectId user_id;
    @Indexed(name="user", unique=true)
    private String user_name;
    private String user_password;
    @Reference(lazy = true)
    private List<Character> favorites;
    @Reference(lazy = true)
    private List<Team> teams;
    private Date lastAccess;

    public User(){/*Necessary for Mongo*/}

    public User(String username, String password) {
        this.user_name = username;
        this.setPassword(password);
        this.lastAccess = new Date();
        this.favorites = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String username) {
        this.user_name = username;
    }

    public void setPassword(String password) {
        this.user_password = DigestUtils.sha256Hex(password);
    }

    public String getPassword() {
        return this.user_password;
    }

    public boolean passwordIsCorrect(String password){
        return this.user_password.equals(DigestUtils.sha256Hex(password));
    }

    public List<Character> getFavorites() {
        return favorites;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }
}
