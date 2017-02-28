package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.DateTime;

/**
 * Created by callum on 27/02/2017.
 */

public class Visitor {

    private long id;
    private DateTime visitStart;
    private DateTime visitEnd;
    private Species[] starredSpecies;

    // Constructor
    public Visitor(long id, DateTime visitStart, DateTime visitEnd, Species[] starredSpecies){
        this.id = id;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
        this.starredSpecies = starredSpecies;
    }

    // Getter and setter methods
    public long getId() {
        return id;
    }

    public DateTime getVisitStart() {
        return visitStart;
    }

    public DateTime getVisitEnd() {
        return visitEnd;
    }

    public Species[] getStarredSpecies() {
        return starredSpecies;
    }

    public void setVisitStart(DateTime visitStart) {
        this.visitStart = visitStart;
    }

    public void setVisitEnd(DateTime visitEnd) {
        this.visitEnd = visitEnd;
    }

    public void setStarredSpecies(Species[] starredSpecies) {
        this.starredSpecies = starredSpecies;
    }
}