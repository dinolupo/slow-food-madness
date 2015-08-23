package io.github.dinolupo.demo;

import com.mongodb.MongoClient;
import io.dropwizard.lifecycle.Managed;
import org.eclipse.jetty.util.component.LifeCycle;

/**
 * Created by dino on 03/08/15.
 */
public class MongoClientManager implements Managed {
    private MongoClient mongoClient;

    public MongoClientManager(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        mongoClient.close();
    }
}
