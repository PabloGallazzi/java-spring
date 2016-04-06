package database;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import domain.Character;
import domain.Team;
import domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.DSMongoInterface;

import java.io.Console;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;

/**
 * Created by niko118 on 4/2/16.
 */
public class MongoIntegrationTest {
    private MongoClient client;
    private MongoServer server;
    @Autowired
    @Qualifier("mongomemory")
    private DSMongoInterface ds;

    @Before
    public void setUp() throws Exception{
        BeanFactory factory = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");
        ds = (DSMongoInterface) factory.getBean("mongomemory");
        insertTestData();
    }

    private void insertTestData() {
        User user = new User("TACS","test");
        Team team1 = new Team("Teamtest1");
        Team team2 = new Team("Teamtest2");
        Character character1 = new Character(1);
        Character character2 = new Character(2);
        Character character3 = new Character(3);
        team1.getMembers().add(character1);
        team1.getMembers().add(character2);
        team2.getMembers().add(character3);
        user.getFavorites().add(character1);
        user.getTeams().add(team1);
        user.getTeams().add(team2);
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
        User userFind = ds.getDatastore().find(User.class, "username", "TACS").get();
        assertEquals("TACS",userFind.getUsername());
    }

    @Test
    public void testMongoSearchAndDontFind() throws Exception{
        User userFind = ds.getDatastore().find(User.class, "username", "TACSDontGet").get();
        assertNull(userFind);
    }


}