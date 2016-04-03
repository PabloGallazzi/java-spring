package domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
@Embedded
public class Team {
    @Id private ObjectId id;
    private String name;
    @Reference
    private List<Character> members;

    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Character> getMembers() {
        return members;
    }

}