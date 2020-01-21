package skymeet.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import skymeet.Publisher;
import skymeet.data.ActiveFlightsManager;
import skymeet.data.FileManager;
import skymeet.model.Flight;
import skymeet.model.Location;
import skymeet.util.DistanceHelper;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(Publisher.resourceFlightsNear)
public class FlightNearResource {

    private static final int DEFAULT_RADIUS_KM = 100;

    private ActiveFlightsManager activeFlightsManager = ActiveFlightsManager.getInstance();

    public static Location userLocationLastRemembered;

    @GET
    public String testConnection() {
        return "Success connect :)";
    }

    private void saveUserLocation(double lat, double lon) {
        userLocationLastRemembered = new Location(lat, lon);
        FileManager.getInstance().writeUserLocationToFile(userLocationLastRemembered);
    }

    @GET
    @Path("{lat}/{lon}/{kmRadius}")
    public Response getFlightsNearLocation(@PathParam("lat") double lat, @PathParam("lon") double lon, @PathParam("kmRadius") int kmRadius) {
        if (lat > 90 || lat < -90) {
            return Response.serverError().entity("Latitude must be between 90 & -90").build();
        }
        if (lon > 180 || lon < -180) {
            return Response.serverError().entity("Longitude must be between 180 & -180").build();
        }
        if (kmRadius <= 0) {
            kmRadius = DEFAULT_RADIUS_KM;
        }
//        if (kmRadius > 5000) {
//            return Response.serverError().entity("Supported only searching at a max radius of 5000 kilometres").build();
//        }

        saveUserLocation(lat, lon);

        Set<Flight> responseList = new HashSet<>();
        for (Flight flight : activeFlightsManager.getFlightList()) {
            //if distance between user && flight current location is less than specified kmRadius
            if (DistanceHelper.distanceBetweenLocationsInKm(userLocationLastRemembered, flight.getFlightPositions().get(0).getLocation()) < kmRadius) {
                responseList.add(flight);
            }
        }
        System.out.println("\n\n\n");
        System.out.println("ResponseLIST: " + responseList);

        Response response = Response.ok().entity(responseList).build();
        System.out.println("getFlightsNearLocation() Lat: " + lat + " Lon: " + lon + " kmRadius: " + kmRadius +
                "\nReturning flight list: " + response);
        return response;
    }

    @GET
    @Path("{lat}/{lon}")
    public Response getFlightsNearLocation(@PathParam("lat") double lat, @PathParam("lon") double lon) {
        return getFlightsNearLocation(lat, lon, -1);
    }

    public static boolean flagWestMoved = false;

    @GET
    @Path("{lat}/{lon}/{kmRadius}/west/")
    public Response getWestFlight(@PathParam("lat") double lat, @PathParam("lon") double lon, @PathParam("kmRadius") int kmRadius) {
        if (flagWestMoved) //if moved return empty
            return Response.ok().entity(new ArrayList(0)).build();

        if (kmRadius <= 0) {
            kmRadius = DEFAULT_RADIUS_KM;
        }

        saveUserLocation(lat, lon);

        Set<Flight> responseList = new HashSet<>();
        //if distance between user && flight current location is less than specified kmRadius
        Flight flightWest = activeFlightsManager.getFlightList().get(0);
        if (DistanceHelper.distanceBetweenLocationsInKm(userLocationLastRemembered, flightWest
                .getFlightPositions().get(0).getLocation()) < kmRadius) {
            responseList.add(flightWest);
        }
        return Response.ok().entity(responseList).build();
    }

    @GET
    @Path("{lat}/{lon}/{kmRadius}/north/")
    public Response getNorthFlights(@PathParam("lat") double lat, @PathParam("lon") double lon, @PathParam("kmRadius") int kmRadius) {
        List<Flight> flightsNorth = new ArrayList<>();
        if (!flagWestMoved) { //if west not moved yet return only 1 north
            flightsNorth.add(activeFlightsManager.getFlightList().get(1));
        } else { //west and north
            flightsNorth.add(activeFlightsManager.getFlightList().get(0));
            flightsNorth.add(activeFlightsManager.getFlightList().get(1));

        }
        //else return west and north flights

        if (kmRadius <= 0) {
            kmRadius = DEFAULT_RADIUS_KM;
        }
//        if (kmRadius > 5000) {
//            return Response.serverError().entity("Supported only searching at a max radius of 5000 kilometres").build();
//        }

        saveUserLocation(lat, lon);

        Set<Flight> responseList = new HashSet<>();
        for (Flight flight : flightsNorth) {
            //if distance between user && flight current location is less than specified kmRadius
            if (DistanceHelper.distanceBetweenLocationsInKm(userLocationLastRemembered, flight.getFlightPositions().get(0).getLocation()) < kmRadius) {
                responseList.add(flight);
            }
        }
        return Response.ok().entity(responseList).build();
    }


}
