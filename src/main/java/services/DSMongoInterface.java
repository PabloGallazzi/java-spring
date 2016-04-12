package services;

import org.mongodb.morphia.Datastore;

/**
 * Created by niko118 on 4/6/16.
 */
public interface DSMongoInterface {
    Datastore getDatastore();
    void stopDatastore();
}
