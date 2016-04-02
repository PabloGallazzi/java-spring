package domain;


import org.mongodb.morphia.annotations.Id;

/**
 * Created by niko118 on 3/29/16.
 */
public class Character {
    @Id private Integer id;

    public Character(Integer id) {
        this.id = id;
    }
}
