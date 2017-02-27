package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Amenity implements Parcelable{
    private long id;
    private String label;
    private String visitorDestination;
    private String[] coordinates;
    private String description;
    private String amenityType;

    // Constructor
    public Amenity(long id, String label, String visitorDestination, String[] coordinates, String description, String amenityType){
        this.id = id;
        this.label = label;
        this.visitorDestination = visitorDestination;
        this.coordinates = coordinates;
        this.description = description;
        this.amenityType = amenityType;
    }

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

    public String getDescription() {
        return description;
    }

    public String getAmenityType() {
        return amenityType;
    }

    // Parcelling part
    public Amenity(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        this.id = Long.parseLong(data[0]);
        this.label = data[1];
        this.visitorDestination = data[2];
        this.coordinates = data[3].split(",");
        this.description = data[4];
        this.amenityType = data[5];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                String.valueOf(this.id),
                this.label,
                this.visitorDestination,
                this.coordinates.toString(),
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