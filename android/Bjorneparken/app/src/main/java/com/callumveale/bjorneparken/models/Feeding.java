package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Feeding extends Event implements IModel, Parcelable{

    private Keeper keeper;

    // Constructor
    public Feeding(long id, String label, String description, Enclosure location, String startTime, String endTime, boolean isActive, Keeper keeper){
        super(id, label, description, location, startTime, endTime, isActive);
        this.keeper = keeper;
    }

    // Getter and setter methods
    public Keeper getKeeper(){

        return this.keeper;
    }

    // Parcelling part
    public Feeding(Parcel in){

        super.setId(in.readLong());

        String[] data = new String[4];
        in.readStringArray(data);
        super.setLabel(data[0]);
        super.setDescription(data[1]);
        super.setStartTime(data[2]);
        super.setEndTime(data[3]);

        super.setLocation((Area)in.readParcelable(Enclosure.class.getClassLoader()));

        boolean[] boolArray = new boolean[1];
        in.readBooleanArray(boolArray);
        super.setActive(boolArray[0]);

        this.keeper = in.readParcelable(Keeper.class.getClassLoader());
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getId());
        dest.writeStringArray(new String[]{this.getLabel(), this.getDescription(), this.getStartTime(), this.getEndTime()});
        dest.writeParcelable((Enclosure)this.getLocation(), 0);
        dest.writeBooleanArray(new boolean[]{this.isActive()});
    }
    public static final Creator CREATOR = new Creator() {
        public Feeding createFromParcel(Parcel in) {
            return new Feeding(in);
        }

        public Feeding[] newArray(int size) {
            return new Feeding[size];
        }
    };

    @Override
    public String getSubcaption(){
        return this.keeper.getName();
    }
}