package domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by niko118 on 3/31/16.
 */
public class TeamTest {
    private Team team1;

    @Before
    public void setUp() throws Exception {
        team1 = new Team("EquipoTest");
    }

    @Test
    public void testGetName() throws Exception {
        Team team = new Team("Equipo1");
        assertEquals(team.getTeamName(),"Equipo1");
    }

    @Test
    public void testSetName() throws Exception {
        Team team = new Team("Equipo1");
        team.setTeamName("Equipo2");
        assertEquals(team.getTeamName(),"Equipo2");
    }

    @Test
    public void testGetMembers() throws Exception {
        Team team = new Team("");
        Character character = new Character(1, "Thor", "Asgard guy");
        team.getMembers().add(character);
        assertTrue(team.getMembers().contains(character));
    }
}