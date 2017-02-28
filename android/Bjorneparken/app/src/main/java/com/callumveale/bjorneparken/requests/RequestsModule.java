package com.callumveale.bjorneparken.requests;

import android.os.Parcelable;

import com.callumveale.bjorneparken.models.Amenity;
import com.callumveale.bjorneparken.models.Animal;
import com.callumveale.bjorneparken.models.Enclosure;
import com.callumveale.bjorneparken.models.Event;
import com.callumveale.bjorneparken.models.Feeding;
import com.callumveale.bjorneparken.models.Keeper;
import com.callumveale.bjorneparken.models.Species;

import java.util.ArrayList;
import java.util.List;

import none.bjorneparkappen_api.model.MainAmenityResponse;
import none.bjorneparkappen_api.model.MainAnimalResponse;
import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainEnclosureResponse;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainEventResponse;
import none.bjorneparkappen_api.model.MainFeedingResponse;
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

        } else if (response.getClass() == MainAreaListResponse.class){

            if (((MainAreaListResponse) response).getAmenities() != null) {

                for (MainAmenityResponse amenityResponse : ((MainAreaListResponse) response).getAmenities()){

                    List<String> coordinatesList = amenityResponse.getCoordinates();
                    String[] coordinates = coordinatesList.toArray(new String[coordinatesList.size()]);

                    Amenity amenity = new Amenity(amenityResponse.getId(), amenityResponse.getLabel(), amenityResponse.getVisitorDestination(), coordinates, amenityResponse.getDescription(), amenityResponse.getAmenityType());
                    returnList.add(amenity);
                }
            }

            return returnList;

        } else if (response.getClass() == MainEventListResponse.class){

            if (((MainEventListResponse) response).getEvents() != null) {

                for (MainEventResponse eventResponse : ((MainEventListResponse) response).getEvents()){

                    MainAmenityResponse locationResponse = eventResponse.getLocation();

                    List<String> coordinatesList = locationResponse.getCoordinates();
                    String[] coordinates = coordinatesList.toArray(new String[coordinatesList.size()]);

                    Amenity location = new Amenity(locationResponse.getId(), locationResponse.getLabel(), locationResponse.getVisitorDestination(), coordinates, locationResponse.getDescription(), locationResponse.getAmenityType());

                    Event event = new Event(eventResponse.getId(), eventResponse.getLabel(), eventResponse.getDescription(), location, eventResponse.getStartTime(), eventResponse.getEndTime(), eventResponse.getIsActive());
                    returnList.add(event);
                }
            }

            if (((MainEventListResponse) response).getFeedings() != null) {

                for (MainFeedingResponse feedingResponse : ((MainEventListResponse) response).getFeedings()){

                    MainEnclosureResponse locationResponse = feedingResponse.getLocation();

                    // Get co-ordinates
                    List<String> coordinatesList = locationResponse.getCoordinates();
                    String[] coordinates = coordinatesList.toArray(new String[coordinatesList.size()]);

                    // Create animals
                    List<MainAnimalResponse> animalsList = locationResponse.getAnimals();
                    Animal[] animals = new Animal[animalsList.size()];
                    for (int i = 0; i < animalsList.size() - 1; i++){

                        MainSpeciesResponse speciesResponse = animalsList.get(i).getSpecies();
                        Species species = new Species(speciesResponse.getId(), speciesResponse.getCommonName(), speciesResponse.getLatin(), speciesResponse.getDescription());

                        animals[i] = new Animal(animalsList.get(i).getId(), animalsList.get(i).getName(), species, animalsList.get(i).getDescription(), animalsList.get(i).getIsAvailable());
                    }

                    // Create enclosure, passing in animals
                    Enclosure location = new Enclosure(locationResponse.getId(), locationResponse.getLabel(), locationResponse.getVisitorDestination(), coordinates, animals);

                    // Create keeper
                    Keeper keeper = new Keeper(feedingResponse.getKeeper().getId(), feedingResponse.getKeeper().getName(), feedingResponse.getKeeper().getBio());

                    // Create feeding, passing in co-ordinates, enclosure and keeper
                    Feeding feeding = new Feeding(feedingResponse.getId(), feedingResponse.getLabel(), feedingResponse.getDescription(), location, feedingResponse.getStartTime(), feedingResponse.getEndTime(), feedingResponse.getIsActive(), keeper);
                    returnList.add(feeding);
                }
            }

            return returnList;
        }

        return returnList;
    }
}
