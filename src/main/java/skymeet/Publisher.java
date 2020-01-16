package skymeet;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.pusher.rest.Pusher;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.UriBuilder;

import skymeet.data.ActiveFlightsManager;
import skymeet.data.FileManager;
import skymeet.model.Flight;
import skymeet.model.Location;
import skymeet.resource.FirebaseTokenResources;
import skymeet.resource.FlightResources;
import skymeet.util.DistanceHelper;

// The Java class will be hosted at the URI path "/helloworld"
public class Publisher {

    private static Pusher pusher = new Pusher(
            System.getenv("PUSHER_APP_ID"),
            System.getenv("PUSHER_APP_KEY"),
            System.getenv("PUSHER_APP_SECRET"));

    public static final String resourceFlightsNear = "flightsNear";
    private static final int PORT = 9095;
    private static final String URL = "http://192.168.178.18:" + PORT + "/";
//    private static final ExecutorService pool = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        try {
            try {
                FileManager.getInstance().readFirebaseTokenFromFile();
            } catch (NoSuchFileException e) {
                System.out.println("A user TOKEN has not been submitted yet. Awaiting app launch...");
            }
            try {
                FileManager.getInstance().readRememberedUserLocationFromFile();
            } catch (NoSuchFileException e) {
                System.out.println("A user LOCATION has not been submitted yet. Awaiting app launch...");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        final URI baseUri = UriBuilder.fromUri(URL).build();
        ResourceConfig resourceConfig = new ResourceConfig(FlightResources.class, FirebaseTokenResources.class);
        JdkHttpServerFactory.createHttpServer(baseUri, resourceConfig, true);
        System.out.println("Service hosted at " + baseUri + resourceFlightsNear);
        System.out.println("Enter \"N!\" in console to test notification...");
        System.out.println("Enter \"P!\" in console to test trigger Pusher message...");
        System.out.println("Enter \"D!\" in console to enter developer mode...");

//        initFirebase();
        initPusher();
        prepareDevMode();
    }

    private static void initFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://skymeet-adcdc.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void initPusher() {
        pusher.setCluster("eu");
    }

    private static class LocationItemPusher {
        private String latitude;
        private String longitude;
        private String heading = "360";

        private LocationItemPusher(Location location) {
            this.latitude = String.valueOf(location.getLatitude());
            this.longitude = String.valueOf(location.getLongitude());
        }

        private LocationItemPusher(Location location, String heading) {
            this.latitude = String.valueOf(location.getLatitude());
            this.longitude = String.valueOf(location.getLongitude());
            this.heading = heading;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    private static void triggerPusherMessage() {
//        LocationItemPusher arrLocation = new LocationItemPusher(new Location(51.441643, 5.469722), "360");
        LocationItemPusher[] arrLocation = new LocationItemPusher[]{
                new LocationItemPusher(new Location(51.441643, 5.469722)),
                new LocationItemPusher(new Location(51.440318, 5.468905)),
                new LocationItemPusher(new Location(51.439435, 5.468983)),
                new LocationItemPusher(new Location(51.438713, 5.468983)),
                new LocationItemPusher(new Location(51.438579, 5.470186)),
                new LocationItemPusher(new Location(51.438525, 5.471818)),
                new LocationItemPusher(new Location(51.437937, 5.471646)),
                new LocationItemPusher(new Location(51.437520, 5.470796)),
                new LocationItemPusher(new Location(51.437373, 5.470839)),
                new LocationItemPusher(new Location(51.437224, 5.471026)),
                new LocationItemPusher(new Location(51.437097, 5.471162)),
                new LocationItemPusher(new Location(51.437010, 5.471318)),
                new LocationItemPusher(new Location(51.436927, 5.471418)),
                new LocationItemPusher(new Location(51.436840, 5.471526)),
        };

        final AtomicInteger index = new AtomicInteger(0);
        Timer timer = new Timer(); // creating timer
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int i = index.get();
                System.out.println("Triggered " + index + " " + arrLocation[i].toString());
                pusher.trigger("private-channel", "new-location", arrLocation[i]);
                if (i == arrLocation.length - 1) {
                    timer.cancel();
                }
                index.incrementAndGet();
            }
        }; // creating timer task
        // scheduling the task for repeated fixed-delay execution, beginning after the specified delay
        timer.schedule(task, 0, 2000);
    }

    private static void showList(List<Flight> list) {
        System.out.println("Flight list:");
        for (int i = 0; i < list.size(); i++) {
            Flight flight = list.get(i);
            System.out.println("#" + i + ": " + flight);
        }
    }

    private static int RANGE_KM_NOTIFICATION = 1; //1km

    private static void prepareDevMode() {
        ActiveFlightsManager activeFlightsManager = ActiveFlightsManager.getInstance();
        List<Flight> flightList = activeFlightsManager.getFlightList();

        boolean flagDevMode = false;
        boolean flagFlightUpdated = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String readLine = null;
                if (!flagFlightUpdated) {
                    readLine = reader.readLine();
                }
                if (readLine != null && !readLine.equals("N!") && !readLine.equals("D!") && !readLine.equals("P!")) {
                    continue;
                }

                if (!flagFlightUpdated && readLine.equals("N!")) {
                    sendPushNotification(flightList.get(3));
                } else if (!flagFlightUpdated && readLine.equals("P!")) {
                    triggerPusherMessage();
                } else if (flagDevMode || readLine.equals("D!")) {
                    flagDevMode = true;
                    System.out.println("Entered developer mode! Enter \"q!\" at any point to go back.");
                    System.out.println("");

                    showList(flightList);

                    System.out.println("");
                    System.out.println("Choose flight you'd like to move:");

                    flagFlightUpdated = false;
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

                                do {
                                    line = reader.readLine();
                                } while (line == null || line.isEmpty());
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

                                        if (indexFlight == 0) //if west flight
                                        {
                                            FlightResources.flagWestMoved = true;
                                            Flight flightWest = flightList.get(0);
                                            if (DistanceHelper.distanceBetweenLocationsInKm(
                                                    FlightResources.userLocationLastRemembered,
                                                    flightWest.getFlightPositions().get(0).getLocation()) <= RANGE_KM_NOTIFICATION) {
                                                sendPushNotification(flightWest);
                                            }
                                        }

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

    private static void sendPushNotification(Flight flight) {
        // This registration token comes from the client FCM SDKs.
// See documentation on defining a message payload.
        if (FirebaseTokenResources.demoToken == null || FirebaseTokenResources.demoToken.isEmpty()) {
            System.err.println("Demo token null. Reset app storage.");
            return;
        }

        Message message = Message.builder()
                .putData("tail_sign", flight.getAircraft().getTailsign())
                .putData("distance", String.valueOf(RANGE_KM_NOTIFICATION))
                .setToken(FirebaseTokenResources.demoToken)
//                .setNotification(new Notification.Builder()
//                        .setTitle("Nearby tracked flight")
//                        .setBody("")
//                        .build(
//                        "$GOOG up 1.43% on the day",
//                        "$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day."))
                .build();

// Send a message to the device corresponding to the provided
// registration token.
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
// Response is a message ID string.
    }

}
