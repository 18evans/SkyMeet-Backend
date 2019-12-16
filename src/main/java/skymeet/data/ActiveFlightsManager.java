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
            new FlightPosition(new Location(51.459956, 5.215027)), //W
            new FlightPosition(new Location(51.612280, 5.442877)), //N
            new FlightPosition(new Location(51.465490, 5.876176)), //E
            new FlightPosition(new Location(51.228169, 5.510068))  //S
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
