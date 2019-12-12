package skymeet.model;

import com.google.gson.Gson;

import java.util.List;

public class Flight {
    private Aircraft aircraft;
    private Operator operatedBy;
    private List<FlightPosition> flightPositions;

    public Flight(Aircraft aircraft, List<FlightPosition> flightPositions, Operator operatedBy) {
        this.aircraft = aircraft;
        this.flightPositions = flightPositions;
        this.operatedBy = operatedBy;
    }

    public Operator getOperatedBy() {
        return operatedBy;
    }

    public void setOperatedBy(Operator operatedBy) {
        this.operatedBy = operatedBy;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public List<FlightPosition> getFlightPositions() {
        return flightPositions;
    }

    public void setFlightPositions(List<FlightPosition> flightPositions) {
        this.flightPositions = flightPositions;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
