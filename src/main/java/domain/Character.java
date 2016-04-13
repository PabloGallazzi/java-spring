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
    private Integer character_id;
    private String name;
    private String description;
    private Integer elected_times = 0;
    private String eTag;

    public Integer getElected_times() {
        return elected_times;
    }

    public void setElected_times(Integer elected_times) {
        this.elected_times = elected_times;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character() {/*Necessary for Mongo*/}

    public Character(Integer id, String name, String description) {
        this.character_id = id;
        this.name = name;
        this.description = description;
    }

    public Integer getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(Integer character_id) {
        this.character_id = character_id;
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
