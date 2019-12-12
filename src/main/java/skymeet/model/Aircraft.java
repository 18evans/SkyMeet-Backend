package skymeet.model;

import com.google.gson.Gson;

public class Aircraft {

    private static int NEXT_ID = 1;

    private int aircraftId;
    private String callsign;
    private String tailsign;

    public int getAircraftId() {
        return aircraftId;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getTailsign() {
        return tailsign;
    }

    public void setTailsign(String tailsign) {
        this.tailsign = tailsign;
    }

    public Aircraft(String callsign, String tailsign) {
        this.aircraftId = NEXT_ID++;
        this.callsign = callsign;
        this.tailsign = tailsign;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
