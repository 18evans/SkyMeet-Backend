package skymeet.model.response;

import com.google.gson.Gson;

import skymeet.model.Flight;
import skymeet.model.FlightPosition;

public class FlightIdWithPositionResponse {
    private final FlightPosition flightPosition;
    private final int idAircraft;

    public FlightIdWithPositionResponse(Flight flight) {
        this.flightPosition = flight.getLastFlightPosition();
        this.idAircraft = flight.getAircraft().getAircraftId();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}