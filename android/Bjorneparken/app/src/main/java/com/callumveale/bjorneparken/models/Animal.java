package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Animal implements IModel, Parcelable{

    //region Constants

    public static final Creator CREATOR = new Creator() {

        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

    //endregion Constants

    //region Properties

    private long id;
    private String name;
    private Species species;
    private String description;
    private boolean isAvailable;

    //endregion Properties

    //region Constructors

    public Animal(long id, String name, Species species, String description, boolean isAvailable){
        this.id = id;
        this.name = name;
        this.species = species;
        this.description = description;
        this.isAvailable = isAvailable;
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

    //endregion Constructors

    //region Methods

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

    //region IModel Overridden Methods

    @Override
    public String getHeader() {
        return this.getName();
    }

    @Override
    public String getSubheader() {
        return getSpecies().getCommonName();
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public String getSubcaption() {
        return null;
    }

    //endregion IModel Overridden Methods

    //region Parcelable Overridden Methods

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);

        dest.writeStringArray(new String[]{
                this.name,
                this.description});

        dest.writeParcelable(this.species, 0);

        dest.writeBooleanArray(new boolean[]{this.isAvailable});
    }

    //endregion Parcelable Overriden Methods

    //endregion Methods
}