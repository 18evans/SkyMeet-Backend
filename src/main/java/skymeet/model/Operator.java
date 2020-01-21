package skymeet.model;

import com.google.gson.Gson;

public class Operator extends Airline {
    public Operator(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
