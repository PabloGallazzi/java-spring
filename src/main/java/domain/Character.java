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
    private String description;
    private String eTag;

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

    public Character(Integer id, String name, String description) {
        this.characterId = id;
        this.name = name;
        this.description = description;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public void selectedAsFavorite(){
        elected_times++;
    }

    public void removedAsFavorite(){
        elected_times--;
    }
}
