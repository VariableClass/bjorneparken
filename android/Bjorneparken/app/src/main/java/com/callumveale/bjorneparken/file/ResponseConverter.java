package com.callumveale.bjorneparken.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.callumveale.bjorneparken.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import none.bjorneparkappen_api.model.*;

/**
 * Created by callum on 03/03/2017.
 */

public class ResponseConverter {

    //region Methods

    //region Species

    private static Species convertSpeciesResponse(MainSpeciesResponse response){

        // If response is null, return null
        if (response == null){

            return null;
        }

        String base64mime = response.getImage();

        byte[] imageBytes = null;

        if (base64mime != null){

            String base64 = base64mime.split(",")[1];

            // Decode base64 image
            imageBytes = Base64.decode(base64, Base64.DEFAULT);
        }

        // Return a new species object created from the species response
        return new Species(response.getId(), response.getCommonName(), response.getLatin(), response.getDescription(), imageBytes);
    }

    public static ArrayList<Species> convertSpeciesListResponse(MainSpeciesListResponse response){

        // Create new list to return
        ArrayList<Species> returnList = new ArrayList<>();

        // If response is null or does not have species
        if (response == null || response.getSpecies() == null) {

            // Return the empty list
            return returnList;
        }

        // Else, for each species response within the list
        for (MainSpeciesResponse speciesResponse : response.getSpecies()){

            // Create a new species
            Species species = convertSpeciesResponse(speciesResponse);

            // Add it to the return list
            returnList.add(species);
        }

        // Return populated list
        return returnList;
    }

    //endregion Species

    //region Animals

    private static Animal convertAnimalResponse(MainAnimalResponse response){

        // If response is null, return null
        if (response == null) {

            return null;
        }

        // Retrieve species
        Species species = convertSpeciesResponse(response.getSpecies());

        // Return a new animal object created from the animal response
        return new Animal(response.getId(), response.getName(), species, response.getDescription(), response.getIsAvailable());
    }

    public static ArrayList<Animal> convertAnimalListResponse(MainAnimalListResponse response){

        // Create new list to return
        ArrayList<Animal> returnList = new ArrayList<>();

        // If response is null or does not have animals
        if (response == null || response.getAnimals() == null) {

            // Return the empty list
            return returnList;
        }

        // Else, for each animal response within the list
        for (MainAnimalResponse animalResponse : response.getAnimals()){

            // Create a new animal
            Animal animal = convertAnimalResponse(animalResponse);

            // Add it to the return list
            returnList.add(animal);
        }

        // Return populated list
        return returnList;
    }

    private static ArrayList<Animal> convertAnimalListResponse(List<MainAnimalResponse> response){

        // Create new list to return
        ArrayList<Animal> returnList = new ArrayList<>();

        // If response is null
        if (response == null) {

            // Return the empty list
            return returnList;
        }

        // Else, for each animal response within the list
        for (MainAnimalResponse animalResponse : response){

            // Create a new animal
            Animal animal = convertAnimalResponse(animalResponse);

            // Add it to the return list
            returnList.add(animal);
        }

        // Return populated list
        return returnList;
    }

    //endregion Animals

    //region Areas

    //region Amenties

    private static Amenity convertAmenityResponse(MainAmenityResponse response){

        // If response is null, return null
        if (response == null){

            return null;
        }

        // Retrieve coordinates
        ArrayList<String> coordinates = new ArrayList<>(response.getCoordinates());

        // Return a new amenity object created from the amenity response
        return new Amenity(response.getId(), response.getLabel(), response.getVisitorDestination(), coordinates, response.getDescription(), response.getAmenityType());
    }

    public static ArrayList<Amenity> convertAmenityListResponse(MainAreaListResponse response){

        // Create new list to return
        ArrayList<Amenity> returnList = new ArrayList<>();

        // If response is null or does not have amenities
        if (response == null || response.getAmenities() == null) {

            // Return the empty list
            return returnList;
        }

        // Else, for each amenity response within the list
        for (MainAmenityResponse amenityResponse : response.getAmenities()){

            // Create a new amenity
            Amenity amenity = convertAmenityResponse(amenityResponse);

            // Add it to the return list
            returnList.add(amenity);
        }

        // Return populated list
        return returnList;
    }

    //endregion Amenities

    //region Enclosures

    private static Enclosure convertEnclosureResponse(MainEnclosureResponse response) {

        // If response is null, return null
        if (response == null) {

            return null;
        }

        // Retrieve coordinates
        ArrayList<String> coordinates = new ArrayList<>(response.getCoordinates());

        // Retrieve animals
        ArrayList<Animal> animals = convertAnimalListResponse(response.getAnimals());

        // Return a new enclosure object created from the enclosure response
        return new Enclosure(response.getId(), response.getLabel(), response.getVisitorDestination(), coordinates, animals);
    }

    private static ArrayList<Enclosure> convertEnclosureListResponse(MainAreaListResponse response){

        // Create new list to return
        ArrayList<Enclosure> returnList = new ArrayList<>();

        // If response is null or does not have enclosures
        if (response == null || response.getEnclosures() == null) {

            // Return the empty list
            return returnList;
        }

        // Else, for each enclosure response within the list
        for (MainEnclosureResponse enclosureResponse : response.getEnclosures()){

            // Create a new enclosure
            Enclosure enclosure = convertEnclosureResponse(enclosureResponse);

            // Add it to the return list
            returnList.add(enclosure);
        }

        // Return populated list
        return returnList;
    }

    //endregion Enclosures

    public static ArrayList<Area> convertAreaListResponse(MainAreaListResponse response){

        // Create new list to return
        ArrayList<Area> returnList = new ArrayList<>();

        // If response is null or does not have enclosures or amenities
        if (response == null || (response.getEnclosures() == null && response.getAmenities() == null)) {

            // Return the empty list
            return returnList;
        }

        // Convert all amenities
        ArrayList<Amenity> amenities = convertAmenityListResponse(response);

        // Convert all enclosures
        ArrayList<Enclosure> enclosures = convertEnclosureListResponse(response);

        // Add all to return list
        returnList.addAll(amenities);
        returnList.addAll(enclosures);

        return returnList;
    }

    //endregion Areas

    //region Keepers

    private static Keeper convertKeeperResponse(MainKeeperResponse response){

        // If response is null, return null
        if (response == null){

            return null;
        }

        // Return a new keeper object created from the keeper response
        return new Keeper(response.getId(), response.getName(), response.getBio());
    }

    //endregion Keepers

    //region Events

    private static Event convertEventResponse(MainEventResponse response){

        // If response is null, return null
        if (response == null) {

            return null;
        }

        // Retrieve location
        Amenity location = convertAmenityResponse(response.getLocation());

        // Return a new event object created from the event response
        return new Event(response.getId(), response.getLabel(), response.getDescription(), location, response.getStartTime(), response.getEndTime(), response.getIsActive());

    }

    private static ArrayList<Event> convertEventListEventsResponse(MainEventListResponse response){

        // Create new list to return
        ArrayList<Event> returnList = new ArrayList<>();

        // If response is null or does not have events
        if (response == null || response.getEvents() == null) {

            // Return the empty list
            return returnList;
        }

        // Else, for each event response within the list
        for (MainEventResponse eventResponse : response.getEvents()){

            // Create a new event
            Event event = convertEventResponse(eventResponse);

            // Add it to the return list
            returnList.add(event);
        }

        // Return populated list
        return returnList;
    }

    //region Feedings

    private static Feeding convertFeedingResponse(MainFeedingResponse response){

        // If response is null, return null
        if (response == null) {

            return null;
        }

        // Retrieve location
        Enclosure location = convertEnclosureResponse(response.getLocation());

        // Retrieve keeper
        Keeper keeper = convertKeeperResponse(response.getKeeper());

        // Return a new feeding object created from the feeding response
        return new Feeding(response.getId(), response.getLabel(), response.getDescription(), location, response.getStartTime(), response.getEndTime(), response.getIsActive(), keeper);
    }

    public static ArrayList<Feeding> convertFeedingListResponse(MainEventListResponse response){

        // Create new list to return
        ArrayList<Feeding> returnList = new ArrayList<>();

        // If response is null or does not have feedings
        if (response == null || response.getFeedings() == null) {

            // Return the empty list
            return returnList;
        }

        // Else, for each feeding response within the list
        for (MainFeedingResponse feedingResponse : response.getFeedings()){

            // Create a new feeding
            Feeding feeding = convertFeedingResponse(feedingResponse);

            // Add it to the return list
            returnList.add(feeding);
        }

        // Return populated list
        return returnList;
    }

    //endregion Feedings

    public static ArrayList<Event> convertEventListResponse(MainEventListResponse response){

        // Create new list to return
        ArrayList<Event> returnList = new ArrayList<>();

        // If response is null or does not have events or feedings
        if (response == null || (response.getEvents() == null && response.getFeedings() == null)) {

            // Return the empty list
            return returnList;
        }

        // Convert all events
        ArrayList<Event> events= convertEventListEventsResponse(response);

        // Convert all feedings
        ArrayList<Feeding> feedings = convertFeedingListResponse(response);

        // Add all to return list
        returnList.addAll(events);
        returnList.addAll(feedings);

        // Sort events by time
        Collections.sort(returnList, new Event.EventTimeComparator());
        return returnList;
    }

    //endregion Events

    //endregion Methods
}
