package database;

import com.mongodb.MongoClient;
import de.bwaldvogel.mongo.MongoServer;
import domain.Character;
import domain.Team;
import domain.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import services.DSMongoInterface;

import static org.junit.Assert.*;

/**
 * Created by niko118 on 4/2/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/app-context.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class MongoIntegrationTest {
    private MongoClient client;
    private MongoServer server;
    @Autowired
    private DSMongoInterface ds;

    @Before
    public void setUp() throws Exception{
        insertTestData();
    }

    private void insertTestData() {
        User user = new User("TACS","test");
        user.setUser_id(1);
        Team team1 = new Team("Teamtest1");
        team1.setTeam_id(1);
        Team team2 = new Team("Teamtest2");
        team2.setTeam_id(2);
        Character character1 = new Character(1);
        Character character2 = new Character(2);
        Character character3 = new Character(3);
        team1.getMembers().add(character1);
        team1.getMembers().add(character2);
        team2.getMembers().add(character3);
        user.getFavorites().add(character1);
        user.getTeams().add(team1);
        user.getTeams().add(team2);
        ds.getDatastore().getDB().dropDatabase();
        ds.getDatastore().save(character1);
        ds.getDatastore().save(character2);
        ds.getDatastore().save(character3);
        ds.getDatastore().save(team1);
        ds.getDatastore().save(team2);
        ds.getDatastore().save(user);
    }

    @After
    public void tearDown() throws Exception{
        ds.stopDatastore();
    }

    @Test
    public void testMongoSearchAndFind() throws Exception{
        User userFind = ds.getDatastore().find(User.class, "user_name", "TACS").get();
        assertEquals("TACS",userFind.getUser_name());
    }

    @Test
    public void testMongoSearchAndDontFind() throws Exception{
        User userFind = ds.getDatastore().find(User.class, "user_name", "TACSDontGet").get();
        assertNull(userFind);
    }


}