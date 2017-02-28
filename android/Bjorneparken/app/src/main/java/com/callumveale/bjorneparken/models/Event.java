package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Event implements Parcelable{

    private long id;
    private String label;
    private String description;
    private Area location;
    private String startTime;
    private String endTime;
    private boolean isActive;

    // Constructor
    public Event(long id, String label, String description, Area location, String startTime, String endTime, boolean isActive){
        this.id = id;
        this.label = label;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

    public Event(){}

    // Getter and setter methods
    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Area getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Area location) {
        this.location = location;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Parcelling part
    public Event(Parcel in){

        this.id = in.readLong();

        String[] data = new String[4];
        in.readStringArray(data);
        this.label = data[0];
        this.description = data[1];
        this.startTime = data[2];
        this.endTime = data[3];

        this.location = in.readParcelable(Amenity.class.getClassLoader());

        boolean[] boolArray = new boolean[1];
        in.readBooleanArray(boolArray);
        this.isActive = boolArray[0];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeStringArray(new String[]{this.label, this.description, this.startTime, this.endTime});

        if (this.location.getClass() == Amenity.class) {
            dest.writeParcelable((Amenity) this.location, 0);

        } else if (this.location.getClass() == Enclosure.class){

            dest.writeParcelable((Enclosure) this.location, 0);
        }


        dest.writeBooleanArray(new boolean[]{this.isActive});
    }
    public static final Creator CREATOR = new Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}