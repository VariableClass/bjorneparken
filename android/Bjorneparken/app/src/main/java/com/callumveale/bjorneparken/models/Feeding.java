package com.callumveale.bjorneparken.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callum on 27/02/2017.
 */

public class Feeding extends Event implements IModel, Parcelable{

    //region Constants

    public static final Creator CREATOR = new Creator() {

        public Feeding createFromParcel(Parcel in) {
            return new Feeding(in);
        }

        public Feeding[] newArray(int size) {
            return new Feeding[size];
        }
    };

    //endregion Constants

    //region Properties

    private Keeper keeper;

    //endregion Properties

    //region Constructors

    public Feeding(long id, String label, String description, Enclosure location, String startTime, String endTime, boolean isActive, Keeper keeper, String imageUrl){
        super(id, label, description, location, startTime, endTime, isActive, imageUrl);
        this.keeper = keeper;
    }

    public Feeding(long id, String label, String description, Enclosure location, String startTime, String endTime, boolean isActive, Keeper keeper){
        super(id, label, description, location, startTime, endTime, isActive);
        this.keeper = keeper;
    }



    // Parcelling part
    public Feeding(Parcel in){

        setId(in.readLong());

        String[] data = new String[5];
        in.readStringArray(data);
        setLabel(data[0]);
        setDescription(data[1]);
        setStartTime(data[2]);
        setEndTime(data[3]);
        setImageUrl(data[4]);

        setLocation((Area)in.readParcelable(Enclosure.class.getClassLoader()));

        boolean[] boolArray = new boolean[1];
        in.readBooleanArray(boolArray);
        setActive(boolArray[0]);

        keeper = in.readParcelable(Keeper.class.getClassLoader());
    }

    //endregion Constructors

    //region Methods

    public Keeper getKeeper(){

        return this.keeper;
    }

    //region IModel Overridden Methods

    @Override
    public String getSubcaption(){
        return this.keeper.getName();
    }

    //endregion IModel Overridden Methods

    //region Parcelable Overridden Methods

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getId());
        dest.writeStringArray(new String[]{this.getLabel(), this.getDescription(), this.getStartTime(), this.getEndTime(), this.getImageUrl()});
        dest.writeParcelable((Enclosure)this.getLocation(), 0);
        dest.writeBooleanArray(new boolean[]{this.isActive()});
        dest.writeParcelable(this.keeper, 0);
    }

    //endregion Parcelable Overridden Methods

    //endregion Methods
}