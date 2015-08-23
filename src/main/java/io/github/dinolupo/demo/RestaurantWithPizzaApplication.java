package io.github.dinolupo.demo;

import com.mongodb.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by dino on 31/07/15.
 */



public class RestaurantWithPizzaApplication extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new RestaurantWithPizzaApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        AssetsBundle assetsBundle = new AssetsBundle("/html","/");
        bootstrap.addBundle(assetsBundle);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        MongoClient mongoClient = new MongoClient();
        environment.lifecycle().manage(new MongoClientManager(mongoClient));
        environment.jersey().register(new RestaurantWithPizzaResource(mongoClient));
    }
}

