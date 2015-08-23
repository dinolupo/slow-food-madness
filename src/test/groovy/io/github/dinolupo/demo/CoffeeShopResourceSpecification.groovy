package io.github.dinolupo.demo

import com.mongodb.MongoClient
import org.bson.types.ObjectId
import spock.lang.Ignore
import spock.lang.Specification

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

class CoffeeShopResourceSpecification extends Specification {

    def 'should return closest restaurant with pizza to Naples (Italy)'() {
        given:
        def mongoClient = new MongoClient()
        def restaurant = new RestaurantWithPizzaResource(mongoClient)

        when:
        double latitude = 40.8517746
        double longitude = 14.2681244
        def nearestRestaurant = restaurant.getNearest(longitude, latitude)

        then:
        nearestRestaurant.name == 'Pizzeria Trianon'
        println nearestRestaurant
    }

    @Ignore('Not implemented yet')
    def 'should return 404 if no coffee shop found'() {
        given:
        def mongoClient = new MongoClient()
        def restaurant = new RestaurantWithPizzaResource(mongoClient)

        when:
        double latitude = 37.3981841
        double longitude = -5.9776375999999996
        def nearestShop = restaurant.getNearest(latitude, longitude)

        then:
        def exception = thrown(WebApplicationException)
        exception.response.status == Response.Status.NOT_FOUND.statusCode
    }

    //functional test
    def 'should save all fields to the database when order is saved'() {
        given:
        def mongoClient = new MongoClient()
        def database = mongoClient.getDatabase("SlowFoodMadnessDB")
        def collection = database.getCollection('PizzaOrder')
        collection.drop();

        def restaurant = new RestaurantWithPizzaResource(mongoClient)

        def pizzaType = new PizzaType()
        pizzaType.with {
            family = 'Red'
            name = 'Margherita'
        }

        def order = new PizzaOrder()
        order.with {
            size = 'medium'
            customer = 'Dino Lupo'
            type = pizzaType
            selectedOptions = ['+ bacon']
            restaurantShopId = 89439
        }

        when:
        Response response = restaurant.saveOrder(order);

        then:
        collection.count() == 1
        def createdOrder = collection.find().limit(1).first()
        createdOrder['selectedOptions'] == order.selectedOptions
        createdOrder['type'].name == order.type.name
        createdOrder['type'].family == order.type.family
        createdOrder['size'] == order.size
        createdOrder['customer'] == order.customer
        createdOrder['restaurantShopId'] == order.restaurantShopId
        createdOrder['_id'] != null
        createdOrder['prettyString'] == null

        cleanup:
        mongoClient.close()
    }

    //functional test
    @Ignore('Not implemented yet')
    def 'should return me an existing order'() {
        given:
        def mongoClient = new MongoClient()
        def database = mongoClient.getDatabase("SlowFoodMadnessDB")

        def restaurant = new RestaurantWithPizzaResource(database)
        def pizzaType = new PizzaType()
        pizzaType.with {
            family = 'Red'
            name = 'Margherita'
        }
        def expectedOrder = new PizzaOrder()
        expectedOrder.with {
            size = 'standard'
            customer = 'Yo'
            type = pizzaType
        }

        def coffeeShopId = 89438
        restaurant.saveOrder(coffeeShopId, expectedOrder);

        when:
        PizzaOrder actualOrder = restaurant.getOrder(coffeeShopId, expectedOrder.getId());

        then:
        actualOrder != null
        actualOrder == expectedOrder

        cleanup:
        mongoClient.close()
    }

    //functional test
    @Ignore('Not implemented yet')
    def 'should throw a 404 if the order is not found'() {
        given:
        def mongoClient = new MongoClient()
        def database = mongoClient.getDB("Cafelito")
        def coffeeShop = new RestaurantWithPizzaResource(database)

        when:
        coffeeShop.getOrder(7474, new ObjectId().toString());

        then:
        def e = thrown(WebApplicationException)
        e.response.status == 404

        cleanup:
        mongoClient.close()
    }

}
