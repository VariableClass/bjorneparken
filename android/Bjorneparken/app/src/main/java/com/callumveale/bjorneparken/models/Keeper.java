package com.callumveale.bjorneparken.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Keeper implements Parcelable{
    private long id;
    private String name;
    private String bio;

    // Constructor
    public Keeper(long id, String name, String bio){
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

    // Getter and setter methods
    public long getId(){ return id;}

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    // Parcelling part
    public Keeper(Parcel in){

        this.id = in.readLong();

        String[] data = new String[2];
        in.readStringArray(data);
        this.name = data[0];
        this.bio = data[1];
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
                this.bio});
    }
    public static final Creator CREATOR = new Creator() {
        public Keeper createFromParcel(Parcel in) {
            return new Keeper(in);
        }

        public Keeper[] newArray(int size) {
            return new Keeper[size];
        }
    };
}