package domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("teams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team {

    @Id
    private Integer teamId;
    private String teamName;
    @Reference(lazy = true)
    private List<Character> members;

    public Team(){/*Necessary for Mongo*/}

    public Team(String name) {
        this.teamName = name;
        //this.id = new ObjectId();
        this.members = new ArrayList<>();
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String name) {
        this.teamName = name;
    }

    public List<Character> getMembers() {
        return members;
    }

    public void setMembers(List<Character> members) {
        this.members = members;
    }

}
