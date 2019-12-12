package skymeet;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import skymeet.data.ActiveFlightsManager;
import skymeet.model.Flight;
import skymeet.model.Location;
import skymeet.resource.FlightResources;

// The Java class will be hosted at the URI path "/helloworld"
public class Publisher {

    public static final String resourceFlightsNear = "flightsNear";
    private static final int PORT = 9095;
    private static final String URL = "http://localhost:" + PORT + "/";

    public static void main(String[] args) {
        final URI baseUri = UriBuilder.fromUri(URL).build();
        ResourceConfig resourceConfig = new ResourceConfig(FlightResources.class);
        JdkHttpServerFactory.createHttpServer(baseUri, resourceConfig, true);
        System.out.println("Service hosted at " + baseUri + resourceFlightsNear);
        System.out.println("Enter \"D!\" in console to enter developer mode...");


        prepareDevMode();
    }

    private static void showList(List<Flight> list) {
        System.out.println("Flight list:");
        for (int i = 0; i < list.size(); i++) {
            Flight flight = list.get(i);
            System.out.println("#" + i + ": " + flight);
        }
    }

    private static void prepareDevMode() {
        ActiveFlightsManager activeFlightsManager = ActiveFlightsManager.getInstance();

        boolean flagDevMode = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                if (flagDevMode || reader.readLine().equals("D!")) {
                    flagDevMode = true;
                    System.out.println("Entered developer mode! Enter \"q!\" at any point to go back.");
                    System.out.println("");

                    List<Flight> flightList = activeFlightsManager.getFlightList();
                    showList(flightList);

                    System.out.println("");
                    System.out.println("Choose flight you'd like to move:");

                    boolean flagFlightUpdated = false;
                    while (!flagFlightUpdated) {
                        try {
//                            if (reader.readLine().equals("q!")) {
//                                flagDevMode = false;
//                                break;
//                            }
                            String line = reader.readLine();
                            if (line.equalsIgnoreCase("list")) {
                                break;
                            }
                            int indexFlight = Integer.parseInt(line);
                            System.out.println();
                            System.out.println("Enter new latitude and longitude of this flight, separated by comma & 1 white space character (eg. \"51.5555, 23.6666\")");
                            System.out.println();
                            while (!flagFlightUpdated) {
                                line = reader.readLine();
                                try {
                                    if (!line.isEmpty()) {
                                        String[] latlon = line.trim().split(", ");
                                        if (latlon.length != 2) {
                                            System.err.println("Wrong Lat/Lon format (see example above)!");
                                            continue;
                                        }
                                        double lat = Double.parseDouble(latlon[0]);
                                        double lon = Double.parseDouble(latlon[1]);

                                        System.out.print("Moved flight - " + flightList.get(indexFlight).getAircraft());
                                        Location location = new Location(lat, lon);
                                        activeFlightsManager.moveFlight(indexFlight, location);
                                        System.out.println(" to new location - " + location);
                                        System.out.println();

                                        System.out.println("Updated flight list:");
                                        flagFlightUpdated = true;
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    System.out.println("Try again:");
                                }
                            }


                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                            System.out.println("Try again:");
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Dev mode closed!");
    }
}
