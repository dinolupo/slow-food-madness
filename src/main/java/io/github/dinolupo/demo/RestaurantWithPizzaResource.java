package io.github.dinolupo.demo;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by dino on 01/08/15.
 */

@Path("/restaurant")
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantWithPizzaResource {

    private Datastore datastore;

    public RestaurantWithPizzaResource(final MongoClient mongoClient) {
        datastore = new Morphia().createDatastore(mongoClient, "SlowFoodMadnessDB");
    }

    @Path("nearest/{longitude}/{latitude}")
    @GET
    public Object getNearest(@PathParam("longitude") double longitude,
                        @PathParam("latitude") double latitude) {

        return datastore.find(RestaurantWithPizza.class)
                        .field("location")
                        .near(longitude, latitude, true)
                        .get();
    }

    @Path("order")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveOrder(PizzaOrder order) {
        
        datastore.save(order);

        return Response.created(URI.create(order.getId())).entity(order).build();
    }
    
}
