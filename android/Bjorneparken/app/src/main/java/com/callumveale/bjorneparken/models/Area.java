package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 28/02/2017.
 */

public class Area {

    private long id;
    private String label;
    private String visitorDestination;
    private String[] coordinates;

    public Area(long id, String label, String visitorDestination, String[] coordinates){
        this.id = id;
        this.label = label;
        this.visitorDestination = visitorDestination;
        this.coordinates = coordinates;
    }

    public Area(){}

    // Getter and setter methods
    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getVisitorDestination() {
        return visitorDestination;
    }

    public String[] getCoordinates() {
        return coordinates;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setVisitorDestination(String visitorDestination) {
        this.visitorDestination = visitorDestination;
    }

    public void setCoordinates(String[] coordinates) {
        this.coordinates = coordinates;
    }
}
