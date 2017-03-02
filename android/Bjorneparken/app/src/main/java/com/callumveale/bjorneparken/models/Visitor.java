package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by callum on 27/02/2017.
 */

public class Visitor {

    private long id;
    private DateTime visitStart;
    private DateTime visitEnd;
    private List<Event> itinerary;
    private List<Species> starredSpecies;

    // Constructor
    public Visitor(long id, DateTime visitStart, DateTime visitEnd, List<Event> itinerary, List<Species> starredSpecies){
        this.id = id;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
        this.itinerary = itinerary;
        this.starredSpecies = starredSpecies;
    }

    public Visitor(long id, DateTime visitStart, DateTime visitEnd){
        this.id = id;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
        this.itinerary = new ArrayList<>();
        this.starredSpecies = new ArrayList<>();
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

    public ArrayList<Parcelable> getParcelableItinerary(){

        ArrayList<Parcelable> returnList = new ArrayList<>();

        for (Event event : itinerary){

            returnList.add(event);
        }

        return returnList;
    }

    public List<Event> getItinerary(){ return itinerary; }

    public List<Species> getStarredSpecies() {
        return starredSpecies;
    }

    public ArrayList<Parcelable> getParcelableStarredSpecies(){

        ArrayList<Parcelable> returnList = new ArrayList<>();

        for (Species species : starredSpecies){

            returnList.add(species);
        }

        return returnList;
    }

    public void setVisitStart(DateTime visitStart) {
        this.visitStart = visitStart;
    }

    public void setVisitEnd(DateTime visitEnd) {
        this.visitEnd = visitEnd;
    }

    public void setItinerary(List<Event> itinerary){ this.itinerary = itinerary; }

    public void setStarredSpecies(List<Species> starredSpecies) {
        this.starredSpecies = starredSpecies;
    }
}