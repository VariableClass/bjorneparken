package com.callumveale.bjorneparken.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by callum on 27/02/2017.
 */

public class Enclosure extends Area implements IModel, Parcelable {

    //region Constants

    public static final Creator CREATOR = new Creator() {

        public Enclosure createFromParcel(Parcel in) {
            return new Enclosure(in);
        }

        public Enclosure[] newArray(int size) {
            return new Enclosure[size];
        }
    };

    //endregion Constants

    //region Properties

    private ArrayList<Animal> animals;

    //endregion Properties

    //region Constructors

    public Enclosure(long id, String label, String visitorDestination, ArrayList<String> coordinates, String imageUrl, ArrayList<Animal> animals){
        super(id, label, visitorDestination, coordinates, imageUrl);
        this.animals = animals;
    }

    public Enclosure(long id, String label, String visitorDestination, ArrayList<String> coordinates, ArrayList<Animal> animals){
        super(id, label, visitorDestination, coordinates);
        this.animals = animals;
    }

    // Parcelling part
    public Enclosure(Parcel in){

        setId(in.readLong());

        String[] data = new String[2];
        in.readStringArray(data);
        setLabel(data[0]);
        setVisitorDestination(data[1]);

        ArrayList<String> coordinates = new ArrayList<>();
        in.readStringList(coordinates);
        setCoordinates(coordinates);

        animals = new ArrayList<>();
        in.readTypedList(animals, Animal.CREATOR);
    }

    //endregion Constructors

    //region Methods

    public ArrayList<Species> getSpecies(){

        ArrayList<Species> species = new ArrayList<>();

        for (Animal animal : animals){

            boolean speciesAlreadyOnList = false;

            for (Species speciesInst : species){

                if (animal.getSpecies().getId() == speciesInst.getId()){

                    speciesAlreadyOnList = true;
                }
            }

            if (!speciesAlreadyOnList){

                species.add(animal.getSpecies());
            }
        }

        return species;
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    //region IModel Overridden Methods

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
        return null;
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

        dest.writeStringArray(new String[]{
                getLabel(),
                getVisitorDestination()});

        dest.writeStringList(getCoordinates());

        dest.writeTypedList(animals);
    }

    //endregion Parcelable Overridden Methods

    //endregion Methods
}