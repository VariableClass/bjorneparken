package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Species implements Parcelable{
    private String commonName;
    private String latin;
    private String description;

    // Constructor
    public Species(String commonName, String latin, String description){
        this.commonName = commonName;
        this.latin = latin;
        this.description = description;
    }

    // Getter and setter methods
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getLatin() {
        return latin;
    }

    public void setLatin(String latin) {
        this.latin = latin;
    }

    // Parcelling part
    public Species(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.commonName = data[0];
        this.latin = data[1];
        this.description = data[2];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.commonName,
                this.latin,
                this.description});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Species createFromParcel(Parcel in) {
            return new Species(in);
        }

        public Species[] newArray(int size) {
            return new Species[size];
        }
    };
}