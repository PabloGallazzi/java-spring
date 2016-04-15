package domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by niko118 on 3/31/16.
 */
public class UserTest {

    private User user1;

    @Before
    public void setUp() throws Exception {
        user1 = new User("Test","Test");
    }

    @Test
    public void testGetUsername() throws Exception {
        User user = new User("testName", "");
        assertEquals(user.getUserName(),"testName");
    }

    @Test
    public void testSetUsername() throws Exception {
        User user = new User("test", "");
        user.setUserName("test1");
        assertEquals(user.getUserName(),"test1");
    }

    @Test
    public void testSetPassword() throws Exception {
        User user = new User("", "test");
        user.setUserPassword("test2");
        assertTrue(user.passwordIsCorrect("test2"));
    }

    @Test
    public void testGetFavorites() throws Exception {
        User user = new User("", "");
        Character character = new Character(1, "IronMan", "Rich, famous, filantropous.");
        user.getFavorites().add(character);
        assertTrue(user.getFavorites().contains(character));
    }

    @Test
    public void testGetTeams() throws Exception {
        User user = new User("", "");
        Team team = new Team("");
        user.getTeams().add(team);
        assertTrue(user.getTeams().contains(team));
    }

    @Test
    public void testGetLastAccess() throws Exception {
        User user = new User("", "");
        Date dateNow = new Date();
        user.setLastAccess(dateNow);
        assertEquals(user.getLastAccess(),dateNow);
    }

    @Test
    public void testSetLastAccess() throws Exception {
        User user = new User("", "");
        Date dateNow = new Date();
        user.setLastAccess(dateNow);
        assertEquals(user.getLastAccess(),dateNow);
    }

    @Test
    public void testPasswordIsCorrect() throws Exception {
        User user = new User("","test");
        assertTrue(user.passwordIsCorrect("test"));
    }

    @Test
    public void testGetPassword() throws Exception {
        String passwordTest = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        User user = new User("", "test");
        assertEquals(user.getUserPassword(), passwordTest);
    }
}