package skymeet.util;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

import skymeet.model.Location;

public class DistanceHelper {

    public static double distanceBetweenLocationsInMetres(Location from, Location to) {
        Coordinate lat = Coordinate.fromDegrees(from.getLatitude());
        Coordinate lng = Coordinate.fromDegrees(from.getLongitude());
        final Point pointFrom = Point.at(lat, lng);

        lat = Coordinate.fromDegrees(to.getLatitude());
        lng = Coordinate.fromDegrees(to.getLongitude());
        final Point pointTo = Point.at(lat, lng);

        return EarthCalc.gcdDistance(pointTo, pointFrom);
    }

    public static double distanceBetweenLocationsInKm(Location from, Location to) {
        return distanceBetweenLocationsInMetres(from, to) / 1000;
    }

}
