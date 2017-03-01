package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Enclosure extends Area implements IModel, Parcelable {
    private Animal[] animals;

    // Constructor
    public Enclosure(long id, String label, String visitorDestination, String[] coordinates, Animal[] animals){
        super(id, label, visitorDestination, coordinates);
        this.animals = animals;
    }

    // Getter and setter methods
    public Animal[] getAnimals() {
        return animals;
    }

    // Parcelling part
    public Enclosure(Parcel in){

        super.setId(in.readLong());

        String[] data = new String[3];
        in.readStringArray(data);
        super.setLabel(data[0]);
        super.setVisitorDestination(data[1]);
        super.setCoordinates(data[2].split(","));

        this.animals = (Animal[]) in.readParcelableArray(Animal.class.getClassLoader());
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(super.getId());

        dest.writeStringArray(new String[]{super.getLabel(),
                super.getVisitorDestination(),
                super.getCoordinates().toString()});

        dest.writeParcelableArray(this.animals, 0);
    }

    public static final Creator CREATOR = new Creator() {
        public Enclosure createFromParcel(Parcel in) {
            return new Enclosure(in);
        }

        public Enclosure[] newArray(int size) {
            return new Enclosure[size];
        }
    };

    @Override
    public String getHeader() {
        return this.getLabel();
    }

    @Override
    public String getSubheader() {
        return null;
    }

    @Override
    public String getDescription() {
        return this.getDescription();
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public String getSubcaption() {
        return null;
    }
}