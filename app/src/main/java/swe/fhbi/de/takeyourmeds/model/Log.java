package swe.fhbi.de.takeyourmeds.model;

import java.io.Serializable;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class Log implements Serializable {
    private int id;
    private String date;
    private String time;
    private int drugId;
    private int status;
    private Drug drug;

    public Log(String date, String time, int drugId, int status) {
        this.date = date;
        this.time = time;
        this.drugId = drugId;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getDrugId() {
        return drugId;
    }

    public int getStatus() {
        return status;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Drug getDrug() {
        return drug;
    }
}
