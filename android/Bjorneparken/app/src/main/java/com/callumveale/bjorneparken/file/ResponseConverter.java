package com.callumveale.bjorneparken.file;

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

    //region To JSON

    //region Species

    private static MainSpeciesResponse convertLocalSpecies(Species species){

        // If species is null, return null
        if (species == null){

            return null;
        }

        MainSpeciesResponse response = new MainSpeciesResponse();
        response.setId(species.getId());
        response.setCommonName(species.getCommonName());
        response.setLatin(species.getLatin());
        response.setDescription(species.getDescription());
        response.setImage(species.getImageUrl());

        // Return a new species response created from the species object
        return response;
    }

    public static MainSpeciesListResponse convertLocalSpeciesList(ArrayList<Species> species){

        // Create new response to return
        MainSpeciesListResponse returnList = new MainSpeciesListResponse();

        // If species list is null
        if (species == null) {

            // Return the empty list
            return returnList;
        }

        ArrayList<MainSpeciesResponse> listOfSpecies = new ArrayList<>();

        // Else, for each species within the list
        for (Species speciesInst : species){

            // Create a new species response
            MainSpeciesResponse response = convertLocalSpecies(speciesInst);

            // Add it to the return list
            listOfSpecies.add(response);
        }

        returnList.setSpecies(listOfSpecies);

        // Return populated list
        return returnList;
    }

    //endregion Species

    //region Animals

    private static MainAnimalResponse convertLocalAnimal(Animal animal){

        // If animal is null, return null
        if (animal == null){

            return null;
        }

        MainSpeciesResponse species = convertLocalSpecies(animal.getSpecies());

        MainAnimalResponse response = new MainAnimalResponse();
        response.setId(animal.getId());
        response.setName(animal.getName());
        response.setSpecies(species);
        response.setDescription(animal.getDescription());
        response.setIsAvailable(animal.isAvailable());

        // Return a new animal response created from the animal object
        return response;
    }

    //endregion Animals

    //region Areas

    //region Amenities

    private static MainAmenityResponse convertLocalAmenity(Amenity amenity){

        // If amenity is null, return null
        if (amenity == null){

            return null;
        }

        MainAmenityResponse response = new MainAmenityResponse();
        response.setId(amenity.getId());
        response.setLabel(amenity.getLabel());
        response.setVisitorDestination(amenity.getVisitorDestination());
        response.setCoordinates(amenity.getCoordinates());
        response.setAmenityType(amenity.getAmenityType());
        response.setImage(amenity.getImageUrl());

        // Return a new amenity response created from the amenity object
        return response;
    }

    //endregion Amenities

    //region Enclosures

    private static MainEnclosureResponse convertLocalEnclosure(Enclosure enclosure){

        // If enclosure is null, return null
        if (enclosure == null){

            return null;
        }

        ArrayList<MainAnimalResponse> animals = new ArrayList<>();

        for (Animal animal : enclosure.getAnimals()){

            MainAnimalResponse response = convertLocalAnimal(animal);

            animals.add(response);
        }

        MainEnclosureResponse response = new MainEnclosureResponse();
        response.setId(enclosure.getId());
        response.setLabel(enclosure.getLabel());
        response.setVisitorDestination(enclosure.getVisitorDestination());
        response.setCoordinates(enclosure.getCoordinates());
        response.setAnimals(animals);
        response.setImage(enclosure.getImageUrl());

        // Return a new amenity response created from the amenity object
        return response;
    }

    //endregion Enclosures

    //endregion Areas

    //region Keepers

    private static MainKeeperResponse convertLocalKeeper(Keeper keeper){

        // If keeper is null, return null
        if (keeper == null){

            return null;
        }

        MainKeeperResponse response = new MainKeeperResponse();
        response.setId(keeper.getId());
        response.setName(keeper.getName());
        response.setBio(keeper.getBio());

        // Return a new keeper response created from the keeper object
        return response;
    }

    //endregion Keepers

    //region Events

    private static MainEventResponse convertLocalEvent(Event event){

        // If event is null, return null
        if (event == null){

            return null;
        }

        MainAmenityResponse amenity = convertLocalAmenity((Amenity) event.getLocation());

        MainEventResponse response = new MainEventResponse();
        response.setId(event.getId());
        response.setLabel(event.getLabel());
        response.setDescription(event.getDescription());
        response.setLocation(amenity);
        response.setStartTime(event.getStartTime());
        response.setEndTime(event.getEndTime());
        response.setIsActive(event.isActive());
        response.setImage(event.getImageUrl());

        // Return a new event response created from the event object
        return response;
    }

    private static MainFeedingResponse convertLocalFeeding(Feeding feeding){

        // If feeding is null, return null
        if (feeding == null){

            return null;
        }

        MainEnclosureResponse enclosure = convertLocalEnclosure((Enclosure) feeding.getLocation());
        MainKeeperResponse keeper = convertLocalKeeper(feeding.getKeeper());

        MainFeedingResponse response = new MainFeedingResponse();
        response.setId(feeding.getId());
        response.setLabel(feeding.getLabel());
        response.setDescription(feeding.getDescription());
        response.setLocation(enclosure);
        response.setStartTime(feeding.getStartTime());
        response.setEndTime(feeding.getEndTime());
        response.setIsActive(feeding.isActive());
        response.setKeeper(keeper);
        response.setImage(feeding.getImageUrl());

        // Return a new feeding response created from the feeding object
        return response;
    }

    public static MainEventListResponse convertLocalEventList(ArrayList<Event> events){

        // Create new response to return
        MainEventListResponse returnList = new MainEventListResponse();

        // If event list is null
        if (events == null) {

            // Return the empty list
            return returnList;
        }

        ArrayList<MainEventResponse> listOfEvents = new ArrayList<>();
        ArrayList<MainFeedingResponse> listOfFeedings = new ArrayList<>();

        // Else, for each event within the list
        for (Event event : events){

            if (event instanceof Feeding){

                // Create a new feeding response
                MainFeedingResponse response = convertLocalFeeding((Feeding) event);

                // Add it to the return list
                listOfFeedings.add(response);

            } else {

                // Create a new event response
                MainEventResponse response = convertLocalEvent(event);

                // Add it to the return list
                listOfEvents.add(response);
            }
        }

        returnList.setEvents(listOfEvents);
        returnList.setFeedings(listOfFeedings);

        // Return populated list
        return returnList;
    }

    //endregion Events

    //endregion To JSON

    //region From JSON

    //region Species

    private static Species convertSpeciesResponse(MainSpeciesResponse response){

        // If response is null, return null
        if (response == null){

            return null;
        }


        // Return a new species object created from the species response
        return new Species(response.getId(), response.getCommonName(), response.getLatin(), response.getDescription(), response.getImage());
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
        return new Amenity(response.getId(), response.getLabel(), response.getVisitorDestination(), coordinates, response.getImage(), response.getDescription(), response.getAmenityType());
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
        return new Enclosure(response.getId(), response.getLabel(), response.getVisitorDestination(), coordinates, response.getImage(), animals);
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
        return new Event(response.getId(), response.getLabel(), response.getDescription(), location, response.getStartTime(), response.getEndTime(), response.getIsActive(), response.getImage());

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
        return new Feeding(response.getId(), response.getLabel(), response.getDescription(), location, response.getStartTime(), response.getEndTime(), response.getIsActive(), keeper, response.getImage());
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

    //endregion From JSON

    //endregion Methods
}
