package services;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.h2.H2Backend;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by niko118 on 4/6/16.
 */
@Service
@Scope("singleton")
@Profile("develop")
public class DSMongoLocal implements DSMongoInterface {

    Datastore ds;
    private MongoClient client;
    private MongoServer server;


    public DSMongoLocal(){}

    @PostConstruct
    public void initialize() throws UnknownHostException {
        client = new MongoClient("localhost",27017);
        Morphia morphia = new Morphia();
        morphia.mapPackage("domain");
        ds = morphia.createDatastore(client,"bdtptacs_dev");
        ds.ensureIndexes();
    }

    @Override
    public Datastore getDatastore() {
        return ds;
    }

    @Override
    public void stopDatastore() {
        client.close();
    }
}
