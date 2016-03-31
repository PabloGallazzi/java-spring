package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
public class User {
    private String username;
    private String password;
    private List<Character> favorites;
    private List<Team> teams;
    private Date lastAccess;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.lastAccess = new Date();
        this.favorites = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
