package skymeet.resource;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import skymeet.Publisher;
import skymeet.data.ActiveFlightsManager;
import skymeet.model.Flight;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(Publisher.resourceFlight)
public class FlightResource {

    @GET
    public Response getFlightByAircraftId(@QueryParam("aircraftId") int id) {
        System.out.println("Flight requested for aircraft id " + id);
        for (Flight flight : ActiveFlightsManager.getInstance().getFlightList()) {
            if (flight.getAircraft().getAircraftId() == id) {
                return Response.ok().entity(flight).build();
            }
        }
        return Response.status(404).build();
    }
}
