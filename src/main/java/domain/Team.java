package domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import spring.utils.ObjectIdToStringSerializer;
import spring.utils.StringToObjectIdDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("teams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team {

    @JsonSerialize(using = ObjectIdToStringSerializer.class)
    @JsonDeserialize(using = StringToObjectIdDeserializer.class)
    @Id
    private ObjectId teamId;
    private String teamName;
    @Reference(lazy = true)
    private List<Character> members = new ArrayList<>();

    public Team(){/*Necessary for Mongo*/}

    public Team(String name) {
        this.teamName = name;
    }

    public ObjectId getTeamId() {
        return teamId;
    }

    public void setTeamId(ObjectId teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String name) {
        this.teamName = name;
    }

    public void addMember(Character character) {
        this.members.add(character);
    }

    public List<Character> getMembers() {
        return members;
    }

    public void setMembers(List<Character> members) {
        this.members = members;
    }

}
