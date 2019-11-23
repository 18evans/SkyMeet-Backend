package skymeet.model;

import java.util.Date;

public class Airline {
    private String name;

    public Airline(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Date getDateFounded() {
        return dateFounded;
    }

    private void setDateFounded(Date dateFounded) {
        this.dateFounded = dateFounded;
    }

    private Date dateFounded;
}
