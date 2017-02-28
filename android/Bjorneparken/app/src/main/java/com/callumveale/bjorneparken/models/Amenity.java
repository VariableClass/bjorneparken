package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Amenity extends Area implements Parcelable{
    private String description;
    private String amenityType;

    // Constructor
    public Amenity(long id, String label, String visitorDestination, String[] coordinates, String description, String amenityType){
        super(id, label, visitorDestination, coordinates);
        this.description = description;
        this.amenityType = amenityType;
    }

    // Getter and setter methods
    public String getDescription() {
        return description;
    }

    public String getAmenityType() {
        return amenityType;
    }

    // Parcelling part
    public Amenity(Parcel in){

        super.setId(in.readLong());

        String[] data = new String[5];
        in.readStringArray(data);
        super.setLabel(data[0]);
        super.setVisitorDestination(data[1]);
        super.setCoordinates(data[2].split(","));

        this.description = data[3];
        this.amenityType = data[4];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(super.getId());

        dest.writeStringArray(new String[] {
                super.getLabel(),
                super.getVisitorDestination(),
                super.getCoordinates().toString(),
                this.description,
                this.amenityType});
    }
    public static final Creator CREATOR = new Creator() {
        public Amenity createFromParcel(Parcel in) {
            return new Amenity(in);
        }

        public Amenity[] newArray(int size) {
            return new Amenity[size];
        }
    };
}