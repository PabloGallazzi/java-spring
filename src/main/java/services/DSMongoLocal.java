package services;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;

/**
 * Created by niko118 on 4/6/16.
 */
@Service
@Scope("singleton")
@Profile("mongo") //Por ahora lo dejamos así, por como se está mockeando el servicio de maven
public class DSMongoLocal implements DSMongoInterface {

    Datastore ds;
    private MongoClient client;
    @Value("${spring.connection.host}")
    private String host;
    @Value("${spring.connection.port}")
    private int port;
    @Value("${spring.connection.bdname}")
    private String bdname;

    public DSMongoLocal(){}

    @PostConstruct
    public void initialize() throws UnknownHostException {
        client = new MongoClient(host,port);
        Morphia morphia = new Morphia();
        morphia.mapPackage("domain");
        ds = morphia.createDatastore(client,bdname);
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
