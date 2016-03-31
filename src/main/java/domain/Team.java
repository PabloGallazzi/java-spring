package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 3/29/16.
 */
public class Team {
    private String name;
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
