package services;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko118 on 4/6/16.
 */
@Service
@Scope("singleton")
@Profile({"openshift"})
public class DSMongoOpenShift implements DSMongoInterface {

    Datastore ds;
    private MongoServer server;


    public DSMongoOpenShift(){}

    @PostConstruct
    public void initialize() throws UnknownHostException {
        String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
        String port = System.getenv("OPENSHIFT_MONGODB_DB_PORT");
        Integer port_number = new Integer(port);
        String user = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
        String pass = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
        String gearName = System.getenv("OPENSHIFT_GEAR_NAME");
        ServerAddress addr = new ServerAddress(host, port_number);
        List<MongoCredential> credencials = new ArrayList<MongoCredential>();
        MongoCredential cred = MongoCredential.createCredential(user, gearName, pass.toCharArray());
        credencials.add(cred);
        Morphia morphia = new Morphia();
        morphia.mapPackage("domain");
        ds = morphia.createDatastore(new MongoClient(addr,credencials),gearName);
        ds.ensureIndexes();
    }

    @Override
    public Datastore getDatastore() {
        return ds;
    }

    @Override
    public void stopDatastore() {
    }
}
