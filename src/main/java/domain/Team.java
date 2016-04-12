package domain;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
@Entity("teams")
public class Team {

    @Id
    private Integer team_id;
    private String team_name;
    @Reference(lazy = true)
    private List<Character> members;

    public Team(){/*Necessary for Mongo*/}

    public Team(String name) {
        this.team_name = name;
        //this.id = new ObjectId();
        this.members = new ArrayList<>();
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String name) {
        this.team_name = name;
    }

    public List<Character> getMembers() {
        return members;
    }

    public void setMembers(List<Character> members) {
        this.members = members;
    }

}
