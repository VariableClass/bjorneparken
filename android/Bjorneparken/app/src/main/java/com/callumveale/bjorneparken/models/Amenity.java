package com.callumveale.bjorneparken.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.callumveale.bjorneparken.R;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by callum on 27/02/2017.
 */

public class Amenity extends Area implements IModel, Parcelable{

    //region Constants

    public static final Creator CREATOR = new Creator() {

        public Amenity createFromParcel(Parcel in) {
            return new Amenity(in);
        }

        public Amenity[] newArray(int size) {
            return new Amenity[size];
        }
    };

    //endregion Constants

    //region Properties

    private String description;
    private String amenityType;

    //endregion Properties

    //region Constructors

    public Amenity(long id, String label, String visitorDestination, ArrayList<String> coordinates, String imageUrl, String description, String amenityType){
        super(id, label, visitorDestination, coordinates, imageUrl);
        this.description = description;
        this.amenityType = amenityType;
    }

    public Amenity(long id, String label, String visitorDestination, ArrayList<String> coordinates, String description, String amenityType){
        super(id, label, visitorDestination, coordinates);
        this.description = description;
        this.amenityType = amenityType;
    }

    // Parcelling part
    public Amenity(Parcel in){

        setId(in.readLong());

        String[] data = new String[4];
        in.readStringArray(data);
        setLabel(data[0]);
        setVisitorDestination(data[1]);
        description = data[2];
        amenityType = data[3];

        ArrayList<String> coordinates = new ArrayList<>();
        in.readStringList(coordinates);
        setCoordinates(coordinates);
    }

    //endregion Constructors

    //region Methods

    public String getAmenityType() {

        return amenityType;
    }

    public String getAmenityTypeTranslation(Context context) {

        int resourceId = context.getResources().getIdentifier(amenityType, "string", context.getPackageName());

        return context.getString(resourceId);
    }

    public String getDescription() {
        return description;
    }

    //region IModel Overridden Methods

    @Override
    public String getHeader() {
        return this.getLabel();
    }

    @Override
    public String getSubheader() {
        return getAmenityType();
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
        return super.getImageUrl();
    }

    @Override
    public Bitmap getImage() {
        return super.getImage();
    }

    @Override
    public void setImage(Bitmap bitmap) {
        super.setImage(bitmap);
    }

    //endregion IModel Overridden Methods

    //region Parcelable Overridden Methods

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(getId());

        dest.writeStringArray(new String[] {
                getLabel(),
                getVisitorDestination(),
                description,
                amenityType});

        dest.writeStringList(getCoordinates());
    }

    //endregion Parcelable Overridden Methods

    //endregion Methods

    //region Comparators

    public static class AmenityComparator implements Comparator<Amenity> {

        private Context mContext;

        public AmenityComparator(Context context){

            mContext = context;
        }

        @Override
        public int compare(Amenity amenity1, Amenity amenity2) {

            return amenity1.getAmenityTypeTranslation(mContext).compareTo(amenity2.getAmenityTypeTranslation(mContext));
        }
    }

    //endregion Comparators
}