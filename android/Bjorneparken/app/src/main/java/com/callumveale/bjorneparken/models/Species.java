package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Species implements IModel, Parcelable{

    //region Constants

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Species createFromParcel(Parcel in) {
            return new Species(in);
        }

        public Species[] newArray(int size) {
            return new Species[size];
        }
    };

    //endregion Constants

    //region Properties

    private long id;
    private String commonName;
    private String latin;
    private String description;

    //endregion Properties

    //region Constructors

    public Species(long id, String commonName, String latin, String description){
        this.id = id;
        this.commonName = commonName;
        this.latin = latin;
        this.description = description;
    }

    // Parcelling part
    public Species(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.id = Long.parseLong(data[0]);
        this.commonName = data[1];
        this.latin = data[2];
        this.description = data[3];
    }

    //endregion Constructors

    //region Methods

    public long getId(){ return id;}

    public String getCommonName() {
        return commonName;
    }

    public String getLatin() {
        return latin;
    }

    public String getDescription() {
        return description;
    }

    //region IModel Overridden Methods

    @Override
    public String getHeader() {
        return this.commonName;
    }

    @Override
    public String getSubheader() {
        return this.latin;
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
        dest.writeStringArray(new String[] {
                String.valueOf(this.id),
                this.commonName,
                this.latin,
                this.description});
    }

    //endregion Parcelable Overridden Methods

    //endregion Methods
}