package swe.fhbi.de.takeyourmeds.model;

import java.io.Serializable;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */
public class Plan extends Drug implements Serializable {
    private int status;
    private String timeTitle;
    private String time;

    public Plan(String name, int quantity, String note, String unit, int status, String time, String timeTitle) {
        super(name, quantity, note, unit);
        this.status = status;
        this.timeTitle = timeTitle;
        this.time = time;
    }

    public Boolean getStatus() {
        return status > 0;
    }

    public String getTimeTitle() {
        return timeTitle;
    }

    public String getTime() {
        return time;
    }
}
