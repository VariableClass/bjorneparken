package com.callumveale.bjorneparken.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by callum on 28/02/2017.
 */

public class Area {

    //region Properties

    private long id;
    private String label;
    private String visitorDestination;
    private ArrayList<String> coordinates;
    private String imageUrl;
    private Bitmap image;

    //endregion Properties

    //region Constructors

    public Area(long id, String label, String visitorDestination, ArrayList<String> coordinates, String imageUrl){
        this.id = id;
        this.label = label;
        this.visitorDestination = visitorDestination;
        this.coordinates = coordinates;
        this.imageUrl = imageUrl;
    }

    public Area(long id, String label, String visitorDestination, ArrayList<String> coordinates){
        this.id = id;
        this.label = label;
        this.visitorDestination = visitorDestination;
        this.coordinates = coordinates;
    }

    public Area(){}

    //endregion Constructors

    //region Methods

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getVisitorDestination() {
        return visitorDestination;
    }

    public ArrayList<String> getCoordinates() {
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

    public void setCoordinates(ArrayList<String> coordinates) {
        this.coordinates = coordinates;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap bitmap) {
        this.image = bitmap;
    }

    //endregion Methods
}
