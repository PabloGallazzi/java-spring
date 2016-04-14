package domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("characters")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Character {
    @Id
    private Integer characterId;
    String name;
    Integer electedTimes = 0;

    public Integer getElectedTimes() {
        return electedTimes;
    }

    public void setElectedTimes(Integer electedTimes) {
        this.electedTimes = electedTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character() {/*Necessary for Mongo*/}

    public Character(Integer id) {
        this.characterId = id;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }
}
