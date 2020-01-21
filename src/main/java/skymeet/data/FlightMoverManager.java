package skymeet.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import skymeet.model.Flight;
import skymeet.model.FlightPosition;
import skymeet.model.Location;
import skymeet.model.response.FlightIdWithPositionResponse;

public class FlightMoverManager {
    private static final int INTERVAL_UPDATE_FLIGHT_POSITION = 2000; //millis

    private static final Timer timer = new Timer();
    private static AtomicBoolean isTimerActive = new AtomicBoolean();
    private static FlightMoverManager instance;

    public static FlightMoverManager getInstance() {
        if (instance == null) {
            instance = new FlightMoverManager();
        }
        return instance;
    }

    public void startMovingFlights() {
        if (isTimerActive.get()) {
            System.err.println("Moving thread already ongoing, the call to submit moving didn't do anything.");
            return;
        } else {
            isTimerActive.set(true);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<FlightIdWithPositionResponse> listResponse = new ArrayList<>();
                for (int i = 0; i < ActiveFlightsManager.getInstance().getFlightList().size(); i++) { //move each flight
                    Flight flight = ActiveFlightsManager.getInstance().getFlightList().get(i);
                    final Location lastLocation = flight.getLastFlightPosition().getLocation();
                    moveFlight(flight, new Location(
                            lastLocation.getLatitude() + ((i % 2 == 0) ? 0.01 : -0.01),
                            lastLocation.getLongitude()
                    ));
                    listResponse.add(new FlightIdWithPositionResponse(flight));
                }
                PusherManager.triggerFlightsMoved(listResponse);
//                Publisher.printFlightListLatestLocation(); //reprint the list of flights with the latest positions after each move
            }
        }, 0, INTERVAL_UPDATE_FLIGHT_POSITION);
    }


    public static void moveFlight(Flight flight, Location location) {
        flight.addFlightPosition(new FlightPosition(location));
    }

}
