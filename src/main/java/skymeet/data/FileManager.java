package skymeet.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import skymeet.model.Location;

import static skymeet.resource.FirebaseTokenResource.demoToken;
import static skymeet.resource.FlightNearResource.userLocationLastRemembered;

public class FileManager {
    public static final String PATHNAME_USER_TOKEN = "userToken.txt";
    public static final String PATHNAME_USER_REMEMBERED_LOCATION = "userRememberedLocation.txt";

    private static FileManager instance;

    private FileManager() {

    }

    public static FileManager getInstance() {
        if (instance == null)
            instance = new FileManager();
        return instance;
    }

    private void writeToFile(File file, String data) {
        try (FileWriter fr = new FileWriter(file)) {
            System.out.println();
            System.out.println("New saved data: " + data);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTokenToFile(String token) {
        File file = new File(PATHNAME_USER_TOKEN);

        if (!file.exists()) {
            System.out.println();
            System.out.println("Created NEW TOKEN: " + token);
        }

        writeToFile(file, token);
    }

    public void writeUserLocationToFile(Location location) {
        File file = new File(PATHNAME_USER_REMEMBERED_LOCATION);

        writeToFile(file, location.toString());
    }

    private String readFromFile(String pathName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pathName)));
    }

    public String readFirebaseTokenFromFile() throws IOException {
        demoToken = readFromFile(PATHNAME_USER_TOKEN);
        if (!demoToken.isEmpty()) {
            System.out.println("Read TOKEN from file: " + demoToken);
        }
        return demoToken;
    }

    public Location readRememberedUserLocationFromFile() throws IOException {
        String content = readFromFile(PATHNAME_USER_REMEMBERED_LOCATION);
        String[] latlon = content.split(", ");
        return userLocationLastRemembered = new Location(Double.parseDouble(latlon[0]), Double.parseDouble(latlon[1]));
    }
}

