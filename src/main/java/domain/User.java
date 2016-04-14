package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.codec.digest.DigestUtils;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    private Integer userId;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

}
