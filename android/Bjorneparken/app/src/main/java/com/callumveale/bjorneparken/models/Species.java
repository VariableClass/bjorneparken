package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

import none.bjorneparkappen_api.model.MainSpeciesListResponse;
import none.bjorneparkappen_api.model.MainSpeciesRequest;
import none.bjorneparkappen_api.model.MainSpeciesResponse;

/**
 * Created by callum on 27/02/2017.
 */

public class Species implements IModel, Parcelable{
    private long id;
    private String commonName;
    private String latin;
    private String description;

    // Constructor
    public Species(long id, String commonName, String latin, String description){
        this.id = id;
        this.commonName = commonName;
        this.latin = latin;
        this.description = description;
    }

    // Getter and setter methods
    public long getId(){ return id;}

    @Override
    public String getHeader() {
        return this.commonName;
    }

    @Override
    public String getSubheader() {
        return this.latin;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public String getSubcaption() {
        return null;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getLatin() {
        return latin;
    }

    public void setLatin(String latin) {
        this.latin = latin;
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
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Species createFromParcel(Parcel in) {
            return new Species(in);
        }

        public Species[] newArray(int size) {
            return new Species[size];
        }
    };
}