package io.github.dinolupo.demo;

import org.mongodb.morphia.annotations.Id;

/**
 * Created by dino on 02/08/15.
 */
public class PizzaOrder {

    //    {
    //        "type": {
    //                "name": "Margherita",
    //                "family": "Traditional"
    //    },
    //        "size": "medium",
    //        "customer": "Dino Lupo",
    //        "restaurantShopId": 1
    //    }

    private String customer;
    private String size;
    private long restaurantShopId;
    private String[] selectedOptions;
    private PizzaType type;

    @Id
    private String id;

    public String getCustomer() {
        return customer;
    }

    public String getSize() {
        return size;
    }

    public long getRestaurantShopId() {
        return restaurantShopId;
    }

    public String[] getSelectedOptions() {
        return selectedOptions;
    }

    public PizzaType getType() {
        return type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
