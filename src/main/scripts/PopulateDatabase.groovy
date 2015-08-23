/**
 * Created by dino on 21/08/15.
 */

/*

 http://www.overpass-api.de/api/xapi?*[amenity=restaurant][cuisine=pizza]

 <node id="16351361" lat="49.5517432" lon="8.6756383">
    <tag k="addr:housenumber" v="108"/>
    <tag k="addr:street" v="Grundelbachstraße"/>
    <tag k="amenity" v="restaurant"/>
    <tag k="cuisine" v="pizza"/>
    <tag k="name" v="Galileo"/>
    <tag k="opening_hours" v="Mo-Su 11:00-14:00,17:00-23:00; We off"/>
    <tag k="phone" v="+49 6201 68883"/>
    <tag k="website" v="http://galileo.sx64.de/"/>
    <tag k="wheelchair" v="yes"/>
  </node>
* */

import org.bson.Document
import com.mongodb.MongoClient
import com.mongodb.BasicDBObject

def mongoClient = new MongoClient();
def collection = mongoClient.getDatabase("SlowFoodMadnessDB").getCollection("RestaurantWithPizza");
collection.drop();

def xmlSlurper = new XmlSlurper().parse(new File("resources/all-restaurants-pizza.xml"));

xmlSlurper.node.each { child ->
    Map restaurant = [  openStreetMapId: child.@id.text(),
                        location:       [coordinates: [Double.valueOf(child.@lon.text()),
                                                       Double.valueOf(child.@lat.text())],
                                        type:          "Point"
                                        ]
                     ];

    child.tag.each { theNode ->
        def fieldName = theNode.@k.text();
        if (isValidFieldName(fieldName)) {
            restaurant.put(fieldName, theNode.@v.text());
        }
    }

    if (restaurant.name != null) {
        println restaurant;
        collection.insertOne(restaurant as Document);
    }

    collection.createIndex(new BasicDBObject('location', '2dsphere'));

}

private boolean isValidFieldName(fieldName) {
    return !fieldName.contains('.') && fieldName != 'location';
}