package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exceptions.rest.BadRequestException;
import exceptions.rest.NotFoundException;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import spring.utils.ObjectIdToStringSerializer;
import spring.utils.StringToObjectIdDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


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

    @Indexed(name = "user", unique = true)
    private String userName;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String userPassword;
    @Reference(lazy = true)
    private List<Character> favorites;
    @Reference(lazy = true)
    private List<Team> teams;
    private Date lastAccess;

    @JsonIgnore
    private boolean isAdmin = false;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public User() {/*Necessary for Mongo*/}

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
        this.userPassword = password;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public boolean passwordIsCorrect(String password) {
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

    private void encryptPassword() {
        this.userPassword = DigestUtils.sha256Hex(this.userPassword);
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void addAsFavorite(Character character) {
        favorites.add(character);
    }

    public void addNewTeam(Team team){
        teams.add(team);
    }

    public Character deleteFavorite(Integer id) {
        Character characterToRemove = null;
        for (Character character : favorites) {
            if (character.getId().equals(id))
                characterToRemove = character;
        }
        if (characterToRemove != null) {
            favorites.remove(characterToRemove);
            characterToRemove.removedAsFavorite();
        } else {
            throw new NotFoundException("Unable to remove character", "character_not_found", new String[0]);
        }
        return characterToRemove;
    }

    public static void validateUser(User userToValidate) {
        userToValidate.setUserId(null);
        validatePassword(userToValidate.getUserPassword());
        userToValidate.encryptPassword();
    }

    private static void validatePassword(String userPassword) {
        List<String> causes = new ArrayList<>();
        if (userPassword == null) {
            causes.add("must_provide_a_password");
        } else {
            if (userPassword.length() < 6) {
                causes.add("user_password_length_below_6_chars");
            }
            if (!userPassword.contains(";") && !userPassword.contains(",") && !userPassword.contains(".") && !userPassword.contains("_") && !userPassword.contains("-")) {
                causes.add("user_password_must_contain_one_of_|;,._-|");
            }
        }
        if (causes.size() != 0) {
            String[] arrayOfCauses = new String[causes.size()];
            int index = 0;
            for (String cause : causes) {
                arrayOfCauses[index] = cause;
                index++;
            }
            throw new BadRequestException("Unable to create user", "Validation error", arrayOfCauses);
        }
    }

}
