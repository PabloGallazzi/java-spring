package database;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

/**
 * Created by niko118 on 4/2/16.
 */
public class MongoIntegrationTest {
    private MongoClient client;
    private MongoServer server;
    private Datastore ds;

    @Before
    public void setUp() throws Exception{
        // create memory server
        server = new MongoServer(new MemoryBackend());
        // bind on a random local port
        InetSocketAddress serverAddress = server.bind();
        // create connection
        client = new MongoClient(new ServerAddress(serverAddress));
        Morphia morphia = new Morphia();
        morphia.mapPackage("domain");
        ds = morphia.createDatastore(client,"bdtptacs_test");
    }

    @After
    public void tearDown() throws Exception{
        client.close();
        server.shutdown();
    }

    @Test
    public void testMongoConnection() throws Exception{
        User user = new User("TACS","");
        ds.save(user);
        User userFind = ds.find(User.class, "username", "TACS").get();
        assertEquals(user.getUsername(),userFind.getUsername());
    }


}