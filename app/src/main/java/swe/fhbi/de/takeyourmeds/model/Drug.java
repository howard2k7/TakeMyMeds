package swe.fhbi.de.takeyourmeds.model;

import java.io.Serializable;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class Drug implements Serializable {
    private int id;
    private String name;
    private int quantity;
    private String note;
    private String unit;

    public Drug(String name, int quantity, String note, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.note = note;
        this.unit = unit;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getNote() {
        return note;
    }

    public String getUnit() {
        return unit;
    }

}
