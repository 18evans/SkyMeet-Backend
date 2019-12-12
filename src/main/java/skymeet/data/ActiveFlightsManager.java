package skymeet.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import skymeet.model.Aircraft;
import skymeet.model.Flight;
import skymeet.model.FlightPosition;
import skymeet.model.Location;
import skymeet.model.Operator;

public class ActiveFlightsManager {

    private static ActiveFlightsManager instance;

    private final List<Flight> flightList = new ArrayList<>();

    private static final List<Aircraft> aircraftList = new ArrayList<>(Arrays.asList(
            new Aircraft("WQ356D", "WYW782347"),
            new Aircraft("FR6016", "RYR876896"),
            new Aircraft("SMH505", "DBA423DF"),
            new Aircraft("BA5A6Y", "ASGAD787")
    ));
    private static final List<FlightPosition> flightPositionList = new ArrayList<>(Arrays.asList(
            new FlightPosition(new Location(51.429515, 5.348602)),
            new FlightPosition(new Location(51.540720, 5.442034)),
            new FlightPosition(new Location(51.442825, 5.646201)),
            new FlightPosition(new Location(51.355705, 5.707136))
    ));
    private static final List<Operator> operatorList = new ArrayList<>(Arrays.asList(
            new Operator("British Airlines"),
            new Operator("RyanAir"),
            new Operator("WizzAir"),
            new Operator("KLM")
    ));

    public List<Flight> getFlightList() {
        return flightList;
    }

    private ActiveFlightsManager() {
        for (int i = 0; i < aircraftList.size(); i++) {
            final Aircraft aircraft = aircraftList.get(i);
            this.flightList.add(
                    new Flight(aircraftList.get(i),
                    new ArrayList<>(Collections.singletonList(flightPositionList.get(i))),
                    operatorList.get(i))
            );
        }
    }

    public static ActiveFlightsManager getInstance() {
        if (instance == null) {
            instance = new ActiveFlightsManager();
        }
        return instance;
    }

    public void moveFlight(Flight flight, Location location) {
        int index = flightList.indexOf(flight);
        flightList.get(index).setFlightPositions(Collections.singletonList(new FlightPosition(location)));
    }

    public void moveFlight(int index, Location location) {
        flightList.get(index).setFlightPositions(Collections.singletonList(new FlightPosition(location)));
    }
}
