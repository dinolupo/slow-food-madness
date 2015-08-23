package io.github.dinolupo.demo;


/**
 * Created by dino on 22/08/15.
 */
public class RestaurantWithPizza {
    private Point location;
    private String name;
    private long openStreetMapId;

    public Point getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public long getOpenStreetMapId() {
        return openStreetMapId;
    }

    public static class Point {
        private double[] coordinates;

        public double[] getCoordinates() {
            return coordinates;
        }

    }
}
