package skymeet.util;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

import org.junit.Test;

import skymeet.model.Location;

import static org.junit.Assert.assertEquals;

//@RunWith(Arquillian.class)
public class DistanceHelperTest {
//    @Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//                .addClass(DistanceHelper.class)
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }

    private final double kewLat = 51.474448;
    private final double kewLong = 5.302997;
    private Location locationKew = new Location(kewLat, kewLong); //Kew, London

    private final double richLat = 51.479335;
    private final double richLong = 5.338404;
    private Location locationRichmond = new Location(richLat, richLong); //Richmond, London

    @Test
    public void distanceBetweenLocationsInMetres() {
        Coordinate lat = Coordinate.fromDegrees(locationKew.getLatitude());
        Coordinate lng = Coordinate.fromDegrees(locationKew.getLongitude());
        Point pointKew = Point.at(lat, lng);

        lat = Coordinate.fromDegrees(locationRichmond.getLatitude());
        lng = Coordinate.fromDegrees(locationRichmond.getLongitude());
        Point pointRichmond = Point.at(lat, lng);

        double distance = EarthCalc.gcdDistance(pointKew, pointRichmond); //in meters

        System.out.println("Distance is: " + distance);
        assertEquals(distance, DistanceHelper.distanceBetweenLocationsInMetres(locationKew, locationRichmond), 0.00001);
    }
}
