package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import spring.utils.ObjectIdToStringSerializer;
import spring.utils.StringToObjectIdDeserializer;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @JsonSerialize(using = ObjectIdToStringSerializer.class)
    @JsonDeserialize(using = StringToObjectIdDeserializer.class)
    @Id
    private ObjectId userId;

    @Indexed(name="user", unique=true)
    private String userName;
    private String userPassword;
    @Reference(lazy = true)
    private List<Character> favorites;
    @Reference(lazy = true)
    private List<Team> teams;
    private Date lastAccess;

    public User(){/*Necessary for Mongo*/}

    public User(String username, String password) {
        this.userName = username;
        this.setUserPassword(password);
        this.lastAccess = new Date();
        this.favorites = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public void setUserPassword(String password) {
        this.userPassword = DigestUtils.sha256Hex(password);
    }

    @JsonIgnore
    public String getUserPassword() {
        return this.userPassword;
    }

    public boolean passwordIsCorrect(String password){
        return this.userPassword.equals(DigestUtils.sha256Hex(password));
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
    public void setFavorites(List<Character> favorites) {
        this.favorites = favorites;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getUserId() {
        return userId;
    }

}
