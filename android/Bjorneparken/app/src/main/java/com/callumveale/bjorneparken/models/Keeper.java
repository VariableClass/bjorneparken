package com.callumveale.bjorneparken.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Keeper implements IModel, Parcelable{

    //region Constants

    public static final Creator CREATOR = new Creator() {

        public Keeper createFromParcel(Parcel in) {
            return new Keeper(in);
        }

        public Keeper[] newArray(int size) {
            return new Keeper[size];
        }
    };

    //endregion Constants

    //region Properties

    private long id;
    private String name;
    private String bio;

    //endregion Properties

    //region Constructors

    public Keeper(long id, String name, String bio){
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

    // Parcelling part
    public Keeper(Parcel in){

        this.id = in.readLong();

        String[] data = new String[2];
        in.readStringArray(data);
        this.name = data[0];
        this.bio = data[1];
    }

    //endregion Constructors

    //region Methods

    public long getId(){ return id;}

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    //region IModel Overridden Methods

    @Override
    public String getHeader() {
        return name;
    }

    @Override
    public String getSubheader() {
        return null;
    }

    @Override
    public String getDescription() {
        return bio;
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public String getSubcaption(Context context) {
        return null;
    }

    @Override
    public String getListTitle(Context context) {
        return null;
    }

    @Override
    public String[] getList() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public Bitmap getImage() {
        return null;
    }

    @Override
    public void setImage(Bitmap bitmap) {
        return;
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

        dest.writeStringArray(new String[] {
                this.name,
                this.bio});
    }

    //endregion Parcelable Overridden Methods

    //endregion Methods
}