package com.callumveale.bjorneparken.requests;

import android.os.Parcelable;

import com.callumveale.bjorneparken.models.Amenity;
import com.callumveale.bjorneparken.models.Species;

import java.util.ArrayList;
import java.util.List;

import none.bjorneparkappen_api.model.MainAmenityResponse;
import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;
import none.bjorneparkappen_api.model.MainSpeciesResponse;

/**
 * Created by callum on 27/02/2017.
 */

public class RequestsModule {


    static final String API_KEY = "AIzaSyCuD2dk_XFcn512V5JxAZbFlAK9dgNlQ9c";
    static final String ROOT_URL = "https://api-dot-bjorneparkappen.appspot.com/_ah/api/";

    public static String LANGUAGE;

    static ArrayList<Parcelable> convertListResponseToList(Object response){

        if (response == null){

            throw new NullPointerException("Unable to process null response");
        }

        ArrayList<Parcelable> returnList = new ArrayList<>();

        if (response.getClass() == MainSpeciesListResponse.class){

            if (((MainSpeciesListResponse) response).getSpecies() != null) {

                for (MainSpeciesResponse speciesResponse : ((MainSpeciesListResponse) response).getSpecies()){

                    Species species = new Species(speciesResponse.getId(), speciesResponse.getCommonName(), speciesResponse.getLatin(), speciesResponse.getDescription());
                    returnList.add(species);
                }
            }

            return returnList;
        }

        if (response.getClass() == MainAreaListResponse.class){

            if (((MainAreaListResponse) response).getAmenities() != null) {

                for (MainAmenityResponse amenityResponse : ((MainAreaListResponse) response).getAmenities()){


                    List<String> coordinatesList = amenityResponse.getCoordinates();
                    String[] coordinates = coordinatesList.toArray(new String[coordinatesList.size()]);

                    Amenity amenity = new Amenity(amenityResponse.getId(), amenityResponse.getLabel(), amenityResponse.getVisitorDestination(), coordinates, amenityResponse.getDescription(), amenityResponse.getAmenityType());
                    returnList.add(amenity);
                }
            }

            return returnList;
        }

        return returnList;
    }
}
