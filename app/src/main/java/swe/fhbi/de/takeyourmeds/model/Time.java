package swe.fhbi.de.takeyourmeds.model;

import java.io.Serializable;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class Time implements Serializable {
    private int id;
    private String name;
    private String time;
    private int drugId;
    private int status;

    public Time(String name, String time, int status) {
        this.name = name;
        this.time = time;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status > 0;
    }
}
