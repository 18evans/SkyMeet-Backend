package skymeet.model;

import com.google.gson.Gson;

public class FlightPosition {

    private Location location;

    public FlightPosition(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
