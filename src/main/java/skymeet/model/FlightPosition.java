package skymeet.model;

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
}
