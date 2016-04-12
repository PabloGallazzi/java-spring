package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.codec.digest.DigestUtils;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("users")
public class User {

    @Id
    private Integer user_id;
    @Indexed(name="user", unique=true)
    private String user_name;
    private String user_password;
    @Reference(lazy = true)
    private List<Character> favorites;
    @Reference(lazy = true)
    private List<Team> teams;
    private Date last_access;

    public User(){/*Necessary for Mongo*/}

    public User(String username, String password) {
        this.user_name = username;
        this.setUser_password(password);
        this.last_access = new Date();
        this.favorites = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String username) {
        this.user_name = username;
    }

    public void setUser_password(String password) {
        this.user_password = DigestUtils.sha256Hex(password);
    }

    @JsonIgnore
    public String getUser_password() {
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

    public Date getLast_access() {
        return last_access;
    }

    public void setLast_access(Date last_access) {
        this.last_access = last_access;
    }
    public void setFavorites(List<Character> favorites) {
        this.favorites = favorites;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

}
