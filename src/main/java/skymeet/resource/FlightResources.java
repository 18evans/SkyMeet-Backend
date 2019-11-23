package skymeet.resource;

import java.util.HashSet;
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
import skymeet.model.Flight;
import skymeet.model.Location;
import skymeet.util.DistanceHelper;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(Publisher.resourceFlightsNear)
public class FlightResources {

    private static final int DEFAULT_RADIUS_KM = 100;

    private ActiveFlightsManager activeFlightsManager = new ActiveFlightsManager();

    @GET
    public String testConnection() {
        return "Success connect :)";
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

        final Location userLocation = new Location(lat, lon);

        Set<Flight> responseList = new HashSet<>();
        for (Flight flight : activeFlightsManager.getFlightList()) {
            //if distance between user && flight current location is less than specified kmRadius
            if (DistanceHelper.distanceBetweenLocationsInKm(userLocation, flight.getFlightPositions().get(0).getLocation()) < kmRadius) {
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



}
