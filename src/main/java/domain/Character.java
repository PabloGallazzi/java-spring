package domain;


import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("characters")
public class Character {
    @Id
    private Integer character_id;
    String name;
    Integer elected_times;

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

    public Character(Integer id) {
        this.character_id = id;
    }

    public Integer getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(Integer character_id) {
        this.character_id = character_id;
    }
}
