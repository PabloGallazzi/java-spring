package domain;


import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("characters")
public class Character {
    @Id private Integer id;

    public Character(){/*Necessary for Mongo*/}

    public Character(Integer id) {
        this.id = id;
    }
}
