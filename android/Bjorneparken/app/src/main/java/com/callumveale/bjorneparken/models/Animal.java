package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

import none.bjorneparkappen_api.BjorneparkappenApi;

/**
 * Created by callum on 27/02/2017.
 */

public class Animal implements Parcelable{
    private long id;
    private String name;
    private Species species;
    private String description;
    private boolean isAvailable;

    // Constructor
    public Animal(long id, String name, Species species, String description, boolean isAvailable){
        this.id = id;
        this.name = name;
        this.species = species;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    // Getter and setter methods
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Parcelling part
    public Animal(Parcel in){

        this.id = in.readLong();

        String[] data = new String[2];
        in.readStringArray(data);
        this.name = data[0];
        this.description = data[1];

        this.species = in.readParcelable(Species.class.getClassLoader());

        boolean[] boolArray = new boolean[1];
        in.readBooleanArray(boolArray);
        this.isAvailable = boolArray[0];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);

        dest.writeStringArray(new String[] {
                this.name,
                this.description});

        dest.writeParcelable(this.species, 0);

        dest.writeBooleanArray(new boolean[]{this.isAvailable});
    }
    public static final Creator CREATOR = new Creator() {
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };
}