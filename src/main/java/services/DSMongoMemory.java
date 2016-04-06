package services;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * Created by niko118 on 4/6/16.
 */
@Service
@Scope("singleton")
@Profile("test")
public class DSMongoMemory implements DSMongoInterface {

    Datastore ds;
    private MongoClient client;
    private MongoServer server;


    public DSMongoMemory(){}

    @PostConstruct
    public void initialize(){
        // create memory server
        server = new MongoServer(new MemoryBackend());
        // bind on a random local port
        InetSocketAddress serverAddress = server.bind();
        // create connection
        client = new MongoClient(new ServerAddress(serverAddress));
        Morphia morphia = new Morphia();
        morphia.mapPackage("domain");
        ds = morphia.createDatastore(client,"bdtptacs_test");
        ds.ensureIndexes();
    }

    @Override
    public Datastore getDatastore() {
        return ds;
    }

    @Override
    public void stopDatastore() {
        client.close();
        server.shutdown();
    }
}
